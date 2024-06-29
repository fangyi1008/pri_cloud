import { configureStore, combineReducers } from '@reduxjs/toolkit'
import { TypedUseSelectorHook, useDispatch, useSelector } from 'react-redux'
import { persistStore, persistReducer } from 'redux-persist'
import storage from 'redux-persist/lib/storage'
import global from '@redux/modules/global/global'
import menu from '@redux/modules/menu/menu'
import tabs from '@redux/modules/tabs/tabs'
import breadcrumb from '@redux/modules/breadcrumb/breadcrumb'
import auth from '@redux/modules/auth/auth'
// import deviceManagement from '@redux/modules/deviceManagement/index'
import clusterOperate from '@redux/modules/deviceManagement/clusterOperate'
import dataCenterOperate from '@redux/modules/deviceManagement/dataCenterOperate'
import hostOperate from '@redux/modules/deviceManagement/hostOperate'
import storagePoolOperate from '@redux/modules/deviceManagement/storagePool'
import storageVolumeOperate from '@redux/modules/deviceManagement/storageVolume'
import groupOperate from '@redux/modules/deviceManagement/securityGroup'
import ruleOperate from '@redux/modules/deviceManagement/securityRule'
import vmOperate from '@redux/modules/deviceManagement/vmOperate'
import dataCenterListRefresh from '@redux/modules/dataCenterList'
import clusterList from '@redux/modules/clusterList'
import hostList from '@redux/modules/hostList'
import hostInfo from '@redux/modules/hostInfo'
import virtualSwitchList from '@redux/modules/virtualSwitchList'
import storageList from '@redux/modules/storageList'
import recycleOperate from '@redux/modules/deviceManagement/recycleOperate'
import vmList from '@redux/modules/vmList'
import vm from '@redux/modules/vm'
import clusterInfo from '@redux/modules/clusterInfo'
import virtualSwitch from '@redux/modules/deviceManagement/virtualSwitch'
import taskBoard from '@redux/modules/taskBoard'

const persistConfig = {
  key: 'root',
  storage,
  blacklist: [
    'clusterOperate',
    'dataCenterOperate',
    'hostOperate',
    'vmOperate',
    'virtualSwitch',
    'taskBoard',
    'storageVolumeOperate',
  ],
}

const rootReducer = combineReducers({
  global,
  menu,
  tabs,
  breadcrumb,
  auth,
  clusterOperate,
  dataCenterOperate,
  dataCenterListRefresh,
  hostOperate,
  storagePoolOperate,
  storageVolumeOperate,
  recycleOperate,
  groupOperate,
  ruleOperate,
  vmOperate,
  clusterList,
  hostList,
  hostInfo,
  virtualSwitchList,
  virtualSwitch,
  storageList,
  vmList,
  vm,
  clusterInfo,
  taskBoard,
  // deviceManagement,
})

const myPersistReducer = persistReducer(persistConfig, rootReducer)

const store = configureStore({
  reducer: myPersistReducer,
  middleware: getDefaultMiddleware => getDefaultMiddleware({ serializableCheck: false }),
  devTools: true,
})

export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch

export const useAppDispatch = () => useDispatch<AppDispatch>()
export const useAppSelector: TypedUseSelectorHook<RootState> = useSelector

export const persistor = persistStore(store)
export default store
