import './index.less'
import {
  IAssignBrowseFnParams,
  ICreateAssignBrowseParam,
  ISliceUploadPropsType,
  ISliceUploadRef,
  IUploaderOptions,
} from './type'
import SimpleUploader from 'simple-uploader.js'
import UploaderContext from './uploaderContext'
import {
  useRef,
  forwardRef,
  useImperativeHandle,
  useEffect,
  useState,
  Ref,
  useCallback,
} from 'react'
import { SliceUploadWrap, HiddenButtonWrap, ListPanlWrap } from './style'
import eventEmitter from '@/events/index'
import SparkMD5 from 'spark-md5'
import Unsupport from './components/unsupport'
import FileListPanl from './components/list'
import { nextTick } from '@/utils/util'
import { fileAcceptConfig } from '@/common/utils'
import { useNavigate } from 'react-router-dom'
import { message } from 'antd'

function SliceUpload(props: ISliceUploadPropsType, ref: Ref<ISliceUploadRef>) {
  const { autoStart = false, fileCheckFn, fileComplete } = props

  const [openStatus, setOpenStatus] = useState(false)
  const navigate = useNavigate()

  const targetWrapRef: React.RefObject<HTMLLabelElement> = useRef(null)
  const queryParamRef: React.MutableRefObject<Record<string, any>> = useRef({})
  const uploaderRef = useRef(
    new SimpleUploader({
      target: 'http://localhost:3000/upload',
      chunkSize: '2048000',
      fileParameterName: 'file',
      maxChunkRetries: 3,
      testChunks: true,
      checkChunkUploadedByResponse: function (chunk: any, message: string) {
        console.log('checkChunkUploadedByResponse==>', chunk, message)
        let skip = false
        try {
          const objMessage = JSON.parse(message)
          console.log('objMessage==>', objMessage)
          if (objMessage.chunkVo && objMessage.chunkVo.skipUpload) {
            skip = true
          } else {
            skip = (objMessage.chunkVo.uploaded || []).indexOf(chunk.offset + 1) >= 0
          }
        } catch (e) {
          return skip
        }
        return skip
      },
      processResponse: function (response: any, cb: any) {
        const data = JSON.parse(response)
        if (data.code === 401) {
          navigate(`/login`)
        }
        if (data.code === 'INTERNAL_SERVER_ERROR') {
          message.error('上传错误')
        }
        cb(null, response)
      },
      query: (file: any) => {
        return {
          ...file.params,
        }
      },
    }),
  )

  const createAssignBrowse = (param: ICreateAssignBrowseParam) => {
    const { singleFile = false, attributes = {} } = param
    const fileInput = document.querySelector<HTMLInputElement>('.open-file-window input')
    fileInput && fileInput.remove()
    const uploader = uploaderRef.current
    uploader.uploader.assignBrowse(targetWrapRef.current, false, singleFile, {
      ...attributes,
    })
  }

  const setUploadeConfig = useCallback((params: IAssignBrowseFnParams) => {
    const { options = {}, queryParam } = params
    queryParamRef.current = queryParam
    const { singleFile, accept, ...uploaderOption } = options
    const attributes: Record<string, string> = {}
    if (accept) {
      attributes['accept'] = accept || fileAcceptConfig.getAllFileType().join(',')
    }
    customizeOptions(uploaderOption)
    createAssignBrowse({ singleFile, attributes })
    console.log(targetWrapRef.current)
    targetWrapRef?.current?.click()
  }, [])

  useEffect(() => {
    const uploader = uploaderRef.current

    const filesSubmittedHandler = () => {
      if (autoStart) {
        uploader.upload()
      }
    }
    const openGlobalUploaderFn = (params: IAssignBrowseFnParams) => {
      setUploadeConfig(params)
    }
    eventEmitter.on('openGlobalUploader', openGlobalUploaderFn)
    uploader.on('filesSubmitted', filesSubmittedHandler)
    return () => {
      uploader.on('filesSubmitted', filesSubmittedHandler)
      eventEmitter.off('openGlobalUploader', openGlobalUploaderFn)
    }
  }, [])

  useImperativeHandle(ref, () => ({
    getUploader: () => uploaderRef.current,
    openSelectFileWindow: (params: IAssignBrowseFnParams) => {
      setUploadeConfig(params)
    },
  }))

  const statusSet = (id: string, status: 'md5') => {
    const statusMap = {
      md5: {
        text: '校验MD5',
        bgc: '#fff',
      },
    }
    nextTick(() => {
      const statusTag = document.createElement('p')
      statusTag.className = `custom-status-${id} custom-status`
      statusTag.innerText = statusMap[status].text
      statusTag.style.backgroundColor = statusMap[status].bgc
      const statusWrap = document.querySelector(`.file-${id} .uploader-file-status`)
      statusWrap && statusWrap.appendChild(statusTag)
    })
  }

  const computeMD5 = async (file: any): Promise<{ md5: string; file: any }> => {
    const fileReader = new FileReader()
    const blobSlice = File.prototype.slice
    let currentChunk = 0
    const chunkSize = 10 * 1024 * 1000
    const chunks = Math.ceil(file.size / chunkSize)
    const spark = new SparkMD5.ArrayBuffer()
    statusSet(file.id, 'md5')
    md5HideResumeAction(file.id, true)
    function loadNext() {
      const start = currentChunk * chunkSize
      const end = start + chunkSize >= file.size ? file.size : start + chunkSize
      fileReader.readAsArrayBuffer(blobSlice.call(file.file, start, end))
    }
    file.pause()
    loadNext()
    return new Promise((resolve, reject) => {
      fileReader.onload = e => {
        spark.append(e.target?.result as ArrayBuffer)
        if (currentChunk < chunks) {
          currentChunk++
          loadNext()
          nextTick(() => {
            const element = document.querySelector<HTMLElement>(`.custom-status-${file.id} `)
            const md5ProgressText = '校验MD5 ' + ((currentChunk / chunks) * 100).toFixed(0) + '%'
            element && (element.innerText = md5ProgressText)
          })
        } else {
          const md5 = spark.end()
          // md5计算完毕
          resolve({ md5, file })
        }
      }
      fileReader.onerror = function () {
        file.cancel()
        reject()
      }
    })
  }

  const fileAdded = async (file: any) => {
    file.params = queryParamRef.current || {}
    file.pause()
    setOpenStatus(true)
    const result = await computeMD5(file)
    const { md5 } = result
    file.uniqueIdentifier = md5
    const checkResult = fileCheckFn && (await fileCheckFn(file))
    if (checkResult) {
      return false
    }
    startUpload(result)
  }

  const md5HideResumeAction = (id: string, flag: boolean) => {
    nextTick(() => {
      const resumeButton = document.querySelector<HTMLSpanElement>(
        `.file-${id} .uploader-action .uploader-file-resume`,
      )
      resumeButton && (resumeButton.style.display = flag ? 'none' : '')
    })
  }

  const statusRemove = (id: string) => {
    nextTick(() => {
      const statusTag = document.querySelector(`.custom-status-${id}`)
      statusTag && statusTag.remove()
    })
  }

  const startUpload = ({ file }: { md5: string; file: any }) => {
    file.resume()
    statusRemove(file.id)
    md5HideResumeAction(file.id, false)
  }

  const onClose = () => {
    setOpenStatus(false)
    const uploader = uploaderRef.current
    uploader.cancel()
  }

  const customizeOptions = (opts: IUploaderOptions) => {
    const { target, chunkSize, fileParameterName, headers, testChunks } = opts
    const uploader = uploaderRef.current
    if (target) {
      uploader.opts.target = target
    }
    if (testChunks !== undefined || testChunks != null) {
      uploader.opts.testChunks = testChunks
    }
    if (chunkSize && chunkSize > 1 * 1024 * 1024) {
      uploader.opts.chunkSize = chunkSize
    }
    if (fileParameterName) {
      uploader.opts.fileParameterName = fileParameterName
    }
    if (headers) {
      uploader.opts.headers = { ...uploader.opts.headers, ...headers }
    }
  }

  return (
    <UploaderContext.Provider
      value={{
        uploaderRef,
        support: uploaderRef.current.support,
      }}
    >
      <SliceUploadWrap>
        <Unsupport></Unsupport>
        <HiddenButtonWrap ref={targetWrapRef} className='open-file-window'>
          选择文件
        </HiddenButtonWrap>
        <ListPanlWrap openStatus={openStatus}>
          <FileListPanl
            onClose={onClose}
            fileAdded={fileAdded}
            fileComplete={fileComplete}
          ></FileListPanl>
        </ListPanlWrap>
      </SliceUploadWrap>
    </UploaderContext.Provider>
  )
}

export default forwardRef(SliceUpload)
