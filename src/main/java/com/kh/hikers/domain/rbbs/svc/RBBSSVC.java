package com.kh.hikers.domain.rbbs.svc;


import com.kh.hikers.domain.entity.RBBS;

import java.util.List;

public interface RBBSSVC {
  Long save(RBBS rbbs);

  int deleteById(Long rbbsId);

  int deleteByIds(List<Long> rbbsIds);

  int updateById(Long rbbsId, RBBS rbbs);

  List<RBBS> findAll();

  List<RBBS> findAll(Long reqPage, Long recordCnt);

  int totalCnt();


}
