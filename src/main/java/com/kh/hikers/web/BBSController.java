package com.kh.hikers.web;

import com.kh.hikers.domain.bbs.svc.BBSSVC;
import com.kh.hikers.domain.entity.Bbs;
import com.kh.hikers.domain.entity.Mountain;
import com.kh.hikers.web.form.LoginMember;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/community")
@RequiredArgsConstructor
public class BBSController {


  private final BBSSVC bbssvc;
  private final UploadFileController uploadFileController;

  @GetMapping("/addcommunity")         // Get, http://localhost:9080/community/addcommunity
  public String addForm() {
    return "community/addcommunity";     // view이름  상품등록화면
  }

  @PostMapping("/addcommunity")        // Post, http://localhost:9080/community/add
  public String add(
      @RequestParam(value = "mntnLoc",required = false) Long mntnCode,
      @RequestParam(value = "title",required = false) String title,
      @RequestParam(value = "memberId",required = false) Long memberId,
      @RequestParam(value = "bcontent",required = false) String bcontent,
      @RequestParam(value = "staring",required = false) Long staring,
      @RequestParam(value = "ctime",required = false) Long ctime,
      @RequestParam(value = "checkboxData",required = false) String tags,
      @RequestParam(value = "code",required = false) String code,
      @RequestParam(value = "file",required = false) MultipartFile file,
      Model model,
      RedirectAttributes redirectAttributes,
      HttpServletRequest request
  ){
    log.info("title={}, {}, {}, {}, {}, {}, {} , {}, {}", title,memberId,bcontent,staring,ctime,tags,code, mntnCode, file);
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
    //게시판등록
    Bbs bbs = new Bbs();
    bbs.setMntnCode(mntnCode);
    bbs.setTitle(title);
    bbs.setMemberId(memberId);
    bbs.setBcontent(bcontent);
    bbs.setStaring(staring);
    bbs.setCtime(ctime);
    bbs.setCode(code);
    bbs.setTags(tags);
    bbs.setBcontent(bcontent);

    Long bbsId = bbssvc.save(bbs);

    redirectAttributes.addAttribute("pid",bbsId);
    code = "B";
    String bbsIds = String.valueOf(bbsId);
    String data = uploadFileController.uploadFile(file,code,bbsIds);
    System.out.printf(data);
    return "redirect:/community"; // 상품조회화면  302 GET http://서버:9080/community/상품번호/detail
  }

  //단일 삭제
  @GetMapping("/{pid}/del")
  public String deleteById(@PathVariable("pid") Long bbsId){
    log.info("deleteById={}",bbsId);

    //1)상품 삭제 -> 상품테이블에서 삭제
    int deletedRowCnt = bbssvc.deleteById(bbsId);

    return "redirect:/community";     // GET http://localhost:9080/community
  }

  //여러건삭제
  @PostMapping("/del")          // POST http://localhost:9080/community/del
  public String deleteByIds(@RequestParam("pids") List<Long> pids) {

    log.info("deleteByIds={}",pids);
    int deletedRowCnt = bbssvc.deleteByIds(pids);

    return "redirect:/community";    // GET http://localhost:9080/community/
  }

  
  //단일 조회
  @GetMapping("/{pid}/detail")       //GET http://localhost:9080/community/상품번호/detail
  public String findById(@PathVariable("pid")
                         Long bbsId, Model model){

    Optional<Bbs> findedBBS = bbssvc.findById(bbsId);
    Bbs bbs = findedBBS.orElseThrow();

    model.addAttribute("bbs", bbs);

    log.info("title={}, {}, {}, {}, {}, {}, {} , {}", bbs.getTitle(),bbs.getMemberId(),bbs.getBcontent(),bbs.getStaring(),bbs.getCtime(),bbs.getTags(),bbs.getCode(), bbs.getMntnCode());
    return "community/communitydt";
  }

  @GetMapping("/{pid}/edit")      // GET http://locahost:9080/products/상품번호/edit
  public String updateForm(
          @PathVariable("pid") Long bbsId,
          Model model){

    Optional<Bbs> optionalClub = bbssvc.findById(bbsId);
    Bbs findedClub = optionalClub.orElseThrow();

    model.addAttribute("bbs",findedClub);
    return "community/updatecommunity";
  }

  //수정 처리
  @PostMapping("/{pid}/edit")
  public String update(
          //경로변수 pid로부터 상품번호을 읽어온다
          @PathVariable("pid") Long bbsId,
          //요청메세지 바디로부터 대응되는 상품정보를 읽어온다.
          @RequestParam(value = "mntnCode",required = false) Long mntnCode,
          @RequestParam(value = "title",required = false) String title,
          @RequestParam(value = "memberId",required = false) Long memberId,
          @RequestParam(value = "bcontent",required = false) String bcontent,
          @RequestParam(value = "staring",required = false) Long staring,
          @RequestParam(value = "ctime",required = false) Long ctime,
          @RequestParam(value = "checkboxData",required = false) String tags,
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
    Bbs bbs = new Bbs();
    bbs.setMntnCode(mntnCode);
    bbs.setTitle(title);
    bbs.setMemberId(memberId);
    bbs.setBcontent(bcontent);
    bbs.setStaring(staring);
    bbs.setCtime(ctime);
    bbs.setCode(code);
    bbs.setTags(tags);
    bbs.setBcontent(bcontent);
    int updateRowCnt = bbssvc.updateById(bbsId, bbs);
    redirectAttributes.addAttribute("pid",bbsId);
    code = "B";
    String bbsIds = String.valueOf(bbsId);
    String data = uploadFileController.uploadFile(file,code,bbsIds);
    System.out.printf(data);
    return "redirect:/community";
  }

//  @GetMapping   // GET http://localhost:9080/community
//  public String findAll(Model model){
//
//    List<BBS> list = bbssvc.findAll();
//    model.addAttribute("list", list);
//    for (BBS bbs : list) {
//      log.info("product={}",bbs);
//    }
//    log.info("size={}",list.size());
//    return "community/all";
//  }
  
  //목록
//  @GetMapping   // GET http://localhost:9080/community?reqPage=2&reqCnt=10
//  public String findAllByPaging(
//      Model model,
//      @RequestParam(value = "reqPage", defaultValue = "1") Long reqPage, // 요청 페이지
//      @RequestParam(value = "reqCnt", defaultValue = "10") Long reqCnt,   // 레코드 수
//      @RequestParam(value = "cpgs", defaultValue = "1") Long cpgs,   //페이지 그룹 시작번호
//      @RequestParam(value = "cp", defaultValue = "1") Long cp   // 현재 페이지
//  ){
//
//    List<BBS> list = bbssvc.findAll(reqPage, reqCnt);
//    int totalCnt = bbssvc.totalCnt();
//
//    model.addAttribute("list", list);
//    model.addAttribute("totalCnt", totalCnt);
//    model.addAttribute("cpgs", cpgs);
//    model.addAttribute("cp", cp);
//
//    for (BBS bbs : list) {
//      log.info("product={}",bbs);
//    }
//    log.info("size={}",list.size());
//
//    return "community/all";
//  }

  //선택 리턴
  @GetMapping("/selectmntn")
  public ResponseEntity<List<Mountain>> selectmntn(@RequestParam(value = "mntnNm", required = false) String mntnNm) {
    System.out.println(mntnNm);
    List<Mountain> mntnselect = bbssvc.selectmntn(mntnNm);
    return new ResponseEntity<>(mntnselect, HttpStatus.OK);
  }

  @GetMapping("/blog")         // Get, http://localhost:9080/community/blog
  public String blog() {
    return "community/blog";
  }
  @GetMapping("/club")         // Get, http://localhost:9080/community/club
  public String club() {
    return "community/club";
  }
  @GetMapping("/contact")         // Get, http://localhost:9080/community/contact
  public String contact() {
    return "community/contact";
  }
}