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
import dev.azn9.plugins.discord.settings.ApplicationSettings
import dev.azn9.plugins.discord.settings.options.impl.PersistentStateOptionHolderImpl
import dev.azn9.plugins.discord.settings.options.types.*
import dev.azn9.plugins.discord.settings.values.*

@Suppress("unused")
@State(name = "DiscordApplicationSettings", storages = [Storage("discord.xml")])
class ApplicationSettingsImpl : ApplicationSettings, PersistentStateOptionHolderImpl() {
    override val show by check("Enable Rich Presence", true)

    /* ========== Timeout / Idle ========== */

    private val timeoutOptionPair by pair()
    override val timeoutMinutes by timeoutOptionPair.first.spinner(
        "Time required before considered idle",
        "Time without any activity before the plugin marks the session as idle. Changes mighty require a restart to take effect",
        5,
        1 until 24 * 60,
        format = "# " + "Minutes"
    )
    override val timeoutResetTimeEnabled by timeoutOptionPair.second.check("Reset open time when returning", "Reset open time for the application as well as open projects and files", true)

    override val idle by selection(text = "While idle", "While the session is marked as idle", initialValue = IdleVisibility.IDLE)

    /* ========== Layout ========== */

    private val layoutGroup by group("Layout")
    private val preview by layoutGroup.preview()
    private val tabs by preview.tabbed()

    /* ---------- Application Tab ---------- */

    private val applicationTab = tabs["Application"]
    private val applicationInfo by applicationTab.info("Visible when no project is open")

    private val applicationDetailsToggle by applicationTab.toggleable<PresenceText>()
    override val applicationDetails by applicationDetailsToggle.toggle { it == PresenceText.CUSTOM }.selection("First line", PresenceText.Application1)
    override val applicationDetailsCustom by applicationDetailsToggle.option.template("Custom", "")

    private val applicationStateToggle by applicationTab.toggleable<PresenceText>()
    override val applicationState by applicationStateToggle.toggle { it == PresenceText.CUSTOM }.selection("Second line", PresenceText.Application2)
    override val applicationStateCustom by applicationStateToggle.option.template("Custom", "")

    private val applicationIconLargeToggle by applicationTab.toggleable<PresenceIcon>()
    override val applicationIconLarge by applicationIconLargeToggle.enableOn(PresenceIcon.CUSTOM).selection("Large icon", PresenceIcon.Large.Application)
    override val applicationIconLargeCustom by applicationIconLargeToggle.option.template("Custom", "")

    private val applicationIconLargeTextToggle by applicationTab.toggleable<PresenceText>()
    override val applicationIconLargeText by applicationIconLargeTextToggle.enableOn(PresenceText.CUSTOM).selection("Large icon text", PresenceText.ApplicationIconLarge)
    override val applicationIconLargeTextCustom by applicationIconLargeTextToggle.option.template("Custom", "")

    private val applicationIconSmallToggle by applicationTab.toggleable<PresenceIcon>()
    override val applicationIconSmall by applicationIconSmallToggle.enableOn(PresenceIcon.CUSTOM).selection("Small icon", PresenceIcon.Large.Application)
    override val applicationIconSmallCustom by applicationIconSmallToggle.option.template("Custom", "")

    private val applicationIconSmallTextToggle by applicationTab.toggleable<PresenceText>()
    override val applicationIconSmallText by applicationIconSmallTextToggle.enableOn(PresenceText.CUSTOM).selection("Small icon text", PresenceText.ApplicationIconSmall)
    override val applicationIconSmallTextCustom by applicationIconSmallTextToggle.option.template("Custom", "")

    override val applicationTime by applicationTab.selection("Show elapsed time", PresenceTime.Application)

    /* ---------- Project Tab ---------- */

    private val projectTab = tabs["Project"]
    private val projectInfo by projectTab.info("Visible when a project is open but no editor")

    private val projectDetailsToggle by projectTab.toggleable<PresenceText>()
    override val projectDetails by projectDetailsToggle.enableOn(PresenceText.CUSTOM).selection("First line", PresenceText.Project1)
    override val projectDetailsCustom by projectDetailsToggle.option.template("Custom", "")

    private val projectStateToggle by projectTab.toggleable<PresenceText>()
    override val projectState by projectStateToggle.enableOn(PresenceText.CUSTOM).selection("Second line", PresenceText.Project2)
    override val projectStateCustom by projectStateToggle.option.template("Custom", "")

    private val projectIconLargeToggle by projectTab.toggleable<PresenceIcon>()
    override val projectIconLarge by projectIconLargeToggle.enableOn(PresenceIcon.CUSTOM).selection("Large icon", PresenceIcon.Large.Project)
    override val projectIconLargeCustom by projectIconLargeToggle.option.template("Custom", "")

    private val projectIconLargeTextToggle by projectTab.toggleable<PresenceText>()
    override val projectIconLargeText by projectIconLargeTextToggle.enableOn(PresenceText.CUSTOM).selection("Large icon text", PresenceText.ProjectIconLarge)
    override val projectIconLargeTextCustom by projectIconLargeTextToggle.option.template("Custom", "")

    private val projectIconSmallToggle by projectTab.toggleable<PresenceIcon>()
    override val projectIconSmall by projectIconSmallToggle.enableOn(PresenceIcon.CUSTOM).selection("Small icon", PresenceIcon.Large.Project)
    override val projectIconSmallCustom by projectIconSmallToggle.option.template("Custom", "")

    private val projectIconSmallTextToggle by projectTab.toggleable<PresenceText>()
    override val projectIconSmallText by projectIconSmallTextToggle.enableOn(PresenceText.CUSTOM).selection("Small icon text", PresenceText.ProjectIconSmall)
    override val projectIconSmallTextCustom by projectIconSmallTextToggle.option.template("Custom", "")

    override val projectTime by projectTab.selection("Show elapsed time", PresenceTime.Project)

    /* ---------- File Tab ---------- */

    private val fileTab = tabs["File"]
    private val fileInfo by fileTab.info("Visible when a file is open in an editor")

    private val fileDetailsToggle by fileTab.toggleable<PresenceText>()
    override val fileDetails by fileDetailsToggle.enableOn(PresenceText.CUSTOM).selection("First line", PresenceText.File1)
    override val fileDetailsCustom by fileDetailsToggle.option.template("Custom", "")

    private val fileStateToggle by fileTab.toggleable<PresenceText>()
    override val fileState by fileStateToggle.enableOn(PresenceText.CUSTOM).selection("Second line", PresenceText.File2)
    override val fileStateCustom by fileStateToggle.option.template("Custom", "")

    private val fileIconLargeToggle by fileTab.toggleable<PresenceIcon>()
    override val fileIconLarge by fileIconLargeToggle.enableOn(PresenceIcon.CUSTOM).selection("Large icon", PresenceIcon.Large.File)
    override val fileIconLargeCustom by fileIconLargeToggle.option.template("Custom", "")

    private val fileIconLargeTextToggle by fileTab.toggleable<PresenceText>()
    override val fileIconLargeText by fileIconLargeTextToggle.enableOn(PresenceText.CUSTOM).selection("Large icon text", PresenceText.FileIconLarge)
    override val fileIconLargeTextCustom by fileIconLargeTextToggle.option.template("Custom", "")

    private val fileIconSmallToggle by fileTab.toggleable<PresenceIcon>()
    override val fileIconSmall by fileIconSmallToggle.enableOn(PresenceIcon.CUSTOM).selection("Small icon", PresenceIcon.Small.File)
    override val fileIconSmallCustom by fileIconSmallToggle.option.template("Custom", "")

    private val fileIconSmallTextToggle by fileTab.toggleable<PresenceText>()
    override val fileIconSmallText by fileIconSmallTextToggle.enableOn(PresenceText.CUSTOM).selection("Small icon text", PresenceText.FileIconSmall)
    override val fileIconSmallTextCustom by fileIconSmallTextToggle.option.template("Custom", "")

    override val fileTime by fileTab.selection("Show elapsed time", PresenceTime.File)

    override val filePrefixEnabled by fileTab.check("Prefix files names with Reading/Editing", true)

    override val fileHideVcsIgnored by fileTab.check("Hide VCS ignored files", "E.g. files in your .gitignore", false)

    /* ========== General Settings ========== */

    override val applicationType by selection("Application name", ApplicationType.IDE_EDITION)

    override val applicationTheme by themeChooser("Theme for the application icon", null, false, ThemeType.APPLICATION_ONLY)
    override val iconsTheme by themeChooser("Theme for the language icons", null, false, ThemeType.LANGUAGE_ONLY)

    /* ---------- Hidden Settings ---------- */

    private val hidden by hidden()

    override val applicationLastUpdateNotification by hidden.text("<unused>", "")
}
