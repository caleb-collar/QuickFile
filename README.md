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

### Usage

Drag and drop files into the GUI to send them over LAN directly to the root of the server.

The client will automatically find a running server instance after launching the Driver as long
as the client and server are on the same LAN.

There are multiple strategy patterns to choose from for handling file inputs including
a recursive extraction of `.zip` files.

Use the ```.ignoreExtensions``` file like a `.gitignore` with the Filter strategy.

The `view` menu contains multiple themes to fit your workspace.

By default, the system binds to port 54321.
