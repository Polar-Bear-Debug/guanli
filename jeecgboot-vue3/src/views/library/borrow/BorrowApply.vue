<template>
  <div class="p-6 bg-white rounded-lg shadow-sm max-w-lg mx-auto my-8 border border-gray-100">
    <div class="text-center mb-8">
      <h2 class="text-2xl font-bold text-blue-600">⚡ 智能借阅事务工作流</h2>
      <p class="text-xs text-gray-400 mt-2">发起借阅流转申请，系统将自动校验借阅规则并异步扣减图书库存</p>
    </div>

    <!-- 发起流转表单 -->
    <a-form :model="borrowForm" layout="vertical" @submit.prevent="submitBorrow" class="px-4">
      <!-- 第一步：选择图书 -->
      <a-form-item label="第一步：检索并勾选目标借阅图书" required class="mb-6">
        <a-select 
          v-model:value="borrowForm.bookId" 
          placeholder="请选择馆内当前在库的图书" 
          :loading="booksLoading"
          size="large"
          class="w-full"
        >
          <a-select-option v-for="book in books" :key="book.id" :value="book.id">
            {{ book.bookName }} (馆藏剩余: {{ book.stock }} 本)
          </a-select-option>
        </a-select>
        <div v-if="books.length === 0 && !booksLoading" class="text-xs text-orange-500 mt-1">
          ⚠️ 当前暂无在库图书，请先在图书页面录入新书并补充库存。
        </div>
      </a-form-item>

      <!-- 第二步：手动输入卡号 -->
      <a-form-item label="第二步：指定发起借阅的读者会员 (直接输入学号/扫码枪扫入)" required class="mb-8">
        <a-input 
          v-model:value="borrowForm.readerId" 
          placeholder="请直接输入读者学号或借书证卡号" 
          size="large"
          class="w-full"
        />
        <div class="text-xs text-gray-400 mt-1">
          💡 提示：支持物理扫码枪直接扫码录入证号，实现秒级流转。
        </div>
      </a-form-item>

      <!-- 提交工作流控制 -->
      <div class="text-center">
        <a-button 
          type="primary" 
          html-type="submit" 
          :loading="submitting" 
          size="large" 
          class="w-full py-6 flex items-center justify-center font-bold text-lg rounded-md shadow-md"
        >
          确认流转申请并出库
        </a-button>
      </div>
    </a-form>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, reactive } from 'vue';
import { message } from 'ant-design-vue';
// 核心修复：修改为正确的 3 层向上物理路径，解决 Failed to resolve import 报错！
import { defHttp } from '../../../utils/http/axios';

const books = ref<any[]>([]);
const booksLoading = ref<boolean>(false);
const submitting = ref<boolean>(false);

const borrowForm = reactive({ bookId: undefined, readerId: '' });

const loadFormOptions = async () => {
  booksLoading.value = true;
  try {
    const bookRes = await defHttp.get({ url: '/library/books/list' });
    if (bookRes) {
      books.value = bookRes.filter((b: any) => b.stock > 0);
    }
  } catch (err) {
    message.error('流转组件加载图书数据异常，请确认后端 8080 服务已启动！');
  } finally {
    booksLoading.value = false;
  }
};

const submitBorrow = async () => {
  if (!borrowForm.bookId || !borrowForm.readerId) {
    message.warning('请确保已选定目标图书并填写了读者卡号/学号！');
    return;
  }
  submitting.value = true;
  try {
    await defHttp.post({ url: '/library/borrow/apply', params: borrowForm });
    message.success('流转成功！已向审批队列提交申请并异步预扣物理库存！');
    borrowForm.bookId = undefined;
    borrowForm.readerId = '';
    await loadFormOptions();
  } catch (err) {
    message.error('流转失败，馆藏可用物理库存可能已为空，或网络连接故障！');
  } finally {
    submitting.value = false;
  }
};

onMounted(() => {
  loadFormOptions();
});
</script>