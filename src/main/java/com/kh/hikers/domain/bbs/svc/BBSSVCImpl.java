package com.kh.hikers.domain.bbs.svc;

import com.kh.hikers.domain.bbs.dao.BBSDAO;
import com.kh.hikers.domain.entity.Bbs;
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
public class BBSSVCImpl implements BBSSVC {

  private final BBSDAO bbsdao;

  //등록
  @Override
  public Long save(Bbs bbs) {
    return bbsdao.save(bbs);
  }

  //삭제
  @Override
  public int deleteById(Long bbsId) {
    return bbsdao.deleteById(bbsId);
  }

  //여러건삭제
  @Override
  public int deleteByIds(List<Long> bbsIds) {
    return bbsdao.deleteByIds(bbsIds);
  }

  //조회
  @Override
  public Optional<Bbs> findById(Long bbsId) {
    return bbsdao.findById(bbsId);
  }


  @Override
  public int updateById(Long bbsId, Bbs bbs) {
    return bbsdao.updateById(bbsId, bbs);
  }
  
  //목록
  @Override
  public List<Bbs> findAll() {
    return bbsdao.findAll();
  }

  @Override
  public List<Bbs> findAll(Long reqPage, Long recordCnt) {
    return bbsdao.findAll(reqPage,recordCnt);
  }

  @Override
  public int totalCnt() {
    return bbsdao.totalCnt();
  }

  @Override
  public List<Mountain> outmntn(Long MntnCode){return bbsdao.outmntn(MntnCode);}

  @Override
  public List<Mountain> selectmntn(String mntnNm){return bbsdao.selectmntn(mntnNm);}

  @Override
  public List<UploadFile> selectpoto(String code, Long bbsId){return bbsdao.selectpoto(code,bbsId);}
}