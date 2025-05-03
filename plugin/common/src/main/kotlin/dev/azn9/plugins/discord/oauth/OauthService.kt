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

package dev.azn9.plugins.discord.oauth

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import dev.azn9.plugins.discord.DiscordPlugin
import dev.azn9.plugins.discord.rpc.User
import dev.azn9.plugins.discord.rpc.rpcService
import dev.azn9.plugins.discord.settings.ApplicationSettings
import dev.azn9.plugins.discord.settings.settings
import dev.azn9.plugins.discord.utils.DisposableCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

val oauthService: OauthService
    get() = service()

val appSettings: ApplicationSettings = settings

const val API_URL = "https://jdioauth.azn9.dev"
const val DISCORD_API_URL = "https://discord.com/api"

@Service
class OauthService : DisposableCoroutineScope {
    override val parentJob: Job = SupervisorJob()

    companion object {
        val httpClient = HttpClient.newBuilder().build()!!
        val gson: Gson = GsonBuilder().create()!!
    }

    override fun dispose() {

    }

    suspend fun validateDiscordToken(): Boolean {
        val discordToken = appSettings.discordOauthToken.getStoredValue()
        val refreshToken = appSettings.discordRefreshToken.getStoredValue()

        if (discordToken.isEmpty() || refreshToken.isEmpty()) {
            DiscordPlugin.LOG.info("Discord token and/or refresh token is null or empty")
            return false
        }

        val request = HttpRequest.newBuilder()
            .uri(URI.create("$DISCORD_API_URL/oauth2/@me"))
            .GET()
            .header("Accept", "application/json")
            .header("Authorization", "Bearer $discordToken")
            .build()

        val response = withContext(Dispatchers.IO) {
            httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        }

        if (response.statusCode() != 200) {
            DiscordPlugin.LOG.info("Response code: ${response.statusCode()} body: ${response.body()}")
            return false
        }

        val authorizationInformation: AuthorizationInformation
        try {
            val body = response.body()
            DiscordPlugin.LOG.info("Received $body")

            authorizationInformation = gson.fromJson(body, AuthorizationInformation::class.java)
        } catch (e: JsonSyntaxException) {
            return false
        }

        val oauthUser = authorizationInformation.user
            ?: return false // TODO: special case, missing scope

        rpcService.updateUser(
            User.Normal(
                oauthUser.globalName ?: oauthUser.username,
                oauthUser.discriminator ?: "0",
                oauthUser.id.toLong(),
                oauthUser.avatar
            )
        )

        DiscordPlugin.LOG.info("User ${oauthUser.username} was updated")

        return true
    }

    fun needsToDisplayToolWindow(): Boolean {
        return true // TODO
    }

    fun startLogin() {
        TODO("Not yet implemented")
    }

}
