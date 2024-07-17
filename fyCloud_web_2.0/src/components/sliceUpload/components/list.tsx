import { useState, useEffect, useContext } from 'react'
import { Tooltip } from 'antd'
import { useToggle } from 'ahooks'
import { ExpandOutlined, LineOutlined, CloseOutlined } from '@ant-design/icons'
import {
  FileList,
  FilePanel,
  FilePanelHead,
  FilePanelTitel,
  FilePanelOperate,
  OperateItem,
  FilePanelBody,
  ListEmpty,
} from './style'
import { IFileListPanlPropsType } from '../type'
import File from './file'
import UploaderContext from '../uploaderContext'

export default function FileListPanl(props: IFileListPanlPropsType) {
  const { onClose, fileAdded: fileAddedHandler, fileComplete: fileCompleteHandler } = props
  const [collapse, { toggle }] = useToggle()
  const { uploaderRef } = useContext(UploaderContext)
  const [fileListData, setFileListData] = useState<Record<string, any>>([])

  const fileAdded = (file: any) => {
    const uploader = uploaderRef?.current
    if (uploader) {
      fileAddedHandler && fileAddedHandler(file)
      file.pause()
      uploader.uploader.fileList
      setFileListData([...uploader.uploader.fileList])
    }
  }

  const fileRemoved = () => {
    const uploader = uploaderRef?.current
    if (uploader) {
      uploader.uploader.fileList
      setFileListData([...uploader.uploader.fileList])
    }
  }

  useEffect(() => {
    const uploader = uploaderRef?.current
    uploader && uploader.on('fileAdded', fileAdded)
    uploader && uploader.on('fileRemoved', fileRemoved)

    const fileComplete = (rootFile: any) => {
      fileCompleteHandler && fileCompleteHandler(rootFile)
    }
    uploader && uploader.on('fileComplete', fileComplete)
    return () => {
      uploader && uploader.off('fileAdded', fileAdded)
      uploader && uploader.off('fileRemoved', fileRemoved)
      uploader && uploader.off('fileComplete', fileComplete)
    }
  }, [])

  return (
    <FileList>
      <FilePanel>
        <FilePanelHead>
          <FilePanelTitel>文件列表</FilePanelTitel>
          <FilePanelOperate>
            <OperateItem onClick={toggle}>
              {collapse ? <ExpandOutlined /> : <LineOutlined />}
            </OperateItem>
            <OperateItem>
              <CloseOutlined onClick={onClose} />
            </OperateItem>
          </FilePanelOperate>
        </FilePanelHead>
        {fileListData.length > 0 ? (
          <FilePanelBody collapse={collapse}>
            {fileListData.map((item: any) => {
              return <File key={item.id} file={item}></File>
            })}
          </FilePanelBody>
        ) : (
          <ListEmpty collapse={collapse}>暂无待上传文件</ListEmpty>
        )}
      </FilePanel>
    </FileList>
  )
}
