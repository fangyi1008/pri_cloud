import styled from 'styled-components'
import { DirectionType } from './type'
export const Container = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: row;
`
export const StepsContainer = styled.div<{
  isShowResult: boolean
  layoutType: DirectionType
}>`
  width: ${(props: { isShowResult: boolean }) => (props.isShowResult ? '60%' : '100%')};
  height: 100%;
  display: flex;
  flex-direction: ${(props: { layoutType: DirectionType }) =>
    props.layoutType === 'horizontal' ? 'column' : 'row'};
`

export const StepsWrap = styled.div`
  width: ${(props: { layoutType: DirectionType }) =>
    props.layoutType === 'vertical' ? '25%' : '100%'};
  margin: 20px 10px 10px 10px;
`
export const Content = styled.div`
  width: 100%;
  height: 300px;
  max-height: 400px;
  overflow-y: auto;
`
export const ShowResultWrap = styled.div`
  width: ${(props: { isShowResult: boolean }) => (props.isShowResult ? '39%' : '')};
  max-height: 400px;
  overflow-y: auto;
  margin: 20px 0px 5px 10px;
  border-left: 1px solid #e8e8e8;
`
