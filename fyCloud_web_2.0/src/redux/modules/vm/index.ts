import { createSlice, createAsyncThunk } from '@reduxjs/toolkit'
import { Vm } from '@/api/interface/index'
import { getVMInfo } from '@/api/modules/virtualMachineService'

const vmState: { info: Vm.IVmType } = {
  info: {
    vmId: '',
    vmName: '',
    hostId: '',
    osIp: '',
    vmMark: '',
    clusterId: '',
    dataCenterId: '',
    state: '',
    storageVolumeId: '',
    createTime: '',
    createUserId: '',
    vmOs: '',
    clusterName: '',
    createUserName: '',
    dataCenterName: '',
    hostName: '',
  },
}

export const reduxGetVmInfo = createAsyncThunk('virtualSwitch/getVmInfo', async (vmId: string) => {
  const res = await getVMInfo(vmId)
  return res?.vm
})

const vmSlice = createSlice({
  name: 'vm',
  initialState: vmState,
  reducers: {},
  extraReducers: builder => {
    builder.addCase(reduxGetVmInfo.fulfilled, (state, { payload }) => {
      state.info = payload || vmState.info
    })
  },
})

export default vmSlice.reducer
