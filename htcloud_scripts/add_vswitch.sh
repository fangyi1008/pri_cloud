#!/bin/sh
if [  "X$1" != "X" ];then
    name=$1
fi
bridge=$(ovs-vsctl list-br  | grep $name)
if [ "$bridge" = "$name" ];then
    echo "$curtime $0:bridge $name is exist" >> /var/log/htcloud.log
    exit 1
else
    ovs-vsctl add-br $name
fi

if [  "X$2" != "X" ];then
    ip=$2
    exec_cmd="ifconfig $name $ip"
fi
if [  "X$3" != "X" ] && [ "X$ip" != "X" ];then
    netmask=$3
    exec_cmd=$exec_cmd" netmask $netmask"
    $exec_cmd
fi
if [  "X$4" != "X" ];then
    gw=$4
    route add default gw $gw
fi
exit 0
