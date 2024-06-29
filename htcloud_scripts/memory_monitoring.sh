#!/bin/bash
#brief info
brief_location=$(dmidecode -t memory |grep Location |awk -F":" '{print $2}')	#位置
brief_num=$(dmidecode -t memory |grep "Number Of Devices" |awk -F":" '{print $2}') #插槽数量
brief_capacity=$(dmidecode -t memory |grep "Maximum Capacity" |awk -F":" '{print $2}') #总内存
brief_speed=$(dmidecode -t memory |grep "Configured Memory Speed" |awk -F":" 'NR==1 {print $2}')	#运行频率
#detailed info
json_code="{\"brief\":
			{\"brief_location\":\"$brief_location\",\"brief_num\":\"$brief_num\",\"brief_capacity\":\"$brief_capacity\",\"brief_speed\":\"$brief_speed\"},
			\"detail\":["

detail_status=$(ipmitool sdr |grep Memory |awk -F" " '{printf "%s %s %s\n",$3,$4,$6}') #状态
count=$(dmidecode -t memory |grep -v Bank|grep -c Locator)
for (( i = 1;i <= $count;i++ ))
do
	detail_location=$(dmidecode -t memory |grep Locator |grep -v Bank |awk -v i="$i" -F":" 'NR==i {print $2}')
	detail_slot=$(dmidecode -t memory |grep "Bank Locator" |awk -v i="$i" -F":" 'NR==i {print $2}')
	detail_size=$(dmidecode -t memory |grep ^[[:space:]]Size |awk -v i="$i" -F":" 'NR==i {print $2}')
	detail_model=$(dmidecode -t memory |grep "Memory Technology" |awk -v i="$i" -F":" 'NR==i {print $2}')
	detail_speed=$(dmidecode -t memory |grep ^[[:space:]]Speed |awk -v i="$i" -F":" 'NR==i {print $2}')
	detail_tag=$(dmidecode -t memory |grep "Asset Tag" |awk -v i="$i" -F":" 'NR==i {print $2}')
	detail_manufacturer=$(dmidecode -t memory |grep ^[[:space:]]Manufacturer |awk -v i="$i" -F":" 'NR==i {print $2}')
	detail_json="{\"detail_location\":\"$detail_location\",\"detail_slot\":\"$detail_slot\",\"detail_status\":\"$detail_status\",\"detail_size\",\"$detail_size\",
				  \"detail_model\":\"$detail_model\",\"detail_speed\":\"$detail_speed\",\"detail_tag\":\"$detail_tag\",\"detail_manufacturer\":\"$detail_manufacturer\"}"
	if [ $i -lt $count ]
	then
		json_code=$json_code$detail_json","
	else
		json_code=$json_code$detail_json
	fi
done

json_code=$json_code"]}"
echo $json_code | tr -d '\r' | tr -d '\n'
echo
