package com.kh.hikers.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.hikers.domain.entity.Bbs;
import com.kh.hikers.domain.entity.Mountain;
import com.kh.hikers.domain.recommend.svc.RecommendSVC;
import com.kh.hikers.web.form.LoginMember;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller // Controller 역할을 하는 클래스
@RequestMapping("/recommend")    // http://localhost:9080/recommend
public class RecommendController {

  private RecommendSVC recommendSVC;

  RecommendController(RecommendSVC recommendSVC){
    this.recommendSVC = recommendSVC;
  }

  //100대 명산 목록
  @GetMapping("/100_mountains")     // GET http://localhost:9080/recommend/100_mountains
  public String top100MountainsList(Model model){

    List<Mountain> list = recommendSVC.top100MountainsList();
    model.addAttribute("list", list);

    return "recommend/100_mountains";
  }

//   개인별 추천
  @GetMapping("/personal_recommend")   // GET http://localhost:9080/recommend/personal_recommend
  public String personalizedRecommend(Model model, HttpServletRequest request) {

    HttpSession session = request.getSession(false);
    if (session == null) {
      log.info("NOK");
    }else{
      log.info("ok={}",session);
    }

    LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");
    log.info("loginMember={}", loginMember);
    Long memberId = loginMember.getMemberId();
    log.info("memberId={}", memberId);

    List<Mountain> list = recommendSVC.personalRecommend(memberId);
    log.info("list={}", list);

    model.addAttribute("list", list);
    model.addAttribute("memberId", memberId);

    return "recommend/personal_recommend";
  }

  // 별점 순
  @GetMapping("/rated_mountains")
  public String mountainsRating(Model model) {
    final String INTERPRETER_PATH = "d:/kdt/anaconda3/python.exe";
    final String PYTHON_SOURCE_PATH = "d:/kdt/projects/pythonDemo3/sentimentAnalysis";

    List<Map<String, Object>> list = recommendSVC.mountainsRating();
    model.addAttribute("topRateMountain", list);
    int totalCnt = recommendSVC.totalCnt();

    // 각 산의 본문 내용과 mntnCode를 함께 추출하여 리스트로 만듭니다.
    List<Map<String, String>> bcontents = new ArrayList<>();
    for (Map<String, Object> entry : list) {
      Mountain mountain = (Mountain) entry.get("mountain");
      Long  mntnCode = mountain.getMntnCode();
      System.out.println("Extracted mntnCode: " + mntnCode);
      List<Bbs> posts = (List<Bbs>) entry.get("posts");
      for (Bbs post : posts) {
        Map<String, String> reviewData = new HashMap<>();
        reviewData.put("mntnCode", String.valueOf(mntnCode));
        reviewData.put("bcontent", post.getBcontent());
        bcontents.add(reviewData);
      }
    }

    System.out.println("Extracted bcontents: " + bcontents);

    try {
      // bcontents를 JSON 형식의 문자열로 변환
      ObjectMapper objectMapper = new ObjectMapper();
      String bcontentsJson = objectMapper.writeValueAsString(bcontents);

      System.out.println("Sending to Python script: " + bcontentsJson);  // 로그 추가

      // 파이썬 스크립트 실행을 위한 ProcessBuilder 설정
      ProcessBuilder processBuilder = new ProcessBuilder(INTERPRETER_PATH, "test1.py");
      processBuilder.directory(new File(PYTHON_SOURCE_PATH));
      processBuilder.redirectInput(ProcessBuilder.Redirect.PIPE);
      processBuilder.redirectErrorStream(true);

      Process process = processBuilder.start();

      // 파이썬 스크립트에 JSON 데이터 전송
      try (OutputStream outputStream = process.getOutputStream();
           BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {
        writer.write(bcontentsJson);
      }

      // 파이썬 스크립트의 표준 출력을 읽음
      BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      while ((line = in.readLine()) != null) {
        System.out.println(line);
      }

      // 에러 메시지와 출력 메시지를 함께 읽기
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
        while ((line = reader.readLine()) != null) {
          System.out.println(line);
        }
      }

      int exitCode = process.waitFor();
      System.out.println("Python script exited with code: " + exitCode);

      // 결과 파일 경로
      String resultFilePath = "d:/kdt/projects/hikers/src/main/resources/static/files/test.txt";

      // 파일에서 결과 데이터 읽기
      List<Map<String, Object>> pythonResult = new ArrayList<>();
      try (BufferedReader reader = new BufferedReader(new FileReader(resultFilePath))) {
        while ((line = reader.readLine()) != null) {
          // JSON 문자열을 Map 객체로 변환
          Map<String, Object> item = objectMapper.readValue(line, new TypeReference<Map<String, Object>>() {});
          pythonResult.add(item);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }

      System.out.println("Received from Python script file: " + pythonResult);  // 로그 추가

      // 모델에 파이썬 스크립트 실행 결과 추가
      model.addAttribute("pythonResult", pythonResult);

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
    return "recommend/rated_mountains";
  }

  // 산 이미지를 제공하는 함수
  @GetMapping("/image/{mntnCode}")
  public ResponseEntity<byte[]> getMountainImage(@PathVariable("mntnCode") Long mntnCode) {
    byte[] image = recommendSVC.getMountainImage(mntnCode);

    if(image != null) {
      return ResponseEntity
              .ok()
              .contentType(MediaType.IMAGE_PNG) // 이미지 포맷에 따라 이 부분을 변경 가능
              .body(image);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
  
}
