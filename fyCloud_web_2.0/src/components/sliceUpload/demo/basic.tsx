import { Fragment, RefObject, useRef } from 'react'
import SliceUpload from '../index'
import eventEmitter from '@/events/index'
import { ISliceUploadRef } from '../type'

export default function Demo() {
  const onClick = () => {
    sliceUploadRef?.current?.openSelectFileWindow({
      options: {
        singleFile: false,
        target: 'http://localhost:3000/upload',
      },
      queryParam: { a: 1 },
    })
    // eventEmitter.emit('openGlobalUploader')
  }
  const sliceUploadRef: RefObject<ISliceUploadRef> = useRef(null)

  const onClick1 = () => {
    eventEmitter.emit('openGlobalUploader', {
      options: {
        singleFile: true,
        accept: '.iso',
        target: 'http://www.baidu.com',
      },
      queryParam: { a: 2 },
    })
  }

  return (
    <Fragment>
      <button onClick={onClick}>上传代码</button>

      <button onClick={onClick1}>上传代码</button>
      <SliceUpload autoStart={false} ref={sliceUploadRef} />
    </Fragment>
  )
}
