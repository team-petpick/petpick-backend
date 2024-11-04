#!/bin/bash
set -x
# Variables
ROOT_PATH="/home/ec2-user/petpick-deploy"
JAR="$ROOT_PATH/petpick-0.0.1-SNAPSHOT.jar"
STOP_LOG="$ROOT_PATH/stop.log"
NOW=$(date +"%Y-%m-%d %H:%M:%S")  # Standardized date format

# Log the stop action
echo "[$NOW] Attempting to stop application." >> $STOP_LOG

# Find the PID of the running application
SERVICE_PID=$(pgrep -f "$JAR")

if [ -z "$SERVICE_PID" ]; then
    echo "[$NOW] Service not found. No action needed." >> $STOP_LOG
else
    echo "[$NOW] Stopping service with PID: $SERVICE_PID" >> $STOP_LOG
    kill "$SERVICE_PID"

    # Wait for the process to terminate gracefully
    sleep 5

    # Verify if the process has stopped
    SERVICE_PID_CHECK=$(pgrep -f "$JAR")
    if [ -z "$SERVICE_PID_CHECK" ]; then
        echo "[$NOW] Service stopped successfully." >> $STOP_LOG
    else
        echo "[$NOW] Failed to stop service gracefully. Forcing termination." >> $STOP_LOG
        kill -9 "$SERVICE_PID_CHECK"

        # Final verification
        FINAL_CHECK=$(pgrep -f "$JAR")
        if [ -z "$FINAL_CHECK" ]; then
            echo "[$NOW] Service forcefully terminated." >> $STOP_LOG
        else
            echo "[$NOW] Unable to terminate service. Manual intervention required." >> $STOP_LOG
        fi
    fi
fi

echo "[$NOW] stop.sh completed." >> $STOP_LOG
