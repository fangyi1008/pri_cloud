import { useAppDispatch, useAppSelector } from '@redux/store'
import AntdFormPopup from '@components/antdFormPopup/index'
import {
  setHostInfo,
  setDeleteHost,
  setEnterMaintain,
  setRestart,
  setClose,
  setStartUp,
  setExitMaintain,
} from '@redux/modules/deviceManagement/hostOperate'
import { FormDataType } from '@/components/multipleFormItem/type'
import { DeleteModal } from './components'
import CryptoJS from 'crypto-js'
import {
  deleteAsyncHostData,
  editAsyncHostData,
  hostStartUp,
  inMaintenance,
  outMaintenance,
  reboot,
  saveAsyncHostData,
  turnOff,
} from '@/api/modules/host'
import { GetMenuData } from './dataCenter'
import eventEmitter from '@/events/index'
import { reduxGetHostInfo } from '@/redux/modules/hostInfo'
import { message } from 'antd'
import { batchOperationTip } from '@/utils/util'
import { setTaskList } from '@/redux/modules/taskBoard'
import { useNavigate } from 'react-router'
import { Host } from '@/api/interface'

export const hostListOperations = [
  {
    key: 'editHost',
    type: 'editHost',
    text: '编辑',
    disabledFn: (selectedRow: Host.IHostType[]) =>
      selectedRow?.length !== 1 || ['3', '4', '5'].includes(selectedRow?.[0]?.state),
  },
  {
    key: 'delHost',
    type: 'delHost',
    text: '删除',
    disabledFn: (selectedRow: Host.IHostType[]) => selectedRow?.length <= 0,
  },
]
const hostMenuData = [
  {
    label: '修改主机',
    type: 'edit',
    key: 'editHost',
    disabledFn: (record: any) => ['3', '4', '5'].includes(record?.state),
  },
  {
    label: '删除主机',
    type: 'delete',
    key: 'delHost',
    disabledFn: (record: any) => ['3', '4', '5'].includes(record?.state),
  },
  {
    label: '开机',
    key: 'startUp',
    disabledFn: (record: any) => record.state !== '2',
  },
  {
    label: '关机',
    key: 'turnOff',
    disabledFn: (record: any) => ['4', '5'].includes(record?.state),
  },
  {
    label: '重启',
    key: 'reboot',
    disabledFn: (record: any) => ['4', '5'].includes(record?.state),
  },
]

export const hostOperations = [
  {
    key: 'addVirtualMachine',
    type: 'add',
    text: '新增虚拟机',
    disabledFn: (record: any) => ['2', '3', '4', '5'].includes(record?.state),
  },
  {
    key: 'enter',
    type: 'edit',
    text: '进入维护模式',
    disabledFn: (record: any) => ['3', '4', '5'].includes(record?.state),
  },
  {
    key: 'outer',
    type: 'outer',
    text: '退出维护模式',
    disabledFn: (record: any) => record.state !== '3',
  },
  {
    key: 'more',
    type: 'operate',
    text: '更多操作',
    isSelect: true,
    menuData: hostMenuData,
  },
]

export const edmitHostFormData: FormDataType[] = [
  {
    id: 'hostName',
    content: '主机名称',
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
      placeholder: '请输入主机名称',
    },
  },
  // {
  //   id: 'hostPassword',
  //   content: '主机密码',
  //   type: 'input',
  //   formItemProps: {
  //     required: true,
  //     rules: [
  //       {
  //         required: true,
  //       },
  //     ],
  //   },
  //   componentProps: {
  //     type: 'password',
  //     placeholder: '请输入主机密码',
  //   },
  // },
]

export const hostFormData: FormDataType[] = [
  {
    id: 'hostUser',
    content: '主机用户名',
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
      placeholder: '请输入主机用户名',
    },
  },
  {
    id: 'hostPassword',
    content: '主机密码',
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
      type: 'password',
      placeholder: '请输入主机密码',
    },
  },
  {
    id: 'osIp',
    content: '操作系统管理IP',
    type: 'input',
    formItemProps: {
      required: true,
      rules: [
        {
          pattern: /^((2[0-4]\d|25[0-5]|[01]?\d\d?)\.){3}(2[0-4]\d|25[0-5]|[01]?\d\d?)$/,
          message: '请输入正确格式的ip地址',
          required: true,
        },
      ],
    },
    componentProps: {
      placeholder: '请输入操作系统管理IP',
    },
  },
]

const getInfo = async (dispatch: any, hostId: string) => {
  dispatch(reduxGetHostInfo(hostId))
}

export function HostDevice() {
  const dispatch = useAppDispatch()
  const { edit } = useAppSelector(store => store.hostOperate)
  const { visible, initialValues, type, formData } = edit
  const onFinish = async (value: Record<string, any>) => {
    if (type === 'add') {
      const { dataCenterId, clusterId } = initialValues
      const { hostPassword } = value
      const key = CryptoJS.enc.Utf8.parse('404142434445464748494A4B4C4D4E4C')
      const encrypted = CryptoJS.AES.encrypt(hostPassword, key, {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7,
      })
      const encryptedPwd = encrypted.ciphertext.toString()
      const encryptData = CryptoJS.enc.Utf8.parse(encryptedPwd)
      const base64Data = CryptoJS.enc.Base64.stringify(encryptData)
      dispatch(setHostInfo({ visible: false, initialValues: {}, type: 'add', formData: [] }))
      const result: { msg?: string; code?: number } = await saveAsyncHostData({
        dataCenterId,
        clusterId,
        ...value,
        hostPassword: base64Data,
      })
      const { msg, code } = result
      if (msg === 'success' && code === 0) {
        eventEmitter.emit('hostListLoad', { type: 'reset' })
        GetMenuData(dispatch)
      }
    } else {
      const { hostName } = value
      const { hostId } = initialValues
      const result: { msg: string; code: number } | undefined = await editAsyncHostData({
        hostId,
        hostName,
      })
      const { msg, code } = result || {}
      if (msg === 'success' && code === 0) {
        GetMenuData(dispatch)
        eventEmitter.emit('hostListLoad', { type: 'reload' })
        eventEmitter.emit('hostListLoad', { type: 'clearSelected' })
        getInfo(dispatch, hostId)
      }
      dispatch(
        setHostInfo({
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
    dispatch(setHostInfo({ visible: false, initialValues: {}, type: 'add', formData: [] }))
  }

  return (
    <AntdFormPopup
      popupTitle={type === 'add' ? '新建主机' : '编辑主机'}
      visible={visible}
      width={'700px'}
      height={'300px'}
      labelCol={{ span: 6 }}
      wrapperCol={{ span: 18 }}
      formInitialValues={initialValues}
      formData={formData}
      onFinish={onFinish}
      onFinishFailed={onFinishFailed}
      onCancel={onCancel}
    />
  )
}

export function DeleteHost() {
  const navigate = useNavigate()
  const dispatch = useAppDispatch()
  const { delete: delteeData } = useAppSelector(store => store.hostOperate)
  const { visible, title, content, values } = delteeData
  const deleteDataCenter = async () => {
    const { dataCenterId, clusterId, ids } = values
    dispatch(
      setDeleteHost({
        visible: false,
        title: '',
        content: '',
        values: { dataCenterId: '', clusterId: '', ids: [] },
      }),
    )
    const { flag, errorList = [], successList = [] } = await deleteAsyncHostData(ids)
    dispatch(setTaskList([...errorList, ...successList]))
    if (flag) {
      message.success('主机删除成功', 2)
      const path = clusterId
        ? `cluster/${clusterId}`
        : dataCenterId
        ? `dataCenter/${dataCenterId}`
        : ''
      path && navigate(path)
      GetMenuData(dispatch)
      eventEmitter.emit('hostListLoad', { type: 'reload' })
      eventEmitter.emit('hostListLoad', { type: 'clearSelected' })
    }
    if (!flag) {
      batchOperationTip(ids, errorList, successList)
      if (successList.length > 0) {
        GetMenuData(dispatch)
        eventEmitter.emit('hostListLoad', { type: 'reload' })
        eventEmitter.emit('hostListLoad', { type: 'clearSelected' })
      }
    }
  }
  const handleCancel = () => {
    dispatch(
      setDeleteHost({
        visible: false,
        title: '',
        content: '',
        values: { dataCenterId: '', clusterId: '', ids: [] },
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

export const enterMaintainFormData: FormDataType[] = [
  {
    id: 'moveFlag',
    content: '是否迁移虚拟机',
    type: 'radio',
    componentProps: {
      options: [
        { label: '是', value: true },
        { label: '否', value: false },
      ],
    },
  },
]

export function EnterMaintainHost() {
  const dispatch = useAppDispatch()
  const { enterMaintain } = useAppSelector(store => store.hostOperate)
  const { visible, title, initialValues, formData } = enterMaintain
  const { hostId } = initialValues
  const onFinish = async (value: Record<string, any>) => {
    dispatch(setEnterMaintain({ visible: false, title: '', initialValues: [], formData: [] }))
    const { moveFlag } = value
    const result: { msg?: string; code?: number } = await inMaintenance({
      moveFlag,
      hostId,
    })
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      GetMenuData(dispatch)
      eventEmitter.emit('hostListLoad', { type: 'reload' })
      eventEmitter.emit('hostListLoad', { type: 'clearSelected' })
      getInfo(dispatch, hostId)
    }
  }
  const onFinishFailed = (data: any) => {
    console.log('onFinishFailed==>', data)
  }
  const onCancel = () => {
    dispatch(setEnterMaintain({ visible: false, title: '', initialValues: [], formData: [] }))
  }
  return (
    <AntdFormPopup
      popupTitle={title}
      visible={visible}
      width={'400px'}
      height={'300px'}
      labelCol={{ span: 10 }}
      wrapperCol={{ span: 14 }}
      formInitialValues={initialValues}
      formData={formData}
      onFinish={onFinish}
      onFinishFailed={onFinishFailed}
      onCancel={onCancel}
    />
  )
}

export function ExitMaintainHost() {
  const dispatch = useAppDispatch()
  const { exitMaintain } = useAppSelector(store => store.hostOperate)
  const { visible, title, content, values } = exitMaintain
  const exitMaintainFn = async () => {
    const [hostId] = values
    const result: { msg?: string; code?: number } = await outMaintenance({ hostId: String(hostId) })
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      GetMenuData(dispatch)
      eventEmitter.emit('hostListLoad', { type: 'reload' })
      eventEmitter.emit('hostListLoad', { type: 'clearSelected' })
      getInfo(dispatch, String(hostId))
    }
    dispatch(setExitMaintain({ visible: false, title: '', content: '', values: [] }))
  }
  const handleCancel = () => {
    dispatch(setExitMaintain({ visible: false, title: '', content: '', values: [] }))
  }
  return (
    <DeleteModal
      visible={visible}
      title={title}
      content={content}
      handleCancel={handleCancel}
      handleOk={exitMaintainFn}
    />
  )
}

export function OpenHost() {
  const dispatch = useAppDispatch()
  const { startUp } = useAppSelector(store => store.hostOperate)
  const { visible, title, content, values } = startUp
  const openHost = async () => {
    const [hostInfo] = values
    const { bmcIp, hostUser, hostPassword, hostId } = hostInfo
    const result: { msg?: string; code?: number } = await hostStartUp({
      bmcIp,
      hostUser,
      hostPassword,
      hostId,
    })
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      GetMenuData(dispatch)
      eventEmitter.emit('hostListLoad', { type: 'reload' })
      eventEmitter.emit('hostListLoad', { type: 'clearSelected' })
      getInfo(dispatch, String(hostId))
    }
    dispatch(setStartUp({ visible: false, title: '', content: '', values: [] }))
  }
  const handleCancel = () => {
    dispatch(setStartUp({ visible: false, title: '', content: '', values: [] }))
  }
  return (
    <DeleteModal
      visible={visible}
      title={title}
      content={content}
      handleCancel={handleCancel}
      handleOk={openHost}
    />
  )
}

export function CloseHost() {
  const dispatch = useAppDispatch()
  const { close } = useAppSelector(store => store.hostOperate)
  const { visible, title, content, values } = close
  const closeHost = async () => {
    const [hostId] = values
    const result: { msg?: string; code?: number } = await turnOff({ hostId: String(hostId) })
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      GetMenuData(dispatch)
      eventEmitter.emit('hostListLoad', { type: 'reload' })
      eventEmitter.emit('hostListLoad', { type: 'clearSelected' })
      getInfo(dispatch, String(hostId))
    }
    dispatch(setClose({ visible: false, title: '', content: '', values: [] }))
  }
  const handleCancel = () => {
    dispatch(setClose({ visible: false, title: '', content: '', values: [] }))
  }
  return (
    <DeleteModal
      visible={visible}
      title={title}
      content={content}
      handleCancel={handleCancel}
      handleOk={closeHost}
    />
  )
}

export function RestartHost() {
  const dispatch = useAppDispatch()
  const { restart } = useAppSelector(store => store.hostOperate)
  const { visible, title, content, values } = restart
  const restartHost = async () => {
    const [hostId] = values
    const result: { msg?: string; code?: number } = await reboot({ hostId: String(hostId) })
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      GetMenuData(dispatch)
      eventEmitter.emit('hostListLoad', { type: 'reload' })
      eventEmitter.emit('hostListLoad', { type: 'clearSelected' })
      getInfo(dispatch, String(hostId))
    }
    dispatch(setRestart({ visible: false, title: '', content: '', values: [] }))
  }
  const handleCancel = () => {
    dispatch(setRestart({ visible: false, title: '', content: '', values: [] }))
  }
  return (
    <DeleteModal
      visible={visible}
      title={title}
      content={content}
      handleCancel={handleCancel}
      handleOk={restartHost}
    />
  )
}
