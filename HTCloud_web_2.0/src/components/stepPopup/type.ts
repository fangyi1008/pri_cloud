import { FormDataType } from '../multipleFormItem/type'

export type BasicObj = { [key: string]: any }

export interface FormStepPopupPropsType {
  steps: StepsType[]
  layoutType?: DirectionType
  popupTitle: string
  visible: boolean
  isShowResult?: boolean
  showResultsData?: BasicObj
  formValue?: BasicObj
  resultTitle?: string
  width?: string
  current: number
  labelField?: string
  labelNameField?: string
  formInitialValues?: Record<string, any>
  resultData?: Record<string, any>
  onFinish?: (value: Record<string, any>) => void
  onStepChange?: (current: number) => void
  onCancel?: () => void
  onFinishFailed?: (data: any) => void
  onValuesChange?: (changedValues: any, values: any) => void
  transformResultData?: (data: Record<string, any>) => Record<string, any>
}
export type StepsType = {
  key: string
  title: string
  data: FormDataType[]
  render?: (current: number) => JSX.Element
}
export type DirectionType = 'horizontal' | 'vertical'
