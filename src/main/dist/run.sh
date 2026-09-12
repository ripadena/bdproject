#!/usr/bin/env sh
cd "$(dirname "$0")"
CONFIG_FILE="${MILLIONAIRE_DB_CONFIG:-../config/db-postgres.properties}"
java --enable-native-access=ALL-UNNAMED -Dmillionaire.db.config="$CONFIG_FILE" -cp "millionaire-quiz.jar:lib/*" ru.misha.millionaire.App
