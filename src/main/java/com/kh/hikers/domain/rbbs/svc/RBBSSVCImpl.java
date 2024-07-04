package com.kh.hikers.domain.rbbs.svc;

import com.kh.hikers.domain.entity.RBBS;
import com.kh.hikers.domain.rbbs.dao.RBBsDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RBBSSVCImpl implements RBBSSVC {

  private final RBBsDAO RBBsDAO;

  //등록
  @Override
  public Long save(RBBS rbbs) {
    return RBBsDAO.save(rbbs);
  }

  //삭제
  @Override
  public int deleteById(Long rbbsId) {
    return RBBsDAO.deleteById(rbbsId);
  }

  //여러건삭제
  @Override
  public int deleteByIds(List<Long> rbbsIds) {
    return RBBsDAO.deleteByIds(rbbsIds);
  }

  @Override
  public int updateById(Long rbbsId, RBBS rbbs) {
    return RBBsDAO.updateById(rbbsId, rbbs);
  }

  //목록
  @Override
  public List<RBBS> findAll() {
    return RBBsDAO.findAll();
  }

  @Override
  public List<RBBS> findAll(Long reqPage, Long recordCnt) {
    return RBBsDAO.findAll(reqPage,recordCnt);
  }

  @Override
  public int totalCnt() {
    return RBBsDAO.totalCnt();
  }

}
