import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import { AuthState } from '@redux/interface'

const authState: AuthState = {
  authButtons: {},
  authRouter: [],
}

const authSlice = createSlice({
  name: 'auth',
  initialState: authState,
  reducers: {
    setAuthButtons: (state, action: PayloadAction<{ [propName: string]: any }>) => {
      state.authButtons = action.payload
    },
    setAuthRouter: (state, action: PayloadAction<string[]>) => {
      state.authRouter = action.payload
    },
  },
})

export const { setAuthButtons, setAuthRouter } = authSlice.actions
export default authSlice.reducer
