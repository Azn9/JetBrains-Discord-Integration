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

package dev.azn9.plugins.discord.settings.gui.themes

import com.intellij.openapi.ui.DialogWrapper
import dev.azn9.plugins.discord.icons.source.Theme
import dev.azn9.plugins.discord.settings.options.types.CustomThemeOption
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import java.awt.Component
import javax.swing.*
import javax.swing.plaf.basic.BasicComboBoxRenderer
import kotlin.coroutines.CoroutineContext

class ThemeDialog(private val themes: Map<String, Theme>, private val initialValue: String?, private val showDefault: Boolean = false) : DialogWrapper(null, true, IdeModalityType.IDE),
    CoroutineScope {
    private val parentJob: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + parentJob

    private lateinit var tabs: JTabbedPane
    private lateinit var classicThemesField: JComboBox<Theme>
    private lateinit var customThemeField: CustomThemeOption

    val value: String
        get() = if (tabs.selectedIndex == 1) {
            val value = customThemeField.componentValue.template
            value.ifEmpty {
                Theme.Default.id
            }
        } else {
            (classicThemesField.selectedItem as Theme).id
        }

    /*
     * TODO
     * Optional: Override the getPreferredFocusedComponent() method and return the component that should be focused when the dialog is first displayed.
     * Optional: Override the getDimensionServiceKey() method to return the identifier which will be used for persisting the dialog dimensions.
     * Optional: Override the getHelpId() method to return the context help topic associated with the dialog.
     */

    init {
        init()

        title = "Themes"
    }

    override fun createCenterPanel(): JComponent = JPanel().apply panel@{
        tabs = JTabbedPane().apply {
            val isCustom = initialValue?.startsWith("http") ?: false

            val classicTab = JPanel().apply tab@{
                val renderer = object : BasicComboBoxRenderer() {
                    override fun getListCellRendererComponent(list: JList<*>?, value: Any?, index: Int, isSelected: Boolean, cellHasFocus: Boolean): Component {
                        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)

                        val theme = value as Theme
                        text = "<html><b>${theme.name}</b><br>${theme.description}</html>"

                        return this
                    }
                }

                var themeValues = themes.values.toTypedArray()
                if (showDefault) {
                    themeValues += Theme.Default
                }

                classicThemesField = JComboBox(themeValues).apply box@{
                    this@box.renderer = renderer
                    selectedItem = themes[initialValue] ?: (if (showDefault) Theme.Default else themes.values.first())
                }

                add(classicThemesField)
            }
            addTab("Classic themes", classicTab)

            val customTab = JPanel().apply tab@{
                customThemeField = CustomThemeOption("Custom theme", "", initialValue ?: "")
                add(customThemeField.component)
            }
            addTab("Custom theme", customTab)

            selectedIndex = if (isCustom) 1 else 0;
        }

        add(tabs)
    }
}
