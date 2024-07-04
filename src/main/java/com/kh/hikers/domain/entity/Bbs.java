package com.kh.hikers.domain.entity;

import lombok.Data;

import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Bbs {
  private int Hit;
  private Long bbsId;
  private String potolog;
  private String nickname;
  private String mntnum;
  private Long mntnCode;
  private String title;
  private Long memberId;
  private String bcontent;
  private Long staring;
  private Long ctime;
  private String status;
  private LocalDateTime cdate;
  private LocalDateTime udate;
  private String tags;
  private String code; // 이미지 경로를 저장할 리스트

}