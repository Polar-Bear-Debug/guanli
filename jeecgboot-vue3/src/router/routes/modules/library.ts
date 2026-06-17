import type { AppRouteModule } from '/@/router/types';
import { LAYOUT } from '/@/router/constant';

const library: AppRouteModule = {
  path: '/library',
  name: 'Library',
  component: LAYOUT,
  redirect: '/library/books',
  meta: {
    orderNo: 100,
    icon: 'ion:book-outline',
    title: '图书借阅管理',
  },
  children: [
    {
      path: 'books',
      name: 'BookList',
      component: () => import('/@/views/library/book/BookList.vue'),
      meta: {
        title: '图书档案管理',
      },
    },
    {
      path: 'books/add',
      name: 'BookForm',
      component: () => import('/@/views/library/book/BookForm.vue'),
      meta: {
        title: '新书入库',
      },
    },
    {
      path: 'books/detail',
      name: 'BookDetail',
      component: () => import('/@/views/library/book/BookDetail.vue'),
      meta: {
        title: '馆藏明细看板',
      },
    },
    {
      path: 'borrow/apply',
      name: 'BorrowApply',
      component: () => import('/@/views/library/borrow/BorrowApply.vue'),
      meta: {
        title: '发起借阅',
      },
    },
    {
      path: 'borrow/audit',
      name: 'BorrowAudit',
      component: () => import('/@/views/library/borrow/BorrowAudit.vue'),
      meta: {
        title: '借阅审批',
      },
    },
    {
      path: 'borrow/history',
      name: 'HistoryRecords',
      component: () => import('/@/views/library/borrow/HistoryRecords.vue'),
      meta: {
        title: '历史借阅记录',
      },
    },
    {
      path: 'borrow/overdue',
      name: 'OverdueBill',
      component: () => import('/@/views/library/borrow/OverdueBill.vue'),
      meta: {
        title: '逾期罚款账单',
      },
    },
    {
      path: 'borrow/notice',
      name: 'NoticeConfig',
      component: () => import('/@/views/library/borrow/NoticeConfig.vue'),
      meta: {
        title: '催缴通知配置',
      },
    },
    {
      path: 'readers',
      name: 'ReaderList',
      component: () => import('/@/views/library/reader/ReaderList.vue'),
      meta: {
        title: '读者花名册',
      },
    },
    {
      path: 'readers/level',
      name: 'ReaderLevel',
      component: () => import('/@/views/library/reader/ReaderLevel.vue'),
      meta: {
        title: '会员权限分级',
      },
    },
  ],
};

export default library;
