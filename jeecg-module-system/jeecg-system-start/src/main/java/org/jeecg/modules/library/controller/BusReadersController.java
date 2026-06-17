package org.jeecg.modules.library.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.library.entity.BusReaders;
import org.jeecg.modules.library.service.IBusReadersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/library/readers")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BusReadersController {

    @Autowired
    private IBusReadersService busReadersService;

    @GetMapping("/list")
    public Result<List<BusReaders>> queryList() {
        return Result.OK(busReadersService.list());
    }

    @PostMapping("/add")
    public Result<String> add(@RequestBody BusReaders busReaders) {
        busReadersService.save(busReaders);
        return Result.OK("读者会员卡开户成功！");
    }

    @DeleteMapping("/delete")
    public Result<String> delete(@RequestParam(name="id") String id) {
        busReadersService.removeById(id);
        return Result.OK("读者账号销户注销成功！");
    }
}