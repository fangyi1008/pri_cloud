import { UnitNumberInputPropsType } from './type'
import { SelectDataType } from '../multipleForm/type'
import { InputNumber, Select } from 'antd'
import { CloseOutlined } from '@ant-design/icons'
import { buttonIconFontSize } from '@common/commonAariable'
import { DeleteWrap, InputWrap, NumberInputWrap } from './style'

function UnitNumberInput(props: UnitNumberInputPropsType) {
  const {
    value,
    unitOptions = [],
    step = 1,
    min = 1,
    max = Number.MAX_SAFE_INTEGER,
    allowDelete = true,
    fieldNames,
    disabled,
    onDelete,
    onChange,
  } = props
  const getFirstValue = (array: SelectDataType[]) => {
    const { value: valueField = 'value' } = fieldNames || {}
    if (Array.isArray(array) && array.length >= 1) {
      return unitOptions[0][valueField] ?? ''
    }
    return ''
  }
  const { unitValue = getFirstValue(unitOptions), inputValue } = value || {}

  const onInputChange = (value: number | null) => {
    const { value: valueField = 'value' } = fieldNames || {}
    onChange &&
      value &&
      onChange({
        inputValue: value,
        unitValue,
        UnitObject: unitOptions.find((item: SelectDataType) => item[valueField] === unitValue),
      })
  }

  const onSelectChange = (unitValue: string, optionInfo: SelectDataType | SelectDataType[]) => {
    onChange &&
      onChange({
        inputValue,
        unitValue: unitValue,
        UnitObject: Array.isArray(optionInfo) ? optionInfo[0] : optionInfo,
      })
  }
  return (
    <NumberInputWrap>
      <InputWrap>
        <InputNumber
          style={{ width: '100%' }}
          step={step}
          min={min}
          max={max}
          value={inputValue}
          disabled={disabled}
          onChange={onInputChange}
          addonAfter={
            <Select
              value={unitValue}
              disabled={disabled}
              options={unitOptions}
              fieldNames={fieldNames}
              style={{ minWidth: 40 }}
              showArrow={false}
              allowClear={false}
              onChange={onSelectChange}
            />
          }
        />
      </InputWrap>
      {allowDelete && (
        <DeleteWrap onClick={onDelete}>
          <CloseOutlined style={buttonIconFontSize} />
        </DeleteWrap>
      )}
    </NumberInputWrap>
  )
}

export default UnitNumberInput
