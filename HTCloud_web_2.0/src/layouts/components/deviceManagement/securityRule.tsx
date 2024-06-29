import {
  deleteSecurityRuleData,
  editSecurityRuleData,
  saveSecurityRuleData,
} from '@/api/modules/network'
import AntdFormPopup from '@/components/antdFormPopup'
import { setDeleteRule, setRuleInfo } from '@/redux/modules/deviceManagement/securityRule'
import { useAppDispatch, useAppSelector } from '@/redux/store'
import { DeleteModal } from './components'
import eventEmitter from '@/events/index'
import { FormDataType } from '@/components/multipleFormItem/type'
import { message } from 'antd'
import { batchOperationTip } from '@/utils/util'
import { setTaskList } from '@/redux/modules/taskBoard'

export const securityRuleListOperations = [
  {
    key: 'addSecurityRule',
    type: 'addSecurityRule',
    text: '新增',
  },
  {
    key: 'editSecurityRule',
    type: 'editSecurityRule',
    text: '编辑',
    disabledFn: (selectedKey: React.Key[]) => selectedKey?.length !== 1,
  },
  {
    key: 'deleteSecurityRule',
    type: 'deleteSecurityRule',
    text: '删除',
    disabledFn: (selectedKey: React.Key[]) => selectedKey?.length <= 0,
  },
]
const flowOptions = [
  { label: '入方向', value: '1' },
  { label: '出方向', value: '2' },
  { label: '入方向、出方向', value: '3' },
]
const agreeTypeOptions = [
  { label: 'ALL', value: 'ALL' },
  { label: 'ICMP', value: 'ICMP' },
  { label: 'TCP', value: 'TCP' },
  { label: 'UDP', value: 'UDP' },
]
const actionOptions = [
  { label: '允许', value: 'accept' },
  { label: '拒绝', value: 'drop' },
]
export const ruleformData: FormDataType[] = [
  {
    id: 'inOutFlow',
    content: '方向',
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
      placeholder: '请选择方向',
      options: flowOptions,
    },
  },
  {
    id: 'agreeType',
    content: '协议',
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
      placeholder: '请选择协议',
      options: agreeTypeOptions,
    },
  },
  {
    id: 'sourceIp',
    content: '源IP地址',
    type: 'input',
    formItemProps: {
      rules: [
        {
          pattern: /^((2[0-4]\d|25[0-5]|[01]?\d\d?)\.){3}(2[0-4]\d|25[0-5]|[01]?\d\d?)$/,
          message: '请输入正确格式的ip地址',
        },
      ],
    },
    componentProps: {
      placeholder: '请输入源IP地址',
    },
  },
  {
    id: 'sourceMask',
    content: '源子网掩码',
    type: 'input',
    formItemProps: {
      rules: [
        {
          pattern:
            /^(254|252|248|240|224|192|128|0)\.0\.0\.0|255\.(254|252|248|240|224|192|128|0)\.0\.0|255\.255\.(254|252|248|240|224|192|128|0)\.0|255\.255\.255\.(254|252|248|240|224|192|128|0)$/,
          message: '请输入正确格式的子网掩码',
        },
      ],
    },
    componentProps: {
      placeholder: '请输入源子网掩码',
    },
  },
  {
    id: 'sourcePort',
    content: '源端口',
    type: 'input',
    formItemProps: {
      rules: [
        {
          pattern: /^((6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])|[0-5]?\d{0,4})$/g,
          message: '请输入正确格式的端口号',
        },
      ],
    },
    componentProps: {
      placeholder: '请输入源端口',
    },
  },
  {
    id: 'destIp',
    content: '目的IP地址',
    type: 'input',
    formItemProps: {
      rules: [
        {
          pattern: /^((2[0-4]\d|25[0-5]|[01]?\d\d?)\.){3}(2[0-4]\d|25[0-5]|[01]?\d\d?)$/,
          message: '请输入正确格式的ip地址',
        },
      ],
    },
    componentProps: {
      placeholder: '请输入目的IP地址',
    },
  },
  {
    id: 'destMask',
    content: '目的子网掩码',
    type: 'input',
    formItemProps: {
      rules: [
        {
          pattern:
            /^(254|252|248|240|224|192|128|0)\.0\.0\.0|255\.(254|252|248|240|224|192|128|0)\.0\.0|255\.255\.(254|252|248|240|224|192|128|0)\.0|255\.255\.255\.(254|252|248|240|224|192|128|0)$/,
          message: '请输入正确格式的子网掩码',
        },
      ],
    },
    componentProps: {
      placeholder: '请输入目的子网掩码',
    },
  },
  {
    id: 'destPort',
    content: '目的端口',
    type: 'input',
    formItemProps: {
      rules: [
        {
          pattern: /^((6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])|[0-5]?\d{0,4})$/g,
          message: '请输入正确格式的端口号',
        },
      ],
    },
    componentProps: {
      placeholder: '请输入目的端口',
    },
  },
  {
    id: 'action',
    content: '动作',
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
      placeholder: '请选择动作',
      options: actionOptions,
    },
  },
]
export function SecurityRuleDevice() {
  const dispatch = useAppDispatch()
  const { edit } = useAppSelector(store => store.ruleOperate)
  const { visible, initialValues, type, formData } = edit
  const userInfo = useAppSelector(store => store.global.userInfo)
  const { userId } = userInfo || {}
  const onFinish = async (value: Record<string, any>) => {
    if (type === 'add') {
      const { securityGroupId } = initialValues
      dispatch(setRuleInfo({ visible: false, initialValues: {}, type: 'add', formData: [] }))
      const result: { msg?: string; code?: number } = await saveSecurityRuleData({
        createTime: Date.now(),
        createUserId: userId,
        ...value,
        securityGroupId,
      })
      const { msg, code } = result
      if (msg === 'success' && code === 0) {
        eventEmitter.emit('groupListLoad', { type: 'reset', isGroup: false })
      }
    } else {
      const { securityGroupId, ruleId } = initialValues
      const result: { msg: string; code: number } | undefined = await editSecurityRuleData({
        securityGroupId,
        ruleId,
        ...value,
      })
      const { msg, code } = result || {}
      if (msg === 'success' && code === 0) {
        eventEmitter.emit('groupListLoad', { type: 'reload', isGroup: false })
        eventEmitter.emit('groupListLoad', { type: 'clearSelected', isGroup: false })
      }
      dispatch(
        setRuleInfo({
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
    dispatch(setRuleInfo({ visible: false, initialValues: {}, type: 'add', formData: [] }))
  }

  return (
    <AntdFormPopup
      popupTitle={type === 'add' ? '新建安全规则' : '编辑安全规则'}
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

export function DeleteSecurityRule() {
  const dispatch = useAppDispatch()
  const { delete: delteeData } = useAppSelector(store => store.ruleOperate)
  const { visible, title, content, values } = delteeData
  const deleteSecurityRule = async () => {
    dispatch(setDeleteRule({ visible: false, title: '', content: '', values: [] }))
    const { flag, errorList = [], successList = [] } = await deleteSecurityRuleData(values)
    dispatch(setTaskList([...errorList, ...successList]))
    if (flag) {
      message.success('安全规则删除成功', 2)
      eventEmitter.emit('groupListLoad', { type: 'reload', isGroup: false })
      eventEmitter.emit('groupListLoad', { type: 'clearSelected', isGroup: false })
    }
    if (!flag) {
      batchOperationTip(values, errorList, successList)
      if (successList.length > 0) {
        eventEmitter.emit('groupListLoad', { type: 'reload', isGroup: false })
        eventEmitter.emit('groupListLoad', { type: 'clearSelected', isGroup: false })
      }
    }
  }
  const handleCancel = () => {
    dispatch(setDeleteRule({ visible: false, title: '', content: '', values: [] }))
  }
  return (
    <DeleteModal
      visible={visible}
      title={title}
      content={content}
      handleCancel={handleCancel}
      handleOk={deleteSecurityRule}
    />
  )
}
