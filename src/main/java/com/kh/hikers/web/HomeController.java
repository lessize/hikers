package com.kh.hikers.web;

import com.kh.hikers.web.form.LoginMember;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {

  @GetMapping("/")
  public String home(HttpServletRequest request){
    String view = null;
    HttpSession session = request.getSession(false);

    //로그인전
    if(session == null || session.getAttribute("loginMember") == null){
      view = "beforeLogin";
    }else{
      //로인후
      LoginMember loginMember = (LoginMember)session.getAttribute("loginMember");
      //관리자
      if(loginMember.getGubun().equals("M")){
        view = "admin";
      }else {
        //관리자외
        view = "afterLogin";
      }
    }
    return view;
  }
}
