package com.kh.hikers.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Member {
  private Long memberId;            //   NUMBER(8,0)
  //아이디
  private String id;             //   VARCHAR2(40 BYTE)
  //비밀번호
  private String pw;            //   VARCHAR2(20 BYTE)
  //전화번호
  private String tel;           //varchar2(13)
  //닉네임
  private String nickname;          //   VARCHAR2(30 BYTE)
  //성별
  private String gender;            // VARCHAR2(6)
  //등산경험(0,1,2)
  private int mexp;                 //number(1)
  //지역
  private String loc;             //varchar2(10)
  //구분(일반 U, 관리자 M)
  private String gubun;             //   VARCHAR2(11 BYTE)
  private LocalDateTime cdate;       //TIMESTAMP(6)
  private LocalDateTime udate;       //TIMESTAMP(6)
  //코드
  private String code;              // VARCHAR2(10 BYTE)
}
