import { Input, Radio, Select, Checkbox, Upload } from 'antd'
import { ItemWrapper, ItemTitle, RequiredWrap, TitleStyle } from './style'
import { FormDataType, MultipleFormPropsType, SelectDataType } from './type'
import SelectBoxGroup from '../selectBoxGroup'
import { isNotEmptyArray } from '@common/utils'
import { BoxItemType } from '../selectBoxGroup/type'
const { TextArea } = Input
const { Dragger } = Upload

// 必选参数不能位于可选参数后
const findItem = (value: string[] | string, selectData?: SelectDataType[] | undefined) => {
  const newItem =
    selectData && isNotEmptyArray(selectData) && selectData.find(items => items.value === value)
  if (newItem) {
    return {
      newLabel: newItem.label,
      newItem,
    }
  }
  return {
    newLabel: [],
    newItem: [],
  }
}
const getSelectItemAndLabel = (
  selectData: SelectDataType[] | undefined,
  selectKey: string[] | string,
) => {
  const newItem: SelectDataType[] = []
  const newLabel: string[] = []
  if (selectKey && isNotEmptyArray(selectKey) && typeof selectKey !== 'string') {
    selectKey.forEach(keys => {
      newItem.push(findItem(keys, selectData).newItem as SelectDataType)
      newLabel.push(findItem(keys, selectData).newLabel as string)
    })
    return {
      newItem,
      newLabel,
    }
  }
  return {
    newItem: [],
    newLabel: [],
  }
}
export default function MultipleForm(props: MultipleFormPropsType) {
  const { data = [], onChange, value: valueObj = {} } = props

  const handleChange =
    (key: string, selectData?: SelectDataType[]) => (obj: { target: { value: string } }) => {
      const {
        target: { value },
      } = obj
      const { newItem = [], newLabel = [] } = findItem(value, selectData)
      onChange &&
        onChange({
          key,
          newValue: value,
          newItem: newItem,
          // 如果selectData不存在表示为input输入
          newLabel: selectData ? newLabel : value,
        })
    }

  const handleSelectChange =
    (key: string, selectData?: SelectDataType[]) => (selectKey: string[] | string) => {
      const { newItem = [], newLabel = [] } = getSelectItemAndLabel(selectData, selectKey)
      onChange &&
        onChange({
          key,
          newValue: selectKey,
          newItem,
          newLabel: newLabel.length > 0 ? newLabel : findItem(selectKey, selectData).newLabel,
        })
    }
  const handleCheckButtonChange = (key: string) => (newValue: string, newItem: BoxItemType) => {
    onChange &&
      onChange({
        key,
        newValue,
        newItem,
        newLabel: newItem.label,
      })
  }

  const getComponentsByType = (item: FormDataType) => {
    const { type = 'input', key = '', data = [], render, uploadProps, title, component } = item

    let element
    const propsObj: any = {
      ...item,
      value: valueObj && valueObj[key],
    }
    switch (type) {
      case 'input':
        element = <Input {...propsObj} onChange={handleChange(key)} />
        break
      case 'select':
        element = (
          <Select
            {...propsObj}
            onChange={handleSelectChange(key, data)}
            options={data}
            style={{ width: '100%' }}
          />
        )
        break
      case 'textarea':
        element = <TextArea {...propsObj} options={data} onChange={handleChange(key)} />
        break
      case 'text':
        element = (
          <Input {...item} bordered={false} readOnly={true} value={valueObj && valueObj[key]} />
        )
        break
      case 'radio':
        element = <Radio.Group {...propsObj} options={data} onChange={handleChange(key, data)} />
        break
      case 'checkbox':
        element = (
          <Checkbox.Group {...propsObj} options={data} onChange={handleSelectChange(key, data)} />
        )
        break
      case 'selectBoxGroup':
        element = <SelectBoxGroup {...propsObj} onChange={handleCheckButtonChange(key)} />
        break
      case 'upload':
        element = (
          <Dragger {...uploadProps}>{render ? render() : <TitleStyle>{title}</TitleStyle>}</Dragger>
        )
        break
      case 'table':
        element = component
        break
      case 'custom':
        element = render && render(key)
        break
      default:
        element = <Input {...propsObj} onChange={handleChange(key)} />
    }
    return element
  }

  return (
    <>
      {isNotEmptyArray(data) &&
        data.map((item, index) => {
          const { key, title, isRequired } = item
          return (
            <ItemWrapper key={`${key}_${index}`}>
              {title ? (
                <ItemTitle>
                  {title}
                  {isRequired ? <RequiredWrap>*</RequiredWrap> : null}
                </ItemTitle>
              ) : null}
              {getComponentsByType(item)}
            </ItemWrapper>
          )
        })}
    </>
  )
}
