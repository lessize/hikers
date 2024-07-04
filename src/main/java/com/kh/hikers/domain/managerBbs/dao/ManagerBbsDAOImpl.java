package com.kh.hikers.domain.managerBbs.dao;

import com.kh.hikers.domain.entity.ManagerBbs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class ManagerBbsDAOImpl implements ManagerBbsDAO {
  private final NamedParameterJdbcTemplate template;

  ManagerBbsDAOImpl(NamedParameterJdbcTemplate template) {
    this.template = template;
  }

  @Override
  public List<ManagerBbs> mReadComplain() {
    String query = " SELECT " +
            " m.member_id AS member_id, " +
            " m.nickname AS member_nickname, " +
            " b.bbs_id AS bbs_id , " +
            " b.title AS bbs_title, " +
            " b.bcontent AS bbs_content, " +
            " b.hit As bbs_hit, " +
            " b.staring AS bbs_staring, " +
            " b.ctime As bbs_ctime, " +
            " b.status As bbs_status, " +
            " b.cdate AS bbs_cdate, " +
            " b.udate AS bbs_udate, " +
            " mt.MNTN_CODE AS mountain_code, " +
            " mt.MNTN_NM AS mountain_name, " +
            " mt.MNTN_loc as mountain_loc " +
            " FROM bbs b " +
            " RIGHT JOIN member m ON m.member_id = b.member_id " +
            " LEFT JOIN mountain mt ON b.MNTN_CODE = mt.MNTN_CODE " +
            " Where b.status != 'D' and b.status = 'W' " +
            " order by b.cdate desc " ;

    List<ManagerBbs> list = template.query(query, (ResultSet rs) -> {
      List<ManagerBbs> resultList = new ArrayList<>();
      while (rs.next()) {
        ManagerBbs managerBbs = new ManagerBbs();
        managerBbs.setMemberId(rs.getLong("member_id"));
        managerBbs.setNickname(rs.getString("member_nickname"));
        managerBbs.setBbsId(rs.getLong("bbs_id"));
        managerBbs.setTitle(rs.getString("bbs_title"));
        managerBbs.setBcontent(rs.getString("bbs_content"));
        managerBbs.setHit(rs.getInt("bbs_hit"));
        managerBbs.setStaring(rs.getInt("bbs_staring"));
        managerBbs.setCtime(rs.getInt("bbs_ctime"));
        managerBbs.setStatus(rs.getString("bbs_status"));
        Timestamp cdateTimestamp = rs.getTimestamp("bbs_cdate");
        managerBbs.setCdate(cdateTimestamp != null ? cdateTimestamp.toLocalDateTime() : null);
        Timestamp udateTimestamp = rs.getTimestamp("bbs_udate");
        managerBbs.setUdate(udateTimestamp != null ? udateTimestamp.toLocalDateTime() : null);
        managerBbs.setMntnCode(rs.getLong("mountain_code"));
        managerBbs.setMntnNm(rs.getString("mountain_name"));
        managerBbs.setMntnLoc(rs.getString("mountain_loc"));
        resultList.add(managerBbs);
      }
      return resultList;
    });
    return list;
  }
//    StringBuffer sql = new StringBuffer();
//    sql.append(" select BBS_ID, MNTN_CODE, TITLE, BCONTENT, STATUS, CDATE, UDATE ");
//    sql.append(" from BBS ");
//    sql.append(" where STATUS = 'W' ");
//    sql.append(" order by cdate desc ");
//    List<ManagerBbs> list = template.query(sql.toString(), BeanPropertyRowMapper.newInstance(ManagerBbs.class));
//    return list;
//  }

  @Override
  public int deleteComplain(List<Long> bbsIds) {
    StringBuffer sql = new StringBuffer();
    sql.append(" UPDATE BBS");
    sql.append(" SET STATUS = 'D' ");
    sql.append(" where bbs_id in (:bbsIds) ");

    Map<String, List<Long>> map = Map.of("bbsIds", bbsIds);
    int deletedRowCnt = template.update(sql.toString(), map);
    return deletedRowCnt;
  }

  //게시글 관련 모든 정보 들고오기
  @Override
  public List<ManagerBbs> viewBbsAll() {
    String query = " SELECT " +
            " m.member_id AS member_id, " +
            " m.nickname AS member_nickname, " +
            " b.bbs_id AS bbs_id , " +
            " b.title AS bbs_title, " +
            " b.bcontent AS bbs_content, " +
            " b.hit As bbs_hit, " +
            " b.staring AS bbs_staring, " +
            " b.ctime As bbs_ctime, " +
            " b.status As bbs_status, " +
            " b.cdate AS bbs_cdate, " +
            " b.udate AS bbs_udate, " +
            " mt.MNTN_CODE AS mountain_code, " +
            " mt.MNTN_NM AS mountain_name, " +
            " mt.MNTN_loc as mountain_loc " +
            " FROM bbs b " +
            " RIGHT JOIN member m ON m.member_id = b.member_id " +
            " LEFT JOIN mountain mt ON b.MNTN_CODE = mt.MNTN_CODE " +
            " Where b.status != 'D' " +
            " order by b.cdate desc " ;

    List<ManagerBbs> list = template.query(query, (ResultSet rs) -> {
      List<ManagerBbs> resultList = new ArrayList<>();
      while (rs.next()) {
        ManagerBbs managerBbs = new ManagerBbs();
        managerBbs.setMemberId(rs.getLong("member_id"));
        managerBbs.setNickname(rs.getString("member_nickname"));
        managerBbs.setBbsId(rs.getLong("bbs_id"));
        managerBbs.setTitle(rs.getString("bbs_title"));
        managerBbs.setBcontent(rs.getString("bbs_content"));
        managerBbs.setHit(rs.getInt("bbs_hit"));
        managerBbs.setStaring(rs.getInt("bbs_staring"));
        managerBbs.setCtime(rs.getInt("bbs_ctime"));
        managerBbs.setStatus(rs.getString("bbs_status"));
        Timestamp cdateTimestamp = rs.getTimestamp("bbs_cdate");
        managerBbs.setCdate(cdateTimestamp != null ? cdateTimestamp.toLocalDateTime() : null);
        Timestamp udateTimestamp = rs.getTimestamp("bbs_udate");
        managerBbs.setUdate(udateTimestamp != null ? udateTimestamp.toLocalDateTime() : null);
        managerBbs.setMntnCode(rs.getLong("mountain_code"));
        managerBbs.setMntnNm(rs.getString("mountain_name"));
        managerBbs.setMntnLoc(rs.getString("mountain_loc"));
        resultList.add(managerBbs);
      }
      return resultList;
    });
    return list;
  }

  // 제목으로 게시글 찾기
  @Override
  public List<ManagerBbs> searchByTitle(String title) {
    String query = "SELECT " +
            " m.member_id AS member_id, " +
            " m.nickname AS member_nickname, " +
            " b.bbs_id AS bbs_id , " +
            " b.title AS bbs_title, " +
            " b.bcontent AS bbs_content, " +
            " b.hit AS bbs_hit, " +
            " b.staring AS bbs_staring, " +
            " b.ctime AS bbs_ctime, " +
            " b.status AS bbs_status, " +
            " b.cdate AS bbs_cdate, " +
            " b.udate AS bbs_udate, " +
            " mt.MNTN_CODE AS mountain_code, " +
            " mt.MNTN_NM AS mountain_name, " +
            " mt.MNTN_loc AS mountain_loc " +
            " FROM bbs b " +
            " RIGHT JOIN member m ON m.member_id = b.member_id " +
            " LEFT JOIN mountain mt ON b.MNTN_CODE = mt.MNTN_CODE " +
            " WHERE b.status != 'D' AND ( b.title LIKE :title ) " +
            " order by b.cdate desc ";

    Map<String, Object> params = new HashMap<>();
    params.put("title", "%" + title + "%");

    List<ManagerBbs> list = template.query(query, params, (ResultSet rs) -> {
      List<ManagerBbs> resultList = new ArrayList<>();
      while (rs.next()) {
        ManagerBbs managerBbs = new ManagerBbs();
        managerBbs.setMemberId(rs.getLong("member_id"));
        managerBbs.setNickname(rs.getString("member_nickname"));
        managerBbs.setBbsId(rs.getLong("bbs_id"));
        managerBbs.setTitle(rs.getString("bbs_title"));
        managerBbs.setBcontent(rs.getString("bbs_content"));
        managerBbs.setHit(rs.getInt("bbs_hit"));
        managerBbs.setStaring(rs.getInt("bbs_staring"));
        managerBbs.setCtime(rs.getInt("bbs_ctime"));
        managerBbs.setStatus(rs.getString("bbs_status"));
        Timestamp cdateTimestamp = rs.getTimestamp("bbs_cdate");
        managerBbs.setCdate(cdateTimestamp != null ? cdateTimestamp.toLocalDateTime() : null);
        Timestamp udateTimestamp = rs.getTimestamp("bbs_udate");
        managerBbs.setUdate(udateTimestamp != null ? udateTimestamp.toLocalDateTime() : null);
        managerBbs.setMntnCode(rs.getLong("mountain_code"));
        managerBbs.setMntnNm(rs.getString("mountain_name"));
        managerBbs.setMntnLoc(rs.getString("mountain_loc"));
        resultList.add(managerBbs);
      }
      return resultList;
    });
    return list;
  }

  //산이름으로 게시글 찾기
  @Override
  public List<ManagerBbs> searchBymntnNm(String mntnNm) {
    String query = "SELECT " +
            " m.member_id AS member_id, " +
            " m.nickname AS member_nickname, " +
            " b.bbs_id AS bbs_id, " +
            " b.title AS bbs_title, " +
            " b.bcontent AS bbs_content, " +
            " b.hit AS bbs_hit, " +
            " b.staring AS bbs_staring, " +
            " b.ctime AS bbs_ctime, " +
            " b.status AS bbs_status, " +
            " b.cdate AS bbs_cdate, " +
            " b.udate AS bbs_udate, " +
            " mt.MNTN_CODE AS mountain_code, " +
            " mt.MNTN_NM AS mountain_name, " +
            " mt.MNTN_loc AS mountain_loc " +
            " FROM bbs b " +
            " RIGHT JOIN member m ON m.member_id = b.member_id " +
            " LEFT JOIN mountain mt ON b.MNTN_CODE = mt.MNTN_CODE " +
            " WHERE b.status != 'D' AND " +
            " mt.MNTN_NM LIKE :mntnNm " +
            " order by b.cdate desc ";

    Map<String, Object> params = new HashMap<>();
    params.put("mntnNm", "%" + mntnNm + "%");

    List<ManagerBbs> list = template.query(query, params, (ResultSet rs) -> {
      List<ManagerBbs> resultList = new ArrayList<>();
      while (rs.next()) {
        ManagerBbs managerBbs = new ManagerBbs();
        managerBbs.setMemberId(rs.getLong("member_id"));
        managerBbs.setNickname(rs.getString("member_nickname"));
        managerBbs.setBbsId(rs.getLong("bbs_id"));
        managerBbs.setTitle(rs.getString("bbs_title"));
        managerBbs.setBcontent(rs.getString("bbs_content"));
        managerBbs.setHit(rs.getInt("bbs_hit"));
        managerBbs.setStaring(rs.getInt("bbs_staring"));
        managerBbs.setCtime(rs.getInt("bbs_ctime"));
        managerBbs.setStatus(rs.getString("bbs_status"));
        Timestamp cdateTimestamp = rs.getTimestamp("bbs_cdate");
        managerBbs.setCdate(cdateTimestamp != null ? cdateTimestamp.toLocalDateTime() : null);
        Timestamp udateTimestamp = rs.getTimestamp("bbs_udate");
        managerBbs.setUdate(udateTimestamp != null ? udateTimestamp.toLocalDateTime() : null);
        managerBbs.setMntnCode(rs.getLong("mountain_code"));
        managerBbs.setMntnNm(rs.getString("mountain_name"));
        managerBbs.setMntnLoc(rs.getString("mountain_loc"));
        resultList.add(managerBbs);
      }
      return resultList;
    });
    return list;
  }

  // 작성자 닉네임으로 게시글 찾기
  @Override
  public List<ManagerBbs> searchBynickname(String nickname) {
    String query = "SELECT " +
            " m.member_id AS member_id, " +
            " m.nickname AS member_nickname, " +
            " b.bbs_id AS bbs_id, " +
            " b.title AS bbs_title, " +
            " b.bcontent AS bbs_content, " +
            " b.hit AS bbs_hit, " +
            " b.staring AS bbs_staring, " +
            " b.ctime AS bbs_ctime, " +
            " b.status AS bbs_status, " +
            " b.cdate AS bbs_cdate, " +
            " b.udate AS bbs_udate, " +
            " mt.MNTN_CODE AS mountain_code, " +
            " mt.MNTN_NM AS mountain_name, " +
            " mt.MNTN_loc AS mountain_loc " +
            " FROM bbs b " +
            " RIGHT JOIN member m ON m.member_id = b.member_id " +
            " LEFT JOIN mountain mt ON b.MNTN_CODE = mt.MNTN_CODE " +
            " WHERE b.status != 'D' AND ( m.nickname LIKE :nickname ) " +
            " order by b.cdate desc ";

    Map<String, Object> params = new HashMap<>();
    params.put("nickname", "%" + nickname + "%");

    List<ManagerBbs> list = template.query(query, params, (ResultSet rs) -> {
      List<ManagerBbs> resultList = new ArrayList<>();
      while (rs.next()) {
        ManagerBbs managerBbs = new ManagerBbs();
        managerBbs.setMemberId(rs.getLong("member_id"));
        managerBbs.setNickname(rs.getString("member_nickname"));
        managerBbs.setBbsId(rs.getLong("bbs_id"));
        managerBbs.setTitle(rs.getString("bbs_title"));
        managerBbs.setBcontent(rs.getString("bbs_content"));
        managerBbs.setHit(rs.getInt("bbs_hit"));
        managerBbs.setStaring(rs.getInt("bbs_staring"));
        managerBbs.setCtime(rs.getInt("bbs_ctime"));
        managerBbs.setStatus(rs.getString("bbs_status"));
        Timestamp cdateTimestamp = rs.getTimestamp("bbs_cdate");
        managerBbs.setCdate(cdateTimestamp != null ? cdateTimestamp.toLocalDateTime() : null);
        Timestamp udateTimestamp = rs.getTimestamp("bbs_udate");
        managerBbs.setUdate(udateTimestamp != null ? udateTimestamp.toLocalDateTime() : null);
        managerBbs.setMntnCode(rs.getLong("mountain_code"));
        managerBbs.setMntnNm(rs.getString("mountain_name"));
        managerBbs.setMntnLoc(rs.getString("mountain_loc"));
        resultList.add(managerBbs);
      }
      return resultList;
    });
    return list;
  }

  //전체 검색

  @Override
  public List<ManagerBbs> searchByAll(String keyword) {
    String query = "SELECT " +
            " m.member_id AS member_id, " +
            " m.nickname AS member_nickname, " +
            " b.bbs_id AS bbs_id, " +
            " b.title AS bbs_title, " +
            " b.bcontent AS bbs_content, " +
            " b.hit AS bbs_hit, " +
            " b.staring AS bbs_staring, " +
            " b.ctime AS bbs_ctime, " +
            " b.status AS bbs_status, " +
            " b.cdate AS bbs_cdate, " +
            " b.udate AS bbs_udate, " +
            " mt.MNTN_CODE AS mountain_code, " +
            " mt.MNTN_NM AS mountain_name, " +
            " mt.MNTN_loc AS mountain_loc " +
            " FROM bbs b " +
            " RIGHT JOIN member m ON m.member_id = b.member_id " +
            " LEFT JOIN mountain mt ON b.MNTN_CODE = mt.MNTN_CODE " +
            " WHERE b.status != 'D' " +
            " AND ( m.nickname LIKE :keyword" +
            " OR mt.MNTN_NM LIKE :keyword" +
            " OR b.title LIKE :keyword ) " +
            " order by b.cdate desc " ;

    Map<String, Object> params = new HashMap<>();
    params.put("keyword", "%" + keyword + "%");

    List<ManagerBbs> list = template.query(query, params, (ResultSet rs) -> {
      List<ManagerBbs> resultList = new ArrayList<>();
      while (rs.next()) {
        ManagerBbs managerBbs = new ManagerBbs();
        managerBbs.setMemberId(rs.getLong("member_id"));
        managerBbs.setNickname(rs.getString("member_nickname"));
        managerBbs.setBbsId(rs.getLong("bbs_id"));
        managerBbs.setTitle(rs.getString("bbs_title"));
        managerBbs.setBcontent(rs.getString("bbs_content"));
        managerBbs.setHit(rs.getInt("bbs_hit"));
        managerBbs.setStaring(rs.getInt("bbs_staring"));
        managerBbs.setCtime(rs.getInt("bbs_ctime"));
        managerBbs.setStatus(rs.getString("bbs_status"));
        Timestamp cdateTimestamp = rs.getTimestamp("bbs_cdate");
        managerBbs.setCdate(cdateTimestamp != null ? cdateTimestamp.toLocalDateTime() : null);
        Timestamp udateTimestamp = rs.getTimestamp("bbs_udate");
        managerBbs.setUdate(udateTimestamp != null ? udateTimestamp.toLocalDateTime() : null);
        managerBbs.setMntnCode(rs.getLong("mountain_code"));
        managerBbs.setMntnNm(rs.getString("mountain_name"));
        managerBbs.setMntnLoc(rs.getString("mountain_loc"));
        resultList.add(managerBbs);
      }
      return resultList;
    });
    return list;
  }

  @Override
  public List<ManagerBbs> viewBbsAll(Long reqPage, Long recCnt) {
    StringBuffer sql = new StringBuffer();
    sql.append(" SELECT ")
            .append(" m.member_id AS member_id, ")
            .append(" m.nickname AS member_nickname, ")
            .append(" b.bbs_id AS bbs_id , ")
            .append(" b.title AS bbs_title, ")
            .append(" b.bcontent AS bbs_content, ")
            .append(" b.hit AS bbs_hit, ")
            .append(" b.staring AS bbs_staring, ")
            .append(" b.ctime AS bbs_ctime, ")
            .append(" b.status AS bbs_status, ")
            .append(" b.cdate AS bbs_cdate, ")
            .append(" b.udate AS bbs_udate, ")
            .append(" mt.MNTN_CODE AS mountain_code, ")
            .append(" mt.MNTN_NM AS mountain_name, ")
            .append(" mt.MNTN_loc AS mountain_loc ")
            .append(" FROM bbs b ")
            .append(" RIGHT JOIN member m ON m.member_id = b.member_id ")
            .append(" LEFT JOIN mountain mt ON b.MNTN_CODE = mt.MNTN_CODE ")
            .append(" WHERE b.status != 'D' ")
            .append(" order by b.cdate desc ")
            .append(" OFFSET (:reqPage - 1) * :recCnt ROWS FETCH FIRST :recCnt ROWS ONLY ");

    Map<String, Object> params = new HashMap<>();
    params.put("reqPage", reqPage);
    params.put("recCnt", recCnt);

    List<ManagerBbs> list = template.query(sql.toString(), params, (ResultSet rs) -> {
      List<ManagerBbs> resultList = new ArrayList<>();
      while (rs.next()) {
        ManagerBbs managerBbs = new ManagerBbs();
        managerBbs.setMemberId(rs.getLong("member_id"));
        managerBbs.setNickname(rs.getString("member_nickname"));
        managerBbs.setBbsId(rs.getLong("bbs_id"));
        managerBbs.setTitle(rs.getString("bbs_title"));
        managerBbs.setBcontent(rs.getString("bbs_content"));
        managerBbs.setHit(rs.getInt("bbs_hit"));
        managerBbs.setStaring(rs.getInt("bbs_staring"));
        managerBbs.setCtime(rs.getInt("bbs_ctime"));
        managerBbs.setStatus(rs.getString("bbs_status"));
        Timestamp cdateTimestamp = rs.getTimestamp("bbs_cdate");
        managerBbs.setCdate(cdateTimestamp != null ? cdateTimestamp.toLocalDateTime() : null);
        Timestamp udateTimestamp = rs.getTimestamp("bbs_udate");
        managerBbs.setUdate(udateTimestamp != null ? udateTimestamp.toLocalDateTime() : null);
        managerBbs.setMntnCode(rs.getLong("mountain_code"));
        managerBbs.setMntnNm(rs.getString("mountain_name"));
        managerBbs.setMntnLoc(rs.getString("mountain_loc"));
        resultList.add(managerBbs);
      }
      return resultList;
    });

    return list;
  }

  //토탈 건수
  @Override
  public int totalCnt() {
    String sql =" SELECT count(bbs_id) from bbs where status != 'D' ";
    SqlParameterSource param = new MapSqlParameterSource();
    Integer cnt = template.queryForObject(sql, param, Integer.class);

    return cnt;
  }
}