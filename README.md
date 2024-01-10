<p align="center">
  <img src=".idea/icon.png" alt="JetBrains Discord Integration" width="200"/>
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
`https://github.com/Azn9/JetBrains-Discord-Integration/raw/develop/update.xml`.
after this you can search it in Marketplace just like normal plugin. It will be named `Discord Integration v2` as a separate plugin.

#### ...from the GitHub release page

Go to the [GitHub release page](https://github.com/Azn9/JetBrains-Discord-Integration/tags) and grab the latest `JetBrains-Discord-Integration-X.X.X.zip` **corresponding to your version** ([Which version to choose?](#which-version-to-choose)). Do not unzip the file. In your IDE go to `Settings > Plugins > Install plugin from disk...` and select the previously downloaded zip file.
See below to choose the correct version for your IDE.

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
See below to choose the correct version for your IDE.

### Which version to choose?
This plugin is split into different versions corresponding to the lowest supported version of the IDE.
- `v212` is for IDEs 2021.2.X and above
- `v223` is for IDEs 2022.3.X and above
- `v231` is for IDEs 2023.1.X and above
- `v232` is for IDEs 2023.2.X and above

Warning, you must choose the correct lowest version for your IDE!
For instance, if you are using IntelliJ IDEA 2022.2.X you **must** choose the `v212` version, not the `v223` version, even though the `v223` version is nearer to your version. 

If you are unsure which version to choose, use the [JetBrains Plugin Repository](#from-the-jetbrains-plugin-repository-need-to-do-it-manually) method to install the plugin. It will automatically choose the correct version for you.


## Support...

### ...by joining the plugin Discord server

There is a [dedicated Discord server](https://discord.gg/mEDvg6sYp2) for the JetBrains Discord Integration Plugin where we provide support for the plugin and have many of the users of the plugin chilling out.

[![JetBrains Discord Integration Plugin Server](https://discordapp.com/api/guilds/1148801249425235968/embed.png?style=banner3)](https://discord.gg/mEDvg6sYp2)

### ...using GitHub issues

For feature requests and bug reports, feel free to make use of the GitHub issues by submitting a new issue. Please check whether someone has reported your issue already before creating your own report. If you submit a new Issue please include as much detail as possible in your issue and how to reproduce it if possible and relevant.

## License

This project has been released under the Apache 2.0 license. You can see the full license [here](/LICENSE.md)
