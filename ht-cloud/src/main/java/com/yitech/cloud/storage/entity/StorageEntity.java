/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.storage.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 存储实体
 * @author fangyi
 *
 */
@TableName("storage_table")
@ApiModel(value = "存储实体",description = "存储实体")
public class StorageEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 存储唯一标识
	 */
	@TableId(type=IdType.ASSIGN_ID)
	@ApiModelProperty(value = "存储ID")
	private Long storageId;
	/**
	 * 存储uuid（用于生成xml)
	 */
	@ApiModelProperty(value = "存储卷uuid")
	private String volumeUuid;
	/**
	 * 存储卷名称
	 */
	@ApiModelProperty(value = "存储卷名称")
	@NotBlank(message="存储卷名称不能为空")
	private String storageVolumeName;
	/**
	 * 文件系统类型（虚拟机镜像、iso文件镜像）
	 */
	@ApiModelProperty(value = " 文件系统类型（虚拟机镜像、iso文件镜像）")
	private String filesystem;
	/**
	 * 镜像文件大小
	 */
	@ApiModelProperty(value = "镜像文件大小")
	private String fileSize;
	/**
	 * 存储卷总容量
	 */
	@ApiModelProperty(value = "存储卷总容量")
	private String capacity;
	/**
	 * 存储方式（本地存储、NAS存储、ISCSI存储、FC存储）
	 * 1为本地存储，2为NAS存储、3为ISCSI存储，4为FC存储
	 */
	@ApiModelProperty(value = "存储方式（本地存储、NAS存储、ISCSI存储、FC存储）")
	private Integer storageType;
	/**
	 * 存储路径(宿主机镜像)
	 */
	@ApiModelProperty(value = "存储路径(宿主机镜像)")
	private String storagePath;
	/**
	 * 基础镜像ID
	 */
	@ApiModelProperty(value = "基础镜像ID")
	private Long basicVolumeId;
	/**
	 * 格式（高速RAW、智能QCOW2）
	 */
	@ApiModelProperty(value = "格式（高速RAW、智能QCOW2）")
	@TableField(exist=false)
	private String createFormat;
	/**
	 * 存储所属资源池
	 */
	@ApiModelProperty(value = "存储所属资源池")
	private Long storagePoolId;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**
	 * 创建者ID
	 */
	@ApiModelProperty(value = "创建者ID")
	private Long createUserId;
	/**
	 * 区分存储卷是否为模板（0-普通存储卷 1-模板存储卷）
	 */
	@ApiModelProperty(value = "区分存储卷是否为模板")
	private String judge;
	/**
     * 文件标识--上传文件时才有
     */
	@ApiModelProperty(value = "文件标识")
    private String identifier;
	
	/**
	 * 存储卷状态 1为正常，2为逻辑删除即放入回收站
	 */
	private Integer status;
	
	@ApiModelProperty(value = "存储资源池名称")
	@TableField(exist=false)
	private String storagePoolName;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "创建者")
	private String createUserName;
	/**
	 * 基础镜像名称
	 */
	@ApiModelProperty(value = "基础镜像名称")
	@TableField(exist=false)
	private String basicVolumeName;
	@ApiModelProperty(value = "存储池状态")
	@TableField(exist=false)
	private Integer poolStatus;

	public String getJudge() {
		return judge;
	}

	public void setJudge(String judge) {
		this.judge = judge;
	}

	public Long getStorageId() {
		return storageId;
	}
	public void setStorageId(Long storageId) {
		this.storageId = storageId;
	}
	public String getFilesystem() {
		return filesystem;
	}
	public void setFilesystem(String filesystem) {
		this.filesystem = filesystem;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public Integer getStorageType() {
		return storageType;
	}
	public void setStorageType(Integer storageType) {
		this.storageType = storageType;
	}
	public String getStoragePath() {
		return storagePath;
	}
	public void setStoragePath(String storagePath) {
		this.storagePath = storagePath;
	}
	public Long getStoragePoolId() {
		return storagePoolId;
	}
	public void setStoragePoolId(Long storagePoolId) {
		this.storagePoolId = storagePoolId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	public String getStorageVolumeName() {
		return storageVolumeName;
	}
	public void setStorageVolumeName(String storageVolumeName) {
		this.storageVolumeName = storageVolumeName;
	}
	public String getCapacity() {
		return capacity;
	}
	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}
	public String getVolumeUuid() {
		return volumeUuid;
	}
	public void setVolumeUuid(String volumeUuid) {
		this.volumeUuid = volumeUuid;
	}
	public Long getBasicVolumeId() {
		return basicVolumeId;
	}
	public void setBasicVolumeId(Long basicVolumeId) {
		this.basicVolumeId = basicVolumeId;
	}
	public String getCreateFormat() {
		return createFormat;
	}
	public void setCreateFormat(String createFormat) {
		this.createFormat = createFormat;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getStoragePoolName() {
		return storagePoolName;
	}

	public void setStoragePoolName(String storagePoolName) {
		this.storagePoolName = storagePoolName;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getBasicVolumeName() {
		return basicVolumeName;
	}

	public void setBasicVolumeName(String basicVolumeName) {
		this.basicVolumeName = basicVolumeName;
	}

	public Integer getPoolStatus() {
		return poolStatus;
	}

	public void setPoolStatus(Integer poolStatus) {
		this.poolStatus = poolStatus;
	}
	
	
}
