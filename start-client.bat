@echo off
echo Starte Chat-Client...
javac -d out src\client\*.java
java -cp out client.ClientMain
pause
