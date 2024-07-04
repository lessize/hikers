package com.kh.hikers.domain.manager.svc;

import com.kh.hikers.domain.entity.Manager;

import java.util.List;
import java.util.Optional;

public interface ManagerSVC {
  Long save(Manager manager);

  int deleteById(Long inquiryId);

  int deleteByIds(List<Long> inquiryIds);

  Optional<Manager> findById(Long inquiryId);

  int updateById(Long inquiryId, Manager manager);

  List<Manager> findAll();

  List<Manager> findAll(Long reqPage, Long recordCnt);

  int totalCnt();

}