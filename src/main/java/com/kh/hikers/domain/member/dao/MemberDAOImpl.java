package com.kh.hikers.domain.member.dao;

import com.kh.hikers.domain.entity.Member;
import com.kh.hikers.web.form.MemberProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberDAOImpl implements MemberDAO {

  private final NamedParameterJdbcTemplate template;

  @Value("${profile.image.defaultCode}")
  private String profileImageDefaultCode;

  @Value("${profile.image-url-prefix}")
  private String imageUrlPrefix;

  // 회원 가입
  @Override
  public Long insertMember(Member member) {
    // sql
    StringBuffer sql = new StringBuffer();
    sql.append("insert into member (member_id, id, pw, tel, nickname, gender, mexp, loc) ");
    sql.append("values (member_id_seq.nextval, :id, :pw, :tel, :nickname, :gender, :mexp, :loc) ");

    SqlParameterSource param = new BeanPropertySqlParameterSource(member);
    KeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(sql.toString(), param, keyHolder, new String[]{"member_id"});
    Long member_id = ((BigDecimal) keyHolder.getKeys().get("member_id")).longValue();

    return member_id;
  }

  // 이메일 유무
  public boolean existId(String id) {
    // Null check for template
    if (template == null) {
      throw new IllegalStateException("JdbcTemplate is not initialized");
    }

    String sql = "select count(id) from member where id = :id ";
    Map<String, Object> paramMap = Collections.singletonMap("id", id);

    Integer count = template.queryForObject(sql, paramMap, Integer.class);

    // Check if count is exactly 1
    return count != null && count == 1;
  }

  // 회원 조회
  @Override
  public Optional<Member> findByEmailPw(String id, String pw) {
    StringBuffer sql = new StringBuffer();
    sql.append("select *" );
    sql.append("  from member" );
    sql.append(" where id = :id" );
    sql.append("   and pw = :pw" );

    Map param = Map.of("id", id, "pw", pw);
    try {
      Member member = template.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(Member.class));
      return Optional.of(member);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  // 프로필 조회
  @Override
  public Optional<MemberProfile> findById(String id) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT m.id, m.pw, m.tel, m.nickname, m.gender, m.mexp, m.loc, m.code, uf.store_filename ");
    sql.append("  FROM member m ");
    sql.append("  LEFT JOIN uploadfile uf ON m.id = uf.rid ");
    sql.append("   AND uf.code = :profileImageCode ");
    sql.append(" WHERE m.id = :id");

    try {
      Map<String, Object> params = Map.of("id", id, "profileImageCode", profileImageDefaultCode);
      MemberProfile memberProfile = template.queryForObject(sql.toString(), params, (rs, rowNum) -> {
        Member member = new Member();
        member.setId(rs.getString("id"));
        member.setPw(rs.getString("pw"));
        member.setTel(rs.getString("tel"));
        member.setNickname(rs.getString("nickname"));
        member.setGender(rs.getString("gender"));
        member.setMexp(rs.getInt("mexp"));
        member.setLoc(rs.getString("loc"));
        member.setCode(rs.getString("code"));

        MemberProfile profile = new MemberProfile();
        profile.setMember(member);
        String storeFilename = rs.getString("store_filename");
        if (storeFilename != null) {
          profile.setProfileImageUrl(storeFilename);
        }
        return profile;
      });
      return Optional.of(memberProfile);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }


  // 프로필 수정
  @Override
  public int updateById(String id, Member member) {
    StringBuffer sql = new StringBuffer();
    sql.append("update member ");
    sql.append("   set tel = :tel, ");
    sql.append("       nickname = :nickname, ");
    sql.append("       mexp = :mexp, ");
    sql.append("       loc = :loc ");
    sql.append(" where id = :id ");

    SqlParameterSource param = new MapSqlParameterSource()
            .addValue("id", id)
            .addValue("tel", member.getTel())
            .addValue("nickname", member.getNickname())
            .addValue("mexp", member.getMexp())
            .addValue("loc", member.getLoc());
    int updateProfile = template.update(sql.toString(), param);

    return updateProfile;
  }

  // 아이디 찾기
  @Override
  public Optional<Member> findByNicknameTel(String nickname, String tel) {
    StringBuffer sql = new StringBuffer();
    sql.append("select id" );
    sql.append("  from member" );
    sql.append(" where nickname = :nickname" );
    sql.append("   and tel = :tel" );

    Map param = Map.of("nickname", nickname, "tel", tel);
    try {
      Member member = template.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(Member.class));
      return Optional.of(member);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }


  //비밀번호 유무
  @Override
  public boolean hasPasswd(String id, String nickname) {
    StringBuffer sql = new StringBuffer();

    sql.append("select count(*) ");
    sql.append("  from member ");
    sql.append(" where id = :id ");
    sql.append("   and nickname = :nickname ");

    Map<String, String> param = Map.of("id", id, "nickname",nickname);
    Integer cnt = template.queryForObject(sql.toString(), param, Integer.class);

    return cnt == 1 ? true : false;
  }

  //비밀번호 변경
  @Override
  public int changePw(String id, String pw) {
    StringBuffer sql = new StringBuffer();
    sql.append("update member ");
    sql.append("   set pw = :pw ");
    sql.append(" where id  = :id ");

    Map<String, String> param = Map.of("pw", pw, "id", id);
    int updateRow = template.update(sql.toString(), param);

    return updateRow;
  }
}
