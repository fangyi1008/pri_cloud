import SelectBoxGroup from '../index'
import { useState } from 'react'
import win from './icon_os_windows.svg'
import linux from './icon_os_linux.svg'

export default function Demo() {
  const [value, setValue] = useState('')

  const buttons = [
    {
      src: win,
      label: 'win1',
      value: 'win1',
    },
    {
      src: linux,
      label: 'linux1',
      value: 'linux1',
    },
  ]

  const buttonsText = [
    {
      label: '更改主机',
      value: '更改主机',
    },
    {
      label: '更改数据存储',
      value: '更改数据存储',
    },
    {
      label: '更改主机和数据存储',
      value: '更改主机和数据存储',
    },
  ]

  const onChange = (selectKey: string) => {
    setValue(selectKey)
  }
  return (
    <>
      <SelectBoxGroup data={buttons} value={value} onChange={onChange} />
      <br />
      <SelectBoxGroup
        data={buttonsText}
        value={value}
        onChange={onChange}
        height={32}
        width={160}
      />
      <br />
      <SelectBoxGroup
        height={32}
        width={160}
        direction={'column'}
        data={buttonsText}
        value={value}
        onChange={onChange}
      />
    </>
  )
}
