#!/bin/bash

# Install Nginx if it's not already installed
if ! command -v nginx > /dev/null 2>&1; then
    echo "Installing Nginx..."
    sudo yum install -y nginx
    sudo systemctl start nginx
    sudo systemctl enable nginx
else
    echo "Nginx is already installed."
fi

# Check if Nginx is running
if systemctl status nginx > /dev/null 2>&1; then
    echo "Nginx is running."
else
    echo "Nginx is not running. Starting Nginx..."
    sudo systemctl start nginx
fi

# Optional: Add firewall rules for HTTP/HTTPS traffic if needed
sudo firewall-cmd --permanent --add-service=http
sudo firewall-cmd --permanent --add-service=https
sudo firewall-cmd --reload

echo "Nginx setup completed."
