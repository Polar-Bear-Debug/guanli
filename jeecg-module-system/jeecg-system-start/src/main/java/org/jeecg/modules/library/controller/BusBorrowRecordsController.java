// 物理路径：D:\JeecgBoot-main\jeecg-boot\jeecg-module-system\jeecg-system-start\src\main\java\org\jeecg\modules\library\controller\BusBorrowRecordsController.java
package org.jeecg.modules.library.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.library.entity.BusBorrowRecords;
import org.jeecg.modules.library.service.IBusBorrowRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/library/borrow")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BusBorrowRecordsController {

    @Autowired
    private IBusBorrowRecordsService busBorrowRecordsService;

    @GetMapping("/list")
    public Result<List<BusBorrowRecords>> queryList() {
        return Result.OK(busBorrowRecordsService.list());
    }

    @PostMapping("/apply")
    public Result<String> apply(@RequestBody BusBorrowRecords record) {
        try {
            boolean success = busBorrowRecordsService.applyBorrow(record);
            if (success) {
                return Result.OK("借阅工作流事务流转成功，MySQL已同步生成‘待审批’申请单！");
            }
            return Result.error("借阅流转申请失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 核心增加：支持前台审批控制动态修改单据在 MySQL 里的流转状态
     */
    @PutMapping("/edit")
    public Result<String> edit(@RequestBody BusBorrowRecords record) {
        boolean success = busBorrowRecordsService.updateById(record);
        if (success) {
            return Result.OK("单据状态已成功在 MySQL 数据库中变更！");
        }
        return Result.error("单据流转状态修改失败");
    }
}