#!/usr/bin/bash

echo "Initializing environment..."

# Create the centos user to match live environments
#useradd -m -s /bin/bash -U centos --groups wheel

# Install ansible locally
yum update
yum install -y ansible
