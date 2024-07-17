import { CheckOutlined } from '@ant-design/icons'
import {
  SelectBoxWrap,
  SelectStatus,
  SelectIconWrap,
  SelectBoxGroupWrap,
  DescribeWrap,
  SelectBoxItemBox,
} from './style'
import { SelectBoxPropsType, SelectBoxGroupPropsType } from './type'

export function SelectBox(props: SelectBoxPropsType) {
  const { data, width, height, isChecked, space = 0, direction, onChange } = props
  const { src = '', label = '', value = '', describe = '' } = data || {}

  const onChangeHandler = () => {
    onChange && onChange(value, data)
  }
  return (
    <SelectBoxItemBox direction={direction} space={space}>
      <SelectBoxWrap width={width} height={height} isChecked={isChecked} onClick={onChangeHandler}>
        {isChecked && (
          <SelectStatus>
            <SelectIconWrap>
              <CheckOutlined style={{ fontSize: '18px', color: '#fff' }} />
            </SelectIconWrap>
          </SelectStatus>
        )}
        {src ? <img src={src} /> : label}
      </SelectBoxWrap>
      {describe && direction === 'column' && (
        <DescribeWrap title={describe}>{describe}</DescribeWrap>
      )}
    </SelectBoxItemBox>
  )
}

function SelectBoxGroup(props: SelectBoxGroupPropsType) {
  const { data = [], value, onChange, space = 10, height, width, direction = 'row' } = props
  return (
    <SelectBoxGroupWrap direction={direction}>
      {data.map((item, index, array) => {
        const { value: itemValue } = item
        const isChecked = itemValue === value
        const itemSpace = index === array.length - 1 ? 0 : space
        return (
          <SelectBox
            direction={direction}
            key={itemValue}
            width={width}
            height={height}
            data={item}
            isChecked={isChecked}
            onChange={onChange}
            space={itemSpace}
          />
        )
      })}
    </SelectBoxGroupWrap>
  )
}

export default SelectBoxGroup
