package com.kh.hikers.domain.club.dao;

import com.kh.hikers.domain.entity.Club;
import com.kh.hikers.domain.entity.Mountain;
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
public class ClubDAOImpl implements ClubDAO{

  private final NamedParameterJdbcTemplate template;

  @Override
  public Long save(Club club) {
    System.out.print(club);
    StringBuffer sql = new StringBuffer();
    sql.append("insert into club(club_id, title, member_id, ccontent, mntn_code, code, timetable, recruit) ");
    sql.append("values(club_id_seq.nextval, :title, :memberId, :ccontent, :mntnCode, default, :timetable, :recruit) ");

    // SQL 파라미터 자동매핑
    SqlParameterSource param = new BeanPropertySqlParameterSource(club); //
    KeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(sql.toString(),param,keyHolder,new String[]{"club_id"});

    long clubId = keyHolder.getKey().longValue();    //상품아이디
    return clubId;
  }

  @Override
  public int deleteById(Long clubId) {
    // 첫 번째 쿼리: rclub 테이블에서 해당 clubId를 삭제
    String deleteRClubSQL = "DELETE FROM rclub WHERE club_id = :clubId";
    SqlParameterSource rClubParam = new MapSqlParameterSource().addValue("clubId", clubId);
    int deletedRClubRowCnt = template.update(deleteRClubSQL, rClubParam);

    // 두 번째 쿼리: club 테이블에서 해당 clubId를 삭제
    String deleteClubSQL = "DELETE FROM club WHERE club_id = :clubId";
    SqlParameterSource clubParam = new MapSqlParameterSource().addValue("clubId", clubId);
    int deletedClubRowCnt = template.update(deleteClubSQL, clubParam);

    // 삭제된 행의 총 수 반환
    return deletedRClubRowCnt + deletedClubRowCnt;
  }

  //여러건 삭제
  @Override
  public int deleteByIds(List<Long> clubIds) {
    StringBuffer sql = new StringBuffer();
    sql.append("delete from club ");
    sql.append(" where club_id in (:clubIds) ");

    Map<String,List<Long>> map = Map.of("clubIds",clubIds);
    int deletedRowCnt = template.update(sql.toString(), map);
    return deletedRowCnt;
  }

  //조회
  @Override
  public Optional<Club> findById(Long clubId) {
    StringBuffer sql = new StringBuffer();
    sql.append("select b.club_id AS club_id,b.member_id AS member_id,m.nickname AS nickname,b.mntn_code AS mntn_code,b.title AS title," +
            "b.ccontent AS ccontent, b.code AS code, " +
            "b.RECRUIT AS RECRUIT, mt.mntn_nm AS mntn_nm, "  +
            "b.cdate AS cdate,b.udate AS udate");
    sql.append("  from club b RIGHT JOIN member m ON m.member_id = b.member_id"+
        " LEFT JOIN mountain mt ON b.MNTN_CODE = mt.MNTN_CODE ");
    sql.append(" where club_id = :clubId ");

    try {
      Map<String, Long> map = Map.of("clubId", clubId);
      Club club = template.queryForObject(sql.toString(), map, BeanPropertyRowMapper.newInstance(Club.class));
      return Optional.of(club);

    } catch (EmptyResultDataAccessException e) {
      //조회결과가 없는경우
      return Optional.empty();
    }
  }

  //수정
  @Override
  public int updateById(Long clubId, Club club) {
    StringBuffer sql = new StringBuffer();
    sql.append("update club ");
    sql.append("   set mntn_code = :mntn_code, ");
    sql.append("       title = :title, ");
    sql.append("       member_id = :memberId, ");
    sql.append("       ccontent = :ccontent, ");
    sql.append("       ctime = :ctime, ");
    sql.append("       mntn_code = :mntnCode, ");
    sql.append("       code = :code, ");
    sql.append("       timetable = :timetable, ");
    sql.append("       recruit = :recruit, ");
    sql.append("       udate = default ");
    sql.append(" where club_id = :clubId ");
    //sql 파라미터 변수에 값 매핑
    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("timetable", club.getTimetable())
        .addValue("title", club.getTitle())
        .addValue("memberId", club.getMemberId())
        .addValue("ccontent", club.getCcontent())
        .addValue("region", club.getMntnCode())
        .addValue("code", club.getCode())
        .addValue("recruit", club.getRecruit())
        .addValue("clubId", clubId);

    //update수행 후 변경된 행수 반환
    int updateRowCnt = template.update(sql.toString(), param);

    return updateRowCnt;
  }

  //목록 세트
  @Override
  public List<Club> findAll() {
    StringBuffer sql = new StringBuffer();
    sql.append("select club_id, title, member_id, ccontent, mntn_code, code, timetable, recruit, cdate,udate");
    sql.append("    from club ");
    sql.append("order by club_id desc ");

    List<Club> list = template.query(sql.toString(), BeanPropertyRowMapper.newInstance(Club.class));

    return list;
  }

  @Override
  public List<Club> findAll(Long reqPage, Long recCnt) {
    String query = " SELECT " +
            " m.member_id AS member_id, " +
            " m.nickname AS member_nickname, " +
            " c.club_id AS club_id , " +
            " c.title AS bbs_title, " +
            " c.ccontent AS club_content, " +
            " c.hit As bbs_hit, " +
            " c.status As club_status, " +
            " c.cdate AS club_cdate, " +
            " c.udate AS club_udate, " +
            " c.RECRUIT AS club_RECRUIT, " +
            " mt.MNTN_CODE AS mountain_code, " +
            " mt.MNTN_NM AS mountain_name, " +
            " mt.MNTN_loc AS mountain_loc, " +
            " c.code AS code " +
            " FROM club c " +
            " RIGHT JOIN member m ON m.member_id = c.member_id " +
            " LEFT JOIN mountain mt ON c.MNTN_CODE = mt.MNTN_CODE " +
            " Where c.status != 'D' " +
            " order by c.cdate desc " +
            " offset (:reqPage - 1) * :recCnt rows fetch first :recCnt rows only ";
    Map<String,Long> param = Map.of("reqPage",reqPage,"recCnt",recCnt);
    List<Club> list = template.query(query, param, (ResultSet rs) -> {
      List<Club> resultList = new ArrayList<>();
      while (rs.next()) {
        Club club = new Club();
        club.setCode(rs.getString("code"));
        club.setClubId(rs.getLong("club_id"));
        club.setMemberId(rs.getLong("member_id"));
        club.setTitle(rs.getString("bbs_title"));
        club.setNickname(rs.getString("member_nickname"));
        club.setRecruit(rs.getLong("club_RECRUIT"));
        Timestamp cdateTimestamp = rs.getTimestamp("club_cdate");
        club.setCdate(cdateTimestamp != null ? LocalDate.from(cdateTimestamp.toLocalDateTime()) : null);
        Timestamp udateTimestamp = rs.getTimestamp("club_udate");
        club.setUdate(udateTimestamp != null ? LocalDate.from(udateTimestamp.toLocalDateTime()) : null);
        resultList.add(club);
      }
      return resultList;
    });
    return list;
  }

  @Override
  public int totalCnt() {
    String sql = "SELECT COUNT(club_id) FROM club ";

    SqlParameterSource param = new MapSqlParameterSource();
    Integer cnt = template.queryForObject(sql, param, Integer.class);

    return cnt;
  }

  @Override
  public List<Mountain> selectmntn(String mntnNm){
    log.info("product={}",mntnNm);
    StringBuffer sql = new StringBuffer();
    sql.append("select mntn_loc");
    sql.append(" from mountain ");
    sql.append(" where mntn_nm = :mntnNm");
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("mntnNm", mntnNm);
    List<Mountain> mntnSelect = template.query(sql.toString(),params, BeanPropertyRowMapper.newInstance(Mountain.class));
    return mntnSelect;
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
}
