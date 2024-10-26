#!/bin/bash

# Define variables
ROOT_PATH="/home/ec2-user/spring-github-action"

# Ensure the deployment directory exists
if [ ! -d "$ROOT_PATH" ]; then
    echo "Creating deployment directory: $ROOT_PATH"
    mkdir -p "$ROOT_PATH"
    chown ec2-user:ec2-user "$ROOT_PATH"
fi

# Remove old files if necessary
rm -rf "$ROOT_PATH"/*

# Copy the new files from the current directory (where CodeDeploy unzipped them)
cp -r . "$ROOT_PATH"

echo "before_install.sh completed."
