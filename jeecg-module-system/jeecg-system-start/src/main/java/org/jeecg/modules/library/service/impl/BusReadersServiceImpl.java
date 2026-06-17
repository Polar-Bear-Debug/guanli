package org.jeecg.modules.library.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.library.entity.BusReaders;
import org.jeecg.modules.library.mapper.BusReadersMapper;
import org.jeecg.modules.library.service.IBusReadersService;
import org.springframework.stereotype.Service;

@Service
public class BusReadersServiceImpl extends ServiceImpl<BusReadersMapper, BusReaders> implements IBusReadersService {
}