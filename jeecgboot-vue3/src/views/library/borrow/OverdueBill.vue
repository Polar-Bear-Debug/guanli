<template>
  <div class="p-6 bg-white rounded-lg shadow-sm">
    <div class="mb-6 border-b pb-4 flex justify-between items-center">
      <div>
        <h2 class="text-xl font-bold text-gray-800">💰 逾期未还罚款账单汇总</h2>
        <p class="text-xs text-gray-400 mt-1">全馆逾期超限借阅明细监控及滞纳费自动计算催收账单</p>
      </div>
      <a-button type="default" @click="loadBills" :loading="loading">🔄 刷新账单</a-button>
    </div>

    <a-table :dataSource="bills" :columns="columns" rowKey="id" :loading="loading" :pagination="{ pageSize: 5 }">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'overdays'">
          <a-tag color="red">{{ record.overdays }} 天</a-tag>
        </template>
        <template v-if="column.key === 'fee'">
          <span class="text-red-500 font-bold text-base">￥{{ record.fee }}</span>
        </template>
        <template v-if="column.key === 'action'">
          <a-popconfirm title="确认已收到该读者缴纳的滞纳金？" okText="确认销账" cancelText="取消" @confirm="handlePay(record.id)">
            <a-button type="primary" size="small" danger>确认交纳销账</a-button>
          </a-popconfirm>
        </template>
      </template>
    </a-table>

    <a-empty v-if="!loading && bills.length === 0" description="暂无逾期账单，所有借阅均在期限内" />
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { defHttp } from '../../../utils/http/axios';

const columns = [
  { title: '账单流水号', dataIndex: 'id', key: 'id' },
  { title: '读者学号', dataIndex: 'readerCard', key: 'readerCard' },
  { title: '读者姓名', dataIndex: 'readerName', key: 'readerName' },
  { title: '超限图书名称', dataIndex: 'bookName', key: 'bookName' },
  { title: '应还日期', dataIndex: 'dueDate', key: 'dueDate' },
  { title: '超期天数', dataIndex: 'overdays', key: 'overdays' },
  { title: '应收滞纳费', key: 'fee' },
  { title: '款项催付', key: 'action' }
];

const bills = ref<any[]>([]);
const loading = ref(false);

const loadBills = async () => {
  loading.value = true;
  try {
    const res = await defHttp.get({ url: '/library/borrow/overdue' });
    bills.value = res || [];
  } catch (err) {
    message.error('加载逾期账单失败，请确认后端已启动！');
  } finally {
    loading.value = false;
  }
};

const handlePay = async (id: string) => {
  try {
    await defHttp.put({ url: `/library/borrow/payOverdue?id=${id}` });
    message.success('罚金已核销，该记录永久标记为已清欠！');
    await loadBills();
  } catch (err) {
    message.error('销账操作失败');
  }
};

onMounted(() => { loadBills(); });
</script>
