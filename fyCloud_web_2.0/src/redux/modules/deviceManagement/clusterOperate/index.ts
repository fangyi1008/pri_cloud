import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import { DeviceManagementAddOrEdit, ClusterOperateState, DeleteClusterType } from '@redux/interface'

const clusterOperateState: ClusterOperateState = {
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
    values: { dataCenterId: '', ids: [] },
  },
}

const clusterOperateSlice = createSlice({
  name: 'clusterOperate',
  initialState: clusterOperateState,
  reducers: {
    setClusterInfo: (state, action: PayloadAction<DeviceManagementAddOrEdit>) => {
      const { visible, type, formData, initialValues } = action.payload
      state.edit = { visible, type, formData: formData as any, initialValues }
    },
    setDeleteCluster: (state, action: PayloadAction<DeleteClusterType>) => {
      state.delete = action.payload
    },
  },
})

export const { setClusterInfo, setDeleteCluster } = clusterOperateSlice.actions
export default clusterOperateSlice.reducer
