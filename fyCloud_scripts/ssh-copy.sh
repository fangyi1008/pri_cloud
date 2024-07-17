#!/bin/bash
if [ ! -f /root/.ssh/id_rsa ];then
ssh-keygen -t rsa -b 4096 -N "" -f /root/.ssh/id_rsa -q
fi
sed -i "/$1/d" /root/.ssh/known_hosts
expect /fyCloud/scripts/ssh-copy $1 $2
