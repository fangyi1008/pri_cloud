import { Host } from '@/api/interface'
import { hostDataInfo } from '@/api/modules/host'
import { createAsyncThunk, createSlice, PayloadAction } from '@reduxjs/toolkit'

const hostInfoState: { info: Host.IHostType; nodeInfo: any } = {
  info: {
    bmcIp: '',
    clusterId: '',
    cpuType: '',
    createTime: '',
    createUserId: '',
    dataCenterId: '',
    hostId: '',
    hostName: '',
    hostPassword: '',
    hostUser: '',
    osIp: '',
    state: '5',
    virtualVersion: '',
    clusterName: '',
    createUserName: '',
    dataCenterName: '',
  },
  nodeInfo: undefined,
}

export const reduxGetHostInfo = createAsyncThunk('host/getHostInfo', async (hostId: string) => {
  const res = await hostDataInfo(hostId)
  return res?.host && res?.nodeInfo ? { info: res.host, nodeInfo: res.nodeInfo } : hostInfoState
})

const hostSlice = createSlice({
  name: 'host',
  initialState: hostInfoState,
  reducers: {},
  extraReducers: builder => {
    builder.addCase(reduxGetHostInfo.fulfilled, (state, { payload }) => {
      const { info, nodeInfo } = payload
      state.info = info || hostInfoState.info
      state.nodeInfo = nodeInfo || hostInfoState.nodeInfo
    })
  },
})

export default hostSlice.reducer

// const hostInfoSlice = createSlice({
//   name: 'hostInfo',
//   initialState: hostInfoState,
//   reducers: {
//     saveHostInfo: (state, action: PayloadAction<Host.IHostType>) => {
//       state.info = action.payload
//     },
//   },
// })

// export const { saveHostInfo } = hostInfoSlice.actions
// export default hostInfoSlice.reducer
