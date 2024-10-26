#!/bin/bash

# Define paths
ROOT_PATH="/home/ec2-user/spring-github-action"
JAR_FILE="$ROOT_PATH/application.jar"
LOG_PATH="$ROOT_PATH/check_directory.log"

NOW=$(date +"%Y-%m-%d %H:%M:%S")  # Standardized date format

# Ensure the log file exists and is writable
if [ ! -f "$LOG_PATH" ]; then
    touch "$LOG_PATH"
    chown ec2-user:ec2-user "$LOG_PATH"
fi

# Log the check start
echo "[$NOW] Starting directory and file checks..." >> $LOG_PATH

# Check if the root directory exists
if [ -d "$ROOT_PATH" ]; then
    echo "[$NOW] Directory $ROOT_PATH exists." >> $LOG_PATH
else
    echo "[$NOW] Directory $ROOT_PATH does not exist. Exiting." >> $LOG_PATH
    exit 1
fi

# Check if the JAR file exists
if [ -f "$JAR_FILE" ]; then
    echo "[$NOW] JAR file $JAR_FILE exists." >> $LOG_PATH
else
    echo "[$NOW] JAR file $JAR_FILE does not exist. Exiting." >> $LOG_PATH
    exit 1
fi

# Optional: List the contents of the root directory
echo "[$NOW] Listing contents of $ROOT_PATH:" >> $LOG_PATH
ls -la "$ROOT_PATH" >> $LOG_PATH

# Final success message
echo "[$NOW] Directory and file checks completed successfully." >> $LOG_PATH

# Exit with success
exit 0
