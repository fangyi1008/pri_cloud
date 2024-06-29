import styled from 'styled-components'
import { getCommonScrollStyle } from '../../common/commonStyle'

export const DetailInfoPopupWrap = styled.div`
  width: 100%;
  height: 100%;
  padding: 24px;
  overflow: auto;
  ${getCommonScrollStyle({})}
`

export const DetailInfoPopupScrollWrap = styled.div`
  display: flex;
  width: 100%;
  flex-direction: column;
  border: 1px solid #dedede;
`

export const DetailInfoPopupRow = styled.div<{ backgroundColor: string }>`
  display: flex;
  padding: 4px 0;
  background-color: ${props => props.backgroundColor || 'transparent'};
`

export const DetailInfoPopupRowTitle = styled.div<{ color: string }>`
  display: flex;
  align-items: center;
  width: 100px;
  flex-shrink: 1;
  padding-left: 10px;
`

export const DetailInfoPopupRowContent = styled.div<{ color: string }>`
  display: flex;
  flex: 1;
  align-items: center;
  color: ${props => props.color || '#000'};
  word-break: break-all;
`
