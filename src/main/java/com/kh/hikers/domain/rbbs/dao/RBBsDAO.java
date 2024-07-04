package com.kh.hikers.domain.rbbs.dao;


import com.kh.hikers.domain.entity.RBBS;

import java.util.List;

public interface RBBsDAO {

  //등록
  Long save(RBBS rbbs);

  //삭제
  int deleteById(Long rbbsId);

  int deleteByIds(List<Long> rbbsIds);

  //수정
  int updateById(Long rbbsId, RBBS rbbs);

  //목록
  List<RBBS> findAll();

  List<RBBS> findAll(Long reqPage, Long recordCnt);

  int totalCnt();
}
