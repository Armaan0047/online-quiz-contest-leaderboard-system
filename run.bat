@echo off
title Online Quiz Contest ^& Leaderboard System - Mohd Armaan Tak
color 0A

echo =========================================================================
echo       ONLINE QUIZ CONTEST ^& LEADERBOARD SYSTEM
echo       Student: Mohd Armaan Tak (ID: PIET25CR035)
echo       B.Tech Computer Science ^& Engineering ^| PIET Idea Lab (NSP)
echo =========================================================================
echo.

:: Step 0: Ensure local MySQL Database Server is running on port 3306
netstat -ano | findstr :3306 >nul
if %errorlevel% neq 0 (
    echo [*] Starting local MySQL Database Server...
    start "" /b "%~dp0mysql-env\mariadb-10.11.6-winx64\bin\mysqld.exe" --datadir="%~dp0mysql-data" --port=3306 --console
    timeout /t 3 /nobreak >nul
)

:: Step 1: Ensure bin directory exists
if not exist "bin" (
    echo [*] Creating 'bin' output directory...
    mkdir bin
)

:: Step 2: Compile Java Source Files
echo [*] Compiling Java source files...
javac -d bin -cp "lib/*" src/Main.java src/database/*.java src/model/*.java src/dao/*.java src/ui/*.java

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Compilation failed! Please check Java SDK installation.
    echo.
    pause
    exit /b %errorlevel%
)

echo [*] Compilation successful!
echo [*] Launching Online Quiz Contest Application...
echo.

:: Step 3: Launch Java Swing Application
java -cp "bin;lib/*" Main

if %errorlevel% neq 0 (
    echo.
    echo [NOTE] Application closed with code %errorlevel%.
    pause
)
