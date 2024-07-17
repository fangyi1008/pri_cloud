import { useAppDispatch, useAppSelector } from '@redux/store'
import AntdFormPopup from '@components/antdFormPopup/index'
import {
  setDataCenterInfo,
  setDeleteDataCenter,
} from '@redux/modules/deviceManagement/dataCenterOperate'
import { FormDataType } from '@/components/multipleFormItem/type'
import { DeleteModal } from './components'
import {
  deleteAsyncDataCenterData,
  editAsyncDataCenterData,
  saveAsyncDataCenterData,
} from '@/api/modules/datacenter'
import { getMenuData, setMenuList } from '@/redux/modules/menu/menu'
import { useLocation, useNavigate } from 'react-router'
import eventEmitter from '@/events/index'
import { batchOperationTip } from '@/utils/util'
import { message } from 'antd'
import { setTaskList } from '@/redux/modules/taskBoard'

export const dataCenterFormData: FormDataType[] = [
  {
    id: 'dataCenterName',
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
      placeholder: '请输入数据中心名称',
    },
  },
]

export const dataCenterListOperations = [
  {
    key: 'editDataCenter',
    type: 'editDataCenter',
    text: '编辑',
    disabledFn: (selectedKey: React.Key[]) => selectedKey?.length !== 1,
  },
  {
    key: 'delDataCenter',
    type: 'delDataCenter',
    text: '删除',
    disabledFn: (selectedKey: React.Key[]) => selectedKey?.length <= 0,
  },
]

export const dataCenterOperations = [
  {
    key: 'addCluster',
    type: 'add',
    text: '新增集群',
  },
  {
    key: 'addHost',
    type: 'add',
    text: '新增主机',
  },
  {
    key: 'delDataCenter',
    type: 'delete',
    text: '删除数据中心',
  },
]

export const GetMenuData = async (dispatch: any) => {
  const result = await dispatch(getMenuData())
  const payload = result.payload as Menu.MenuOptions[]
  dispatch(setMenuList(payload))
}

export function DataCenterDevice() {
  const dispatch = useAppDispatch()
  const { edit } = useAppSelector(store => store.dataCenterOperate)
  const { visible, initialValues, type, formData } = edit
  const userInfo = useAppSelector(store => store.global.userInfo)
  const { userId } = userInfo || {}
  const onFinish = async (value: Record<string, any>) => {
    if (type === 'add') {
      dispatch(setDataCenterInfo({ visible: false, initialValues: {}, type: 'add', formData: [] }))
      const result: { msg: string; code: number } | undefined = await saveAsyncDataCenterData({
        createTime: Date.now(),
        createUserId: userId,
        ...value,
      })
      const { msg, code } = result || {}
      if (msg === 'success' && code === 0) {
        eventEmitter.emit('dataCenterListLoad', { type: 'reset' })
        GetMenuData(dispatch)
      }
    } else {
      const { dataCenterId } = initialValues
      const result: { msg: string; code: number } | undefined = await editAsyncDataCenterData({
        ...value,
        createTime: Date.now(),
        dataCenterId,
        createUserId: userId,
      })
      const { msg, code } = result || {}
      if (msg === 'success' && code === 0) {
        GetMenuData(dispatch)
        eventEmitter.emit('dataCenterListLoad', { type: 'reload' })
        eventEmitter.emit('dataCenterListLoad', { type: 'clearSelected' })
      }
      dispatch(
        setDataCenterInfo({
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
    dispatch(setDataCenterInfo({ visible: false, initialValues: {}, type: 'add', formData: [] }))
  }

  return (
    <AntdFormPopup
      popupTitle={type === 'add' ? '新建数据中心' : '编辑数据中心'}
      visible={visible}
      width={'500px'}
      height={'300px'}
      formInitialValues={initialValues}
      formData={formData}
      onFinish={onFinish}
      onFinishFailed={onFinishFailed}
      onCancel={onCancel}
    />
  )
}

export function DeleteDataCenter() {
  const dispatch = useAppDispatch()
  const { delete: delteeData } = useAppSelector(store => store.dataCenterOperate)
  const { visible, title, content, values } = delteeData
  const { pathname } = useLocation()
  const navigate = useNavigate()
  const deleteDataCenter = async () => {
    dispatch(setDeleteDataCenter({ visible: false, title: '', content: '', values: [] }))
    const { flag, errorList = [], successList = [] } = await deleteAsyncDataCenterData(values)
    dispatch(setTaskList([...errorList, ...successList]))
    if (flag) {
      message.success('数据中心删除成功', 2)
      GetMenuData(dispatch)
      eventEmitter.emit('dataCenterListLoad', { type: 'reload' })
      eventEmitter.emit('dataCenterListLoad', { type: 'clearSelected' })
      pathname.split('/')[1] === 'dataCenter' ? navigate(`/compute`) : null
    }
    if (!flag) {
      batchOperationTip(values, errorList, successList)
      if (successList.length > 0) {
        GetMenuData(dispatch)
        eventEmitter.emit('dataCenterListLoad', { type: 'reload' })
        eventEmitter.emit('dataCenterListLoad', { type: 'clearSelected' })
      }
    }
  }
  const handleCancel = () => {
    dispatch(setDeleteDataCenter({ visible: false, title: '', content: '', values: [] }))
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
