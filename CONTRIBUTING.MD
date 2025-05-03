# Contribute to the plugin

> [!WARNING]  
> As I want to maintain compability with older IDEs (2021.2+), this plugin requires five different versions of the IDEs to build!
> This means that it will take roughly 4–5GB space on your disk.

## 1. Loading the project

The project is a Gradle project, so it should auto-import when opening it on IntelliJ.
If it fails, you might want to change the used JDK to one supporting at least Java 21.
Please note that the first launch may take a while to load depending on your internet connection speed!

## 2. Understanding the project structure

The project is structured as follows:
- icons/
  - data/
    - applications: contains the descriptor files for the different IDEs. 
    - languages: contains the descriptor files for the different detected languages.
    - themes: contains the icon themes that are displayed.
- plugin/
  - common: contains all the code that is common to all IDEs versions
  - vXXX: contains the code specific to a certain IDE version

## 3. Understanding descriptor files
### A. IDEs descriptors
The IDE descriptors files describe how the plugin detects which IDE it runs on and how to display it on the presence.

```Yaml
# IntelliJ IDEA Ultimate
ids:
  - IU
  - intellij_idea_ultimate
discordId: 1211095369635860570
dummyFile: Main.java
```

For instance, this file will detect IDEs with either the code "IU" or the application name "intellij_idea_ultimate". It will display it with the discord application id "1211095369635860570" and the file name used in preview will be "Main.java".

### B. Languages descriptors
Language descriptor files describe how the plugin detects the type of the current displayed file and how to display it on the presence.

```Yaml
name: Kotlin
asset: kotlin
parent: text
match:
  extension:
    equals: .kt
flavors:
  - name: Kotlin Script
    match:
      extension:
        equals: .kts
```

For instance, this file registers the "Kotlin" language, with the assets name "kotlin" (see themes descriptors below), that matches text files with the ".kt" extensions. It also declares an override for "Kotlin Script" which ends with ".kts".

### C. Themes descriptors
Themes descriptors files describe how the different themes will be displayed in the plugin settings.
A theme descriptor file is as follows:
```Yaml
name: <name of the theme>
description: <description of the theme> (optional)
onlyApplicationIcons: <true|false> (if true, can only be used for application icons, false by default)
onlyLanguageIcons: <true|false> (if true, can only be used for language icons, false by default)
```

A theme is composed of its descriptor file and a folder with the same name, containing two subfolders, "applications," and "languages."

## 4. Testing the plugin
To test your changes, just runs the following Gradle task `gradle :plugin:common:runIde` or `gradle :plugin:v242:runIde` for the latest IDE version.

## 5. Building the plugin
1. Write your changes in the `plugin/changelog.md` file
2. Commit all your changes
3. Create a tag with the version name, i.e. `v2.X.Y`.
4. Run the `gradle default --stacktrace`

## 6. Other things to know
The whole "connection to Discord" part is handled by a fork of KDiscordIPC, that you can find [here](https://github.com/Azn9/KDiscordIPC).
