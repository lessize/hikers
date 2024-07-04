package com.kh.hikers.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ManagerInquiry {

  // Inquiry_id	문의글 아이디			NUMBER	10
  private Long inquiryId;

  // MEMBER_ID	작성자멤버ID			NUMBER	8
  private Long memberId;

  // 작성자 id(email)
  private String memberemail;
  // 닉네임
  private String nickname;

  // Inquiry_title	문의 제목			VARCHAR2	150
  private String inquiryTitle;

  // Inquiry_content	문의 내용			CLOB
  private String inquiryContent;

  // Inquiry_comment	문의 답변			CLOB
  private String inquiryComment;

  // Inquiry_state	문의 상태			VARCHAR2
  private String inquiryState;

//문의 작성일 문의 수정일
//Inquiry_cdate
//Inquiry_udate
  private LocalDateTime inquiryCdate;
  private LocalDateTime inquiryUdate;

// 답변 작성일 답변 수정일
// Inquiry_comment_cdate
// Inquiry_comment_udate

  private LocalDateTime inquiryCommentCdate;
  private LocalDateTime inquiryCommentUdate;


}
