import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import {
  DeviceManagementAddOrEdit,
  BaseOperateState as RuleOperateState,
  TipsDeviceType,
} from '@redux/interface'

const ruleOperateState: RuleOperateState = {
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

const ruleOperateSlice = createSlice({
  name: 'ruleOperate',
  initialState: ruleOperateState,
  reducers: {
    setRuleInfo: (state, action: PayloadAction<DeviceManagementAddOrEdit>) => {
      const { visible, type, formData, initialValues } = action.payload
      state.edit = { visible, type, formData: formData as any, initialValues }
    },
    setDeleteRule: (state, action: PayloadAction<TipsDeviceType>) => {
      state.delete = action.payload
    },
  },
})

export const { setRuleInfo, setDeleteRule } = ruleOperateSlice.actions
export default ruleOperateSlice.reducer
