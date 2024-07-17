import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import {
  DeviceManagement,
  DeviceManagementAddOrEdit,
  DownloadType,
  loadingType,
  volumeOperateState,
} from '@redux/interface'

const volumeOperate: volumeOperateState = {
  edit: {
    visible: false,
    type: 'add',
    formData: [],
    initialValues: {},
  },
  delete: {
    visible: false,
    title: '删除',
    formData: [],
    initialValues: {},
  },
  upload: {
    title: '上传',
    visible: false,
    formData: [],
    initialValues: {},
  },
  download: {
    visible: false,
    title: '下载',
    content: '您确认要下载该存储卷吗？',
    values: { storageId: '', storageVolumeName: '', filesystem: '' },
  },
  downloadStorageVolume: { isloading: false, percent: 0 },
}

const volumeOperateSlice = createSlice({
  name: 'volumeOperate',
  initialState: volumeOperate,
  reducers: {
    setVolumeInfo: (state, action: PayloadAction<DeviceManagementAddOrEdit>) => {
      const { visible, type, formData, initialValues } = action.payload
      state.edit = { visible, type, formData: formData as any, initialValues }
    },
    setDeleteVolume: (state, action: PayloadAction<DeviceManagement>) => {
      const { visible, title, formData, initialValues } = action.payload
      state.delete = { visible, title, formData: formData as any, initialValues }
    },
    setUploadVolume: (state, action: PayloadAction<DeviceManagement>) => {
      const { visible, title, formData, initialValues } = action.payload
      state.upload = { visible, title, formData: formData as any, initialValues }
    },
    setDownloadVolume: (state, action: PayloadAction<DownloadType>) => {
      state.download = action.payload
    },
    setLoading: (state, action: PayloadAction<loadingType>) => {
      state.downloadStorageVolume = action.payload
    },
  },
})

export const { setVolumeInfo, setDeleteVolume, setUploadVolume, setDownloadVolume, setLoading } =
  volumeOperateSlice.actions
export default volumeOperateSlice.reducer
