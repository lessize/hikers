package com.kh.hikers.domain.mountain.dao;

import com.kh.hikers.domain.entity.Bbs;
import com.kh.hikers.domain.entity.Mountain;

import java.util.List;

public interface MountainDAO {
  List<Mountain> cityMountain(String city);
  List<Mountain> searchMountainByName(String name);
  List<Mountain> searchMountainByCode(Long mntnCode);
  List<Mountain> getAllMountains();
  List<Bbs> goToBbs(Long mntnCode);
  String getCityByMntnCode(Long mntnCode);
}
