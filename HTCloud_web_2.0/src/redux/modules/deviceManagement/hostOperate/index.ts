import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import {
  DeviceManagementAddOrEdit,
  HostOperateState,
  TipsDeviceType,
  DeviceManagement,
  startUpType,
  DeleteHostType,
} from '@redux/interface'

const hostOperateState: HostOperateState = {
  edit: {
    visible: false,
    type: 'add',
    formData: [],
    initialValues: {},
  },
  delete: {
    visible: false,
    title: '删除主机',
    content: '您确定要删除主机吗?',
    values: { dataCenterId: '', clusterId: '', ids: [] },
  },
  enterMaintain: {
    visible: false,
    title: '进入维护模式',
    formData: [],
    initialValues: {},
  },
  exitMaintain: {
    visible: false,
    title: '退出维护模式',
    content: '您确定要退出维护模式吗?',
    values: [],
  },
  startUp: {
    visible: false,
    title: '开机',
    content: '您确定要开机吗?',
    values: [],
  },
  close: {
    visible: false,
    title: '关闭',
    content: '您确定要关闭吗?',
    values: [],
  },
  restart: {
    visible: false,
    title: '重启',
    content: '您确定要重启吗?',
    values: [],
  },
}

const hostOperateSlice = createSlice({
  name: 'hostOperateState',
  initialState: hostOperateState,
  reducers: {
    setHostInfo: (state, action: PayloadAction<DeviceManagementAddOrEdit>) => {
      const { visible, type, formData, initialValues } = action.payload
      state.edit = { visible, type, formData: formData as any, initialValues }
    },
    setDeleteHost: (state, action: PayloadAction<DeleteHostType>) => {
      state.delete = action.payload
    },
    setEnterMaintain: (state, action: PayloadAction<DeviceManagement>) => {
      const { visible, formData, title, initialValues } = action.payload
      state.enterMaintain = { visible, title, formData: formData as any, initialValues }
    },
    setExitMaintain: (state, action: PayloadAction<TipsDeviceType>) => {
      state.exitMaintain = action.payload
    },
    setStartUp: (state, action: PayloadAction<startUpType>) => {
      state.startUp = action.payload
    },
    setClose: (state, action: PayloadAction<TipsDeviceType>) => {
      state.close = action.payload
    },
    setRestart: (state, action: PayloadAction<TipsDeviceType>) => {
      state.restart = action.payload
    },
  },
})

export const {
  setHostInfo,
  setDeleteHost,
  setEnterMaintain,
  setExitMaintain,
  setStartUp,
  setClose,
  setRestart,
} = hostOperateSlice.actions
export default hostOperateSlice.reducer
