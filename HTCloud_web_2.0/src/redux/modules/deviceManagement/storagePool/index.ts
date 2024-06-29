import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import {
  DeviceManagement,
  DeviceManagementAddOrEdit,
  poolOperateState,
  startPoolType,
  TipsDeviceType,
} from '@redux/interface'

const poolOperate: poolOperateState = {
  add: {
    visible: false,
    type: 'add',
    formData: [],
    initialValues: {},
  },
  edit: {
    visible: false,
    title: '编辑',
    formData: [],
    initialValues: {},
  },
  delete: {
    visible: false,
    title: '删除存储池',
    content: '您确定要删除该存储池吗?',
    values: [],
  },
  format: {
    visible: false,
    title: '格式化',
    formData: [],
    initialValues: {},
  },
  stop: {
    visible: false,
    title: '暂停存储池',
    content: '您确定要暂停该存储池吗?',
    values: { poolType: '', storagePoolId: '' },
  },
  start: {
    visible: false,
    title: '启动存储池',
    content: '您确定要启动该存储池吗?',
    values: { poolType: '', storagePoolId: '' },
  },
}

const poolOperateSlice = createSlice({
  name: 'poolOperate',
  initialState: poolOperate,
  reducers: {
    setPoolInfo: (state, action: PayloadAction<DeviceManagementAddOrEdit>) => {
      const { visible, type, formData, initialValues } = action.payload
      state.add = { visible, type, formData: formData as any, initialValues }
    },
    setEditPool: (state, action: PayloadAction<DeviceManagement>) => {
      const { visible, title, formData, initialValues } = action.payload
      state.edit = { visible, title, formData: formData as any, initialValues }
    },
    setDeletePool: (state, action: PayloadAction<TipsDeviceType>) => {
      state.delete = action.payload
    },
    setFormatPool: (state, action: PayloadAction<DeviceManagement>) => {
      const { visible, title, formData, initialValues } = action.payload
      state.format = { visible, title, formData: formData as any, initialValues }
    },
    setStartPool: (state, action: PayloadAction<startPoolType>) => {
      state.start = action.payload
    },
    setStopPool: (state, action: PayloadAction<startPoolType>) => {
      state.stop = action.payload
    },
  },
})

export const { setPoolInfo, setEditPool, setDeletePool, setFormatPool, setStartPool, setStopPool } =
  poolOperateSlice.actions
export default poolOperateSlice.reducer
