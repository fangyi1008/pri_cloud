/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年10月9日
 */
package com.yitech.cloud.storage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yitech.cloud.storage.entity.Chunk;

@Mapper
public interface ChunkDao extends BaseMapper<Chunk>{
	/**
	 * 根据文件标识查询
	 * @param resultChunk
	 * @return
	 */
	List<Chunk> selectIdentifier(@Param(value= "identifier") String identifier);
	
	/**
    *
    * 功能描述: 根据文件名和MD5值删除chunk记录
    *
    * @param:
    * @return:
    * @author: xjd
    * @date: 2020/7/31 23:43
    */
   int deleteBackChunkByIdentifier(Chunk backChunk);
	
}
