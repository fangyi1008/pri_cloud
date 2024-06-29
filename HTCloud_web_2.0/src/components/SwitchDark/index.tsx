import { Switch } from 'antd'
import { setThemeConfig } from '@/redux/modules/global/global'
import { useAppDispatch, useAppSelector } from '@redux/store'

const SwitchDark = () => {
  const { themeConfig } = useAppSelector(state => state.global)
  const dispatch = useAppDispatch()
  const onChange = (checked: boolean) => {
    dispatch(setThemeConfig({ ...themeConfig, isDark: checked }))
  }

  return (
    <Switch
      className='dark'
      defaultChecked={themeConfig.isDark}
      checkedChildren={<>🌞</>}
      unCheckedChildren={<>🌜</>}
      onChange={onChange}
    />
  )
}

// const mapStateToProps = (state: any) => state.global
// const mapDispatchToProps = { setThemeConfig }
// export default connect(mapStateToProps, mapDispatchToProps)(SwitchDark)
export default SwitchDark
