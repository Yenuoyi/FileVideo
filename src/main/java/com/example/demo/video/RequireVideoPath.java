package com.example.demo.video;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.File;

@RestController
public class RequireVideoPath {
    /**
     * 获取文件夹下所有文件
     * @return
     */
    @GetMapping("/VideoPath")
    public static String requireVideoPath(){
        String path = null;
        String[] paths = null;
        //使用枚举代替常量
        File rootFile = new File(VideoPath.ONE.getName());
        File[] files = rootFile.listFiles();
        paths = new String[files.length];
        for(int i =0;i<files.length;i++){
            File childrenFile = files[i];
            System.out.println(childrenFile.getPath());
            paths[i] = childrenFile.getName();
        }
        System.out.println(JSONObject.toJSONString(paths));
        path = JSONObject.toJSONString(paths);
        return path;
    }
    public static void main(String[] args){
        requireVideoPath();
    }
}
