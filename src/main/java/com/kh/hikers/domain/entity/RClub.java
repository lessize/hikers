package com.kh.hikers.domain.entity;

import lombok.Data;

@Data
public class RClub {
  private Long clubId;
  private Long memberId;
  private String comments;
}