import logo from '@/assets/images/logo.png'
import { connect } from 'react-redux'

const Logo = (props: any) => {
  const { isCollapse } = props
  return (
    <div className={!isCollapse ? 'logo-box' : 'logo-box-collapse'}>
      <h2 className='logo-text'>{!isCollapse ? '中科信工' : '中'}</h2>
    </div>
  )
}

const mapStateToProps = (state: any) => state.menu
export default connect(mapStateToProps)(Logo)
