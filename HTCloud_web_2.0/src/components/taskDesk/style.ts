import styled from 'styled-components'

import { THEME_COLOR, THEME_COLOR_HOVER, THEME_COLOR_FOCUS } from '@common/theme'

export const TaskDeskWrap = styled.div`
  width: 100%;
  background: #ffffff;
  box-shadow: 0px 2px 8px 0px rgba(0, 0, 0, 0.15);
  position: relative;
  display: ${(props: { visible: boolean }) => (props.visible ? '' : 'none')};
`
export const DeskHeader = styled.div`
  width: 100%;
  height: 40px;
  background: ${THEME_COLOR};
  display: flex;
  align-items: center;
`
export const HeaderTitle = styled.div`
  color: #ffffff;
  padding: 20px;
`

export const IconWrap = styled.div`
  width: 15px;
  height: 20px;
  cursor: pointer;
  position: absolute;
  top: 10px;
  right: 10px;
  color: #ffffff;
  & :hover {
    color: ${THEME_COLOR_FOCUS};
  }
`
export const TableWrap = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  & .ant-table-thead > tr > th {
    background: ${THEME_COLOR_HOVER};
    color: #ffffff;
  }
`
