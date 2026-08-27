package com.zzs.zzsadmin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzs.zzsadmin.entity.Dictionary;
import com.zzs.zzsadmin.service.IDictionaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (Dictionary)表控制层
 *
 * @author makejava
 * @since 2022-12-22 11:12:59
 */
@RestController
@RequestMapping("dictionary")
public class DictionaryController {
//    /**
//     * 服务对象
//     */
//    @Resource
//    private IDictionaryService dictionaryService;
//
//    /**
//     * 分页查询
//     *
//     * @param dictionary 筛选条件
//     * @param pageRequest      分页对象
//     * @return 查询结果
//     */
//    @GetMapping
//    public ResponseEntity<Page<Dictionary>> queryByPage(Dictionary dictionary, PageRequest pageRequest) {
//        return ResponseEntity.ok(this.dictionaryService.queryByPage(dictionary, pageRequest));
//    }
//
//    /**
//     * 通过主键查询单条数据
//     *
//     * @param id 主键
//     * @return 单条数据
//     */
//    @GetMapping("{id}")
//    public ResponseEntity<Dictionary> queryById(@PathVariable("id") String id) {
//        return ResponseEntity.ok(this.dictionaryService.queryById(id));
//    }
//
//    /**
//     * 新增数据
//     *
//     * @param dictionary 实体
//     * @return 新增结果
//     */
//    @PostMapping
//    public ResponseEntity<Dictionary> add(Dictionary dictionary) {
//        return ResponseEntity.ok(this.dictionaryService.insert(dictionary));
//    }
//
//    /**
//     * 编辑数据
//     *
//     * @param dictionary 实体
//     * @return 编辑结果
//     */
//    @PutMapping
//    public ResponseEntity<Dictionary> edit(Dictionary dictionary) {
//        return ResponseEntity.ok(this.dictionaryService.update(dictionary));
//    }
//
//    /**
//     * 删除数据
//     *
//     * @param id 主键
//     * @return 删除是否成功
//     */
//    @DeleteMapping
//    public ResponseEntity<Boolean> deleteById(String id) {
//        return ResponseEntity.ok(this.dictionaryService.deleteById(id));
//    }

}

