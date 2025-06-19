@echo off
echo [Starte Chat-Server mit SQLite-Unterstützung...]

:: Pfade zur .jar und compiled .class
set JAR=sqlite-jdbc-3.49.1.0.jar
set OUT=out

:: Kompilieren (optional – falls noch nicht geschehen)
javac -d %OUT% src\server\*.java 

:: Server starten
java -cp "%OUT%;%JAR%" server.ServerMain

pause

