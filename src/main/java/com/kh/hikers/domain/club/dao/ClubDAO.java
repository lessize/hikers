package com.kh.hikers.domain.club.dao;


import com.kh.hikers.domain.entity.Club;
import com.kh.hikers.domain.entity.Mountain;

import java.util.List;
import java.util.Optional;

public interface ClubDAO {

  //등록
  Long save(Club club);

  //삭제
  int deleteById(Long clubId);

  int deleteByIds(List<Long> clubIds);

  //조회
  Optional<Club> findById(Long clubId);

  //수정
  int updateById(Long clubId, Club club);

  //목록
  List<Club> findAll();

  List<Club> findAll(Long reqPage, Long recordCnt);

  int totalCnt();

  List<Mountain> selectmntn(String mntnNm);

  List<Mountain> outmntn(Long MntnCode);
}
