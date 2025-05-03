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


package dev.azn9.plugins.discord.rpc.connection

import dev.azn9.plugins.discord.rpc.RichPresence
import dev.azn9.plugins.discord.rpc.UserCallback
import dev.azn9.plugins.discord.utils.DisposableCoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob

class DiscordOauthConnection(override val appId: Long, private val userCallback: UserCallback) : DiscordConnection, DisposableCoroutineScope {
    override val parentJob: Job = SupervisorJob()

    override val running: Boolean
        get() = TODO("Not yet implemented")

    override suspend fun connect() {
        TODO("Not yet implemented")
    }

    override suspend fun clearActivity() {
        TODO("Not yet implemented")
    }

    override suspend fun disconnect() {
        TODO("Not yet implemented")
    }

    override suspend fun send(presence: RichPresence?) {
        TODO("Not yet implemented")
    }
}
