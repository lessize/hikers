package com.kh.hikers.domain.rclub.svc;

import com.kh.hikers.domain.entity.RClub;
import com.kh.hikers.domain.rclub.dao.RClubDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RClubSVCImpl implements RClubSVC {

  private final RClubDAO rClubDAO;

  //등록
  @Override
  public Long save(RClub rClub) {
    return rClubDAO.save(rClub);
  }

  //삭제
  @Override
  public int deleteById(Long rclubId) {
    return rClubDAO.deleteById(rclubId);
  }

  //여러건삭제
  @Override
  public int deleteByIds(List<Long> rclubIds) {
    return rClubDAO.deleteByIds(rclubIds);
  }

  @Override
  public int updateById(Long rclubId, RClub rClub) {
    return rClubDAO.updateById(rclubId, rClub);
  }

  //목록
  @Override
  public List<RClub> findAll() {
    return rClubDAO.findAll();
  }

  @Override
  public List<RClub> findAll(Long reqPage, Long recordCnt) {
    return rClubDAO.findAll(reqPage,recordCnt);
  }

  @Override
  public int totalCnt() {
    return rClubDAO.totalCnt();
  }

}
