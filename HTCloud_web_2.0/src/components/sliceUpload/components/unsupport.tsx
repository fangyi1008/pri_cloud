import { useContext } from 'react'
import { UploaderContext } from '../uploaderContext'
import { IUnsupportPropsType } from '../type'

export default function Unsupport(props: IUnsupportPropsType) {
  const { unsupportText = '当前浏览器不支持上传' } = props
  const { support } = useContext(UploaderContext)
  return (
    <>
      {support ? null : (
        <div>
          <p>{unsupportText}</p>
        </div>
      )}
    </>
  )
}
