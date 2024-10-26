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

# Log the copy action
echo "[$NOW] Copying JAR to $JAR" >> $START_LOG

# Copy the latest JAR file
#cp "$ROOT_PATH/build/libs/petpick-0.0.1-SNAPSHOT.jar" "$JAR"

# Verify if the copy was successful


# Log the execution action
echo "[$NOW] Starting application with $JAR" >> $START_LOG

# Start the application using nohup
nohup java -jar "$JAR" > "$APP_LOG" 2> "$ERROR_LOG" &

# Capture the PID of the newly started process
SERVICE_PID=$(pgrep -f "$JAR")

# Verify if the application started successfully
if [ -z "$SERVICE_PID" ]; then
    echo "[$NOW] Failed to start the application." >> $START_LOG
    exit 1
else
    echo "[$NOW] Application started successfully with PID: $SERVICE_PID" >> $START_LOG
fi
