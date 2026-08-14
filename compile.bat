@echo off
setlocal EnableDelayedExpansion

echo ===========================================
echo FastSIMD - MSVC AVX2 Native Compiler
echo ===========================================

if not defined JAVA_HOME (
    if exist "C:\Program Files\Java\jdk-21.0.12" ( set "JAVA_HOME=C:\Program Files\Java\jdk-21.0.12" )
    if exist "C:\Program Files\Java\jdk-17" ( set "JAVA_HOME=C:\Program Files\Java\jdk-17" )
    if exist "C:\Program Files\Eclipse Adoptium\jdk-17-hotspot" ( set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17-hotspot" )
)

set "VSWHERE=%ProgramFiles(x86)%\Microsoft Visual Studio\Installer\vswhere.exe"
for /f "usebackq tokens=*" %%i in (`"%VSWHERE%" -latest -products * -requires Microsoft.VisualStudio.Component.VC.Tools.x86.x64 -property installationPath`) do (
    set "VS_INSTALL=%%i"
)
set "VCVARS=%VS_INSTALL%\VC\Auxiliary\Build\vcvars64.bat"
call "%VCVARS%"

if not exist build mkdir build

cl /LD /Fe:build\fastsimd.dll native\fastsimd.cpp /I"%JAVA_HOME%\include" /I"%JAVA_HOME%\include\win32" /EHsc /std:c++17 /O2 /W3 /arch:AVX2

if %ERRORLEVEL% NEQ 0 ( echo ❌ Compilation failed. & pause & exit /b %ERRORLEVEL% )

if not exist "src\main\resources\win32-x86-64" mkdir "src\main\resources\win32-x86-64"
if not exist "src\main\resources\native" mkdir "src\main\resources\native"

copy /Y build\fastsimd.dll src\main\resources\win32-x86-64\fastsimd.dll
copy /Y build\fastsimd.dll src\main\resources\win32-x86-64\FastSIMD.dll
copy /Y build\fastsimd.dll src\main\resources\native\fastsimd.dll
copy /Y build\fastsimd.dll src\main\resources\native\FastSIMD.dll

echo ✅ fastsimd.dll and FastSIMD.dll successfully compiled and copied!
