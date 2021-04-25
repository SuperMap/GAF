/**
 * Created by huanl on 2019/8/8
 */
export const columns = [
  { title: '工程名称', dataIndex: 'projName', key: 'projName' },
  {
    title: '可见性',
    dataIndex: 'visibility',
    key: 'visibility',
    scopedSlots: { customRender: 'visibility' },
  },
  {
    title: 'Git地址',
    dataIndex: 'gitUrl',
    key: 'gitUrl',
    scopedSlots: { customRender: 'url' },
    width: '320px',
  },
  {
    title: '所属工程组',
    dataIndex: 'projGroupName',
    key: 'projGroupName',
  },
  {
    title: '描述',
    dataIndex: 'description',
    key: 'description',
  },
  {
    title: '是否已创建',
    dataIndex: 'status',
    key: 'status',
    scopedSlots: { customRender: 'status' },
    width: '80px',
  },
  {
    title: '操作',
    dataIndex: 'action',
    key: 'action',
    scopedSlots: { customRender: 'action' },
  },
]
