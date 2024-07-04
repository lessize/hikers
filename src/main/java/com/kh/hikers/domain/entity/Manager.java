package com.kh.hikers.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Manager {
  private Long inquiryId;
  private Long memberId;
  private String nickname;
  private String inquiryTitle;
  private String inquiryContent;
  private String inquiryComment;
  private String inquiryState;
  private String inquiryHidden;
  private LocalDateTime inquiryCdate;
  private LocalDateTime inquiryUdate;
  private LocalDateTime inquiryCommentCdate;
  private LocalDateTime inquiryCommentUdate;
}
