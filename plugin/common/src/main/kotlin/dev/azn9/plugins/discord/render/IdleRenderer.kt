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

import dev.azn9.plugins.discord.icons.source.web.WebAsset
import dev.azn9.plugins.discord.render.templates.asCustomTemplateContext
import dev.azn9.plugins.discord.rpc.RichPresence
import dev.azn9.plugins.discord.settings.settings
import dev.azn9.plugins.discord.settings.values.PresenceIcon
import dev.azn9.plugins.discord.settings.values.PresenceText
import dev.azn9.plugins.discord.utils.Plugin
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

class IdleRenderer(context: RenderContext) : Renderer(context) {
    override suspend fun RenderContext.render(): RichPresence {

        return RichPresence(idleData?.applicationId) presence@{
            this@presence.details = "Idling"

            this@presence.startTimestamp = idleData?.idleTimestamp?.let {
                OffsetDateTime.ofInstant(
                    Instant.ofEpochMilli(it),
                    ZoneId.systemDefault()
                )
            }

            val customTemplateContext by lazy { context.asCustomTemplateContext() }

            this@presence.largeImage = when (val icon = settings.applicationIconLarge.getValue().get(context)) {
                PresenceIcon.Result.Empty -> null
                is PresenceIcon.Result.Asset -> {
                    val caption = when (val text = settings.applicationIconLargeText.getValue().get(context)) {
                        PresenceText.Result.Empty -> null
                        is PresenceText.Result.String -> text.value
                        PresenceText.Result.Custom -> settings.applicationIconLargeTextCustom.getValue().execute(customTemplateContext)
                    }
                    RichPresence.Image(icon.value, caption)
                }
                PresenceIcon.Result.Custom -> {
                    val assetUrl = settings.projectIconLargeCustom.getStoredValue().execute(customTemplateContext).trim()

                    val largeImageCaption = when (val text = settings.projectIconLargeText.getValue().get(context)) {
                        PresenceText.Result.Empty -> null
                        is PresenceText.Result.String -> text.value
                        PresenceText.Result.Custom -> settings.projectIconLargeTextCustom.getValue().execute(customTemplateContext)
                    }

                    if (assetUrl.isEmpty()) {
                        null
                    } else {
                        RichPresence.Image(WebAsset(assetUrl), largeImageCaption)
                    }
                }
            }

            this.partyId = Plugin.version?.toString()
        }
    }
}
