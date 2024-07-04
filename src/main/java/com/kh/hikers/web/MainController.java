package com.kh.hikers.web;

import com.kh.hikers.domain.bbs.svc.BBSSVC;
import com.kh.hikers.domain.club.svc.ClubSVC;
import com.kh.hikers.domain.entity.*;
import com.kh.hikers.domain.manager.svc.ManagerSVC;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/community")
@RequiredArgsConstructor
public class MainController {

  private final BBSSVC bbssvc;
  private final ManagerSVC managerSVC;
  private final ClubSVC clubSVC;


  @GetMapping("/test")
  public String uploadtest() {
    return "test/uploadTest";
  }
  @GetMapping("/test2")
  public String upload() {
    return "test/testtest";
  }

  @GetMapping   // GET http://localhost:9080/community?reqPage=2&reqCnt=10
  public String findAllByPaging(
          Model model,
          @RequestParam(value = "reqPage", defaultValue = "1") Long reqPage, // 요청 페이지
          @RequestParam(value = "reqCnt", defaultValue = "10") Long reqCnt,   // 레코드 수
          @RequestParam(value = "cpgs", defaultValue = "1") Long cpgs,   //페이지 그룹 시작번호
          @RequestParam(value = "cp", defaultValue = "1") Long cp // 현재 페이지pagetag
  ){

    List<Bbs> list = bbssvc.findAll(reqPage, reqCnt);

    int totalCnt = bbssvc.totalCnt();
    model.addAttribute("list", list);
    model.addAttribute("totalCnt", totalCnt);
    model.addAttribute("cpgs", cpgs);
    model.addAttribute("cp", cp);

    for (Bbs bbs : list) {
      log.info("product={}",bbs);
    }
    log.info("size={}",list.size());

    return "community/allBlog";
  }
  //목록 club
  @GetMapping("/clubAll")   // GET http://localhost:9080/community?reqPage=2&reqCnt=10
  public String findclubByPaging(
      Model model,
      @RequestParam(value = "reqPage", defaultValue = "1") Long reqPage, // 요청 페이지
      @RequestParam(value = "reqCnt", defaultValue = "10") Long reqCnt,   // 레코드 수
      @RequestParam(value = "cpgs", defaultValue = "1") Long cpgs,   //페이지 그룹 시작번호
      @RequestParam(value = "cp", defaultValue = "1") Long cp  // 현재 페이지
  ){

    List<Club> list = clubSVC.findAll(reqPage, reqCnt);
    int totalCnt = clubSVC.totalCnt();

    model.addAttribute("list", list);
    model.addAttribute("totalCnt", totalCnt);
    model.addAttribute("cpgs", cpgs);
    model.addAttribute("cp", cp);

    for (Club club : list) {
      log.info("product={}",club);
    }
    log.info("size={}",list.size());

    return "community/allClub";
  }

  //목록 contact
  @GetMapping("/contactAll")   // GET http://localhost:9080/community?reqPage=2&reqCnt=10
  public String findcontactByPaging(
      Model model,
      @RequestParam(value = "reqPage", defaultValue = "1") Long reqPage, // 요청 페이지
      @RequestParam(value = "reqCnt", defaultValue = "10") Long reqCnt,   // 레코드 수
      @RequestParam(value = "cpgs", defaultValue = "1") Long cpgs,   //페이지 그룹 시작번호
      @RequestParam(value = "cp", defaultValue = "1") Long cp // 현재 페이지
  ){

    List<Manager> list = managerSVC.findAll(reqPage, reqCnt);
    int totalCnt = managerSVC.totalCnt();

    model.addAttribute("list", list);
    model.addAttribute("totalCnt", totalCnt);
    model.addAttribute("cpgs", cpgs);
    model.addAttribute("cp", cp);

    for (Manager manager : list) {
      log.info("product={}",manager);
    }
    log.info("size={}",list.size());

    return "community/allContact";
  }

}