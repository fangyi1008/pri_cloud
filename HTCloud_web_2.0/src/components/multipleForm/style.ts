import styled from 'styled-components'
import { THEME_COLOR_ACTIVE, THEME_COLOR, THEME_COLOR_HOVER } from '@common/theme'

export const TitleWrapper = styled.div`
  height: 40px;
  line-height: 40px;
  border-bottom: 1px solid #e8e8e8;
  font-size: 14px;
  color: #50575d;
  font-weight: 600;
  padding-left: 18px;
`
export const ItemWrapper = styled.div`
  margin: 20px;
  padding: 0 22px 0 18px;
  display: flex;
  align-items: center;
`
export const ItemTitle = styled.div`
  flex-shrink: 0;
  width: 100px;
  line-height: 32px;
  margin-right: 14px;
  font-size: 14px;
  color: #50575d;
  font-weight: 400;
`
export const FooterWrapper = styled.div`
  height: 84px;
  padding-right: 15px;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  border-top: 1px solid #e8e8e8;
`

const CommonButtonStyle = styled.div`
  width: ${(props: { width?: string }) => (props.width ? props.width : '70px')};
  height: 30px;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.15);
  cursor: pointer;
  padding: 0 4px;
  margin-left: 10px;
`
export const ButtonCancelSpan = styled(CommonButtonStyle)`
  border: 1px solid ${THEME_COLOR};
  color: ${THEME_COLOR};
  &:hover {
    border: 1px solid ${THEME_COLOR_HOVER};
    color: ${THEME_COLOR_HOVER};
  }
  &:active {
    border: 1px solid ${THEME_COLOR_ACTIVE};
    color: ${THEME_COLOR_ACTIVE};
  }
`

export const ButtonConfirmSpan = styled(CommonButtonStyle)`
  border: 1px solid ${THEME_COLOR};
  background: ${THEME_COLOR};
  color: #ffffff;
  &:hover {
    background-color: ${THEME_COLOR_HOVER};
    color: #ffffff;
  }
  &:active {
    background-color: ${THEME_COLOR_ACTIVE};
    color: #ffffff;
  }
`
export const RequiredWrap = styled.span`
  color: #dc2e2e;
`
export const TitleStyle = styled.div`
  margin: 10px 30px;
  color: rgba(0, 0, 0, 0.85);
  font-size: 16px;
  &:hover {
    background-color: white;
    color: skyblue;
  }
`
