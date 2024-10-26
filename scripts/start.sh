#!/bin/bash

# Variables
ROOT_PATH="/home/ec2-user/petpick-deploy"
JAR="$ROOT_PATH/petpick-0.0.1-SNAPSHOT.jar"

APP_LOG="$ROOT_PATH/application.log"
ERROR_LOG="$ROOT_PATH/error.log"
START_LOG="$ROOT_PATH/start.log"

NOW=$(date +"%Y-%m-%d %H:%M:%S")  # Standardized date format

# Ensure the ROOT_PATH exists
if [ ! -d "$ROOT_PATH" ]; then
    echo "[$NOW] Directory $ROOT_PATH does not exist. Creating it." >> $START_LOG
    mkdir -p "$ROOT_PATH"
    chown ec2-user:ec2-user "$ROOT_PATH"
fi

# Log the start action
echo "[$NOW] Starting application $JAR" >> $START_LOG

# Start the application using nohup, redirect output, and background the process
nohup java -jar "$JAR" >> $APP_LOG 2>> $ERROR_LOG &

# Log that start.sh completed
echo "[$NOW] start.sh completed." >> $START_LOG
