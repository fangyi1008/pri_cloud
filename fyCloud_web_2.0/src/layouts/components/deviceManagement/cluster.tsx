import { useAppDispatch, useAppSelector } from '@redux/store'
import AntdFormPopup from '@components/antdFormPopup/index'
import { setClusterInfo, setDeleteCluster } from '@redux/modules/deviceManagement/clusterOperate'
import { FormDataType } from '@/components/multipleFormItem/type'
import { DeleteModal } from './components'
import {
  deleteAsyncClusterData,
  editAsyncClusterData,
  saveAsyncClusterData,
} from '@/api/modules/cluster'
import { GetMenuData } from './dataCenter'
import eventEmitter from '@/events/index'
import { Host, Cluster } from '@/api/interface'
import { batchOperationTip } from '@/utils/util'
import { message } from 'antd'
import { setTaskList } from '@/redux/modules/taskBoard'
import { useNavigate } from 'react-router'
export const clusterFormData: (
  option: { value: string; label: string }[],
  isDataCenter?: string | undefined,
) => FormDataType[] = (option, isDataCenter) => [
  {
    id: 'dataCenterId',
    content: '数据中心',
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
      disabled: isDataCenter ? true : false,
      options: option,
      placeholder: '请选择数据中心',
    },
  },
  {
    id: 'clusterName',
    content: '集群名称',
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
      placeholder: '请输入集群名称',
    },
  },
  {
    id: 'drsSwitch',
    content: '分布式调度',
    type: 'radio',
    componentProps: {
      options: [
        { label: '开启', value: 1 },
        { label: '关闭', value: 0 },
      ],
    },
  },
  {
    id: 'haSwitch',
    content: '高可用',
    type: 'radio',
    componentProps: {
      options: [
        { label: '开启', value: 1 },
        { label: '关闭', value: 0 },
      ],
    },
  },
]

export const clusterListOperations = [
  {
    key: 'editCluster',
    type: 'editCluster',
    text: '编辑',
    disabledFn: (selectedKey: React.Key[]) => selectedKey?.length !== 1,
  },
  {
    key: 'delHCluster',
    type: 'delCluster',
    text: '删除',
    disabledFn: (selectedKey: React.Key[]) => selectedKey?.length <= 0,
  },
]

export const clusterOperations = [
  {
    key: 'addHost',
    type: 'add',
    text: '新增主机',
    disabledFn: (clusterInfo?: Cluster.IClusterType | Host.IHostType) => {
      if (!clusterInfo) return true
      const { dataCenterId, clusterId } = clusterInfo as Cluster.IClusterType
      return !(dataCenterId && clusterId)
    },
  },
  {
    key: 'addVirtualMachine',
    type: 'add',
    text: '新增虚拟机',
    disabledFn: (hostInfo?: Cluster.IClusterType | Host.IHostType) => {
      if (!hostInfo) return true
      const { dataCenterId, hostId } = hostInfo as Host.IHostType
      return !(dataCenterId && hostId)
    },
  },
]

export function ClusterDevice() {
  const dispatch = useAppDispatch()
  const { edit } = useAppSelector(store => store.clusterOperate)
  const { visible, initialValues, type, formData } = edit
  const userInfo = useAppSelector(store => store.global.userInfo)
  const { userId } = userInfo || {}
  const onFinish = async (value: Record<string, any>) => {
    if (type === 'add') {
      dispatch(setClusterInfo({ visible: false, initialValues: {}, type: 'add', formData: [] }))
      const result: { msg?: string; code?: number } = await saveAsyncClusterData({
        createTime: Date.now(),
        createUserId: userId,
        ...value,
      })
      const { msg, code } = result
      if (msg === 'success' && code === 0) {
        eventEmitter.emit('clusterListLoad', { type: 'reset' })
        GetMenuData(dispatch)
      }
    } else {
      const { clusterId } = initialValues
      const result: { msg: string; code: number } | undefined = await editAsyncClusterData({
        clusterId,
        ...value,
        createTime: Date.now(),
        createUserId: userId,
      })
      const { msg, code } = result || {}
      if (msg === 'success' && code === 0) {
        GetMenuData(dispatch)
        eventEmitter.emit('clusterListLoad', { type: 'reload' })
        eventEmitter.emit('clusterListLoad', { type: 'clearSelected' })
      }
      dispatch(
        setClusterInfo({
          visible: false,
          initialValues: {},
          type: 'edit',
          formData: [],
        }),
      )
    }
  }
  const onFinishFailed = (data: any) => {
    console.log('onFinishFailed==>', data)
  }
  const onCancel = () => {
    dispatch(setClusterInfo({ visible: false, initialValues: {}, type: 'add', formData: [] }))
  }

  return (
    <AntdFormPopup
      popupTitle={type === 'add' ? '新建集群' : '编辑集群'}
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

export function DeleteCluster() {
  const navigate = useNavigate()
  const dispatch = useAppDispatch()
  const { delete: delteeData } = useAppSelector(store => store.clusterOperate)
  const { visible, title, content, values } = delteeData
  const deleteDataCenter = async () => {
    const { ids = [], dataCenterId = '' } = values
    dispatch(
      setDeleteCluster({
        visible: false,
        title: '',
        content: '',
        values: { dataCenterId: '', ids: [] },
      }),
    )
    const { flag, errorList = [], successList = [] } = await deleteAsyncClusterData(ids)
    dispatch(setTaskList([...errorList, ...successList]))
    if (flag) {
      message.success('集群删除成功', 2)
      const path = dataCenterId ? `dataCenter/${dataCenterId}` : ''
      path && navigate(path)
      GetMenuData(dispatch)
      eventEmitter.emit('clusterListLoad', { type: 'reload' })
      eventEmitter.emit('clusterListLoad', { type: 'clearSelected' })
    }
    if (!flag) {
      batchOperationTip(ids, errorList, successList)
      if (successList.length > 0) {
        GetMenuData(dispatch)
        eventEmitter.emit('clusterListLoad', { type: 'reload' })
        eventEmitter.emit('clusterListLoad', { type: 'clearSelected' })
      }
    }
  }
  const handleCancel = () => {
    dispatch(
      setDeleteCluster({
        visible: false,
        title: '',
        content: '',
        values: { dataCenterId: '', ids: [] },
      }),
    )
  }
  return (
    <DeleteModal
      visible={visible}
      title={title}
      content={content}
      handleCancel={handleCancel}
      handleOk={deleteDataCenter}
    />
  )
}
