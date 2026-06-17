// 物理路径：D:\JeecgBoot-main\jeecg-boot\jeecg-module-system\jeecg-system-start\src\main\java\org\jeecg\modules\library\service\impl\BusBorrowRecordsServiceImpl.java
package org.jeecg.modules.library.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.library.entity.BusBorrowRecords;
import org.jeecg.modules.library.mapper.BusBorrowRecordsMapper;
import org.jeecg.modules.library.service.IBusBorrowRecordsService;
import org.jeecg.modules.library.service.IBusBooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

@Service
public class BusBorrowRecordsServiceImpl extends ServiceImpl<BusBorrowRecordsMapper, BusBorrowRecords> implements IBusBorrowRecordsService {

    @Autowired
    private IBusBooksService busBooksService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean applyBorrow(BusBorrowRecords record) {
        // 第一步：级联扣减可用在库物理库存 1 本，防超卖
        boolean hasStock = busBooksService.updateBookStock(record.getBookId(), -1);
        if (!hasStock) {
            throw new RuntimeException("库存流转异常：当前馆藏实物可用库存不足！");
        }
        record.setBorrowTime(new Date());
        // 核心改动：将初始状态设为 待审批，供审批页面动态拉取，形成闭环
        record.setStatus("待审批");
        return this.save(record);
    }
}