import React from 'react'

export type UploaderContextProps = {
  uploaderRef?: React.MutableRefObject<Record<string, any> | null>
  support: boolean
}

const UploaderContext = React.createContext<UploaderContextProps>({
  support: true,
})

export { UploaderContext }

export default UploaderContext
