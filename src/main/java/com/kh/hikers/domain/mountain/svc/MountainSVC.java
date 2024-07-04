package com.kh.hikers.domain.mountain.svc;

import com.kh.hikers.domain.entity.Bbs;
import com.kh.hikers.domain.entity.Mountain;

import java.util.List;

public interface MountainSVC {
  List<Mountain> cityMountain(String city);
  List<Mountain> searchMountainByName(String name);
  List<Mountain> searchMountainByCode(Long mntnCode);
  List<Mountain> getAllMountains();
  List<Bbs> goToBbs(Long mntnCode);
  String getCityByMntnCode(Long mntnCode);
}