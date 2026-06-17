<template>
  <div class="p-6 bg-white rounded-lg shadow-sm max-w-lg mx-auto my-8 border border-gray-100">
    <div class="text-center mb-8">
      <h2 class="text-2xl font-bold text-blue-600">⚡ 智能借阅事务工作流</h2>
      <p class="text-xs text-gray-400 mt-2">发起借阅流转申请，系统将自动校验借阅规则并异步扣减图书库存</p>
    </div>

    <a-form :model="borrowForm" layout="vertical" @submit.prevent="submitBorrow" class="px-4">
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

      <a-form-item label="第二步：指定发起借阅的读者会员 (直接输入学号/扫码枪扫入)" required class="mb-4">
        <a-input
          v-model:value="borrowForm.readerId"
          placeholder="请直接输入读者学号或借书证卡号"
          size="large"
          class="w-full"
          @blur="validateReader"
          allowClear
        />
        <div class="text-xs text-gray-400 mt-1">
          💡 提示：支持物理扫码枪直接扫码录入证号，实现秒级流转。
        </div>
      </a-form-item>

      <div v-if="readerCheckStatus === 'checking'" class="mb-6 p-3 bg-blue-50 border border-blue-200 rounded text-sm text-blue-600">
        🔍 正在验证读者身份...
      </div>
      <div v-else-if="readerCheckStatus === 'found'" class="mb-6 p-3 bg-green-50 border border-green-200 rounded">
        <div class="flex items-center justify-between">
          <div>
            <span class="text-sm font-bold text-green-700">✅ 读者已核实</span>
            <span class="ml-3 text-sm text-gray-600">姓名：{{ foundReader.name }}</span>
            <a-tag :color="foundReader.level === 2 ? 'gold' : 'blue'" class="ml-2">
              {{ foundReader.level === 2 ? '高级会员' : '普通读者' }}
            </a-tag>
          </div>
          <div class="text-xs text-gray-400">
            最大借阅 {{ foundReader.level === 2 ? '10' : '3' }} 本 / {{ foundReader.level === 2 ? '90' : '30' }} 天
          </div>
        </div>
      </div>
      <div v-else-if="readerCheckStatus === 'notfound'" class="mb-6 p-3 bg-red-50 border border-red-200 rounded text-sm text-red-600">
        ❌ 未找到该读者卡号，请先在「读者花名册」页面注册该读者！
      </div>
      <div v-else-if="readerCheckStatus === 'error'" class="mb-6 p-3 bg-orange-50 border border-orange-200 rounded text-sm text-orange-600">
        ⚠️ 读者校验接口异常，请确认后端服务已启动。
      </div>

      <div class="text-center">
        <a-button
          type="primary"
          html-type="submit"
          :loading="submitting"
          :disabled="readerCheckStatus !== 'found'"
          size="large"
          class="w-full py-6 flex items-center justify-center font-bold text-lg rounded-md shadow-md"
        >
          确认流转申请并出库
        </a-button>
        <div v-if="readerCheckStatus !== 'found' && borrowForm.readerId" class="text-xs text-gray-400 mt-2">
          请先输入正确的读者卡号完成身份核验
        </div>
      </div>
    </a-form>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { defHttp } from '../../../utils/http/axios';

const books = ref<any[]>([]);
const booksLoading = ref<boolean>(false);
const submitting = ref<boolean>(false);
const readerCheckStatus = ref<'idle' | 'checking' | 'found' | 'notfound' | 'error'>('idle');
const foundReader = ref<any>(null);

const borrowForm = reactive({ bookId: undefined as string | undefined, readerId: '' });

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

const validateReader = async () => {
  if (!borrowForm.readerId || borrowForm.readerId.trim() === '') {
    readerCheckStatus.value = 'idle';
    foundReader.value = null;
    return;
  }
  readerCheckStatus.value = 'checking';
  try {
    const reader = await defHttp.get({ url: '/library/readers/findByCard', params: { card: borrowForm.readerId.trim() } });
    if (reader) {
      foundReader.value = reader;
      readerCheckStatus.value = 'found';
    } else {
      readerCheckStatus.value = 'notfound';
      foundReader.value = null;
    }
  } catch (err) {
    readerCheckStatus.value = 'notfound';
    foundReader.value = null;
  }
};

const submitBorrow = async () => {
  if (!borrowForm.bookId) {
    message.warning('请先选择一本在库图书！');
    return;
  }
  if (!borrowForm.readerId) {
    message.warning('请输入读者卡号！');
    return;
  }
  if (readerCheckStatus.value !== 'found') {
    message.warning('请先通过读者身份核验（失焦输入框自动触发）！');
    return;
  }
  submitting.value = true;
  try {
    await defHttp.post({ url: '/library/borrow/apply', params: borrowForm });
    message.success('流转成功！已向审批队列提交申请并异步预扣物理库存！');
    borrowForm.bookId = undefined;
    borrowForm.readerId = '';
    readerCheckStatus.value = 'idle';
    foundReader.value = null;
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
