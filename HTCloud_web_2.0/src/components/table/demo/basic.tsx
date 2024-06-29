import Table from '../index'

export default function Demo() {
  const columns = [
    {
      title: '名称',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'id',
      dataIndex: 'clusterId',
      key: 'clusterId',
    },
    {
      title: '分布式资源调度',
      dataIndex: 'drsSwitch',
      key: 'drsSwitch',
    },

    {
      title: '高可用',
      dataIndex: 'haSwitch',
      key: 'haSwitch',
    },

    {
      title: '数据中心',
      dataIndex: 'dataCenterId',
      key: 'dataCenterId',
    },

    {
      title: '创建时间',
      dataIndex: 'createTime',
      key: 'createTime',
    },
  ]
  const data = [
    {
      name: '张三',
      clusterId: 1233,
    },
  ]
  return <Table tableColumn={columns} tableData={data} firstColRender={true}></Table>
}
