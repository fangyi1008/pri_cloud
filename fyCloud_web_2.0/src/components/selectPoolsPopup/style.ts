import styled from 'styled-components'
import { THEME_COLOR } from '@common/theme'
import { getCommonScrollStyle } from '../../common/commonStyle'

export const ModalLeftWrap = styled.div`
  width: 270px;
  flex-shrink: 0;
  overflow: auto;
  padding: 10px 0 10px 10px;
  ${getCommonScrollStyle({})}
  & > div:last-child {
    margin-bottom: 0 !important;
  }
  border-right: 1px solid #f0f0f0;
`

export const ModalRightWrap = styled.div`
  flex: 1;
  overflow: hidden;
  padding: 10px 0 10px 20px;
  .ant-table-body {
    ${getCommonScrollStyle({})};
  }
`

export const OperationButton = styled.div`
  width: 26px;
  height: 26px;
  border: 1px solid #dddee0;
  display: inline-flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  &:hover {
    border-color: ${THEME_COLOR};
  }
  & > img {
    width: 20px;
  }
`

export const PoolInfoContentWrap = styled.div`
  width: 166px;
  padding-left: 10px;
  & > p {
    margin: 0;
    padding: 0;
    font-size: 12px;
    word-break: break-word;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
`

export const PoolInfoImgWrap = styled.div`
  width: 50px;
  & > img {
    width: 50px;
  }
`

export const PoolInfoWrap = styled.div`
  display: flex;
`

export const PoolOperationButton = styled.div`
  display: flex;
  align-items: center;
  justify-content: end;
  height: 30px;
  & > div {
    margin-right: 4px;
  }
  & > div:last-child {
    margin-right: 0;
  }
`

export const SearchPopupWrap = styled.div`
  display: flex;
  width: 100%;
  height: 400px;
  overflow: hidden;
`

export const PoolWrap = styled.div<{ isSelect: boolean }>`
  width: 230px;
  padding: 6px;
  border: 1px solid ${props => (props.isSelect ? THEME_COLOR : '#dddee0')};
  position: relative;
  margin-bottom: 8px;
  cursor: pointer;
  &::before {
    content: '';
    position: absolute;
    top: calc(50% - 10px);
    left: 100%;
    width: 0px;
    height: 0px;
    border-top: 10px solid transparent;
    border-bottom: 10px solid transparent;
    border-left: 10px solid ${props => (props.isSelect ? THEME_COLOR : 'transparent')};
  }
`
