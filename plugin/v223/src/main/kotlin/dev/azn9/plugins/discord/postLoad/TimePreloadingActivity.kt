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

package dev.azn9.plugins.discord.postLoad

import dev.azn9.plugins.discord.time.timeService
import dev.azn9.plugins.discord.utils.DisposableCoroutineScope
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import dev.azn9.plugins.discord.DiscordPlugin
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class TimePreloadingActivity : StartupActivity.Background, StartupActivity.DumbAware, DisposableCoroutineScope {
    override val parentJob: Job = SupervisorJob()

    override fun runActivity(project: Project) {
        launch {
            if (DiscordPlugin.isAlmightyAlpacasPluginPresent()) {
                return@launch
            }

            timeService.load()
        }
    }

}
