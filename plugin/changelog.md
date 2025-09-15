## v2.1.8
- Fix detection of files incorrectly marked as Gradle
- Fix .h files being displayed with the C icon

## v2.1.7
- Fix small issues that raise errors without being real errors
- Fix the webstorm icon not showing with the "macos" icons theme

## v2.1.6
- Fix presence no longer showing after a recent Discord update

## v2.1.5
- Add avalonia (.axml) support and icons for themes classic, flat, and material
- Fix a typo in the settings where "Large icon" is written instead of "Small icon"
- Fix an issue with JetBrains Client when using version without edition mode
- Fix an issue with phpstorm due to a missing identifier
- Fix an incorrect browser detection leading to false-positive warnings
- Fix some other issues

## v2.1.4
- Lower the java version on 242 plugin versions as some IDE still use java 17
- Add detection for custom discord client such as Vesktop
- Add a custom field for large and small icons, allowing to set custom icon URLs
- Fix an issue causing the application name to not change while changing the setting
- Fix an issue causing the plugin to not work on remote development/JetBrains Client
- Fix some connection issues
- Add solidity icons

## v2.1.3
- Mark the plugin as incompatible with Almighty Alpaca's old version
- Fix the presence not hiding or showing "idling" when idling
- Fix exception "This socket has either been closed, or, was never connected"
- Fix incompatibility wiht DataSpell v2024.2
- Fix wrong templates.adoc link
- Update all application IDs
- Update the IntelliJ IDEA Community icon in the "2024 icons" theme
- Update C# icon in the "classic" theme

## v2.1.2
- Cancel some functionalities of the plugin if Almighty Alpaca's old version is detected as it raises exceptions
- Add notifications when an issue that may/will prevent the plugin from working is detected
- Fix error "IPC error: ErrorEventData(code=1000, message=Request has been terminated..."
- Fix error "Throwable: unknown runnable: dev.azn9.plugins.discord.time.TimeService"

## v2.1.1
- Re-enable the need to restart the IDE when updating or disabling the plugin to avoid issues like `getService(T::class.java) must not be null`

## v2.1.0
- Added support for IntelliJ IDEA 2024+
- Allow users to set a custom discord application id per project
- Allow users to choose a different icon theme for application icons and file icons
- Added support for hlsl files, and added missing icons for glsl
- Added a theme with the new JetBrains icons
- Added a theme with the macOS JetBrains icons
- Added two themes for language: Catppuccin Late and Mocha
- Allow users to set an url for the large and small images
- Allow users to automatically report errors as GitHub issues via the error dialog
- Disable the need to restart the IDE when updating or disabling the plugin
- Add missing matchers for C++ modules (.cppm and .ixx)
- The plugin will no longer display the project name if the user has not chosen a display mode to avoid leaking information
- Fix an issue where choosing the "default" theme for a project would not update when changing the global theme
- Fix an issue where changing theme would not update the presence
- Fix a false positive with arRPC
- Fix an issue with "1d" idle value
- Move icons to a CDN to allow for faster updates
- Rework the theme system to allow for more flexibility and easier updates

## v2.0.8
- Fixed compatibility issues with IDEs version 2023.2 and above 

## v2.0.7
- Added back support for IDEs version from 2021.2 to 2022.3
- Fixed some deprecated API usages to conform with the jetbrains marketplace requirements
- Changed the plugin id to `dev.azn9.plugins.discord` to conform with the jetbrains marketplace requirements
- Fixed the "ContainerDisposedException" happening when switching between projects
- Fixed an issue occurring when closing the IDE while a render update is in progress, causing logging of errors
- Updated the plugin description to explain the changes made to the original plugin and why there is two versions of the same plugin

## v2.0.6
- Changed plugin id to `dev.azn9.jetbrains.plugins.discord` to avoid conflicts with the original plugin
- Added buttons to the rich presence. The title and the url of each button can be configured in the project settings
- Allow to change the icon theme per project
- Changed the discord icon on the tools menu
- Prevent `ContainerDisposedException` happening when switching between projects
- Updated copyright year
- Updated whole project to rebase onto the original plugin
- Added some missing themes for RustRover, Writerside and Aqua

## v2.0.5
- Added support for CLion Nova
- Fixed Discord link not clickable in the notification popup

## v2.0.4
- Added support for Writerside (thanks @mister-proger)
- Added support for Aqua
- Fixed info link to the correct GitHub repo (thanks @KaylaSolace)
- Fixed issues with latest IDEs versions

## v2.0.3
- Fix icons for RustRover (thanks @zakuciael)
- Fix plugin not working on Linux with IDEs version 2023.2.3.X.
- Fix many issues triggering error logs when closing discord, or launching the IDE before discord.
- Fix the application id for RustRover with the classic theme.
- Added a link to the community discord (https://discord.gg/mEDvg6sYp2)

## v2.0.2
- Added support for RustRover
- Fix presence being updated even if there is no change, causing an exponential delay between updates
- Fix some debug data being logged even if debug logging is disabled

## v2.0.1
This version fixes https://github.com/Azn9/JetBrains-Discord-Integration/issues/3
- Fix presence not re-showing if discord is restarted
- Fix presence not showing if discord is started after the IDE
- Fix error logged when closing discord
- Support IntelliJ version up to 2023.2.1

## v2.0.0
THIS RELEASE IS FROM A FORK OF THE ORIGINAL PLUGIN - see readme for more info
- Fix fields `large_text` and `small_text` throwing errors if empty (https://github.com/Almighty-Alpaca/JetBrains-Discord-Integration/pull/289)
- Fix discord ipc issues (https://github.com/Almighty-Alpaca/JetBrains-Discord-Integration/pull/314)
- Support IntelliJ version up to 2023.1.4

## v1.11.0
- Fix compatibility with IntelliJ version 2022.3

## v1.10.0
- Fix `Cannot create listener` exception (#230)
- Fix active time for projects (#237)
- Update Zig icon (#239)
- Update Dart icon (#214)

## v1.9.0
- Support IntelliJ version 2022.1 (#221)
- Add classic Godot icon (#222)

## v1.8.0
- Fix compatibility with IntelliJ version 2021.3 (#204)
- Add support for DataSpell (#205)
- Make some settings easier to understand (#206)

## v1.7.1
- Fix conflict with other plugins using the same project structure (e.g. Perl) (#192)
- Fix `Access is allowed from event dispatch thread only` error in commit view (#191)

## v1.7.0
- Add option to show a simple "Idling" message instead of hiding everything when afk (#163)
- Support for Apple Silicon (#166)
- Support for IntelliJ version 2021.2 (#159, #164)
- Enhance template documentation (#155)
- Better icon search order (#147) 
- Better error handling in templates

## v1.6.1
- Fix unneeded dependencies inflating plugin size 

## v1.6.0
- Implement custom templates
- Add support for .cjs and .mjs files
- Add option to hide  presence while a specific project is open
- Fix log spam by moving all repetitive logging to debug level
- Fix extensions conflicts
- Fix AlreadyDisposedExceptions
- Fix compilation on Java 8
- Fix ClassCastException when rendering preview with disabled presence
- Fix preview for default avatars

## v1.5.0
- Icon text is now more customizable
- Fix sporadic ProcessCanceledException when updating presence
- Fix invisible lines
- Fix resetting open time
- Fix read action required error when getting Git info
- Fix access error when getting current editor

## v1.4.1 & 1.4.2
- Fix error when the Git plugin is disabled
- Fix update notification spam
- Fix "null" branch when no vcs was found
- Reduce log spam
- Enable local icons and languages

## v1.4.0
- Add ability to ignore files in .gitignore
- Add option to show a project but hide files
- Add support for showing the time the IDe was active instead of total time since startup 
- Rework actions in toolbar
- Detect Discord running in Flatpak
- Enhance preview in settings
- Fix exception when checking for other Discord plugins on startup
- Fix idle timeout always being enabled

## v1.3.0
- Add a button to force reconnect to Discord client
- Add ability to show current git branch (#89)
- Add support for Rider for Unreal Engine preview
- Add support for idle timeout and elapsed time for files/projects again 
- Fix a lot of bugs that could prevent the plugin from loading
- Fix settings preview being rendered in the background
- Fix hidden projects being shown
- Change target Java version back to 1.8

## v1.2.0
- Support for 2020.1 (#77)
- Completely new way how current presence is calculated
- Move settings to "other" category 
- Fix presence for very short project names/custom strings
- Disable timeout option for now due to architectural changes (will be enabled again later)

## v1.1.1
- Fix error when closing a project (for real this time 🙏🏼). (#63)
- Change required IntelliJ version to 2019.1

## v1.1.0
- Fix error when closing a project. (#63)
- Beta testers now get a special role in the server
- Add IDE names without editions (e.g. 'IntelliJ IDEA' instead of 'IntelliJ IDEA Community')
- Show warning in settings when other Discord rich presence plugins are installed

## v1.0.1
- Fix Reading/editing being swapped
- Fix show project prompt always showing
- Fix show project prompt disappearing too quickly
- Fix compatibility with plugins with non-default file system implementations

## v1.0.0
- Brand new codebase written in Kotlin
- Support for multiple icon themes
- Customize every aspect of the rich presence
- Beautiful preview in settings
- Support for project descriptions
- Whole new language detection with support for a wide range of languages and frameworks 
- Adding more languages is now possible in a matter of seconds without a new plugin update  
- ...And so much more I can't even remember what already existed before and what didn't
 
## v0.9.0
- Added F# (#43)
- Added additional C# file extensions (#43)
- Added setting to force big IDE icon (#27)
- Added setting to hide elapsed time (#37)
- Added additional C++ file extensions (#40)
- Added Slim and CoffeeScript (#34)
- Fixed C files not showing (#42)
- Fixed file not reappearing after removing project description (#31)
- Fixed debug folder being created after when debug logging has been disabled
- Updated Java-DiscordRPC to v2.0.1
- Updated JGroups to 4.0.12

## v0.8.0
- Added the ability to add project descriptions (#24)
- Added Dart (#30)
- Added option to hide files (#23)
- Added Twig (#26)
- Added Sass (#25)
- Added PowerShell (#29)
- Updated JGroups to 4.0.11
- Updated Java-DiscordRPC to v1.3.6

## v0.7.2
- Fixed ghost files

## v0.7.1
- Fixed wrong artifact

## v0.7.0
- Added Ceylon (#20)
- Clearing the presence should now work better
- Added experimental and debug settings
  - Added option to enable debug logs
  - Added option to enable the experimental VisibleAreaListener

## v0.6.0
- Added an option to reset the open time after an inactivity timeout
- Added Handlebars.js (#16)
- Added Shell (#18)
- Added Elixir (#17)
- Added Erlang
- Added option hide the presence after a period of inactivity (#10)
- Fixed switching between multiple projects (#12)
- The RPC connection will now be closed if there's nothing to show
- Added a button to fast enable/disable rich presence per project
- Added Golo
- Added .htaccess
- Added .cxml and .fxml to XML
- Added Git
- Fixed ConcurrentModificationExceptions when running multiple instances

## v0.5.0
- Added Vue.js
- Fixed file name change handling
- Fixed rare issue with locale settings (#13)
- Added YAML
- Updated Java-DiscordRPC to v1.3.1 and utilized it's new equals methods
- Implemented settings menu (#15)

## v0.4.0
- Fixed elapsed time
- Sync is now done after a small delay which will speed up the process if you open multiple files rapidly
- Fixed compatibility with Android Studio
- Added TypeScript (#8)
- Added Lua

## v0.3.0
- Fixed name for files without extension
- Added MPS
- Fixed C++ and C# asset names
- Added CMake as language
- Fixed NPE on older versions of CLion (#5)
- Updated Java-DiscordRPC to v1.3.0

## v0.2.0
- The IDE distribution and the language of the current file are now shown as images of the rich presence.

## v0.1.0
- Initial Release
