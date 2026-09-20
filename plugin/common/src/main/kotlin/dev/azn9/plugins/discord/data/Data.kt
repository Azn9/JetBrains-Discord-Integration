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

import com.intellij.openapi.util.NlsSafe
import dev.azn9.plugins.discord.icons.matcher.Matcher
import dev.azn9.plugins.discord.icons.utils.toSet
import dev.azn9.plugins.discord.settings.ApplicationSettings
import dev.azn9.plugins.discord.settings.ProjectSettings
import dev.azn9.plugins.discord.utils.find
import org.apache.commons.io.FilenameUtils

sealed class Data {
    open class None private constructor() : Data() {
        companion object : None()

        override fun toString(): String = "Data.None"
    }

    open class Idle(
        val idleTimestamp: Long,
        val applicationId: Long,
        val applicationVersion: String
    ) : Data() {
        override fun toString(): String {
            return "Data.Idle(idleTimestamp=$idleTimestamp)"
        }
    }

    open class Application(
        val applicationId: Long,
        val applicationName: String,
        val applicationVersion: String,
        val applicationTimeOpened: Long,
        val applicationTimeActive: Long,
        val applicationSettings: ApplicationSettings
    ) : Data() {
        override fun toString(): String {
            return "Data.Application(applicationVersion='$applicationVersion', applicationTimeOpened=$applicationTimeOpened, applicationTimeActive=$applicationTimeActive, applicationSettings=$applicationSettings)"
        }
    }

    open class Project(
        applicationId: Long,
        applicationName: String,
        applicationVersion: String,
        applicationTimeOpened: Long,
        applicationTimeActive: Long,
        applicationSettings: ApplicationSettings,
        val projectName: String,
        val projectDescription: String,
        val projectTimeOpened: Long,
        val projectTimeActive: Long,
        val projectSettings: ProjectSettings,
        val vcsBranch: String?,
        val debuggerActive: Boolean
    ) : Application(applicationId, applicationName, applicationVersion, applicationTimeOpened, applicationTimeActive, applicationSettings) {
        override fun toString(): String {
            return "Data.Project(applicationName='$applicationName', applicationVersion='$applicationVersion', applicationTimeOpened=$applicationTimeOpened, applicationTimeActive=$applicationTimeActive, projectName='$projectName', projectDescription='$projectDescription', projectTimeOpened=$projectTimeOpened, projectTimeActive=$projectTimeActive, vcsBranch=$vcsBranch)"
        }
    }

    open class File(
        applicationId: Long,
        applicationName: String,
        applicationVersion: String,
        applicationTimeOpened: Long,
        applicationTimeActive: Long,
        applicationSettings: ApplicationSettings,
        projectName: String,
        projectDescription: String,
        projectTimeOpened: Long,
        projectTimeActive: Long,
        projectSettings: ProjectSettings,
        vcsBranch: String?,
        debuggerActive: Boolean,
        val fileName: String,
        val fileNameUnique: String,
        val fileTimeOpened: Long,
        val fileTimeActive: Long,
        val filePath: String,
        val fileIsWriteable: Boolean,
        val editorIsTextEditor: Boolean,
        val isDiffEditor: Boolean,
        val caretLine: Int,
        val lineCount: Int,
        val moduleName: String?,
        val pathInModule: String?,
        val fileSize: Int
    ) : Project(
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
        projectSettings,
        vcsBranch,
        debuggerActive
    ), Matcher.Target.Provider {
        /** Path relative to the project directory */
        private val filePathRelative: String by lazy {
            FilenameUtils.separatorsToUnix(filePath)
        }

        private val fileBaseNames: Collection<String> by lazy {
            fileName.find('.')
                .mapToObj { i -> fileName.substring(0, i) }
                .toSet()
        }

        private val fileExtensions: Collection<String> by lazy {
            fileName.find('.')
                .mapToObj { i -> fileName.substring(i) }
                .toSet()
        }

        override fun getField(target: Matcher.Target) = when (target) {
            Matcher.Target.EXTENSION -> fileExtensions
            Matcher.Target.NAME -> listOf(fileName)
            Matcher.Target.BASENAME -> fileBaseNames
            Matcher.Target.PATH -> listOf(filePathRelative)
        }

        override fun toString(): String {
            return "Data.File(applicationName='$applicationName', applicationVersion='$applicationVersion', applicationTimeOpened=$applicationTimeOpened, applicationTimeActive=$applicationTimeActive, projectName='$projectName', projectDescription='$projectDescription', projectTimeOpened=$projectTimeOpened, projectTimeActive=$projectTimeActive, vcsBranch=$vcsBranch, fileName='$fileName', fileNameUnique='$fileNameUnique', fileTimeOpened=$fileTimeOpened, fileTimeActive=$fileTimeActive, filePath='$filePath', fileIsWriteable=$fileIsWriteable, editorIsTextEditor=$editorIsTextEditor, isDiffEditor=$isDiffEditor, caretLine=$caretLine, lineCount=$lineCount, moduleName=$moduleName, pathInModule=$pathInModule, fileSize=$fileSize)"
        }
    }
}
