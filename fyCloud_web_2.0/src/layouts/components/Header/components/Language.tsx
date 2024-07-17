import { Dropdown, Menu } from 'antd'
import { connect } from 'react-redux'
import { setLanguage } from '@/redux/modules/global/global'
import { useAppDispatch, useAppSelector } from '@redux/store'

const Language = (props: any) => {
  const language = useAppSelector(state => state.global.language)
  const dispatch = useAppDispatch()
  console.log('Language render')
  const menu = (
    <Menu
      items={[
        {
          key: '1',
          label: <span>简体中文</span>,
          onClick: () => dispatch(setLanguage('zh')),
          disabled: language === 'zh',
        },
        {
          key: '2',
          label: <span>English</span>,
          onClick: () => dispatch(setLanguage('en')),
          disabled: language === 'en',
        },
      ]}
    />
  )
  return (
    <Dropdown overlay={menu} placement='bottom' trigger={['click']} arrow={true}>
      <i className='icon-style iconfont icon-zhongyingwen'></i>
    </Dropdown>
  )
}

// const mapStateToProps = (state: any) => state.global
// const mapDispatchToProps = { setLanguage }
// export default connect(mapStateToProps, mapDispatchToProps)(Language)
export default Language
