package com.kh.hikers.domain.managerBbs.dao;


import com.kh.hikers.domain.entity.ManagerBbs;

import java.util.List;

public interface ManagerBbsDAO {

  //신고된 게시판만 조회하기
  List<ManagerBbs> mReadComplain();

  //삭제하기(상태만 변경)
  int deleteComplain(List<Long> bbsIds);

  //조인해서 불러오기(게시글, 회원정보, 산)
  List<ManagerBbs> viewBbsAll();

  // 제목으로 게시글 찾기
  List <ManagerBbs> searchByTitle(String title);

  //산 이름으로 게시글 찾기
  List <ManagerBbs> searchBymntnNm(String mntnNm);

  // 회원 닉네임으로 게시글 찾기
  List <ManagerBbs> searchBynickname(String nickname);

  // 전체 검색
  List <ManagerBbs> searchByAll(String keyword);

  // 전체 조회(페이징)
  List <ManagerBbs> viewBbsAll(Long reqPage, Long recordCnt);

  //총레코드 건수
  int totalCnt();

  //단건 조회(상세조회)

}
