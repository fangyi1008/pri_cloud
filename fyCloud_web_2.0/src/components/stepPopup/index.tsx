import React, { useEffect, useMemo, useRef, useState } from 'react'
import { Steps, Modal, Row, Col, Button, Form } from 'antd'
import {
  Container,
  StepsWrap,
  StepsAndFormWrap,
  StepsResultWrap,
  ResultHeadWrap,
  ResultBodyWrap,
  FormWrap,
  FormBodyWrap,
  FormButtonWrap,
} from './style'
import { FormStepPopupPropsType } from './type'
import { DetailInfo } from '../detailInfoPopup'
import MultipleFormItem from '../multipleFormItem'
import { FormInstance } from 'antd/es/form'

const { Step } = Steps

export default function StepPopup(props: FormStepPopupPropsType) {
  const {
    current = 0,
    steps = [],
    layoutType = 'horizontal',
    popupTitle = '表单',
    visible,
    isShowResult = false,
    resultTitle = '配置信息',
    width = '50%',
    labelField = 'content',
    labelNameField = 'id',
    formInitialValues = {},
    onStepChange,
    onFinish,
    onFinishFailed,
    onCancel,
    onValuesChange,
    transformResultData,
  } = props

  const formRef: React.RefObject<FormInstance> = useRef(null)
  const formsValue: React.MutableRefObject<Record<string, any>> = useRef({})
  const maxStepNumber: React.MutableRefObject<number> = useRef(0)
  const [resultData, setResultData] = useState(
    transformResultData ? transformResultData(formInitialValues) : formInitialValues,
  )

  const stepsCurrent = steps[current]
  const formData = stepsCurrent && stepsCurrent.data
  const isLastStep = current === steps.length - 1

  const resultFields = useMemo(() => {
    const index = maxStepNumber.current ?? 0
    return steps.slice(0, index + 1).reduce((preObject, stepItem) => {
      const objectData: { [key: string]: string } = preObject
      stepItem?.data?.forEach(stepData => {
        if (stepData.type === 'groupFrom') {
          stepData?.groupItems?.forEach(groupItem => {
            const { [labelField]: text, [labelNameField]: key } = groupItem
            objectData[key] = text
          })
        } else {
          const { [labelField]: text, [labelNameField]: key } = stepData
          objectData[key] = text
        }
      })
      return { ...preObject, ...objectData }
    }, {})
  }, [steps, maxStepNumber.current])

  useEffect(() => {
    formRef.current?.setFieldsValue(formInitialValues)
  }, [formInitialValues])

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

  const onStepChangeHandler = async (toStepNumber: number) => {
    let validateStatus = true
    if (toStepNumber > current) {
      validateStatus = await validateForm()
      maxStepNumber.current = toStepNumber
    }
    validateStatus && onStepChange && onStepChange(toStepNumber)
  }

  const onNextStepClick = async () => {
    const validateStatus = await validateForm()
    const nextStepNumber = current + 1
    const stepTotalNumber = steps.length - 1
    if (!validateStatus) {
      return
    }
    if (maxStepNumber.current < nextStepNumber) {
      maxStepNumber.current = nextStepNumber
    }
    if (current === stepTotalNumber) {
      onFinish && onFinish(formsValue.current)
    } else {
      onStepChange && onStepChange(nextStepNumber)
    }
  }

  const onPrevStepClick = () => {
    onStepChange && onStepChange(current - 1)
  }

  const onValuesChangeHandle = (changedValues: any, values: any) => {
    onValuesChange && onValuesChange(changedValues, values)
    if (transformResultData) {
      setResultData({ ...resultData, ...transformResultData(changedValues) })
    } else {
      setResultData({ ...resultData, ...changedValues })
    }
  }

  const refreshResultData = (data: any) => {
    // 处理数据
    setResultData({ ...resultData, ...data })
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
      <Container>
        <Row>
          <Col span={isShowResult ? 16 : 24}>
            <StepsAndFormWrap layoutType={layoutType}>
              <StepsWrap layoutType={layoutType}>
                <Steps current={current} direction={layoutType} onChange={onStepChangeHandler}>
                  {steps.map(item => (
                    <Step key={item.title} title={item.title} />
                  ))}
                </Steps>
              </StepsWrap>
              <FormWrap layoutType={layoutType}>
                <FormBodyWrap>
                  <Form
                    labelCol={{ span: 6 }}
                    wrapperCol={{ span: 18 }}
                    ref={formRef}
                    initialValues={formInitialValues}
                    onValuesChange={onValuesChangeHandle}
                  >
                    <MultipleFormItem
                      formItems={formData}
                      labelField={labelField}
                      labelNameField={labelNameField}
                      labelAlign={'left'}
                      refreshResultData={refreshResultData}
                    />
                  </Form>
                </FormBodyWrap>
                <FormButtonWrap>
                  {current !== 0 && <Button onClick={onPrevStepClick}>上一步</Button>}
                  <Button type={'primary'} style={{ marginLeft: '8px' }} onClick={onNextStepClick}>
                    {isLastStep ? '确定' : '下一步'}
                  </Button>
                </FormButtonWrap>
              </FormWrap>
            </StepsAndFormWrap>
          </Col>
          {isShowResult && (
            <Col span={8}>
              <StepsResultWrap>
                <ResultHeadWrap>{resultTitle}</ResultHeadWrap>
                <ResultBodyWrap>
                  <DetailInfo data={resultData} fieldNames={resultFields} />
                </ResultBodyWrap>
              </StepsResultWrap>
            </Col>
          )}
        </Row>
      </Container>
    </Modal>
  )
}
