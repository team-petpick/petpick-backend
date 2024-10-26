#!/bin/bash

# Define variables
ROOT_PATH="/home/ec2-user/spring-github-action"
ZIP_FILE="$ROOT_PATH/deployment.zip"

# Ensure the deployment directory exists
if [ ! -d "$ROOT_PATH" ]; then
    echo "Creating deployment directory: $ROOT_PATH"
    mkdir -p "$ROOT_PATH"
    chown ec2-user:ec2-user "$ROOT_PATH"
fi

# Move to the deployment directory
cd "$ROOT_PATH" || exit 1

# Unzip the deployment package
echo "Unzipping deployment package: $ZIP_FILE"
if [ -f "$ZIP_FILE" ]; then
    unzip -o "$ZIP_FILE" -d "$ROOT_PATH"
    if [ $? -ne 0 ]; then
        echo "Failed to unzip deployment package."
        exit 1
    else
        echo "Deployment package unzipped successfully."
    fi
else
    echo "Deployment package $ZIP_FILE does not exist."
    exit 1
fi

# Optional: Clean up the zip file after extraction
rm -f "$ZIP_FILE"

echo "before_install.sh completed."
