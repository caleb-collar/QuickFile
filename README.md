# QuickFile FTP Server & Client With GUI

![gui_example](https://github.com/Mindstormer-0/QuickFile/blob/main/screenshots/gui_example.png?raw=true)

![gui_example2](https://github.com/Mindstormer-0/QuickFile/blob/main/screenshots/gui_example2.png?raw=true)

![server_example](https://github.com/Mindstormer-0/QuickFile/blob/main/screenshots/server_example.png?raw=true)

![client_example](https://github.com/Mindstormer-0/QuickFile/blob/main/screenshots/client_example.png?raw=true)

### Compiling & Running

- **Windows:**
- Compile: ```javac -cp "lib\*" src\*.java```
- Run (client): ```java -cp "src;lib\*" Driver```
- Run (server): ```java -cp "src;lib\*" Server```

- **OSX/Linux:**
- Compile: ```javac -cp "lib/*" src/*.java```
- Run (client): ```java -cp "src:lib/*" Driver```
- Run (server): ```java -cp "src:lib/*" Server```

- **Clean up:**
- ```rm -r src\*.class```

### Usage

Drag and drop files into the GUI to send them over LAN directly to the root of the server.

The client will **automatically** detect other running clients on startup as long as they are on the same network and port `54321` is available.

If an additional client is opened after the initial detection, there is a *Refresh Connections* option in the `file` dropdown.

There are multiple strategy patterns to choose from for `Handling` file inputs including
a recursive extraction of `.zip` files.

You may change the default download location from the root of the executable to another directory of a given client from the `file` dropdown and choosing *Download Location*.

Use the `.ignoreExtensions` file like a `.gitignore` with the Filter strategy.

The `view` menu contains multiple themes to fit your workspace.

By default, the system binds to port 54321.

### Java

https://www.oracle.com/java/technologies/downloads/#jdk17-windows
