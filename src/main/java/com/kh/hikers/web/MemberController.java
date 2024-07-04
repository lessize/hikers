package com.kh.hikers.web;

import com.kh.hikers.domain.common.mail.MailService;
import com.kh.hikers.domain.entity.Member;
import com.kh.hikers.domain.member.svc.MemberSVC;
import com.kh.hikers.web.form.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

  private final MemberSVC memberSVC;
  private final MailService mailService;

  // 회원가입 페이지
  @GetMapping("/join")
  public String joinForm() {
    return "/member/joinForm";
  }

  // 회원 가입하기
  @PostMapping("/join")
  public String join(@RequestBody JoinForm joinForm) {
    log.info("joinForm = {}", joinForm);

    // 가입
    Member member = new Member();
    BeanUtils.copyProperties(joinForm, member);
    Long memberId = memberSVC.insertMember(member);

    log.info("member = {}", member);

    return memberId != null ? "redirect:/members/login" : "redirect:/";
  }

  // 로그인 페이지
  @GetMapping("/login")
  public String loginForm(Model model) {
    model.addAttribute("loginForm", new LoginForm());
    return "member/login";
  }
  
  // 로그인
  @PostMapping("/login")
  public String login(LoginForm loginForm, HttpServletRequest request,
                      @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
                      Model model) {

    log.info("loginForm={}", loginForm);

    if (memberSVC.existId(loginForm.getId())) {
      Optional<Member> optionalMember = memberSVC.findByEmailPw(loginForm.getId(), loginForm.getPw());

      if (optionalMember.isPresent()) {
        HttpSession session = request.getSession(true);
        Member member = optionalMember.get();

        LoginMember loginMember = new LoginMember(
                member.getMemberId(),
                member.getId(),
                member.getPw(),
                member.getTel(),
                member.getNickname(),
                member.getGender(),
                member.getMexp(),
                member.getLoc(),
                member.getGubun(),
                member.getCode()
        );
        session.setAttribute("loginMember", loginMember);
        log.info("loginMember={}", loginMember);

        // 로그인 성공 시 리다이렉트
        if (redirectUrl != null && !redirectUrl.isEmpty()) {
          return "redirect:" + redirectUrl;
        } else {
          return "redirect:/";
        }
      }
    }

    // 로그인 실패 시 에러 메시지 추가
    model.addAttribute("errorMessage", "아이디 혹은 비밀번호가 존재하지 않습니다.");
    return "member/login"; // 로그인 페이지로 리다이렉트 대신 다시 보여줌
  }

  // 로그아웃
  @ResponseBody
  @PostMapping("/logout")
  public String logout(HttpServletRequest request) {
    String flag = "NOK";

    HttpSession session = request.getSession(false);

    if (session != null) {
      session.invalidate();
      flag = "OK";
    }
    return flag;
  }

  // 프로필 조회
  @GetMapping("/profile")
  public String viewProfile(HttpServletRequest request, Model model) {
    LoginMember loginMember = (LoginMember) request.getSession().getAttribute("loginMember");
    List<String> locations = Arrays.asList(
            "서울특별시", "부산광역시", "대구광역시", "인천광역시", "광주광역시",
            "대전광역시", "울산광역시", "세종특별자치시", "경기도", "충청북도",
            "충청남도", "강원특별자치도", "경상북도", "경상남도", "전북특별자치도",
            "전라남도", "제주특별자치도"
    );

    if (loginMember != null) {
      Optional<MemberProfile> memberProfileOpt = memberSVC.findById(loginMember.getId());
      if (memberProfileOpt.isPresent()) {
        MemberProfile memberProfile = memberProfileOpt.get();
        model.addAttribute("memberProfile", memberProfile);
        model.addAttribute("locations", locations);
      } else {
        // 프로필이 없는 경우 처리
        model.addAttribute("memberProfile", null);
      }
    } else {
      // 로그인되지 않은 사용자 처리
      return "redirect:/members/login"; // 로그인 페이지로 리다이렉트
    }

    return "member/profile";
  }

  @PostMapping("/profile/update")
  @ResponseBody
  public ResponseEntity<?> updateProfile(@ModelAttribute Member member, HttpServletRequest request) {
    LoginMember loginMember = (LoginMember) request.getSession().getAttribute("loginMember");

    if (loginMember != null) {
      // 로그인한 사용자의 ID 가져오기
      String memberId = loginMember.getId();

      // 수정할 회원 정보 객체 생성
      Member updatedMember = new Member();
      updatedMember.setTel(member.getTel());
      updatedMember.setNickname(member.getNickname());
      updatedMember.setMexp(member.getMexp());
      updatedMember.setLoc(member.getLoc());

      int updatedRows = memberSVC.updateById(memberId, updatedMember);

      if (updatedRows > 0) {
        // 세션 정보 업데이트
        loginMember.setTel(member.getTel());
        loginMember.setNickname(member.getNickname());
        loginMember.setMexp(member.getMexp());
        loginMember.setLoc(member.getLoc());
        request.getSession().setAttribute("loginMember", loginMember);

        log.info(String.valueOf(loginMember));

        // 업데이트 성공 시 JSON 응답 반환
        return ResponseEntity.ok(loginMember);
      } else {
        // 업데이트 실패 시 JSON 에러 메시지 반환
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("프로필 업데이트에 실패하였습니다.");
      }
    } else {
      // 로그인되지 않은 사용자 처리
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
    }
  }

  // 아이디 찾기 양식
  @GetMapping("/findId")
  public String findIdForm() {
    return "member/findIdForm";
  }

  // 아이디 찾기
  @ResponseBody
  @PostMapping("findId")
  public ResponseEntity<Map<String, String>> findMyId(@RequestBody IdForm idForm) {
    Optional<Member> member = memberSVC.findByNicknameTel(idForm.getNickname(), idForm.getTel());
    Map<String, String> response = new HashMap<>();
    if (member.isPresent()) {
      response.put("status", "success");
      response.put("id", member.get().getId());
    } else {
      response.put("status", "error");
      response.put("message", "존재하지 않는 회원입니다.");
    }
    return ResponseEntity.ok(response);
  }

  //비밀번호 찾기 양식
  @GetMapping("/findPwd")
  public String findPwdForm(){

    return "member/findPwForm";
  }

  //비밀번호 찾기
  @ResponseBody
  @PostMapping("/findPwd")
  public ResponseEntity<Map<String, String>> findPwd(@RequestBody PwForm pwForm) {
    log.info("pwForm={}", pwForm);

    boolean hasPasswd = memberSVC.hasPasswd(pwForm.getId(), pwForm.getNickname());
    Map<String, String> response = new HashMap<>();

    if (hasPasswd) {
      // 임시번호 생성 6자리 랜덤 생성
      String tmpPwd = UUID.randomUUID().toString().substring(0, 6);
      // 회원 비밀번호를 임시 비밀번호로 변경
      memberSVC.changePw(pwForm.getId(), tmpPwd);
      // 메일 발송
      StringBuffer str = new StringBuffer();
      str.append("<html>");
      str.append("<p><b>");
      str.append(tmpPwd);
      str.append("</b></p>");
      str.append("<p>로그인 후 비밀번호를 변경하시기 바랍니다.</p>");
      str.append("</html>");

      mailService.sendMail(pwForm.getId(), "비밀번호 변경", str.toString());
      response.put("status", "success");
      response.put("message", "임시 비밀번호가 메일로 전송되었습니다.");
      return ResponseEntity.ok(response);
    }

    response.put("status", "error");
    response.put("message", "회원정보가 없습니다.");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }
}
