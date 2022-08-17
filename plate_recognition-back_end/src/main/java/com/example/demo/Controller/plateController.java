package com.example.demo.Controller;
import com.example.demo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/test")
public class plateController {
    @Autowired
    private static String FILE_ADDRESS;

    @Value("${file_address}")//保存地址
    public void setfILE_ADDRESS(String fILE_ADDRESS) {
        FILE_ADDRESS = fILE_ADDRESS;
    }

    @PostMapping("/runplate")
    @ResponseBody
    public Result<?> run(@RequestParam("video") MultipartFile file, @RequestParam("operation") String flag, HttpServletResponse response) throws IOException {
        if (!file.isEmpty()) {
            String path = FILE_ADDRESS;
            Integer num = (int) (Math.random()*100000);
            String filename=num.toString()+".mp4";
            File filepath = new File(path, filename);
            while(filepath.exists()){
                Integer num2 = (int) (Math.random()*1000000);
                String filename2=num.toString()+".mp4";
                File rename=new File(path, filename2);
                filepath.renameTo(rename);
            }
            File fi = new File(path + File.separator + filename);
            file.transferTo(fi);
            Runtime.getRuntime().exec("sh /home/plate/run.sh "+filename+" "+flag);
            response.sendRedirect("www.baidu.com");
            return Result.success("上传成功，识别可能需要一定时间");


        }
        else{
            return Result.success("上传失败，请检查上传的文件是否正确");
        }
    }

    @PostMapping("/s")
    @ResponseBody
    public String run2(RedirectAttributes attributes) throws IOException {
        attributes.addAttribute("redirect_url", "www.baidu.com");
        return 	"redirect:www.baidu.com";


    }


}
