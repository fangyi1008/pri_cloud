import os
import sys
import xml.etree.ElementTree as ET


def usage():
    print("usage: openflow.py command xmlfile [port_name]")
    print("description:")
    print("    Deploy or Delete Safety rules.\n")
    print("    -h for help\n")
    print("example:")
    print("    openflow.py deploy openflow.xml [vnet0]")
#    print("    openflow.py update openflow.xml [vnet0]")
    print("    openflow.py delete openflow.xml [vnet0]")
   
nw_proto = {
    "icmp":"1",
    "tcp":"6",
    "udp":"17"
}

def joint_flow(port_name, port_mac, rule):
    direction = rule.get("direction")
    protocol = rule.get("protocol")
    action = rule.get("action")
    flow=""
    if direction == "1": #入方向流量
        flow=flow+",dl_dst="+port_mac
    elif direction == "2": #出方向流量
        flow=flow+",dl_src="+port_mac
    elif direction == "3": #双向流量
        pass
    else:
        print("error direction.")
        return None

    flow=flow+",dl_type=0x0800,nw_proto="+nw_proto[protocol]
    if rule.find('source_ip'):
        flow=flow+",nw_src="+rule.get("source_ip")
    if rule.find('source_mask'):
        flow=flow+"/"+rule.get("source_mask")
    if rule.find('dest_ip'):
        flow=flow+"nw_dst="+rule.get("dest_ip")
    if rule.find('dest_mask'):
        flow=flow+"/"+rule.get("dest_mask")
    if rule.find('source_port'):
        flow=flow+"tp_src="+rule.get("source_port")
    if rule.find('dest_port'):
        flow=flow+",tp_dst="+rule.get("dest_port")
    return flow

def deploy_flow(sec_group):
    port = sec_group.find("port")
    port_name = port.text
    port_mac = port.get("mac")
    bridge = sec_group.find("bridge").text
    res = os.system("ovs-ofctl del-flows "+bridge)
    if res == -1:
        print("add flow "+flow+" to "+bridge+"error")
        return 1
    res = os.system("ovs-ofctl add-flow "+bridge+" actions=normal")
    if res == -1:
        print("add flow "+flow+" to "+bridge+"error")
        return 1
    rules = sec_group.findall("rule")
    for rule in rules:
        action = rule.get("action")
        flow = joint_flow(port_name, port_mac, rule)
        if flow != None:
            flow = flow+",priority=100"
            if action == "accept":
                flow=flow+",action=normal"
            elif action == "drop":
                flow=flow+",action=drop"
            else:
                print("error action.")
                return 1
            #print(flow)
            res = os.system("ovs-ofctl add-flow "+bridge+" "+flow)
            if res == -1:
                print("add flow "+flow+" to "+bridge+"error")
                return 1
    return 0

def deploy_flow_from_xml(file,port=None):
    tree = ET.parse(file)
    flow=tree.getroot()
    sec_groups = flow.getchildren()

    if port == None:
        for sec_group in sec_groups:
            ret = deploy_flow(sec_group) 
            if ret != 0:
                return ret
    else:
        for sec_group in sec_groups:
            port_node = sec_group.find("port")
            if port_node.text == port:
                ret = deploy_flow(sec_group) 
                if ret != 0:
                    return ret
                break
    return 0
    
def delete_flow(sec_group):
    port = sec_group.find("port")
    port_name = port.text
    port_mac = port.get("mac")
    bridge = sec_group.find("bridge").text
    rules = sec_group.findall("rule")
    for rule in rules:
        flow = joint_flow(port_name, port_mac, rule)
        if flow != None:
            #print(flow)
            res = os.system("ovs-ofctl del-flows "+bridge+" "+flow)
            if res == -1:
                print("delete flow "+flow+" from "+bridge+"error")
                return 1
    return 0
    
def delete_flow_from_xml(file,port=None):
    tree = ET.parse(file)
    flow=tree.getroot()
    sec_groups = flow.getchildren()
    if port == None:
        for sec_group in sec_groups:
            ret = delete_flow(sec_group) 
            if ret != 0:
                return ret
    else:
        for sec_group in sec_groups:
            port_node = sec_group.find("port")
            if port_node.text == port:
                ret = delete_flow(sec_group) 
                if ret != 0:
                    return ret
                break
    return 0

def update_flow(file,port=None):
    delete_flow_from_xml(file,port)
    deploy_flow_from_xml(file,port)
    return 0


funcs = {
    "deploy":deploy_flow_from_xml,
    "update":update_flow,
    "delete":delete_flow_from_xml,
    "-h":usage
}

if __name__=='__main__':
    if len(sys.argv) < 2:
        usage()
        exit(1)

    if sys.argv[1] not in funcs:
        print(sys.argv[1]+"is not valid command,use -h options to view usage")
        exit(1) 
    port_name = None
    if len(sys.argv) == 4:
        port_name = sys.argv[3]
    func = funcs.get(sys.argv[1],None)
    if func == None:
        usage()
        exit(1)
    func(sys.argv[2],port=port_name)
