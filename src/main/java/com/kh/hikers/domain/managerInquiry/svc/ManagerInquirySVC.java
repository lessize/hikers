package com.kh.hikers.domain.managerInquiry.svc;

import com.kh.hikers.domain.entity.ManagerInquiry;

import java.util.List;
import java.util.Optional;

public interface ManagerInquirySVC {
  List<ManagerInquiry> viewInquiryAll();

  //처리 중인 글의 목록만 보기
  List<ManagerInquiry> viewInquiryNull();
  List<ManagerInquiry> viewInquiryComplete();

  List<ManagerInquiry> viewInquiryProgress();

  //상세 조회(+댓글)
  Optional<ManagerInquiry> mViewByInquiryId(Long inquiryId);

  //문의 답변+상태변경
  int commentInquiry(Long inquiryId, ManagerInquiry managerInquiry);

}
