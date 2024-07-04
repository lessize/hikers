package com.kh.hikers.web;

import com.kh.hikers.domain.common.mail.MailService;
import com.kh.hikers.domain.member.svc.MemberSVC;
import com.kh.hikers.web.form.EmailChkReq;
import com.kh.hikers.web.form.VerifiReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class ApiJoinController {

  private final MemberSVC memberSVC;
  private final MailService mailService;

  // 이메일과 인증번호를 매핑할 맵
  private Map<String, String> verificationCodes = new HashMap<>();

  @PostMapping("/verify")
  public ResponseEntity<String> sendVerificationCode(@RequestBody EmailChkReq emailChkReq) {

    String email = emailChkReq.getId();

    // 이메일 존재 여부 확인
    if (memberSVC.existId(email)) {
      // 이미 존재하는 이메일이면 오류 응답 반환
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 이메일입니다.");
    }

    // 임시번호 생성 6자리 랜덤 생성
    String authCode = UUID.randomUUID().toString().substring(0, 6);

    // 이메일에 인증번호 저장
    verificationCodes.put(emailChkReq.getId(), authCode);

    // 메일 발송 로직
    StringBuffer emailContent = new StringBuffer();
    emailContent.append("<html><body>");
    emailContent.append("<p><b>").append(authCode).append("</b></p>");
    emailContent.append("<p>인증 코드를 확인해주세요.</p>");
    emailContent.append("</body></html>");

    mailService.sendMail(emailChkReq.getId(), "인증번호 송부", emailContent.toString());

    return ResponseEntity.ok("인증번호가 이메일로 전송되었습니다.");
  }

  // 이메일 인증 확인
  @PostMapping("/check")
  public boolean verifyCode(@RequestBody VerifiReq verifiReq) {
    String email = verifiReq.getId();
    String authCode = verifiReq.getAuthCode();

    // 입력된 인증번호와 저장된 인증번호 비교
    String storedAuthCode = verificationCodes.get(email);
    return storedAuthCode != null && storedAuthCode.equals(authCode);
  }

//    // 회원 가입 처리
//    Member member = new Member();
//    BeanUtils.copyProperties(joinForm, member);
//    Long memberId = memberSVC.insertMember(member);
//
//    if (memberId != null) {
//      Map<String, String> response = new HashMap<>();
//      response.put("status", "success");
//      response.put("message", "회원 가입이 완료되었습니다.");
//      return ResponseEntity.ok(response);
//    } else {
//      Map<String, String> response = new HashMap<>();
//      response.put("status", "error");
//      response.put("message", "회원 가입에 실패했습니다. 다시 시도해 주세요.");
//      return ResponseEntity.status(500).body(response);
//    }
//  }
}
