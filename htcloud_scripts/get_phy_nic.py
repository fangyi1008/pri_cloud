import os
import sys

def usage():
    print("Usage:"+format(sys.argv[0])+" [nic_name]")
    print("description:")
    print("    Get physical network card information. If there are no parameters, all network card information will be output.\n")
    print("    -h for help\n")
    print("example:")
    print("    python3 get_phy_nic.python eth0")
    print("    python3 get_phy_nic.python")

def read_file(file_name):
    nic_state_file = open(file_name)
    try:
        file_str = nic_state_file.read()
    finally:
        nic_state_file.close()

    return file_str

def get_info_by_name(nic_name):
       
    res = os.popen("ls -l /sys/class/net/" + nic_name).readlines()
    if len(res) == 0:
        exit(1)
    nic_res_list = res[0].split(' ')
    nic_path_pci_1 = nic_res_list[-1].split('/')[3]
    nic_path_pci_2 = nic_res_list[-1].split('/')[4]
    nic_path_pci_3 = nic_res_list[-1].split('/')[5]
    
    nic_pci_num_list = nic_res_list[-1].split('/')[5].split(':')
    nic_pci_num = nic_pci_num_list[1]+":"+nic_pci_num_list[2]
    nic_pci_list = os.popen("lspci | grep "+nic_pci_num).readlines()
           
    nic_path = "/sys/devices/" + nic_path_pci_1 + "/" + nic_path_pci_2 + "/" + nic_path_pci_3 + "/"
    
    nic_state_int = read_file(nic_path + "net/" + nic_name + "/carrier").strip()
    if nic_state_int == "0":
        nic_state = "inactive"
    elif nic_state_int == "1":
        nic_state = "active"
    else:
        nic_state = 'unknown'

    nic_mac = read_file(nic_path + "net/" + nic_name + "/address").strip()
    nic_speed = read_file(nic_path + "net/" + nic_name + "/speed").strip()
    nic_mtu = read_file(nic_path + "net/" + nic_name + "/mtu").strip()
    nic_model = nic_pci_list[0].split(":")[2].strip()
    nic_numa_node = read_file(nic_path + "numa_node").strip()
    json_code = '{"name":"' + nic_name +'","model":"' + nic_model +'","mac":"' + nic_mac +'", "speed":"' + nic_speed +'","state":"' + nic_state +'","mtu":"' + nic_mtu +'","numa_node":"' + nic_numa_node +'","pci":"' + nic_path_pci_3 +'"}'

    return json_code


if __name__=='__main__':
    if len(sys.argv) == 1:
        res = os.popen("ls /sys/class/net/ | grep eth").readlines()
        nics_info = list()
        for nic in res:
            nic=nic.strip()
            os.popen("ifconfig " + nic + " up")
            json_code = get_info_by_name(nic)
            nics_info.append(json_code)
        print(nics_info)
    else:
        nic_name = sys.argv[1]
        if nic_name == "-h":
            usage()
            exit(0)
        os.popen("ifconfig " + nic_name + " up")
        
        print(get_info_by_name(sys.argv[1]))
