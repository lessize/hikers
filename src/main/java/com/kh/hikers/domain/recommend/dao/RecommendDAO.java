package com.kh.hikers.domain.recommend.dao;


import com.kh.hikers.domain.entity.Bbs;
import com.kh.hikers.domain.entity.Mountain;

import java.util.List;

public interface RecommendDAO {
  // 100대 명산 목록
  List<Mountain> top100MountainsList();

  // 사용자 게시글의 별점을 기준으로 추천하는 함수
  List<Mountain>mountainsRating();

  // 특정 산의 사용자 게시글의 본문내용을 가져오는 함수
  List<Bbs> mountainBcontent(Long mntnCode);

  // 사용자의 등산 경험을 가져오는 함수
  int getMemberExperience(Long memberId);

  // 회원 정보를 이용하여 개인별 추천을 하는 함수
  List<Mountain> personalRecommend(Long memberId);

  // 산의 이미지를 가져오는 함수
  public byte[] getMountainImage(Long mntnCode);

  // 산의 난이도를 계산하는 함수
  int mountaindiffCal(Long mntnCode);

  //총레코드 건수
  int totalCnt();
}
