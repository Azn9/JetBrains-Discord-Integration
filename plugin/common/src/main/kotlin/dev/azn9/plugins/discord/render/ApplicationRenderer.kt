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

import dev.azn9.plugins.discord.settings.settings

class ApplicationRenderer(context: RenderContext) : Renderer(context) {
    override suspend fun RenderContext.render() = render(
        details = settings.applicationDetails,
        detailsCustom = settings.applicationDetailsCustom,
        state = settings.applicationState,
        stateCustom = settings.applicationStateCustom,
        largeIcon = settings.applicationIconLarge,
        largeIconCustom = settings.applicationIconLargeCustom,
        largeIconText = settings.applicationIconLargeText,
        largeIconTextCustom = settings.applicationIconLargeTextCustom,
        smallIcon = settings.applicationIconSmall,
        smallIconCustom = settings.applicationIconSmallCustom,
        smallIconText = settings.applicationIconSmallText,
        smallIconTextCustom = settings.applicationIconSmallTextCustom,
        startTimestamp = settings.applicationTime,
        button1Title = settings.button1Text.getValue(),
        button1Url = settings.button1Url.getValue(),
        button2Title = settings.button2Text.getValue(),
        button2Url = settings.button2Url.getValue()
    )
}
