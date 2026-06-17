<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { message } from 'ant-design-vue';
// 核心修复：1. 推荐使用官方虚拟别名 '/@/utils/http/axios' (绝不会因数错层级而报错)
// 2. 如果你的环境别名失效，请切换为正确的 3 层相对路径: '../../../utils/http/axios'
import { defHttp } from '/@/utils/http/axios';

const columns = [
  { title: '流转审批单号', dataIndex: 'id', key: 'id' },
  { title: '关联图书ID', dataIndex: 'bookId', key: 'bookId' },
  { title: '借阅卡号/学号', dataIndex: 'readerId', key: 'readerId' },
  { title: '申请时间', dataIndex: 'borrowTime', key: 'borrowTime' },
  { title: '流转状态', key: 'status' },
  { title: '出库控制', key: 'action' }
];

const auditList = ref<any[]>([]);
const loading = ref<boolean>(false);

const fetchPendingList = async () => {
  loading.value = true;
  try {
    const res = await defHttp.get({ url: '/library/borrow/list' });
    if (res) {
      // 过滤出只有待审批状态的记录，展示给库管员
      auditList.value = res.filter((item: any) => item.status === '待审批');
    }
  } catch (err) {
    message.error('无法连接后端网关，请确认后端已经启动！');
  } finally {
    loading.value = false;
  }
};

const handleApprove = async (record: any, targetStatus: string) => {
  try {
    const params = {
      ...record,
      status: targetStatus // 直接修改数据库中单据状态
    };
    await defHttp.put({ url: '/library/borrow/edit', params });
    message.success(`单据 ${record.id} 审批处理完毕，流转状态变更为：${targetStatus}`);
    await fetchPendingList(); // 重新加载，同意的数据从待审批中动态消失！
  } catch (err) {
    message.error('审批流转网络请求异常');
  }
};

onMounted(() => {
  fetchPendingList();
});
</script>