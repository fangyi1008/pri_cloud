import {
  deleteSecurityGroupData,
  editSecurityGroupData,
  saveSecurityGroupData,
} from '@/api/modules/network'
import AntdFormPopup from '@/components/antdFormPopup'
import { setDeleteGroup, setGroupInfo } from '@/redux/modules/deviceManagement/securityGroup'
import { useAppDispatch, useAppSelector } from '@/redux/store'
import { DeleteModal } from './components'
import eventEmitter from '@/events/index'
import { FormDataType } from '@/components/multipleFormItem/type'
import { message } from 'antd'
import { batchOperationTip } from '@/utils/util'
import { setTaskList } from '@/redux/modules/taskBoard'

export const securityGroupListOperations = [
  {
    key: 'addSecurityGroup',
    type: 'addSecurityGroup',
    text: '新增',
  },
  {
    key: 'editSecurityGroup',
    type: 'editSecurityGroup',
    text: '编辑',
    disabledFn: (selectedKey: React.Key[]) => selectedKey?.length !== 1,
  },
  {
    key: 'deleteSecurityGroup',
    type: 'deleteSecurityGroup',
    text: '删除',
    disabledFn: (selectedKey: React.Key[]) => selectedKey?.length <= 0,
  },
]
export const groupFormData: FormDataType[] = [
  {
    id: 'securityGroupName',
    content: '安全组名称',
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
      placeholder: '请输入安全组名称',
    },
  },
  {
    id: 'securityGroupRemark',
    content: '安全组描述',
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
      placeholder: '请输入安全组描述',
    },
  },
]
export function SecurityGroupDevice() {
  const dispatch = useAppDispatch()
  const { edit } = useAppSelector(store => store.groupOperate)
  const { visible, initialValues, type, formData } = edit
  const userInfo = useAppSelector(store => store.global.userInfo)
  const { userId } = userInfo || {}
  const onFinish = async (value: Record<string, any>) => {
    if (type === 'add') {
      dispatch(setGroupInfo({ visible: false, initialValues: {}, type: 'add', formData: [] }))
      const result: { msg?: string; code?: number } = await saveSecurityGroupData({
        createTime: Date.now(),
        createUserId: userId,
        ...value,
      })
      const { msg, code } = result
      if (msg === 'success' && code === 0) {
        eventEmitter.emit('groupListLoad', { type: 'reset', isGroup: true })
      }
    } else {
      const { securityGroupId } = initialValues
      const result: { msg: string; code: number } | undefined = await editSecurityGroupData({
        securityGroupId,
        ...value,
      })
      const { msg, code } = result || {}
      if (msg === 'success' && code === 0) {
        eventEmitter.emit('groupListLoad', {
          type: 'reload',
          isGroup: true,
          value: { ...value, securityGroupId },
        })
      }
      dispatch(
        setGroupInfo({
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
    dispatch(setGroupInfo({ visible: false, initialValues: {}, type: 'add', formData: [] }))
  }

  return (
    <AntdFormPopup
      popupTitle={type === 'add' ? '新建安全组' : '编辑安全组'}
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

export function DeleteGroup() {
  const dispatch = useAppDispatch()
  const { delete: delteeData } = useAppSelector(store => store.groupOperate)
  const { visible, title, content, values } = delteeData
  const deleteGroup = async () => {
    dispatch(setDeleteGroup({ visible: false, title: '', content: '', values: [] }))
    const { flag, errorList = [], successList = [] } = await deleteSecurityGroupData(values)
    dispatch(setTaskList([...errorList, ...successList]))
    const successValue = [...successList].map(item => item.operObj)
    if (flag) {
      message.success('安全组删除成功', 2)
      eventEmitter.emit('groupListLoad', {
        type: 'reload',
        isGroup: true,
        isDelete: true,
        value: [...successValue],
      })
      eventEmitter.emit('groupListLoad', { type: 'clearSelected', isGroup: true })
    }
    if (!flag) {
      batchOperationTip(values, errorList, successList)
      if (successList.length > 0) {
        eventEmitter.emit('groupListLoad', {
          type: 'reload',
          isGroup: true,
          isDelete: true,
          value: [...successValue],
        })
        eventEmitter.emit('groupListLoad', { type: 'clearSelected', isGroup: true })
      }
    }
  }
  const handleCancel = () => {
    dispatch(setDeleteGroup({ visible: false, title: '', content: '', values: [] }))
  }
  return (
    <DeleteModal
      visible={visible}
      title={title}
      content={content}
      handleCancel={handleCancel}
      handleOk={deleteGroup}
    />
  )
}
