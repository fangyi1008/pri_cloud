#facility
ipmitool sdr > ipmitool-sdr.log
cpu0_status=$(cat ipmitool-sdr.log |grep CPU0_Status |awk -F" " '{printf "%s %s\n", $3,$5}') #第一个CPU状态
cpu1_status=$(cat ipmitool-sdr.log |grep CPU1_Status |awk -F" " '{printf "%s %s\n", $3,$5}') #第二个CPU状态
memory_status=$(cat ipmitool-sdr.log |grep Memory |awk -F" " '{printf "%s %s %s\n",$3,$4,$6}') #内存状态
cpu0_temp=$(cat ipmitool-sdr.log |grep CPU0_Temp |awk -F" " '{printf "%d %s %s %s\n",$3,$4,$5,$7}') #第一个CPU温度
cpu1_temp=$(cat ipmitool-sdr.log |grep CPU1_Temp |awk -F" " '{printf "%d %s %s %s\n",$3,$4,$5,$7}') #第二个CPU温度
power_status=$(cat ipmitool-sdr.log |grep Sys_Total_Power |awk -F" " '{printf "%d %s %s\n",$3,$4,$6}') #电源状态
fan1_status=$(cat ipmitool-sdr.log |grep FAN1_Present |awk -F" " '{printf "%s %s\n",$3,$5}') #第一个风扇状态
fan2_status=$(cat ipmitool-sdr.log |grep FAN2_Present |awk -F" " '{printf "%s %s\n",$3,$5}') #第二个风扇状态
fan3_status=$(cat ipmitool-sdr.log |grep FAN3_Present |awk -F" " '{printf "%s %s\n",$3,$5}') #第三个风扇状态
fan4_status=$(cat ipmitool-sdr.log |grep FAN4_Present |awk -F" " '{printf "%s %s\n",$3,$5}') #第四个风扇状态
#system
system_manufacture=$(dmidecode -s system-manufacturer) #系统厂商
system_product_name=$(dmidecode -s system-product-name) #系统产品名称
system_serial_number=$(dmidecode -s system-serial-number) #系统序列号
system_uuid=$(dmidecode -s system-uuid)     #系统uuid
system_status=$(dmidecode -t system | grep Status | grep -v "," | awk -F":" '{print $2}') #系统状态
#BIOS
bios_vendor=$(dmidecode -s bios-vendor)  #BIOS供应商
bios_version=$(dmidecode -s bios-version) #BIOS版本
bios_release_date=$(dmidecode -s bios-release-date) #BIOS出厂日期
bios_rom_size=$(dmidecode -t bios | grep "ROM Size" | awk -F" " '{print $3 $4}') #BIOS只读存储大小
#
json_code="{\"facility:\"{\"cpu0_status\":\"$cpu0_status\",\"cpu1_status\":\"$cpu1_status\",\"memory_status\":\"$memory_status\",\"cpu0_temp\":\"$cpu0_temp\",\"cpu1_temp\":\"$cpu1_temp\",\"power_status\":\"$power_status\",
						\"fan1_status\":\"$fan1_status\",\"fan2_status\":\"$fan2_status\",\"fan3_status\":\"$fan3_status\",\"fan4_status\":\"$fan4_status\"},
			\"system:\"{\"system_manufacture\":\"$system_product_name\",\"system_product_name\":\"$system_product_name\",\"system_serial_number\":\"$system_serial_number\",\"system_uuid\":\"$system_uuid\",\"system_status\":\"$system_status\"},
			\"BIOS:\"{\"bios_vendor\":\"$bios_vendor\",\"bios_version\":\"$bios_version\",\"bios_release_date\":\"$bios_release_date\",\"bios_rom_size\":\"$bios_rom_size\"}}"
echo $json_code | tr -d '\r' | tr -d '\n'
echo