@echo off
javac -cp ../lib/org.json.jar com/locked_chess/LockedChessCentre.java
jar cvf com.locked_chess.jar com/locked_chess/*.class
