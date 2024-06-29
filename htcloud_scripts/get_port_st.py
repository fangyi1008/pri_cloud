import os
import sys
import json

def read_file(file_name):
    nic_state_file = open(file_name)
    try:
        file_str = nic_state_file.read()
    finally:
        nic_state_file.close()

    return file_str

def get_info_by_name(vnet_name):

    rx_bytes = read_file("/sys/class/net/"+vnet_name+"/statistics/rx_bytes").strip()
    rx_packets = read_file("/sys/class/net/"+vnet_name+"/statistics/rx_packets").strip()
    rx_errors = read_file("/sys/class/net/"+vnet_name+"/statistics/rx_errors").strip()
    tx_bytes = read_file("/sys/class/net/"+vnet_name+"/statistics/tx_bytes").strip()
    tx_packets = read_file("/sys/class/net/"+vnet_name+"/statistics/tx_packets").strip()
    tx_errors = read_file("/sys/class/net/"+vnet_name+"/statistics/tx_errors").strip()

    #json_code = dict()
    #json_code["rx_bytes"]=rx_bytes
    #json_code["rx_packets"]=rx_packets
    #json_code["rx_errors"]=rx_errors
    #json_code["tx_bytes"]=tx_bytes
    #json_code["tx_packets"]=tx_packets
    #json_code["tx_errors"]=tx_errors

    json_code = '{"rx_bytes":"' + rx_bytes +'","rx_packets":"' + rx_packets +'","rx_errors":"' + rx_errors +'", "tx_bytes":"' + tx_bytes +'","tx_packets":"' + tx_packets +'","tx_errors":"' + tx_errors +'",'

    return json_code

if __name__=='__main__':
        
    ports = os.popen("ovs-vsctl list-ports "+sys.argv[1]+" | grep vnet").readlines()

    port_list = list()
    for port in ports:
        port_name = port.strip()
        #print(vm_id_cmd)
        vm_id = os.popen("ovs-vsctl list interface " + port_name + " | grep vm-id | cut -d \",\" -f 4 | cut -d \'\"\' -f 2").readline()
        vm_name = os.popen("virsh domname " + vm_id).readline().strip()
        vnet_json=get_info_by_name(port_name)
        vnet_json=vnet_json+'"vm_name":"'+vm_name+'","name":"'+port_name+'"}'
        #vnet_json["vm_name"]=vm_name
        #vnet_json["name"]=port_name
        port_list.append(vnet_json)

    print(port_list)
