import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import {
  DeviceManagementAddOrEdit,
  BaseOperateState as DataCenterOperateState,
  TipsDeviceType,
} from '@redux/interface'

const dataCenterOperateState: DataCenterOperateState = {
  edit: {
    visible: false,
    type: 'add',
    formData: [],
    initialValues: {},
  },
  delete: {
    visible: false,
    title: '删除数据中心',
    content: '您确定要删除该数据中心吗?',
    values: [],
  },
}

const dataCenterOperateSlice = createSlice({
  name: 'dataCenterOperateState',
  initialState: dataCenterOperateState,
  reducers: {
    setDataCenterInfo: (state, action: PayloadAction<DeviceManagementAddOrEdit>) => {
      const { visible, type, formData, initialValues } = action.payload
      state.edit = { visible, type, formData: formData as any, initialValues }
    },
    setDeleteDataCenter: (state, action: PayloadAction<TipsDeviceType>) => {
      state.delete = action.payload
    },
  },
})

export const { setDataCenterInfo, setDeleteDataCenter } = dataCenterOperateSlice.actions
export default dataCenterOperateSlice.reducer
