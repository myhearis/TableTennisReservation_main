package com.atsu.tabletennisreservation.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 文件上传工具类
 */
public class FileUploadUtil {

    //以时间作为文件夹
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");

    //文件存放的路径
    //public static String fileSavePath="D:/GraduationDesignProject/upload/";


    /**
     * 上传文件到指定路径中，
     * 例如 fileSavePath=E:/uploadFile/"
     * @param uploadFile uploadPahtFile
     * @param req   request
     * @return 返回readPath通过项目访问文件的路径（需要配置资源映射）和relativePath相对路径
     */
    public static Map<String,String> uploadPahtFile(MultipartFile uploadFile, HttpServletRequest req,String fileSavePath) {
        Map<String,String> pathMap=new HashMap<>();//响应信息
        if ("".equals(uploadFile.getOriginalFilename())){
            return null;
        }
        String filePath = "";
        String format = sdf.format(new Date());
        //固定物理路径
        File folder = new File(fileSavePath + format);
        //如果文件夹不存在则创建
        if (!folder.isDirectory()) {
            folder.mkdirs();//创建文件夹
        }
        //上传的文件名
        String oldName = uploadFile.getOriginalFilename();
        //新的文件名
        String newName = UUID.randomUUID().toString() +
                oldName.substring(oldName.lastIndexOf("."), oldName.length());
        try {
            //相对路径
            String relativePath=format + newName;
            //将uploadFile存到一个路径为：folder，名字为：newName的文件，
            uploadFile.transferTo(new File(folder, newName));
            //获取项目访问的路径
            filePath = req.getScheme() + "://" + req.getServerName() + ":" +
                    req.getServerPort() + "/uploadFile/" + relativePath;
            pathMap.put("relativePath",relativePath);//相对路径
            pathMap.put("readPath",filePath);//读取路径
        } catch (IOException e) {
            e.printStackTrace();
            pathMap.put("msg","上传失败!");
            return pathMap;
        }
        return pathMap;
    }

    /**
     * 上传文件到服务器路径，
     * 当上传过后项目再一次启动则会改变之前的路径，
     * 之前的文件也无法访问到了，但是还存在再电脑的临时文件夹 Temp 中
     * @param uploadFile 上传的文件
     * @param req request
     * @return 返回通过项目访问文件的路径（不需要配置资源映射）
     */
    public static String uploadServerFile(MultipartFile uploadFile, HttpServletRequest req) {
        if ("".equals(uploadFile.getOriginalFilename())){
            return "";
        }
        //文件存在的物理路径，但会随着项目的重新启动而改变
        String realPath =
                req.getSession().getServletContext().getRealPath("/uploadFile/");
        System.out.println(realPath);
        String format = sdf.format(new Date());
        File folder = new File(realPath + format);
        String filePath="";
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        //当前文件名
        String oldName = uploadFile.getOriginalFilename();
        //新文件名
        String newName = UUID.randomUUID().toString() +
                oldName.substring(oldName.lastIndexOf("."), oldName.length());
        try {
            //将接收到的文件传输到给定的目标文件。
            uploadFile.transferTo(new File(folder, newName));
            //项目访问文件的路径
            filePath = req.getScheme() + "://" + req.getServerName() + ":" +
                    req.getServerPort() + "/uploadFile/" + format + newName;

        } catch (IOException e) {
            e.printStackTrace();
            return "上传失败! ";
        }
        return filePath;
    }

    /**
     * 上传多个文件到指定路径中
     * @param uploadFiles   uploadPahtFile
     * @param req request
     * @return 返回通过项目访问文件的路径的集合（需要配置资源映射）
     */
    public static List<String> uploadPahtFiles(MultipartFile[] uploadFiles, HttpServletRequest req,String fileSavePath) {
        //所传入文件项目访问路径的集合
        ArrayList<String> filePathlist=new ArrayList();
        String filePath = "";
        for(MultipartFile uploadFile:uploadFiles){
            //如果文件名位空，则不保存
            if ("".equals(uploadFile.getOriginalFilename())){
                continue;
            }
            String format = sdf.format(new Date());
            File folder = new File(fileSavePath + format);
            if (!folder.isDirectory()) {
                folder.mkdirs();
            }
            String oldName = uploadFile.getOriginalFilename();
            String newName = UUID.randomUUID().toString() +
                    oldName.substring(oldName.lastIndexOf("."), oldName.length());
            try {
                uploadFile.transferTo(new File(folder, newName));
                filePath = req.getScheme() + "://" + req.getServerName() + ":" +
                        req.getServerPort() + "/uploadFile/" + format + newName;
            } catch (IOException e) {
                e.printStackTrace();
            }
            //将访问路径放入集合中
            filePathlist.add(filePath);
        }
        return filePathlist;
    }


}

//配置资源映射的配置类
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//    /**
//     * 图片保存路径，自动从yml文件中获取数据
//     *   示例： E:/uploadFile/
//     */
//    private String fileSavePath= FileUploadUtil.fileSavePath;
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        /**
//         * 配置资源映射
//         * 意思是：如果访问的资源路径是以“/uploadFile/”开头的，
//         * 就给我映射到本机的“E:/uploadFile/”这个文件夹内，去找你要的资源
//         * 注意：E:/uploadFile/ 后面的 “/”一定要带上
//         */
//        registry.addResourceHandler("/uploadFile/**")
//                .addResourceLocations("file:"+fileSavePath);
//    }
//}

//文件上传的配置参数
//#是否开启文件上传支持，默认为true。
//spring. servlet.multipart.enabled=true
//#文件写入磁盘的阈值，默认为0。
//spring.servlet.multipart.file-size-threshold=0
//#上传文件的临时保存位置。
//spring.servlet.multipart.location=E:ltemp
//#上传的单个文件的最大大小，默认为1MB。
//spring.servlet.multipart.max-file-size=1MB
//#多文件上传时文件的总大小，默认为10MB。
//spring.servlet.multipart.max-request-size=10MB
//#文件是否延迟解析，默认为false。
//spring.servlet.multipart.resolve-lazily=false