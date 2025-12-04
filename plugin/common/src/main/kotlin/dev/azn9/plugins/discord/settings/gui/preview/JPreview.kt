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

package dev.azn9.plugins.discord.settings.gui.preview

import dev.azn9.plugins.discord.render.Renderer
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.awt.event.HierarchyEvent
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.Timer
import javax.swing.SwingUtilities

class JPreview : JLabel() {

    private val preview = PreviewRenderer()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val worker = Timer(1000) {
        scope.launch(Dispatchers.Default) {
            update()
        }
    }
    private val mutex = Mutex()

    var type: Renderer.Type.Application = Renderer.Type.Application
        set(value) {
            field = value
            scope.launch { update() }
        }

    init {
        icon = ImageIcon(preview.dummy)

        scope.launch { update(true) }
        worker.start()

        addHierarchyListener { e ->
            if (e.hasChangeFlag(HierarchyEvent.SHOWING_CHANGED)) {
                if (isShowing) {
                    worker.start()
                } else {
                    worker.stop()
                }
            }
        }
    }

    suspend fun update(force: Boolean = false) {
        mutex.withLock {
            if (isShowing) {
                val (modified, image) = preview.draw(type, force)

                if (modified) {
                    SwingUtilities.invokeLater {
                        icon = ImageIcon(image)
                    }
                }
            }
        }
    }
}

private fun HierarchyEvent.hasChangeFlag(flag: Int) = (changeFlags and flag.toLong()) != 0L
