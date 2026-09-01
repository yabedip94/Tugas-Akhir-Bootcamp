@echo off
setlocal

title HADIR Selenium Automation - TestNG

echo ================================================
echo   HADIR Selenium Automation - TestNG
echo ================================================
echo.

REM Selalu jalankan dari folder tempat file .bat berada
cd /d "%~dp0"

echo [INFO] Project folder:
echo %CD%
echo.

REM Pastikan pom.xml ada
if not exist "pom.xml" (
    echo [ERROR] pom.xml tidak ditemukan.
    echo Pastikan run-tests.bat disimpan di folder utama project.
    echo.
    pause
    exit /b 1
)

REM Pastikan Maven tersedia
where mvn >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Maven tidak ditemukan di PATH.
    echo Pastikan Maven sudah ter-install dan mvn bisa dijalankan dari CMD.
    echo.
    pause
    exit /b 1
)

echo [INFO] Menjalankan seluruh TestNG suite...
echo [INFO] Command: mvn clean test
echo.

call mvn clean test

set "EXIT_CODE=%ERRORLEVEL%"

echo.
echo ================================================
if "%EXIT_CODE%"=="0" (
    echo   TEST SUITE SELESAI - BUILD SUCCESS
) else (
    echo   TEST SUITE SELESAI - BUILD FAILURE
)
echo ================================================
echo.
echo Exit code: %EXIT_CODE%
echo.

pause
exit /b %EXIT_CODE%
