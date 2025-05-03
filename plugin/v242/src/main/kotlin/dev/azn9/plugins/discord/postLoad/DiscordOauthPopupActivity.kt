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

package dev.azn9.plugins.discord.postLoad

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import dev.azn9.plugins.discord.DiscordPlugin
import dev.azn9.plugins.discord.oauth.oauthService
import dev.azn9.plugins.discord.settings.settings
import dev.azn9.plugins.discord.utils.DisposableCoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class DiscordOauthPopupActivity : ProjectActivity, DisposableCoroutineScope {
    override val parentJob: Job = SupervisorJob()

    private suspend fun showPopup() {

    }

    private suspend fun validateDiscordToken() {
        if (!oauthService.validateDiscordToken()) {
            showPopup()
        }
    }

    override suspend fun execute(project: Project) {
        launch {
            if (DiscordPlugin.isAlmightyAlpacasPluginPresent()) {
                return@launch
            }

            validateDiscordToken()
        }
    }

}
