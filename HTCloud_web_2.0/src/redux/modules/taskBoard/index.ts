import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import { ITaskBoardState } from '@/redux/interface'
import { ITaskType } from '@/api/interface'

const taskBoardState: ITaskBoardState = {
  open: false,
  list: [],
}

export const taskBoardSlice = createSlice({
  name: 'taskBoard',
  initialState: taskBoardState,
  reducers: {
    openTaskBoard: state => {
      state.open = true
    },
    closeTaskBoard: state => {
      state.open = false
    },
    setTaskList: (state, action: PayloadAction<ITaskType[]>) => {
      const { payload } = action
      state.list = [...payload, ...state.list]
    },
  },
})

export const { openTaskBoard, closeTaskBoard, setTaskList } = taskBoardSlice.actions

export default taskBoardSlice.reducer
