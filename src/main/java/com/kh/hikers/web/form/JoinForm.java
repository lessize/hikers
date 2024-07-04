package com.kh.hikers.web.form;

import lombok.Data;

@Data
public class JoinForm {
  private String id;
  private String pw;
  private String tel;
  private String nickname;
  private String gender;
  private int mexp;
  private String loc;
}
