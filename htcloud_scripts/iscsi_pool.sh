function usage {
    echo "Usage:"
    echo "description:"
    echo "    Storage pool related operations.\n"
    echo "    -h for help\n"
    echo "example:"
    echo "    iscsi_pool.sh discovery 192.168.0.1:3260"
    echo "    iscsi_pool.sh session"
    echo "    iscsi_pool.sh login iqn 192.168.0.1"
    echo "    iscsi_pool.sh logout iqn 192.168.0.1"
}
if [ $# -eq 0 ];then
    usage
    exit 1
fi
if [ $1 == "discovery" ];then
    iscsiadm -m discovery -t sendtargets -p $2 | grep $2 | cut -d " " -f 2 
elif [ $1 == "session" ];then
    iscsiadm -m session
elif [ $1 == "login" ];then
    iscsiadm -m node -T $2 -p $3 -l
elif [ $1 == "logout" ];then
    iscsiadm -m node -T $2 -p $3 -u
fi
