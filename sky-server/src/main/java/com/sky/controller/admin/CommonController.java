package com.sky.controller.admin;

import com.aliyun.oss.AliOSSUtils;
import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api("通用接口")
@Slf4j
@RestController
@RequestMapping("/admin/common")
public class CommonController {

    /**
     * 文件上传
     * @param file
     * @return
     */
    @Autowired
    private AliOSSUtils aliOSSUtils;

    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传阿里云，文件名：{}",file.getOriginalFilename());

        try {
            String url = aliOSSUtils.upload(file);
            return Result.success(url);
        } catch (IOException e) {
            log.error("文件上传失败：{}", e);
        }

        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
