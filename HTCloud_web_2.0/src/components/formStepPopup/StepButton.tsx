import React from 'react'
import { StepButtonProps } from './type'

import { ButtonConfirmSpan, FooterWrapper, ButtonCancelSpan } from '../multipleForm/style'

export default function StepButton(props: StepButtonProps) {
  const { onPrev, onNext, onFinish, current = 0, steps = [] } = props
  return (
    <FooterWrapper>
      {current > 0 && (
        <ButtonCancelSpan onClick={() => onPrev && onPrev()}>上一步</ButtonCancelSpan>
      )}
      {current < steps.length - 1 && (
        <ButtonConfirmSpan onClick={() => onNext && onNext()}>下一步</ButtonConfirmSpan>
      )}
      {current === steps.length - 1 && (
        <ButtonConfirmSpan onClick={() => onFinish && onFinish()}>完成</ButtonConfirmSpan>
      )}
    </FooterWrapper>
  )
}
