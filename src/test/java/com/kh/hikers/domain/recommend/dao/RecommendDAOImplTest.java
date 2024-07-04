package com.kh.hikers.domain.recommend.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class RecommendDAOImplTest {

  @Autowired
  RecommendDAO recommendDAO;

  @Test
  void getMemberExperience() {
    log.info("recommandDAO={}", recommendDAO.getMemberExperience(30L));
  }
}