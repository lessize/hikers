package com.kh.hikers.domain.entity;

import lombok.Data;

@Data
public class Mountain {
  private Long mntnCode;      // 산 코드 NUMBER 9
  private String mntnLoc;     // 위치 VARCHAR2 50
  private String city;
  private String mntnNm;      // 산 이름 VARCHAR2 50
  private String gubun;       // 100대 명산 구분
  private int mntnDffl;    // 산 난이도
  private double lot;         // 경도 NUMBER(12, 8)
  private double lat;         // 위도 NUMBER(12, 8)
  private double avgStaring;  // 산 평균 별점
  private byte[] mntnPic;     // 산 이미지
}
