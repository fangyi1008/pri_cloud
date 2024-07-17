function usage {
    echo "Usage:"
    echo "description:"
    echo "    FC Storage pool related operations.\n"
    echo "example:"
    echo "    fc_pool.sh"
}
fc_host=$(ls /sys/class/fc_host)
port_list="["
for fc in $fc_host
do
state=$(cat /sys/class/fc_host/$fc/port_state)
wwn=$(cat /sys/class/fc_host/$fc/port_name)
port_list=$port_list"{\"port_name\":\"$wwn\",\"state\":\"$state\"},"
done
port_list=$port_list"]"

echo $port_list
