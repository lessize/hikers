package com.kh.hikers.domain.mountain.dao;

import com.kh.hikers.domain.entity.Bbs;
import com.kh.hikers.domain.entity.Mountain;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j            // console로그 출력
@SpringBootTest   // springboot 환경에서 테스트 진행
class MountainDAOImplTest {

  @Autowired    // sc(스프링컨테이너)에서 동일타입의 객체를 찾아 주입받음.
  private MountainDAO mountainDAO;

  @Test
  @DisplayName("산 게시물 검색")
  void goToBbs() {
    List<Bbs> list = mountainDAO.goToBbs(418200801L);
    log.info("내용={}", list);
  }

  @Test
  void searchMountainByName() {
    List<Mountain> list = mountainDAO.searchMountainByName("가야산");
    log.info("내용={}", list);
  }

  @Test
  void searchMountainByCode() {
    List<Mountain> list = mountainDAO.searchMountainByCode(418200801L);
    log.info("내용={}", list);
  }
}