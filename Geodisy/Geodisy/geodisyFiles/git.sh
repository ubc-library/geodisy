String callAdd = "cd " + $1 + " && git add . && git commit -m '" + $2 + "' && git push"
sudo su - root -c callAdd
