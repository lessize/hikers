package com.kh.hikers.domain.recommend.svc;


import com.kh.hikers.domain.entity.Mountain;

import java.util.List;
import java.util.Map;

public interface RecommendSVC {
  // 100대 명산 목록
  List<Mountain> top100MountainsList();

//  List<Map<String, Object>> mountainsRating(String order);

  public List<Map<String, Object>> mountainsRating();

  public List<Mountain> personalRecommend(Long memberId);

  // 산의 이미지를 가져오는 함수
  public byte[] getMountainImage(Long mntnCode);

  int totalCnt();
}
