#!/bin/bash

#cpu使用率
cpu_use_ratio=$(iostat -c |grep -A1 avg-cpu |grep -v avg-cpu |awk -F" " '{print $3}')
#内存使用率
mem_use_ratio=$(free -m | awk 'NR==2{printf "%0.2f\n", $3*100/$2 }')

json_code="{\"cpu_use_ratio\":\"$cpu_use_ratio\",
			\"mem_use_ratio\":\"$mem_use_ratio\",
		    \"disk_io\":{"

#disk_io
count=$(iostat |grep -A20 "Device" |grep -v "^$"|grep -vc "Device")
for (( i = 1;i <= $count;i++ ))
do
	partition_name=$(iostat -x|grep -A20 "Device" |grep -v "^$"|grep -v "Device" |awk -v i="$i" -F" " 'NR==i {print $1}')
	#io吞吐率
	rkB=$(iostat -x|grep -A20 "Device" |grep -v "^$"|grep -v "Device" |awk -v i="$i" -F" " 'NR==i {print $4}')
	wkB=$(iostat -x|grep -A20 "Device" |grep -v "^$"|grep -v "Device" |awk -v i="$i" -F" " 'NR==i {print $5}')
	#磁盘请求
	rrqm=$(iostat -x|grep -A20 "Device" |grep -v "^$"|grep -v "Device" |awk -v i="$i" -F" " 'NR==i {print $6}')
	wrqm=$(iostat -x|grep -A20 "Device" |grep -v "^$"|grep -v "Device" |awk -v i="$i" -F" " 'NR==i {print $7}')
	#磁盘I/O延迟
	r_await=$(iostat -x|grep -A20 "Device" |grep -v "^$"|grep -v "Device" |awk -v i="$i" -F" " 'NR==i {print $10}')
	w_await=$(iostat -x|grep -A20 "Device" |grep -v "^$"|grep -v "Device" |awk -v i="$i" -F" " 'NR==i {print $11}')
	
	diskio_code="\"$partition_name\":
				{
					\"diskio_throughput\":
					{
						\"rkB/s\":\"$rkB\",
						\"wkB/s\":\"$wkB\"
					},
					\"diskio_iops\":
					{
						\"rrqm/s\":\"$rrqm\",
						\"wrqm/s\":\"$wrqm\"
					},
					\"disk_io_delay\":
					{
						\"r_await\":\"$r_await ms\",
						\"w_await\":\"$w_await ms\"
					}
				}"
	if [ $i -lt $count ]
	then
		json_code=$json_code$diskio_code","
	else
		json_code=$json_code$diskio_code"}"
	fi
done

#磁盘利用率
count=$(df |grep -c sda)
sda_total=0 #磁盘总量
sda_used=0  #磁盘使用总量
for (( i = 1;i <= $count;i++ ))
do
	total=$(df |grep sda |awk -v i="$i" -F" " 'NR==i {print $2}')
	let sda_total+=total
	used=$(df |grep sda |awk -v i="$i" -F" " 'NR==i {print $3}')
	let sda_used+=used
done
disk_utilization=$(awk -v used="$sda_used" -v total="$sda_total" 'BEGIN{printf "%.2f\n",used/total}')

json_code=$json_code",\"disk_utilization\":\"$disk_utilization\",
		  \"part_utilization\":{"
		  
#分区占用率
count=$(df -Th |grep sda |grep -vc Filesystem)
for (( i = 1;i <= $count;i++ ))
do
	filesystem_name=$(df -Th |grep sda |grep -v Filesystem |awk -v i="$i" -F" " 'NR==i {print $1}')
	type=$(df -Th |grep sda |grep -v Filesystem |awk -v i="$i" -F" " 'NR==i {print $2}')
	mount=$(df -Th |grep sda |grep -v Filesystem |awk -v i="$i" -F" " 'NR==i {print $7}')
	size=$(df -Th |grep sda |grep -v Filesystem |awk -v i="$i" -F" " 'NR==i {print $3}')
	used=$(df -Th |grep sda |grep -v Filesystem |awk -v i="$i" -F" " 'NR==i {print $4}')
	use_rate=$(df -Th |grep sda |grep -v Filesystem |awk -v i="$i" -F" " 'NR==i {print $6}')
	part_code="\"$filesystem_name\":
				{
				\"type\":\"$type\",
				\"mount\":\"$mount\",
				\"size\":\"$size\",
				\"used\":$used\",
				\"use_rate\":\"$use_rate\"
				}"
	
	if [ $i -lt $count ]
	then
		json_code=$json_code$part_code","
	else
		json_code=$json_code$part_code"}"
	fi
done
json_code=$json_code"}"

echo $json_code | tr -d '\r' | tr -d '\n'
echo
