package com.jiutian.controller;

import com.jiutian.constant.QiNiuConstant;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Date: 2022/3/22 9:55
 * @Author: jiutian
 * @Description:
 */
@Controller
public class QiNiuUpload {
    private static final String ACCESS_KEY = "Ns6G1Kk_usMIY5HHCkx2NlrIfZHZOrQGtvMpMxHl";
    private static final String SECRET_KEY = "AN4B9CetKQbZIszY1mYVRiZWRmapVT3FcNJ-n-2p";

    //要上传的空间
    private static final String BUCKET_NAME = "jiutian-cdn";

    //密钥
    private static final Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

    /**
     * 七牛云上传生成凭证
     */
    @PostMapping("/qiNiuUpToken")
    @ResponseBody
    public Map<String, Object> qiNiuUpToken(@RequestParam String suffix) {
        Map<String, Object> result = new HashMap<>(16);
        try {
            //生成凭证
            String upToken = auth.uploadToken(BUCKET_NAME);
            result.put("token", upToken);
            //存入外链默认域名，用于拼接完整的资源外链路径
            result.put("domain", QiNiuConstant.QI_NIU_DOMAIN);

            //生成实际路径名
            String randomFileName = UUID.randomUUID().toString().replace("-", "") + "." + suffix;
            String realImgUrl = QiNiuConstant.QI_NIU_DIRECTORY + randomFileName;
            result.put("imgUrl", realImgUrl);
            result.put("code", 200);
        } catch (Exception e) {
            result.put("message", "获取凭证失败，" + e.getMessage());
            result.put("code", -1);
        }
        return result;
    }
}

