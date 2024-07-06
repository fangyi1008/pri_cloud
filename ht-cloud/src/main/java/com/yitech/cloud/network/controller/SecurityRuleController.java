/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月10日
 */
package com.yitech.cloud.network.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.xml.bind.JAXB;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yitech.cloud.common.utils.AddGroup;
import com.yitech.cloud.common.utils.IPUtils;
import com.yitech.cloud.common.utils.PageUtils;
import com.yitech.cloud.common.utils.Result;
import com.yitech.cloud.common.utils.SysLogUtil;
import com.yitech.cloud.common.utils.UpdateGroup;
import com.yitech.cloud.common.utils.ValidatorUtils;
import com.yitech.cloud.common.utils.crypt.CryptUtil;
import com.yitech.cloud.common.utils.sftp.SFTPUtil;
import com.yitech.cloud.common.utils.ssh.SshUtil;
import com.yitech.cloud.host.dao.HostDao;
import com.yitech.cloud.host.entity.HostEntity;
import com.yitech.cloud.network.dao.PortDao;
import com.yitech.cloud.network.dao.SecurityGroupDao;
import com.yitech.cloud.network.dao.SecurityGroupVmDao;
import com.yitech.cloud.network.dao.SecurityRuleDao;
import com.yitech.cloud.network.dao.VmSwitchDao;
import com.yitech.cloud.network.entity.PortEntity;
import com.yitech.cloud.network.entity.SecurityGroupEntity;
import com.yitech.cloud.network.entity.SecurityGroupVmEntity;
import com.yitech.cloud.network.entity.SecurityRuleEntity;
import com.yitech.cloud.network.entity.VmSwitchEntity;
import com.yitech.cloud.network.service.SecurityRuleService;
import com.yitech.cloud.network.xml.FlowXml;
import com.yitech.cloud.network.xml.PortXml;
import com.yitech.cloud.network.xml.RuleXml;
import com.yitech.cloud.network.xml.SecGroupXml;
import com.yitech.cloud.sys.controller.AbstractController;
import com.yitech.cloud.sys.dao.SysLogDao;
import com.yitech.cloud.sys.entity.SysLogEntity;
import com.yitech.cloud.vm.dao.VmDao;
import com.yitech.cloud.vm.entity.VmEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 安全规则管理
 * @author fangyi
 *
 */
@Api("安全规则管理")
@RestController
@RequestMapping("/securityRule")
public class SecurityRuleController extends AbstractController{
	@Autowired
	private SecurityRuleService securityRuleService;
	@Autowired
	private SysLogDao sysLogDao;
	@Autowired
	private SecurityGroupVmDao securityGroupVmDao;
	@Autowired
	private VmDao vmDao;
	@Autowired
	private HostDao hostDao;
	@Autowired
	private PortDao portDao;
	@Autowired
	private SecurityGroupDao securityGroupDao;
	@Autowired
	private SecurityRuleDao securityRuleDao;
	@Autowired
	private VmSwitchDao vmSwitchDao;
	/**
	 * 所有安全规则列表
	 */
	@ApiOperation("安全规则列表")
	@RequestMapping(value="/list",method=RequestMethod.POST)
	@RequiresPermissions("network:securityRule:list")
	public Result list(@RequestBody Map<String, Object> params){
		PageUtils page = securityRuleService.queryPage(params);
		return Result.ok().put("page", page);
	}
	
	/**
	 * 获取安全规则信息
	 */
	@ApiOperation("获取安全规则信息")
	@GetMapping("/info/{securityRuleId}")
	@RequiresPermissions("network:securityRule:info")
	public Result info(@PathVariable("securityRuleId") Long securityRuleId){
		SecurityRuleEntity securityRule = securityRuleService.getById(securityRuleId);
		return Result.ok().put("securityRule", securityRule);
	}
	
	/**
	 * 保存安全规则
	 */
	@ApiOperation("保存安全规则")
	@PostMapping("/save")
	@RequiresPermissions("network:securityRule:save")
	public Result save(@RequestBody SecurityRuleEntity securityRule){
		ValidatorUtils.validateEntity(securityRule, AddGroup.class);
		securityRule.setAgreeType(securityRule.getAgreeType().toLowerCase());
		securityRule.setCreateUserId(getUserId());
		securityRule.setCreateTime(new Date());
		securityRuleService.save(securityRule);
		//异步下发安全规则
		asynDistribution(securityRule);
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("安全规则动作");
		sysLog.setOperObj(securityRule.getRuleId()+"");
		sysLog.setOperMark("增加安全规则");
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		return Result.ok().put("sysLog", sysLog);
	}
	
	/**
	 * 
	 */
	public void asynDistribution(SecurityRuleEntity securityRule) {
		CompletableFuture.runAsync(new Runnable() {// 异步执行
			@Override
			public void run() {
				try {
					List<SecurityGroupVmEntity> securityGroupVmList = securityGroupVmDao.queryByGroupId(securityRule.getSecurityGroupId());//根据安全组id查询安全组与虚拟机关联
					if(securityGroupVmList.size() > 0) {
						for(int i =0;i<securityGroupVmList.size();i++) {
							SecurityGroupVmEntity securityGroupVm = securityGroupVmList.get(i);
							if(securityGroupVm != null) {
								VmEntity vmEntity = vmDao.selectById(securityGroupVm.getVmId());
								if(vmEntity != null) {
									logger.info("虚拟机 "+vmEntity.getVmName() +"下发规则");
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
									}
								}
							}
							
						}
					}
				}catch (Exception e) {
					logger.error("下发网络规则失败 {}",e.getMessage());
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
	
	/**
	 * 修改安全规则
	 */
	@ApiOperation("修改安全规则")
	@PostMapping("/update")
	@RequiresPermissions("network:securityRule:update")
	public Result update(@RequestBody SecurityRuleEntity securityRule){
		ValidatorUtils.validateEntity(securityRule, UpdateGroup.class);
		securityRule.setCreateUserId(getUserId());
		securityRuleService.updateById(securityRule);
		//异步下发安全规则
		asynDistribution(securityRule);
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("安全规则动作");
		sysLog.setOperObj(securityRule.getRuleId()+"");
		sysLog.setOperMark("修改安全规则");
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		return Result.ok().put("sysLog", sysLog);
	}
	
	/**
	 * 删除安全规则
	 */
	@ApiOperation("删除安全规则")
	@PostMapping("/delete")
	@RequiresPermissions("network:securityRule:delete")
	public Result delete(@RequestBody Long[] securityRuleIds){
		List<SysLogEntity> sysLogList = new ArrayList<SysLogEntity>();
		for(int i = 0;i<securityRuleIds.length;i++) {
			SysLogEntity sysLog = new SysLogEntity();
			sysLog.setUsername(SysLogUtil.getUserName());
			sysLog.setIp(SysLogUtil.getLoginIp());
			sysLog.setOperation("安全规则动作");
			SecurityRuleEntity securityRule = securityRuleDao.selectById(securityRuleIds[i]);
			asynDistribution(securityRule);
			securityRuleDao.deleteById(securityRuleIds[i]);
			sysLog.setOperObj(securityRuleIds[i]+"");
			sysLog.setOperMark("删除安全规则");
			sysLog.setResult("成功");
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			sysLogList.add(sysLog);
		}
		return Result.ok().put("sysLogList", sysLogList);
	}
}
