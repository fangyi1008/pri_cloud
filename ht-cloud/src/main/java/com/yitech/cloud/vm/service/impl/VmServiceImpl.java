package com.yitech.cloud.vm.service.impl;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import javax.xml.bind.JAXB;
import org.apache.commons.lang3.StringUtils;
import org.libvirt.*;
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
import com.yitech.cloud.common.config.CloudConfig;
import com.yitech.cloud.common.service.ConnectService;
import com.yitech.cloud.common.utils.*;
import com.yitech.cloud.common.utils.crypt.CryptUtil;
import com.yitech.cloud.common.utils.sftp.SFTPUtil;
import com.yitech.cloud.common.utils.ssh.SshUtil;
import com.yitech.cloud.common.utils.text.Convert;
import com.yitech.cloud.host.dao.HostDao;
import com.yitech.cloud.host.entity.HostEntity;
import com.yitech.cloud.network.dao.*;
import com.yitech.cloud.network.entity.*;
import com.yitech.cloud.network.xml.*;
import com.yitech.cloud.storage.dao.*;
import com.yitech.cloud.storage.entity.*;
import com.yitech.cloud.storage.service.StorageService;
import com.yitech.cloud.sys.dao.SysLogDao;
import com.yitech.cloud.sys.entity.SysLogEntity;
import com.yitech.cloud.vm.dao.*;
import com.yitech.cloud.vm.entity.*;
import com.yitech.cloud.vm.entity.DTO.*;
import com.yitech.cloud.vm.service.VmService;
import com.yitech.cloud.vm.xml.VmXml;

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
		SysLogEntity sysLog = createSysLog("增加虚拟机", vmEntityDTO.getVmName());
		if (vmEntity != null) {
			logFailure(sysLog, "虚拟机名称不能有重复");
			return Result.error("虚拟机名称不能有重复");
		}
		VmEntity vm = packVmDb(vmEntityDTO);
		this.save(vm);
		VmHardwareEntity vmHardware = packVmHardwareDb(vmEntityDTO, vm.getVmId());
		vmHardwareDao.insert(vmHardware);
		try {
			packStorage(vmEntityDTO);
			updateVmStorage(vm, vmHardware, vmEntityDTO);
			handleSecurityGroup(vm, vmEntityDTO);
			HostEntity hostEntity = hostDao.selectById(vmEntityDTO.getHostId());
			VmSwitchEntity vmSwitchEntity = vmSwitchDao.selectById(vmEntityDTO.getVmSwitchId());
			vmEntityDTO.setVmSwitchName(vmSwitchEntity.getVmSwitchName());
			vmEntityDTO.setVmId(vm.getVmId());
			String vmXml = VmXml.vmXml(vmEntityDTO, hostEntity);
			createVm(vm, vmXml);
		} catch (Exception e) {
			handleVmCreationFailure(vm, vmHardware, vmEntityDTO, e, sysLog);
			return Result.error(e.getMessage());
		}
		logSuccess(sysLog);
		return Result.ok();
	}

	private void updateVmStorage(VmEntity vm, VmHardwareEntity vmHardware, VmEntityDTO vmEntityDTO) {
		vm.setStorageVolumeId(vmEntityDTO.getStorageId());
		vmHardware.setVmStorageLocation(vmEntityDTO.getVmStorageLocation());
		vmDao.updateById(vm);
		vmHardwareDao.updateById(vmHardware);
	}

	private void handleSecurityGroup(VmEntity vm, VmEntityDTO vmEntityDTO) {
		SecurityGroupVmEntity securityGroupVm = securityGroupVmDao.queryByVmId(vm.getVmId());
		if (securityGroupVm != null && vmEntityDTO.getSecurityGroupId() != null) {
			securityGroupVm.setSecurityGroupId(vmEntityDTO.getSecurityGroupId());
			securityGroupVmDao.updateById(securityGroupVm);
		} else if (securityGroupVm != null && vmEntityDTO.getSecurityGroupId() == null) {
			securityGroupVmDao.deleteById(securityGroupVm);
		} else {
			SecurityGroupVmEntity securityGroupVmEntity = new SecurityGroupVmEntity();
			securityGroupVmEntity.setSecurityGroupId(vmEntityDTO.getSecurityGroupId());
			securityGroupVmEntity.setVmId(vm.getVmId());
			securityGroupVmDao.insert(securityGroupVmEntity);
		}
	}

	private void handleVmCreationFailure(VmEntity vm, VmHardwareEntity vmHardware, VmEntityDTO vmEntityDTO, Exception e,
			SysLogEntity sysLog) throws Exception {
		this.removeById(vm.getVmId());
		vmHardwareDao.deleteById(vmHardware.getVmHardwareId());
		List<String> storageIds = new ArrayList<>();
		StorageEntity storage = storageDao.selectById(vmEntityDTO.getStorageId());
		if (storage != null) {
			storageIds.add(String.valueOf(vmEntityDTO.getStorageId()));
		}
		storageService.deleteBatch(storageIds, false);
		logger.error("虚拟机创建失败:" + e.getMessage());
		logFailure(sysLog, "虚拟机创建失败:" + e.getMessage());
	}

	private SysLogEntity createSysLog(String operation, String vmName) {
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("虚拟机动作");
		sysLog.setOperObj(vmName);
		sysLog.setOperMark(operation);
		return sysLog;
	}

	private void logFailure(SysLogEntity sysLog, String errorMsg) {
		sysLog.setResult("失败");
		sysLog.setCreateDate(new Date());
		sysLog.setErrorMsg(errorMsg);
		sysLogDao.insert(sysLog);
	}

	private void logSuccess(SysLogEntity sysLog) {
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
	}

	public void packStorage(VmEntityDTO vmEntityDTO) throws Exception {
		try {
			StorageEntity storageVolume = null;
			if (vmEntityDTO.getStoragePoolId() == null) {
				vmEntityDTO.setStoragePoolId(Long.valueOf(Constant.STORAGE_POOL_ID));
			}
			if (vmEntityDTO.getBasicVolumeId() != null) {
				storageVolume = storageDao.selectById(vmEntityDTO.getBasicVolumeId());
				vmEntityDTO.setStoragePoolId(storageVolume.getStoragePoolId());
			}
			StoragePoolEntity storagePoolEntity = storagePoolDao.selectById(vmEntityDTO.getStoragePoolId());
			StorageEntity storageEntity = createStorageEntity(vmEntityDTO, storagePoolEntity, storageVolume);
			Result result = storageService.saveVolume(storageEntity);
			if (Convert.toInt(result.get("code")) != 0 || Convert.toInt(result.get("code")) == null) {
				throw new Exception("生成存储卷失败 : " + result.get("msg"));
			}
			vmEntityDTO.setVmStorageLocation(storageEntity.getStoragePath());
			vmEntityDTO.setStorageId(storageEntity.getStorageId());
		} catch (Exception e) {
			logger.error("虚拟机组装并生成存储卷失败 : " + e.getMessage());
			throw new Exception("生成存储卷失败");
		}
	}

	private StorageEntity createStorageEntity(VmEntityDTO vmEntityDTO, StoragePoolEntity storagePoolEntity,
			StorageEntity storageVolume) {
		StorageEntity storageEntity = new StorageEntity();
		storageEntity.setJudge("0");
		storageEntity.setStorageVolumeName(vmEntityDTO.getVmName());
		storageEntity.setCapacity(vmEntityDTO.getVmDiskSize() == null ? Constant.STORAGE_VOLUME_CAPACITY
				: vmEntityDTO.getVmDiskSize() + "G");
		storageEntity.setFilesystem(Constant.FILE_SYSTEM);
		storageEntity.setCreateFormat(Constant.FILE_SYSTEM);
		storageEntity.setStoragePoolId(storagePoolEntity.getStoragePoolId());
		storageEntity.setStorageType(determineStorageType(storagePoolEntity));
		if (storageVolume != null) {
			storageEntity.setBasicVolumeId(vmEntityDTO.getBasicVolumeId());
		}
		return storageEntity;
	}

	private int determineStorageType(StoragePoolEntity storagePoolEntity) {
		switch (storagePoolEntity.getPoolType()) {
		case "dir":
			return 1;
		case "iscsi":
			return 3;
		case "lvm":
			return 4;
		default:
			throw new IllegalArgumentException("Unknown pool type: " + storagePoolEntity.getPoolType());
		}
	}

	public VmEntity packVmDb(VmEntityDTO vmEntityDTO) {
		VmEntity vm = vmEntityDTO.getVmId() == null ? new VmEntity() : vmDao.selectById(vmEntityDTO.getVmId());
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

	public VmHardwareEntity packVmHardwareDb(VmEntityDTO vmEntityDTO, Long vmId) {
		VmHardwareEntity vmHardwareEntity = vmEntityDTO.getVmHardwareId() == null ? new VmHardwareEntity()
				: vmHardwareDao.queryByVmId(vmEntityDTO.getVmId());
		vmHardwareEntity.setVmId(vmId);
		vmHardwareEntity.setVmCdDriver(vmEntityDTO.getCdDriverId() + "");
		vmHardwareEntity.setVmCpuAduit(vmEntityDTO.getVmCpuAduit());
		vmHardwareEntity.setVmCpuNum(vmEntityDTO.getVmCpuAduit() * vmEntityDTO.getVmCpuSockets());
		vmHardwareEntity.setVmDiskSize(vmEntityDTO.getVmDiskSize());
		vmHardwareEntity.setVmMemSize(vmEntityDTO.getVmMemSize());
		vmHardwareEntity.setVmNetworkMac(vmEntityDTO.getVmNetworkMac());
		vmHardwareEntity.setVmOs(vmEntityDTO.getVmOs());
		vmHardwareEntity.setVmOsPath(vmEntityDTO.getVmOsPath());
		vmHardwareEntity.setVmStorageLocation(vmEntityDTO.getVmStorageLocation());
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
		String vmName = Convert.toStr(params.get("vmName"));
		IPage<VmEntity> page;
		try {
			page = this.page(new Query<VmEntity>().getPage(params),
					new QueryWrapper<VmEntity>().eq(hostId != null, "host_id", hostId)
							.eq(clusterId != null, "cluster_id", clusterId)
							.eq(dataCenterId != null, "data_center_id", dataCenterId)
							.like(StringUtils.isNotBlank(vmName), "vm_name", vmName).orderByDesc("create_time"));
		} catch (Exception e) {
			logger.error("虚拟机查询失败:" + e.getMessage());
			throw new Exception("查询虚拟机失败:" + e.getMessage());
		}
		return new PageUtils(page);
	}

	@Override
	public Result updateVm(VmEntityDTO vmEntityDTO) throws Exception {
		VmEntity vmEntity = vmDao.selectById(vmEntityDTO.getVmId());
		VmHardwareEntity vmHardwareEntity = vmHardwareDao.queryByVmId(vmEntityDTO.getVmId());
		vmEntityDTO.setVmHardwareId(vmHardwareEntity.getVmHardwareId());
		HostEntity hostEntity = hostDao.selectById(vmEntity.getHostId());
		SysLogEntity sysLog = createSysLog("修改虚拟机", vmEntity.getVmName());
		try {
			if (undefineVm(vmEntity, hostEntity)) {
				updateVmStorage(vmEntity, vmHardwareEntity, vmEntityDTO);
				handleSecurityGroup(vmEntity, vmEntityDTO);
				VmSwitchEntity vmSwitchEntity = vmSwitchDao.selectById(vmEntityDTO.getVmSwitchId());
				vmEntityDTO.setVmSwitchName(vmSwitchEntity.getVmSwitchName());
				vmEntityDTO.setVmId(vmEntity.getVmId());
				String vmXml = VmXml.vmXml(vmEntityDTO, hostEntity);
				createVm(vmEntity, vmXml);
			} else {
				logFailure(sysLog, "虚拟机执行undefine失败");
				return Result.error("修改虚拟机失败");
			}
		} catch (Exception e) {
			logFailure(sysLog, "虚拟机修改失败:" + e.getMessage());
			return Result.error("修改虚拟机失败" + e.getMessage());
		}
		logSuccess(sysLog);
		return Result.ok();
	}

	private boolean undefineVm(VmEntity vmEntity, HostEntity hostEntity) throws Exception {
		String command = "virsh undefine " + vmEntity.getVmName();
		String flag = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(),
				CryptUtil.decrypt(hostEntity.getHostPassword()), command);
		return flag.contains("undefined");
	}

	public void createVm(VmEntity vmEntity, String vmXml) throws Exception {
		HostEntity hostEntity = hostDao.selectById(vmEntity.getHostId());
		try {
			Connect connect = connectService.kvmConnect(hostEntity.getOsIp(), hostEntity.getHostUser(),
					Constant.KVM_SSH, 0);
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

	public void asynDistribution(VmEntity vmEntity, String hostIp) {
		CompletableFuture.runAsync(() -> {
			try {
				SecurityGroupVmEntity securityGroupVm = securityGroupVmDao.queryByVmId(vmEntity.getVmId());
				if (securityGroupVm != null) {
					distributeSecurityRules(vmEntity, securityGroupVm, hostIp);
				} else {
					logger.info("securityGroupVm为空");
				}
			} catch (Exception e) {
				logger.error("获取流量控制失败 : " + e.getMessage());
			}
		});
	}

	private void distributeSecurityRules(VmEntity vmEntity, SecurityGroupVmEntity securityGroupVm, String hostIp)
			throws Exception {
		FlowXml flowXml = new FlowXml();
		HostEntity hostEntity = hostDao.selectById(vmEntity.getHostId());
		String command = "virsh domiflist " + vmEntity.getVmName();
		String result = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(),
				CryptUtil.decrypt(hostEntity.getHostPassword()), command);
		List<SecGroupXml> secGroupXmlList = new ArrayList<>();
		SecGroupXml secGroupXml = new SecGroupXml();
		if (securityGroupVm.getSecurityGroupId() != null) {
			SecurityGroupEntity securityGroupEntity = securityGroupDao.selectById(securityGroupVm.getSecurityGroupId());
			secGroupXml.setName(securityGroupEntity.getSecurityGroupName());
			List<SecurityRuleEntity> sucurityRuleList = securityRuleDao
					.queryByGroupId(securityGroupVm.getSecurityGroupId());
			if (!sucurityRuleList.isEmpty()) {
				parseDomiflist(securityGroupVm, result, secGroupXml);
				createRuleXml(sucurityRuleList, secGroupXml);
				secGroupXmlList.add(secGroupXml);
				flowXml.setSecGroupXmlList(secGroupXmlList);
				String xmlFilePath = "/htcloud/scripts/" + vmEntity.getVmId() + "_openflow.xml";
				File xmlFile = new File(xmlFilePath);
				if (!xmlFile.exists()) {
					xmlFile.createNewFile();
				}
				JAXB.marshal(flowXml, xmlFile);
				if (!hostEntity.getOsIp().equals(IPUtils.getIp())) {
					SFTPUtil sftpUtil = new SFTPUtil(hostEntity.getHostUser(),
							CryptUtil.decrypt(hostEntity.getHostPassword()), hostEntity.getOsIp(), 22);
					sftpUtil.upFile("/htcloud/scripts/", vmEntity.getVmId() + "_openflow.xml", xmlFilePath, true);
				}
				String switchCommand = "python3 /htcloud/scripts/openflow.py deploy " + xmlFilePath;
				String flowResult = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(),
						CryptUtil.decrypt(hostEntity.getHostPassword()), switchCommand);
				logger.info(flowResult);
			}
		} else {
			logger.info("securityGroupVm SecurityGroupId为空");
		}
	}

	public void parseDomiflist(SecurityGroupVmEntity securityGroupVmEntity, String result, SecGroupXml secGroupXml)
			throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				new ByteArrayInputStream(result.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")))) {
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
					VmSwitchEntity vmSwitchEntity = vmSwitchDao.queryByName(lineDatas[2].trim());
					if (vmSwitchEntity != null) {
						PortEntity portEntity = new PortEntity();
						portEntity.setPortName(lineDatas[0].trim());
						portEntity.setType(lineDatas[1].trim());
						portEntity.setPortType(1);
						portEntity.setVmSwitchId(vmSwitchEntity.getVmSwitchId());
						portEntity.setModel(lineDatas[3].trim());
						portEntity.setMac(lineDatas[4].trim());
						portDao.insert(portEntity);
						securityGroupVmEntity.setPortId(portEntity.getPortId());
						securityGroupVmDao.updateById(securityGroupVmEntity);
					}
				}
				currentLine++;
			}
		}
	}

	public void createRuleXml(List<SecurityRuleEntity> ruleList, SecGroupXml secGroupXml) {
		List<RuleXml> ruleXmlList = new ArrayList<>();
		for (SecurityRuleEntity rule : ruleList) {
			RuleXml ruleXml = new RuleXml();
			ruleXml.setDirection(rule.getInOutFlow());
			ruleXml.setProtocol(rule.getAgreeType().toLowerCase());
			ruleXml.setAction(rule.getAction());
			ruleXml.setSourceIp(rule.getSourceIp());
			ruleXml.setSourceMask(rule.getSourceMask());
			if (rule.getSourcePort() != null) {
				ruleXml.setSourcePort(String.valueOf(rule.getSourcePort()));
			}
			ruleXml.setDestIp(rule.getDestIp());
			ruleXml.setDestMask(rule.getDestMask());
			if (rule.getDestPort() != null) {
				ruleXml.setDestPort(String.valueOf(rule.getDestPort()));
			}
			ruleXmlList.add(ruleXml);
		}
		secGroupXml.setRuleXmlList(ruleXmlList);
	}

	@Override
	@Transactional
	public Result deleteVm(List<String> vmIds, Boolean rbFlag) throws Exception {
		List<SysLogEntity> sysLogList = new ArrayList<>();
		for (String vmId : vmIds) {
			VmEntity vmEntity = this.getById(vmId);
			SysLogEntity sysLog = createSysLog("删除虚拟机", vmEntity.getVmName());
			try {
				if (deleteVmEntity(vmEntity, rbFlag, sysLog)) {
					sysLog.setResult("成功");
				}
			} catch (Exception e) {
				logger.error("虚拟机删除失败:" + e.getMessage());
				logFailure(sysLog, "虚拟机删除失败:" + e.getMessage());
			}
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			sysLogList.add(sysLog);
		}
		return Result.ok().put("sysLogList", sysLogList);
	}

	private boolean deleteVmEntity(VmEntity vmEntity, Boolean rbFlag, SysLogEntity sysLog) throws Exception {
		if (booleanDelete(vmEntity, sysLog)) {
			deleteVncConfig(vmEntity.getVmId());
			SecurityGroupVmEntity securityGroupVm = securityGroupVmDao.queryByVmId(vmEntity.getVmId());
			securityGroupVmDao.deleteById(securityGroupVm.getId());
			VmHardwareEntity vmHardwareEntity = vmHardwareDao.queryByVmId(vmEntity.getVmId());
			if (vmHardwareEntity != null) {
				vmHardwareDao.deleteById(vmHardwareEntity.getVmHardwareId());
			}
			this.removeById(vmEntity.getVmId());
			if (rbFlag) {
				List<String> storageIds = new ArrayList<>();
				StorageEntity storage = storageDao.selectById(vmEntity.getStorageVolumeId());
				if (storage != null) {
					storageIds.add(String.valueOf(storage.getStorageId()));
				}
				storageService.deleteBatch(storageIds, true);
			}
			return true;
		}
		return false;
	}

	private Boolean booleanDelete(VmEntity vmEntity, SysLogEntity sysLog) {
		try {
			HostEntity host = hostDao.selectById(vmEntity.getHostId());
			Connect connect = connectService.kvmConnect(host.getOsIp(), host.getHostUser(), Constant.KVM_SSH, 0);
			Domain domain = connect.domainLookupByName(vmEntity.getVmName());
			if (domain.getInfo().state.equals(DomainState.VIR_DOMAIN_SHUTOFF)) {
				domain.undefine();
			} else {
				domain.destroy();
				domain.undefine();
			}
			return true;
		} catch (Exception e) {
			logFailure(sysLog, vmEntity.getVmName() + "删除失败 : " + e.getMessage());
			logger.error("链接虚拟机，并删除失败:" + e.getMessage());
			return false;
		}
	}

	@Override
	public Result startVm(Long[] vmIds) throws Exception {
		List<SysLogEntity> sysLogList = new ArrayList<>();
		for (Long vmId : vmIds) {
			VmEntity vmEntity = vmDao.selectById(vmId);
			SysLogEntity sysLog = createSysLog("启动虚拟机", vmEntity.getVmName());
			try {
				if (booleanStartVm(vmEntity, sysLog)) {
					vmDao.updateState("运行", vmEntity.getVmName());
					logSuccess(sysLog);
					HostEntity host = hostDao.selectById(vmEntity.getHostId());
					asynDistribution(vmEntity, host.getOsIp());
				}
			} catch (Exception e) {
				logger.error("虚拟机启动失败:" + e.getMessage());
				logFailure(sysLog, "虚拟机启动失败:" + e.getMessage());
			}
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			sysLogList.add(sysLog);
		}
		return Result.ok().put("sysLogList", sysLogList);
	}

	private Boolean booleanStartVm(VmEntity vmEntity, SysLogEntity sysLog) throws Exception {
		try {
			HostEntity host = hostDao.selectById(vmEntity.getHostId());
			Connect connect = connectService.kvmConnect(host.getOsIp(), host.getHostUser(), Constant.KVM_SSH, 0);
			Domain domain = connect.domainLookupByName(vmEntity.getVmName());
			if (domain.getInfo().state == DomainState.VIR_DOMAIN_RUNNING && "关机".equals(vmEntity.getState())) {
				return true;
			} else if (domain.getInfo().state == DomainState.VIR_DOMAIN_PAUSED) {
				domain.resume();
				return true;
			}
			domain.create();
			return true;
		} catch (Exception e) {
			logFailure(sysLog, vmEntity.getVmName() + "启动失败 : " + e.getMessage());
			logger.error("虚拟机启动链接失败" + e.getMessage());
			return false;
		}
	}

	@Override
	public Result shutDownVm(Long[] vmIds) throws Exception {
		List<SysLogEntity> sysLogList = new ArrayList<>();
		for (Long vmId : vmIds) {
			VmEntity vmEntity = vmDao.selectById(vmId);
			SysLogEntity sysLog = createSysLog("关闭虚拟机", vmEntity.getVmName());
			try {
				if (booleanShutDown(vmEntity, sysLog)) {
					deleteVncConfig(vmId);
					vmDao.updateState("关机", vmEntity.getVmName());
					logSuccess(sysLog);
				}
			} catch (Exception e) {
				logger.error("虚拟机关机失败:" + e.getMessage());
				logFailure(sysLog, "虚拟机关机失败:" + e.getMessage());
			}
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			sysLogList.add(sysLog);
		}
		return Result.ok().put("sysLogList", sysLogList);
	}

	private Boolean booleanShutDown(VmEntity vmEntity, SysLogEntity sysLog) throws Exception {
		try {
			HostEntity host = hostDao.selectById(vmEntity.getHostId());
			Connect connect = connectService.kvmConnect(host.getOsIp(), host.getHostUser(), Constant.KVM_SSH, 0);
			Domain domain = connect.domainLookupByName(vmEntity.getVmName());
			domain.shutdown();
			return true;
		} catch (Exception e) {
			logFailure(sysLog, vmEntity.getVmName() + "关机失败 : " + e.getMessage());
			logger.error("虚拟机链接关机失败:" + e.getMessage());
			return false;
		}
	}

	@Override
	public Result suspendVm(Long[] vmIds) throws Exception {
		List<SysLogEntity> sysLogList = new ArrayList<>();
		for (Long vmId : vmIds) {
			VmEntity vmEntity = vmDao.selectById(vmId);
			SysLogEntity sysLog = createSysLog("挂起虚拟机", vmEntity.getVmName());
			try {
				if (booleanSuspend(vmEntity, sysLog)) {
					vmDao.updateState("挂起", vmEntity.getVmName());
					logSuccess(sysLog);
				}
			} catch (Exception e) {
				logger.error("虚拟机挂起失败:" + e.getMessage());
				logFailure(sysLog, "虚拟机挂起失败:" + e.getMessage());
			}
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			sysLogList.add(sysLog);
		}
		return Result.ok().put("sysLogList", sysLogList);
	}

	private Boolean booleanSuspend(VmEntity vmEntity, SysLogEntity sysLog) {
		try {
			HostEntity host = hostDao.selectById(vmEntity.getHostId());
			Connect connect = connectService.kvmConnect(host.getOsIp(), host.getHostUser(), Constant.KVM_SSH, 0);
			Domain domain = connect.domainLookupByName(vmEntity.getVmName());
			domain.suspend();
			return true;
		} catch (Exception e) {
			logFailure(sysLog, vmEntity.getVmName() + "挂起失败 : " + e.getMessage());
			logger.error("虚拟机链接，挂起失败:" + e.getMessage());
			return false;
		}
	}

	@Override
	public Result resumeVm(Long[] vmIds) throws Exception {
		List<SysLogEntity> sysLogList = new ArrayList<>();
		for (Long vmId : vmIds) {
			VmEntity vmEntity = vmDao.selectById(vmId);
			SysLogEntity sysLog = createSysLog("恢复挂起虚拟机", vmEntity.getVmName());
			try {
				if (booleanResume(vmEntity, sysLog)) {
					vmDao.updateState("运行", vmEntity.getVmName());
					logSuccess(sysLog);
				}
			} catch (Exception e) {
				logger.error("虚拟机恢复挂起失败:" + e.getMessage());
				logFailure(sysLog, "虚拟机恢复挂起失败:" + e.getMessage());
			}
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			sysLogList.add(sysLog);
		}
		return Result.ok().put("sysLogList", sysLogList);
	}

	private Boolean booleanResume(VmEntity vmEntity, SysLogEntity sysLog) {
		try {
			HostEntity host = hostDao.selectById(vmEntity.getHostId());
			Connect connect = connectService.kvmConnect(host.getOsIp(), host.getHostUser(), Constant.KVM_SSH, 0);
			Domain domain = connect.domainLookupByName(vmEntity.getVmName());
			domain.resume();
			return true;
		} catch (Exception e) {
			logFailure(sysLog, vmEntity.getVmName() + "恢复挂起失败 : " + e.getMessage());
			logger.error("链接虚拟机，并恢复挂起失败:" + e.getMessage());
			return false;
		}
	}

	@Override
	public Result restartVm(Long[] vmIds) throws Exception {
		List<SysLogEntity> sysLogList = new ArrayList<>();
		for (Long vmId : vmIds) {
			VmEntity vmEntity = vmDao.selectById(vmId);
			SysLogEntity sysLog = createSysLog("重启虚拟机", vmEntity.getVmName());
			try {
				if (booleanRestart(vmEntity, sysLog)) {
					logSuccess(sysLog);
				}
			} catch (Exception e) {
				logger.error("虚拟机重启失败:" + e.getMessage());
				logFailure(sysLog, "虚拟机重启失败:" + e.getMessage());
			}
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			sysLogList.add(sysLog);
		}
		return Result.ok().put("sysLogList", sysLogList);
	}

	private Boolean booleanRestart(VmEntity vmEntity, SysLogEntity sysLog) {
		try {
			HostEntity host = hostDao.selectById(vmEntity.getHostId());
			Connect connect = connectService.kvmConnect(host.getOsIp(), host.getHostUser(), Constant.KVM_SSH, 0);
			Domain domain = connect.domainLookupByName(vmEntity.getVmName());
			domain.reboot(1);
			return true;
		} catch (Exception e) {
			logFailure(sysLog, vmEntity.getVmName() + "重启失败 : " + e.getMessage());
			logger.error("连接虚拟机，并重启失败:" + e.getMessage());
			return false;
		}
	}

	@Override
	public VmEntity queryByVmName(String vmName) {
		return vmDao.queryByVmName(vmName);
	}

	@Override
	public Result moveVm(VmMoveEnityDTO vmMoveEnityDTO) {
		List<SysLogEntity> sysLogList = new ArrayList<>();
		List<Integer> storageTypes = new ArrayList<>();
		HostEntity hostEntity = hostDao.selectById(vmMoveEnityDTO.getHostId());
		Long[] vmIds = vmMoveEnityDTO.getVmIds();
		if (vmMoveEnityDTO.getHostId().equals(vmMoveEnityDTO.getDestHostId())) {
			return Result.error("虚拟机不允许迁移到同一台主机，请选择另一台主机。");
		}
		String moveType = vmMoveEnityDTO.getMoveType();
		for (Long vmId : vmIds) {
			VmEntity vmEntity = this.getById(vmId);
			SysLogEntity sysLog = createSysLog("迁移虚拟机", vmEntity.getVmName());
			StorageEntity storageEntity = storageDao.selectById(vmEntity.getStorageVolumeId());
			if (storageEntity == null) {
				logFailure(sysLog, "存储卷不存在");
				return Result.error("存储卷不存在");
			} else {
				storageTypes.add(storageEntity.getStorageType());
			}
			HostEntity destHostEntity = hostDao.selectById(vmMoveEnityDTO.getDestHostId());
			if ("2".equals(moveType)) {
				if (storageTypes.contains(Integer.valueOf(moveType))) {
					logFailure(sysLog, "热迁移不支持本地存储");
					return Result.error("热迁移不支持本地存储");
				}
				liveMigrate(vmEntity, hostEntity, destHostEntity, sysLog);
			} else {
				clodMigrate(vmEntity, hostEntity, destHostEntity, sysLog);
			}
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			sysLogList.add(sysLog);
		}
		return Result.ok().put("sysLogList", sysLogList);
	}

	public void liveMigrate(VmEntity vmEntity, HostEntity hostEntity, HostEntity destHostEntity, SysLogEntity sysLog) {
		try {
			Connect connect = connectService.kvmConnect(hostEntity.getOsIp(), hostEntity.getHostUser(),
					Constant.KVM_SSH, 0);
			Domain domain = connect.domainLookupByName(vmEntity.getVmName());
			String hostUser = hostEntity.getHostUser();
			String destUrl = "qemu+" + Constant.KVM_SSH + "://" + hostUser + "@" + destHostEntity.getOsIp()
					+ ":22/system";
			int migrateFlag = domain.migrateToURI(destUrl, 0, vmEntity.getVmName(), 0);
			if (migrateFlag == 0) {
				vmEntity.setHostId(destHostEntity.getHostId());
				this.updateById(vmEntity);
				logSuccess(sysLog);
			} else {
				logFailure(sysLog, "调用libvirt迁移失败");
			}
		} catch (Exception e) {
			logFailure(sysLog, vmEntity.getVmName() + "热迁移失败 : " + e.getMessage());
			logger.error(vmEntity.getVmName() + "热迁移失败 : " + e.getMessage());
		}
	}

	public void clodMigrate(VmEntity vmEntity, HostEntity hostEntity, HostEntity destHostEntity, SysLogEntity sysLog) {
		try {
			VmHardwareEntity vmHardwareEntity = vmHardwareDao.queryByVmId(vmEntity.getVmId());
			String cpCommand = "scp root@" + hostEntity.getOsIp() + ":" + vmHardwareEntity.getVmOsPath() + " "
					+ vmHardwareEntity.getVmOsPath().substring(0, vmHardwareEntity.getVmOsPath().lastIndexOf("/"));
			int cpFlag = SshUtil.sshShell(destHostEntity.getOsIp(), 22, destHostEntity.getHostUser(),
					CryptUtil.decrypt(destHostEntity.getHostPassword()), cpCommand);
			String command = "virsh migrate --verbose --unsafe " + vmEntity.getVmName() + " qemu+ssh://"
					+ destHostEntity.getOsIp() + "/system tcp://" + destHostEntity.getOsIp();
			int migrateFlag = SshUtil.sshShell(hostEntity.getOsIp(), 22, hostEntity.getHostUser(),
					CryptUtil.decrypt(hostEntity.getHostPassword()), command);
			if (cpFlag == 0 && migrateFlag == 0) {
				vmEntity.setHostId(destHostEntity.getHostId());
				this.updateById(vmEntity);
				logSuccess(sysLog);
			} else {
				logFailure(sysLog, "调用libvirt迁移失败");
			}
		} catch (Exception e) {
			logFailure(sysLog, vmEntity.getVmName() + "冷迁移失败 : " + e.getMessage());
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
		macAddr[0] = (byte) (macAddr[0] & (byte) 254);
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
			if (storageEntity.getBasicVolumeId() != null) {
				basicStorageEntity = storageDao.selectById(storageEntity.getBasicVolumeId());
			}
			SecurityGroupVmEntity securityGroupVm = securityGroupVmDao.queryByVmId(vmId);
			if (securityGroupVm != null && securityGroupVm.getSecurityGroupId() != null) {
				securityGroup = securityGroupDao.selectById(securityGroupVm.getSecurityGroupId());
			}
			Connect connect = connectService.kvmConnect(host.getOsIp(), host.getHostUser(), Constant.KVM_SSH, 0);
			Domain domain = connect.domainLookupByName(vmEntity.getVmName());
			domainInfo = domain.getInfo();
			if (domainInfo.state == DomainState.VIR_DOMAIN_RUNNING) {
				vmEntity.setState("运行");
			} else if (domainInfo.state == DomainState.VIR_DOMAIN_SHUTDOWN
					|| domainInfo.state == DomainState.VIR_DOMAIN_SHUTOFF) {
				vmEntity.setState("关机");
			} else if (domainInfo.state == DomainState.VIR_DOMAIN_PAUSED) {
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
		List<SysLogEntity> sysLogList = new ArrayList<>();
		for (Long vmId : vmIds) {
			VmEntity vmEntity = vmDao.selectById(vmId);
			SysLogEntity sysLog = createSysLog("虚拟机关闭电源", vmEntity.getVmName());
			try {
				if (booleanDestroy(vmEntity, sysLog)) {
					deleteVncConfig(vmId);
					vmDao.updateState("关机", vmEntity.getVmName());
					logSuccess(sysLog);
				}
			} catch (Exception e) {
				logger.error("虚拟机关闭电源失败:" + e.getMessage());
				logFailure(sysLog, "虚拟机关闭电源失败:" + e.getMessage());
			}
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			sysLogList.add(sysLog);
		}
		return Result.ok().put("sysLogList", sysLogList);
	}

	private Boolean booleanDestroy(VmEntity vmEntity, SysLogEntity sysLog) throws Exception {
		try {
			HostEntity host = hostDao.selectById(vmEntity.getHostId());
			Connect connect = connectService.kvmConnect(host.getOsIp(), host.getHostUser(), Constant.KVM_SSH, 0);
			Domain domain = connect.domainLookupByName(vmEntity.getVmName());
			domain.destroy();
			return true;
		} catch (Exception e) {
			logFailure(sysLog, vmEntity.getVmName() + "虚拟机关闭电源失败:" + e.getMessage());
			logger.error("虚拟机关闭电源失败:" + e.getMessage());
			return false;
		}
	}

	@Override
	public Result deleteDbVm(Long[] vmIds) {
		for (Long vmId : vmIds) {
			VmEntity vmEntity = this.getById(vmId);
			try {
				this.removeById(vmId);
				VmHardwareEntity vmHardwareEntity = vmHardwareDao.queryByVmId(vmId);
				if (vmHardwareEntity != null) {
					vmHardwareDao.deleteById(vmHardwareEntity.getVmHardwareId());
				}
				List<String> storageIds = new ArrayList<>();
				StorageEntity storage = storageDao.selectById(vmEntity.getStorageVolumeId());
				if (storage != null) {
					storageIds.add(String.valueOf(storage.getStorageId()));
				}
				storageService.deleteBatch(storageIds, false);
			} catch (Exception e) {
				logger.error("虚拟机删除失败:" + e.getMessage());
			}
		}
		return Result.ok();
	}

	public void deleteVncConfig(Long vmId) throws Exception {
		VmEntity vmEntity = this.getById(vmId);
		HostEntity hostEntity = hostDao.selectById(vmEntity.getHostId());
		String filePath = CloudConfig.getNoVNCPath() + "vnc/noVNC-master/utils/websockify/token/";
		String fileName = hostEntity.getOsIp() + "_token.conf";
		String command = "sed -i '/" + vmEntity.getVmName() + ": " + hostEntity.getOsIp() + "/d'" + " " + filePath
				+ fileName;
		int delFlag = SshUtil.sshShell(hostEntity.getOsIp(), 22, hostEntity.getHostUser(),
				CryptUtil.decrypt(hostEntity.getHostPassword()), command);
		if (delFlag != 0) {
			logger.error("删除novnc配置文件匹配行失败");
		}
	}

	@Override
	public Result vncUrl(Long vmId) {
		VmEntity vm = vmDao.selectById(vmId);
		String vmName = vm.getVmName();
		HostEntity host = hostDao.selectById(vm.getHostId());
		FileOutputStream outputStream = null;
		String filePath = CloudConfig.getNoVNCPath() + "vnc/noVNC-master/utils/websockify/token/";
		String fileName = host.getOsIp() + "_token.conf";
		File file = new File(filePath + fileName);
		String url;
		try {
			String vncPortCommand = "virsh vncdisplay " + vmName;
			String vncPortResult = SshUtil.sshExecute(host.getOsIp(), 22, host.getHostUser(),
					CryptUtil.decrypt(host.getHostPassword()), vncPortCommand);
			String vncPort = vncPortResult.substring(1).replaceAll("\n", "");
			vncPort = vncPort.length() == 1 ? ":590" + vncPort : ":59" + vncPort;
			SFTPUtil sftpUtil = new SFTPUtil(host.getHostUser(), CryptUtil.decrypt(host.getHostPassword()),
					host.getOsIp(), 22);
			if (!host.getOsIp().equals(IPUtils.getIp())) {
				if (!sftpUtil.downFile(filePath, fileName, filePath, fileName, false)) {
					file.createNewFile();
				}
			} else {
				if (!file.exists()) {
					file.createNewFile();
				}
			}
			String writeData = vmName + ": " + host.getOsIp() + vncPort + "\n";
			if (!isTokenConfigExists(file, vmName, host.getOsIp() + vncPort)) {
				outputStream = new FileOutputStream(filePath + fileName, true);
				outputStream.write(writeData.getBytes());
				if (!host.getOsIp().equals(IPUtils.getIp())) {
					sftpUtil.upFile(filePath, fileName, filePath + fileName, true);
				}
			}
			url = "http://" + host.getOsIp() + ":6080/vnc_lite.html?path=websockify/?token=" + vmName;
		} catch (Exception e) {
			logger.error("获取vnc url失败 : " + e.getMessage());
			return Result.error("获取vnc url失败 : " + e.getMessage());
		} finally {
			try {
				if (!host.getOsIp().equals(IPUtils.getIp())) {
					file.delete();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return Result.ok(url);
	}

	private boolean isTokenConfigExists(File file, String vmName, String vncPort) throws IOException {
		List<String> lines = Files.readLines(file, Charsets.UTF_8);
		for (String line : lines) {
			if (line.contains(vmName + ": " + vncPort)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<VmEntity> queryList(Map<String, Object> params) {
		Long hostId = Convert.toLong(params.get("hostId"));
		Long clusterId = Convert.toLong(params.get("clusterId"));
		Long dataCenterId = Convert.toLong(params.get("dataCenterId"));
		String vmName = Convert.toStr(params.get("vmName"));
		Integer page = Convert.toInt(params.get("page"));
		Integer limit = Convert.toInt(params.get("limit"));
		return vmDao.queryList(dataCenterId, clusterId, hostId, vmName, page - 1, limit);
	}
}