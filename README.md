<p align="center">
  <img src="plugin\src\main\resources\META-INF\pluginIcon.svg" alt="JetBrains Discord Integration" width="200"/>
</p>
<h1 align="center">JetBrains Discord Integration v2</h1>

----

# Notice
This repo is a fork of the original JetBrains Discord Integration plugin, as its owner has been inactive for a while. I will try to keep it up to date with the latest versions of discord and jetbrains IDEs, and fix any issues that may arise.
Feel free to open an issue if you have any problems with the plugin, or if you have any suggestions.
Feel free to open a pull request if you want to contribute to the project.

Note : The plugin is not available on the JetBrains plugin repository yet.

----

## Installation...

### ...from the JetBrains Plugin Repository (need to do it manually)

Go to `Setting` -> `Plugin` -> `Manage Plugin Repositories...` click add button and paste
`https://github.com/Azn9/JetBrains-Discord-Integration/raw/master/update.xml`.
after this you can search it in Marketplace just like normal plugin.

#### ...from the GitHub release page

Go to the [GitHub release page](https://github.com/Azn9/JetBrains-Discord-Integration/tags) and grab the latest `JetBrains-Discord-Integration-X.X.X.zip`. Do not unzip the file. In your IDE go to `Settings > Plugins > Install plugin from disk...` and select the previously downloaded zip file.

#### ...or compile from source

As this project uses Gradle it's very easy to compile yourself. Be aware though that depending on your system this may take a bit as it needs to download the sources for IntelliJ first.
Open your favorite terminal and execute the following commands:

```bash
git clone https://github.com/Azn9/JetBrains-Discord-Integration.git
cd JetBrains-Discord-Integration
# The next line is only necessary on Linux to make the Gradle Wrapper executable
chmod +x gradlew
./gradlew
```
This will generate the file `JetBrains-Discord-Integration-X.X.X.zip`.

To install the zip file follow the steps from the [previous install method](#from-the-github-release-page).

## Support...

### ...using GitHub issues

For feature requests and bug reports, feel free to make use of the GitHub issues by submitting a new issue. Please check whether someone has reported your issue already before creating your own report. If you submit a new Issue please include as much detail as possible in your issue and how to reproduce it if possible and relevant.

#### ...or why not join the JetBrains community server

If you have questions regarding any JetBrains IDE or other JetBrains products and projects feel free to join the [JetBrains community Discord server](https://discord.gg/9ut9sqD) where many users of JetBrains hang out alongside some of the JetBrains team.

[![JetBrains Community Discord Server](https://discordapp.com/api/guilds/433980600391696384/embed.png?style=banner2)](https://discord.gg/9ut9sqD)

## License

This project has been released under the Apache 2.0 license. You can see the full license [here](/LICENSE.md)
