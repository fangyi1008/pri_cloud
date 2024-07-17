import { useAppDispatch, useAppSelector } from '@/redux/store'
import { DeleteModal } from './components'
import eventEmitter from '@/events/index'
import { message } from 'antd'
import { batchOperationTip } from '@/utils/util'
import { setTaskList } from '@/redux/modules/taskBoard'
import { setDeleteRecycle, setResumeStorage } from '@/redux/modules/deviceManagement/recycleOperate'
import { deleteStorageData, resumeStorage } from '@/api/modules/storagePools'
import { StorageVolume } from '@/api/interface'

const operationState = (selectedData: StorageVolume.IStorageVolumeType[]) => {
  if (selectedData?.length === 1) {
    return selectedData?.[0]?.poolStatus !== 1
  }
  return selectedData?.length > 1 ? false : true
}
export const recycleListOperations = [
  {
    key: 'resumeStorage',
    type: 'resumeStorage',
    text: '恢复',
    disabledFn: (selectedData: StorageVolume.IStorageVolumeType[]) => operationState(selectedData),
  },
  {
    key: 'deleteStorage',
    type: 'deleteStorage',
    text: '删除',
    disabledFn: (selectedData: StorageVolume.IStorageVolumeType[]) => operationState(selectedData),
  },
]

export function RecycleDevice() {
  const dispatch = useAppDispatch()
  const { edit } = useAppSelector(store => store.recycleOperate)
  const { visible, title, content, values } = edit
  const resume = async () => {
    dispatch(setResumeStorage({ visible: false, title: '', content: '', values: [] }))
    const { flag, errorList = [], successList = [] } = await resumeStorage(values)
    dispatch(setTaskList([...errorList, ...successList]))
    if (flag) {
      message.success('恢复存储卷成功', 2)
      eventEmitter.emit('recycleListLoad', { type: 'reload' })
      eventEmitter.emit('recycleListLoad', { type: 'clearSelected' })
    }
    if (!flag) {
      batchOperationTip(values, errorList, successList)
      if (successList.length > 0) {
        eventEmitter.emit('recycleListLoad', { type: 'reload' })
        eventEmitter.emit('recycleListLoad', { type: 'clearSelected' })
      }
    }
  }
  const handleCancel = () => {
    dispatch(setResumeStorage({ visible: false, title: '', content: '', values: [] }))
  }
  return (
    <DeleteModal
      visible={visible}
      title={title}
      content={content}
      handleCancel={handleCancel}
      handleOk={resume}
    />
  )
}

export function DeleteRecycleStorage() {
  const dispatch = useAppDispatch()
  const { delete: deleteData } = useAppSelector(store => store.recycleOperate)
  const { visible, title, content, values } = deleteData
  const deleteStorage = async () => {
    dispatch(setDeleteRecycle({ visible: false, title: '', content: '', values: [] }))
    const {
      flag,
      errorList = [],
      successList = [],
    } = await deleteStorageData({ storageIds: values, rbFlag: false })
    dispatch(setTaskList([...errorList, ...successList]))
    if (flag) {
      message.success('存储卷删除成功', 2)
      eventEmitter.emit('recycleListLoad', { type: 'reload' })
      eventEmitter.emit('recycleListLoad', { type: 'clearSelected' })
    }
    if (!flag) {
      batchOperationTip(values, errorList, successList)
      if (successList.length > 0) {
        eventEmitter.emit('recycleListLoad', { type: 'reload' })
        eventEmitter.emit('recycleListLoad', { type: 'clearSelected' })
      }
    }
  }
  const handleCancel = () => {
    dispatch(setDeleteRecycle({ visible: false, title: '', content: '', values: [] }))
  }
  return (
    <DeleteModal
      visible={visible}
      title={title}
      content={content}
      handleCancel={handleCancel}
      handleOk={deleteStorage}
    />
  )
}
