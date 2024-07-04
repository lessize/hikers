package com.kh.hikers.domain.club.svc;


import com.kh.hikers.domain.entity.Club;
import com.kh.hikers.domain.entity.Mountain;

import java.util.List;
import java.util.Optional;

public interface ClubSVC {
  Long save(Club club);

  int deleteById(Long clubId);

  int deleteByIds(List<Long> clubIds);

  Optional<Club> findById(Long clubId);

  int updateById(Long clubId, Club club);

  List<Club> findAll();

  List<Club> findAll(Long reqPage, Long recordCnt);

  int totalCnt();

  List<Mountain> selectmntn(String mntnNm);
  List<Mountain> outmntn(Long MntnCode);
}
