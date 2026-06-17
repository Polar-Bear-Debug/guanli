<!-- 物理路径：D:\JeecgBoot-main\jeecgboot-vue3\src\views\library\borrow\OverdueBill.vue -->
<template>
  <div class="p-6 bg-white rounded-lg shadow-sm">
    <div class="mb-6 border-b pb-4">
      <h2 class="text-xl font-bold text-gray-800">💰 逾期未还罚款账单汇总</h2>
      <p class="text-xs text-gray-400 mt-1">全馆逾期超限借阅明细监控及滞纳费自动计算催收账单</p>
    </div>

    <a-table :dataSource="bills" :columns="columns" rowKey="id">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'fee'">
          <span class="text-red-500 font-bold">￥{{ record.fee }} 元</span>
        </template>
        <template v-if="column.key === 'action'">
          <a-button type="primary" size="small" danger @click="handlePay(record.id)">确认交纳销账</a-button>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { message } from 'ant-design-vue';

const columns = [
  { title: '账单流水号', dataIndex: 'id', key: 'id' },
  { title: '读者学号', dataIndex: 'readerCard', key: 'readerCard' },
  { title: '超限图书名称', dataIndex: 'bookName', key: 'bookName' },
  { title: '超期天数', dataIndex: 'overdays', key: 'overdays' },
  { title: '应收滞纳费', key: 'fee' },
  { title: '款项催付', key: 'action' }
];

const bills = ref([
  { id: 'BILL-20260601', readerCard: '23060420', bookName: 'MySQL必知必会(第三版)', overdays: '5 天', fee: '2.50' }
]);

const handlePay = (id: string) => {
  message.success(`账单 ${id} 罚金收取入账完毕，状态更新为“已清欠”！`);
  bills.value = bills.value.filter(b => b.id !== id);
};
</script>