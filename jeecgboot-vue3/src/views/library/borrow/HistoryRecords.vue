<template>
  <div class="p-6 bg-white rounded-lg shadow-sm">
    <div class="mb-6 border-b pb-4">
      <h2 class="text-xl font-bold text-gray-800">⏳ 历史借阅记录与轨迹流水</h2>
      <p class="text-xs text-gray-400 mt-1">系统全量归档的图书借出与归还生命轨迹日志看板</p>
    </div>
    <a-table :dataSource="historyList" :columns="columns" rowKey="id" :loading="loading" :pagination="{ pageSize: 5 }">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <a-tag :color="record.status === '已批准' ? 'success' : record.status === '待审批' ? 'processing' : 'error'">{{ record.status }}</a-tag>
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
  { title: '轨迹流水号', dataIndex: 'id', key: 'id' },
  { title: '关联图书ID', dataIndex: 'bookId', key: 'bookId' },
  { title: '学号', dataIndex: 'readerId', key: 'readerId' },
  { title: '借阅时间', dataIndex: 'borrowTime', key: 'borrowTime' },
  { title: '流转状态', dataIndex: 'status', key: 'status' }
];
const historyList = ref<any[]>([]);
const loading = ref<boolean>(false);

const loadHistory = async () => {
  loading.value = true;
  try {
    const res = await defHttp.get({ url: '/library/borrow/list' });
    historyList.value = res || [];
  } catch (err) { message.error('加载历史数据异常'); }
  finally { loading.value = false; }
};
onMounted(() => { loadHistory(); });
</script>
