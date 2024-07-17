import { Input } from 'antd'
const { Search: SearchInput } = Input
import { SearchWrap } from './style'
import { SearchOutlined } from '@ant-design/icons'
import { SearchPropsType } from './type'
function Search(props: SearchPropsType) {
  const { onSearch, addonBefore, placeholder, loading } = props
  return (
    <SearchWrap>
      <SearchInput
        addonBefore={addonBefore}
        placeholder={placeholder}
        allowClear
        onSearch={onSearch}
        enterButton={<SearchOutlined />}
        loading={loading}
      />
    </SearchWrap>
  )
}

export default Search
