import styled from 'styled-components'

export const SliceUploadWrap = styled.div`
  user-select: none;
  position: fixed;
  z-index: 20;
  right: 15px;
  bottom: 15px;
  box-sizing: border-box;
  width: 520px;
`

export const HiddenButtonWrap = styled.label`
  position: absolute;
  clip: rect(0, 0, 0, 0);
`

export const ListPanlWrap = styled.div<{ openStatus: boolean }>`
  display: ${props => (props.openStatus ? 'block' : 'none')};
`
