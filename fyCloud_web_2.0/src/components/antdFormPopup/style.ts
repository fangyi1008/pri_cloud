import styled from 'styled-components'
import { getCommonScrollStyle } from '../../common/commonStyle'

export const Container = styled.div<{ height: string }>`
  width: 100%;
  max-height: ${props => props.height};
  overflow: hidden;
  display: flex;
`

export const AntdFormPopupWrap = styled.div`
  display: flex;
  flex: 1;
  width: 100%;
  overflow: hidden;
  flex-direction: column;
  padding-bottom: 10px;
`

export const AntdFormPopupBodyWrap = styled.div`
  padding: 20px 40px 0 40px;
  flex: 1;
  overflow: auto;
  ${getCommonScrollStyle({})}
`

export const AntdFormPopupButtonWrap = styled.div`
  height: 40px;
  display: flex;
  flex-shrink: 1;
  align-items: center;
  border-top: 1px solid #d4d9dd;
  padding-top: 10px;
  box-sizing: border-box;
  justify-content: flex-end;
  padding-right: 40px;
`

export const DeleteDataCenterWrap = styled.div`
  display: flex;
  align-items: center;
  font-size: 20px;
  margin-bottom: 20px;
`
