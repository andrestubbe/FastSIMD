@echo off
chcp 65001 >nul
cd /d "%~dp0"

echo ⚡ Building Main Project (FastSIMD)...
call mvn install -DskipTests -q
if %ERRORLEVEL% NEQ 0 ( echo ❌ Main build failed. & pause & exit /b %ERRORLEVEL% )

echo 🚀 Running Demo for FastSIMD...
cd examples\Demo
call mvn compile exec:java -Dexec.mainClass=fastsimd.Demo -q

cd ..\..
pause
