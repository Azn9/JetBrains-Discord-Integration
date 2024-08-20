/*
 * Copyright 2017-2020 Aljoscha Grebe
 * Copyright 2023-2024 Axel JOLY (Azn9) <contact@azn9.dev>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.azn9.plugins.discord.data

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ex.ApplicationInfoEx
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.fileEditor.UniqueVFilePathBuilder
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessModuleDir
import com.intellij.openapi.wm.IdeFocusManager

import com.intellij.serviceContainer.AlreadyDisposedException
import com.intellij.xdebugger.XDebuggerManager
import dev.azn9.plugins.discord.DiscordPlugin
import dev.azn9.plugins.discord.extensions.VcsInfoExtension
import dev.azn9.plugins.discord.render.Renderer
import dev.azn9.plugins.discord.settings.settings
import dev.azn9.plugins.discord.settings.values.ApplicationType
import dev.azn9.plugins.discord.settings.values.IdleVisibility.*
import dev.azn9.plugins.discord.settings.values.ProjectShow
import dev.azn9.plugins.discord.source.sourceService
import dev.azn9.plugins.discord.time.timeActive
import dev.azn9.plugins.discord.time.timeOpened
import dev.azn9.plugins.discord.time.timeService
import dev.azn9.plugins.discord.utils.*

val dataService: DataService
    get() = service()

@Service
class DataService {
    suspend fun getData(mode: Renderer.Mode): Data? = tryOrNull {
        mode.runCatching { getData() }
            .onFailure { e ->
                if (e is AlreadyDisposedException) {
                    return@onFailure
                }

                DiscordPlugin.LOG.warnLazy(e) { "Failed to get data" }
            }
            .getOrNull()
    }

    @JvmName("getDataInternal")
    private suspend fun (Renderer.Mode).getData(): Data {
        DiscordPlugin.LOG.debug("Getting data")

        val applicationSettings = settings

        val application = ApplicationManager.getApplication()
        val applicationInfo = ApplicationInfoEx.getInstance()
        val applicationCode = ApplicationType.IDE_EDITION.applicationName
        val applicationName = settings.applicationType.getValue().applicationNameReadable
        val applicationVersion = applicationInfo.fullVersion
        val applicationTimeOpened = application.timeOpened
        val applicationTimeActive = application.timeActive

        val project: Project? = IdeFocusManager.getGlobalInstance().lastFocusedFrame?.project

        val editor: FileEditor? = project?.let {
            invokeOnEventThread {
                runCatching {
                    FileEditorManager.getInstance(project)?.selectedEditor
                }.onFailure {
                    DiscordPlugin.LOG.warnLazy(it) { "Failed to get selected editor" }
                }.getOrNull()
            }
        }

        val projectSettings = project?.settings
        val applicationId = projectSettings?.customApplicationId?.getValue().let {
            val longId = it?.trim()?.toLongOrNull()
            if (longId != null && longId > 0) {
                longId
            } else {
                null
            }
        } ?: let {
            val applicationsData = sourceService.source.getApplicationsOrNull() ?: let {
                DiscordPlugin.LOG.warn("No applications data found!")
                return Data.None
            }
            val currentApplicationData = applicationsData[applicationCode] ?: let {
                DiscordPlugin.LOG.warn("No data found for application code $applicationCode!")
                return Data.None
            }

            currentApplicationData.discordId
        }

        if (!settings.show.getStoredValue()) {
            return Data.None
        } else if (timeService.idle) {
            when (settings.idle.getStoredValue()) {
                IGNORE -> Unit
                IDLE -> {
                    val idleTimestamp = System.currentTimeMillis() - application.idleTime
                    return Data.Idle(
                        idleTimestamp,
                        applicationId,
                        applicationVersion
                    )
                }

                HIDE -> return Data.None
            }
        }

        if (project != null) {
            if (project.settings.show.getValue() <= ProjectShow.DISABLE) {
                return Data.None
            } else if (!project.isDefault && project.settings.show.getValue() >= ProjectShow.PROJECT) {
                val projectName = project.name
                val projectDescription = project.settings.description.getValue()
                val projectTimeOpened = project.timeOpened
                val projectTimeActive = project.timeActive
                val debuggerActive: Boolean = XDebuggerManager.getInstance(project).currentSession != null

                if (editor != null) {
                    val file = editor.file

                    /*if (editor is Editor && file != null) {
                        val psiFile = PsiManager.getInstance(project).findFile(file);

                        if (psiFile != null) {
                            val document = editor.document
                            var offset = editor.caretModel.offset

                            offset -= document.text.substring(0, offset).length

                            val element = psiFile.findElementAt(offset)

                            val currentMethodPsi = PsiTreeUtil.getParentOfType(element, )
                            val currentClassPsi = PsiTreeUtil.getParentOfType(element, )
                        }
                    }*/

                    if (file != null
                        && project.settings.show.getValue() >= ProjectShow.PROJECT_FILES
                        && !(settings.fileHideVcsIgnored.getValue() && isVcsIgnored(project, file))
                    ) {
                        val fileName = file.name
                        val fileUniqueName = when (DumbService.isDumb(project)) {
                            true -> fileName
                            false -> invokeReadAction {
                                tryOrDefault(fileName, false) {
                                    UniqueVFilePathBuilder.getInstance().getUniqueVirtualFilePath(project, file)
                                }
                            }
                        }

                        val fileTimeOpened = file.timeOpened
                        val fileTimeActive = file.timeActive
                        val filePath = file.path
                        val fileIsWriteable = file.isWritable

                        val editorIsTextEditor: Boolean
                        val caretLine: Int
                        val lineCount: Int
                        val fileSize: Int

                        if (editor is TextEditor) {
                            editorIsTextEditor = true
                            caretLine = editor.editor.caretModel.primaryCaret.logicalPosition.line + 1
                            lineCount = editor.editor.document.lineCount
                            fileSize = editor.editor.document.textLength
                        } else {
                            editorIsTextEditor = false
                            caretLine = 0
                            lineCount = 0
                            fileSize = 0
                        }

                        data class ModuleData(val moduleName: String?, val pathInModule: String)

                        val moduleData = runReadAction action@{
                            val module = ModuleUtil.findModuleForFile(file, project)
                            val moduleName = module?.name
                            val moduleDirPath = module?.guessModuleDir()
                            val pathInModule = if (moduleDirPath != null) file.path.removePrefix(moduleDirPath.path) else ""
                            return@action ModuleData(moduleName, pathInModule)
                        }

                        val vcsBranch = VcsInfoExtension.getCurrentVcsBranch(project, file)

                        DiscordPlugin.LOG.debug("Returning file data")

                        return Data.File(
                            applicationId,
                            applicationName,
                            applicationVersion,
                            applicationTimeOpened,
                            applicationTimeActive,
                            applicationSettings,
                            projectName,
                            projectDescription,
                            projectTimeOpened,
                            projectTimeActive,
                            projectSettings!!,
                            vcsBranch,
                            debuggerActive,
                            fileName,
                            fileUniqueName,
                            fileTimeOpened,
                            fileTimeActive,
                            filePath,
                            fileIsWriteable,
                            editorIsTextEditor,
                            caretLine,
                            lineCount,
                            moduleData.moduleName,
                            moduleData.pathInModule,
                            fileSize
                        )
                    }
                }

                val vcsBranch = VcsInfoExtension.getCurrentVcsBranch(project, null)

                DiscordPlugin.LOG.debug("Returning project data")

                return Data.Project(
                    applicationId,
                    applicationName,
                    applicationVersion,
                    applicationTimeOpened,
                    applicationTimeActive,
                    applicationSettings,
                    projectName,
                    projectDescription,
                    projectTimeOpened,
                    projectTimeActive,
                    projectSettings!!,
                    vcsBranch,
                    debuggerActive
                )
            }
        }

        DiscordPlugin.LOG.debug("Returning application data")

        return Data.Application(
            applicationId,
            applicationName,
            applicationVersion,
            applicationTimeOpened,
            applicationTimeActive,
            applicationSettings
        )
    }
}
