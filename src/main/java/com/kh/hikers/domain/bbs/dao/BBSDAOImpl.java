package com.kh.hikers.domain.bbs.dao;

import com.kh.hikers.domain.entity.Bbs;
import com.kh.hikers.domain.entity.Mountain;
import com.kh.hikers.domain.entity.UploadFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BBSDAOImpl implements BBSDAO {

  private final NamedParameterJdbcTemplate template;


  @Override
  public Long save(Bbs bbs) {
    System.out.print(bbs);
    StringBuffer sql = new StringBuffer();
    sql.append("insert into bbs(bbs_id, mntn_code, title, member_id, bcontent, staring, ctime, code, tags) ");
    sql.append("values(bbs_id_seq.nextval, :mntnCode, :title, :memberId, :bcontent, :staring, :ctime,default, :tags) ");

    // SQL 파라미터 자동매핑
    SqlParameterSource param = new BeanPropertySqlParameterSource(bbs); //
    KeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(sql.toString(),param,keyHolder,new String[]{"bbs_id"});

    long bbsId = keyHolder.getKey().longValue();    //상품아이디
    return bbsId;
  }

  @Override
  public int deleteById(Long bbsId) {
    // 첫 번째 쿼리: rclub 테이블에서 해당 clubId를 삭제
    String deleteRClubSQL = "DELETE FROM rbbs WHERE bbs_id = :bbsId";
    SqlParameterSource rClubParam = new MapSqlParameterSource().addValue("bbsId", bbsId);
    int deletedRClubRowCnt = template.update(deleteRClubSQL, rClubParam);

    // 두 번째 쿼리: club 테이블에서 해당 clubId를 삭제
    String deleteClubSQL = "DELETE FROM bbs WHERE bbs_id = :bbsId";
    SqlParameterSource clubParam = new MapSqlParameterSource().addValue("bbsId", bbsId);
    int deletedClubRowCnt = template.update(deleteClubSQL, clubParam);

    // 삭제된 행의 총 수 반환
    return deletedRClubRowCnt + deletedClubRowCnt;
  }

  //여러건 삭제
  @Override
  public int deleteByIds(List<Long> bbsIds) {
    StringBuffer sql = new StringBuffer();
    sql.append("delete from bbs ");
    sql.append(" where bbs_id in (:bbsIds) ");

    Map<String,List<Long>> map = Map.of("bbsIds",bbsIds);
    int deletedRowCnt = template.update(sql.toString(), map);
    return deletedRowCnt;
  }

  //조회
  @Override
  public Optional<Bbs> findById(Long bbsId) {
    StringBuffer sql = new StringBuffer();
    sql.append("select b.bbs_id AS bbs_id,b.member_id AS member_id,m.nickname AS nickname,b.mntn_code AS mntn_code,b.title AS title," +
        "b.bcontent AS bcontent,b.staring AS staring,b.ctime AS ctime, b.code AS code,b.tags AS tags," +
        "b.cdate AS cdate,b.udate AS udate");
    sql.append("  from bbs b RIGHT JOIN member m ON m.member_id = b.member_id");
    sql.append(" where bbs_id = :bbsId ");
    try {
      Map<String, Long> map = Map.of("bbsId", bbsId);
      Bbs bbs = template.queryForObject(sql.toString(), map, BeanPropertyRowMapper.newInstance(Bbs.class));
      return Optional.of(bbs);

    } catch (EmptyResultDataAccessException e) {
      //조회결과가 없는경우
      return Optional.empty();
    }
  }

  //수정
  @Override
  public int updateById(Long bbsId, Bbs bbs) {
    StringBuffer sql = new StringBuffer();
    sql.append("update bbs ");
    sql.append("   set mntn_code = :mntnCode, ");
    sql.append("       title = :title, ");
    sql.append("       member_id = :memberId, ");
    sql.append("       bcontent = :bcontent, ");
    sql.append("       ctime = :ctime, ");
    sql.append("       staring = :staring, ");
    sql.append("       code = :code, ");
    sql.append("       tags = :tags, ");
    sql.append("       udate = default ");
    sql.append(" where bbs_id = :bbsId ");

    //sql 파라미터 변수에 값 매핑
    SqlParameterSource param = new MapSqlParameterSource()
            .addValue("mntnCode", bbs.getMntnCode())
            .addValue("title", bbs.getTitle())
            .addValue("memberId", bbs.getMemberId())
            .addValue("bcontent", bbs.getBcontent())
            .addValue("staring", bbs.getStaring())
            .addValue("ctime", bbs.getCtime())
            .addValue("checkboxData", bbs.getTags())
            .addValue("code", bbs.getCode())
            .addValue("tags", bbs.getTags())
            .addValue("bbsId", bbsId);

    //update수행 후 변경된 행수 반환
    int updateRowCnt = template.update(sql.toString(), param);

    return updateRowCnt;
  }
  //목록 세트
  @Override
  public List<Bbs> findAll() {
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
        " mt.MNTN_loc as mountain_loc, " +
        " b.code as code " +
        " FROM bbs b " +
        " RIGHT JOIN member m ON m.member_id = b.member_id " +
        " LEFT JOIN mountain mt ON b.MNTN_CODE = mt.MNTN_CODE " +
        " Where b.status != 'D' " +
        " order by b.cdate desc " ;
    List<Bbs> list = template.query(query, (ResultSet rs) -> {
      List<Bbs> resultList = new ArrayList<>();
      while (rs.next()) {
        Bbs bbs = new Bbs();
        bbs.setCode(rs.getString("code"));
        bbs.setBbsId(rs.getLong("bbs_id"));
        bbs.setMemberId(rs.getLong("member_id"));
        bbs.setTitle(rs.getString("bbs_title"));
        bbs.setMntnum(rs.getString("mountain_name"));
        bbs.setNickname(rs.getString("member_nickname"));
        Timestamp cdateTimestamp = rs.getTimestamp("bbs_cdate");
        bbs.setCdate(cdateTimestamp != null ? LocalDate.from(cdateTimestamp.toLocalDateTime()).atStartOfDay() : null);
        Timestamp udateTimestamp = rs.getTimestamp("bbs_udate");
        bbs.setUdate(udateTimestamp != null ? LocalDate.from(udateTimestamp.toLocalDateTime()).atStartOfDay() : null);
        resultList.add(bbs);
      }
      return resultList;
    });
    return list;
  }

  @Override
  public List<Bbs> findAll(Long reqPage, Long recCnt) {
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
        " mt.MNTN_loc AS mountain_loc, " +
        " b.code AS code " +
        " FROM bbs b " +
        " RIGHT JOIN member m ON m.member_id = b.member_id " +
        " LEFT JOIN mountain mt ON b.MNTN_CODE = mt.MNTN_CODE " +
        " Where b.status != 'D' " +
        " order by b.cdate desc " +
        " offset (:reqPage - 1) * :recCnt rows fetch first :recCnt rows only ";
    Map<String,Long> param = Map.of("reqPage",reqPage,"recCnt",recCnt);
    List<Bbs> list = template.query(query, param, (ResultSet rs) -> {
      List<Bbs> resultList = new ArrayList<>();
      while (rs.next()) {
        Bbs bbs = new Bbs();
        bbs.setCode(rs.getString("code"));
        bbs.setBbsId(rs.getLong("bbs_id"));
        bbs.setMemberId(rs.getLong("member_id"));
        bbs.setTitle(rs.getString("bbs_title"));
        bbs.setMntnum(rs.getString("mountain_name"));
        bbs.setNickname(rs.getString("member_nickname"));
        Timestamp cdateTimestamp = rs.getTimestamp("bbs_cdate");
        bbs.setCdate(cdateTimestamp != null ? LocalDate.from(cdateTimestamp.toLocalDateTime()).atStartOfDay() : null);
        Timestamp udateTimestamp = rs.getTimestamp("bbs_udate");
        bbs.setUdate(udateTimestamp != null ? LocalDate.from(udateTimestamp.toLocalDateTime()).atStartOfDay() : null);
        resultList.add(bbs);
      }
      return resultList;
    });
    return list;
  }

  @Override
  public int totalCnt() {
    String sql = "SELECT COUNT(bbs_id) FROM bbs ";

    SqlParameterSource param = new MapSqlParameterSource();
    Integer cnt = template.queryForObject(sql, param, Integer.class);

    return cnt;
  }

  @Override
  public List<Mountain> outmntn(Long mntnCode){
    StringBuffer sql = new StringBuffer();
    sql.append("select mntn_nm, mntn_loc");
    sql.append("  from mountain ");
    sql.append(" where mntn_code = :mntnCode ");
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("mntnCode", mntnCode);
    List<Mountain> mntnNm = template.query(sql.toString(), params, BeanPropertyRowMapper.newInstance(Mountain.class));
    return mntnNm;
  }

  @Override
  public List<Mountain> selectmntn(String mntnNm){
    log.info("product={}",mntnNm);
    StringBuffer sql = new StringBuffer();
    sql.append("select mntn_code, mntn_nm, mntn_loc, city");
    sql.append(" from mountain ");
    sql.append(" where mntn_nm = :mntnNm");
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("mntnNm", mntnNm);
    List<Mountain> response = template.query(sql.toString(),params, BeanPropertyRowMapper.newInstance(Mountain.class));
    return response;
  }

  @Override
  public List<UploadFile> selectpoto(String code, Long bbsId){
    String bbsIds = String.valueOf(bbsId);
    log.info("code={}, {}",code, bbsIds);
    StringBuffer sql = new StringBuffer();
    sql.append("select *");
    sql.append(" from uploadfile ");
    sql.append(" where RID = :bbsId");
    sql.append(" and code = :code");
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("code", code);
    params.addValue("bbsId", bbsIds);
    List<UploadFile> response = template.query(sql.toString(),params, BeanPropertyRowMapper.newInstance(UploadFile.class));
    return response;
  }
}