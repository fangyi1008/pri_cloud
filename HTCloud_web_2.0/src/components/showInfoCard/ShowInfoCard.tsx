import { Progress } from 'antd'
import {
  ItemWrapper,
  ItemTitle,
  RequiredWrap,
  Container,
  CardTitle,
  DashboardCardContainer,
  ItemWrap,
  Text,
  InfoWrap,
} from './style'
import { BasicCardDataType, InfoType, ShowInfoCardPropsType } from './type'
import { isNotEmptyArray } from '@common/utils'
import EmptyData from '@components/emptyData'

export default function Demoe(props: ShowInfoCardPropsType) {
  const {
    basicData = [],
    cardTitle = '',
    type = 'basic',
    customCard,
    dashboardData = [],
    height = '',
    width = '',
  } = props

  const getDiffCardContentByType = () => {
    let element
    switch (type) {
      case 'basic':
        element = getBasicCardElement(basicData)
        break
      case 'dashboard':
        element = getDashboardCardElement()
        break
      case 'custom':
        element = customCard
        break
      default:
        element = null
    }
    return element
  }
  const getBasicCardElement = (data: BasicCardDataType[]) => {
    return (
      isNotEmptyArray(data) &&
      data.map((item: BasicCardDataType) => {
        const { key, label, isRequired } = item
        return (
          <ItemWrapper key={key}>
            {label ? (
              <ItemTitle>
                {label}
                {isRequired ? <RequiredWrap>*</RequiredWrap> : null}
              </ItemTitle>
            ) : null}
            {getComponentsByType(item)}
          </ItemWrapper>
        )
      })
    )
  }

  const getDashboardInfoElement = (info: InfoType[]) => {
    return (
      <div>
        {isNotEmptyArray(info) &&
          info.map((item: InfoType) => {
            const { key, label, value } = item
            return (
              <InfoWrap key={key}>
                {label}：<Text>{value}</Text>
              </InfoWrap>
            )
          })}
      </div>
    )
  }
  const getDashboardCardElement = () => {
    return (
      <DashboardCardContainer>
        {dashboardData.length > 0 ? (
          isNotEmptyArray(dashboardData) &&
          dashboardData.map(item => {
            const { key, width, percent, strokeColor, info = [] } = item
            return (
              <ItemWrap key={key}>
                <Progress
                  type='circle'
                  strokeColor={
                    strokeColor || {
                      '0%': '#108ee9',
                      '100%': '#87d068',
                    }
                  }
                  percent={percent}
                  width={width || 100}
                />
                <br />
                {getDashboardInfoElement(info)}
              </ItemWrap>
            )
          })
        ) : (
          <EmptyData />
        )}
      </DashboardCardContainer>
    )
  }
  const getComponentsByType = (item: BasicCardDataType) => {
    const { type = 'text', key = '', render, value = '' } = item

    let element

    switch (type) {
      case 'text':
        element = <Text>{value}</Text>
        break
      case 'progress':
        element = (
          <Progress
            {...item}
            strokeColor={{
              '0%': '#108ee9',
              '100%': '#87d068',
            }}
            strokeLinecap={'butt'}
            type={'line'}
            percent={value}
          />
        )
        break
      case 'custom':
        element = render && render(key)
        break
      default:
        element = null
    }
    return element
  }

  return (
    <Container width={width} height={height}>
      <CardTitle>{cardTitle}</CardTitle>
      {getDiffCardContentByType()}
    </Container>
  )
}
