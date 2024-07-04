package com.kh.hikers.domain.recommend.svc;

import com.kh.hikers.domain.entity.Bbs;
import com.kh.hikers.domain.entity.Mountain;
import com.kh.hikers.domain.recommend.dao.RecommendDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service //SVC 역할을 하는 클래스
public class RecommendSVCImpl implements RecommendSVC {

  private RecommendDAO recommendDAO;

  RecommendSVCImpl(RecommendDAO recommendDAO) {
    this.recommendDAO = recommendDAO;
  }

  @Override
  public List<Mountain> top100MountainsList() {
    return recommendDAO.top100MountainsList();
  }


  // 별점 순으로 산 정보와 산들의 본문내용을 가져옴
  @Override
  public List<Map<String, Object>> mountainsRating() {
    // 별점 높은 순으로 상위 10개의 산 정보를 가져옴
    List<Mountain> top10Mountains = recommendDAO.mountainsRating();

    // 각 산에 대해 게시글 내용을 가져와 결합
    return top10Mountains.stream().map(mountain -> {
      List<Bbs> posts = recommendDAO.mountainBcontent(mountain.getMntnCode());
      return Map.of(
              "mountain", mountain,
              "posts", posts
      );
    }).collect(Collectors.toList());
  }

  @Override
  public List<Mountain> personalRecommend(Long memberId) {
    // DAO를 통해 멤버 아이디를 사용하여 산 정보 리스트를 가져옴
    List<Mountain> mountains = recommendDAO.personalRecommend(memberId);
    // 멤버의 경험을 가져옴
    int memberExperience = recommendDAO.getMemberExperience(memberId);

    List<Mountain> recommendedMountains = new ArrayList<>();
    List<Mountain> otherMountains = new ArrayList<>();

    // 각 산의 난이도를 계산하여 설정 및 비교
    for (Mountain mountain : mountains) {
      int difficulty = recommendDAO.mountaindiffCal(mountain.getMntnCode());
      mountain.setMntnDffl(difficulty);

      // 멤버의 경험과 산의 난이도가 같을 경우 우선적으로 추천 목록에 추가
      if (difficulty == memberExperience) {
        recommendedMountains.add(mountain);
      } else if (difficulty < memberExperience) {
        // 난이도가 낮은 경우 다른 리스트에 추가
        otherMountains.add(mountain);
      }
      // 난이도가 높은 경우에는 추가하지 않음
    }

    // 우선 순위가 없는 산들을 추천 목록에 추가
    recommendedMountains.addAll(otherMountains);

    return recommendedMountains;
  }

  @Override
  public byte[] getMountainImage(Long mntnCode) {
    return recommendDAO.getMountainImage(mntnCode);
  }

  @Override
  public int totalCnt() {
    return recommendDAO.totalCnt();
  }
}
