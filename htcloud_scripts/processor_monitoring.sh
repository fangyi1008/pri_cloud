#!/bin/bash
cpu1_type=$(dmidecode -s processor-version |awk 'NR==1 {print}')		#处理器1类型
cpu2_type=$(dmidecode -s processor-version |awk 'NR==2 {print}')		#处理器2类型
cpu1_status=$(dmidecode -t processor |grep Status|awk -F" " 'NR==1 {printf "%s %s\n",$2,$3}')	#处理器1状态
cpu2_status=$(dmidecode -t processor |grep Status|awk -F" " 'NR==2 {printf "%s %s\n",$2,$3}')	#处理器2状态
cpu_architecture=$(lscpu |grep Architecture |awk -F" " '{print $2}') #处理器结构
cpu_memory=$(lscpu |grep "CPU op-mode" |awk -F" " '{printf "%s %s\n",$3,$4}') #内存技术
cpu1_voltage=$(dmidecode -t processor |grep Voltage |awk -F" " 'NR==1 {printf "%.1f %s\n",$2,$3}')	#处理器1电压
cpu2_voltage=$(dmidecode -t processor |grep Voltage |awk -F" " 'NR==2 {printf "%.1f %s\n",$2,$3}')	#处理器2电压
cpu1_max_speed=$(dmidecode -t processor |grep "Max Speed" |awk -F" " 'NR==1 {printf "%d %s\n",$3,$4}') 	#处理器1最大频率
cpu2_max_speed=$(dmidecode -t processor |grep "Max Speed" |awk -F" " 'NR==2 {printf "%d %s\n",$3,$4}') 	#处理器2最大频率
cpu1_cur_speed=$(dmidecode -t processor |grep "Current Speed" |awk -F" " 'NR==1 {printf "%d %s\n",$3,$4}') 	#处理器1当前频率
cpu2_cur_speed=$(dmidecode -t processor |grep "Current Speed" |awk -F" " 'NR==2 {printf "%d %s\n",$3,$4}') 	#处理器2当前频率
cpu_sockets=$(lscpu |grep Socket | awk -F" " '{print $2}')	#处理器物理核数
cpu_threads=$(lscpu |grep Socket | awk -F" " '{print $2}')	#处理器每个核的线程数
cpu1_cache1=$(dmidecode -t processor |grep "L1 Cache" |awk -F" " 'NR==1 {print $4}')	#处理器1的一级缓存
cpu1_cache2=$(dmidecode -t processor |grep "L2 Cache" |awk -F" " 'NR==1 {print $4}')	#处理器1的二级缓存
cpu1_cache3=$(dmidecode -t processor |grep "L3 Cache" |awk -F" " 'NR==1 {print $4}')	#处理器1的三级缓存
cpu2_cache1=$(dmidecode -t processor |grep "L1 Cache" |awk -F" " 'NR==2 {print $4}')	#处理器2的一级缓存
cpu2_cache2=$(dmidecode -t processor |grep "L2 Cache" |awk -F" " 'NR==2 {print $4}')	#处理器2的二级缓存
cpu2_cache3=$(dmidecode -t processor |grep "L3 Cache" |awk -F" " 'NR==2 {print $4}')	#处理器2的三级缓存

json_code="{\"cpu1_type\":\"$cpu1_type\",\"cpu2_type\":\"$cpu2_type\",\"cpu1_status\":\"$cpu1_status\",\"cpu2_status\":\"$cpu2_status\",\"cpu_architecture\":\"$cpu_architecture\",\"cpu_memory\":\"$cpu_memory\",
			\"cpu1_voltage\":\"$cpu1_voltage\",\"cpu2_voltage\":\"$cpu2_voltage\",\"cpu1_max_speed\":\"$cpu1_max_speed\",\"cpu2_max_speed\":\"$cpu2_max_speed\",\"cpu1_cur_speed\":\"$cpu1_cur_speed\",\"cpu2_cur_speed\":\"$cpu2_cur_speed\",
			\"cpu_sockets\":\"$cpu_sockets\",\"cpu_threads\":\"$cpu_threads\",\"cpu1_cache1\":\"$cpu1_cache1\",\"cpu1_cache2\":\"$cpu1_cache2\",\"cpu1_cache3\":\"$cpu1_cache3\",\"cpu2_cache1\":\"$cpu2_cache1\",\"cpu2_cache2\":\"$cpu2_cache2\",\"cpu2_cache3\":\"$cpu2_cache3\"}"
echo $json_code | tr -d '\r' | tr -d '\n'
echo 
