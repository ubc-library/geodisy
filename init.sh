#!/usr/bin/bash

echo "Initializing environment..."

# Create the centos user to match live environments
#useradd -m -s /bin/bash -U centos --groups wheel

# install Extra Packages for Enterprise Linux (EPEL)
yum install -y https://dl.fedoraproject.org/pub/epel/epel-release-latest-7.noarch.rpm

# Install ansible locally
yum update -y
yum install -y ansible

