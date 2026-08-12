# FastSIMD Compilation Guide

## Requirements
- **JDK 17+**
- **Apache Maven 3.8+**
- **MSVC / Visual Studio 2022** (with AVX2 support enabled `/arch:AVX2`)

## Building
```cmd
mvn clean package -DskipTests
```

## Running Demo
```cmd
run-demo.bat
```
