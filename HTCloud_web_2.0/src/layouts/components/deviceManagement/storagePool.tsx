export const storagePoolListOperations = [
  {
    key: 'addStoragePool',
    type: 'addStoragePool',
    text: '新增',
  },
  {
    key: 'editStoragePool',
    type: 'editStoragePool',
    text: '编辑',
    disabledFn: (selectedKey: React.Key[], row: BasicObj[]) =>
      selectedKey?.length !== 1 || [null].includes(row?.[0]?.status),
  },
  {
    key: 'deleteStoragePool',
    type: 'deleteStoragePool',
    text: '删除',
    disabledFn: (selectedKey: React.Key[], row: BasicObj[]) => selectedKey?.length <= 0,
  },
  {
    key: 'formatStoragePool',
    type: 'formatStoragePool',
    text: '格式化',
    disabledFn: (selectedKey: React.Key[], row: BasicObj[]) => {
      if (selectedKey?.length === 1 && ![null].includes(row?.[0]?.status)) {
        const [data] = row
        const { poolType } = data
        return !(poolType === 'iscsi')
      }
      return true
    },
  },
  {
    key: 'startStoragePool',
    type: 'startStoragePool',
    text: '启动',
    disabledFn: (selectedKey: React.Key[], row: BasicObj[]) =>
      selectedKey?.length !== 1 || [1, 3, null].includes(row?.[0]?.status),
  },
  {
    key: 'stopStoragePool',
    type: 'stopStoragePool',
    text: '暂停',
    disabledFn: (selectedKey: React.Key[], row: BasicObj[]) =>
      selectedKey?.length !== 1 || [4, 2, 3, null].includes(row?.[0]?.status),
  },
]

import { useAppDispatch, useAppSelector } from '@redux/store'
import { FormDataType } from '@/components/multipleFormItem/type'
import { DeleteModal } from './components'
import eventEmitter from '@/events/index'
import { useState } from 'react'
import StepPopup from '@components/stepPopup'
import {
  setDeletePool,
  setEditPool,
  setFormatPool,
  setPoolInfo,
  setStartPool,
  setStopPool,
} from '@/redux/modules/deviceManagement/storagePool'
import {
  SetAlert,
  SetFormat,
  SetIp,
  SetIqnFormat,
  SetIqnList,
  SetStoragePoolPath,
  SetWarn,
} from './components/storageComponents/storageComponents'
import {
  deleteStoragePoolData,
  editStoragePoolData,
  saveStoragePoolData,
  startStoragePool,
  stopStoragePool,
  storagePoolFormat,
} from '@/api/modules/storagePools'
import AntdFormPopup from '@/components/antdFormPopup'
import { BasicObj } from '@/components/showInfoCard/type'
import { hostDataInfo } from '@/api/modules/host'
import { message } from 'antd'
import { batchOperationTip } from '@/utils/util'
import { setTaskList } from '@/redux/modules/taskBoard'
export const oneFormData: (
  option: { value: string; label: string | undefined }[],
  isStorage?: string | undefined,
) => FormDataType[] = (option, isStorage) => [
  {
    id: 'hostId',
    content: '主机',
    type: 'select',
    formItemProps: {
      required: true,
      rules: [
        {
          required: true,
        },
      ],
    },
    componentProps: {
      disabled: isStorage ? true : false,
      options: option,
      placeholder: '请选择主机',
    },
  },
  {
    id: 'storagePoolName',
    content: '存储池名称',
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
      placeholder: '请输入存储池名称',
    },
  },
  {
    id: 'poolShowName',
    content: '显示名称',
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
      placeholder: '请输入显示名称',
    },
  },
  {
    id: 'poolType',
    content: '储存池类型',
    type: 'select',
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
        { value: 'dir', label: '本地文件目录' },
        { value: 'iscsi', label: 'iscsi存储' },
        { value: 'lvm', label: 'lvm存储' },
      ],
      placeholder: '请选择存储池类型',
    },
  },
]

export const dirFormData = [
  {
    id: 'storagePoolPath',
    content: '目标路径',
    type: 'custom',
    component: SetStoragePoolPath,
    formItemProps: {
      required: true,
      rules: [
        {
          required: true,
        },
      ],
    },
    componentProps: {
      disabled: true,
      placeholder: '请选择目标路径',
    },
  },
]

export const iscsiFormData = [
  ...dirFormData,
  {
    id: 'isFormdata',
    content: '是否格式化',
    type: 'checkbox',
    componentProps: {
      options: [{ label: '格式化', value: true }],
    },
  },
  {
    id: 'mkfsFormat',
    content: '格式化形式',
    type: 'custom',
    component: SetFormat,
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
        { label: 'ext2', value: 'ext2' },
        { label: 'ext3', value: 'ext3' },
        { label: 'ext4', value: 'ext4' },
      ],
    },
  },
  {
    id: 'warn',
    type: 'custom',
    component: SetWarn,
  },
  {
    id: 'storageIp',
    content: '存储ip',
    type: 'custom',
    component: SetIp,
    formItemProps: {
      required: true,
      rules: [
        {
          pattern:
            /^((25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))\.){3}(25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))$/,
          message: '请输入正确格式的ip地址',
        },
      ],
    },
    componentProps: {
      placeholder: '请输入存储ip',
    },
  },
  {
    id: 'iqn',
    content: 'target',
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
      disabled: true,
    },
  },
  {
    id: 'iqnFormat',
    content: '磁盘',
    type: 'custom',
    component: SetIqnList,
    formItemProps: {
      required: true,
      rules: [
        {
          required: true,
        },
      ],
    },
    componentProps: {
      options: [],
      placeholder: '请选择磁盘',
    },
  },
]

export const editPoolFormData = [
  {
    id: 'poolShowName',
    content: '显示名称',
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
      placeholder: '请输入显示名称',
    },
  },
]

export const formatPoolFormData = (data: BasicObj | undefined) => [
  {
    id: 'formatAlert',
    type: 'custom',
    component: SetAlert,
  },
  {
    id: 'mkfsFormat',
    content: '格式化形式',
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
        { label: 'ext2', value: 'ext2' },
        { label: 'ext3', value: 'ext3' },
        { label: 'ext4', value: 'ext4' },
      ],
    },
  },
  {
    id: 'iqnFormat',
    content: '磁盘',
    type: 'custom',
    component: SetIqnFormat,
    formItemProps: {
      required: true,
      rules: [
        {
          required: true,
        },
      ],
    },
    componentProps: {
      data,
    },
  },
]

export function PoolDevice() {
  const { info } = useAppSelector(state => state.hostInfo)
  const dispatch = useAppDispatch()
  const { add } = useAppSelector(store => store.storagePoolOperate)
  const { visible, initialValues, type, formData } = add
  const userInfo = useAppSelector(store => store.global.userInfo)
  const { userId } = userInfo || {}
  const [current, setCurrent] = useState(0)
  const [twoFormData, setTwoFormData] = useState<FormDataType[]>([])
  const steps = [
    {
      key: '1',
      title: '基本信息',
      data: formData,
    },
    {
      key: '2',
      title: '其他信息',
      data: twoFormData,
    },
  ]
  const onFinish = async (value: Record<string, any>) => {
    const { isFormdata, poolType, storagePoolName, poolShowName, storagePoolPath, hostId } = value
    isFormdata ? delete value.isFormdata : null
    let newValue = {}
    if (poolType !== 'iscsi') {
      newValue = { poolType, storagePoolName, poolShowName, storagePoolPath }
    } else {
      newValue = { ...value }
    }
    const { clusterId: cluster, dataCenterId: datacenter } = info
    const hostInfo =
      Reflect.has(initialValues, 'hostId') === false ? await hostDataInfo(hostId) : null
    const { host } = hostInfo || { host: { clusterId: '', dataCenterId: '' } }
    const { clusterId, dataCenterId } = host
    const params =
      hostInfo !== null
        ? {
            hostId,
            clusterId,
            dataCenterId,
            judge: '0',
            ...newValue,
          }
        : {
            hostId,
            clusterId: cluster,
            dataCenterId: datacenter,
            judge: '0',
            ...newValue,
          }
    dispatch(setPoolInfo({ visible: false, initialValues: {}, type: 'add', formData: [] }))
    const result: { msg?: string; code?: number } = await saveStoragePoolData(params)
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      eventEmitter.emit('poolListLoad', { type: 'reset', isPool: true })
    }
    setCurrent(0)
  }
  const onFinishFailed = (data: any) => {
    console.log('onFinishFailed==>', data)
  }
  const onCancel = () => {
    dispatch(setPoolInfo({ visible: false, initialValues: {}, type: 'add', formData: [] }))
    setCurrent(0)
  }
  const onValuesChange = (changedValues: any, values: any) => {
    const { poolType } = changedValues
    if (poolType) {
      if (changedValues.poolType === 'iscsi') {
        setTwoFormData(iscsiFormData)
      } else {
        setTwoFormData(dirFormData)
      }
    }
  }
  return visible ? (
    <StepPopup
      onValuesChange={onValuesChange}
      steps={steps}
      current={current}
      onStepChange={(current: number) => {
        setCurrent(current)
      }}
      layoutType={'horizontal'}
      popupTitle={'新建存储池'}
      formInitialValues={initialValues}
      visible={visible}
      onCancel={onCancel}
      onFinish={onFinish}
      onFinishFailed={onFinishFailed}
      isShowResult={true}
    />
  ) : null
}

export function DeletePool() {
  const dispatch = useAppDispatch()
  const { delete: delteeData } = useAppSelector(store => store.storagePoolOperate)
  const { visible, title, content, values } = delteeData
  const deletePool = async () => {
    dispatch(setDeletePool({ visible: false, title: '', content: '', values: [] }))
    const { flag, errorList = [], successList = [] } = await deleteStoragePoolData(values)
    dispatch(setTaskList([...errorList, ...successList]))
    const successValue = [...successList].map(item => item.storagePoolId)
    if (flag) {
      message.success('存储池删除成功', 2)
      eventEmitter.emit('poolListLoad', {
        type: 'reload',
        isPool: true,
        isDelete: true,
        value: [...successValue],
      })
      eventEmitter.emit('poolListLoad', { type: 'clearSelected', isPool: true })
    }
    if (!flag) {
      batchOperationTip(values, errorList, successList)
      if (successList.length > 0) {
        eventEmitter.emit('poolListLoad', {
          type: 'reload',
          isPool: true,
          isDelete: true,
          value: [...successValue],
        })
        eventEmitter.emit('poolListLoad', { type: 'clearSelected', isPool: true })
      }
    }
  }
  const handleCancel = () => {
    dispatch(setDeletePool({ visible: false, title: '', content: '', values: [] }))
  }
  return (
    <DeleteModal
      visible={visible}
      title={title}
      content={content}
      handleCancel={handleCancel}
      handleOk={deletePool}
    />
  )
}

export function EditPoolDevice() {
  const dispatch = useAppDispatch()
  const { edit } = useAppSelector(store => store.storagePoolOperate)
  const { visible, initialValues, title, formData } = edit
  const onFinish = async (value: Record<string, any>) => {
    const { storagePoolId } = initialValues
    const result: { msg: string; code: number } | undefined = await editStoragePoolData({
      storagePoolId,
      ...value,
    })
    const { msg, code } = result || {}
    if (msg === 'success' && code === 0) {
      eventEmitter.emit('poolListLoad', {
        type: 'reload',
        isPool: true,
        value: { ...value, storagePoolId },
      })
    }
    dispatch(
      setEditPool({
        visible: false,
        initialValues: {},
        title: '编辑存储池',
        formData: [],
      }),
    )
  }
  const onFinishFailed = (data: any) => {
    console.log('onFinishFailed==>', data)
  }
  const onCancel = () => {
    dispatch(setEditPool({ visible: false, initialValues: {}, title: '编辑存储池', formData: [] }))
  }

  return (
    <AntdFormPopup
      popupTitle={title}
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

export function StartPool() {
  const dispatch = useAppDispatch()
  const { start } = useAppSelector(store => store.storagePoolOperate)
  const { visible, title, content, values } = start
  const startPool = async () => {
    dispatch(
      setStartPool({
        visible: false,
        title: '',
        content: '',
        values: { poolType: '', storagePoolId: '' },
      }),
    )
    const result: { msg?: string; code?: number } = await startStoragePool(values)
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      eventEmitter.emit('poolListLoad', { type: 'reset', isPool: true })
    }
  }
  const handleCancel = () => {
    dispatch(
      setStartPool({
        visible: false,
        title: '',
        content: '',
        values: { poolType: '', storagePoolId: '' },
      }),
    )
  }
  return (
    <DeleteModal
      visible={visible}
      title={title}
      content={content}
      handleCancel={handleCancel}
      handleOk={startPool}
    />
  )
}

export function StopPool() {
  const dispatch = useAppDispatch()
  const { stop } = useAppSelector(store => store.storagePoolOperate)
  const { visible, title, content, values } = stop
  const stopPool = async () => {
    dispatch(
      setStopPool({
        visible: false,
        title: '',
        content: '',
        values: { poolType: '', storagePoolId: '' },
      }),
    )
    const result: { msg?: string; code?: number } = await stopStoragePool(values)
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      eventEmitter.emit('poolListLoad', { type: 'reset', isPool: true })
    }
  }
  const handleCancel = () => {
    dispatch(
      setStopPool({
        visible: false,
        title: '',
        content: '',
        values: { poolType: '', storagePoolId: '' },
      }),
    )
  }
  return (
    <DeleteModal
      visible={visible}
      title={title}
      content={content}
      handleCancel={handleCancel}
      handleOk={stopPool}
    />
  )
}
export function FormatPool() {
  const dispatch = useAppDispatch()
  const { format } = useAppSelector(store => store.storagePoolOperate)
  const { visible, initialValues, title, formData } = format
  const onFinish = async (value: Record<string, any>) => {
    const { storagePoolId } = initialValues
    const { mkfsFormat, iqnFormat } = value
    const result: { msg: string; code: number } | undefined = await storagePoolFormat({
      storagePoolId,
      mkfsFormat,
      iqnFormat,
    })
    const { msg, code } = result || {}
    if (msg === 'success' && code === 0) {
      eventEmitter.emit('poolListLoad', { type: 'reload', isPool: true })
    }
    dispatch(
      setFormatPool({
        visible: false,
        initialValues: {},
        title: '',
        formData: [],
      }),
    )
  }
  const onFinishFailed = (data: any) => {
    console.log('onFinishFailed==>', data)
  }
  const onCancel = () => {
    dispatch(setFormatPool({ visible: false, initialValues: {}, title: '', formData: [] }))
  }

  return (
    <AntdFormPopup
      popupTitle={title}
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
