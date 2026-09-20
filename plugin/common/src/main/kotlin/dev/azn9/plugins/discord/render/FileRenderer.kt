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

import dev.azn9.plugins.discord.data.Data
import dev.azn9.plugins.discord.rpc.RichPresence
import dev.azn9.plugins.discord.settings.settings

class FileRenderer(context: RenderContext) : Renderer(context) {
    override suspend fun RenderContext.render(): RichPresence {
        val projectSettings = (context.data as Data.Project).projectSettings

        return render(
            details = settings.fileDetails,
            detailsCustom = settings.fileDetailsCustom,
            state = settings.fileState,
            stateCustom = settings.fileStateCustom,
            largeIcon = settings.fileIconLarge,
            largeIconCustom = settings.fileIconLargeCustom,
            largeIconText = settings.fileIconLargeText,
            largeIconTextCustom = settings.fileIconLargeTextCustom,
            smallIcon = settings.fileIconSmall,
            smallIconCustom = settings.fileIconSmallCustom,
            smallIconText = settings.fileIconSmallText,
            smallIconTextCustom = settings.fileIconSmallTextCustom,
            startTimestamp = settings.fileTime,
            button1Title = projectSettings.button1Title.getValue().ifBlank { settings.button1Text.getValue() },
            button1Url = projectSettings.button1Url.getValue().ifBlank { settings.button1Url.getValue() },
            button2Title = projectSettings.button2Title.getValue().ifBlank { settings.button2Text.getValue() },
            button2Url = projectSettings.button2Url.getValue().ifBlank { settings.button2Url.getValue() }
        )
    }
}
