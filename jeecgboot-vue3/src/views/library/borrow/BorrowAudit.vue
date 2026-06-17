<!-- 物理绝对路径：D:\JeecgBoot-main\jeecgboot-vue3\src\views\library\borrow\BorrowAudit.vue -->
<template>
  <div class="p-6 bg-white rounded-lg shadow-sm">
    <div class="mb-6 border-b pb-4 flex justify-between items-center">
      <div>
        <h2 class="text-xl font-bold text-gray-800">📋 借阅事务审批流程</h2>
        <p class="text-xs text-gray-400 mt-1">人工复核并执行流转业务的图书出库审批后台控制台</p>
      </div>
      <a-button type="default" @click="fetchPendingList" :loading="loading">🔄 刷新申请队列</a-button>
    </div>

    <!-- 数据表格展示区：动态读取自 MySQL -->
    <a-table :dataSource="auditList" :columns="columns" rowKey="id" :loading="loading">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <a-tag :color="record.status === '待审批' ? 'orange' : 'blue'">
            ⏳ {{ record.status }}
          </a-tag>
        </template>
        <template v-if="column.key === 'action'">
          <a-space size="middle">
            <a @click="handleApprove(record, '已批准')" class="text-green-500 font-bold">同意并出库</a>
            <a @click="handleApprove(record, '已驳回')" class="text-red-500">拒绝</a>
          </a-space>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { defHttp } from '../../../utils/http/axios';

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
      // 兼容性核心修复：由于后端的 Service 层的初始状态可能被设为了 "待审批" 或 "借出"
      // 我们在这里将状态为 "待审批" 或 "借出" 的记录都拉取出来展示到审批列表中
      // 审批通过（更新为"已批准"）后，单据就会从当前列表中动态消失，刷新也绝不会重复出现！
      auditList.value = res.filter((item: any) => item.status === '待审批' || item.status === '借出');
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