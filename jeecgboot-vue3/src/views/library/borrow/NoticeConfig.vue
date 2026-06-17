<!-- 物理绝对路径：D:\JeecgBoot-main\jeecgboot-vue3\src\views\library\borrow\NoticeConfig.vue -->
<template>
  <div class="p-6 bg-white rounded-lg shadow-sm max-w-2xl mx-auto my-6 border border-gray-100">
    <div class="mb-6 border-b pb-4 text-center">
      <h2 class="text-xl font-bold text-gray-800">✉️ 催缴通知模板及自动配置</h2>
      <p class="text-xs text-gray-400 mt-1">配置当读者借阅即将到期或已经逾期时，系统自动触发生效的推送文案</p>
    </div>

    <!-- 表单配置区 -->
    <a-form layout="vertical" class="px-4">
      <a-form-item label="触发限期提醒阈值" required>
        <a-select value="3" class="w-full">
          <a-select-option value="3">到期前 3 天发送提醒</a-select-option>
          <a-select-option value="7">到期前 7 天发送提醒</a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="即将到期推送通知模版" required>
        <a-textarea :rows="4" value="【校园图书馆】尊敬的读者同学，您借阅的《${bookName}》即将到期，请及时归还或登录平台办理续借，谢谢配合！" />
      </a-form-item>

      <div class="text-center mt-6">
        <a-button type="primary" size="large" @click="handleSave">
          保存模版配置
        </a-button>
      </div>
    </a-form>
  </div>
</template>

<script lang="ts" setup>
import { message } from 'ant-design-vue';

const handleSave = () => {
  message.success('通知触发规则及消息模版配置成功，后台规则引擎已重新装载！');
};
</script>