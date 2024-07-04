package com.kh.hikers.web;

import com.kh.hikers.domain.entity.Club;
import com.kh.hikers.domain.entity.Manager;
import com.kh.hikers.domain.manager.svc.ManagerSVC;
import com.kh.hikers.web.form.LoginMember;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/community")
@RequiredArgsConstructor
public class ContactController {


  private final ManagerSVC managerSVC;

  @GetMapping("/addcontact")         // Get, http://localhost:9080/community/addclub
  public String addForm() {
    return "community/addcontact";     // view이름  상품등록화면
  }

  @PostMapping("/addcontact")        // Post, http://localhost:9080/community/add
  public String add(
      @RequestParam(value = "title",required = false) String title,
      @RequestParam(value = "memberId",required = false) Long memberId,
      @RequestParam(value = "bcontent",required = false) String bcontent,
      @RequestParam(value = "checkboxData",required = false) String inquiryHidden,

      Model model,
      RedirectAttributes redirectAttributes,
      HttpServletRequest request
  ){

    log.info("title={}, {}, {}, {}", title,memberId,bcontent,inquiryHidden);
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
    Manager manager = new Manager();
    manager.setInquiryTitle(title);
    manager.setMemberId(memberId);
    manager.setInquiryContent(bcontent);
    manager.setInquiryHidden(inquiryHidden);
    Long contactId = managerSVC.save(manager);

    redirectAttributes.addAttribute("pid",contactId);
    return "redirect:/community/contactAll"; // 상품조회화면  302 GET http://서버:9080/community/상품번호/detail
  }

  //단일 삭제
  @GetMapping("/{pid}/contactdel")
  public String deleteById(@PathVariable("pid") Long contactId){
    log.info("deleteById={}",contactId);

    //1)상품 삭제 -> 상품테이블에서 삭제
    int deletedRowCnt = managerSVC.deleteById(contactId);

    return "redirect:/community";     // GET http://localhost:9080/community
  }

  //여러건삭제
  @PostMapping("/contactdel")          // POST http://localhost:9080/community/del
  public String deleteByIds(@RequestParam("pids") List<Long> pids) {

    log.info("deleteByIds={}",pids);
    int deletedRowCnt = managerSVC.deleteByIds(pids);

    return "redirect:/community";    // GET http://localhost:9080/community/
  }


  //단일 조회
  @GetMapping("/{pid}/contactdetail")       //GET http://localhost:9080/community/상품번호/detail
  public String findById(@PathVariable("pid") Long contactId, Model model){

    Optional<Manager> findedClub = managerSVC.findById(contactId);
    Manager manager = findedClub.orElseThrow();
    model.addAttribute("manager", manager);

    log.info("title={}, {}, {}, {}, {}, {}",manager.getInquiryId(), manager.getInquiryTitle(),manager.getMemberId(),manager.getInquiryContent(),manager.getInquiryCdate(),manager.getInquiryUdate());
    return "community/contactedit";
  }
  @GetMapping("/{pid}/contactedit")      // GET http://locahost:9080/products/상품번호/edit
  public String updateForm(
          @PathVariable("pid") Long culbId,
          Model model){

    Optional<Manager> optionalClub = managerSVC.findById(culbId);
    Manager findedClub = optionalClub.orElseThrow();

    model.addAttribute("manager",findedClub);
    return "community/updatecontact";
  }
  //수정 처리
  @PostMapping("/{pid}/contactedit")
  public String update(
          //경로변수 pid로부터 상품번호을 읽어온다
          @PathVariable("pid") Long contactId,
          //요청메세지 바디로부터 대응되는 상품정보를 읽어온다.
          @RequestParam(value = "title",required = false) String title,
          @RequestParam(value = "memberId",required = false) Long memberId,
          @RequestParam(value = "bcontent",required = false) String bcontent,
          @RequestParam(value = "checkboxData",required = false) String inquiryHidden,
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

    Manager manager = new Manager();
    manager.setInquiryTitle(title);
    manager.setMemberId(memberId);
    manager.setInquiryContent(bcontent);
    manager.setInquiryHidden(inquiryHidden);
    int updateRowCnt = managerSVC.updateById(contactId, manager);

    redirectAttributes.addAttribute("pid",contactId);
    return "redirect:/community/dit/{pid}/contactdetail";
  }

}