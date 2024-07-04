package com.kh.hikers.domain.member.dao;


import com.kh.hikers.domain.entity.Member;
import com.kh.hikers.web.form.MemberProfile;

import java.util.Optional;

public interface MemberDAO {
  // 회원 가입
  Long insertMember(Member member);

  // 이메일 유무 조회
  public boolean existId(String id);

  // 회원 조회
  Optional<Member> findByEmailPw(String id, String pw);

  // 프로필 조회
  Optional<MemberProfile> findById(String id);

  // 프로필 수정
  public int updateById(String id, Member member);

  // 아이디 찾기
  Optional<Member> findByNicknameTel(String nickname, String tel);

  //비밀번호 유무확인
  boolean hasPasswd(String id, String nickname);

  //비밀번호 변경
  int changePw(String id, String pw);
}
