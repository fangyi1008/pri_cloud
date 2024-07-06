/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年10月9日
 */
package com.yitech.cloud.storage.entity;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@TableName("storage_chunk_table")
@ApiModel(value = "存储分块实体",description = "存储分块实体")
public class Chunk {
	/**
     * 主键ID
     */
	@TableId
	@ApiModelProperty(value = "分块ID")
    private Long id;
    /**
     * 当前文件块，从1开始
     */
	@ApiModelProperty(value = "当前文件块，从1开始")
    private Integer chunkNumber;
    /**
     * 分块大小
     */
	@ApiModelProperty(value = "分块大小")
    private Long chunkSize;
    /**
     * 当前分块大小
     */
	@ApiModelProperty(value = "当前分块大小")
    private Long currentChunkSize;
    /**
     * 总大小
     */
	@ApiModelProperty(value = "总大小")
    private Long totalSize;
    /**
     * 文件标识
     */
	@ApiModelProperty(value = "文件标识")
    private String identifier;
    /**
     * 文件名
     */
    @ApiModelProperty(value = "文件名")
    private String filename;
    /**
     * 相对路径
     */
    @ApiModelProperty(value = "相对路径")
    private String relativePath;
    /**
     * 总块数
     */
    @ApiModelProperty(value = "总块数")
    private Integer totalChunks;

    /**
     * 二进制文件
     */
    @TableField(exist=false)
    @ApiModelProperty(value = "二进制文件")
    private MultipartFile file;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getChunkNumber() {
        return chunkNumber;
    }

    public void setChunkNumber(Integer chunkNumber) {
        this.chunkNumber = chunkNumber;
    }

    public Long getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(Long chunkSize) {
        this.chunkSize = chunkSize;
    }

    public Long getCurrentChunkSize() {
        return currentChunkSize;
    }

    public void setCurrentChunkSize(Long currentChunkSize) {
        this.currentChunkSize = currentChunkSize;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public Integer getTotalChunks() {
        return totalChunks;
    }

    public void setTotalChunks(Integer totalChunks) {
        this.totalChunks = totalChunks;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
