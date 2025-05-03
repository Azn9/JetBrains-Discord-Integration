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

package dev.azn9.plugins.discord.settings.impl

import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.util.IncorrectOperationException
import dev.azn9.plugins.discord.settings.ProjectSettings
import dev.azn9.plugins.discord.settings.options.impl.PersistentStateOptionHolderImpl
import dev.azn9.plugins.discord.settings.options.types.*
import dev.azn9.plugins.discord.settings.settings
import dev.azn9.plugins.discord.settings.values.ProjectShow
import dev.azn9.plugins.discord.settings.values.ThemeType

@State(name = "DiscordProjectSettings", storages = [Storage("discord.xml")])
class ProjectSettingsImpl(override val project: Project) : ProjectSettings, PersistentStateOptionHolderImpl() {
    override val show by when (project.isDefault) {
        true -> selection("Project visibility", ProjectShow.ASK to ProjectShow.VALUES_DEFAULT)
        false -> selection(
            "Project visibility", try {
                ProjectManager.getInstance().defaultProject.settings.show.getStoredValue()
            } catch (e: IncorrectOperationException) { // Work around #246
                ProjectShow.ASK
            } to ProjectShow.VALUES
        )
    }

    private val nameOverrideToggle by toggleable<Boolean>()
    override val nameOverrideEnabled by nameOverrideToggle.toggle.check("Override project name", false)
    override val nameOverrideText by nameOverrideToggle.option.text("", "")

    override val description by text("Project description", "")

    override val applicationTheme by themeChooser("Theme for the application icon", null, true, ThemeType.APPLICATION_ONLY)
    override val iconsTheme by themeChooser("Theme for the language icons", null, true, ThemeType.LANGUAGE_ONLY)

    override val button1Title by text("Button 1 title", "")
    override val button1Url by text("Button 1 url", "")
    override val button2Title by text("Button 2 title", "")
    override val button2Url by text("Button 2 url", "")

    override val customApplicationId by text("Custom application ID", "")
}
