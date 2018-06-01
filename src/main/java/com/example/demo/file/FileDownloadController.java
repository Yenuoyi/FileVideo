package com.example.demo.file;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Controller
public class FileDownloadController {
    @GetMapping("/fileDownload")
    public void fileDownload(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        String ContentRange = httpServletRequest.getHeader("Content-Range");
        long rangeStart = 0;
        if(ContentRange!=null){
            rangeStart = Long.parseLong(ContentRange);
        }

        //response响应输出流
        ServletOutputStream servletOutputStream  = httpServletResponse.getOutputStream();
        //响应文件流准备
        File file = new File("L:\\开发软件\\ideaIU-2017.3.5.exe");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileChannel = fileInputStream.getChannel();
        httpServletResponse.addHeader("Content-Range","byte "+rangeStart+"-"+file.length()+"/"+file.length());
        httpServletResponse.addHeader("Content-Disposition", "attachment;filename=\"ideaIU-2017.3.5.exe\"");
        httpServletResponse.addHeader("Content-Length",file.length()+"");
        httpServletResponse.setContentType("application/octet-stream");
        //默认值，表示回复中的消息体会以页面的一部分或者整个页面的形式展示
        httpServletResponse.addHeader("Content-Disposition", "inline");
        //设置rangStart位置
        fileChannel.position(rangeStart);
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
        fileInputStream.close();
        servletOutputStream.close();
    }
}
