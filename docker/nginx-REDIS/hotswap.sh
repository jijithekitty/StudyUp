#set environment
set -e

#Check that PARAMS was passed
if [ -z "$1" ]; then
    echo "No parameter passed to script"
    exit 1
fi

#location of nginx.conf file
config="/etc/nginx/nginx.conf"

#Get the current redis url on line 11 of config file:
lineLocationURL=$(sed -n  '11p' $config)

#extract content before :6379 port number
#server 34.73.56.224:6379;
beforePort=$(lineLocationURL%:6379*)

#store server name, removing server from "server name"
hostName=${beforePort:11}

#if param doesn't match host name, hotswap and replace host name with param
#reload nginx after swapping hostname
if ["$1" != hostName]; then
	echo "Swapping hostnames"
	sed -i "s/server.*:/server $1:/g" $config
	/usr/sbin/nginx -s reload
else
	echo "error, host name is already set to param"
fi
