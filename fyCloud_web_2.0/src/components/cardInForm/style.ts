import styled from 'styled-components'

export const Container = styled.div`
  display: flex;
  justify-content: space-around;
  overflow: auto;
  width: 100%;
  height: 500px;
  & .ant-card {
    color: rgb(67, 107, 179);
  }
  & .ant-card-hoverable:hover {
    border-color: rgb(67, 107, 179);
    color: green;
    box-shadow: 0 0 0 0;
  }
  & .ant-card-actions {
    border-top: 0 solid white;
  }
`
export const CardPart = styled.div`
  margin-right: 10px;
`
export const ContentStyle = styled.div`
  width: 200px;
  height: 40px;
`
export const InfoStyle = styled.p`
  height: 10px;
  line-height: 10px;
  font-size: 14px;
  font-weight: 500;
  padding-left: 18px;
`
export const Action = styled.span`
  width: 50px;
  margin-right: 5px;
  border: 1px solid #eeee;
`
export const Operation = styled.div`
  display: flex;
  justify-content: flex-end;
  width: 100%;
  height: 40px;
  line-height: 40px;
`
export const card = {
  width: 250,
  marginBottom: 10,
}
export const cardForm = { minWidth: '610px' }
