package com.kh.hikers.web.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginMember {
  private Long memberId;
  private String id;
  private String pw;
  private String tel;
  private String nickname;
  private String gender;
  private int mexp;
  private String loc;
  private String gubun;
  private String code;
}
