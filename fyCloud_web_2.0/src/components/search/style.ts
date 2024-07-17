import styled from 'styled-components'

import { THEME_COLOR } from '@common/theme'
export const SearchWrap = styled.div`
  & .ant-btn-primary {
    background: ${THEME_COLOR};
    border-color: ${THEME_COLOR};
  }

  & .ant-btn {
    font-size: 20px;
  }
`
