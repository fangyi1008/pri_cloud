no_mount_disk=$(lsblk -r --output NAME,MOUNTPOINT | awk -F \/ '/sd/ { dsk=substr($1,1,3);dsks[dsk]+=1 } END { for ( i in dsks ) { if (dsks[i]==1) print i } }')
#echo $no_mount_disk
empty_disk=""
for disk in $no_mount_disk
do
    mpath=$(multipath -l /dev/$disk | head -1 | cut -d " " -f 1)
    if [ $mpath"xxx" != "xxx" ];then
        mount /dev/mapper/$mpath /mnt 2> /dev/null
        if [ $? != 0 ];then
            empty_disk=$empty_disk" /dev/mapper/$mpath"
        else
            umount /mnt 2> /dev/null
        fi
    else
        mount /dev/$disk /mnt 2> /dev/null
	if [ $? != 0 ] && !(pvs | grep -q /dev/$disk);then
            empty_disk=$empty_disk" /dev/$disk"
        else
            umount /mnt 2> /dev/null
        fi
    fi
done
echo $empty_disk
