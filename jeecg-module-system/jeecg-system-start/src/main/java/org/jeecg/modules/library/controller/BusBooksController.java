package org.jeecg.modules.library.controller;

import org.jeecg.common.api.vo.Result; // 100% 避坑：Jeecg 统一 Result 真实的包路径在 .vo，非 .dto
import org.jeecg.modules.library.entity.BusBooks;
import org.jeecg.modules.library.service.IBusBooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/library/books")
@CrossOrigin(origins = "*", allowedHeaders = "*") // 核心直连提权
public class BusBooksController {

    @Autowired
    private IBusBooksService busBooksService;

    @GetMapping("/list")
    public Result<List<BusBooks>> queryList() {
        return Result.OK(busBooksService.list());
    }

    @PostMapping("/add")
    public Result<String> add(@RequestBody BusBooks busBooks) {
        busBooksService.save(busBooks);
        return Result.OK("新图书档案初始化入库落盘成功！");
    }

    @PutMapping("/edit")
    public Result<String> edit(@RequestBody BusBooks busBooks) {
        busBooksService.updateById(busBooks);
        return Result.OK("图书档案信息更新成功！");
    }

    @DeleteMapping("/delete")
    public Result<String> delete(@RequestParam(name="id") String id) {
        busBooksService.removeById(id);
        return Result.OK("图书档案物理注销下架成功！");
    }
}