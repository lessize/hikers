package com.kh.hikers.domain.manager.dao;

import com.kh.hikers.domain.entity.Manager;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ManagerDAOImpl implements ManagerDAO {

  private final NamedParameterJdbcTemplate template;


  @Override
  public Long save(Manager manager) {
    System.out.print(manager);
    StringBuffer sql = new StringBuffer();
    sql.append("insert into manager(inquiry_id, inquiry_title, inquiry_content , member_id, INQUIRY_STATE, inquiry_cdate, inquiry_udate, inquiry_hidden) ");
    sql.append("values(MANAGER_INQUIRY_ID_SEQ.nextval, :inquiryTitle, :inquiryContent, :memberId, default, default, default, :inquiryHidden ) ");

    // SQL 파라미터 자동매핑
    SqlParameterSource param = new BeanPropertySqlParameterSource(manager); //
    KeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(sql.toString(),param,keyHolder,new String[]{"inquiry_id"});

    long inquiryId = keyHolder.getKey().longValue();    //상품아이디
    return inquiryId;
  }

  @Override
  public int deleteById(Long inquiryId) {
    StringBuffer sql = new StringBuffer();
    sql.append("delete from manager ");
    sql.append(" where inquiry_id = :inquiryId ");

    SqlParameterSource param = new MapSqlParameterSource()
            .addValue("inquiryId",inquiryId);

    int deletedRowCnt = template.update(sql.toString(), param);

    return deletedRowCnt;
  }

  //여러건 삭제
  @Override
  public int deleteByIds(List<Long> inquiryIds) {
    StringBuffer sql = new StringBuffer();
    sql.append("delete from manager ");
    sql.append(" where inquiry_id in (:inquiryIds) ");

    Map<String,List<Long>> map = Map.of("inquiryIds",inquiryIds);
    int deletedRowCnt = template.update(sql.toString(), map);
    return deletedRowCnt;
  }

  //조회
  @Override
  public Optional<Manager> findById(Long inquiryId) {
    StringBuffer sql = new StringBuffer();
    sql.append("select b.inquiry_id AS inquiry_id,b.member_id AS member_id,m.nickname AS nickname,b.INQUIRY_TITLE AS INQUIRY_TITLE," +
        "b.INQUIRY_CONTENT AS INQUIRY_CONTENT,b.INQUIRY_COMMENT AS INQUIRY_COMMENT,b.INQUIRY_COMMENT_CDATE AS INQUIRY_COMMENT_CDATE, b.INQUIRY_COMMENT_UDATE AS INQUIRY_COMMENT_UDATE,b.INQUIRY_HIDDEN AS INQUIRY_HIDDEN," +
        "b.INQUIRY_CDATE AS INQUIRY_CDATE,b.INQUIRY_UDATE AS INQUIRY_UDATE");
    sql.append("  from manager b RIGHT JOIN member m ON m.member_id = b.member_id");
    sql.append(" where inquiry_id = :inquiryId ");
    try {
      Map<String, Long> map = Map.of("inquiryId", inquiryId);
      Manager manager = template.queryForObject(sql.toString(), map, BeanPropertyRowMapper.newInstance(Manager.class));
      return Optional.of(manager);

    } catch (EmptyResultDataAccessException e) {
      //조회결과가 없는경우
      return Optional.empty();
    }
  }

  //수정
  @Override
  public int updateById(Long inquiryId, Manager manager) {
    StringBuffer sql = new StringBuffer();
    sql.append("update manager ");
    sql.append("   set inquiry_title = :inquiryTitle, ");
    sql.append("       inquiry_content = :inquiryContent, ");
    sql.append("       member_id = :memberId, ");
    sql.append("       inquiry_udate = default, ");
    sql.append(" where inquiry_id = :inquiryId ");

    //sql 파라미터 변수에 값 매핑
    SqlParameterSource param = new MapSqlParameterSource()
            .addValue("inquiryTitle", manager.getInquiryTitle())
            .addValue("inquiryContent", manager.getInquiryTitle())
            .addValue("memberId", manager.getMemberId())
            .addValue("inquiryId", inquiryId);

    //update수행 후 변경된 행수 반환
    int updateRowCnt = template.update(sql.toString(), param);

    return updateRowCnt;
  }

  //목록 세트
  @Override
  public List<Manager> findAll() {
    StringBuffer sql = new StringBuffer();
    sql.append("select inquiry_id, inquiry_title, inquiry_content , member_id, inquiry_cdate, inquiry_udate, inquiry_comment, inquiry_state, inquiry_comment_udate, inquiry_comment_cdate");
    sql.append("    from manager ");
    sql.append("order by inquiry_id desc ");

    List<Manager> list = template.query(sql.toString(), BeanPropertyRowMapper.newInstance(Manager.class));

    return list;
  }

  @Override
  public List<Manager> findAll(Long reqPage, Long recCnt) {
    StringBuffer sql = new StringBuffer();
    sql.append("  select *");
    sql.append("    from manager ");
    sql.append("order by inquiry_id DESC ");
    sql.append("offset (:reqPage - 1) * :recCnt rows fetch first :recCnt rows only ");

    Map<String,Long> param = Map.of("reqPage",reqPage,"recCnt",recCnt);
    List<Manager> list = template.query(sql.toString(), param, BeanPropertyRowMapper.newInstance(Manager.class));

    return list;
  }

  @Override
  public int totalCnt() {
    String sql = "SELECT COUNT(inquiry_id) FROM manager ";

    SqlParameterSource param = new MapSqlParameterSource();
    Integer cnt = template.queryForObject(sql, param, Integer.class);

    return cnt;
  }
}