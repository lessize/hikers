package com.kh.hikers.domain.entity;

import lombok.Data;

@Data
public class Course {
  private Long pmntnCode;         // 등산로 코드 NUMBER(9) PRIMARY KEY, PMNTN_CODE pmntnCode
  private Long pmntnNm;           // 등산로 이름 VARCHAR(100),
  private Long mntnCode;          // 산 코드 NUMBER(9) REFERENCES MOUNTAIN(MNTN_CODE),
  private Long pmntnDffl;         // 등산로 난이도 NUMBER(2),
  private Long pmntnMain;         // 등산로주요지점내용 VARCHAR2(200),
  private Long pmntnLt;           // 등산로 길이 NUMBER(5,2),
  private Long pmntnUppl;         // 상행시간 NUMBER(5),
  private Long pmntnGodn;         // 하행시간 NUMBER(5)
}
