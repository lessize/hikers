package com.kh.hikers.domain.rbbs.dao;

import com.kh.hikers.domain.entity.RBBS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


@Slf4j
@Repository
@RequiredArgsConstructor
public class RBBsDAOImpl implements RBBsDAO {

  private final NamedParameterJdbcTemplate template;

  @Override
  public Long save(RBBS rbbs) {
    System.out.print(rbbs);
    StringBuffer sql = new StringBuffer();
    sql.append("insert into rbbs(rbbs_id, bbs_id, member_id, comments) ");
    sql.append("values(RBBS_RBBS_ID_seq.nextval, :bbsId, :memberId, :comments) ");

    // SQL 파라미터 자동매핑
    SqlParameterSource param = new BeanPropertySqlParameterSource(rbbs); //
    KeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(sql.toString(),param,keyHolder,new String[]{"rbbs_id"});

    long rbbsId = keyHolder.getKey().longValue();    //상품아이디
    return rbbsId;
  }

  @Override
  public int deleteById(Long rbbsId) {
    StringBuffer sql = new StringBuffer();
    sql.append("delete from rbbs ");
    sql.append(" where rbbs_id = :rbbsId ");

    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("rbbsId",rbbsId);

    int deletedRowCnt = template.update(sql.toString(), param);

    return deletedRowCnt;
  }

  //여러건 삭제
  @Override
  public int deleteByIds(List<Long> rbbsId) {
    StringBuffer sql = new StringBuffer();
    sql.append("delete from rbbs ");
    sql.append(" where rbbs_id in (:rbbsId) ");

    Map<String,List<Long>> map = Map.of("rbbsId",rbbsId);
    int deletedRowCnt = template.update(sql.toString(), map);
    return deletedRowCnt;
  }

  //수정
  @Override
  public int updateById(Long rbbsId, RBBS rbbs) {
    StringBuffer sql = new StringBuffer();
    sql.append("update rbbs ");
    sql.append("   set bbs_id = :bbsId, ");
    sql.append("       member_id = :memberId, ");
    sql.append("       comments = :comments, ");
    sql.append("       udate = default ");
    sql.append(" where rbbs_id = :rbbsId ");
    //sql 파라미터 변수에 값 매핑
    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("bbsId", rbbs.getBbsId())
        .addValue("memberId", rbbs.getMemberId())
        .addValue("comments", rbbs.getComments())
        .addValue("rbbsId", rbbsId);

    //update수행 후 변경된 행수 반환
    int updateRowCnt = template.update(sql.toString(), param);

    return updateRowCnt;
  }

  //목록 세트
  @Override
  public List<RBBS> findAll() {
    StringBuffer sql = new StringBuffer();
    sql.append("select rbbs_id, bbs_id, member_id, comments, cdate, udate");
    sql.append("    from rbbs ");
    sql.append("order by rbbs_id desc ");

    List<RBBS> list = template.query(sql.toString(), BeanPropertyRowMapper.newInstance(RBBS.class));

    return list;
  }

  @Override
  public List<RBBS> findAll(Long reqPage, Long recCnt) {
    StringBuffer sql = new StringBuffer();
    sql.append("select rbbs_id, bbs_id, member_id, comments, cdate, udate");
    sql.append("    from rbbs ");
    sql.append("order by rbbs_id desc ");
    sql.append("offset (:reqPage - 1) * :recCnt rows fetch first :recCnt rows only ");

    Map<String,Long> param = Map.of("reqPage",reqPage,"recCnt",recCnt);
    List<RBBS> list = template.query(sql.toString(), param, BeanPropertyRowMapper.newInstance(RBBS.class));

    return list;
  }

  @Override
  public int totalCnt() {
    String sql = "SELECT COUNT(rbbs_id) FROM rbbs ";

    SqlParameterSource param = new MapSqlParameterSource();
    Integer cnt = template.queryForObject(sql, param, Integer.class);

    return cnt;
  }
}
