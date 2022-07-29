package com.zzs.zzsadmin.controller;

import com.zzs.zzsadmin.annotation.IgnoreToken;
import com.zzs.zzsadmin.common.exception.ArgumentException;
import com.zzs.zzsadmin.common.exception.MessageException;
import com.zzs.zzsadmin.common.vo.BaseResultData;
import com.zzs.zzsadmin.common.vo.ResultData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "测试一下")
@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("rtdb/tags")
    @IgnoreToken
    public Map<String, Object> getMock(@RequestParam Long pageNum, @RequestParam Long pageSize, @RequestParam String tagServer, @RequestParam String tagName) {

        Map<String, Object> map = new HashMap<>();
        map.put("errcode", 0);
        map.put("errmsg", null);
        map.put("datas", null);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("TAGNAME", "4LH_J0AI1009.PV");
        map2.put("DESCRIPTION", "分析仪旁可燃气报警");

        List<Map<String, Object>> list = new ArrayList<>();
        list.add(map2);
        map.put("datas", list);

        map.put("total", 28593);

        return map;
    }

    @GetMapping("rtdb/servers")
    @IgnoreToken
    public Map<String, Object> getServers() {

        Map<String, Object> map = new HashMap<>();
        map.put("errcode", 0);
        map.put("errmsg", null);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("NAME", "JNRTDB");
        map2.put("DESCRIPTION", "济南数据库");

        List<Map<String, Object>> list = new ArrayList<>();
        list.add(map2);
        map.put("datas", list);
        return map;
    }


    @ApiOperation(value = "测试")
    @GetMapping("get")
    @IgnoreToken
    public BaseResultData GetResult() {
        return new ResultData<String>("hello,request success!");
    }


    @ApiOperation(value = "测试2")
    @GetMapping("/test")
    public BaseResultData getTest() {
        return new ResultData<String>("hello,Test!");
    }


    @ApiOperation(value = "测试错误信息")
    @IgnoreToken
    @GetMapping("/err")
    public void ErrTest(String errmsg) {
        if (StringUtils.isNotBlank(errmsg)) {
            throw new RuntimeException(errmsg);
        }
    }

}
