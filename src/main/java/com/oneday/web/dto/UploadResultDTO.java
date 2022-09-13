package com.oneday.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

// implements Serializable = 객체 직렬화
@Data
@AllArgsConstructor
public class UploadResultDTO implements Serializable {
    private String fileName;

    private String uuid;

    private String folderPath;

    public String getImageURL() {
        try {
            return URLEncoder.encode(folderPath + "/" + uuid + "_" + fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }
    
    // 썸네일 링크 처리 메소드
    public String getThumbnailURL() {
        try {
            return URLEncoder.encode(folderPath + "/s_" + uuid + "_" + fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
