package com.kh.hikers.web;

import com.kh.hikers.domain.entity.Mountain;
import com.kh.hikers.domain.recommend.dao.RecommendDAO;
import com.kh.hikers.domain.recommend.svc.RecommendSVC;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class ApiRecommendControllerTest {

  @Autowired
  RecommendSVC recommendSVC;

  @Test
  void personalizedRecommend() {
    Long id = 30L;
    List<Mountain> list = recommendSVC.personalRecommend(id);
    for (Mountain mountain : list) {
      log.info("Mountain={}",mountain);
    }
  }
}