package com.kh.hikers.domain.member.svc;

import com.kh.hikers.domain.entity.Member;
import com.kh.hikers.domain.member.dao.MemberDAO;
import com.kh.hikers.web.form.MemberProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberSVCImpl implements MemberSVC{

  private final MemberDAO memberDAO;

  // 회원가입
  @Override
  public Long insertMember(Member member) {
    return memberDAO.insertMember(member);
  }

  // 이메일 유무 조회
  public boolean existId(String id) {
    return memberDAO.existId(id);
  }

  // 회원조회
  @Override
  public Optional<Member> findByEmailPw(String id, String pw) {
    return memberDAO.findByEmailPw(id, pw);
  }

  // 프로필 조회
  @Override
  public Optional<MemberProfile> findById(String id) {
    return memberDAO.findById(id);
  }

  // 프로필 수정
  @Override
  public int updateById(String id, Member member) {
    return memberDAO.updateById(id, member);
  }

  // 아이디 찾기
  @Override
  public Optional<Member> findByNicknameTel(String nickname, String tel) {
    return memberDAO.findByNicknameTel(nickname, tel);
  }

  // 비번 유무
  @Override
  public boolean hasPasswd(String id, String nickname) {
    return memberDAO.hasPasswd(id, nickname);
  }

  // 비번 변경
  @Override
  public int changePw(String id, String pw) {
    return memberDAO.changePw(id, pw);
  }
}
