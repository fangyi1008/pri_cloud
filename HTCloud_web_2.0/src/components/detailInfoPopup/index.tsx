import React from 'react'
import { Modal, Button } from 'antd'
import { DetailInfoPopupPropsType, DetailInfoPropsType } from './type'
import {
  DetailInfoPopupWrap,
  DetailInfoPopupRow,
  DetailInfoPopupRowTitle,
  DetailInfoPopupRowContent,
  DetailInfoPopupScrollWrap,
} from './style'
import { isValueEqualType } from '@common/utils'

export function DetailInfo(props: DetailInfoPropsType) {
  const {
    backgroundColor = ['#ffffff', '#F5F5F5'],
    fontColor = ['#000', '#000'],
    fieldNames = {},
    data = {},
  } = props
  const fieldInfo = Object.entries(fieldNames) || []
  const getValue = (data: any) => {
    if (isValueEqualType(data, 'String') || isValueEqualType(data, 'Number')) {
      return data
    }
    if (isValueEqualType(data, 'Array')) {
      return data.join(',')
    }
    if (isValueEqualType(data, 'Object')) {
      const values = Object.values(data) ?? []
      let objectStr = ''
      values.forEach(value => {
        if (isValueEqualType(value, 'String') || isValueEqualType(value, 'Number')) {
          objectStr += ' ' + value
        }
      })
      return objectStr
    }
    return '--'
  }
  return (
    <>
      {fieldInfo.map(([key, title], index) => {
        const remainder = (index + 1) % 2
        const background = backgroundColor[remainder]
        const color = fontColor[remainder]
        return (
          <DetailInfoPopupRow backgroundColor={background} key={key}>
            <DetailInfoPopupRowTitle color={color}>{title}</DetailInfoPopupRowTitle>
            <DetailInfoPopupRowContent color={color}>
              {getValue(data[key]) || '--'}
            </DetailInfoPopupRowContent>
          </DetailInfoPopupRow>
        )
      })}
    </>
  )
}

function DetailInfoPopup(props: DetailInfoPopupPropsType) {
  const {
    data,
    fieldNames,
    backgroundColor = ['#ffffff', '#F5F5F5'],
    fontColor = ['#000', '#000'],
    ...modalOtherProps
  } = props

  const { onCancel } = modalOtherProps
  return (
    <Modal
      {...modalOtherProps}
      centered
      footer={
        <Button type='primary' onClick={onCancel}>
          关闭
        </Button>
      }
    >
      <DetailInfoPopupWrap>
        <DetailInfoPopupScrollWrap>
          <DetailInfo
            data={data}
            fieldNames={fieldNames}
            backgroundColor={backgroundColor}
            fontColor={fontColor}
          />
        </DetailInfoPopupScrollWrap>
      </DetailInfoPopupWrap>
    </Modal>
  )
}

export default DetailInfoPopup
