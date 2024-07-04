package com.kh.hikers.domain.managerInquiry.dao;


import com.kh.hikers.domain.entity.ManagerInquiry;

import java.util.List;
import java.util.Optional;

public interface ManagerInquiryDAO {
  //문의 글 목록 전체 보기
  List<ManagerInquiry> viewInquiryAll();
  //처리 중인 글의 목록만 보기
  List<ManagerInquiry> viewInquiryProgress();
  //처리 완료인 목록만 보기
  List<ManagerInquiry> viewInquiryComplete();
  //null인 글 목록만 보기(답변 전)
  List<ManagerInquiry> viewInquiryNull();
  //상세 조회(+댓글)
  Optional<ManagerInquiry> mViewByInquiryId(Long inquiryId);
  
  //문의글 답변 달기
  int commentInquiry(Long inquiryId, ManagerInquiry managerInquiry);

  //문의글 상태 변경

}
