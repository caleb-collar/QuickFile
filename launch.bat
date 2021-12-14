if not "%minimized%"=="" goto :minimized
set minimized=true
start /min cmd /C "%~dpnx0"
goto :EOF
:minimized
start /min cmd /C "%~dpnx0 & java -cp "src;lib\*" Server"
start /min cmd /C "%~dpnx0 & java -cp "src;lib\*" Driver"