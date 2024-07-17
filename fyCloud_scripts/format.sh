if [ $disk_format"xxx" != "xxx" ];then
    iscsi_path=$1
    disk_format=$2
    disk=$(readlink $iscsi_path | cut -d '/' -f 3)
    mpath=$(multipath -l /dev/$disk | head -1 | cut -d " " -f 1)
    echo y | mkfs.$disk_format /dev/mapper/$mpath > /dev/null 
    echo $?
    echo "/dev/mapper/$mpath"
else
    iscsi_path=$1
    iscsi_path_array=(${iscsi_path//,/ })
    mpath_disk_list=""

    for var in ${iscsi_path_array[@]}
    do
        #echo $var
        disk=$(readlink $var | cut -d '/' -f 3)
        mpath=$(multipath -l /dev/$disk | head -1 | cut -d " " -f 1)
	mpath_disk_size=$(lsblk | grep $mpath | tr -s " " | cut -d " " -f 4)
	mpath_disk_list=$mpath_disk_list",/dev/mapper/$mpath($mpath_disk_size)"
    done
    echo $mpath_disk_list
fi
