package com.kh.hikers.domain.bbs.svc;


import com.kh.hikers.domain.entity.Bbs;
import com.kh.hikers.domain.entity.Mountain;
import com.kh.hikers.domain.entity.UploadFile;

import java.util.List;
import java.util.Optional;

public interface BBSSVC {
  Long save(Bbs bbs);

  int deleteById(Long bbsId);

  int deleteByIds(List<Long> bbsIds);

  Optional<Bbs> findById(Long bbsId);

  int updateById(Long bbsId, Bbs bbs);

  List<Bbs> findAll();

  List<Bbs> findAll(Long reqPage, Long recordCnt);

  int totalCnt();

  List<Mountain> outmntn(Long MntnCode);

  List<Mountain> selectmntn(String mntnNm);

  List<UploadFile> selectpoto(String code, Long bbsId);
}