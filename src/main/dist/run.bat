@echo off
cd /d "%~dp0"
if "%MILLIONAIRE_DB_CONFIG%"=="" (
    set "CONFIG_FILE=..\config\db-postgres.properties"
) else (
    set "CONFIG_FILE=%MILLIONAIRE_DB_CONFIG%"
)
java --enable-native-access=ALL-UNNAMED -Dmillionaire.db.config="%CONFIG_FILE%" -cp "millionaire-quiz.jar;lib/*" ru.misha.millionaire.App
