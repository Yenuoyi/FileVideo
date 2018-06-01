package com.example.demo.video;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Controller
public class VideoDownload {
    @GetMapping("/videoDownload/{id}")
    public void fileDownload(@PathVariable("id") String id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        //响应文件流准备
        File file = new File(VideoPath.ONE.getName()+id);
        FileInputStream fileInputStream = null;
        //response响应输出流
        ServletOutputStream servletOutputStream = null;
        //响应Http信息头准备
        httpServletResponse.addHeader("Content-Disposition", "attachment;filename=\""+file.getName()+"\"");
        httpServletResponse.addHeader("Content-Length",file.length()+"");
        httpServletResponse.setContentType("application/octet-stream");
        try{
            //输出流信息准备
            servletOutputStream = httpServletResponse.getOutputStream();
            //文件流准备，打开NIO流管道
            fileInputStream = new FileInputStream(file);
            //调用方法
            servletOutput(fileInputStream, servletOutputStream);
        }catch (Exception e){
            System.out.println("servletOutputStream异常！");
        }finally{
            try {
                servletOutputStream.close();
                fileInputStream.close();
            } catch (IOException e) {
                System.out.println("文件流关闭异常！");
            }
        }
    }

    public void servletOutput(FileInputStream fileInputStream, ServletOutputStream servletOutputStream){
        FileChannel fileChannel = fileInputStream.getChannel();
        //设置rangStart位置
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024*1024);
            byte[] bytes = new byte[1024*1024];
            int i = fileChannel.read(byteBuffer);
            byteBuffer.flip();
            while(i!=-1){
                //如果缓冲区剩下字节数不足1024*1024个字节，则重新创建数组对象
                if(byteBuffer.remaining()!=1024*1024){
                    bytes = new byte[byteBuffer.remaining()];
                }
                byteBuffer.get(bytes);
                servletOutputStream.write(bytes);
                byteBuffer.clear();
                i = fileChannel.read(byteBuffer);
                byteBuffer.flip();
            }
            servletOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
