import styled from 'styled-components'
import { THEME_COLOR_ACTIVE, THEME_COLOR_FOCUS, THEME_COLOR_HOVER } from '@common/theme'

const setStyleByDisabled = ({ disabled }: { disabled: boolean }) => {
  return {
    opacity: disabled ? 0.5 : 1,
    cursor: disabled ? 'not-allowed' : 'pointer',
    'pointer-events': disabled ? 'none' : 'auto',
  }
}
export const Wrap = styled.div`
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
`

export const ButtonWrap = styled.div`
  display: flex;
  flex-direction: row;
  margin: 4px 4px 4px 0;
  ${setStyleByDisabled};
`
export const ButtonSpan = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  height: 30px;
  box-shadow: 0px 2px 8px 0px rgba(0, 0, 0, 0.15);
  border: 1px solid ${THEME_COLOR_HOVER};
  color: gray;
  // cursor: pointer;
  &:hover {
    background-color: ${THEME_COLOR_HOVER};
    color: white;
  }
  &:active {
    background-color: ${THEME_COLOR_FOCUS};
    color: white;
  }
  padding: 0 4px;
`

export const IconWrap = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 30px;
  height: 30px;
  border-right: 1px solid ${THEME_COLOR_HOVER};
  border-top: 1px solid ${THEME_COLOR_HOVER};
  border-bottom: 1px solid ${THEME_COLOR_HOVER};
  color: gray;
  &:hover {
    background-color: ${THEME_COLOR_HOVER};
    color: white;
  }
  &:active {
    background-color: ${THEME_COLOR_ACTIVE};
    color: white;
  }
`
