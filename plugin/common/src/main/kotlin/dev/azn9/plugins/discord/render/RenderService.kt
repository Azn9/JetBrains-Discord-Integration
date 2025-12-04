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

package dev.azn9.plugins.discord.render

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.util.concurrency.AppExecutorUtil
import dev.azn9.plugins.discord.DiscordPlugin
import dev.azn9.plugins.discord.data.dataService
import dev.azn9.plugins.discord.rpc.rpcService
import dev.azn9.plugins.discord.source.sourceService
import dev.azn9.plugins.discord.utils.DisposableCoroutineScope
import dev.azn9.plugins.discord.utils.debugLazy
import dev.azn9.plugins.discord.utils.scheduleWithFixedDelay
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

val renderService: RenderService
    get() = service()

@Service
class RenderService : DisposableCoroutineScope {
    override val parentJob: Job = SupervisorJob()

    private var renderClockJob: ScheduledFuture<*>? = null
    private var renderJob: Job? = null

    @Synchronized
    fun render(force: Boolean = false) {
        renderJob?.let {
            DiscordPlugin.LOG.debugLazy { "Canceling previous render due to new request" }
            it.cancel()
        }

        renderJob = launch {
            DiscordPlugin.LOG.debugLazy { "Scheduling render, force=$force" }

            val data = dataService.getData(Renderer.Mode.NORMAL)

            if (data == null) {
                DiscordPlugin.LOG.debugLazy { "No data to render" }
                renderJob = null
                return@launch
            }

            val context = RenderContext(sourceService.source, data, Renderer.Mode.NORMAL)

            val renderer = context.createRenderer()
            val presence = renderer?.render()

            if (presence == null) {
                DiscordPlugin.LOG.debugLazy { "Render result: hidden" }
            } else {
                DiscordPlugin.LOG.debugLazy { "Render result: visible" }
            }

            runBlocking { rpcService.update(presence, force) }

            renderJob = null
        }
    }

    fun startRenderClock() {
        DiscordPlugin.LOG.debugLazy { "Starting render service" }

        if (renderClockJob != null && !renderClockJob!!.isCancelled && !renderClockJob!!.isDone) {
            DiscordPlugin.LOG.debugLazy { "Render clock job already running" }
            return
        }

        val executor = AppExecutorUtil.getAppScheduledExecutorService()

        this.renderClockJob = executor.scheduleWithFixedDelay(delay = 5, unit = TimeUnit.SECONDS) {
            try {
                render()
            } catch (e: CancellationException) {
                // Ignore coroutine cancellations (e.g., previous render cancelled)
            } catch (e: Exception) {
                DiscordPlugin.LOG.error("Error rendering presence", e)
            }
        }
    }

    override fun dispose() {
        DiscordPlugin.LOG.info("Disposing render service")
        renderClockJob?.cancel(true)
    }
}
