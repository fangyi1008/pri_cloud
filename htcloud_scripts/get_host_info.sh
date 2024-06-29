host_model=$(dmidecode -s system-product-name) #主机型号
cpu_sockets=$(lscpu |grep Socket | awk -F" " '{print $2}')	#CPU插槽数量
cpu_cores=$(lscpu |grep Core | awk -F" " '{print $4}')   #每个CPU核数
cpu_threads=$(lscpu |grep Socket | awk -F" " '{print $2}') #每个核线程数
cpu_model=$(lscpu |grep "Model name" |grep -v "BIOS" |awk -F" " '{print $3" "$4" "$5" "$6" "$7}') #CPU型号
cpu_frequency=$(lscpu |grep "CPU MHz" |awk -F" " '{print $3}') #CPU主频
cpu_utilization=$(top -bn1 | grep load | awk '{printf "%.2f\n", $(NF-2)}')
mem_total=$(free -h |grep Mem |awk -F" " '{print $2}')  #内存大小
mem_free=$(free -h |grep Mem |awk -F" " '{print $4}') #空闲内存
mem_utilization=$(free -m | awk 'NR==2{printf "%0.2f\n", $3*100/$2 }') #内存分配比
disk_total=$(df -h |awk -F" " '$6=="/" {print $2}') #本地存储
disk_free=$(df -h |awk -F" " '$6=="/" {print $4}') #本地空闲存储
disk_utilization=$(df -h |awk -F" " '$6=="/" {print $5}') #存储利用率
host_time=$(date "+%Y-%m-%d %H:%M:%S")		#主机时间
tmp=$(uptime -p)
host_run_time=${tmp:3}  #运行时间
#
#输出
json_code="{\"host_model\":\"$host_model\",\"host_time\":\"$host_time\",\"host_run_time\":\"$host_run_time\",
			\"cpu_sockets\":\"$cpu_sockets\",\"cpu_cores\":\"$cpu_cores\",\"cpu_model\":\"$cpu_model\",\"cpu_utilization\":\"$cpu_utilization\",\"cpu_frequency\":\"$cpu_frequency\",\"cpu_threads\":\"$cpu_threads\",
			\"mem_total\":\"$mem_total\",\"mem_free\":\"$mem_free\",\"mem_utilization\":\"$mem_utilization\",
			\"disk_total\":\"$disk_total\",\"disk_free\":\"$disk_free\",\"disk_utilization\":\"$disk_utilization\"}"
echo $json_code | tr -d '\r' | tr -d '\n'
echo
