package com.oneday.web.controller;

import com.oneday.web.dto.UploadResultDTO;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
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
import java.io.UnsupportedEncodingException;
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
import net.coobird.thumbnailator.*;

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
                // 원본 파일 저장
                uploadFile.transferTo(savePath);

                // 섬네일 생성
                String thumbnailSaveName = uploadPath + File.separator + folderPath + File.separator + "s_" + uuid + "_" + fileName;

                File thumbnailFile = new File(thumbnailSaveName);

                // 썸네일 생성 > Thumbnailator 라이브러리 이용
                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100, 100);

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
    public ResponseEntity<byte[]> getFile(String fileName, String size) {

        log.info("fileName MJ: " + fileName);
        ResponseEntity<byte[]> result = null;

        try {
            String srcFileNm = URLDecoder.decode(fileName, "UTF-8");

            log.info("fileNm: " + srcFileNm);

            File file = new File(uploadPath + File.separator + srcFileNm);

            // size 확인 후 값이 있는 경우 원본 파일 전달(substring s_ 두개 뒤부터,,)
            if(size != null && size.equals("1")) {
                file = new File(file.getParent(), file.getName().substring(2));
            }
            
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

    // 원본 & 썸네일 삭제 메소드
    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName) {
        String srcFileName = null;

        try {
            srcFileName = URLDecoder.decode(fileName, "UTF-8");
            File file = new File(uploadPath + File.separator + srcFileName);
            //  파일 삭제
            boolean result = file.delete();

            // getParent() => 현재 파일객체의 부모 디렉토리의 절대 경로명을 리턴
            File thumbnail = new File(file.getParent(), "s_" + file.getName());

            result = thumbnail.delete();

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
