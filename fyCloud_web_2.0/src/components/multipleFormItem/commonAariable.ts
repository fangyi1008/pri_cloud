import { Input, Select, Radio, Checkbox, Slider, Switch, Rate, Cascader, InputNumber } from 'antd'
import SelectBoxGroup from '../selectBoxGroup'
import UnitNumberInput from '../unitNumberInput'
import SearchInput from '../searchInput'
const { TextArea } = Input

export const baseComponents: { [key: string]: any } = {
  input: Input,
  select: Select,
  textarea: TextArea,
  radio: Radio.Group,
  checkbox: Checkbox.Group,
  slider: Slider,
  switch: Switch,
  rate: Rate,
  cascader: Cascader,
  selectBoxGroup: SelectBoxGroup,
  searchInput: SearchInput,
  unitNumber: UnitNumberInput,
  inputNumber: InputNumber,
  text: Input,
  //   upload table custom
}

export const typeToBaseProps: {
  [key: string]: any
} = {
  text: {
    bordered: false,
    readOnly: true,
  },
}

export default baseComponents
