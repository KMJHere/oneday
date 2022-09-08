package com.oneday.web.controller;

import com.oneday.web.dto.UploadResultDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.DateFormatter;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
public class UploadController {
    @Value("${org.oneday.upload.path}") // application.properties 선언
    private String uploadPath;

    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles)  {
        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        for (MultipartFile uploadFile : uploadFiles) {
            // 이미지 파일만 업로드 가능하도록..
            if(uploadFile.getContentType().startsWith("image") == false) {
                log.warn("이미지 파일만 업로드 가능");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            // 실제 파일 이름 IE, Edge = 전체경로
            String orgName = uploadFile.getOriginalFilename();
            String fileName = orgName.substring(orgName.lastIndexOf("\\") + 1);

            log.info("fileName:" + fileName);

            // 날짜 폴더 생성
            String folderPath = makeFolder();

            // UUID 고유 이름 붙이기
            String uuid = UUID.randomUUID().toString();

            // 저장할 파일 이름 중간 "_" 구분자
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;

            Path savePath = Paths.get(saveName);

            try {
                uploadFile.transferTo(savePath);
                resultDTOList.add(new UploadResultDTO(fileName, uuid, folderPath));
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }

    // 디렉터리 생성
    private String makeFolder() {
        // DateTimeFormatter -> LocalDate(날짜만, x) / LocalDateTime(날짜+시간, o)
        String str = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/mm/dd"));

        String folderPath = str.replace("//", File.separator);

        // make foler
        File uploadPathFolder = new File(uploadPath, folderPath);


        // 해당 folder 없을 경우 make
        if(uploadPathFolder.exists() == false) {
            uploadPathFolder.mkdirs();
        }

        return folderPath;
    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName) {

        log.info("fileName MJ: " + fileName);
        ResponseEntity<byte[]> result = null;

        try {
            String srcFileNm = URLDecoder.decode(fileName, "UTF-8");

            log.info("fileNm: " + srcFileNm);

            File file = new File(uploadPath + File.separator + srcFileNm);

            log.info("file: " + file);

            HttpHeaders header = new HttpHeaders();

            // MIME타입 처리 > Files.probeContentType = 확장자가 없을 경우 NULL 반환
            header.add("Content-Type", Files.probeContentType(file.toPath()));

            // 파일 데이터 처리
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
        } catch(Exception e) {
            log.error(e.getMessage());

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("이게 뭐여:" + result);

        return result;
    }
}
