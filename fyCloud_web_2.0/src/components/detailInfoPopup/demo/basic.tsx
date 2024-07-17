import { useState } from 'react'
import { Button } from 'antd'
import DetailInfoPopup from '../index'

export default function Demo() {
  const [visible, setVisible] = useState<boolean>(false)
  const data = {
    name: 'testD1',
    displayName: 'testD1',
    type: '本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录本地文件目录',
    targetPath: '/vms/testD1',
    size: '111GB',
    useSize: '80GB',
    canUseSize: '100GB',
    status: '活动',
  }
  const fieldNames = {
    name: '名称',
    displayName: '显示名称',
    type: '类型',
    targetPath: '目标路基',
    size: '总容量',
    useSize: '已分配容量',
    canUseSize: '实际可用容量',
    status: '状态',
  }
  return (
    <>
      <Button type={'primary'} onClick={() => setVisible(true)}>
        展示详情
      </Button>
      <DetailInfoPopup
        onCancel={() => {
          setVisible(false)
        }}
        bodyStyle={{ height: '290px', padding: '0' }}
        visible={visible}
        title={'详情数据展示'}
        data={data}
        fieldNames={fieldNames}
      />
    </>
  )
}
