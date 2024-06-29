export type BasicObj = { [key: string]: any }

export interface BasicCardDataType extends BasicObj {
  key: string
  label: string
  type: 'text' | 'progress' | 'custom'
  value: any
}

export interface ShowInfoCardPropsType {
  basicData?: BasicCardDataType[]
  value?: BasicObj
  cardTitle: string | JSX.Element
  type?: 'basic' | 'dashboard' | 'custom'
  customCard?: string | JSX.Element
  width?: number
  height?: number
  dashboardData?: DashboardDataType[]
}
export interface DashboardDataType {
  key: string
  width?: number
  percent: number
  strokeColor?: { [key: string]: string }
  info?: InfoType[]
}
export interface InfoType {
  key: string
  label: string
  value: string
}
