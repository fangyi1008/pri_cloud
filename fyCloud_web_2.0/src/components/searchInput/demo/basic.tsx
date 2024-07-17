import SearchInput from '../index'

export default function Demo() {
  const onDelete = () => {
    console.log('onDelete')
  }

  const onSearch = () => {
    console.log('onSearch')
  }

  return (
    <>
      <SearchInput onDelete={onDelete} onSearch={onSearch} readOnly />
      <br />
      <SearchInput onSearch={onSearch} allowDelete={false} />
      <br />
      <SearchInput
        addonBefore={<div>before</div>}
        addonAfter={<div>after</div>}
        prefix={<div>prefix</div>}
        suffix={<div>prefix</div>}
        allowSearch={false}
        onDelete={onDelete}
        onSearch={onSearch}
      />
    </>
  )
}
