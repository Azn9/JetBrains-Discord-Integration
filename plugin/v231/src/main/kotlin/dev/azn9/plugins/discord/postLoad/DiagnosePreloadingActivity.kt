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

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import dev.azn9.plugins.discord.DiscordPlugin
import dev.azn9.plugins.discord.diagnose.DiagnoseService
import dev.azn9.plugins.discord.diagnose.diagnoseService
import dev.azn9.plugins.discord.settings.values.ApplicationType
import dev.azn9.plugins.discord.utils.DisposableCoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.future.asCompletableFuture
import kotlinx.coroutines.launch

class DiagnosePreloadingActivity : ProjectActivity, DisposableCoroutineScope {
    override val parentJob: Job = SupervisorJob()

    override suspend fun execute(project: Project) {
        launch {
            diagnose()
        }
    }

    private fun diagnose() {
        DiscordPlugin.LOG.info("App starting, diagnosing environment")

        DiscordPlugin.LOG.info("Application identifiers: ${ApplicationType.IDE.applicationName}, ${ApplicationType.IDE_EDITION.applicationName}")

        if (DiscordPlugin.isAlmightyAlpacasPluginPresent()) {
            NotificationGroupManager.getInstance()
                .getNotificationGroup("dev.azn9.plugins.discord.notification.error")
                .createNotification(
                    "Discord Integration V2",
                    "Detected plugin 'Discord Integration' by Almighty Alpaca. Please uninstall this old plugin as it will prevent this one from working!",
                    NotificationType.ERROR
                )
                .setImportant(true)
                .run(Notifications.Bus::notify)
            return
        }

        diagnoseService.discord.asCompletableFuture().thenAcceptAsync { discord ->
            if (discord == DiagnoseService.Discord.OTHER) {
                return@thenAcceptAsync
            }

            val notificationType = when (discord) {
                DiagnoseService.Discord.CLOSED, DiagnoseService.Discord.SNAP, DiagnoseService.Discord.BROWSER, DiagnoseService.Discord.ADMINISTRATOR -> NotificationType.ERROR
                else -> NotificationType.WARNING
            }

            NotificationGroupManager.getInstance()
                .getNotificationGroup("dev.azn9.plugins.discord.notification.error")
                .createNotification("Discord Integration V2", discord.message, notificationType)
                .setImportant(true)
                .run(Notifications.Bus::notify)
        }

        diagnoseService.plugins.asCompletableFuture().thenAcceptAsync { plugins ->
            if (plugins == DiagnoseService.Plugins.NONE) {
                return@thenAcceptAsync
            }

            NotificationGroupManager.getInstance()
                .getNotificationGroup("dev.azn9.plugins.discord.notification.error")
                .createNotification(
                    "Discord Integration V2",
                    plugins.message,
                    NotificationType.WARNING
                )
                .setImportant(true)
                .run(Notifications.Bus::notify)
        }

        diagnoseService.ide.asCompletableFuture().thenAcceptAsync { ide ->
            if (ide == DiagnoseService.Ide.OTHER) {
                return@thenAcceptAsync
            }

            NotificationGroupManager.getInstance()
                .getNotificationGroup("dev.azn9.plugins.discord.notification.error")
                .createNotification(
                    "Discord Integration V2",
                    ide.message,
                    NotificationType.WARNING
                )
                .setImportant(true)
                .run(Notifications.Bus::notify)
        }
    }
}
