package org.jeecg.modules.library.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.library.entity.BusBooks;
import org.jeecg.modules.library.mapper.BusBooksMapper;
import org.jeecg.modules.library.service.IBusBooksService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BusBooksServiceImpl extends ServiceImpl<BusBooksMapper, BusBooks> implements IBusBooksService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBookStock(String bookId, int count) {
        BusBooks book = this.getById(bookId);
        if (book == null) {
            return false;
        }
        int newStock = book.getStock() + count;
        if (newStock < 0) {
            return false; // 库存保护熔断
        }
        book.setStock(newStock);
        return this.updateById(book);
    }
}