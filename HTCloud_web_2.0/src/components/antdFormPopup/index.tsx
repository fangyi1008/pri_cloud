import { useRef } from 'react'
import { Button, Form, FormInstance, Modal } from 'antd'
import MultipleFormItem from '../multipleFormItem'
import { AntdFormPopupPropsType } from './type'
import {
  Container,
  AntdFormPopupWrap,
  AntdFormPopupBodyWrap,
  AntdFormPopupButtonWrap,
} from './style'
import { DeleteDataCenterWrap } from './style'
import { ExclamationCircleOutlined } from '@ant-design/icons'

export default function AntdFormPopup(props: AntdFormPopupPropsType) {
  const {
    popupTitle = '表单',
    visible,
    width = '50%',
    height = '400px',
    formInitialValues = {},
    formData = [],
    labelField = 'content',
    labelNameField = 'id',
    tipContent = '',
    onFinish,
    onFinishFailed,
    onCancel,
    onValuesChange,
    labelCol = { span: 4 },
    wrapperCol = { span: 20 },
  } = props

  const formRef: React.RefObject<FormInstance> = useRef(null)
  const formsValue: React.MutableRefObject<Record<string, any>> = useRef({})

  const onValuesChangeHandle = (changedValues: any, values: any) => {
    onValuesChange && onValuesChange(changedValues, values)
  }

  const validateForm = async () => {
    try {
      const validateData = await formRef?.current?.validateFields()
      formsValue.current = { ...formsValue?.current, ...validateData }
      return true
    } catch (ex) {
      onFinishFailed && onFinishFailed(ex)
      return false
    }
  }

  const onNextStepClick = async () => {
    const validateStatus = await validateForm()
    if (!validateStatus) {
      return
    }
    onFinish && onFinish(formsValue.current)
  }

  return (
    <Modal
      destroyOnClose
      title={popupTitle}
      bodyStyle={{ padding: 0 }}
      footer={false}
      visible={visible}
      onCancel={onCancel}
      width={width}
    >
      <Container height={height}>
        <AntdFormPopupWrap>
          <AntdFormPopupBodyWrap>
            {tipContent && (
              <DeleteDataCenterWrap>
                <ExclamationCircleOutlined
                  style={{ fontSize: '38px', color: 'red', marginRight: '20px' }}
                />
                {tipContent}
              </DeleteDataCenterWrap>
            )}
            <Form
              labelCol={labelCol}
              wrapperCol={wrapperCol}
              ref={formRef}
              initialValues={formInitialValues}
              onValuesChange={onValuesChangeHandle}
            >
              <MultipleFormItem
                formItems={formData}
                labelField={labelField}
                labelNameField={labelNameField}
                labelAlign={'left'}
              />
            </Form>
          </AntdFormPopupBodyWrap>
          <AntdFormPopupButtonWrap>
            <Button onClick={onCancel}>取消</Button>
            <Button type={'primary'} style={{ marginLeft: '8px' }} onClick={onNextStepClick}>
              确定
            </Button>
          </AntdFormPopupButtonWrap>
        </AntdFormPopupWrap>
      </Container>
    </Modal>
  )
}
