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

package dev.azn9.plugins.discord.settings

import dev.azn9.plugins.discord.diagnose.DiagnoseService
import dev.azn9.plugins.discord.diagnose.diagnoseService
import dev.azn9.plugins.discord.render.renderService
import dev.azn9.plugins.discord.time.timeService
import dev.azn9.plugins.discord.utils.createErrorMessage
import com.intellij.openapi.options.SearchableConfigurable
import dev.azn9.plugins.discord.rpc.rpcService
import kotlinx.coroutines.future.asCompletableFuture
import org.jetbrains.concurrency.createError
import javax.swing.BoxLayout
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.SwingUtilities

class ApplicationConfigurable : SearchableConfigurable {
    override fun getId() = "discord-application"

    override fun isModified(): Boolean = settings.isModified

    override fun getDisplayName() = "Discord Integration Application Settings"

    override fun apply() {
        settings.apply()

        timeService.load()

        renderService.render()
    }

    override fun reset() {
        settings.reset()
    }

    override fun createComponent() = JPanel().apply panel@{
        layout = BoxLayout(this@panel, BoxLayout.Y_AXIS)

        val service = diagnoseService
        var errorComponent: JComponent? = null
        var buildRunnable: Runnable? = null

        buildRunnable = Runnable {
            service.discord.asCompletableFuture().thenAcceptAsync { discord ->
                if (discord != DiagnoseService.Discord.OTHER) {
                    SwingUtilities.invokeLater {
                        val component = createErrorMessage(discord.message) {
                            SwingUtilities.invokeLater {
                                remove(errorComponent)
                                service.restartDetection()
                                buildRunnable!!.run()
                                revalidate()
                            }
                            rpcService.update(null)
                        }
                        errorComponent = component
                        add(component, 0)
                    }
                }
            }
        }
        buildRunnable.run()

        service.plugins.asCompletableFuture().thenAcceptAsync { plugins ->
            if (plugins != DiagnoseService.Plugins.NONE) {
                SwingUtilities.invokeLater { add(createErrorMessage(plugins.message), 0) }
            }
        }

        service.ide.asCompletableFuture().thenAcceptAsync { ide ->
            if (ide != DiagnoseService.Ide.OTHER) {
                SwingUtilities.invokeLater { add(createErrorMessage(ide.message), 0) }
            }
        }

        add(settings.component)
    }

    override fun getHelpTopic(): String? = null
}
