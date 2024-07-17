import { useEffect, useState } from 'react'
import { Form } from 'antd'
import { CaretRightOutlined, CaretDownOutlined } from '@ant-design/icons'
import { FormGroupItemRowPropsType, FormGroupPropsType, FormNormalItemRowPropsType } from './type'
import { typeToBaseProps, baseComponents } from './commonAariable'
import eventEmitter from '@/events/index'
const { Item, List } = Form

function CanExpandedFromLabel(props: any) {
  const { isOpen, onExpanded, label } = props
  const onClick = () => {
    onExpanded(!isOpen)
  }
  return (
    <>
      <span style={{ marginRight: '4px' }} onClick={onClick}>
        {isOpen ? <CaretDownOutlined /> : <CaretRightOutlined />}
      </span>
      <span>{label}</span>
    </>
  )
}

function FormNormalItemRow(props: FormNormalItemRowPropsType) {
  const { type, formItemProps, componentProps, component: CustomDemo, labelAlign = 'right' } = props
  const Element = baseComponents[type] || null
  const baseProps = typeToBaseProps[type] ?? {}
  const newFormItemProps = { labelAlign, ...formItemProps }
  if (type === 'custom' && CustomDemo) {
    return (
      <>
        <CustomDemo formItemProps={{ ...newFormItemProps }} componentProps={componentProps} />
      </>
    )
  }
  if (!Element) {
    return null
  }
  const { refreshResultData, ...newComponentProps } = componentProps || {}
  return (
    <Item {...newFormItemProps}>
      <Element {...newComponentProps} {...baseProps} />
    </Item>
  )
}

function FormGroupItemRow(props: FormGroupItemRowPropsType) {
  const { data, labelField, labelNameField, labelAlign, refreshResultData } = props
  const [isOpen, setIsOpen] = useState(false)
  const { groupItems = [] } = data || {}
  const firstItem = groupItems[0]
  const otherItems = groupItems.slice(1)
  const {
    type,
    componentProps,
    formItemProps,
    component,
    [labelNameField]: name,
    [labelField]: firstLabel,
  } = firstItem
  useEffect(() => {
    const openGroup = (type: string) => {
      console.log(type, name)
      if (name === type) {
        setIsOpen(true)
      }
    }
    eventEmitter.addListener('groupError', openGroup)
    return () => {
      eventEmitter.off('groupError', openGroup)
    }
  }, [])
  const onExpanded = (status: boolean) => {
    setIsOpen(status)
  }
  const newFormItemProps = { labelAlign, ...formItemProps }
  const label = <CanExpandedFromLabel label={firstLabel} isOpen={isOpen} onExpanded={onExpanded} />
  return (
    <>
      <FormNormalItemRow
        key={name}
        type={type}
        formItemProps={{
          ...newFormItemProps,
          name,
          label,
        }}
        componentProps={{ ...componentProps, refreshResultData }}
        component={component}
      />
      <div
        style={{
          paddingLeft: '20px',
          height: isOpen ? 'auto' : '0',
          overflow: 'hidden',
          transition: 'all 0.5s',
        }}
      >
        {otherItems.map(item => {
          const {
            type,
            formItemProps,
            componentProps,
            component,
            [labelField]: label,
            [labelNameField]: name,
          } = item
          return (
            <FormNormalItemRow
              key={name}
              component={component}
              type={type}
              formItemProps={{ ...formItemProps, labelAlign, name, label }}
              componentProps={{ ...componentProps, refreshResultData }}
            />
          )
        })}
      </div>
    </>
  )
}

function FormGroup(props: FormGroupPropsType) {
  const {
    labelField = 'title',
    labelNameField = 'key',
    formItems = [],
    labelAlign = 'right',
    refreshResultData,
  } = props
  return (
    <>
      {formItems.map(item => {
        const {
          type,
          formItemProps,
          componentProps,
          component,
          [labelField]: label,
          [labelNameField]: labelName,
        } = item
        return type === 'groupFrom' ? (
          <FormGroupItemRow
            key={labelName}
            data={item}
            labelField={labelField}
            labelNameField={labelNameField}
            labelAlign={labelAlign}
            refreshResultData={refreshResultData}
          />
        ) : (
          <FormNormalItemRow
            key={labelName}
            labelAlign={labelAlign}
            type={type}
            component={component}
            formItemProps={{ ...formItemProps, name: labelName, label }}
            componentProps={{ ...componentProps, refreshResultData }}
          />
        )
      })}
    </>
  )
}

export default FormGroup
