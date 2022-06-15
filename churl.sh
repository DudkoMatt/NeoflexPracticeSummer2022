proto=$(echo "$1" | sed -e's,^\(.*://\).*,\1,g')
url=$(echo "$1" | sed -e "s,$proto,,g")
userpassword=$(echo "$url" | grep @ | cut -d@ -f1)
user=$(echo "$userpassword" | cut -d: -f1)
password=$(echo "$userpassword" | cut -d: -f2)
hostport=$(echo "$url" | sed -e "s,$userpassword@,,g" | cut -d/ -f1)
host=$(echo "$hostport" | sed -e 's,:.*,,g')
port=$(echo "$hostport" | sed -e 's,^.*:,:,g' -e 's,.*:\([0-9]*\).*,\1,g' -e 's,[^0-9],,g')
path=$(echo "$url" | grep '/' | cut -d/ -f2-)

echo "jdbc:postgresql://${host}:${port}/${path}?user=${user}&password=${password}&sslmode=require ${user} ${password}"
