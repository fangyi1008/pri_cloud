import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import { BreadcrumbState } from '@redux/interface'

const breadcrumbState: BreadcrumbState = {
  breadcrumbList: {},
}

const breadcrumbSlice = createSlice({
  name: 'breadcrumb',
  initialState: breadcrumbState,
  reducers: {
    setBreadcrumbList: (state, action: PayloadAction<{ [propName: string]: any }>) => {
      state.breadcrumbList = action.payload
    },
  },
})

export const { setBreadcrumbList } = breadcrumbSlice.actions
export default breadcrumbSlice.reducer
