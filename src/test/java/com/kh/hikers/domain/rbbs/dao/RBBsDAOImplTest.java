package com.kh.hikers.domain.rbbs.dao;

import com.kh.hikers.domain.entity.RBBS;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Comments;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class RBBsDAOImplTest {

  @Autowired
  RBBsDAO rbBsDAO;

  @Test
  void save() {
    RBBS rbbs = new RBBS();
    rbbs.setComments("가나다라");
    rbbs.setMemberId(21L);
    rbbs.setBbsId(13L);
    Long commentsId = rbBsDAO.save(rbbs);
    log.info("commentsId={}",commentsId);
  }

  @Test
  void deleteById() {
  }

  @Test
  void updateById() {
  }

  @Test
  void findAll() {
  }

  @Test
  void totalCnt() {
  }
}