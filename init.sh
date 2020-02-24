#!/usr/bin/bash

echo "Initializing environment..."

# Create the centos group
groupadd -g 1001 centos

# Create the centos user to match live environments
useradd -s /bin/bash -u 1001 -g centos centos

# Useradd wont create home dir since rsynced files already exist there.
# Manually copy /etc/skel files instead 
cp -r /etc/skel/. /home/centos

# install Extra Packages for Enterprise Linux (EPEL)
yum install -y https://dl.fedoraproject.org/pub/epel/epel-release-latest-7.noarch.rpm

# Install ansible locally
yum update -y
yum install -y ansible

