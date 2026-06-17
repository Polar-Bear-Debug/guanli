<template>
  <div class="p-6 bg-white rounded-lg shadow-sm">
    <div class="mb-6 flex justify-between items-center border-b pb-4">
      <div>
        <h2 class="text-xl font-bold text-gray-800">👥 读者会员管理</h2>
        <p class="text-xs text-gray-400 mt-1">配置全校师生借阅账户，管理读者信用等级和归还限期</p>
      </div>
      <a-button type="primary" size="large" @click="showAddModal">+ 注册新读者</a-button>
    </div>

    <a-table :dataSource="readersList" :columns="columns" rowKey="id" :loading="loading" :pagination="{ pageSize: 5 }">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'level'">
          <a-tag :color="record.level === 2 ? 'gold' : 'blue'">
            {{ record.level === 2 ? '🥇 高级会员' : '🥈 普通读者' }}
          </a-tag>
        </template>
        <template v-if="column.key === 'action'">
          <a-popconfirm title="确定要永久注销该读者吗?" okText="确定" cancelText="取消" @confirm="handleDelete(record.id)">
            <a class="text-red-500">注销借书证</a>
          </a-popconfirm>
        </template>
      </template>
    </a-table>

    <a-modal v-model:visible="visible" title="办理新借阅证 / 读者注册" @ok="handleOk" :confirmLoading="submitLoading">
      <a-form :model="formState" layout="vertical" class="mt-4">
        <a-form-item label="读者学号 / 教工卡号" required><a-input v-model:value="formState.readerCard" /></a-form-item>
        <a-form-item label="读者真实姓名" required><a-input v-model:value="formState.name" /></a-form-item>
        <a-form-item label="会员信用分级" required>
          <a-select v-model:value="formState.level">
            <a-select-option :value="1">普通读者</a-select-option>
            <a-select-option :value="2">高级会员</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, reactive } from 'vue';
import { message } from 'ant-design-vue';
// 核心修复：改用 defHttp 进行通信，会自动携带当前管理员的 X-Access-Token，100% 穿透 Shiro 安全框架拦截
import { defHttp } from '../../../utils/http/axios';

const columns = [
  { title: '借书证号 / 学号', dataIndex: 'readerCard', key: 'readerCard' },
  { title: '读者姓名', dataIndex: 'name', key: 'name' },
  { title: '借阅等级', key: 'level' },
  { title: '业务控制', key: 'action' }
];

const readersList = ref<any[]>([]);
const loading = ref<boolean>(false);
const visible = ref<boolean>(false);
const submitLoading = ref<boolean>(false);
const formState = reactive({ readerCard: '', name: '', level: 1 });

const fetchReaders = async () => {
  loading.value = true;
  try {
    const res = await defHttp.get({ url: '/library/readers/list' });
    readersList.value = res || [];
  } catch (err) {
    message.error('拉取读者列表故障，请确认后端 8080 服务是否开启！');
  } finally {
    loading.value = false;
  }
};

const showAddModal = () => {
  formState.readerCard = '';
  formState.name = '';
  formState.level = 1;
  visible.value = true;
};

const handleOk = async () => {
  if (!formState.readerCard || !formState.name) return;
  submitLoading.value = true;
  try {
    await defHttp.post({ url: '/library/readers/add', params: formState });
    visible.value = false;
    await fetchReaders();
    message.success('新借书卡注册开户成功，数据落盘 MySQL 成功！');
  } catch (err) {
    message.error('办卡注册失败，请确认控制台网络请求响应状态！');
  } finally {
    submitLoading.value = false;
  }
};

const handleDelete = async (id: string) => {
  try {
    await defHttp.delete({ url: `/library/readers/delete?id=${id}` });
    message.success('借书账户已被成功安全注销！');
    await fetchReaders();
  } catch (err) {
    message.error('销户操作未响应');
  }
};

onMounted(() => {
  fetchReaders();
});
</script>