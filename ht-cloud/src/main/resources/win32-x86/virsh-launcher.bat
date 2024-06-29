@echo off
echo Known working connection types:
echo  + QEMU with direct TCP (qemu+tcp://)
echo  + QEMU with TLS (qemu+tls://)
echo  + VMware ESX (esx://)
echo  + VMware VPX (vpx://)
echo Connection types known to not work:
echo  + QEMU with SSH (qemu+ssh://)
set /p uri="Enter libvirt connection URI to be used: "
virsh.exe -c %uri%
pause