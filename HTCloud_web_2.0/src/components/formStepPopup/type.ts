import { FormDataType } from '../multipleForm/type'

export type BasicObj = { [key: string]: any }

export interface FormStepPopupPropsType extends StepButtonProps {
  steps: StepsType[]
  layoutType?: DirectionType
  title: string
  visible: boolean
  onCancel?: () => void
  isShowResult?: boolean
  showResultsData?: BasicObj
  onChange: (value: BasicObj) => void
  formValue?: BasicObj
}
export type StepsType = {
  key: string
  title: string
  data: FormDataType[]
  render?: (current: number) => JSX.Element
}
export type DirectionType = 'horizontal' | 'vertical'
export interface StepButtonProps {
  onPrev?: () => void
  onNext?: () => void
  current: number
  onFinish?: () => void
  steps: StepsType[]
}
