package com.kh.hikers.domain.rclub.svc;


import com.kh.hikers.domain.entity.RClub;

import java.util.List;

public interface RClubSVC {
  Long save(RClub rClub);

  int deleteById(Long rclubId);

  int deleteByIds(List<Long> rclubIds);

  int updateById(Long clubId, RClub rClub);

  List<RClub> findAll();

  List<RClub> findAll(Long reqPage, Long recordCnt);

  int totalCnt();

}
