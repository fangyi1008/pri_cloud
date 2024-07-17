import styled from 'styled-components'
import { DirectionType } from './type'
import { getCommonScrollStyle } from '../../common/commonStyle'
export const Container = styled.div`
  width: 100%;
  height: 400px;
  overflow: hidden;
`

export const StepsWrap = styled.div<{ layoutType: DirectionType }>`
  position: relative;
  overflow: hidden;
  padding: ${props => (props.layoutType === 'horizontal' ? '18px 20px 0px 20px' : '20px 0')};
  height: ${props => (props.layoutType === 'horizontal' ? '70px' : '100%')};
  width: ${props => (props.layoutType === 'horizontal' ? '100%' : '190px')};
  flex-shrink: 1;
  &::after {
    content: '';
    position: absolute;
    ${props => (props.layoutType === 'horizontal' ? 'left:50%;bottom:0;' : 'top:50%;right: 0;')}
    transform:${props =>
      props.layoutType === 'horizontal' ? 'translateX(-50%)' : 'translateY(-50%)'};
    width: ${props => (props.layoutType === 'horizontal' ? '100%' : '1px')};
    height: ${props => (props.layoutType === 'horizontal' ? '1px' : '95%')};
    background: #cbcad8;
  }
`
export const StepsAndFormWrap = styled.div<{ layoutType: DirectionType }>`
  height: 400px;
  overflow: hidden;
  display: flex;
  padding: 0 20px;
  flex-direction: ${props => (props.layoutType === 'horizontal' ? 'column' : 'row')};
  ${getCommonScrollStyle({})}
`
export const FormWrap = styled.div<{ layoutType: DirectionType }>`
  display: flex;
  flex: 1;
  width: 100%;
  overflow: hidden;
  flex-direction: column;
  padding-bottom: 10px;
  padding-left: ${props => (props.layoutType === 'horizontal' ? '0' : '20px')};
`
export const FormBodyWrap = styled.div`
  padding: 20px 20px 0 20px;
  flex: 1;
  overflow: auto;
  ${getCommonScrollStyle({})}
`
export const FormButtonWrap = styled.div`
  height: 40px;
  display: flex;
  flex-shrink: 1;
  align-items: end;
  border-top: 1px solid #d4d9dd;
  justify-content: end;
`

export const StepsResultWrap = styled.div`
  position: relative;
  display: flex;
  height: 400px;
  overflow: hidden;
  border-left: 1px solid #d4d9dd;
  background: #ebebeb;
`

export const ResultHeadWrap = styled.div`
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  display: flex;
  align-items: center;
  height: 42px;
  font-size: 14px;
  font-weight: bold;
  background: #ebebeb;
  padding-left: 10px;
`

export const ResultBodyWrap = styled.div`
  flex: 1;
  padding-top: 42px;
  overflow: auto;
  ${getCommonScrollStyle({})}
`
