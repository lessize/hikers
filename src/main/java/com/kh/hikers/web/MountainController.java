package com.kh.hikers.web;

import com.kh.hikers.domain.entity.Bbs;
import com.kh.hikers.domain.entity.Mountain;
import com.kh.hikers.domain.mountain.svc.MountainSVC;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/service")
public class MountainController {

  private final MountainSVC mountainSVC;

  @GetMapping("/search")
  public String search(@RequestParam(name = "searchText", required = false) String searchText,
                       @RequestParam(name = "city", required = false) String city,
                       @RequestParam(name = "mntnCode", required = false) Long mntnCode,
                       Model model) {
    List<Mountain> mountains;

    if (searchText != null && !searchText.isEmpty()) {
      mountains = mountainSVC.searchMountainByName(searchText);
      model.addAttribute("searchType", "검색 결과");
    } else if (city != null && !city.isEmpty()) {
      mountains = mountainSVC.cityMountain(city);
      model.addAttribute("searchType", "선택한 지역");
    } else if (mntnCode != null) {
      mountains = mountainSVC.searchMountainByCode(mntnCode);
      model.addAttribute("searchType", "산 코드 검색 결과");
    } else {
      mountains = List.of();
      model.addAttribute("searchType", ""); // 검색 타입이 없을 경우 빈 문자열
    }

    model.addAttribute("mountains", mountains);
    model.addAttribute("selectedCity", city);
    model.addAttribute("searchText", searchText);

    return "service/search_mountain_detail"; // 검색 결과를 표시할 HTML 페이지
  }

  @GetMapping("/api/allMountains")
  @ResponseBody
  public List<Mountain> getAllMountains() {
    return mountainSVC.getAllMountains();
  }

  @GetMapping("/bbs")
  @ResponseBody
  public List<Bbs> getMountainBbs(@RequestParam(name = "mntnCode") Long mntnCode) {
    return mountainSVC.goToBbs(mntnCode);
  }

  @GetMapping("/getCityByMntnCode")
  @ResponseBody
  public String getCityByMntnCode(@RequestParam(name = "mntnCode") Long mntnCode) {
    return mountainSVC.getCityByMntnCode(mntnCode);
  }

  @GetMapping("/search_mountain")
  public String search_mountain(){
    return "service/search_mountain";
  }
}

