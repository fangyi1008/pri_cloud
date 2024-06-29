import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import {
  DeviceManagementAddOrEdit,
  BaseOperateState as GroupOperateState,
  TipsDeviceType,
} from '@redux/interface'

const groupOperateState: GroupOperateState = {
  edit: {
    visible: false,
    type: 'add',
    formData: [],
    initialValues: {},
  },
  delete: {
    visible: false,
    title: 'xxx',
    content: '',
    values: [],
  },
}

const groupOperateSlice = createSlice({
  name: 'groupOperate',
  initialState: groupOperateState,
  reducers: {
    setGroupInfo: (state, action: PayloadAction<DeviceManagementAddOrEdit>) => {
      const { visible, type, formData, initialValues } = action.payload
      state.edit = { visible, type, formData: formData as any, initialValues }
    },
    setDeleteGroup: (state, action: PayloadAction<TipsDeviceType>) => {
      state.delete = action.payload
    },
  },
})

export const { setGroupInfo, setDeleteGroup } = groupOperateSlice.actions
export default groupOperateSlice.reducer
