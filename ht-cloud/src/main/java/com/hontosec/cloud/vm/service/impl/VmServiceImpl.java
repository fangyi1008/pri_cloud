/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.vm.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import javax.xml.bind.JAXB;

import org.apache.commons.lang3.StringUtils;
import org.libvirt.Connect;
import org.libvirt.Domain;
import org.libvirt.DomainInfo;
import org.libvirt.DomainInfo.DomainState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.hontosec.cloud.common.config.CloudConfig;
import com.hontosec.cloud.common.service.ConnectService;
import com.hontosec.cloud.common.utils.Constant;
import com.hontosec.cloud.common.utils.IPUtils;
import com.hontosec.cloud.common.utils.PageUtils;
import com.hontosec.cloud.common.utils.Query;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.common.utils.SysLogUtil;
import com.hontosec.cloud.common.utils.crypt.CryptUtil;
import com.hontosec.cloud.common.utils.sftp.SFTPUtil;
import com.hontosec.cloud.common.utils.ssh.SshUtil;
import com.hontosec.cloud.common.utils.text.Convert;
import com.hontosec.cloud.host.dao.HostDao;
import com.hontosec.cloud.host.entity.HostEntity;
import com.hontosec.cloud.network.dao.PortDao;
import com.hontosec.cloud.network.dao.SecurityGroupDao;
import com.hontosec.cloud.network.dao.SecurityGroupVmDao;
import com.hontosec.cloud.network.dao.SecurityRuleDao;
import com.hontosec.cloud.network.dao.VmSwitchDao;
import com.hontosec.cloud.network.entity.PortEntity;
import com.hontosec.cloud.network.entity.SecurityGroupEntity;
import com.hontosec.cloud.network.entity.SecurityGroupVmEntity;
import com.hontosec.cloud.network.entity.SecurityRuleEntity;
import com.hontosec.cloud.network.entity.VmSwitchEntity;
import com.hontosec.cloud.network.xml.FlowXml;
import com.hontosec.cloud.network.xml.PortXml;
import com.hontosec.cloud.network.xml.RuleXml;
import com.hontosec.cloud.network.xml.SecGroupXml;
import com.hontosec.cloud.storage.dao.StorageDao;
import com.hontosec.cloud.storage.dao.StoragePoolDao;
import com.hontosec.cloud.storage.entity.StorageEntity;
import com.hontosec.cloud.storage.entity.StoragePoolEntity;
import com.hontosec.cloud.storage.service.StorageService;
import com.hontosec.cloud.sys.dao.SysLogDao;
import com.hontosec.cloud.sys.entity.SysLogEntity;
import com.hontosec.cloud.vm.dao.VmDao;
import com.hontosec.cloud.vm.dao.VmHardwareDao;
import com.hontosec.cloud.vm.entity.VmEntity;
import com.hontosec.cloud.vm.entity.VmHardwareEntity;
import com.hontosec.cloud.vm.entity.DTO.VmEntityDTO;
import com.hontosec.cloud.vm.entity.DTO.VmMoveEnityDTO;
import com.hontosec.cloud.vm.service.VmService;
import com.hontosec.cloud.vm.xml.VmXml;

/**
 * 虚拟机接口实现层
 * 
 * @author fangyi
 *
 */
@Service("vmService")
public class VmServiceImpl extends ServiceImpl<VmDao, VmEntity> implements VmService {

	private static final Logger logger = LoggerFactory.getLogger(VmServiceImpl.class);

	@Autowired
	private VmDao vmDao;
	@Autowired
	private VmHardwareDao vmHardwareDao;
	@Autowired
	private StorageDao storageDao;
	@Autowired
	private HostDao hostDao;
	@Autowired
	private PortDao portDao;
	@Autowired
	private SecurityGroupVmDao securityGroupVmDao;
	@Autowired
	private SecurityGroupDao securityGroupDao;
	@Autowired
	private SecurityRuleDao securityRuleDao;
	@Autowired
	private VmSwitchDao vmSwitchDao;
	@Autowired
	private StoragePoolDao storagePoolDao;
	@Autowired
	private StorageService storageService;
	@Autowired
	private SysLogDao sysLogDao;
	@Autowired
	private ConnectService connectService;

	@Override
	public List<VmEntity> queryByHostId(Long hostId) {
		return vmDao.queryByHostId(hostId);
	}

	@Override
	public Result saveVm(VmEntityDTO vmEntityDTO) throws Exception {
		VmEntity vmEntity = vmDao.queryByVmName(vmEntityDTO.getVmName());
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("虚拟机动作");
		sysLog.setOperObj(vmEntityDTO.getVmName());
		sysLog.setOperMark("增加虚拟机");
		if (vmEntity != null) {
			sysLog.setResult("失败");
			sysLog.setCreateDate(new Date());
			sysLog.setErrorMsg("虚拟机名称不能有重复");
			sysLogDao.insert(sysLog);
			return Result.error("虚拟机名称不能有重复");
		}
		VmEntity vm = packVmDb(vmEntityDTO);// 组装虚拟机表数据
		this.save(vm);// 插入虚拟机
		VmHardwareEntity vmHardware = packVmHardwareDb(vmEntityDTO, vm.getVmId());// 组装虚拟机配置表数据
		vmHardwareDao.insert(vmHardware);// 插入虚拟机配置
		try {
			packStorage(vmEntityDTO);
			vm.setStorageVolumeId(vmEntityDTO.getStorageId());
			vmHardware.setVmStorageLocation(vmEntityDTO.getVmStorageLocation());
			vmDao.updateById(vm);
			vmHardwareDao.updateById(vmHardware);
			SecurityGroupVmEntity securityGroupVm = securityGroupVmDao.queryByVmId(vm.getVmId());
			if(securityGroupVm != null && vmEntityDTO.getSecurityGroupId() != null) {
				securityGroupVm.setSecurityGroupId(vmEntityDTO.getSecurityGroupId());
				securityGroupVmDao.updateById(securityGroupVm);
			}else if(securityGroupVm != null && vmEntityDTO.getSecurityGroupId() == null){
				securityGroupVmDao.deleteById(securityGroupVm);
			}else {
				SecurityGroupVmEntity securityGroupVmEntity = new SecurityGroupVmEntity();
				securityGroupVmEntity.setSecurityGroupId(vmEntityDTO.getSecurityGroupId());
				securityGroupVmEntity.setVmId(vm.getVmId());
				securityGroupVmDao.insert(securityGroupVmEntity);
			}
			HostEntity hostEntity = hostDao.selectById(vmEntityDTO.getHostId());
			VmSwitchEntity vmSwitchEntity = vmSwitchDao.selectById(vmEntityDTO.getVmSwitchId()); // 根据虚拟机交换机id查询虚拟交换机名称
			vmEntityDTO.setVmSwitchName(vmSwitchEntity.getVmSwitchName());
			vmEntityDTO.setVmId(vm.getVmId());
			String vmXml = VmXml.vmXml(vmEntityDTO, hostEntity);
			createVm(vm,vmXml);// 创建虚拟机
		} catch (Exception e) {
			this.removeById(vm.getVmId());
			vmHardwareDao.deleteById(vmHardware.getVmHardwareId());
			List<String> storageIds = new ArrayList<String>();
			StorageEntity storage = storageDao.selectById(vmEntityDTO.getStorageId());
			if(storage != null) {
				storageIds.add(String.valueOf(vmEntityDTO.getStorageId()));
			}
			storageService.deleteBatch(storageIds, false);
			logger.error("虚拟机创建失败:" + e.getMessage());
			sysLog.setResult("失败");
			sysLog.setCreateDate(new Date());
			sysLog.setErrorMsg("虚拟机创建失败:" + e.getMessage());
			sysLogDao.insert(sysLog);
			return Result.error(e.getMessage());
		}
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		return Result.ok();
	}

	/**
	 * 组装存储卷数据并生成存储卷
	 * 
	 * @param storagePoolId 新建文件选择存储池 -如为空会新建虚拟机到默认存储池
	 * @param volumeId      已有文件--基础镜像
	 * @param vmName        虚拟机名称
	 * @throws Exception
	 */
	public void packStorage(VmEntityDTO vmEntityDTO) throws Exception {
		try {
			StorageEntity storageVolume = null;
			if (vmEntityDTO.getStoragePoolId() == null) {// 新建文件--没有选择存储池
				vmEntityDTO.setStoragePoolId(Long.valueOf(Constant.STORAGE_POOL_ID));
			}
			if (vmEntityDTO.getBasicVolumeId() != null) {// 已有文件
				storageVolume = storageDao.selectById(vmEntityDTO.getBasicVolumeId());
				vmEntityDTO.setStoragePoolId(storageVolume.getStoragePoolId());
			}
			StoragePoolEntity storagePoolEntity = storagePoolDao.selectById(vmEntityDTO.getStoragePoolId());
			StorageEntity storageEntity = new StorageEntity();
			storageEntity.setJudge("0");
			storageEntity.setStorageVolumeName(vmEntityDTO.getVmName());
			if (vmEntityDTO.getVmDiskSize() == null) {
				storageEntity.setCapacity(Constant.STORAGE_VOLUME_CAPACITY);
			} else {
				storageEntity.setCapacity(vmEntityDTO.getVmDiskSize() + "G");
			}
			storageEntity.setFilesystem(Constant.FILE_SYSTEM);
			storageEntity.setCreateFormat(Constant.FILE_SYSTEM);
			storageEntity.setStoragePoolId(storagePoolEntity.getStoragePoolId());
			if ("dir".equals(storagePoolEntity.getPoolType())) {
				storageEntity.setStorageType(1);
			} else if ("iscsi".equals(storagePoolEntity.getPoolType())) {
				storageEntity.setStorageType(3);
			} else if ("lvm".equals(storagePoolEntity.getPoolType())) {
				storageEntity.setStorageType(4);
			}
			if (storageVolume != null) {// 基础镜像
				storageEntity.setBasicVolumeId(vmEntityDTO.getBasicVolumeId());
			}
			Result result = storageService.saveVolume(storageEntity);// 在该存储池下创建存储卷
			logger.info("packStorage result {}",result);
			logger.info("packStorage result code {}",result.get("code"));
			if (Convert.toInt(result.get("code")) != 0 || Convert.toInt(result.get("code")) == null) {
				throw new Exception("生成存储卷失败 : " + result.get("msg"));
			}
			vmEntityDTO.setVmStorageLocation(storageEntity.getStoragePath());// 虚拟机生成后的存储卷路径
			vmEntityDTO.setStorageId(storageEntity.getStorageId());
		} catch (Exception e) {
			logger.error("虚拟机组装并生成存储卷失败 : " + e.getMessage());
			throw new Exception("生成存储卷失败");
		}
	}

	/**
	 * 组装虚拟机表数据
	 */
	public VmEntity packVmDb(VmEntityDTO vmEntityDTO) {
		VmEntity vm = null;
		if (vmEntityDTO.getVmId() == null) {
			vm = new VmEntity();
		} else {
			vm = vmDao.selectById(vmEntityDTO.getVmId());
		}
		vm.setVmName(vmEntityDTO.getVmName());
		vm.setClusterId(vmEntityDTO.getClusterId());
		vm.setDataCenterId(vmEntityDTO.getDataCenterId());
		vm.setHostId(vmEntityDTO.getHostId());
		vm.setOsIp(vmEntityDTO.getOsIp());
		vm.setState("关机");
		vm.setCreateTime(new Date());
		vm.setCreateUserId(SysLogUtil.getUserId());
		vm.setVmMark(vmEntityDTO.getVmMark());
		return vm;
	}

	/**
	 * 组装虚拟机配置表数据
	 */
	public VmHardwareEntity packVmHardwareDb(VmEntityDTO vmEntityDTO, Long vmId) {
		VmHardwareEntity vmHardwareEntity = null;
		if (vmEntityDTO.getVmHardwareId() == null) {
			vmHardwareEntity = new VmHardwareEntity();
		} else {
			vmHardwareEntity = vmHardwareDao.queryByVmId(vmEntityDTO.getVmId());
		}
		vmHardwareEntity.setVmId(vmId);
		vmHardwareEntity.setVmCdDriver(vmEntityDTO.getCdDriverId()+"");
		vmHardwareEntity.setVmCpuAduit(vmEntityDTO.getVmCpuAduit());
		vmHardwareEntity.setVmCpuNum(vmEntityDTO.getVmCpuAduit() * vmEntityDTO.getVmCpuSockets());
		vmHardwareEntity.setVmDiskSize(vmEntityDTO.getVmDiskSize());
		vmHardwareEntity.setVmMemSize(vmEntityDTO.getVmMemSize());
		vmHardwareEntity.setVmNetworkMac(vmEntityDTO.getVmNetworkMac());
		vmHardwareEntity.setVmOs(vmEntityDTO.getVmOs());// 操作系统
		vmHardwareEntity.setVmOsPath(vmEntityDTO.getVmOsPath());// （光驱）操作系统镜像文件路径
		vmHardwareEntity.setVmStorageLocation(vmEntityDTO.getVmStorageLocation());// 虚拟机存储位置
		vmHardwareEntity.setVmSwitchId(vmEntityDTO.getVmSwitchId());
		vmHardwareEntity.setDiskCreateType(vmEntityDTO.getDiskCreateType());
		vmHardwareEntity.setCreateTime(new Date());
		return vmHardwareEntity;
	}

	@Override
	public PageUtils queyVm(Map<String, Object> params) throws Exception {
		Long hostId = Convert.toLong(params.get("hostId"));
		Long clusterId = Convert.toLong(params.get("clusterId"));
		Long dataCenterId = Convert.toLong(params.get("dataCenterId"));
		String vmName = Convert.toStr(params.get("vmName"));// 获取虚拟机名称
		IPage<VmEntity> page = null;
		try {
			page = this.page(new Query<VmEntity>().getPage(params),
					new QueryWrapper<VmEntity>()
					.eq(hostId != null, "host_id", hostId)
					.eq(clusterId != null, "cluster_id", clusterId)
					.eq(dataCenterId != null, "data_center_id", dataCenterId)
					.like(StringUtils.isNotBlank(vmName), "vm_name", vmName)
					.orderByDesc("create_time"));
		} catch (Exception e) {
			logger.error("虚拟机查询失败:" + e.getMessage());
			throw new Exception("查询虚拟机失败:" + e.getMessage());
		}
		return new PageUtils(page);
	}

	@Override
	public Result updateVm(VmEntityDTO vmEntityDTO) throws Exception {
		VmEntity vmEntity = vmDao.selectById(vmEntityDTO.getVmId());// 原虚拟机数据
		VmHardwareEntity vmHardwareEntity = vmHardwareDao.queryByVmId(vmEntityDTO.getVmId());// 原虚拟机配置
		vmEntityDTO.setVmHardwareId(vmHardwareEntity.getVmHardwareId());// 虚拟机配置id
		HostEntity hostEntity = hostDao.selectById(vmEntity.getHostId());
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("虚拟机动作");
		sysLog.setOperObj(vmEntity.getVmName());
		sysLog.setOperMark("修改虚拟机");
		try {
			String command = "virsh undefine " + vmEntity.getVmName();
			String flag = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(),
					CryptUtil.decrypt(hostEntity.getHostPassword()), command);
			if (flag.contains("undefined")) {
				if (vmEntityDTO.getVmStorageLocation() == null) {
					vmEntityDTO.setVmStorageLocation(vmHardwareEntity.getVmStorageLocation());
				}
				if (vmEntityDTO.getStorageId() == null) {
					vmEntityDTO.setStorageId(vmEntity.getStorageVolumeId());
				}
				VmEntity vm = packVmDb(vmEntityDTO);// 组装虚拟机表数据
				vm.setStorageVolumeId(vmEntityDTO.getStorageId());
				vmDao.updateById(vm);// 修改虚拟机
				VmHardwareEntity vmHardware = packVmHardwareDb(vmEntityDTO, vm.getVmId());// 组装虚拟机配置表数据
				vmHardware.setVmStorageLocation(vmEntityDTO.getVmStorageLocation());
				vmHardwareDao.updateById(vmHardware);
				SecurityGroupVmEntity securityGroupVm = securityGroupVmDao.queryByVmId(vm.getVmId());
				if(securityGroupVm != null && vmEntityDTO.getSecurityGroupId() != null) {
					securityGroupVm.setSecurityGroupId(vmEntityDTO.getSecurityGroupId());
					securityGroupVmDao.updateById(securityGroupVm);
				}else if(securityGroupVm != null && vmEntityDTO.getSecurityGroupId() == null){
					securityGroupVmDao.deleteById(securityGroupVm);
				}else {
					SecurityGroupVmEntity securityGroupVmEntity = new SecurityGroupVmEntity();
					securityGroupVmEntity.setSecurityGroupId(vmEntityDTO.getSecurityGroupId());
					securityGroupVmEntity.setVmId(vmEntityDTO.getVmId());
					securityGroupVmDao.insert(securityGroupVmEntity);
				}
				VmSwitchEntity vmSwitchEntity = vmSwitchDao.selectById(vmEntityDTO.getVmSwitchId()); // 根据虚拟机交换机id查询虚拟交换机名称
				vmEntityDTO.setVmSwitchName(vmSwitchEntity.getVmSwitchName());
				vmEntityDTO.setVmId(vm.getVmId());
				String vmXml = VmXml.vmXml(vmEntityDTO, hostEntity);
				createVm(vm,vmXml);// 创建虚拟机
			} else {
				sysLog.setResult("失败");
				sysLog.setCreateDate(new Date());
				sysLog.setErrorMsg("虚拟机执行undefine失败");
				sysLogDao.insert(sysLog);
				return Result.error("修改虚拟机失败");
			}
		} catch (Exception e) {
			/*String command = "virsh define " + CloudConfig.getVmPath() + vmEntity.getVmName() + ".xml";
			SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(),
					CryptUtil.decrypt(hostEntity.getHostPassword()), command);*/
			logger.error("虚拟机修改失败:" + e.getMessage());
			sysLog.setResult("失败");
			sysLog.setCreateDate(new Date());
			sysLog.setErrorMsg("虚拟机修改失败:" + e.getMessage());
			sysLogDao.insert(sysLog);
			return Result.error("修改虚拟机失败" + e.getMessage());
		}
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		return Result.ok();
	}

	/**
	 * 创建虚拟机
	 * 
	 * @param vmEntity
	 * @throws Exception
	 */
	public void createVm(VmEntity vmEntity,String vmXml) throws Exception {
		HostEntity hostEntity = hostDao.selectById(vmEntity.getHostId());
		try {
			//SAXReader reader = new SAXReader();
			Connect connect = connectService.kvmConnect(hostEntity.getOsIp(),hostEntity.getHostUser(), Constant.KVM_SSH, 0);
			//Document document = reader.read(CloudConfig.getVmPath() + vmEntity.getVmId() + ".xml");
			//String xmlDesc = document.asXML();
			Domain domain = connect.domainDefineXML(vmXml);
			logger.info("虚拟机的id：{}", domain.getID());
			logger.info("虚拟机的uuid：{}", domain.getUUIDString());
			logger.info("虚拟机的名称：{}", domain.getName());
			logger.info("虚拟机的是否自动启动：{}", domain.getAutostart());
			logger.info("虚拟机的状态：{}", domain.getInfo().state);
		} catch (Exception e) {
			logger.error("创建虚拟机失败 : " + e.getMessage());
			throw new Exception("创建虚拟机失败");
		}
	}

	/**
	 * 异步下发规则
	 * 
	 * @param domain
	 * @param vmEntity
	 */
	public void asynDistribution(VmEntity vmEntity, String hostIp) {
		CompletableFuture.runAsync(new Runnable() {// 异步执行
			@Override
			public void run() {
				try {
					SecurityGroupVmEntity securityGroupVm = securityGroupVmDao
							.queryByVmId(vmEntity.getVmId());
					if(securityGroupVm != null) {
						FlowXml flowXml = new FlowXml();
						HostEntity hostEntity = hostDao.selectById(vmEntity.getHostId());
						// 调用命令行获取虚拟机interface/type/source/model/mac
						String command = "virsh domiflist " + vmEntity.getVmName();
						String result = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(),
								CryptUtil.decrypt(hostEntity.getHostPassword()), command);
						List<SecGroupXml> secGroupXmlList = new ArrayList<SecGroupXml>();
						SecGroupXml secGroupXml = new SecGroupXml();
						if(securityGroupVm.getSecurityGroupId() != null) {
							SecurityGroupEntity securityGroupEntity = securityGroupDao
									.selectById(securityGroupVm.getSecurityGroupId());
							secGroupXml.setName(securityGroupEntity.getSecurityGroupName());
							List<SecurityRuleEntity> sucurityRuleList = securityRuleDao
									.queryByGroupId(securityGroupVm.getSecurityGroupId());
							if(sucurityRuleList.size() > 0) {
								parseDomiflist(securityGroupVm, result, secGroupXml);// 解析string数据
								createRuleXml(sucurityRuleList, secGroupXml);// 组装安全规则xml
								secGroupXmlList.add(secGroupXml);
								flowXml.setSecGroupXmlList(secGroupXmlList);
								// 调用python脚本下发流量控制信息
								String xmlFilePath = "/htcloud/scripts/" + vmEntity.getVmId() + "_openflow.xml";
								File xmlFile = new File(xmlFilePath);
								if (!xmlFile.exists()) {
									xmlFile.createNewFile();
								}
								JAXB.marshal(flowXml, xmlFile);// 拷贝文件到目标服务器
								if(!hostEntity.getOsIp().equals(IPUtils.getIp())) {
									SFTPUtil sftpUtil = new SFTPUtil(hostEntity.getHostUser(),CryptUtil.decrypt(hostEntity.getHostPassword()),hostEntity.getOsIp(),22);
									sftpUtil.upFile("/htcloud/scripts/", vmEntity.getVmId() + "_openflow.xml", xmlFilePath, true);
								}
								String switchCommand = "python3 /htcloud/scripts/openflow.py deploy " + xmlFilePath;
								String flowResult = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(),
										CryptUtil.decrypt(hostEntity.getHostPassword()), switchCommand);
								System.out.println(flowResult);
							}
						}else {
							logger.info("securityGroupVm SecurityGroupId为空");
						}
					}else {
						logger.info("securityGroupVm为空");
					}
					
				} catch (Exception e) {
					logger.error("获取流量控制失败 : " + e.getMessage());
				}
			}
		});
	}

	/**
	 * 解析virsh domiflist返回信息
	 * 
	 * @throws IOException
	 */
	public void parseDomiflist(SecurityGroupVmEntity securityGroupVmEntity, String result, 
			SecGroupXml secGroupXml) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new ByteArrayInputStream(result.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
		String line;
		int currentLine = 0;
		while ((line = br.readLine()) != null) {
			if (!line.trim().equals("") && currentLine > 1 && currentLine <= 2) {
				String[] lineDatas = line.split("\\s{2,}");
				PortXml portXml = new PortXml();
				portXml.setMac(lineDatas[4].trim());
				portXml.setValue(lineDatas[0].trim());
				secGroupXml.setPortXml(portXml);
				secGroupXml.setBridge(lineDatas[2].trim());
				VmSwitchEntity vmSwitchEntity = vmSwitchDao.queryByName(lineDatas[2].trim());// 根据虚拟交换机名称查询虚拟交换机
				if (vmSwitchEntity != null) {
					PortEntity portEntity = new PortEntity();
					portEntity.setPortName(lineDatas[0].trim());
					portEntity.setType(lineDatas[1].trim());
					portEntity.setPortType(1);// 普通端口
					portEntity.setVmSwitchId(vmSwitchEntity.getVmSwitchId());
					portEntity.setModel(lineDatas[3].trim());
					portEntity.setMac(lineDatas[4].trim());
					portDao.insert(portEntity);// 插入端口表port_table
					securityGroupVmEntity.setPortId(portEntity.getPortId());
					securityGroupVmDao.updateById(securityGroupVmEntity);
				}
			}
			currentLine++;
		}
	}

	/**
	 * 组装安全规则数据
	 */
	public void createRuleXml(List<SecurityRuleEntity> ruleList, SecGroupXml secGroupXml) {
		// 安全规则
		List<RuleXml> ruleXmlList = new ArrayList<RuleXml>();
		for (int j = 0; j < ruleList.size(); j++) {
			RuleXml ruleXml = new RuleXml();
			ruleXml.setDirection(ruleList.get(j).getInOutFlow());
			ruleXml.setProtocol(ruleList.get(j).getAgreeType().toLowerCase());
			ruleXml.setAction(ruleList.get(j).getAction());
			ruleXml.setSourceIp(ruleList.get(j).getSourceIp());
			ruleXml.setSourceMask(ruleList.get(j).getSourceMask());
			if(ruleList.get(j).getSourcePort() != null) {
				ruleXml.setSourcePort(String.valueOf(ruleList.get(j).getSourcePort()));
			}
			ruleXml.setDestIp(ruleList.get(j).getDestIp());
			ruleXml.setDestMask(ruleList.get(j).getDestMask());
			if(ruleList.get(j).getDestPort() != null) {
				ruleXml.setDestPort(String.valueOf(ruleList.get(j).getDestPort()));
			}
			ruleXmlList.add(ruleXml);
		}
		secGroupXml.setRuleXmlList(ruleXmlList);
	}

	@Override
	@Transactional
	public Result deleteVm(List<String> vmIds,Boolean rbFlag) throws Exception {
		List<SysLogEntity> sysLogList = new ArrayList<SysLogEntity>();
		for (int i = 0; i < vmIds.size(); i++) {
			VmEntity vmEntity = this.getById(vmIds.get(i));
			SysLogEntity sysLog = new SysLogEntity();
			sysLog.setUsername(SysLogUtil.getUserName());
			sysLog.setIp(SysLogUtil.getLoginIp());
			sysLog.setOperation("虚拟机动作");
			sysLog.setOperMark("删除虚拟机");
			sysLog.setOperObj(vmEntity.getVmName());
			try {
				Boolean flag = booleanDelete(vmEntity,sysLog);
				if (flag == true) {
					deleteVncConfig(vmEntity.getVmId());
					//删除虚拟机与安全规则关联关系
					SecurityGroupVmEntity securityGroupVm= securityGroupVmDao.queryByVmId(vmEntity.getVmId());
					securityGroupVmDao.deleteById(securityGroupVm.getId());
					VmHardwareEntity vmHardwareEntity = vmHardwareDao.queryByVmId(vmEntity.getVmId());
					if(vmHardwareEntity != null) {
						vmHardwareDao.deleteById(vmHardwareEntity.getVmHardwareId());
					}
					this.removeById(vmEntity.getVmId());
					if(rbFlag == false) {
						sysLog.setResult("成功");
					}else {
						List<String> storageIds = new ArrayList<String>();
						StorageEntity storage = storageDao.selectById(vmEntity.getStorageVolumeId());
						//判断是否作为基础镜像
						List<StorageEntity> basicStorageList = storageDao.selectByBasicId(storage.getStorageId());
						if(basicStorageList.size() > 0) {
							sysLog.setResult("失败");
							sysLog.setErrorMsg("该虚拟机存储卷已被作为基础镜像使用");
						}else {
							storageIds.add(String.valueOf(storage.getStorageId()));
							storageService.deleteBatch(storageIds, true);
							sysLog.setResult("成功");
						}
					}
				}
			} catch (Exception e) {
				logger.error("虚拟机删除失败:" + e.getMessage());
				sysLog.setResult("失败");
				sysLog.setErrorMsg("虚拟机删除失败:" + e.getMessage());
			}
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			sysLogList.add(sysLog);
		}
		return Result.ok().put("sysLogList", sysLogList);
	}

	private Boolean booleanDelete(VmEntity vmEntity,SysLogEntity sysLog) {
		try {
			HostEntity host = hostDao.selectById(vmEntity.getHostId());
			Connect connect = connectService.kvmConnect(host.getOsIp(),host.getHostUser(), Constant.KVM_SSH, 0);
			Domain domain = connect.domainLookupByName(vmEntity.getVmName());
			if (domain.getInfo().state.equals(DomainState.VIR_DOMAIN_SHUTOFF)) {
				domain.undefine();
			} else {
				domain.destroy();
				domain.undefine();
			}
			return true;
		} catch (Exception e) {
			sysLog.setResult("失败");
			sysLog.setErrorMsg(vmEntity.getVmName() + "删除失败 : " + e.getMessage());
			sysLog.setVmId(vmEntity.getVmId());
			logger.error("链接虚拟机，并删除失败:" + e.getMessage());
			return false;
		}
	}

	@Override
	public Result startVm(Long[] vmIds) throws Exception {
		List<SysLogEntity> sysLogList = new ArrayList<SysLogEntity>();
		for (int i = 0; i < vmIds.length; i++) {
			VmEntity vmEntity = vmDao.selectById(vmIds[i]);
			SysLogEntity sysLog = new SysLogEntity();
			sysLog.setUsername(SysLogUtil.getUserName());
			sysLog.setIp(SysLogUtil.getLoginIp());
			sysLog.setOperation("虚拟机动作");
			sysLog.setOperMark("启动虚拟机");
			sysLog.setOperObj(vmEntity.getVmName());
			try {
				// 判断是否链接成功，并启动虚拟机
				Boolean flag = booleanStartVm(vmEntity,sysLog);
				String state = "运行";
				if (flag == true) {
					vmDao.updateState(state, vmEntity.getVmName());
					sysLog.setResult("成功");
					HostEntity host = hostDao.selectById(vmEntity.getHostId());
					asynDistribution(vmEntity, host.getOsIp());
				}else {
					sysLog.setResult("失败");
				}
			} catch (Exception e) {
				logger.error("虚拟机启动失败:" + e.getMessage());
				sysLog.setResult("失败");
				sysLog.setErrorMsg("虚拟机启动失败:" + e.getMessage());
			}
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			sysLogList.add(sysLog);
		}
		return Result.ok().put("sysLogList", sysLogList);
	}

	private Boolean booleanStartVm(VmEntity vmEntity,SysLogEntity sysLog) throws Exception {
		try {
			HostEntity host = hostDao.selectById(vmEntity.getHostId());
			Connect connect = connectService.kvmConnect(host.getOsIp(),host.getHostUser(), Constant.KVM_SSH, 0);
			Domain domain = connect.domainLookupByName(vmEntity.getVmName());
			logger.info("start vm domain info state {}",domain.getInfo().state);
			if(domain.getInfo().state == DomainState.VIR_DOMAIN_RUNNING && "关机".equals(vmEntity.getState())) {
				return true;
			}else if(domain.getInfo().state == DomainState.VIR_DOMAIN_PAUSED) {
				domain.resume();
				return true;
			}
			domain.create();
			return true;
		} catch (Exception e) {
			sysLog.setResult("失败");
			sysLog.setErrorMsg(vmEntity.getVmName() + "启动失败 : " + e.getMessage());
			logger.error("虚拟机启动链接失败" + e.getMessage());
			return false;
		}

	}

	@Override
	public Result shutDownVm(Long[] vmIds) throws Exception {
		List<SysLogEntity> sysLogList = new ArrayList<SysLogEntity>();
		for (int i = 0; i < vmIds.length; i++) {
			VmEntity vmEntity = vmDao.selectById(vmIds[i]);
			SysLogEntity sysLog = new SysLogEntity();
			sysLog.setUsername(SysLogUtil.getUserName());
			sysLog.setIp(SysLogUtil.getLoginIp());
			sysLog.setOperation("虚拟机动作");
			sysLog.setOperMark("关闭虚拟机");
			sysLog.setOperObj(vmEntity.getVmName());
			try {
				// 虚拟机链接，并关机
				Boolean flag = booleanShutDown(vmEntity,sysLog);
				String state = "关机";
				if (flag == true) {
					deleteVncConfig(vmIds[i]);
					vmDao.updateState(state, vmEntity.getVmName());
					sysLog.setResult("成功");
				}
			} catch (Exception e) {
				logger.error("虚拟机关机失败:" + e.getMessage());
				sysLog.setResult("失败");
				sysLog.setErrorMsg("虚拟机关机失败:" + e.getMessage());
			}
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			sysLogList.add(sysLog);
		}
		return Result.ok().put("sysLogList", sysLogList);
	}

	private Boolean booleanShutDown(VmEntity vmEntity,SysLogEntity sysLog) throws Exception {
		try {
			HostEntity host = hostDao.selectById(vmEntity.getHostId());
			Connect connect = connectService.kvmConnect(host.getOsIp(),host.getHostUser(), Constant.KVM_SSH, 0);
			Domain domain = connect.domainLookupByName(vmEntity.getVmName());
			domain.shutdown();
		} catch (Exception e) {
			sysLog.setResult("失败");
			sysLog.setErrorMsg(vmEntity.getVmName() + "关机失败 : " + e.getMessage());
			logger.error("虚拟机链接关机失败:" + e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public Result suspendVm(Long[] vmIds) throws Exception {
		List<SysLogEntity> sysLogList = new ArrayList<SysLogEntity>();
		for (int i = 0; i < vmIds.length; i++) {
			VmEntity vmEntity = vmDao.selectById(vmIds[i]);
			SysLogEntity sysLog = new SysLogEntity();
			sysLog.setUsername(SysLogUtil.getUserName());
			sysLog.setIp(SysLogUtil.getLoginIp());
			sysLog.setOperation("虚拟机动作");
			sysLog.setOperMark("挂起虚拟机");
			sysLog.setOperObj(vmEntity.getVmName());
			try {
				Boolean flag = booleanSuspend(vmEntity,sysLog);
				String state = "挂起";
				if (flag == true) {
					vmDao.updateState(state, vmEntity.getVmName());
					sysLog.setResult("成功");
				} 
			} catch (Exception e) {
				logger.error("虚拟机挂起失败:" + e.getMessage());
				sysLog.setResult("失败");
				sysLog.setErrorMsg("虚拟机挂起失败:" + e.getMessage());
			}
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			sysLogList.add(sysLog);
		}
		return Result.ok().put("sysLogList", sysLogList);
	}

	private Boolean booleanSuspend(VmEntity vmEntity,SysLogEntity sysLog) {
		try {
			HostEntity host = hostDao.selectById(vmEntity.getHostId());
			Connect connect = connectService.kvmConnect(host.getOsIp(),host.getHostUser(), Constant.KVM_SSH, 0);
			Domain domain = connect.domainLookupByName(vmEntity.getVmName());
			domain.suspend();
		} catch (Exception e) {
			sysLog.setResult("失败");
			sysLog.setErrorMsg(vmEntity.getVmName() + "挂起失败 : " + e.getMessage());
			logger.error("虚拟机链接，挂起失败:" + e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public Result resumeVm(Long[] vmIds) throws Exception {
		List<SysLogEntity> sysLogList = new ArrayList<SysLogEntity>();
		for (int i = 0; i < vmIds.length; i++) {
			VmEntity vmEntity = vmDao.selectById(vmIds[i]);
			SysLogEntity sysLog = new SysLogEntity();
			sysLog.setUsername(SysLogUtil.getUserName());
			sysLog.setIp(SysLogUtil.getLoginIp());
			sysLog.setOperation("虚拟机动作");
			sysLog.setOperMark("恢复挂起虚拟机");
			sysLog.setOperObj(vmEntity.getVmName());
			try {
				Boolean flag = booleanResume(vmEntity,sysLog);
				String state = "运行";
				if (flag == true) {
					vmDao.updateState(state, vmEntity.getVmName());
					sysLog.setResult("成功");
				}
			} catch (Exception e) {
				logger.error("虚拟机恢复挂起失败:" + e.getMessage());
				sysLog.setResult("失败");
				sysLog.setErrorMsg("虚拟机恢复挂起失败:" + e.getMessage());
			}
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			sysLogList.add(sysLog);
		}
		return Result.ok().put("sysLogList", sysLogList);
	}

	private Boolean booleanResume(VmEntity vmEntity,SysLogEntity sysLog) {
		try {
			HostEntity host = hostDao.selectById(vmEntity.getHostId());
			Connect connect = connectService.kvmConnect(host.getOsIp(),host.getHostUser(), Constant.KVM_SSH, 0);
			Domain domain = connect.domainLookupByName(vmEntity.getVmName());
			domain.resume();
		} catch (Exception e) {
			sysLog.setResult("失败");
			sysLog.setErrorMsg(vmEntity.getVmName() + "恢复挂起失败 : " + e.getMessage());
			logger.error("链接虚拟机，并恢复挂起失败:" + e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public Result restartVm(Long[] vmIds) throws Exception {
		List<SysLogEntity> sysLogList = new ArrayList<SysLogEntity>();
		for (int i = 0; i < vmIds.length; i++) {
			VmEntity vmEntity = vmDao.selectById(vmIds[i]);
			SysLogEntity sysLog = new SysLogEntity();
			sysLog.setUsername(SysLogUtil.getUserName());
			sysLog.setIp(SysLogUtil.getLoginIp());
			sysLog.setOperation("虚拟机动作");
			sysLog.setOperMark("重启虚拟机");
			sysLog.setOperObj(vmEntity.getVmName());
			try {
				// 判断虚拟机是否链接，并重启
				Boolean flag = booleanRestart(vmEntity,sysLog);
				if (flag == true) {
					sysLog.setResult("成功");
				}
			} catch (Exception e) {
				logger.error("虚拟机重启失败:" + e.getMessage());
				sysLog.setResult("失败");
				sysLog.setErrorMsg("虚拟机重启失败:" + e.getMessage());
			}
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			sysLogList.add(sysLog);
		}
		return Result.ok().put("sysLogList", sysLogList);
	}

	private Boolean booleanRestart(VmEntity vmEntity,SysLogEntity sysLog) {
		try {
			HostEntity host = hostDao.selectById(vmEntity.getHostId());
			Connect connect = connectService.kvmConnect(host.getOsIp(),host.getHostUser(), Constant.KVM_SSH, 0);
			Domain domain = connect.domainLookupByName(vmEntity.getVmName());
			domain.reboot(1);
			return true;
		} catch (Exception e) {
			sysLog.setResult("失败");
			sysLog.setErrorMsg(vmEntity.getVmName() + "重启失败 : " + e.getMessage());
			logger.error("连接虚拟机，并重启失败:" + e.getMessage());
			return false;
		}
	}

	@Override
	public VmEntity queryByVmName(String vmName) {
		VmEntity vmEntity = vmDao.queryByVmName(vmName);
		return vmEntity;
	}

	@Override
	public Result moveVm(VmMoveEnityDTO vmMoveEnityDTO) {
		List<SysLogEntity> sysLogList = new ArrayList<SysLogEntity>();
		List<Integer> storageTypes = new ArrayList<Integer>();
		HostEntity hostEntity = hostDao.selectById(vmMoveEnityDTO.getHostId());// 当前主机
		Long[] vmIds = vmMoveEnityDTO.getVmIds();
		if(vmMoveEnityDTO.getHostId().equals(vmMoveEnityDTO.getDestHostId())) {
			return Result.error("虚拟机不允许迁移到同一台主机，请选择另一台主机。");
		}
		String moveType = vmMoveEnityDTO.getMoveType();// 迁移类型
		for (int i = 0; i < vmIds.length; i++) {
			VmEntity vmEntity = this.getById(vmIds[i]);
			SysLogEntity sysLog = new SysLogEntity();
			sysLog.setUsername(SysLogUtil.getUserName());
			sysLog.setIp(SysLogUtil.getLoginIp());
			sysLog.setOperation("虚拟机动作");
			sysLog.setOperMark("迁移虚拟机");
			sysLog.setOperObj(vmEntity.getVmName());
			// 查询存储卷类型
			StorageEntity storageEntity = storageDao.selectById(vmEntity.getStorageVolumeId());
			if (storageEntity == null) {
				sysLog.setResult("失败");
				sysLog.setErrorMsg("存储卷不存在");
				return Result.error("存储卷不存在");
			} else {
				storageTypes.add(storageEntity.getStorageType());
			}
			HostEntity destHostEntity = hostDao.selectById(vmMoveEnityDTO.getDestHostId());
			if ("2".equals(moveType)) {// 如果选择的迁移类型为热迁移，不支持本地存储方式
				if (storageTypes.contains(Integer.valueOf(moveType))) {
					sysLog.setResult("失败");
					sysLog.setErrorMsg("热迁移不支持本地存储");
					return Result.error("热迁移不支持本地存储");
				}
				liveMigrate(vmEntity, hostEntity, destHostEntity, sysLog);// 进行热迁移

			} else {// 冷迁移既支持本地存储也支持共享存储
				clodMigrate(vmEntity, hostEntity, destHostEntity, sysLog);
			}
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			sysLogList.add(sysLog);
		}
		return Result.ok().put("sysLogList", sysLogList);
	}

	/**
	 * 热迁移
	 * 
	 * @param vmList         要迁移的虚拟机
	 * @param hostEntity     源主机
	 * @param destHostEntity 目标主机
	 * @param moveY          迁移成功信息
	 * @param moveN          迁移失败信息
	 * @throws Exception
	 */
	public void liveMigrate(VmEntity vmEntity, HostEntity hostEntity, HostEntity destHostEntity, SysLogEntity sysLog) {
		try {
			// String command = "virsh migrate --live --verbose --unsafe "+vmName+"
			// qemu+ssh://"+destHostEntity.getOsIp()+"/system tcp://"+
			// destHostEntity.getOsIp();
			// int migrateFlag = SshUtil.sshShell(hostEntity.getOsIp(), 22,
			// hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()),
			// command);
			Connect connect = connectService.kvmConnect(hostEntity.getOsIp(),hostEntity.getHostUser(), Constant.KVM_SSH, 0);
			Domain domain = connect.domainLookupByName(vmEntity.getVmName());
			String hostUser = hostEntity.getHostUser();
			String destUrl = "qemu+" + Constant.KVM_SSH + "://" + hostUser + "@" +destHostEntity.getOsIp() + ":22/system";
			int migrateFlag = domain.migrateToURI(destUrl, 0, vmEntity.getVmName(), 0);
			if (migrateFlag == 0) {
				vmEntity.setHostId(destHostEntity.getHostId());
				this.updateById(vmEntity);
				sysLog.setResult("成功");
			} else {
				sysLog.setResult("失败");
				sysLog.setErrorMsg("调用libvirt迁移失败");
			}
		} catch (Exception e) {
			sysLog.setResult("失败");
			sysLog.setErrorMsg(vmEntity.getVmName() + "热迁移失败 : " + e.getMessage());
			logger.error(vmEntity.getVmName() + "热迁移失败 : " + e.getMessage());
		}
	}

	/**
	 * 冷迁移
	 * 
	 * @param vmList         要迁移的虚拟机
	 * @param hostEntity     源主机
	 * @param destHostEntity 目标主机
	 * @param moveY          迁移成功信息
	 * @param moveN          迁移失败信息
	 * @throws Exception
	 */
	public void clodMigrate(VmEntity vmEntity, HostEntity hostEntity, HostEntity destHostEntity, SysLogEntity sysLog) {
		try {
			VmHardwareEntity vmHardwareEntity = vmHardwareDao.queryByVmId(vmEntity.getVmId());
			// 将镜像文件拷贝到目标主机
			String cpCommand = "scp root@" + hostEntity.getOsIp() + ":" + vmHardwareEntity.getVmOsPath() + " "
					+ vmHardwareEntity.getVmOsPath().substring(0, vmHardwareEntity.getVmOsPath().lastIndexOf("/"));
			int cpFlag = SshUtil.sshShell(destHostEntity.getOsIp(), 22, destHostEntity.getHostUser(),
					CryptUtil.decrypt(destHostEntity.getHostPassword()), cpCommand);
			String command = "virsh migrate --verbose --unsafe " + vmEntity.getVmName() + "  qemu+ssh://"
					+ destHostEntity.getOsIp() + "/system tcp://" + destHostEntity.getOsIp();
			int migrateFlag = SshUtil.sshShell(hostEntity.getOsIp(), 22, hostEntity.getHostUser(),
					CryptUtil.decrypt(hostEntity.getHostPassword()), command);
			if (cpFlag == 0 && migrateFlag == 0) {
				vmEntity.setHostId(destHostEntity.getHostId());
				this.updateById(vmEntity);
				sysLog.setResult("成功");
			} else {
				sysLog.setResult("失败");
				sysLog.setErrorMsg("调用libvirt迁移失败");
			}
		} catch (Exception e) {
			sysLog.setResult("失败");
			sysLog.setErrorMsg(vmEntity.getVmName() + "冷迁移失败 : " + e.getMessage());
			logger.error(vmEntity.getVmName() + "冷迁移失败 : " + e.getMessage());
		}
	}

	@Override
	public List<VmEntity> queryByClusterId(Long clusterId) {
		return vmDao.queryByClusterId(clusterId);
	}

	@Override
	public List<VmEntityDTO> queryVm() {
		return vmDao.selectVmList();
	}

	@Override
	public Result randomMACAddress() {
		Random rand = new Random();
		byte[] macAddr = new byte[6];
		rand.nextBytes(macAddr);
		macAddr[0] = (byte) (macAddr[0] & (byte) 254); // zeroing last 2 bytes to make it unicast and locally
														// adminstrated
		StringBuilder sb = new StringBuilder(18);
		for (byte b : macAddr) {
			if (sb.length() > 0)
				sb.append(":");
			sb.append(String.format("%02x", b));
		}
		return Result.ok().put("mac", sb.toString());
	}

	@Override
	public Result info(Long vmId) {
		VmEntity vmEntity = null;
		VmHardwareEntity vmHardware = null;
		VmSwitchEntity vmSwitchEntity = null;
		StorageEntity storageEntity = null;
		StorageEntity basicStorageEntity = null;
		StoragePoolEntity storagePool = null;
		SecurityGroupEntity securityGroup = null;
		DomainInfo domainInfo = null;
		try {
			vmEntity = vmDao.selectById(vmId);
			HostEntity host = hostDao.selectById(vmEntity.getHostId());
			vmHardware = vmHardwareDao.queryByVmId(vmId);
			vmSwitchEntity = vmSwitchDao.selectById(vmHardware.getVmSwitchId());
			storageEntity = storageDao.selectById(vmEntity.getStorageVolumeId());
			storagePool = storagePoolDao.selectById(storageEntity.getStoragePoolId());
			if(storageEntity.getBasicVolumeId() != null) {
				basicStorageEntity = storageDao.selectById(storageEntity.getBasicVolumeId());
			}
			SecurityGroupVmEntity securityGroupVm = securityGroupVmDao.queryByVmId(vmId);
			if(securityGroupVm != null && securityGroupVm.getSecurityGroupId() != null) {
				securityGroup = securityGroupDao.selectById(securityGroupVm.getSecurityGroupId());
			}
			Connect connect = connectService.kvmConnect(host.getOsIp(),host.getHostUser(), Constant.KVM_SSH, 0);
			Domain domain = connect.domainLookupByName(vmEntity.getVmName());
			domainInfo = domain.getInfo();
			logger.info("domainInfo {}",domainInfo);
			logger.info("domainInfo state {}",domainInfo.state + "," + DomainState.VIR_DOMAIN_RUNNING);
			if(domainInfo.state == DomainState.VIR_DOMAIN_RUNNING) {
				vmEntity.setState("运行");
			}else if(domainInfo.state == DomainState.VIR_DOMAIN_SHUTDOWN || domainInfo.state == DomainState.VIR_DOMAIN_SHUTOFF) {
				vmEntity.setState("关机");
			}else if(domainInfo.state == DomainState.VIR_DOMAIN_PAUSED) {
				vmEntity.setState("挂起");
			}
		} catch (Exception e) {
			logger.error("获取虚拟机信息失败 : " + e.getMessage());
			vmEntity.setState("异常");
		}
		vmDao.updateById(vmEntity);
		return Result.ok().put("vm", vmEntity).put("vmHardware", vmHardware).put("vmSwitchEntity", vmSwitchEntity)
				.put("storagePool", storagePool).put("storageEntity", storageEntity).put("securityGroup", securityGroup)
				.put("basicStorageEntity", basicStorageEntity).put("domainInfo", domainInfo);
	}

	@Override
	public Result destroyVm(Long[] vmIds) {
		List<SysLogEntity> sysLogList = new ArrayList<SysLogEntity>();
		for (int i = 0; i < vmIds.length; i++) {
			VmEntity vmEntity = vmDao.selectById(vmIds[i]);
			SysLogEntity sysLog = new SysLogEntity();
			sysLog.setUsername(SysLogUtil.getUserName());
			sysLog.setIp(SysLogUtil.getLoginIp());
			sysLog.setOperation("虚拟机动作");
			sysLog.setOperMark("虚拟机关闭电源");
			sysLog.setOperObj(vmEntity.getVmName());
			try {
				Boolean flag = booleanDestroy(vmEntity,sysLog);// 虚拟机链接，并关机
				String state = "关机";
				if (flag == true) {
					deleteVncConfig(vmIds[i]);
					vmDao.updateState(state, vmEntity.getVmName());
					sysLog.setResult("成功");
				}else {
					sysLog.setResult("失败");
				}
			} catch (Exception e) {
				logger.error("虚拟机关闭电源失败:" + e.getMessage());
				sysLog.setResult("失败");
				sysLog.setErrorMsg("虚拟机关闭电源失败:" + e.getMessage());
			}
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			sysLogList.add(sysLog);
		}
		return Result.ok().put("sysLogList", sysLogList);
	}

	private Boolean booleanDestroy(VmEntity vmEntity,SysLogEntity sysLog) throws Exception {
		try {
			HostEntity host = hostDao.selectById(vmEntity.getHostId());
			Connect connect = connectService.kvmConnect(host.getOsIp(),host.getHostUser(), Constant.KVM_SSH, 0);
			Domain domain = connect.domainLookupByName(vmEntity.getVmName());
			domain.destroy();
		} catch (Exception e) {
			sysLog.setErrorMsg(vmEntity.getVmName() + "虚拟机关闭电源失败:" + e.getMessage());
			logger.error("虚拟机关闭电源失败:" + e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public Result deleteDbVm(Long[] vmIds) {
		for (int i = 0; i < vmIds.length; i++) {
			VmEntity vmEntity = this.getById(vmIds[i]);
			try {
				this.removeById(vmIds[i]);
				VmHardwareEntity vmHardwareEntity = vmHardwareDao.queryByVmId(vmIds[i]);
				if(vmHardwareEntity != null) {
					vmHardwareDao.deleteById(vmHardwareEntity.getVmHardwareId());
				}
				List<String> storageIds = new ArrayList<String>();
				StorageEntity storage = storageDao.selectById(vmEntity.getStorageVolumeId());
				if(storage != null) {
					storageIds.add(String.valueOf(storage.getStorageId()));
				}
				storageService.deleteBatch(storageIds, false);
			} catch (Exception e) {
				logger.error("虚拟机删除失败:" + e.getMessage());
			}
		}
		return Result.ok();
	}
	/**
	 * 删除虚拟机时删除novnc配置文件中对应的记录
	 * @throws Exception 
	 */
	public void deleteVncConfig(Long vmId) throws Exception {
		VmEntity vmEntity = this.getById(vmId);
		HostEntity hostEntity = hostDao.selectById(vmEntity.getHostId());
		String filePath = CloudConfig.getNoVNCPath() + "vnc/noVNC-master/utils/websockify/token/";
		String fileName = hostEntity.getOsIp() + "_token.conf";
		String command = "sed -i '/"+vmEntity.getVmName()+": " +hostEntity.getOsIp()+"/d'" + " " +filePath+fileName;
		logger.info("删除虚拟机novnc配置文件命令 {}",command);
		int delFlag = SshUtil.sshShell(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()),command);//调用shell执行关机
		if(delFlag != 0) {
			logger.error("删除novnc配置文件匹配行失败" );
		}
	}
	
	@Override
	public Result vncUrl(Long vmId) {
		VmEntity vm = vmDao.selectById(vmId);//根据虚拟机id获取虚拟机对象
		String vmName = vm.getVmName();
		HostEntity host = hostDao.selectById(vm.getHostId());
		FileOutputStream outputStream = null;
		String filePath = CloudConfig.getNoVNCPath() + "vnc/noVNC-master/utils/websockify/token/";
		String fileName = host.getOsIp() + "_token.conf";
		File file = new File(filePath + fileName);
		String url = null;
		try {
			String vncPortCommand = "virsh vncdisplay " + vmName;//通过virsh获取vnc端口号
			String vncPortResult = SshUtil.sshExecute(host.getOsIp(), 22, host.getHostUser(),
					CryptUtil.decrypt(host.getHostPassword()), vncPortCommand);
			logger.info(" vncUrl vncPortResult {}",vncPortResult);
			String vncPort = vncPortResult.substring(1).replaceAll("\n","");
			logger.info(" vncUrl vncPort {}",vncPort);
			if(vncPort.length() == 1) {//因为返回数据中有\n\n占用两个字符
				vncPort = ":590" + vncPort;
			}else {
				vncPort = ":59" + vncPort;
			}
			SFTPUtil sftpUtil = new SFTPUtil(host.getHostUser(),CryptUtil.decrypt(host.getHostPassword()),host.getOsIp(),22);//写入文件
			if(!host.getOsIp().equals(IPUtils.getIp())) {
				//查看vnc_token文件是否存在，如不存在直接新建
				Boolean downFlag = sftpUtil.downFile(filePath, fileName,filePath,fileName,false);
				if(downFlag == false) {//不存在进行新建
					file.createNewFile();
				}
			}else {
				if(!file.exists()) {
					file.createNewFile();
				}
			}
			String wirteData = vmName + ": " + host.getOsIp() + vncPort + "\n";
			logger.info(" vncUrl wirteData {}",wirteData);
			//判断文件中是否已存在该虚拟机token配置
			int num = 0;
			List<String> strings = Files.readLines(file, Charsets.UTF_8);
	        for (String string : strings) {
	            if (string.contains(vmName + ": " + host.getOsIp() + vncPort)) {//判断是否包含方法名称，即指定字符串
	                num++;//包含不为0
	            }
	        }
	        if(num == 0) {
	        	outputStream = new FileOutputStream(filePath + fileName,true);
				outputStream.write(wirteData.getBytes());
				if(!host.getOsIp().equals(IPUtils.getIp())) {
					sftpUtil.upFile(filePath, fileName, filePath + fileName, true);
				}
	        }
	        url = "http://"+host.getOsIp()+":6080/vnc_lite.html?path=websockify/?token="+vmName;
		} catch (Exception e) {
			logger.error("获取vnc url失败 : " + e.getMessage());
			return Result.error("获取vnc url失败 : " + e.getMessage());
		}finally {
			try {
				if(!host.getOsIp().equals(IPUtils.getIp())) {
					file.delete();
				}
				if(outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return Result.ok(url);
	}

	@Override
	public List<VmEntity> queryList(Map<String, Object> params) {
		Long hostId = Convert.toLong(params.get("hostId"));
		Long clusterId = Convert.toLong(params.get("clusterId"));
		Long dataCenterId = Convert.toLong(params.get("dataCenterId"));
		String vmName = Convert.toStr(params.get("vmName"));// 获取虚拟机名称
		Integer page = Convert.toInt(params.get("page"));
		Integer limit = Convert.toInt(params.get("limit"));
		List<VmEntity> vmList = vmDao.queryList(dataCenterId,clusterId,hostId,vmName,page-1,limit);
		return vmList;
	}

}
