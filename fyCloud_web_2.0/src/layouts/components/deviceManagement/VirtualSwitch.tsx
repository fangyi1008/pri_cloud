import { FormDataType } from '@/components/multipleFormItem/type'
import StepPopup from '@components/stepPopup/index'
import {
  setVirtualSwitchDelete,
  setVirtualSwitchInfo,
} from '@/redux/modules/deviceManagement/virtualSwitch'
import { useAppDispatch, useAppSelector } from '@/redux/store'
import { useState } from 'react'
import { DeleteModal } from './components'
import { VSSelectHost, LinkModeAndLoadMode, SelectNetMachine } from './components/virtualSwitch'
import { arrayToSeparatorStr, batchOperationTip } from '@/utils/util'
import {
  deleteVirtualSwitchData,
  editVirtualSwitchData,
  saveVirtualSwitchData,
} from '@/api/modules/virtualSwitch'
import eventEmitter from '@/events/index'
import { message } from 'antd'
import { isObject } from '@/utils/is'
import { setTaskList } from '@/redux/modules/taskBoard'

export const virtualSwitchListOperations = [
  {
    key: 'addVirtualSwitch',
    type: 'addVirtualSwitch',
    text: '新增',
  },
  {
    key: 'editVirtualSwitch',
    type: 'editVirtualSwitch',
    text: '编辑',
    disabledFn: (selectedKey: React.Key[]) => selectedKey?.length !== 1,
  },
  {
    key: 'deleteVirtualSwitch',
    type: 'deleteVirtualSwitch',
    text: '删除',
    disabledFn: (selectedKey: React.Key[]) => selectedKey?.length <= 0,
  },
]

const baseInfo: FormDataType[] = [
  {
    id: 'hostId',
    content: '主机名称',
    type: 'custom',
    component: VSSelectHost,
    formItemProps: {
      required: true,
      rules: [
        {
          required: true,
        },
      ],
    },
  },
  {
    id: 'vmSwitchName',
    content: '名称',
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
      placeholder: '请输入名称',
    },
  },
  {
    id: 'networkType',
    content: '网络类型',
    type: 'checkbox',
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
        { value: '1', label: '管理网络' },
        { value: '2', label: '业务网络' },
        { value: '3', label: '存储网络' },
      ],
    },
  },
  {
    id: 'mtuSize',
    content: 'MTU',
    type: 'inputNumber',
    formItemProps: {
      required: true,
      rules: [
        {
          required: true,
        },
      ],
    },
    componentProps: {
      placeholder: '请输入取值范围为1000-9000的数',
      max: 9000,
      min: 1000,
      style: { width: '100%' },
      step: 1,
    },
  },
]

const configureNetwork: FormDataType[] = [
  {
    id: 'netMachine',
    content: '物理接口',
    type: 'custom',
    component: SelectNetMachine,
    formItemProps: {},
  },
  {
    id: 'linkMode',
    content: '链路聚合模式',
    component: LinkModeAndLoadMode,
    type: 'custom',
    componentProps: {
      options: [
        { value: 'static', label: '静态' },
        { value: 'dynamic', label: '动态' },
      ],
    },
  },
  {
    id: 'loadMode',
    component: LinkModeAndLoadMode,
    content: '负载分担模式',
    type: 'custom',
    componentProps: {
      options: [
        { value: 'equal', label: '均衡' },
        { value: 'main', label: '主备' },
      ],
    },
  },
  {
    id: 'ip',
    content: 'ip地址',
    type: 'input',
    componentProps: {
      placeholder: '请输入ip地址',
    },
    formItemProps: {
      rules: [
        {
          pattern: /^((2[0-4]\d|25[0-5]|[01]?\d\d?)\.){3}(2[0-4]\d|25[0-5]|[01]?\d\d?)$/,
          message: 'ip地址格式错误',
        },
      ],
    },
  },
  {
    id: 'gateway',
    content: '网关',
    type: 'input',
    componentProps: {
      placeholder: '请输入网关',
    },
    formItemProps: {
      rules: [
        {
          pattern: /^(192\.168(\.(\d|([1-9]\d)|(1\d{2})|(2[0-4]\d)|(25[0-5]))){2})$/,
          message: '网关格式式错误！',
        },
      ],
    },
  },
  {
    id: 'netMask',
    content: '子网掩码',
    type: 'input',
    componentProps: {
      placeholder: '请输入子网掩码',
    },
    formItemProps: {
      rules: [
        {
          pattern:
            /^(254|252|248|240|224|192|128|0)\.0\.0\.0|255\.(254|252|248|240|224|192|128|0)\.0\.0|255\.255\.(254|252|248|240|224|192|128|0)\.0|255\.255\.255\.(254|252|248|240|224|192|128|0)$/,
          message: '子网掩码格式式错误！',
        },
      ],
    },
  },
]

export const virtualSwitchFromData = [
  {
    key: '1',
    title: '基本信息',
    data: baseInfo,
  },
  {
    key: '2',
    title: '配置网络',
    data: configureNetwork,
  },
]

export function VirtualSwitch() {
  const [forStepCurrent, setForStepCurrent] = useState(0)
  const dispatch = useAppDispatch()
  const { edit } = useAppSelector(store => store.virtualSwitch)
  const userInfo = useAppSelector(store => store.global.userInfo)
  const { userId } = userInfo || {}
  const { visible, initialValues, type, formData } = edit
  const onFinish = async (value: Record<string, any>) => {
    if (type === 'add') {
      const {
        vmSwitchName,
        gateway,
        hostId,
        ip,
        mtuSize,
        netMask,
        networkType,
        netMachine,
        linkMode,
        loadMode,
      } = value
      const params = {
        createTime: Date.now(),
        createUserId: userId,
        gateway,
        hostId,
        ip,
        linkMode,
        loadMode,
        mtuSize: mtuSize,
        netMachine: arrayToSeparatorStr(netMachine),
        netMask,
        networkType: arrayToSeparatorStr(networkType),
        vmSwitchName,
      }
      const { flag } = await saveVirtualSwitchData(params)
      if (flag) {
        eventEmitter.emit('virtualSwitchListLoad', { type: 'reset' })
        setForStepCurrent(0)
        dispatch(
          setVirtualSwitchInfo({ visible: false, initialValues: {}, type: 'add', formData: [] }),
        )
      }
    } else {
      const { vmSwitchId } = initialValues
      const {
        vmSwitchName,
        gateway,
        hostId,
        ip,
        mtuSize,
        netMask,
        networkType = [],
        netMachine = [],
        linkMode = [],
        loadMode = [],
      } = value
      const params = {
        vmSwitchId,
        gateway,
        hostId,
        ip,
        linkMode: arrayToSeparatorStr(linkMode),
        loadMode: arrayToSeparatorStr(loadMode),
        mtuSize: mtuSize,
        netMachine: arrayToSeparatorStr(netMachine),
        netMask,
        networkType: arrayToSeparatorStr(networkType),
        vmSwitchName,
      }
      const { flag } = await editVirtualSwitchData(params)
      if (flag) {
        eventEmitter.emit('virtualSwitchListLoad', { type: 'reset' })
        eventEmitter.emit('virtualSwitchListLoad', { type: 'clearSelected' })
        eventEmitter.emit('changeSelectedName', { vmSwitchId, vmSwitchName })
        setForStepCurrent(0)
        dispatch(
          setVirtualSwitchInfo({ visible: false, initialValues: {}, type: 'add', formData: [] }),
        )
      }
    }
  }
  const onFinishFailed = (data: any) => {
    console.log('onFinishFailed==>', data)
  }
  const onCancel = () => {
    setForStepCurrent(0)
    dispatch(setVirtualSwitchInfo({ visible: false, initialValues: {}, type: 'add', formData: [] }))
  }

  const onStepChangeHandle = (current: number) => {
    setForStepCurrent(current)
  }

  const transformResultData = (data: Record<string, any>) => {
    const newTransformValue: Record<string, any> = {}
    const netWrokToStr: Record<string, string> = {
      '1': '管理网络',
      '2': '业务网络',
      '3': '存储网络',
    }
    const linkModeToStr: Record<string, string> = {
      static: '静态',
      dynamic: '动态',
    }
    const loadModeToStr: Record<string, string> = {
      equal: '均衡',
      main: '主备',
    }
    const getValueStr = (data: any[], typeToStr: Record<string, string>) => {
      if (Array.isArray(data) && data.length > 0) {
        return data.map((item: string) => typeToStr[item] || '').join(',')
      }
      return data
    }
    if (isObject(data)) {
      for (const key in data) {
        switch (key) {
          case 'networkType':
            newTransformValue[key] = getValueStr(data[key], netWrokToStr)
            break
          case 'linkMode':
            newTransformValue[key] = linkModeToStr[data[key]]
            break
          case 'loadMode':
            newTransformValue[key] = loadModeToStr[data[key]]
            break
          default:
            newTransformValue[key] = data[key]
            break
        }
      }
    }

    return { ...data, ...newTransformValue }
  }

  return visible ? (
    <StepPopup
      transformResultData={transformResultData}
      onFinishFailed={onFinishFailed}
      formInitialValues={initialValues}
      onStepChange={onStepChangeHandle}
      width={'800px'}
      onCancel={onCancel}
      current={forStepCurrent}
      popupTitle={type === 'add' ? '新增虚拟交换机' : '修改交换机'}
      onFinish={onFinish}
      isShowResult
      visible={visible}
      steps={formData}
    />
  ) : null
}

export function DeleteVirtualSwitch() {
  const dispatch = useAppDispatch()
  const { delete: delteeData } = useAppSelector(store => store.virtualSwitch)
  const { visible, title, content, values } = delteeData
  const deleteVirtualSwitch = async () => {
    const { flag, errorList = [], successList = [] } = await deleteVirtualSwitchData(values)
    dispatch(setTaskList([...errorList, ...successList]))
    if (flag) {
      message.success('虚拟交换机删除成功', 2)
      eventEmitter.emit('virtualSwitchListLoad', { type: 'reset' })
      eventEmitter.emit('virtualSwitchListLoad', { type: 'clearSelected' })
      eventEmitter.emit('hidenPortFluxList', { vmSwitchIds: values })
      dispatch(setVirtualSwitchDelete({ visible: false, title: '', content: '', values: [] }))
    }
    if (!flag) {
      batchOperationTip(values, errorList, successList)
    }
  }
  const handleCancel = () => {
    dispatch(setVirtualSwitchDelete({ visible: false, title: '', content: '', values: [] }))
  }
  return (
    <DeleteModal
      visible={visible}
      title={title}
      content={content}
      handleCancel={handleCancel}
      handleOk={deleteVirtualSwitch}
    />
  )
}
