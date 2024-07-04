package com.kh.hikers.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ManagerBbs {
  private Long memberId;            //	NUMBER(8,0)

  //닉네임
  private String nickname;          //	VARCHAR2(30 BYTE)

  // BBS_ID	게시글번호  NUMBER	10
  private Long bbsId;
  // MNTN_CODE	산 아이디 NUMBER	10

  private Long mntnCode;

  // TITLE	제목 VARCHAR2	150
  private String title;

  // HIT	조회수 NUMBER	5
  private Integer hit;

  // BCONTENT	본문내용 CLOB
  private String bcontent;

  // STARING	별점 NUMBER	1
  private Integer staring;

  // CTIME	등반 시간 NUMBER	4
  private Integer ctime;

  // STATUS	게시글 상태 VARCHAR2	1
  private String status;
  // CDATE	작성일 TIMESTAMP
  private LocalDateTime cdate;
  // UDATE	수정일 TIMESTAMP
  private LocalDateTime udate;
  private String mntnNm;
  private String mntnLoc;

}
