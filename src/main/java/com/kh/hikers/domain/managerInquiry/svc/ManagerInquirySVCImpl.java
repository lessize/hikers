package com.kh.hikers.domain.managerInquiry.svc;

import com.kh.hikers.domain.entity.ManagerInquiry;
import com.kh.hikers.domain.managerInquiry.dao.ManagerInquiryDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ManagerInquirySVCImpl implements ManagerInquirySVC {
  private ManagerInquiryDAO managerInquiryDAO;

  ManagerInquirySVCImpl(ManagerInquiryDAO managerInquiryDAO){
    this.managerInquiryDAO = managerInquiryDAO;
  }
  @Override
  public List<ManagerInquiry> viewInquiryAll() {
    return managerInquiryDAO.viewInquiryAll();
  }

  @Override
  public List<ManagerInquiry> viewInquiryNull() {
    return managerInquiryDAO.viewInquiryNull();
  }

  @Override
  public List<ManagerInquiry> viewInquiryComplete() {
    return managerInquiryDAO.viewInquiryComplete();
  }

  @Override
  public List<ManagerInquiry> viewInquiryProgress() {
    return managerInquiryDAO.viewInquiryProgress();
  }

  @Override
  public Optional<ManagerInquiry> mViewByInquiryId(Long inquiryId) {
    return managerInquiryDAO.mViewByInquiryId(inquiryId);
  }

  @Override
  public int commentInquiry(Long inquiryId, ManagerInquiry managerInquiry) {
    return managerInquiryDAO.commentInquiry(inquiryId, managerInquiry);
  }
}
