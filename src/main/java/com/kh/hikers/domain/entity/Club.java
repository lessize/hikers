package com.kh.hikers.domain.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class Club {
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private String nickname;
  private Long clubId;
  private LocalDate timetable;
  private String mntnum;
  private String title;
  private Long memberId;
  private String ccontent;
  private Long mntnCode;
  private Long recruit;
  private LocalDate cdate;
  private LocalDate udate;
  private String code; // 이미지 경로를 저장할 리스트
}