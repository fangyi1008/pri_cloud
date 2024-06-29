import { MenuFoldOutlined, MenuUnfoldOutlined } from '@ant-design/icons'
import { connect } from 'react-redux'
import { updateCollapse } from '@/redux/modules/menu/menu'
import { useAppDispatch, useAppSelector } from '@redux/store'

const CollapseIcon = (props: any) => {
  const { isCollapse } = useAppSelector(state => state.menu)
  const dispatch = useAppDispatch()
  return (
    <div
      className='collapsed'
      onClick={() => {
        dispatch(updateCollapse(!isCollapse))
      }}
    >
      {isCollapse ? <MenuUnfoldOutlined id='isCollapse' /> : <MenuFoldOutlined id='isCollapse' />}
    </div>
  )
}

// const mapStateToProps = (state: any) => state.menu
// const mapDispatchToProps = { updateCollapse }
// export default connect(mapStateToProps, mapDispatchToProps)(CollapseIcon)

export default CollapseIcon
