package com.kh.hikers.domain.mountain.dao;

import com.kh.hikers.domain.entity.Bbs;
import com.kh.hikers.domain.entity.Mountain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MountainDAOImpl implements MountainDAO {

  private final NamedParameterJdbcTemplate template;

  @Override
  public List<Mountain> cityMountain(String city) {
    String sql = "SELECT mntn_code, mntn_nm, city FROM mountain WHERE city = :city";
    SqlParameterSource param = new MapSqlParameterSource("city", city);
    return template.query(sql, param, mountainRowMapper());
  }

  @Override
  public List<Mountain> searchMountainByName(String name) {
    String sql = "SELECT mntn_code, mntn_nm, city FROM mountain WHERE mntn_nm LIKE :name";
    SqlParameterSource param = new MapSqlParameterSource("name", "%" + name + "%");
    return template.query(sql, param, mountainRowMapper());
  }

  @Override
  public List<Mountain> searchMountainByCode(Long mntnCode) {
    String sql = "SELECT mntn_code, mntn_nm, city FROM mountain WHERE mntn_code = :mntnCode";
    SqlParameterSource param = new MapSqlParameterSource("mntnCode", mntnCode);
    return template.query(sql, param, mountainRowMapper());
  }

  @Override
  public List<Mountain> getAllMountains() {
    String sql = "SELECT mntn_code, mntn_nm, city FROM mountain";
    return template.query(sql, mountainRowMapper());
  }

  private RowMapper<Mountain> mountainRowMapper() {
    return (rs, rowNum) -> {
      Mountain mountain = new Mountain();
      mountain.setMntnCode(rs.getLong("mntn_code"));
      mountain.setMntnNm(rs.getString("mntn_nm"));
      mountain.setCity(rs.getString("city"));
      return mountain;
    };
  }

  @Override
  public List<Bbs> goToBbs(Long mntnCode) {
    String sql = "SELECT * FROM BBS WHERE mntn_code = :mntnCode and status = 'N'";
    SqlParameterSource param = new MapSqlParameterSource("mntnCode", mntnCode);
    return template.query(sql, param, mntRowMapper());
  }


  private RowMapper<Bbs> mntRowMapper() {
    return (rs, rowNum) -> {
      Bbs bbs = new Bbs(); // Bbs 객체 생성
      // 여기서 Bbs 객체의 속성을 설정
      bbs.setBbsId(rs.getLong("BBS_ID"));
      bbs.setCode(rs.getString("CODE"));
      bbs.setTitle(rs.getString("TITLE"));
      bbs.setMemberId(rs.getLong("MEMBER_ID"));
      bbs.setHit(rs.getInt("HIT"));
      bbs.setBcontent(rs.getString("BCONTENT"));
      bbs.setStaring(rs.getLong("STARING"));
      bbs.setCtime(rs.getLong("CTIME"));
      bbs.setStatus(rs.getString("STATUS"));
      bbs.setCdate(rs.getTimestamp("CDATE").toLocalDateTime());
      bbs.setUdate(rs.getTimestamp("UDATE").toLocalDateTime());
      return bbs;
    };
  }

  @Override
  public String getCityByMntnCode(Long mntnCode) {
    String sql = "SELECT CITY FROM MOUNTAIN WHERE mntn_code = :mntnCode";
    SqlParameterSource param = new MapSqlParameterSource("mntnCode", mntnCode);
    return template.queryForObject(sql, param, String.class);
  }
}
