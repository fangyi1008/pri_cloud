import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import {
  VirtualMachineAddOrEdit,
  TipsDeviceType,
  TipsDeleteDeviceType,
  VmOperateState,
  IMoveVirtualMachine,
} from '@redux/interface'

const hostOperateState: VmOperateState = {
  edit: {
    visible: false,
    type: 'add',
    formData: [],
    initialValues: {},
  },
  delete: {
    visible: false,
    title: '删除虚拟机',
    content: '您确定要删除该虚拟机吗?',
    formData: [],
    values: {
      ids: [],
    },
  },
  startUp: {
    visible: false,
    title: '启动',
    content: '是否启动虚拟机?',
    values: [],
  },
  close: {
    visible: false,
    title: '关闭',
    content: '是否关闭虚拟机?',
    values: [],
  },
  restart: {
    visible: false,
    title: '重启',
    content: '是否重启虚拟机?',
    values: [],
  },
  destroy: {
    visible: false,
    title: '关闭电源',
    content: '是否关闭虚拟机电源?',
    values: [],
  },
  move: { visible: false, title: '迁移虚拟机', formData: [], initialValues: {} },
  suspend: {
    visible: false,
    title: '暂停',
    content: '是否暂停虚拟机?',
    values: [],
  },
  resume: {
    visible: false,
    title: '恢复',
    content: '是否恢复虚拟机?',
    values: [],
  },
}

const hostOperateSlice = createSlice({
  name: 'hostOperateState',
  initialState: hostOperateState,
  reducers: {
    setVmInfo: (state, action: PayloadAction<VirtualMachineAddOrEdit>) => {
      const { visible, type, formData, initialValues } = action.payload
      state.edit = { visible, type, formData: formData as any, initialValues }
    },
    setDeleteVm: (state, action: PayloadAction<TipsDeleteDeviceType>) => {
      const { visible, title, formData, content, values } = action.payload
      state.delete = { visible, title, formData: formData as any, content, values }
    },
    setStartUpVm: (state, action: PayloadAction<TipsDeviceType>) => {
      state.startUp = action.payload
    },
    setCloseVm: (state, action: PayloadAction<TipsDeviceType>) => {
      state.close = action.payload
    },
    setRestartVm: (state, action: PayloadAction<TipsDeviceType>) => {
      state.restart = action.payload
    },
    setDestroyVm: (state, action: PayloadAction<TipsDeviceType>) => {
      state.destroy = action.payload
    },
    setMoveVm: (state, action: PayloadAction<IMoveVirtualMachine>) => {
      const { visible, formData, title, initialValues } = action.payload
      state.move = { visible, title, formData: formData as any, initialValues }
    },
    setSuspendVm: (state, action: PayloadAction<TipsDeviceType>) => {
      state.suspend = action.payload
    },
    setResumeVm: (state, action: PayloadAction<TipsDeviceType>) => {
      state.resume = action.payload
    },
  },
})

export const {
  setVmInfo,
  setDeleteVm,
  setStartUpVm,
  setCloseVm,
  setRestartVm,
  setDestroyVm,
  setMoveVm,
  setSuspendVm,
  setResumeVm,
} = hostOperateSlice.actions
export default hostOperateSlice.reducer
