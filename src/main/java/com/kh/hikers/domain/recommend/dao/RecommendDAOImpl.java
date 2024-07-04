package com.kh.hikers.domain.recommend.dao;

import com.kh.hikers.domain.entity.Bbs;
import com.kh.hikers.domain.entity.Mountain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository //dao역할을 하는 클래스
public class RecommendDAOImpl implements RecommendDAO {

  private final NamedParameterJdbcTemplate template;

  RecommendDAOImpl(NamedParameterJdbcTemplate template) {
    this.template = template;
  }

  // 100대 명산 목록
  @Override
  public List<Mountain> top100MountainsList() {
    StringBuffer sql = new StringBuffer();
    sql.append("  SELECT MNTN_CODE, MNTN_LOC, MNTN_NM, LOT, LAT  ");
    sql.append("    FROM MOUNTAIN ");
    sql.append("   WHERE GUBUN = 'H' " );
    sql.append("ORDER BY MNTN_CODE ASC ");

    List<Mountain> list = template.query(sql.toString(), BeanPropertyRowMapper.newInstance(Mountain.class));
    return list;
  }

  // 별점 순 산 정보 제공
  @Override
  public List<Mountain> mountainsRating() {
    StringBuffer sql = new StringBuffer();

    sql.append(" SELECT mountain.MNTN_CODE, mountain.MNTN_NM, mountain.MNTN_LOC, ROUND(AVG(b.STARING), 1) AS AVG_STARING " );
    sql.append(" FROM BBS b " );
    sql.append(" JOIN mountain ON b.MNTN_CODE = mountain.MNTN_CODE " );
    sql.append(" GROUP BY mountain.MNTN_CODE, mountain.MNTN_NM, mountain.MNTN_LOC " );
    sql.append(" ORDER BY AVG_STARING DESC ");

    List<Mountain> list = template.query(sql.toString(), BeanPropertyRowMapper.newInstance(Mountain.class));

    return list;
  }

  // 산코드별 후기 본문내용 제공
  @Override
  public List<Bbs> mountainBcontent(Long mntnCode) {
    StringBuffer sql = new StringBuffer();
    sql.append(" SELECT b.BCONTENT ");
    sql.append(" FROM BBS b ");
    sql.append(" WHERE b.MNTN_CODE = :mntnCode ");

    Map<String, Long> paramMap = Map.of("mntnCode", mntnCode);
    List<Bbs> contentsList = template.query(sql.toString(), paramMap, new BeanPropertyRowMapper<>(Bbs.class));

    return contentsList;
  }

  // 사용자의 등산 경험을 가져옴
  public int getMemberExperience(Long memberId) {
    String sql = "SELECT MEXP FROM MEMBER WHERE MEMBER_ID = :memberId";

    SqlParameterSource param = new MapSqlParameterSource().addValue("memberId", memberId);

    int memberExp = template.update(sql.toString(), param);

    return memberExp;
  }

  // 개인별 추천
  @Override
  public List<Mountain> personalRecommend(Long memberId) {
    StringBuffer sql = new StringBuffer();

    sql.append(" SELECT MT.MNTN_CODE, MT.MNTN_NM, MT.MNTN_LOC " );
    sql.append(" FROM MOUNTAIN MT " );
    sql.append(" JOIN MEMBER M ON MT.CITY = M.LOC " );
    sql.append(" WHERE M.MEMBER_ID = :memberId " );

    Map<String, Object> params = new HashMap<>();
    params.put("memberId", memberId);

    List<Mountain> list = template.query(sql.toString(), params, (ResultSet rs) -> {
      List<Mountain> resultList = new ArrayList<>();
      while (rs.next()) {
        Mountain mountain = new Mountain();
        mountain.setMntnCode(rs.getLong("MNTN_CODE"));
        mountain.setMntnNm(rs.getString("MNTN_NM"));
        mountain.setMntnLoc(rs.getString("MNTN_LOC"));
        resultList.add(mountain);
      }
      return resultList;
    });
    return list;
  }

  // 산의 이미지를 가져오는 함수
  public byte[] getMountainImage(Long mntnCode) {
    String sql = " SELECT MNTN_PIC FROM MOUNTAIN WHERE MNTN_CODE = :mntnCode ";

    Map<String, Object> params = new HashMap<>();
    params.put("mntnCode", mntnCode);

    return template.queryForObject(sql, params, (ResultSet rs, int rowNum) -> rs.getBytes("MNTN_PIC"));
  }

  // 산 난이도 계산
  @Override
  public int mountaindiffCal(Long mntnCode) {
    StringBuffer sql = new StringBuffer();
    sql.append(" SELECT PMNTN_DFFL ");
    sql.append(" FROM COURSE ");
    sql.append(" WHERE MNTN_CODE = :mntnCode ");

    Map<String, Long> param = Map.of("mntnCode", mntnCode);
    List<Integer> difficultyList = template.query(sql.toString(), param, (rs, rowNum) -> rs.getInt("PMNTN_DFFL"));

    if (difficultyList.isEmpty()) {
      return 0; // 데이터가 없는 경우 0 반환
    }

    // 난이도별 가중치 (로그 스케일링 적용)
    double easyWeight = Math.log(56130 + 1);
    double mediumWeight = Math.log(844 + 1);
    double hardWeight = Math.log(21 + 1);

    // 총점을 계산
    double totalScore = 0;
    for (int difficulty : difficultyList) {
      switch (difficulty) {
        case 0:
          totalScore += easyWeight;
          break;
        case 1:
          totalScore += mediumWeight;
          break;
        case 2:
          totalScore += hardWeight;
          break;
      }
    }

    // 코스의 총 개수
    int courseCount = difficultyList.size();

    // 평균 점수 계산
    double averageScore = totalScore / courseCount;

    // 평균 점수에 따라 난이도 결정
    if (averageScore <= easyWeight) {
      return 0; // 쉬움
    } else if (averageScore <= mediumWeight) {
      return 1; // 중간
    } else {
      return 2; // 어려움
    }
  }

  //총레코드 건수
  @Override
  public int totalCnt() {
    String sql = "select count(MNTN_CODE) from MOUNTAIN";

    MapSqlParameterSource param = new MapSqlParameterSource();
    Integer cnt = template.queryForObject(sql, param, Integer.class);
    return cnt;
  }
}
