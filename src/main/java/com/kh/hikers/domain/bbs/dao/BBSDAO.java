package com.kh.hikers.domain.bbs.dao;

import com.kh.hikers.domain.entity.Bbs;
import com.kh.hikers.domain.entity.Mountain;
import com.kh.hikers.domain.entity.UploadFile;

import java.util.List;
import java.util.Optional;

public interface BBSDAO {
  
  //등록
  Long save(Bbs bbs);

  //삭제
  int deleteById(Long bbsId);

  int deleteByIds(List<Long> bbsIds);

  //조회
  Optional<Bbs> findById(Long bbsId);

  //수정
  int updateById(Long bbsId, Bbs bbs);

  //목록
  List<Bbs> findAll();

  List<Bbs> findAll(Long reqPage, Long recordCnt);

  int totalCnt();

  List<Mountain> outmntn(Long MntnCode);

  List<Mountain> selectmntn(String mntnNm);

  List<UploadFile> selectpoto(String code, Long bbsId);
}