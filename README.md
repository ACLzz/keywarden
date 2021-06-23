# Keywarden
Keywarden is an official client for <a href="https://github.com/ACLzz/keystore-server">Keystore</a> server.<br/>

## Features
This client has not many functions, but already can be used. Features that would come in future marked with `TODO` label
Features:
  - Register / Login user
  - Manage Collections
  - Manage Passwords
  - Safe copy passwords to clipboard
  - Search
  - Symmetric encryption passwords on client-side: `TODO`
  - Update username and password (password won't be updated if your user uses symmetric encryption): `TODO`

## Installation
Installation are available for Linux / MacOs, but if you wanna use it on Windows checkout build.gradle -> install task and make pull request for adding Windows support.<br/>
Also you need to install: `java-openjdk-15`, `openjfx-15`, `java-openjre-15`
```
git clone https://github.com/ACLzz/keywarden.git && cd keywarden
gradle install
```
It will build an app and make soft link from `~/.keywarden/bin/keywarden` to `/usr/local/bin` (Of course if you told your sudo password while installation)

## Configuration
Config file exist in `~/.keywarden` directory. You only need to change `server_url` parameter with url to your <a href="https://github.com/ACLzz/keystore-server">Keystore</a> server.

## Screenshots
<img src="extra/auth-window.png?raw=true" width="720px"/>
<img src="extra/main-window.png?raw=true" width="720px"/>

## Hotkeys
- DEL button after select row in passwords table: deletes this password
- Right mouse click on cell in passwords table: copies data to clipboard without showing content of that cell

## Used technologies
- `Gradle` as build tool
- `Kotlin` as main language
- `Tornadofx` as UI framework
- `Fuel` as http client
- `Hoplite` as yaml config parser
- `JVM` 15 version

## Contribution
You can contribute your code via merge request if you wanna to add more functionality to app.
