package com.kh.hikers.web;

import com.kh.hikers.domain.club.svc.ClubSVC;
import com.kh.hikers.domain.entity.Club;
import com.kh.hikers.web.form.LoginMember;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/community")
@RequiredArgsConstructor
public class ClubController {
  @DateTimeFormat(pattern = "yyyy-MM-dd")

  private final ClubSVC clubSVC;
  private final UploadFileController uploadFileController;

  @GetMapping("/addclub")         // Get, http://localhost:9080/community/addclub
  public String addForm() {
    return "community/addclub";     // view이름  상품등록화면
  }

  @PostMapping("/addclub")        // Post, http://localhost:9080/community/add
  public String add(
      @RequestParam(value = "mntnLoc",required = false) Long mntnCode,
      @RequestParam(value = "title",required = false) String title,
      @RequestParam(value = "memberId",required = false) Long memberId,
      @RequestParam(value = "bcontent",required = false) String ccontent,
      @RequestParam(value = "personnel",required = false) Long personnel,
      @RequestParam(value = "date",required = false) LocalDate timetable,
      @RequestParam(value = "code",required = false) String code,
      @RequestParam(value = "file",required = false) MultipartFile file,
      Model model,
      RedirectAttributes redirectAttributes,
      HttpServletRequest request
  ){

    log.info("title={}, {}, {}, {}, {} , {}, {}", title,memberId,mntnCode,ccontent,personnel,timetable,file);
    HttpSession session = request.getSession(false);
    if (session == null) {
      log.info("NOK");
    }else{
      log.info("ok={}",session);
    }

    LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");
    log.info("loginMember={}", loginMember);
    memberId = loginMember.getMemberId();
    log.info("memberId={}", memberId);
    //동호회등록
    Club club = new Club();
    club.setMntnCode(mntnCode);
    club.setTitle(title);
    club.setMemberId(memberId);
    club.setCcontent(ccontent);
    club.setRecruit(personnel);
    club.setCode(code);
    club.setTimetable(timetable);

    Long clubId = clubSVC.save(club);

    redirectAttributes.addAttribute("pid",clubId);
    code = "C";
    String clubIds = String.valueOf(clubId);
    String data = uploadFileController.uploadFile(file,code,clubIds);
    System.out.printf(data);
    return "redirect:/community/clubAll"; // 상품조회화면  302 GET http://서버:9080/community/상품번호/detail
  }

  //단일 삭제
  @GetMapping("/{pid}/clubdel")
  public String deleteById(@PathVariable("pid") Long clubId){
    log.info("deleteById={}",clubId);

    //1)상품 삭제 -> 상품테이블에서 삭제
    int deletedRowCnt = clubSVC.deleteById(clubId);

    return "redirect:/community/clubAll";     // GET http://localhost:9080/community
  }

  //여러건삭제
  @PostMapping("/clubdel")          // POST http://localhost:9080/community/del
  public String deleteByIds(@RequestParam("pids") List<Long> pids) {

    log.info("deleteByIds={}",pids);
    int deletedRowCnt = clubSVC.deleteByIds(pids);

    return "redirect:/community";    // GET http://localhost:9080/community/
  }


  //단일 조회
  @GetMapping("/{pid}/clubdetail")       //GET http://localhost:9080/community/상품번호/detail
  public String findById(@PathVariable("pid") Long culbId, Model model){

    Optional<Club> findedClub = clubSVC.findById(culbId);
    Club club = findedClub.orElseThrow();
    model.addAttribute("club", club);

    log.info("title={}, {}, {}, {}, {}, {}, {} ", club.getTitle(),club.getMemberId(),club.getTimetable(),club.getCcontent(),club.getRecruit(),club.getCode(), club.getMntnCode());
    return "/community/clubdt";
  }
  @GetMapping("/{pid}/clubedit")      // GET http://locahost:9080/products/상품번호/edit
  public String updateForm(
          @PathVariable("pid") Long culbId,
          Model model){

    Optional<Club> optionalClub = clubSVC.findById(culbId);
    Club findedClub = optionalClub.orElseThrow();

    model.addAttribute("club",findedClub);
    return "community/updateclub";
  }
  //수정 처리
  @PostMapping("/{pid}/clubedit")
  public String update(
          //경로변수 pid로부터 상품번호을 읽어온다
          @PathVariable("pid") Long clubId,
          //요청메세지 바디로부터 대응되는 상품정보를 읽어온다.
          @RequestParam(value = "mntnLoc",required = false) Long mntnCode,
          @RequestParam(value = "title",required = false) String title,
          @RequestParam(value = "memberId",required = false) Long memberId,
          @RequestParam(value = "bcontent",required = false) String ccontent,
          @RequestParam(value = "personnel",required = false) Long personnel,
          @RequestParam(value = "date",required = false) LocalDate timetable,
          @RequestParam(value = "code",required = false) String code,
          @RequestParam(value = "file",required = false) MultipartFile file,
          //리다이렉트시 경로변수에 값을 설정하기위해 사용
          RedirectAttributes redirectAttributes,
          HttpServletRequest request){
    HttpSession session = request.getSession(false);
    if (session == null) {
      log.info("NOK");
    }else{
      log.info("ok={}",session);
    }

    LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");
    log.info("loginMember={}", loginMember);
    memberId = loginMember.getMemberId();
    log.info("memberId={}", memberId);
    Club club = new Club();
    club.setMntnCode(mntnCode);
    club.setTitle(title);
    club.setMemberId(memberId);
    club.setTimetable(timetable);
    club.setCcontent(ccontent);
    club.setCode(code);
    club.setRecruit(personnel);
    int updateRowCnt = clubSVC.updateById(clubId, club);

    redirectAttributes.addAttribute("pid",clubId);
    code = "C";
    String clubIds = String.valueOf(clubId);
    String data = uploadFileController.uploadFile(file,code,clubIds);
    System.out.printf(data);
    return "redirect:/community/{pid}/clubdetail";
  }

}