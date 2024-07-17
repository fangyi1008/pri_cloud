import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import {
  VirtualMachineAddOrEdit,
  TipsDeviceType,
  IVirtualSwitchOperateState,
} from '@redux/interface'

const virtualSwitchOperateState: IVirtualSwitchOperateState = {
  edit: {
    visible: false,
    type: 'add',
    formData: [],
    initialValues: {},
  },
  delete: {
    visible: false,
    title: '删除虚拟交换机',
    content: '您确定要删除虚拟交换机吗?',
    values: [],
  },
}

const virtualSwitchOperateSlice = createSlice({
  name: 'virtualSwitchOperate',
  initialState: virtualSwitchOperateState,
  reducers: {
    setVirtualSwitchInfo: (state, action: PayloadAction<VirtualMachineAddOrEdit>) => {
      const { visible, type, formData, initialValues } = action.payload
      state.edit = { visible, type, formData: formData as any, initialValues }
    },
    setVirtualSwitchDelete: (state, action: PayloadAction<TipsDeviceType>) => {
      state.delete = action.payload
    },
  },
})

export const { setVirtualSwitchInfo, setVirtualSwitchDelete } = virtualSwitchOperateSlice.actions
export default virtualSwitchOperateSlice.reducer
