export const baseVolumeListOperations = [
  {
    key: 'addStorageVolume',
    type: 'addStorageVolume',
    text: '新增',
    disabledFn: (selectedKey: React.Key[], clickPool: Record<string, any>) =>
      clickPool?.status !== 1,
  },
  {
    key: 'delStorageVolume',
    type: 'delStorageVolume',
    text: '删除',
    disabledFn: (selectedKey: React.Key[], clickPool: Record<string, any>) =>
      selectedKey?.length <= 0 || clickPool?.status !== 1,
  },
]

export const lvmVolumeListOperations = [...baseVolumeListOperations]

export const storageVolumeListOperations = [
  ...baseVolumeListOperations,
  {
    key: 'uploadStorageVolume',
    type: 'uploadStorageVolume',
    text: '上传',
    disabledFn: (selectedKey: React.Key[], clickPool: Record<string, any>) =>
      clickPool?.status !== 1,
  },
  {
    key: 'downloadStorageVolume',
    alias: 'storageVolumeOperate',
    type: 'download',
    text: '下载',
    disabledFn: (selectedKey: React.Key[], clickPool: Record<string, any>) =>
      selectedKey?.length !== 1 || clickPool?.status !== 1,
  },
]

import { useAppDispatch, useAppSelector } from '@redux/store'
import { FormDataType } from '@/components/multipleFormItem/type'
import {
  SetBasicVolumeId,
  SetBasicVolumePath,
  SetCapacity,
  SetDelete,
} from './components/storageComponents/storageComponents'
import eventEmitter from '@/events/index'
import { deleteStorageData, downloadFile, saveStorageData } from '@/api/modules/storagePools'
import AntdFormPopup from '@/components/antdFormPopup'
import {
  setDeleteVolume,
  setDownloadVolume,
  setLoading,
  setVolumeInfo,
} from '@/redux/modules/deviceManagement/storageVolume'
import { message, Modal } from 'antd'
import { DeleteDataCenterWrap } from './components/deleteModal/style'
import { batchOperationTip } from '@/utils/util'
import { setTaskList } from '@/redux/modules/taskBoard'
import downloadFn from '@/utils/download'
export const basicPathColumns = [
  {
    title: '存储卷名称',
    dataIndex: 'storageVolumeName',
    key: 'storageVolumeName',
  },
  {
    title: '文件类型',
    dataIndex: 'filesystem',
    key: 'filesystem',
  },
  {
    title: '容量',
    dataIndex: 'capacity',
    key: 'capacity',
  },
]
export const deleteVolumeFormData: FormDataType[] = [
  {
    id: 'deleteAlert',
    type: 'custom',
    component: SetDelete,
  },
  {
    id: 'rbFlag',
    content: '放入回收站',
    type: 'radio',
    formItemProps: {
      required: true,
      rules: [
        {
          required: true,
        },
      ],
    },
    componentProps: {
      options: [
        { label: '是', value: true },
        { label: '否', value: false },
      ],
    },
  },
]
export const volumeFormData: FormDataType[] = [
  {
    id: 'capacity',
    content: '容量',
    type: 'custom',
    component: SetCapacity,
    formItemProps: {
      required: true,
      rules: [
        {
          required: true,
        },
      ],
    },
    componentProps: {
      placeholder: '请输入容量',
    },
  },
  {
    id: 'storageVolumeName',
    content: '存储卷名称',
    type: 'input',
    formItemProps: {
      required: true,
      rules: [
        {
          required: true,
        },
      ],
    },
    componentProps: {
      placeholder: '请输入存储卷名称',
    },
  },
  {
    id: 'createFormat',
    content: '存储卷类型',
    type: 'radio',
    formItemProps: {
      required: true,
      rules: [
        {
          required: true,
        },
      ],
    },
    componentProps: {
      options: [
        { value: 'qcow2', label: 'qcow2' },
        { value: 'raw', label: 'raw' },
      ],
      placeholder: '请选择存储卷类型',
    },
  },
  {
    id: 'basicVolumePath',
    content: '基础镜像',
    type: 'custom',
    component: SetBasicVolumePath,
    formItemProps: {
      rules: [{}],
    },
    componentProps: {
      resultIsObject: true,
      fieldNames: {
        valueField: 'storagePoolId',
        title: 'storagePoolName',
        subtitle: 'poolType',
        describe: 'sta',
      },
      columns: [
        {
          title: '文件名称',
          dataIndex: 'storageVolumeName',
          key: 'storageVolumeName',
        },
        {
          title: '类型',
          dataIndex: 'filesystem',
          key: 'filesystem',
        },
        {
          title: '文件大小',
          dataIndex: 'fileSize',
          key: 'fileSize',
        },
      ],
      valueField: 'storagePath',
      popupTitle: '选择基础镜像',
      isShowPool: true,
      tableRowKey: 'storageId',
      readOnly: true,
    },
  },
  {
    id: 'basicVolumeId',
    content: '镜像Id',
    type: 'custom',
    component: SetBasicVolumeId,
    formItemProps: {
      rules: [{}],
    },
    componentProps: {
      disabled: true,
    },
  },
]

export function VolumeDevice() {
  const dispatch = useAppDispatch()
  const { edit } = useAppSelector(store => store.storageVolumeOperate)
  const { visible, initialValues, type, formData } = edit

  const onFinish = async (value: Record<string, any>) => {
    const { capacity, storageVolumeName, createFormat, basicVolumePath, basicVolumeId } = value
    const { storagePoolId, storageType } = initialValues
    const { inputValue, unitValue } = capacity
    const newCapacity = inputValue + unitValue
    const params =
      createFormat === 'qcow2'
        ? {
            capacity: newCapacity,
            storageVolumeName,
            createFormat,
            basicVolumePath,
            basicVolumeId,
            filesystem: createFormat,
            storagePoolId,
            storageType,
          }
        : {
            capacity: newCapacity,
            storageVolumeName,
            createFormat,
            filesystem: createFormat,
            storagePoolId,
            storageType,
          }

    dispatch(setVolumeInfo({ visible: false, initialValues: {}, type: 'add', formData: [] }))
    const result: { msg?: string; code?: number } = await saveStorageData(params)
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      eventEmitter.emit('poolListLoad', { type: 'reset', isPool: false })
    }
  }
  const onFinishFailed = (data: any) => {
    console.log('onFinishFailed==>', data)
  }
  const onCancel = () => {
    dispatch(setVolumeInfo({ visible: false, initialValues: {}, type: 'add', formData: [] }))
  }

  return (
    <AntdFormPopup
      popupTitle={'新增存储卷'}
      visible={visible}
      width={'600px'}
      height={'300px'}
      formInitialValues={initialValues}
      formData={formData}
      labelCol={{ span: 6 }}
      wrapperCol={{ span: 18 }}
      onFinish={onFinish}
      onFinishFailed={onFinishFailed}
      onCancel={onCancel}
    />
  )
}

export function DeleteVolume() {
  const dispatch = useAppDispatch()
  const { delete: delteeData } = useAppSelector(store => store.storageVolumeOperate)
  const { visible, title, formData, initialValues } = delteeData
  const onFinish = async (value: Record<string, any>) => {
    const { storageIds } = initialValues
    const { rbFlag } = value
    dispatch(setDeleteVolume({ visible: false, title: '', formData: [], initialValues: {} }))
    const {
      flag,
      errorList = [],
      successList = [],
    } = await deleteStorageData({
      storageIds,
      rbFlag,
    })
    dispatch(setTaskList([...errorList, ...successList]))
    if (flag) {
      message.success('存储卷删除成功', 2)
      eventEmitter.emit('poolListLoad', { type: 'reset', isPool: false })
      eventEmitter.emit('poolListLoad', { type: 'clearSelected', isPool: false })
    }
    if (!flag) {
      batchOperationTip(storageIds, errorList, successList)
      if (successList.length > 0) {
        eventEmitter.emit('poolListLoad', { type: 'reset', isPool: false })
        eventEmitter.emit('poolListLoad', { type: 'clearSelected', isPool: false })
      }
    }
  }
  const onFinishFailed = (data: any) => {
    console.log('onFinishFailed==>', data)
  }
  const onCancel = () => {
    dispatch(setDeleteVolume({ visible: false, title: '', formData: [], initialValues: {} }))
  }
  return (
    <AntdFormPopup
      popupTitle={title}
      visible={visible}
      width={'600px'}
      height={'600px'}
      formInitialValues={initialValues}
      formData={formData}
      labelCol={{ span: 6 }}
      wrapperCol={{ span: 18 }}
      onFinish={onFinish}
      onFinishFailed={onFinishFailed}
      onCancel={onCancel}
    />
  )
}

const failInfo = (dispatch: any) => {
  dispatch(setLoading({ isloading: false, percent: 0 }))
  message.error('下载文件失败')
}
export function DownLoadVolume() {
  const dispatch = useAppDispatch()
  const { download } = useAppSelector(store => store.storageVolumeOperate)
  const { visible, title, content, values } = download
  const { storageVolumeName = '', filesystem = '' } = values

  const loadingState = (evt: any) => {
    const percent = parseInt(`${(evt.loaded / evt.total) * 100}`)
    if (isNaN(percent)) {
      return
    } else {
      percent === 100
        ? dispatch(setLoading({ isloading: false, percent: 100 }))
        : dispatch(setLoading({ isloading: true, percent }))
    }
  }

  const downloadVolume = async () => {
    dispatch(
      setDownloadVolume({
        visible: false,
        title: '',
        content: '',
        values: { storageId: '', storageVolumeName: '', filesystem: '' },
      }),
    )
    try {
      const result: any = await downloadFile(values, loadingState)
      if (result.type === 'application/json') {
        const reader = new FileReader()
        reader.readAsText(result, 'utf-8')
        reader.onload = e => {
          failInfo(dispatch)
        }
        return
      }
      const fileName = `${storageVolumeName}.${filesystem}`
      fileName && downloadFn(result, fileName)
    } catch {
      failInfo(dispatch)
    }
  }
  const handleCancel = () => {
    dispatch(
      setDownloadVolume({
        visible: false,
        title: '',
        content: '',
        values: { storageId: '', storageVolumeName: '', filesystem: '' },
      }),
    )
  }
  return (
    <Modal title={title} open={visible} onOk={downloadVolume} onCancel={handleCancel}>
      <DeleteDataCenterWrap>{content}</DeleteDataCenterWrap>
    </Modal>
  )
}
