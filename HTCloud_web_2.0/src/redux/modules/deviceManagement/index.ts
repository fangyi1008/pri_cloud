import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import { DeviceManagementAddOrEdit, DeviceManagementState } from '@redux/interface'

const deviceManagementState: DeviceManagementState = {
  cluster: {
    visible: false,
    type: 'add',
    formData: [],
    initialValues: {},
  },
  dataCenter: {
    visible: false,
    type: 'add',
    formData: [],
    initialValues: {},
  },
  host: {
    visible: false,
    type: 'add',
    formData: [],
    initialValues: {},
  },
  vm: {
    visible: false,
    type: 'add',
    formData: [],
    initialValues: {},
  },
}

const deviceManagementSlice = createSlice({
  name: 'deviceManagement',
  initialState: deviceManagementState,
  reducers: {
    setClusterInfo: (state, action: PayloadAction<DeviceManagementAddOrEdit>) => {
      const { visible, type, formData, initialValues } = action.payload
      state.cluster = { visible, type, formData: formData as any, initialValues }
    },
    setDataCenterInfo: (state, action: PayloadAction<DeviceManagementAddOrEdit>) => {
      const { visible, type, formData, initialValues } = action.payload
      state.dataCenter = { visible, type, formData: formData as any, initialValues }
    },
    setHostInfo: (state, action: PayloadAction<DeviceManagementAddOrEdit>) => {
      const { visible, type, formData, initialValues } = action.payload
      state.host = { visible, type, formData: formData as any, initialValues }
    },
    setVmInfo: (state, action: PayloadAction<DeviceManagementAddOrEdit>) => {
      const { visible, type, formData, initialValues } = action.payload
      state.vm = { visible, type, formData: formData as any, initialValues }
    },
  },
})

export const { setClusterInfo, setDataCenterInfo, setHostInfo, setVmInfo } =
  deviceManagementSlice.actions
export default deviceManagementSlice.reducer
