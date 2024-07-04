package com.kh.hikers.web.form;

import lombok.Data;

@Data
public class MemberForm {
  private String id;
  private String tel;
  private String nickname;
  private int mexp;
  private String loc;
}
