/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.storage.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hontosec.cloud.common.utils.PageUtils;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.storage.entity.Chunk;
import com.hontosec.cloud.storage.entity.StorageEntity;
import com.hontosec.cloud.storage.entity.vo.CheckChunkVO;

/**
 * 存储接口层
 * @author fangyi
 *
 */
public interface StorageService extends IService<StorageEntity>{
	/**
	 * 分页查询存储列表
	 * @param params
	 * @return
	 */
	PageUtils queryPage(Map<String, Object> params);
	/**
	 * 批量删除
	 * @param storageIds 存储卷id
	 * @param rbFlag 是否放入回收站
	 * @return
	 * @throws Exception 
	 */
	Result deleteBatch(List<String> storageIds,Boolean rbFlag) throws Exception;
	/**
	 * 创建存储卷
	 */
	Result saveVolume(StorageEntity storageEntity);

	/**
	 * 选择存储
	 */
	Result selectStorage(Map<String, Object> params);
	/**
	 * 选择基础镜像
	 * @param storageVolumeId 存储卷id
	 * @return
	 */
	Result selectBasicVol(Long storageVolumeId);
	/**
	 * 上传文件
	 * @throws IOException 
	 */
	Result uploadFile(MultipartFile file,Long storagePoolId) throws IOException;
	/**
	 * 下载文件
	 * @param storageId
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	void downloadFile(Long storageId, HttpServletResponse response) throws Exception;
	/**
	 * 选择光驱镜像
	 * @param storageVolumeId
	 * @return
	 */
	Result selectCdIso(Long storageVolumeId);
	
	/**
     * 每一个上传块都会包含如下分块信息：
     * chunkNumber: 当前块的次序，第一个块是 1，注意不是从 0 开始的。
     * totalChunks: 文件被分成块的总数。
     * chunkSize: 分块大小，根据 totalSize 和这个值你就可以计算出总共的块数。注意最后一块的大小可能会比这个要大。
     * currentChunkSize: 当前块的大小，实际大小。
     * totalSize: 文件总大小。
     * identifier: 这个就是每个文件的唯一标示,md5码
     * filename: 文件名。
     * relativePath: 文件夹上传的时候文件的相对路径属性。
     * 一个分块可以被上传多次，当然这肯定不是标准行为，但是在实际上传过程中是可能发生这种事情的，这种重传也是本库的特性之一。
     * <p>
     * 根据响应码认为成功或失败的：
     * 200 文件上传完成
     * 201 文加快上传成功
     * 500 第一块上传失败，取消整个文件上传
     * 507 服务器出错自动重试该文件块上传
     */
	 int postFileUpload(Chunk chunk, HttpServletResponse response,Long storagePoolId);
	 /**
	  * 检查文件上传状态
	  * @param chunk
	  * @param response
	  * @return
	  */
	 CheckChunkVO getFileUpload(Chunk chunk, HttpServletResponse response,Long storagePoolId);
	 /**
	  * 删除文件
	  * @param id
	  * @return
	  */
     int deleteBackFileByIds(Long id);
     /**
      * 合并文件
      * @param fileInfo
      * @return
     * @throws Exception 
      */
     int mergeFile(StorageEntity fileInfo) throws Exception;
     /**
      * 同步文件保存数据到数据库
      * @param params
      */
     int syncSave(Map<String, String> params);
     /**
      * 同步文件从数据库删除数据
      * @param params
      * @return
      */
	 int syncDel(Map<String, String> params);
	List<StorageEntity> queryList(Map<String, Object> params);
}
