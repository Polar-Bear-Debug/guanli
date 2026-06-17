package org.jeecg.modules.library.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.library.entity.BusBooks;

public interface IBusBooksService extends IService<BusBooks> {
    boolean updateBookStock(String bookId, int count);
}