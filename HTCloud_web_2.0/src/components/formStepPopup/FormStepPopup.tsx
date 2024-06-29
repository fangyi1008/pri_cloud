import React, { useEffect } from 'react'
import { Steps, Modal } from 'antd'
import { Container, StepsContainer, StepsWrap, Content, ShowResultWrap } from './style'
import { DirectionType, FormStepPopupPropsType } from './type'
import { ItemWrapper, ItemTitle } from '../multipleForm/style'
import { TitleWrapper } from '../formPopop/style'
import FormContent from '../multipleForm'
import { isNotEmptyArray } from '@common/utils'
import FooterStepButton from './StepButton'

const { Step } = Steps
export default function FormStepPopup(props: FormStepPopupPropsType) {
  const {
    onPrev,
    onNext,
    onFinish,
    current = 0,
    steps = [],
    layoutType = 'horizontal',
    title = '',
    onCancel,
    visible,
    showResultsData = {},
    isShowResult = false,
    onChange,
    formValue,
  } = props
  const getStep = (direction: DirectionType) => {
    const type = layoutType === 'horizontal' ? 'navigation' : 'default'
    return (
      <StepsWrap layoutType={layoutType}>
        <Steps current={current} type={type} direction={direction}>
          {steps.map(item => (
            <Step key={item.title} title={item.title} />
          ))}
        </Steps>
      </StepsWrap>
    )
  }

  const getValue = (key: string) => {
    if (!showResultsData || showResultsData.length === 0) {
      return ''
    }
    const value = showResultsData[key] && showResultsData[key]
    if (typeof value === 'string') {
      return value
    } else if (value && Array.isArray(value)) {
      return value.join(',')
    } else {
      return ''
    }
  }

  const getShowResults = () => {
    const showResults: { key: string; title: string; value: string }[] = []
    isNotEmptyArray(steps) &&
      steps.forEach(item => {
        const { data } = item
        isNotEmptyArray(data) &&
          data.forEach(({ key, title }) => {
            showResults.push({
              key,
              title,
              value: getValue(key),
            })
          })
      })
    return (
      isNotEmptyArray(showResults) &&
      showResults.map(item => {
        const { key, title, value } = item
        return (
          <ItemWrapper key={key}>
            {title ? <ItemTitle>{title}</ItemTitle> : null}
            {value}
          </ItemWrapper>
        )
      })
    )
  }

  const modalWidth = isShowResult ? '60%' : '50%'
  const stepsCurrent = steps[current]
  const formContentData = stepsCurrent && stepsCurrent.data
  return (
    <Modal footer={false} visible={visible} onCancel={onCancel} width={modalWidth}>
      <Container>
        <StepsContainer layoutType={layoutType} isShowResult={isShowResult}>
          {layoutType === 'horizontal' ? getStep('horizontal') : getStep('vertical')}
          <div style={{ width: '100%' }}>
            <TitleWrapper>{title}</TitleWrapper>
            {stepsCurrent && stepsCurrent.render ? (
              <Content>{stepsCurrent.render(current)}</Content>
            ) : (
              <Content>
                <FormContent data={formContentData} onChange={onChange} value={formValue} />
              </Content>
            )}

            <FooterStepButton
              onPrev={onPrev}
              onNext={onNext}
              onFinish={onFinish}
              current={current}
              steps={steps}
            />
          </div>
        </StepsContainer>
        {isShowResult ? (
          <ShowResultWrap isShowResult={isShowResult}>{getShowResults()}</ShowResultWrap>
        ) : null}
      </Container>
    </Modal>
  )
}
