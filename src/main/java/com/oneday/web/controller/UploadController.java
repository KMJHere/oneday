package com.oneday.web.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Log4j2
public class UploadController {
    @PostMapping("/uploadAjax")
    public void uploadFile(MultipartFile[] uploadFiles)  {
        for (MultipartFile uploadFile : uploadFiles) {
            // 실제 파일 이름 IE, Edge = 전체경로
            String orgName = uploadFile.getOriginalFilename();
            String fileName = orgName.substring(orgName.lastIndexOf("\\") + 1);

            log.info("fileName:" + fileName);
        }
    }
}
