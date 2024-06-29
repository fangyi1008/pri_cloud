import { useEffect, useRef, useState, useMemo, useCallback } from 'react'
import { useReactive } from 'ahooks'
import Uploader from 'simple-uploader.js'
import { IFileItemPropsType } from '../type'
import {
  FilePanelBodyItem,
  UploaderFileWrap,
  UploaderFileProgress,
  UploaderFileInfo,
  UploaderFileName,
  UploaderFileSize,
  UploaderFileMeta,
  UploaderFileStatus,
  UploaderFileActions,
} from './style'

export default function FileItem(props: IFileItemPropsType) {
  const { file } = props
  const fileInfo = useReactive({
    paused: file.paused,
    error: file.error,
    averageSpeed: 0,
    currentSpeed: 0,
    isComplete: file.isComplete(),
    isUploading: file.isUploading(),
    size: file.getSize(),
    formatedSize: file.getFormatSize(),
    uploadedSize: file.sizeUploaded(),
    progress: file.progress(),
    timeRemaining: file.timeRemaining(),
    type: file.getType(),
    extension: file.getExtension(),
  })

  const fileCategory = () => {
    const extension = fileInfo.extension
    const isFolder = file.isFolder
    let type = isFolder ? 'folder' : 'unknown'
    const categoryMap = file.uploader.opts.categoryMap
    const typeMap = categoryMap || {
      image: ['gif', 'jpg', 'jpeg', 'png', 'bmp', 'webp'],
      video: ['mp4', 'm3u8', 'rmvb', 'avi', 'swf', '3gp', 'mkv', 'flv'],
      audio: ['mp3', 'wav', 'wma', 'ogg', 'aac', 'flac'],
      document: [
        'doc',
        'txt',
        'docx',
        'pages',
        'epub',
        'pdf',
        'numbers',
        'csv',
        'xls',
        'xlsx',
        'keynote',
        'ppt',
        'pptx',
      ],
    }
    Object.keys(typeMap).forEach(_type => {
      const extensions = typeMap[_type]
      if (extensions.indexOf(extension) > -1) {
        type = _type
      }
    })
    return type
  }

  const actionCheck = () => {
    fileInfo.paused = file.paused
    fileInfo.error = file.error
    fileInfo.isUploading = file.isUploading()
  }

  const pause = () => {
    file.pause()
    actionCheck()
    fileProgressEvent()
  }
  const resume = () => {
    file.resume()
    actionCheck()
  }
  const remove = () => {
    file.cancel()
  }

  const retry = () => {
    file.retry()
    fileInfo.progress = 0
    setTimeout(() => {
      actionCheck()
    }, 200)
  }

  const status = useMemo(() => {
    const isUploading = fileInfo.isUploading
    const isComplete = fileInfo.isComplete
    const isError = fileInfo.error
    const paused = fileInfo.paused

    if (isComplete) {
      return 'success'
    } else if (isError) {
      return 'error'
    } else if (isUploading) {
      return 'uploading'
    } else if (paused) {
      return 'paused'
    } else {
      return 'waiting'
    }
  }, [fileInfo.isUploading, fileInfo.isComplete, fileInfo.error, fileInfo.paused])

  const statusText = useMemo(() => {
    const fileStatusText = {
      success: '上传成功',
      error: '上传失败',
      uploading: '上传中',
      paused: '已暂停',
      waiting: '等待上传',
    }
    return fileStatusText[status]
  }, [status])

  const formatedAverageSpeed = useMemo(() => {
    return `${Uploader.utils.formatSize(fileInfo.averageSpeed)} / s`
  }, [fileInfo.averageSpeed])

  const fileProgressEvent = (...argment: any[]) => {
    const fileTarte = argment[0]
    if (fileTarte !== file) {
      return
    }
    fileInfo.progress = file.progress()
    fileInfo.averageSpeed = file.averageSpeed
    fileInfo.currentSpeed = file.currentSpeed
    fileInfo.timeRemaining = file.timeRemaining()
    fileInfo.uploadedSize = file.sizeUploaded()
    actionCheck()
  }

  const fileSuccessEvent = (...argment: any[]) => {
    const fileTarte = argment[0]
    if (fileTarte !== file) {
      return
    }
    fileProgressEvent(...argment)
    fileInfo.error = false
    fileInfo.isComplete = true
    fileInfo.isUploading = false
  }

  const fileErrorEvent = (...argment: any[]) => {
    const fileTarte = argment[0]
    if (fileTarte !== file) {
      return
    }
    fileProgressEvent(...argment)
    fileInfo.error = true
    fileInfo.isComplete = false
    fileInfo.isUploading = false
  }

  useEffect(() => {
    file.uploader.on('fileProgress', fileProgressEvent)
    file.uploader.on('fileSuccess', fileSuccessEvent)
    file.uploader.on('fileError', fileErrorEvent)
    return () => {
      file.uploader.off('fileProgress', fileProgressEvent)
      file.uploader.on('fileSuccess', fileSuccessEvent)
      file.uploader.on('fileError', fileErrorEvent)
    }
  }, [])

  const progressStyle = () => {
    const progress = Math.floor(fileInfo.progress * 100)
    const style = `translateX(${Math.floor(progress - 100)}%)`
    return {
      progress: `${progress}%`,
      transform: style,
    }
  }

  return (
    <FilePanelBodyItem>
      <UploaderFileWrap status={status} className='uploader-file'>
        <UploaderFileProgress
          className='uploader-file-progress'
          status={status}
          style={progressStyle()}
        />
        <UploaderFileInfo className={'file-' + file.id}>
          <UploaderFileName>
            <i className='uploader-file-icon' data-icon={fileCategory()}></i>
            {file.name}
          </UploaderFileName>
          <UploaderFileSize>{fileInfo.formatedSize}</UploaderFileSize>
          <UploaderFileMeta></UploaderFileMeta>
          <UploaderFileStatus className='uploader-file-status'>
            {status !== 'uploading' ? (
              <span>{statusText}</span>
            ) : (
              <span>
                <span>{progressStyle().progress}</span>
                <em style={{ marginLeft: '4px' }}>{formatedAverageSpeed}</em>
              </span>
            )}
          </UploaderFileStatus>
          <UploaderFileActions className='uploader-action'>
            <span className='uploader-file-pause' onClick={pause}></span>
            <span className='uploader-file-resume' onClick={resume}></span>
            <span className='uploader-file-retry' onClick={retry}></span>
            <span className='uploader-file-remove' onClick={remove}></span>
          </UploaderFileActions>
        </UploaderFileInfo>
      </UploaderFileWrap>
    </FilePanelBodyItem>
  )
}
