/*
 * Copyright 2023-2025 Axel JOLY (Azn9) <contact@azn9.dev>
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

package dev.azn9.plugins.discord.toolWindow

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import dev.azn9.plugins.discord.oauth.oauthService
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JPanel

class DiscordConnectionToolWindow(toolWindow: ToolWindow) {

    companion object {
        const val discordIconPath: String = "/toolWindow/Discord-icon.png"
    }

    val contentPanel = JPanel()

    init {
        contentPanel.layout = BorderLayout(0, 20)
        contentPanel.border = BorderFactory.createEmptyBorder(20, 20, 20, 20)
        val loginButton = JButton("Login to Discord")
        loginButton.addActionListener { oauthService.startLogin() }
        contentPanel.add(loginButton)
    }

}

class DiscordConnectionToolWindowFactory : ToolWindowFactory, DumbAware {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val toolWindowContent = DiscordConnectionToolWindow(toolWindow)
        val content: Content = ContentFactory.getInstance()
            .createContent(toolWindowContent.contentPanel, "Discord Connection", false)
        toolWindow.contentManager.addContent(content)
    }

    override fun isApplicable(project: Project): Boolean {
        return oauthService.needsToDisplayToolWindow()
    }
}
