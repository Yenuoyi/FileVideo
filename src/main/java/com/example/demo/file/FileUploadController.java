package com.example.demo.file;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Controller
public class FileUploadController {
    /**
     * @param file
     */
    @PostMapping("/fileUpload")
    @ResponseBody
    public void fileUpload(@RequestPart(value = "file") MultipartFile file,int index, String name){
        File fileDirectory = new File(FilePath.ONE.getName()+name);
        //创建文件夹存储分片后的文件
        if(!fileDirectory.exists()){
            fileDirectory.mkdirs();
        }
        //创建分片文件
        File localFile = new File(fileDirectory.getAbsolutePath()+"\\"+index);
        try {
            file.transferTo(localFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 合并文件
     * @param totalSlices
     * @param name
     * @return
     * @throws Exception
     */
    @PostMapping("/mergeFile")
    @ResponseBody
    public int mergeFile( int totalSlices, String name) throws Exception{
        int result = 1;
        //合并后的文件绝对路径
        File mergeFile = new File(FilePath.TWO.getName()+name);
        //创建合并后的文件
        mergeFile.createNewFile();
        //文件输出流
        FileOutputStream fileOutputStream = new FileOutputStream(mergeFile);
        for (int i = 0;i<totalSlices;i++){
            File file = new File(FilePath.ONE.getName()+name+"\\"+i);
            FileInputStream fileInputStream = new FileInputStream(file);
            FileChannel fileChannel = fileInputStream.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024*1024);
            byte[] bytes = new byte[1024*1024];
            int j = fileChannel.read(byteBuffer);
            // 重设buffer，将limit设置为position，position设置为0
            byteBuffer.flip();
            while(j!=-1){
                if(byteBuffer.remaining()!=1024*1024){
                    bytes = new byte[byteBuffer.remaining()];
                }
                byteBuffer.get(bytes);
                byteBuffer.clear();
                fileOutputStream.write(bytes);
                j = fileChannel.read(byteBuffer);
                byteBuffer.flip();
            }
            //必须先关闭流再去删除文件
            fileInputStream.close();
            file.delete();
        }
        //保证目录下的文件都已删除才能删除成功
        File fileDirectory = new File(FilePath.ONE.getName()+name);
        fileDirectory.delete();
        fileOutputStream.close();
        return result;
    }
}
