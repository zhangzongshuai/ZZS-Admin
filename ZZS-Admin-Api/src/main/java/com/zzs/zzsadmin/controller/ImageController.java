package com.zzs.zzsadmin.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.zzs.zzsadmin.annotation.IgnoreToken;
import com.zzs.zzsadmin.common.vo.BaseResultData;
import com.zzs.zzsadmin.common.vo.ResultData;
import com.zzs.zzsadmin.vo.menu.MenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p></p>
 *
 * @author zzs
 * @date 2022-11-14 19:18:04
 */
@RestController
@RequestMapping("/api")
@Api(tags = "图床管理")
public class ImageController {
    @IgnoreToken
    @ApiOperation("上传图片")
    @PostMapping("/importImage")
    public ResultData<String> ImportImage(MultipartFile file){
        ResultData<String> res = new ResultData<>();
        String fullName = file.getOriginalFilename();
        String fileExt = fullName.split("\\.")[1];
        String filename = DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss");
        String rootFilePath = "C:\\Users\\Administrator\\Pictures\\image\\" + filename + "." + fileExt;
        try {
            FileUtil.writeBytes(file.getBytes(), rootFilePath);
        } catch (IOException e) {
            res.setErrcode(1);
            res.setErrmsg("导入失败!");
            res.setDetail_msg(e.getMessage());
            e.printStackTrace();
            return res;
        }
        res.setData(filename + "." + fileExt);
        return res;
    }
}
