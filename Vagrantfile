# -*- mode: ruby -*-
# vi: set ft=ruby :

# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.
Vagrant.configure("2") do |config|
  # The most common configuration options are documented and commented below.
  # For a complete reference, please see the online documentation at
  # https://docs.vagrantup.com.

  # Every Vagrant development environment requires a box. You can search for
  # boxes at https://vagrantcloud.com/search.
  config.vm.box = "centos/7"

  # Disable automatic box update checking. If you disable this, then
  # boxes will only be checked for updates when the user runs
  # `vagrant box outdated`. This is not recommended.
  # config.vm.box_check_update = false

  # Suggested configuration for speeding up guest network
  config.vm.provider "virtualbox" do |v|
     v.customize ["modifyvm", :id, "--natdnsproxy1", "on"]
  end

  # Create a forwarded port mapping which allows access to a specific port
  # within the machine from a port on the host machine. In the example below,
  # accessing "localhost:8080" will access port 80 on the guest machine.
  # NOTE: This will enable public access to the opened port
  config.vm.network "forwarded_port", guest: 8080, host: 8600, host_ip: "0.0.0.0"
  config.vm.network "forwarded_port", guest: 3000, host: 3000, host_ip: "0.0.0.0"
  config.vm.network "forwarded_port", guest: 8983, host: 8983, host_ip: "0.0.0.0"

  # Create a forwarded port mapping which allows access to a specific port
  # within the machine from a port on the host machine and only allow access
  # via 127.0.0.1 to disable public access
  # config.vm.network "forwarded_port", guest: 80, host: 8080, host_ip: "127.0.0.1"

  # Create a private network, which allows host-only access to the machine
  # using a specific IP.
  # config.vm.network "private_network", ip: "192.168.33.10"

  # Create a public network, which generally matched to bridged network.
  # Bridged networks make the machine appear as another physical device on
  # your network.
  # config.vm.network "public_network"

  # Share an additional folder to the guest VM. The first argument is
  # the path on the host to the actual folder. The second argument is
  # the path on the guest to mount the folder. And the optional third
  # argument is a set of non-required options.
  config.vm.synced_folder ".", "/vagrant", type: "rsync"

  # Assign predictable UID and GID values. 'synced_folder' directive will complain
  # on first run since init.sh provisioning script has not run yet to create the user/group
  CENTOS_UID = 1001
  CENTOS_GID = 1001

  # Copy Geodisy code into home folder of centos user
  config.vm.synced_folder "./Geodisy", "/home/centos", type: "rsync", rsync__auto: true, owner: CENTOS_UID, group: CENTOS_GID 
  
  # Provider-specific configuration so you can fine-tune various
  # backing providers for Vagrant. These expose provider-specific options.
  # Example for VirtualBox:
  #
  config.vm.provider "virtualbox" do |vb|
    # Display the VirtualBox GUI when booting the machine
    #vb.gui = true
    #   # Customize the amount of memory on the VM:
    vb.memory = "2048"
  end
  #
  # View the documentation for the provider you are using for more
  # information on available options.

  # Enable provisioning with a shell script. Additional provisioners such as
  # Puppet, Chef, Ansible, Salt, and Docker are also available. Please see the
  # documentation for more information about their specific syntax and use.
  # config.vm.provision "shell", inline: <<-SHELL
  #   apt-get update
  #   apt-get install -y apache2
  # SHELL

  # Shell provisioner to do environment init
  config.vm.provision "shell", type: "shell" do |s|
    s.path = './init.sh'
    s.privileged = true
  end      

  # Frontend Ansible Playbook
  config.vm.provision "ansible", type: "ansible_local" do |a|
    a.playbook = "provisioning/site.yml" 
    a.inventory_path = "provisioning/local-ini"
    a.limit = "all"
    a.raw_arguments = ["--connection=local", "--diff"]
  end

  # Backend Ansible Playbook
  #config.vm.provision "ansible", type: "ansible_local" do |a|
  #  a.playbook = "provisioning/backend.yml" 
  #end
end
