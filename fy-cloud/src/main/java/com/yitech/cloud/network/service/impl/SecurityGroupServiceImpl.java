/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月10日
 */
package com.yitech.cloud.network.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yitech.cloud.common.utils.PageUtils;
import com.yitech.cloud.common.utils.Query;
import com.yitech.cloud.common.utils.Result;
import com.yitech.cloud.common.utils.SysLogUtil;
import com.yitech.cloud.common.utils.text.Convert;
import com.yitech.cloud.network.dao.SecurityGroupDao;
import com.yitech.cloud.network.dao.SecurityGroupVmDao;
import com.yitech.cloud.network.entity.SecurityGroupEntity;
import com.yitech.cloud.network.entity.SecurityGroupVmEntity;
import com.yitech.cloud.network.service.SecurityGroupService;
import com.yitech.cloud.sys.dao.SysLogDao;
import com.yitech.cloud.sys.entity.SysLogEntity;

/**
 * 安全组接口实现层
 * @author fangyi
 *
 */
@Service("securityGroupService")
public class SecurityGroupServiceImpl extends ServiceImpl<SecurityGroupDao, SecurityGroupEntity> implements SecurityGroupService{
	@Autowired
	private SecurityGroupVmDao securityGroupVmDao;
	@Autowired
	private SecurityGroupDao securityGroupDao;
	@Autowired
	private SysLogDao sysLogDao;
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String securityGroupName = Convert.toStr(params.get("securityGroupName"));
		IPage<SecurityGroupEntity> page = this.page(
			new Query<SecurityGroupEntity>().getPage(params),
			new QueryWrapper<SecurityGroupEntity>()
				.like(StringUtils.isNotBlank(securityGroupName),"security_group_name", securityGroupName)
		);
		return new PageUtils(page);
	}

	@Override
	public Result deleteBatch(Long[] securityGroupIds) {
		List<SysLogEntity> sysLogList = new ArrayList<SysLogEntity>();
		for(int i = 0;i<securityGroupIds.length;i++) {
			SecurityGroupEntity securityGroup = securityGroupDao.selectById(securityGroupIds[i]);
			SysLogEntity sysLog = new SysLogEntity();
			sysLog.setUsername(SysLogUtil.getUserName());
			sysLog.setIp(SysLogUtil.getLoginIp());
			sysLog.setOperation("安全组动作");
			sysLog.setOperMark("删除安全组");
			sysLog.setOperObj(securityGroup.getSecurityGroupName());
			//查询安全组下是否存在关联虚拟机
			List<SecurityGroupVmEntity> securityGroupVmList = securityGroupVmDao.queryByGroupId(securityGroupIds[i]);
			if(securityGroupVmList.size() == 0) {//表示可删除
				sysLog.setResult("成功");
				this.removeById(securityGroupIds[i]);
			}else {
				sysLog.setResult("失败");
				sysLog.setErrorMsg("该安全组已被虚拟机使用");
			}
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			sysLogList.add(sysLog);
		}
		return Result.ok().put("sysLogList", sysLogList);
	}

	@Override
	public Result saveGroup(SecurityGroupEntity securityGroup) {
		SecurityGroupEntity securityGroupEntity = securityGroupDao.queryByName(securityGroup.getSecurityGroupName());
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("安全组动作");
		sysLog.setOperObj(securityGroup.getSecurityGroupName());
		sysLog.setOperMark("增加安全组");
		if(securityGroupEntity != null) {//查询是否重复
			sysLog.setResult("失败");
			sysLog.setCreateDate(new Date());
			sysLog.setErrorMsg("该安全组名称已存在");
			sysLogDao.insert(sysLog);
			return Result.error("该安全组名称已存在");
		}
		this.save(securityGroup);
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		return Result.ok();
		
		
	}

	@Override
	public Result updateGroup(SecurityGroupEntity securityGroup) {
		SecurityGroupEntity securityGroupEntity = securityGroupDao.queryByName(securityGroup.getSecurityGroupName());
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("安全组动作");
		sysLog.setOperObj(securityGroup.getSecurityGroupName());
		sysLog.setOperMark("修改安全组");
		if(securityGroupEntity != null && securityGroup.getSecurityGroupId() != securityGroupEntity.getSecurityGroupId()) {
			sysLog.setResult("失败");
			sysLog.setCreateDate(new Date());
			sysLog.setErrorMsg("该安全组名称已存在");
			sysLogDao.insert(sysLog);
			return Result.error("该安全组名称已存在");
		}
		this.updateById(securityGroup);
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		return Result.ok();
	}
	
}
