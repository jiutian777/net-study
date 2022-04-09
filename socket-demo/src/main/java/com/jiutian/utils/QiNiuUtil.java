package com.jiutian.utils;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

/**
 * @Date: 2022/3/12 13:04
 * @Author: jiutian
 * @Description: 七牛云工具类
 */
public class QiNiuUtil {

    //设置需要操作的账号的AK和SK
    private static final String ACCESS_KEY = "Ns6G1Kk_usMIY5HHCkx2NlrIfZHZOrQGtvMpMxHl";
    private static final String SECRET_KEY = "AN4B9CetKQbZIszY1mYVRiZWRmapVT3FcNJ-n-2p";

    //要上传的空间
    private static final String bucketName = "jiutian-yywd";

    //密钥
    private static final Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

    //普通上传
    public Boolean upload(String filePath, String fileName) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        //创建上传对象
        UploadManager uploadManager = new UploadManager(cfg);
        try {
            //调用 put方法上传
            Response res = uploadManager.put(filePath, fileName, auth.uploadToken(bucketName));
            //打印返回的信息
            System.out.println(res.bodyString());
            return true;
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            // System.out.println(r.toString());
            try {
                //响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }
        return false;
    }
}
