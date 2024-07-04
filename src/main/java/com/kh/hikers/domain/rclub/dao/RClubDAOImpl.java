package com.kh.hikers.domain.rclub.dao;

import com.kh.hikers.domain.entity.RClub;
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
public class RClubDAOImpl implements RClubDAO {

  private final NamedParameterJdbcTemplate template;

  @Override
  public Long save(RClub rclub) {
    System.out.print(rclub);
    StringBuffer sql = new StringBuffer();
    sql.append("insert into rclub(rclub_id, club_id, member_id, comments) ");
    sql.append("values(RCLUB_ID_seq.nextval, :clubId, :memberId, :comments) ");

    // SQL 파라미터 자동매핑
    SqlParameterSource param = new BeanPropertySqlParameterSource(rclub); //
    KeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(sql.toString(),param,keyHolder,new String[]{"rclub_id"});

    long rclubId = keyHolder.getKey().longValue();    //상품아이디
    return rclubId;
  }

  @Override
  public int deleteById(Long rclubId) {
    StringBuffer sql = new StringBuffer();
    sql.append("delete from rclub ");
    sql.append(" where rclub_id = :rclubId ");

    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("rclubId",rclubId);

    int deletedRowCnt = template.update(sql.toString(), param);

    return deletedRowCnt;
  }

  //여러건 삭제
  @Override
  public int deleteByIds(List<Long> rclubId) {
    StringBuffer sql = new StringBuffer();
    sql.append("delete from rclub ");
    sql.append(" where rclub_id in (:rclubId) ");

    Map<String,List<Long>> map = Map.of("rclubId",rclubId);
    int deletedRowCnt = template.update(sql.toString(), map);
    return deletedRowCnt;
  }

  //수정
  @Override
  public int updateById(Long rclubId, RClub rclub) {
    StringBuffer sql = new StringBuffer();
    sql.append("update rclub ");
    sql.append("   set club_id = :clubId, ");
    sql.append("       member_id = :memberId, ");
    sql.append("       comments = :comments, ");
    sql.append("       udate = default ");
    sql.append(" where rclub_id = :rclubId ");
    //sql 파라미터 변수에 값 매핑
    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("clubId", rclub.getClubId())
        .addValue("memberId", rclub.getMemberId())
        .addValue("comments", rclub.getComments())
        .addValue("rclubId", rclubId);

    //update수행 후 변경된 행수 반환
    int updateRowCnt = template.update(sql.toString(), param);

    return updateRowCnt;
  }

  //목록 세트
  @Override
  public List<RClub> findAll() {
    StringBuffer sql = new StringBuffer();
    sql.append("select rclub_id, club_id, member_id, comments, cdate, udate");
    sql.append("    from rclub ");
    sql.append("order by rclub_id desc ");

    List<RClub> list = template.query(sql.toString(), BeanPropertyRowMapper.newInstance(RClub.class));

    return list;
  }

  @Override
  public List<RClub> findAll(Long reqPage, Long recCnt) {
    StringBuffer sql = new StringBuffer();
    sql.append("select rclub_id, club_id, member_id, comments, cdate, udate");
    sql.append("    from rclub ");
    sql.append("order by rclub_id desc ");
    sql.append("offset (:reqPage - 1) * :recCnt rows fetch first :recCnt rows only ");

    Map<String,Long> param = Map.of("reqPage",reqPage,"recCnt",recCnt);
    List<RClub> list = template.query(sql.toString(), param, BeanPropertyRowMapper.newInstance(RClub.class));

    return list;
  }

  @Override
  public int totalCnt() {
    String sql = "SELECT COUNT(rclub_id) FROM rclub ";

    SqlParameterSource param = new MapSqlParameterSource();
    Integer cnt = template.queryForObject(sql, param, Integer.class);

    return cnt;
  }

}
