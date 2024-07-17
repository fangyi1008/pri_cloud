import styled from 'styled-components'
import { CheckBoxButtonWrapType, DirectionType } from './type'
import { THEME_COLOR } from '@common/theme'

export const SelectBoxGroupWrap = styled.div<{ direction: DirectionType }>`
  display: flex;
  flex-direction: ${props => {
    return props.direction || 'row'
  }};
`

export const DescribeWrap = styled.p`
  display: block;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  font-size: 12px;
  margin-top: 6px;
  margin-bottom: 0;
  cursor: pointer;
`

export const SelectBoxItemBox = styled.div<{
  direction?: DirectionType
  space?: number
}>`
  ${props =>
    props.direction === 'row'
      ? `margin-right:${props.space}px;`
      : `margin-bottom:${props.space}px;`}
`

export const SelectBoxWrap = styled.div<CheckBoxButtonWrapType>`
  position: relative;
  display: inline-flex;
  justify-content: center;
  align-items: center;
  height: ${props => props.height || 30}px;
  width: ${props => props.width || 120}px;
  border: ${props => (props.isChecked ? `1px solid ${THEME_COLOR}` : '1px solid #dddee0')};
  user-select: none;
  cursor: pointer;
  padding: 4px 6px;
  & > img {
    height: 100%;
  }
`
export const SelectStatus = styled.div`
  position: absolute;
  right: 0;
  top: 0;
  border-bottom: 0 solid transparent;
  border-left: 0 solid transparent;
  border-top: 0 solid transparent;
  border-right-color: #196d85;
  border-style: solid;
  border-width: 0 24px 24px 0;
  height: 0;
  width: 0;
`
export const SelectIconWrap = styled.div`
  position: absolute;
  top: -4px;
  right: -22px;
  width: 14px;
`
