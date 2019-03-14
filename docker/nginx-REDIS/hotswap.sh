# Set environment
set -e

# Check that parameter was passed
if [ -z "$1" ]; then
    echo "No parameter passed to script"
    exit 1
fi

# Location of nginx.conf file
config="/etc/nginx/nginx.conf"

# Get the current redis url on line 11 of config file:
lineLocationURL=$(sed -n  '11p' $config)

# Extract content before :6379 port number
# Server 34.73.56.224:6379;
beforePort=${lineLocationURL%:6379*}

# Extract host name after server text, 'server host'
host=$(echo $beforePort | awk -F 'server' '{print $2}')

# Trim whitespaces from host name after extracting
hostName=$(echo ${host//[[:space:]]})

# Current Hostname
echo "Current Host: $hostName"

# If param doesn't match host name, hotswap and replace host name with param
# Reload nginx after swapping hostname
if [ "$1" != "$hostName" ]; then
	echo "Swapping Hostnames"
	sed -i "s/server.*:/server $1:/g" $config
	/usr/sbin/nginx -s reload
else
	echo "Error. Please use a different hostname."
fi
