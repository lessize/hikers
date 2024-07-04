package com.kh.hikers.domain.mountain.svc;

import com.kh.hikers.domain.entity.Bbs;
import com.kh.hikers.domain.entity.Mountain;
import com.kh.hikers.domain.mountain.dao.MountainDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MountainSVCImpl implements MountainSVC {

  private final MountainDAO mountainDAO;

  @Override
  public List<Mountain> cityMountain(String city) {
    return mountainDAO.cityMountain(city);
  }

  @Override
  public List<Mountain> searchMountainByName(String name) {
    return mountainDAO.searchMountainByName(name);
  }

  @Override
  public List<Mountain> searchMountainByCode(Long mntnCode) {
    return mountainDAO.searchMountainByCode(mntnCode);
  }

  @Override
  public List<Mountain> getAllMountains() {
    return mountainDAO.getAllMountains();
  }

  @Override
  public List<Bbs> goToBbs(Long mntnCode) {
    return mountainDAO.goToBbs(mntnCode);
  }

  @Override
  public String getCityByMntnCode(Long mntnCode) {
    return mountainDAO.getCityByMntnCode(mntnCode);
  }
}
