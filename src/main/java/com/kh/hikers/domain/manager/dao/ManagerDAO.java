package com.kh.hikers.domain.manager.dao;


import com.kh.hikers.domain.entity.Manager;

import java.util.List;
import java.util.Optional;

public interface ManagerDAO {
  
  //등록
  Long save(Manager manager);

  //삭제
  int deleteById(Long inquiryId);

  int deleteByIds(List<Long> inquiryIds);

  //조회
  Optional<Manager> findById(Long inquiryId);

  //수정
  int updateById(Long inquiryId, Manager manager);

  //목록
  List<Manager> findAll();

  List<Manager> findAll(Long reqPage, Long recordCnt);

  int totalCnt();

}