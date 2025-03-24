package com.e1i4.kiosk.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
public class AgePredictionService {

    private static final String PREDICTION_URL = "http://localhost:5000/predict"; // Python Flask API URL

//    public List<Prediction> predictAge(MultipartFile file) {
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "multipart/form-data");
//
//        // 파일 전송을 위한 HttpEntity 설정
//        HttpEntity<MultipartFile> entity = new HttpEntity<>(file, headers);
//
//        // REST API 호출
//        ResponseEntity<List> response = restTemplate.exchange(
//                PREDICTION_URL,
//                HttpMethod.POST,
//                entity,
//                List.class
//        );
//
//        return response.getBody();
//    }
}