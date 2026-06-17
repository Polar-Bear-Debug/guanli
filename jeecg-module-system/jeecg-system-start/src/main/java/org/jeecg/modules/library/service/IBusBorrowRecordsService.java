package org.jeecg.modules.library.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.library.entity.BusBorrowRecords;

public interface IBusBorrowRecordsService extends IService<BusBorrowRecords> {
    boolean applyBorrow(BusBorrowRecords record);
}