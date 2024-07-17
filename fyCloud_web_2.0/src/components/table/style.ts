import styled from 'styled-components'
export const TableWrap = styled.div`
  & .ant-table-tbody > tr.ant-table-row:hover > td,
  .ant-table-tbody > tr > td.ant-table-cell-row-hover,
  td.ant-table-column-sort {
    background: rgb(220, 245, 255);
  }
  & .clickRowStyle {
    background-color: rgb(220, 245, 242);
  }
`
