package com.atsu.tabletennisreservation.controller;

import com.atsu.tabletennisreservation.dto.Result;
import com.atsu.tabletennisreservation.propertyBean.UploadFilePropertyBean;
import com.atsu.tabletennisreservation.service.CommonService;
import com.atsu.tabletennisreservation.utils.FileUploadUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@ResponseBody
public class FileUploadController {
    @Resource
    private CommonService commonService;
    @PostMapping("/public/upload")
    @ResponseBody
    public Result upload(MultipartFile uploadFile, HttpServletRequest req){
        Map<String, String> map = commonService.uploadFile(uploadFile, req);
        return Result.success(map,"请求成功");
    }
}
