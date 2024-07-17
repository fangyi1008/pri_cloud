import { FormDataType } from '../multipleFormItem/type'

export interface AntdFormPopupPropsType {
  popupTitle: string
  visible: boolean
  formValue?: Record<string, any>
  labelField?: string
  labelNameField?: string
  resultTitle?: string
  formData?: FormDataType[]
  width?: string
  height?: string
  tipContent?: string
  formInitialValues?: Record<string, any>
  resultData?: Record<string, any>
  labelCol?: { span: number }
  wrapperCol?: { span: number }
  onFinish?: (value: Record<string, any>) => void
  onCancel?: () => void
  onFinishFailed?: (data: any) => void
  onValuesChange?: (changedValues: any, values: any) => void
}
