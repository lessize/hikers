package com.kh.hikers.web.form;

import com.kh.hikers.domain.entity.Member;
import lombok.Data;

@Data
public class MemberProfile {
  private Member member;
  private String profileImageUrl;
}
