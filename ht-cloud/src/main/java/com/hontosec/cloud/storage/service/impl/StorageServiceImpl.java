/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.storage.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.libvirt.Connect;
import org.libvirt.StorageVol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hontosec.cloud.common.config.CloudConfig;
import com.hontosec.cloud.common.config.ShiroUtils;
import com.hontosec.cloud.common.service.ConnectService;
import com.hontosec.cloud.common.utils.Constant;
import com.hontosec.cloud.common.utils.IPUtils;
import com.hontosec.cloud.common.utils.PageUtils;
import com.hontosec.cloud.common.utils.Query;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.common.utils.SysLogUtil;
import com.hontosec.cloud.common.utils.crypt.CryptUtil;
import com.hontosec.cloud.common.utils.file.FileUploadUtils;
import com.hontosec.cloud.common.utils.file.FileUtils;
import com.hontosec.cloud.common.utils.file.MimeTypeUtils;
import com.hontosec.cloud.common.utils.sftp.SFTPUtil;
import com.hontosec.cloud.common.utils.ssh.SshUtil;
import com.hontosec.cloud.common.utils.text.Convert;
import com.hontosec.cloud.host.dao.HostDao;
import com.hontosec.cloud.host.entity.HostEntity;
import com.hontosec.cloud.storage.dao.ChunkDao;
import com.hontosec.cloud.storage.dao.StorageDao;
import com.hontosec.cloud.storage.dao.StoragePoolDao;
import com.hontosec.cloud.storage.entity.Chunk;
import com.hontosec.cloud.storage.entity.StorageEntity;
import com.hontosec.cloud.storage.entity.StoragePoolEntity;
import com.hontosec.cloud.storage.entity.vo.CheckChunkVO;
import com.hontosec.cloud.storage.service.StorageService;
import com.hontosec.cloud.sys.dao.SysLogDao;
import com.hontosec.cloud.sys.entity.SysLogEntity;
import com.hontosec.cloud.vm.dao.VmDao;
import com.hontosec.cloud.vm.entity.VmEntity;
/**
 * 存储接口实现层
 * @author fangyi
 *
 */
@Service("storageService")
public class StorageServiceImpl extends ServiceImpl<StorageDao, StorageEntity> implements StorageService{
	private static final Logger logger = LoggerFactory.getLogger(StorageServiceImpl.class);
	private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("######0.00");
	@Autowired
	private HostDao hostDao;
	@Autowired
	private StoragePoolDao storagePoolDao;
	@Autowired
	private StorageDao storageDao;
	@Autowired
	private ConnectService connectService;
	@Autowired
	private VmDao vmDao;
	@Autowired
	private SysLogDao sysLogDao;
	@Autowired
	private ChunkDao chunkDao;
	
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		Long storagePoolId = Convert.toLong(params.get("storagePoolId"));
		Long createUserId = Convert.toLong(params.get("createUserId"));
		Integer status = Convert.toInt(params.get("status"));//传递1为查询正常存储卷，2为回收站中的存储卷
		IPage<StorageEntity> page = this.page(
			new Query<StorageEntity>().getPage(params),
			new QueryWrapper<StorageEntity>()
				.eq(createUserId != null,"create_user_id", createUserId)
				.eq(storagePoolId != null,"storage_pool_id", storagePoolId)
				.eq(status != null,"status", status)
		);
		return new PageUtils(page);
	}

	@Override
	public Result deleteBatch(List<String> storageIds,Boolean rbFlag) throws Exception {
		List<SysLogEntity> sysLogList = new ArrayList<SysLogEntity>();
		for(int i =0;i<storageIds.size();i++) {
			//根据存储卷id去虚拟机表查询是否被使用,如被使用不允许删除
			List<VmEntity> vmList = vmDao.queryByVolumeId(Long.parseLong(storageIds.get(i)));
			StorageEntity storageEntity = this.getById(storageIds.get(i));
			String volumeName = storageEntity.getStorageVolumeName();
			SysLogEntity sysLog = new SysLogEntity();
			sysLog.setUsername(SysLogUtil.getUserName());
			sysLog.setIp(SysLogUtil.getLoginIp());
			sysLog.setOperation("存储卷动作");
			sysLog.setOperMark("删除存储卷");
			sysLog.setOperObj(volumeName);
			if(vmList.size() == 0 && delStorageVolume(storageEntity,rbFlag)) {
				sysLog.setResult("成功");
			}else {
				sysLog.setResult("失败");
				sysLog.setErrorMsg(volumeName +"存储卷已被使用，不允许删除");
			}
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			sysLogList.add(sysLog);
		}
		return Result.ok().put("sysLogList", sysLogList);
	}
	
	private boolean delStorageVolume(StorageEntity storageEntity,Boolean rbFlag) throws Exception {
        try {
        	StorageEntity basicStorage = storageDao.selectById(storageEntity.getBasicVolumeId());
        	if(basicStorage != null) {
        		return false;
        	}
        	//判断是否有虚拟机使用其为基础镜像
        	if(rbFlag == true) {//放入回收站
        		storageEntity.setStatus(2);//状态改为逻辑删除
        		this.updateById(storageEntity);
        		return true;
        	}else {//直接删除
        		StoragePoolEntity storagePool = storagePoolDao.selectById(storageEntity.getStoragePoolId());
            	HostEntity hostEntity = hostDao.selectById(storagePool.getHostId());
            	if(!"iso".equalsIgnoreCase(storageEntity.getFilesystem())) {
            		delVolToLibvirt(hostEntity,storageEntity);//删除存储卷
				} /*else if(storageEntity.getBasicVolumeId() != null) {
					 String path = "rm -rf "+storageEntity.getStoragePath();
					    SshUtil.sshShell(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), path);
					}*/
    			this.removeById(storageEntity);
    			return true;
        	}
		} catch (Exception e) {
			logger.error("调用libvirt删除存储卷失败 : " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * 调用libvirt删除存储卷
	 * @throws Exception 
	 */
	public void delVolToLibvirt(HostEntity hostEntity,StorageEntity storageEntity) throws Exception {
		try {
			Connect connect = connectService.kvmConnect(hostEntity.getOsIp(),hostEntity.getHostUser(), Constant.KVM_SSH, 0);
	    	StorageVol delStorageVolume = connect.storageVolLookupByPath(storageEntity.getStoragePath());
	        logger.info("存储卷名称：{}", delStorageVolume.getName());
	        if(delStorageVolume != null) {
	        	delStorageVolume.delete(0);
	        }
		}catch (Exception e) {
			logger.error("调用libvirt删除存储卷 " + storageEntity.getStorageVolumeName() + "失败 : " + e.getMessage());
			throw new Exception("调用libvirt删除存储卷 " + storageEntity.getStorageVolumeName() + "失败");
		}
	}

	@Override
	public Result saveVolume(StorageEntity storageEntity) {
		//根据存储池id获取存储池信息，再根据存储池id获取主机信息
		StoragePoolEntity storagePool = storagePoolDao.selectById(storageEntity.getStoragePoolId());
		HostEntity hostEntity = hostDao.selectById(storagePool.getHostId());
		//判断该存储池下是否存在同名存储卷名称
		StorageEntity storageVolume = storageDao.queryByPoolIdAndName(storagePool.getStoragePoolId(), storageEntity.getStorageVolumeName());
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("存储卷动作");
		sysLog.setOperObj(storageEntity.getStorageVolumeName());
		sysLog.setOperMark("增加存储卷");
		if(storageVolume != null) {
			sysLog.setResult("失败");
			sysLog.setCreateDate(new Date());
			sysLog.setErrorMsg("存储池"+ storagePool.getStoragePoolName() + "下已存在该名称的存储卷");
			sysLogDao.insert(sysLog);
			return Result.error("存储池"+ storagePool.getStoragePoolName() + "下已存在该名称的存储卷");
		}
		String xmlFilePath = null;
		try {
			if("lvm".equals(storagePool.getPoolType())) {
				Boolean lvmVolumeFlag = createLvmVolume(storageEntity, hostEntity, storagePool.getStoragePoolName());
				if(lvmVolumeFlag == false) {
					sysLog.setResult("失败");
					sysLog.setCreateDate(new Date());
					sysLog.setErrorMsg("创建lvm存储卷" + storageEntity.getStorageVolumeName() + "失败");
					sysLogDao.insert(sysLog);
					return Result.error("创建存储卷失败");
				}
				storageEntity.setStatus(1);//正常状态
				this.save(storageEntity);
			}else {
				createImg(storageEntity, hostEntity);//创建存储卷硬盘空间
				storageEntity.setStatus(1);//正常状态
				this.save(storageEntity);
			}
			
		} catch (Exception e) {
			logger.error("创建存储卷失败 : " + e.getMessage());
			sysLog.setResult("失败");
			sysLog.setCreateDate(new Date());
			sysLog.setErrorMsg(e.getMessage());
			sysLogDao.insert(sysLog);
			createErr(xmlFilePath, storageEntity, hostEntity);//删除生成的文件
			return Result.error(e.getMessage());
		}
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		return Result.ok();
	}


	/**
	 * 创建lvm逻辑存储卷
	 * lvcreate -n volumeName -L 6G poolName
	 * @throws Exception 
	 */
	public Boolean createLvmVolume(StorageEntity storageEntity,HostEntity hostEntity,String storagePoolName) throws Exception {
		try {
			StoragePoolEntity storagePoolEntity = storagePoolDao.selectById(storageEntity.getStoragePoolId());
			String lvmCommand = "lvcreate -n "+storageEntity.getStorageVolumeName()+" -L "+storageEntity.getCapacity()+" " + storagePoolName;
			String lvmResult = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), lvmCommand);
			if(!lvmResult.contains("created")) {
				return false;
			}
			storageEntity.setStoragePath( storagePoolEntity.getStoragePoolPath() + "/" + storageEntity.getStorageVolumeName() + "."+storageEntity.getFilesystem());
		} catch (Exception e) {
			logger.error("创建lvm逻辑存储卷失败 : " + e.getMessage());
			return false;
		}
		return true;
		
	}
	/**
	 * 如生成存储卷失败，需把生成的文件及插入的记录进行删除操作
	 */
	public void createErr(String xmlFilePath,StorageEntity storageEntity,HostEntity hostEntity) {
		//判断数据库中是否已存在记录，如存在则删除
		StorageEntity storage = this.getById(storageEntity);
		if(storage != null) {
			this.removeById(storage);
		}
	}
	/**
	 * 创建硬盘空间
	 * virsh vol-create-as test1 test2.qcow2 30G --format qcow2
	 * @throws Exception 
	 */
	public void createImg(StorageEntity storageVolume,HostEntity hostEntity) throws Exception {
		String command = null;
		try {
			StoragePoolEntity storagePoolEntity = storagePoolDao.selectById(storageVolume.getStoragePoolId());
			String storagePath = storagePoolEntity.getStoragePoolPath() + "/" + storageVolume.getStorageVolumeName() + "."+storageVolume.getFilesystem();
			if(storageVolume.getCreateFormat() == null || "qcow2".equals(storageVolume.getCreateFormat())) {
				storageVolume.setFilesystem("qcow2");
				//command = "/opt/qemu-7.0.0/bin/qemu-img create -f "+storageVolume.getFilesystem()+" "+storagePoolEntity.getStoragePoolPath() + "/" + storageVolume.getStorageVolumeName() + "."+storageVolume.getFilesystem()+" "+storageVolume.getCapacity();
			}else {
				storageVolume.setFilesystem("raw");
			}
			if(storageVolume.getBasicVolumeId() == null) {
				command = "virsh vol-create-as "+storagePoolEntity.getStoragePoolName()+" "+ storageVolume.getStorageVolumeName() + "."+storageVolume.getFilesystem()+" "+storageVolume.getCapacity() + " --format " + storageVolume.getFilesystem();
			}else {
				StorageEntity baseStorage = storageDao.selectById(storageVolume.getBasicVolumeId());
				command = "/opt/qemu-7.0.0/bin/qemu-img create -F "+storageVolume.getFilesystem()+" -b "+baseStorage.getStoragePath()+" -f "+storageVolume.getFilesystem()+" "+storagePoolEntity.getStoragePoolPath() + "/" + storageVolume.getStorageVolumeName() + "."+storageVolume.getFilesystem()+" "+storageVolume.getCapacity();
			}
			logger.info("创建存储卷 : " + command);
			String createImgFlag = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), command);
			logger.info("打印shell返回值:"+createImgFlag);
			if(createImgFlag.contains("exists")) {
				throw new Exception("已存在该存储卷");
			}
			if(!createImgFlag.contains("created") && !createImgFlag.contains("Formatting")) {
				throw new Exception("创建硬盘空间失败");
			}
			storageVolume.setStoragePath(storagePath);
		} catch (Exception e) {
			logger.error("创建硬盘空间失败 : " + e.getMessage());
			throw new Exception("创建硬盘空间失败");
		}
		
	}

	@Override
	public Result selectStorage(Map<String, Object> params) {
		try {
			Long storagePoolId = Convert.toLong(params.get("storagePoolId"));
			Long hostId = Convert.toLong(params.get("hostId"));
			String poolType = Convert.toStr(params.get("poolType"));
			List<StoragePoolEntity> storagePoolList = storagePoolDao.queryByType(hostId,poolType);//查询本地存储池
			if(storagePoolId == null && storagePoolList.size() > 0) {
				storagePoolId = storagePoolList.get(0).getStoragePoolId();
				params.put("storagePoolId", storagePoolList.get(0).getStoragePoolId());
			}
			PageUtils page = null;
			if(storagePoolId != null) {
				page = queryPage(params);
			}
			return Result.ok().put("storagePoolList", storagePoolList).put("page", page);
		} catch (Exception e) {
			logger.error("获取基础镜像列表失败 : " + e.getMessage());
			return Result.error("获取基础镜像列表失败");
		}
	}

	@Override
	public Result selectBasicVol(Long storageVolumeId) {
		StorageEntity storage = storageDao.selectById(storageVolumeId);
		List<StorageEntity> storageList = storageDao.selectByBasicId(storageVolumeId);//根据存储卷id查询
		String filesystem = storage.getFilesystem();
		if(!"qcow2".equals(filesystem)) {
			return Result.error("不允许选择文件后缀名为“.vfd”和“.iso”的文件，请选择其它的存储文件。");
		}
		if(storageList.size() > 0) {
			return Result.error("存储文件已经被"+storageList.get(0).getStorageVolumeName()+"使用，请选择其它的存储文件。");
		}
		return Result.ok().put("basicVolumeId", storage.getStorageId()).put("basicVolumePath", storage.getStoragePath());
	}
	
	@Override
	public Result selectCdIso(Long storageVolumeId) {
		StorageEntity storage = storageDao.selectById(storageVolumeId);
		List<StorageEntity> storageList = storageDao.selectByBasicId(storageVolumeId);//根据存储卷id查询
		String filesystem = storage.getFilesystem();
		if(!"qcow2".equals(filesystem) && !"iso".equalsIgnoreCase(filesystem)) {
			return Result.error("不允许选择文件后缀名为“.vfd”的文件，请选择其它的存储文件。");
		}
		if(storageList.size() > 0) {
			return Result.error("存储文件已经被"+storageList.get(0).getStorageVolumeName()+"使用，请选择其它的存储文件。");
		}
		return Result.ok().put("vmOsPath", storage.getStoragePath());
	}

	@Override
	public Result uploadFile(MultipartFile file,Long storagePoolId) {
		try {
			StoragePoolEntity storagePool = storagePoolDao.selectById(storagePoolId);//根据存储池id查询存储池实体
			HostEntity hostEntity = hostDao.selectById(storagePool.getHostId());//获取主机实体
			String tmpfilePath = CloudConfig.getTmpPath();//临时文件夹
			String fileName = FileUploadUtils.upload(tmpfilePath, file,MimeTypeUtils.VM_EXTENSION);//上传文件路径+新文件名
			logger.info("uploadFile filename {}",fileName);
			String originalFilename = file.getOriginalFilename();//原始文件名
			logger.info("uploadFile originalFilename {}",originalFilename);
			logger.info("uploadFile file type {}",originalFilename.substring(originalFilename.lastIndexOf(".") + 1));
			String destFilePath = storagePool.getStoragePoolPath();// 上传文件路径
			if(IPUtils.getIp().equals(hostEntity.getOsIp())) {//当前主机ip与要上传的ip是否一致
				File srcFile = new File(fileName);
				File destFloder = new File(destFilePath +"/" + originalFilename);
				if(!srcFile.renameTo(destFloder)) {
					return Result.error("上传失败");
				}
			}else {
				SFTPUtil sftpUtil = new SFTPUtil(hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), hostEntity.getOsIp(), 22);
				boolean upflag = sftpUtil.upFile(destFilePath, originalFilename, fileName , true);
				if(upflag == false) {
					throw new Exception("上传文件到存储池失败");
				}
			}
			StorageEntity storageEntity = new StorageEntity();
			storageEntity.setStoragePoolId(storagePoolId);
			storageEntity.setStorageVolumeName(originalFilename);
			storageEntity.setStoragePath(destFilePath + "/" + originalFilename);
			if(file.getSize() / 1024.00 / 1024.00 / 1024.00 > 1) {
				storageEntity.setFileSize(DOUBLE_FORMAT.format(file.getSize() / 1024.00 / 1024.00 / 1024.00) + "GB");
			}else {
				storageEntity.setFileSize(DOUBLE_FORMAT.format(file.getSize() / 1024.00 / 1024.00) + "MB");
			}
			storageEntity.setCapacity(storageEntity.getFileSize());
			storageEntity.setFilesystem(originalFilename.substring(originalFilename.lastIndexOf(".") + 1));
			storageEntity.setCreateTime(new Date());
			storageEntity.setCreateUserId(ShiroUtils.getUserId());
			storageEntity.setJudge("0");
			storageEntity.setStatus(1);
			storageDao.insert(storageEntity);
			File delFile = new File(fileName);//清理tmp文件
			if(delFile.exists()) {
				delFile.delete();
			}
		}  catch (Exception e) {
			return Result.error("上传失败");
		}
		return Result.ok();
	}

	@Override
	public void downloadFile(Long storageId, HttpServletResponse response) throws Exception {
		try {
			StorageEntity storageEntity = storageDao.selectById(storageId);
			StoragePoolEntity storagePool = storagePoolDao.selectById(storageEntity.getStoragePoolId());//根据存储池id查询存储池实体
			HostEntity hostEntity = hostDao.selectById(storagePool.getHostId());//获取主机实体
			String tmpfilePath = CloudConfig.getTmpPath();//临时文件夹
			String remoteFile = null;
			if(!IPUtils.getIp().equals(hostEntity.getOsIp())) {//当前主机ip与要上传的ip是否一致
				SFTPUtil sftpUtil = new SFTPUtil(hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), hostEntity.getOsIp(), 22);
				String remotePath = storageEntity.getStoragePath().substring(0,storageEntity.getStoragePath().lastIndexOf("/"));
				remoteFile = storageEntity.getStoragePath().substring(storageEntity.getStoragePath().lastIndexOf("/") + 1,storageEntity.getStoragePath().length());
				boolean downflag = sftpUtil.downFile(remotePath, remoteFile, tmpfilePath + remoteFile, true);
				if(downflag == false) {
					throw new Exception("从远端服务器下载文件失败");
				}
				storageEntity.setStoragePath(tmpfilePath + remoteFile);
			}
			File file = new File(storageEntity.getStoragePath());
			response.setHeader("Content-Length", String.valueOf(file.length()));
			FileUtils.setAttachmentResponseHeader(response, storageEntity.getStorageVolumeName());
			FileUtils.writeBytes(storageEntity.getStoragePath(), response.getOutputStream());
			if(remoteFile != null) {
				File delFile = new File(tmpfilePath + remoteFile);//清理tmp文件
				if(delFile.exists()) {
					delFile.delete();
				}
			}
		} catch (Exception e) {
			logger.error("下载失败 : " + e.getMessage());
			throw new Exception("下载失败");
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
    public int postFileUpload(Chunk chunk, HttpServletResponse response,Long storagePoolId) {
        int result = 0;
        MultipartFile file = chunk.getFile();
        StoragePoolEntity storagePool = storagePoolDao.selectById(storagePoolId);//根据存储池id查询存储池路径
        StorageEntity storage = storageDao.queryByPoolIdAndName(storagePoolId,chunk.getFilename());
        if(storage != null) {
        	return 3;//该存储池下已经存在该文件
        }
        logger.debug("file originName: {}, chunkNumber: {}", file.getOriginalFilename(), chunk.getChunkNumber());
        Path path = Paths.get(generatePath(storagePool.getStoragePoolPath(), chunk));
        try {
            Files.write(path, chunk.getFile().getBytes());
            logger.debug("文件 {} 写入成功, md5:{}", chunk.getFilename(), chunk.getIdentifier());
            chunk.setIdentifier(chunk.getIdentifier() + "|" + storagePoolId);
            result = chunkDao.insert(chunk);//写入数据库
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(507);
            return 0;
        }
        return result;
    }
	/**
     * 功能描述:生成块文件所在地址
     *
     * @param:
     * @return:
     * @author: xjd
     * @date: 2020/7/28 16:10
     */
    private String generatePath(String uploadFolder, Chunk chunk) {
        StringBuilder sb = new StringBuilder();
        sb.append(uploadFolder).append("/").append(chunk.getIdentifier());//文件夹地址/md5
        //判断uploadFolder/identifier 路径是否存在，不存在则创建
        if (!Files.isWritable(Paths.get(sb.toString()))) {
            logger.info("path not exist,create path: {}", sb.toString());
            try {
                Files.createDirectories(Paths.get(sb.toString()));
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return sb.append("/")
                .append(chunk.getFilename())
                .append("-")
                .append(chunk.getChunkNumber()).toString();//文件夹地址/md5/文件名-1
    }

	@Override
	public CheckChunkVO getFileUpload(Chunk chunk, HttpServletResponse response,Long storagePoolId) {
		CheckChunkVO vo = new CheckChunkVO();
        //检查该文件是否存在于filelist中,如果存在,直接返回skipUpload为true,执行闪传
        List<StorageEntity> backFilelists = storageDao.selectIdentifierPoolId(chunk.getIdentifier(),storagePoolId);
        if (backFilelists != null && !backFilelists.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_CREATED);
            vo.setSkipUpload(true);
            return vo;
        }
        List<Chunk> backChunks = chunkDao.selectIdentifier(chunk.getIdentifier());
        //将已存在的块的chunkNumber列表返回给前端,前端会规避掉这些块
        if (backChunks != null && !backChunks.isEmpty()) {
            List<Integer> collect = backChunks.stream().map(Chunk::getChunkNumber).collect(Collectors.toList());
            vo.setUploaded(collect);
        }
        return vo;
	}

	@Override
	public int deleteBackFileByIds(Long id) {
		return 0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
    public int mergeFile(StorageEntity fileInfo) throws Exception {
        String filename = fileInfo.getStorageVolumeName().substring(0, fileInfo.getStorageVolumeName().lastIndexOf("."));
        String fileSystem = fileInfo.getStorageVolumeName().substring(fileInfo.getStorageVolumeName().lastIndexOf(".") + 1, fileInfo.getStorageVolumeName().length());
        StoragePoolEntity storagePool = storagePoolDao.selectById(fileInfo.getStoragePoolId());//根据存储池id查询存储池实体
        String file = storagePool.getStoragePoolPath() + "/" + fileInfo.getIdentifier() + "/" + filename;
        String folder = storagePool.getStoragePoolPath() + "/" + fileInfo.getIdentifier();
        File folderFile = new File(folder);
        if(!folderFile.exists()) {
        	folderFile.mkdirs();
        }
        merge(file, folder, filename);
        //当前文件已存在数据库中时,返回已存在标识
        List<StorageEntity> storageList = storageDao.selectIdentifierPoolId(fileInfo.getIdentifier(),storagePool.getStoragePoolId());
        if (storageList.size() > 0) {
            return -1;
        }
        fileInfo.setStoragePath(file);
        fileInfo.setFilesystem(fileSystem);
        fileInfo.setCreateTime(new Date());
        fileInfo.setCreateUserId(ShiroUtils.getUserId());
        fileInfo.setJudge("0");
        fileInfo.setStatus(1);
        int i = storageDao.insert(fileInfo);
        if (i == 1) {
            //插入文件记录成功后,删除chunk表中的对应记录,释放空间
            Chunk backChunk = new Chunk();
            backChunk.setIdentifier(fileInfo.getIdentifier() + "|" + storagePool.getStoragePoolId());
            backChunk.setFilename(fileInfo.getStorageVolumeName());
            chunkDao.deleteBackChunkByIdentifier(backChunk);
        }
		HostEntity hostEntity = hostDao.selectById(storagePool.getHostId());//获取主机实体
        if(!IPUtils.getIp().equals(hostEntity.getOsIp())){
			SFTPUtil sftpUtil = new SFTPUtil(hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), hostEntity.getOsIp(), 22);
			boolean upflag = sftpUtil.upFile(storagePool.getStoragePoolPath() + "/" + fileInfo.getIdentifier() + "/", filename, file , true);
			if(upflag == false) {
				throw new Exception("上传文件到存储池失败");
			}else {
				File files = new File(file);
				 if(folderFile.exists()) {
					 files.delete();
				 }
				
			}
		}
        return i;
    }
	
	/**
     * 文件合并
     *
     * @param targetFile 要形成的文件名
     * @param folder     要形成的文件夹地址
     * @param filename   文件的名称
     */
    public static void merge(String targetFile, String folder, String filename) {
        try {
            Files.createFile(Paths.get(targetFile));
            Files.list(Paths.get(folder))
                    .filter(path -> !path.getFileName().toString().equals(filename))
                    .sorted((o1, o2) -> {
                        String p1 = o1.getFileName().toString();
                        String p2 = o2.getFileName().toString();
                        int i1 = p1.lastIndexOf("-");
                        int i2 = p2.lastIndexOf("-");
                        return Integer.valueOf(p2.substring(i2)).compareTo(Integer.valueOf(p1.substring(i1)));
                    })
                    .forEach(path -> {
                        try {
                            //以追加的形式写入文件
                            Files.write(Paths.get(targetFile), Files.readAllBytes(path), StandardOpenOption.APPEND);
                            //合并后删除该块
                            Files.delete(path);
                        } catch (IOException e) {
                            logger.error(e.getMessage(), e);
                        }
                    });
        } catch (IOException e) {
        	logger.error(e.getMessage(), e);
        }
    }

	@Override
	public int syncSave(Map<String, String> params) {
		String fileName = params.get("fileName");
		String fileSize = params.get("fileSize");
		String filePath = params.get("filePath");
		String osIp = params.get("osIp");
		//根据osIp获取主机实体
		HostEntity host = hostDao.queryByOsIp(osIp);
		List<StoragePoolEntity> storagePoolList = storagePoolDao.queryByHostId(host.getHostId());
		StorageEntity storageEntity = new StorageEntity();
		for(int i = 0;i<storagePoolList.size();i++) {
			if(filePath.contains(storagePoolList.get(i).getStoragePoolPath())) {
				storageEntity.setStoragePoolId(storagePoolList.get(i).getStoragePoolId());
			}
		}
		storageEntity.setStorageVolumeName(fileName);
		storageEntity.setStoragePath(filePath);
		storageEntity.setFileSize(fileSize);
		storageEntity.setCapacity(storageEntity.getFileSize());
		storageEntity.setFilesystem(fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()));
		storageEntity.setCreateTime(new Date());
		storageEntity.setCreateUserId(ShiroUtils.getUserId());
		storageEntity.setJudge("0");
		storageEntity.setStatus(1);
		int insertResult = storageDao.insert(storageEntity);
		return insertResult;
	}

	@Override
	public int syncDel(Map<String, String> params) {
		String fileName = params.get("fileName");
		String osIp = params.get("osIp");
		//根据存储卷路径查询
		fileName = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		List<StorageEntity> storageList = storageDao.selectStorageName(fileName);
		for(int i = 0;i<storageList.size();i++) {
			StoragePoolEntity storagePool = storagePoolDao.selectById(storageList.get(i).getStoragePoolId());
			HostEntity host = hostDao.selectById(storagePool.getHostId());
			if(osIp.equals(host.getOsIp())) {
				return storageDao.deleteById(storageList.get(i));
			}
		}
		return -1;
	}

	@Override
	public List<StorageEntity> queryList(Map<String, Object> params) {
		Long storagePoolId = Convert.toLong(params.get("storagePoolId"));
		Long createUserId = Convert.toLong(params.get("createUserId"));
		Integer status = Convert.toInt(params.get("status"));//传递1为查询正常存储卷，2为回收站中的存储卷
		Integer page = Convert.toInt(params.get("page"));
		Integer limit = Convert.toInt(params.get("limit"));
		List<StorageEntity> list = storageDao.queryList(storagePoolId,createUserId,status,page-1,limit);
		for(int i = 0;i<list.size();i++) {
			StorageEntity storage = list.get(i);
			if(storage.getBasicVolumeId() != null) {
				StorageEntity basicStorage = storageDao.selectById(storage.getBasicVolumeId());
				if(basicStorage != null) {
					storage.setBasicVolumeName(basicStorage.getStorageVolumeName());
				}
			}
		}
		return list;
	}

}
