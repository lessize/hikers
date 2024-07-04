package com.kh.hikers.domain.manager.svc;

import com.kh.hikers.domain.entity.Manager;
import com.kh.hikers.domain.manager.dao.ManagerDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerSVCImpl implements ManagerSVC {

  private final ManagerDAO managerDAO;

  //등록
  @Override
  public Long save(Manager manager) {
    return managerDAO.save(manager);
  }

  //삭제
  @Override
  public int deleteById(Long inquiryId) {
    return managerDAO.deleteById(inquiryId);
  }

  //여러건삭제
  @Override
  public int deleteByIds(List<Long> inquiryIds) {
    return managerDAO.deleteByIds(inquiryIds);
  }

  //조회
  @Override
  public Optional<Manager> findById(Long inquiryId) {
    return managerDAO.findById(inquiryId);
  }


  @Override
  public int updateById(Long inquiryId, Manager manager) {
    return managerDAO.updateById(inquiryId, manager);
  }
  
  //목록
  @Override
  public List<Manager> findAll() {
    return managerDAO.findAll();
  }

  @Override
  public List<Manager> findAll(Long reqPage, Long recordCnt) {
    return managerDAO.findAll(reqPage,recordCnt);
  }

  @Override
  public int totalCnt() {
    return managerDAO.totalCnt();
  }

}