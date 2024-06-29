/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.vm.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.libvirt.Connect;
import org.libvirt.Domain;
import org.libvirt.DomainSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hontosec.cloud.common.service.ConnectService;
import com.hontosec.cloud.common.utils.Constant;
import com.hontosec.cloud.common.utils.PageUtils;
import com.hontosec.cloud.common.utils.Query;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.common.utils.SysLogUtil;
import com.hontosec.cloud.common.utils.crypt.CryptUtil;
import com.hontosec.cloud.common.utils.ssh.SshUtil;
import com.hontosec.cloud.common.utils.text.Convert;
import com.hontosec.cloud.host.dao.HostDao;
import com.hontosec.cloud.host.entity.HostEntity;
import com.hontosec.cloud.sys.dao.SysLogDao;
import com.hontosec.cloud.sys.entity.SysLogEntity;
import com.hontosec.cloud.vm.dao.VmDao;
import com.hontosec.cloud.vm.dao.VmSnapshotDao;
import com.hontosec.cloud.vm.entity.VmEntity;
import com.hontosec.cloud.vm.entity.VmSnapshotEntity;
import com.hontosec.cloud.vm.entity.DTO.VmSnapshotDTO;
import com.hontosec.cloud.vm.service.VmSnapShotService;

/**
 * 虚拟机快照接口实现层
 * @author fangyi
 *
 */
@Service("vmSnapShotService")
public class VmSnapShotServiceImpl extends ServiceImpl<VmSnapshotDao, VmSnapshotEntity> implements VmSnapShotService {

    @Autowired
    private VmDao vmDao;

    @Autowired
    private HostDao hostDao;

    @Autowired
    private VmSnapshotDao vmSnapshotDao;
    @Autowired
    private SysLogDao sysLogDao;
    @Autowired
    private ConnectService connectService;

    private static final Logger logger = LoggerFactory.getLogger(VmServiceImpl.class);

    @Override
    public void addVmSnapShot(VmSnapshotEntity vmSnapshotEntity) throws Exception {
    	VmEntity vmEntity = vmDao.selectById(vmSnapshotEntity.getVmId());
    	SysLogEntity sysLog = new SysLogEntity();
    	sysLog.setUsername(SysLogUtil.getUserName());
    	sysLog.setIp(SysLogUtil.getLoginIp());
    	sysLog.setOperation("虚拟机动作");
    	sysLog.setOperObj(vmSnapshotEntity.getVmSnapshotName());
    	sysLog.setOperMark("增加虚拟机快照");
    	sysLog.setCreateDate(new Date());
        try {
            HostEntity hostEntity = hostDao.selectById(vmEntity.getHostId());
            String command = "virsh snapshot-create-as " + vmEntity.getVmName() + " " + vmSnapshotEntity.getVmSnapshotName();
            String createImgFlag = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), command);
            if(createImgFlag.contains("exists")) {
            	throw new Exception("快照名称已存在");
            }
            if (!createImgFlag.contains("created")) {
                throw new Exception("virsh添加快照失败");
            }
            logger.info("打印shell返回值:" + createImgFlag);
            vmSnapshotEntity.setVmSnapshotPath("/var/lib/libvirt/qemu/snapshot/" + vmEntity.getVmName() + "/" + vmSnapshotEntity.getVmSnapshotName() + ".xml");
            vmSnapshotEntity.setCreateTime(new Date());
            this.save(vmSnapshotEntity);
            sysLog.setResult("成功");
            sysLogDao.insert(sysLog);
        } catch (Exception e) {
            logger.error("添加虚拟机快照失败:" + e.getMessage());
            sysLog.setResult("失败");
			sysLog.setErrorMsg("添加虚拟机快照失败:" + e.getMessage());
			sysLogDao.insert(sysLog);
            throw new Exception("添加虚拟机快照失败:" + e.getMessage());
        }

    }

    @Override
    public void updateVmSnapShot(VmSnapshotEntity vmSnapshotEntity) throws Exception {
    	SysLogEntity sysLog = new SysLogEntity();
    	sysLog.setUsername(SysLogUtil.getUserName());
    	sysLog.setIp(SysLogUtil.getLoginIp());
    	sysLog.setOperation("虚拟机动作");
    	sysLog.setOperObj(vmSnapshotEntity.getVmSnapshotName());
    	sysLog.setOperMark("修改虚拟机快照");
    	sysLog.setCreateDate(new Date());
        try {
            this.updateById(vmSnapshotEntity);
            sysLog.setResult("失败");
            sysLogDao.insert(sysLog);
        } catch (Exception e) {
            logger.error("修改虚拟机快照失败:" + e.getMessage());
            sysLog.setResult("失败");
			sysLog.setErrorMsg("修改虚拟机快照失败:" + e.getMessage());
			sysLogDao.insert(sysLog);
            throw new Exception("修改虚拟机快照失败:" + e.getMessage());
        }

    }

    @Override
    public Result deleteVmSnapShot(Long[] vmSnapshotId) throws Exception {
        List<SysLogEntity> sysLogList = new ArrayList<SysLogEntity>();
        for (int i = 0; i < vmSnapshotId.length; i++) {
            //根据id查询虚拟机快照
            VmSnapshotEntity vmSnapshotEntity = vmSnapshotDao.selectById(vmSnapshotId[i]);
            SysLogEntity sysLog = new SysLogEntity();
			sysLog.setUsername(SysLogUtil.getUserName());
			sysLog.setIp(SysLogUtil.getLoginIp());
			sysLog.setOperation("虚拟机动作");
			sysLog.setOperMark("删除虚拟机快照");
			sysLog.setOperObj(vmSnapshotEntity.getVmSnapshotName());
			try {
	            VmEntity vmEntity = vmDao.selectById(vmSnapshotEntity.getVmId());
	            HostEntity hostEntity = hostDao.selectById(vmEntity.getHostId());
	            String command = "virsh snapshot-delete " + vmEntity.getVmName() + " " + vmSnapshotEntity.getVmSnapshotName();//拼接shell命令
	            String createImgFlag = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), command);//执行shell命令
	            if (!createImgFlag.contains("deleted")) {
	            	sysLog.setResult("失败");
					sysLog.setErrorMsg("virsh删除虚拟机快照失败");
	            }
	            sysLog.setResult("成功");
	            this.removeById(vmSnapshotId[i]);
	        } catch (Exception e) {
	        	logger.error("删除虚拟机快照失败:" + e.getMessage());
	        	sysLog.setResult("失败");
				sysLog.setErrorMsg("删除虚拟机快照失败:" + e.getMessage());
	        }
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			sysLogList.add(sysLog);
        }
        return Result.ok().put("sysLogList", sysLogList);
    }

    @Override
    public Result queryVmSnapShot(Map<String, Object> params) throws Exception {
        IPage<VmSnapshotEntity> page = null;
        String vmSnapShotName = Convert.toStr(params.get("vmSnapShotName"));
        Long vmId = Convert.toLong(params.get("vmId"));
        Long vmSanpshotId = Convert.toLong(params.get("vmSanpshotId"));
        List<VmSnapshotDTO> vmSnapShotList = new ArrayList<VmSnapshotDTO>();
        try {
            page = this.page(
                    new Query<VmSnapshotEntity>().getPage(params),
                    new QueryWrapper<VmSnapshotEntity>()
                            .like(StringUtils.isNotBlank(vmSnapShotName), "vm_snapshot_name", vmSnapShotName)
                            .eq(vmId != null, "vm_id", vmId)
                            .eq(vmSanpshotId != null, "vm_sanpshot_id", vmSanpshotId)
            );
            PageUtils pageUtils = new PageUtils(page);
            List<VmSnapshotEntity> list = (List<VmSnapshotEntity>) pageUtils.getList();
            for (VmSnapshotEntity vm : list) {
                VmEntity vmEntity = vmDao.selectById(vm.getVmId());
                HostEntity hostEntity = hostDao.selectById(vmEntity.getHostId());
                String command = "virsh snapshot-list "+vmEntity.getVmName();
                String result = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), command);
               // vmSnapShotList = vmSnapShotList(arrayList,result);
                vmSnapShotList.addAll(vmSnapShotList(result));
            }
        } catch (Exception e) {
            logger.error("查询虚拟机快照失败:" + e.getMessage());
            throw new Exception("查询虚拟机快照失败:" + e.getMessage());
        }

        return Result.ok().put("page",new PageUtils(page)).put("pages",vmSnapShotList);
    }

    private List<VmSnapshotDTO> vmSnapShotList(String result) throws IOException, ParseException {
    	List<VmSnapshotDTO> arrayList = new ArrayList<VmSnapshotDTO>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(result.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
        String line;
        int currentLine = 0;
        while ((line = br.readLine()) != null) {
            if (!line.trim().equals("") && currentLine > 1) {
                String[] ss = line.split("\\s{2,}");
                VmSnapshotDTO vmSnapshotDTO = new VmSnapshotDTO();
                vmSnapshotDTO.setVmSnapName(ss[0].trim());
                Date date = simpleDateFormat.parse(ss[1].trim());
                vmSnapshotDTO.setCreateTime(date);
                vmSnapshotDTO.setState(ss[2].trim());
                arrayList.add(vmSnapshotDTO);
            }
            currentLine++;
        }
        return arrayList;
    }

    @Override
    public Result revertVmSnapShot(Long vmSnapshotId) throws Exception {
        VmSnapshotEntity vmSnapshotEntity = vmSnapshotDao.selectById(vmSnapshotId);
        VmEntity vmEntity = vmDao.selectById(vmSnapshotEntity.getVmId());
        HostEntity host = hostDao.selectById(vmEntity.getHostId());
        SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("虚拟机动作");
		sysLog.setOperMark("恢复虚拟机快照");
		sysLog.setOperObj(vmSnapshotEntity.getVmSnapshotName());
		sysLog.setCreateDate(new Date());
        try {//链接虚拟机，并恢复快照
			Connect connect = connectService.kvmConnect(host.getOsIp(),host.getHostUser(), Constant.KVM_SSH, 0);
			Domain domain = connect.domainLookupByName(vmEntity.getVmName());
			DomainSnapshot domainSnapshot = domain.snapshotLookupByName(vmSnapshotEntity.getVmSnapshotName());
			int flag = domain.revertToSnapshot(domainSnapshot);
			if (flag != 0) {
				sysLog.setResult("失败");
				sysLog.setErrorMsg("调用libvirt恢复快照失败");
				return Result.error("恢复快照失败");
			}
		} catch (Exception e) {
			logger.error("恢复快照失败 : " + e.getMessage());
			sysLog.setResult("失败");
			sysLog.setErrorMsg("恢复快照失败 : " + e.getMessage());
			return Result.error("恢复快照失败");
		}
        sysLog.setResult("成功");
		sysLogDao.insert(sysLog);
        return Result.ok();
    }
}
