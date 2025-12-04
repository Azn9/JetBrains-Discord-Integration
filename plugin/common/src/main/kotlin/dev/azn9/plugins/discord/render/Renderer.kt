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

import dev.azn9.plugins.discord.DiscordPlugin
import dev.azn9.plugins.discord.icons.source.web.WebAsset
import dev.azn9.plugins.discord.render.templates.asCustomTemplateContext
import dev.azn9.plugins.discord.rpc.RichPresence
import dev.azn9.plugins.discord.settings.options.types.SimpleValue
import dev.azn9.plugins.discord.settings.options.types.TemplateValue
import dev.azn9.plugins.discord.settings.values.*
import dev.azn9.plugins.discord.utils.Plugin
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

abstract class Renderer(protected val context: RenderContext) {
    suspend fun render(): RichPresence = context.render()

    protected abstract suspend fun RenderContext.render(): RichPresence

    protected fun RenderContext.render(
        details: TextValue?,
        detailsCustom: TemplateValue?,
        state: TextValue?,
        stateCustom: TemplateValue?,
        largeIcon: IconValue?,
        largeIconCustom: TemplateValue?,
        largeIconText: TextValue?,
        largeIconTextCustom: TemplateValue?,
        smallIcon: IconValue?,
        smallIconCustom: TemplateValue?,
        smallIconText: TextValue?,
        smallIconTextCustom: TemplateValue?,
        startTimestamp: TimeValue?,
        button1Title: String? = null,
        button1Url: String? = null,
        button2Title: String? = null,
        button2Url: String? = null
    ): RichPresence {
        DiscordPlugin.LOG.debug("Rendering presence, data=${context.data}, mode=${context.mode}")
        DiscordPlugin.LOG.debug("Themes: ${context.source.getThemesOrNull()}")
        DiscordPlugin.LOG.debug("languages: ${context.source.getLanguagesOrNull()}")
        DiscordPlugin.LOG.debug("Application Icons: ${context.applicationIcons}")
        DiscordPlugin.LOG.debug("Language Icons: ${context.languageIcons}")
        DiscordPlugin.LOG.debug("Data: ${context.data}")
        DiscordPlugin.LOG.debug("Mode: ${context.mode}")

        return RichPresence(context.applicationData?.applicationId) presence@{
            val customTemplateContext by lazy { context.asCustomTemplateContext() }

            this@presence.details = when (val line = details?.getValue()?.get(context)) {
                null, PresenceText.Result.Empty -> null
                PresenceText.Result.Custom -> detailsCustom?.getValue()?.execute(customTemplateContext)
                is PresenceText.Result.String -> line.value
            }

            this@presence.state = when (val line = state?.getValue()?.get(context)) {
                null, PresenceText.Result.Empty -> null
                PresenceText.Result.Custom -> stateCustom?.getValue()?.execute(customTemplateContext)
                is PresenceText.Result.String -> line.value
            }

            this@presence.startTimestamp = when (val time = startTimestamp?.getValue()?.get(context)) {
                null, PresenceTime.Result.Empty -> null
                is PresenceTime.Result.Time ->
                    OffsetDateTime.ofInstant(
                        Instant.ofEpochMilli(time.value),
                        ZoneId.systemDefault()
                    )
            }

            val largeImageCaption = when (val text = largeIconText?.getValue()?.get(context)) {
                null, PresenceText.Result.Empty -> null
                is PresenceText.Result.String -> text.value
                PresenceText.Result.Custom -> largeIconTextCustom?.getValue()?.execute(customTemplateContext)
            }

            this@presence.largeImage = when (val icon = largeIcon?.getValue()?.get(context)) {
                null, PresenceIcon.Result.Empty -> null
                PresenceIcon.Result.Custom -> {
                    val assetUrl = largeIconCustom?.getStoredValue()?.execute(customTemplateContext)?.trim()

                    if (assetUrl?.isEmpty() != false) {
                        null
                    } else {
                        RichPresence.Image(WebAsset(assetUrl), largeImageCaption)
                    }
                }
                is PresenceIcon.Result.Asset -> {
                    RichPresence.Image(icon.value, largeImageCaption)
                }
            }

            val smallImageCaption = when (val text = smallIconText?.getValue()?.get(context)) {
                null, PresenceText.Result.Empty -> null
                is PresenceText.Result.String -> text.value
                PresenceText.Result.Custom -> smallIconTextCustom?.getValue()?.execute(customTemplateContext)
            }

            this@presence.smallImage = when (val icon = smallIcon?.getValue()?.get(context)) {
                null, PresenceIcon.Result.Empty -> null
                PresenceIcon.Result.Custom -> {
                    val assetUrl = smallIconCustom?.getValue()?.execute(customTemplateContext)

                    if (assetUrl == null) {
                        null
                    } else {
                        RichPresence.Image(WebAsset(assetUrl), smallImageCaption)
                    }
                }
                is PresenceIcon.Result.Asset -> {
                    RichPresence.Image(icon.value, smallImageCaption)
                }
            }

            this@presence.button1Title = button1Title
            this@presence.button1Url = button1Url
            this@presence.button2Title = button2Title
            this@presence.button2Url = button2Url

            this.partyId = Plugin.version?.toString()
        }
    }

    enum class Mode {
        NORMAL,
        PREVIEW;

        fun <T> SimpleValue<T>.getValue() = getValue(this@Mode)

        fun <T> SimpleValue<T>.setValue(value: T) = setValue(this@Mode, value)

        fun <T> SimpleValue<T>.updateValue(block: (T) -> T) = updateValue(this@Mode, block)
    }

    sealed class Type {
        abstract fun createRenderer(context: RenderContext): Renderer?

        open class None protected constructor() : Type() {
            override fun createRenderer(context: RenderContext): Renderer? = null

            companion object : None()
        }

        open class Idle protected constructor() : Type() {
            override fun createRenderer(context: RenderContext): Renderer? = IdleRenderer(context)

            companion object : Idle()
        }

        open class Application protected constructor() : Type() {
            override fun createRenderer(context: RenderContext): Renderer = ApplicationRenderer(context)

            companion object : Application()
        }

        open class Project protected constructor() : Application() {
            override fun createRenderer(context: RenderContext): Renderer = ProjectRenderer(context)

            companion object : Project()
        }

        open class File protected constructor() : Project() {
            override fun createRenderer(context: RenderContext): Renderer = FileRenderer(context)

            companion object : File()
        }
    }
}
