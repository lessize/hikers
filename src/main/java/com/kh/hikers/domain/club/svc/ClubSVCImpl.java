package com.kh.hikers.domain.club.svc;

import com.kh.hikers.domain.club.dao.ClubDAO;
import com.kh.hikers.domain.entity.Club;
import com.kh.hikers.domain.entity.Mountain;
import com.kh.hikers.domain.entity.UploadFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClubSVCImpl implements ClubSVC {

  private final ClubDAO clubDAO;

  //등록
  @Override
  public Long save(Club club) {
    return clubDAO.save(club);
  }

  //삭제
  @Override
  public int deleteById(Long clubId) {
    return clubDAO.deleteById(clubId);
  }

  //여러건삭제
  @Override
  public int deleteByIds(List<Long> clubId) {
    return clubDAO.deleteByIds(clubId);
  }

  //조회
  @Override
  public Optional<Club> findById(Long clubId) {
    return clubDAO.findById(clubId);
  }


  @Override
  public int updateById(Long clubId, Club club) {
    return clubDAO.updateById(clubId, club);
  }

  //목록
  @Override
  public List<Club> findAll() {
    return clubDAO.findAll();
  }

  @Override
  public List<Club> findAll(Long reqPage, Long recordCnt) {
    return clubDAO.findAll(reqPage,recordCnt);
  }

  @Override
  public int totalCnt() {
    return clubDAO.totalCnt();
  }

  public List<Mountain> selectmntn(String mntnNm){return clubDAO.selectmntn(mntnNm);}

  @Override
  public List<Mountain> outmntn(Long clubId){return clubDAO.outmntn(clubId);}
}
