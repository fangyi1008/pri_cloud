import { useEffect, useState } from 'react'
import { H1Title } from '@/common/commonStyle'
import { ProColumns, ProTable } from '@ant-design/pro-components'
import { jsonArrayStrToArray } from '@/utils/util'

const MonitorClusterComponent = <T extends Record<string, any>, R = T>(props: {
  title: string
  columns: ProColumns<T>[]
  apiUrl: string
  formatData?: (data: R[]) => T[]
}) => {
  const { title, columns, formatData, apiUrl } = props
  const [data, setData] = useState<T[]>([])
  useEffect(() => {
    const source = new EventSource(apiUrl)
    source.addEventListener('message', event => {
      try {
        const result = jsonArrayStrToArray<R>(event.data)
        let list: T[] = result as unknown as T[]
        if (typeof formatData === 'function') {
          list = formatData(result)
        }
        setData(list)
      } catch (error: any) {
        console.warn(error.message)
        setData([])
      }
    })
    return () => {
      source.close()
    }
  }, [])

  return (
    <div style={{ background: '#fff' }}>
      <H1Title>{title}</H1Title>
      <ProTable<T>
        pagination={false}
        search={false}
        dataSource={data}
        columns={columns}
        rowKey={'clusterName'}
        headerTitle=''
        tableAlertRender={false}
        tableAlertOptionRender={false}
      />
    </div>
  )
}

export default MonitorClusterComponent
