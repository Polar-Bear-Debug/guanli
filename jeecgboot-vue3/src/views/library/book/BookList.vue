<template>
  <div class="p-6 bg-white rounded-lg shadow-sm">
    <div class="mb-6 flex justify-between items-center border-b pb-4">
      <div>
        <h2 class="text-xl font-bold text-gray-800">📚 图书档案管理</h2>
        <p class="text-xs text-gray-400 mt-1">管理馆内图书档案，支持新书登记、信息修改及下架注销</p>
      </div>
      <a-button type="primary" size="large" @click="showAddModal">+ 新增图书入库</a-button>
    </div>

    <a-table :dataSource="booksList" :columns="columns" rowKey="id" :loading="loading" :pagination="{ pageSize: 5 }">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'stock'">
          <a-tag :color="record.stock > 3 ? 'green' : 'orange'">{{ record.stock }} 本</a-tag>
        </template>
        <template v-if="column.key === 'action'">
          <a-space size="middle">
            <a @click="showEditModal(record)">编辑</a>
            <a-popconfirm title="确定要物理下架该图书吗?" okText="确定" cancelText="取消" @confirm="handleDelete(record.id)">
              <a class="text-red-500">下架</a>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>

    <a-modal v-model:visible="visible" :title="modalTitle" @ok="handleOk" :confirmLoading="submitLoading">
      <a-form :model="formState" layout="vertical" class="mt-4">
        <a-form-item label="图书名称" required>
          <a-input v-model:value="formState.bookName" />
        </a-form-item>
        <a-form-item label="ISBN 书号" required>
          <a-input v-model:value="formState.isbn" />
        </a-form-item>
        <a-form-item label="图书作者" required>
          <a-input v-model:value="formState.author" />
        </a-form-item>
        <a-form-item label="初始可用库存量" required>
          <a-input-number v-model:value="formState.stock" class="w-full" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { defHttp } from '../../../utils/http/axios';

interface BookFormState { id: string; bookName: string; isbn: string; author: string; stock: number; }

const columns = [
  { title: '图书名称', dataIndex: 'bookName', key: 'bookName' },
  { title: 'ISBN书号', dataIndex: 'isbn', key: 'isbn' },
  { title: '著者', dataIndex: 'author', key: 'author' },
  { title: '可用库存', dataIndex: 'stock', key: 'stock' },
  { title: '操作选项', key: 'action' }
];

const booksList = ref<any[]>([]);
const loading = ref<boolean>(false);
const visible = ref<boolean>(false);
const submitLoading = ref<boolean>(false);
const modalTitle = ref<string>('新增图书入库');
const formState = reactive<BookFormState>({ id: '', bookName: '', isbn: '', author: '', stock: 10 });

const fetchBooks = async () => {
  loading.value = true;
  try {
    const res = await defHttp.get({ url: '/library/books/list' });
    booksList.value = res || [];
  } catch (err) {
    message.error('加载图书列表失败，请检查后端运行状态！');
  } finally {
    loading.value = false;
  }
};

const showAddModal = () => {
  modalTitle.value = '新书初始化入库';
  formState.id = '';
  formState.bookName = '';
  formState.isbn = '';
  formState.author = '';
  formState.stock = 5;
  visible.value = true;
};

const showEditModal = (record: any) => {
  modalTitle.value = '修改图书档案';
  Object.assign(formState, record);
  visible.value = true;
};

const handleOk = async () => {
  if (!formState.bookName || !formState.isbn || !formState.author) {
    message.warning('请补全表单必填项！');
    return;
  }
  submitLoading.value = true;
  try {
    const isEdit = !!formState.id;
    const url = isEdit ? '/library/books/edit' : '/library/books/add';
    if (isEdit) {
      await defHttp.put({ url, params: formState });
    } else {
      await defHttp.post({ url, params: formState });
    }
    visible.value = false;
    await fetchBooks();
    message.success('图书信息已同步保存！');
  } catch (err) {
    message.error('操作未成功');
  } finally {
    submitLoading.value = false;
  }
};

const handleDelete = async (id: string) => {
  try {
    await defHttp.delete({ url: `/library/books/delete?id=${id}` });
    message.success('图书下架成功！');
    await fetchBooks();
  } catch (err) {
    message.error('请求未响应');
  }
};

onMounted(() => {
  fetchBooks();
});
</script>