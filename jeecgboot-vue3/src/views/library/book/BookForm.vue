<template>
  <div class="p-8 bg-white rounded-lg shadow-sm max-w-2xl mx-auto my-6 border border-gray-100">
    <div class="mb-8 text-center">
      <h2 class="text-2xl font-bold text-gray-800">📖 录入新书入库档案</h2>
      <p class="text-xs text-gray-400 mt-2">在系统中创建独立的图书元数据实体，并配置其初始物理库存</p>
    </div>

    <a-form :model="formState" layout="vertical" class="px-4">
      <a-form-item label="图书名称" required>
        <a-input v-model:value="formState.bookName" placeholder="请填写规范的图书全名" size="large" />
      </a-form-item>

      <a-form-item label="ISBN 国际书号" required>
        <a-input v-model:value="formState.isbn" placeholder="请输入13位标准条形码ISBN号" size="large" />
      </a-form-item>

      <a-form-item label="著作者/译者" required>
        <a-input v-model:value="formState.author" placeholder="请输入主编、著者或团队名称" size="large" />
      </a-form-item>

      <a-form-item label="初始在馆物理库存量" required>
        <a-input-number v-model:value="formState.stock" :min="1" :max="1000" class="w-full" size="large" />
      </a-form-item>

      <div class="mt-8 flex justify-center space-x-4">
        <a-button size="large" @click="resetForm" class="w-32">重置表单</a-button>
        <a-button type="primary" size="large" :loading="submitLoading" @click="handleSubmit" class="w-48">
          确认录入并提交落盘
        </a-button>
      </div>
    </a-form>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref } from 'vue';
import { message } from 'ant-design-vue';
// 核心修复：修改为正确的 3 层向上物理路径，完美解析并彻底解决 Failed to resolve import 报错！
import { defHttp } from '../../../utils/http/axios';

const submitLoading = ref<boolean>(false);
const formState = reactive({
  bookName: '',
  isbn: '',
  author: '',
  stock: 10
});

const resetForm = () => {
  formState.bookName = '';
  formState.isbn = '';
  formState.author = '';
  formState.stock = 10;
};

const handleSubmit = async () => {
  if (!formState.bookName || !formState.isbn || !formState.author) {
    message.warning('请将带星号的必填字段填写完整！');
    return;
  }
  submitLoading.value = true;
  try {
    await defHttp.post({ url: '/library/books/add', params: formState });
    message.success('新图书档案创建成功，数据已安全写入 MySQL 数据库！');
    resetForm();
  } catch (err) {
    message.error('提交失败，请检查后端运行状态和数据库连接！');
  } finally {
    submitLoading.value = false;
  }
};
</script>