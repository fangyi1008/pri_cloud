import { createSlice, PayloadAction, createAsyncThunk } from '@reduxjs/toolkit'
import { Cluster, Host } from '@/api/interface/index'
import { clusterDataInfo } from '@/api/modules/cluster'

const clusterState: { clusterInfo?: Cluster.IClusterType; hostInfo?: Host.IHostType } = {
  clusterInfo: undefined,
  hostInfo: undefined,
}

export const reduxGetClusterInfo = createAsyncThunk(
  'cluster/getClusterInfo',
  async (clusterId: string) => {
    return await clusterDataInfo(clusterId)
  },
)

const clusterSlice = createSlice({
  name: 'vm',
  initialState: clusterState,
  reducers: {},
  extraReducers: builder => {
    builder.addCase(reduxGetClusterInfo.fulfilled, (state, { payload }) => {
      const { cluster, host } = payload
      state.clusterInfo = cluster
      state.hostInfo = host
    })
  },
})

export default clusterSlice.reducer
