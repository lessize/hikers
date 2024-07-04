package com.kh.hikers.web;

import com.kh.hikers.domain.entity.RBBS;
import com.kh.hikers.domain.entity.RClub;
import com.kh.hikers.domain.rclub.svc.RClubSVC;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/community")
@RequiredArgsConstructor
public class RClubController {
  @DateTimeFormat(pattern = "yyyy-MM-dd")

  private final RClubSVC rclubSVC;

  @PostMapping("/addrclub")        // Post, http://localhost:9080/community/addrbbs
  public ResponseEntity<RClub> add(
      @RequestParam(value = "clubId",required = false) Long clubId,
      @RequestParam(value = "memberId",required = false) Long memberId,
      @RequestParam(value = "comments",required = false) String comments,
      Model model,
      RedirectAttributes redirectAttributes
  ){

    try{
      log.info("title={}, {}, {}", clubId,memberId,comments);

      //동호회등록
      RClub rClub = new RClub();
      rClub.setClubId(clubId);
      rClub.setComments(comments);
      rClub.setMemberId(memberId);
      Long rbbsId = rclubSVC.save(rClub);

      return ResponseEntity.ok(rClub); // JSON으로 반환됨

    } catch (Exception e) {
      log.error("문의글 답변 등록 오류: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(null); // 예외 처리
    }
  }

  //단일 삭제
  @GetMapping("/{pid}/rclubdel")
  public String deleteById(@PathVariable("pid") Long clubId){
    log.info("deleteById={}",clubId);

    //1)상품 삭제 -> 상품테이블에서 삭제
    int deletedRowCnt = rclubSVC.deleteById(clubId);

    return "redirect:/community";     // GET http://localhost:9080/community
  }
  //수정 처리
  @PostMapping("/{pid}/rclubedit")
  public String update(
          //경로변수 pid로부터 상품번호을 읽어온다
          @PathVariable("pid") Long rclubId,
          //요청메세지 바디로부터 대응되는 상품정보를 읽어온다.
          @RequestParam(value = "clubId",required = false) Long clubId,
          @RequestParam(value = "memberId",required = false) Long memberId,
          @RequestParam(value = "comments",required = false) String comments,
          //리다이렉트시 경로변수에 값을 설정하기위해 사용
          RedirectAttributes redirectAttributes){
    RClub rClub = new RClub();
    rClub.setClubId(clubId);
    rClub.setMemberId(memberId);
    rClub.setComments(comments);
    int updateRowCnt = rclubSVC.updateById(rclubId, rClub);

    redirectAttributes.addAttribute("pid",clubId);
    return "redirect:/community/{pid}/rclubdetail";
  }

  @GetMapping("/rclubAll")   // GET http://localhost:9080/community?reqPage=2&reqCnt=10
  public ResponseEntity<Map<String, Object>> findclubByPaging(
      @RequestParam(value = "reqPage", defaultValue = "1") Long reqPage, // 요청 페이지
      @RequestParam(value = "reqCnt", defaultValue = "10") Long reqCnt,   // 레코드 수
      @RequestParam(value = "cpgs", defaultValue = "1") Long cpgs,   // 페이지 그룹 시작번호
      @RequestParam(value = "cp", defaultValue = "1") Long cp  // 현재 페이지
  ){

    List<RClub> list = rclubSVC.findAll(reqPage, reqCnt);
    int totalCnt = rclubSVC.totalCnt();

    Map<String, Object> response = new HashMap<>();
    response.put("list", list);
    response.put("totalCnt", totalCnt);
    response.put("cpgs", cpgs);
    response.put("cp", cp);

    for (RClub rClub : list) {
      log.info("product={}", rClub);
    }
    log.info("size={}", list.size());
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

}