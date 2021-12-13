# QuickFile FTP Server & Client With GUI

## CSC 2910 OOP | Caleb Collar | 12.13.21

### Compliling & Running:

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

### Usage:

Drag and drop files into the GUI to transfer them to the root location
of the server.

The client will **automatically** connect to an existing server after running the 
driver method *as long as* the client and server are on the same LAN.

The system allows a user to select between strategies for handling input
files.

*Archived/compressed files will only extract if they are in ```.zip``` format.*

The ```.ignoreExtensions``` file should be used as a '.gitignore' set of exclusions;
used in conjunction with the file filter strategy.

Use the ```view``` menu to change GUI themes to suit your working environment.