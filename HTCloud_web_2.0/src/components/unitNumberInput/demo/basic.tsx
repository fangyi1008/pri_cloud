import { useState } from 'react'
import UnitNumberInput from '../index'
import { UnitNumberValueType } from '../type'

// eslint-disable-next-line react/display-name
export default () => {
  const [value, setValue] = useState<UnitNumberValueType>({
    unitValue: '核',
  })
  const option = [
    { label: '个', value: '个' },
    { label: '核', value: '核' },
  ]
  const onDelete = () => {
    console.log('onDelete')
  }
  const onChange = (target: UnitNumberValueType) => {
    const { inputValue, unitValue = '' } = target
    setValue({ inputValue, unitValue })
  }
  return (
    <>
      <UnitNumberInput
        value={value}
        min={1}
        max={4}
        onDelete={onDelete}
        unitOptions={option}
        onChange={onChange}
      />
      <br />
      <UnitNumberInput
        value={value}
        min={1}
        max={4}
        onDelete={onDelete}
        allowDelete={false}
        unitOptions={option}
        onChange={onChange}
      />
    </>
  )
}
