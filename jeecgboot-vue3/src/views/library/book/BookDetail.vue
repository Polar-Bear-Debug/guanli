<template>
  <div class="p-6 bg-gray-50 min-h-screen">
    <div class="mb-6">
      <h2 class="text-xl font-bold text-gray-800">📊 图书馆馆藏明细及多维数据档案</h2>
      <p class="text-xs text-gray-400 mt-1">综合监控当前在馆图书借阅情况、流转活性以及库存分布比例</p>
    </div>

    <!-- 顶层卡片看板 -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
      <div class="p-6 bg-white rounded-lg border border-gray-100 shadow-sm flex items-center justify-between">
        <div>
          <p class="text-xs text-gray-400 font-medium">累计馆藏品种</p>
          <h3 class="text-3xl font-bold text-gray-800 mt-1">{{ books.length }} <span class="text-xs font-normal text-gray-400">种</span></h3>
        </div>
        <div class="text-3xl">📚</div>
      </div>
      <div class="p-6 bg-white rounded-lg border border-gray-100 shadow-sm flex items-center justify-between">
        <div>
          <p class="text-xs text-gray-400 font-medium">在库总物理册数</p>
          <h3 class="text-3xl font-bold text-green-600 mt-1">{{ totalStock }} <span class="text-xs font-normal text-gray-400">册</span></h3>
        </div>
        <div class="text-3xl">📥</div>
      </div>
      <div class="p-6 bg-white rounded-lg border border-gray-100 shadow-sm flex items-center justify-between">
        <div>
          <p class="text-xs text-gray-400 font-medium">全馆借阅流转活性比</p>
          <h3 class="text-3xl font-bold text-orange-500 mt-1">94.8%</h3>
        </div>
        <div class="text-3xl">⚡</div>
      </div>
    </div>

    <!-- 图书详细网格卡片 -->
    <div class="bg-white p-6 rounded-lg border border-gray-100 shadow-sm">
      <h3 class="text-base font-bold text-gray-800 mb-6">🔍 馆藏图书深度明细列表</h3>
      <a-table :dataSource="books" :columns="columns" rowKey="id" :pagination="{ pageSize: 5 }">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-badge status="processing" text="在馆流通中" />
          </template>
          <template v-if="column.key === 'stock'">
            <span class="font-bold text-gray-700">{{ record.stock }} 本</span>
          </template>
        </template>
      </a-table>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, computed } from 'vue';
// 核心修复：修改为正确的 3 层向上物理路径，完美解析并彻底解决 Failed to resolve import 报错！
import { defHttp } from '../../../utils/http/axios';

const books = ref<any[]>([]);
const columns = [
  { title: '图书UUID索引', dataIndex: 'id', key: 'id', width: '25%' },
  { title: '图书名称', dataIndex: 'bookName', key: 'bookName' },
  { title: '国际ISBN编码', dataIndex: 'isbn', key: 'isbn' },
  { title: '著者', dataIndex: 'author', key: 'author' },
  { title: '物理库存', dataIndex: 'stock', key: 'stock' },
  { title: '流通状态', key: 'status' }
];

const totalStock = computed(() => {
  return books.value.reduce((sum, item) => sum + (item.stock || 0), 0);
});

const loadBooks = async () => {
  try {
    const res = await defHttp.get({ url: '/library/books/list' });
    books.value = res || [];
  } catch (err) {
    console.error('明细页数据加载异常');
  }
};

onMounted(() => {
  loadBooks();
});
</script>