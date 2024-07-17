import styled from 'styled-components'
import { THEME_COLOR } from '@common/theme'
import { getWidthOrHeight } from '@common/utils'

export const Container = styled.div<{
  width?: number | string
  height?: number | string
}>`
  width: ${props => getWidthOrHeight(props.width)};
  height: ${props => getWidthOrHeight(props.height)};
  min-height: 100px;
  padding: 10px;
  border: 1px solid #e8e8e8;
  font-size: 14px;
  color: #50575d;
  font-weight: 600;
`
export const CardTitle = styled.div`
  margin: 0px 5px 10px 10px;
  color: ${THEME_COLOR};
  font-size: 16px;
`

export const TitleWrapper = styled.div`
  height: 40px;
  line-height: 40px;
  border-bottom: 1px solid #e8e8e8;
  font-size: 14px;
  color: #50575d;
  font-weight: 600;
  padding-left: 18px;
`
export const ItemWrapper = styled.div`
  padding: 10px 22px 0 10px;
  display: flex;
  align-items: center;
  color: #50575d;
  font-weight: 400;
`
export const ItemTitle = styled.span`
  flex-shrink: 0;
  width: 100px;
  line-height: 32px;
  margin-right: 14px;
  font-size: 14px;
  color: #50575d;
  font-weight: 400;
`

export const FooterWrapper = styled.div`
  height: 84px;
  padding-right: 15px;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  border-top: 1px solid #e8e8e8;
`

export const RequiredWrap = styled.span`
  color: #dc2e2e;
`
export const DashboardCardContainer = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
`
export const ItemWrap = styled.div`
  margin-right: 15px;
  display: flex;
  align-items: center;
  flex-direction: column;
`
export const Text = styled.span`
  font-size: 13px;
  color: #50575d;
`

export const InfoWrap = styled.div`
  display: flex;
  justify-content: space-between;
`
