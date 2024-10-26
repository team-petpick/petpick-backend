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

# Start the application using nohup
nohup java -jar "$JAR"

# Capture the PID of the newly started process


# Verify if the application started successfully

