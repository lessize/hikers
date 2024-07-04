package com.kh.hikers.domain.rclub.dao;

import com.kh.hikers.domain.entity.RClub;

import java.util.List;

public interface RClubDAO {

  //등록
  Long save(RClub rClub);

  //삭제
  int deleteById(Long rclubId);

  int deleteByIds(List<Long> rclubIds);


  //수정
  int updateById(Long rclubId, RClub rClub);

  //목록
  List<RClub> findAll();

  List<RClub> findAll(Long reqPage, Long recordCnt);

  int totalCnt();
}
