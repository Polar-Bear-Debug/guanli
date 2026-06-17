<!-- 物理路径：D:\JeecgBoot-main\jeecgboot-vue3\src\views\library\reader\ReaderLevel.vue -->
<template>
  <div class="p-6 bg-white rounded-lg shadow-sm">
    <div class="mb-6 border-b pb-4">
      <h2 class="text-xl font-bold text-gray-800">🛡️ 会员借阅权限分级配置</h2>
      <p class="text-xs text-gray-400 mt-1">设置不同会员级别的可借册数、单次借阅天数以及逾期费率比例</p>
    </div>

    <a-table :dataSource="levelData" :columns="columns" rowKey="level">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'level'">
          <a-tag :color="record.level === 2 ? 'gold' : 'blue'">
            {{ record.levelName }}
          </a-tag>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';

const columns = [
  { title: '会员信用级别', key: 'level' },
  { title: '单次最大借阅册数', dataIndex: 'maxBooks', key: 'maxBooks' },
  { title: '单次最长借阅期限(天)', dataIndex: 'maxDays', key: 'maxDays' },
  { title: '逾期每日罚款比率', dataIndex: 'rate', key: 'rate' }
];

const levelData = ref([
  { level: 1, levelName: '普通读者', maxBooks: '3 本', maxDays: '30 天', rate: '0.5 元/天' },
  { level: 2, levelName: '高级会员', maxBooks: '10 本', maxDays: '90 天', rate: '0.1 元/天' }
]);
</script>