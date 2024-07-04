var map;
var polylines = [];
var circles = [];

function normalMap() {
    // 초기 지도 설정
    var map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 36.34, lng: 127.77}, // 서울의 위도와 경도
        zoom: 7 // 초기 줌 레벨
    });
}
normalMap();

document.addEventListener('DOMContentLoaded', function () {
  var mountainItems = document.querySelectorAll('.mountain-item');

  document.addEventListener('click', function () {
    document.querySelector('.weather-wrap').style.display = 'block';
    document.querySelector('.fire-wrap').style.display = 'block';
  });

  mountainItems.forEach(function (item) {
    // 호버 이벤트 처리
    item.addEventListener('mouseover', function () {
      if (!item.classList.contains('clicked')) {
        item.style.backgroundColor = 'var(--color-green)';
        item.style.color = 'var(--color-white)';
      }
    });

    item.addEventListener('mouseout', function () {
      if (!item.classList.contains('clicked')) {
        item.style.backgroundColor = 'transparent';
        item.style.color = 'var(--color-black)';
      }
    });

    // 클릭 이벤트 처리
    item.addEventListener('click', function () {
    // 모든 요소의 clicked 클래스 제거
    mountainItems.forEach(function (item) {
      item.classList.remove('clicked');
      item.style.backgroundColor = 'transparent';
      item.style.color = 'var(--color-black)';
    });

    // 클릭된 요소에 clicked 클래스 추가
    item.classList.add('clicked');
    item.style.backgroundColor = 'var(--color-green)';
    item.style.color = 'var(--color-white)';

    var mountainName = item.textContent.trim();
    var mountainCode = item.getAttribute('data-mountain-code'); // 산 코드를 가져옵니다.
    showMap(mountainName, mountainCode); // 산 이름과 코드를 전달하여 함수 호출합니다.
    });
  });
});

function showMap(mountainName, mountainCode) {
  // 클릭한 산 이름과 산 코드 가져오기
  // var mountainName = "지리산";
  // var mountainCode = "482202301";

  // 클릭한 산 이름 또는 코드가 null인 경우
  if (!mountainName || !mountainCode) {
      console.error('Mountain name or code is missing.');
      return; // 함수 종료
  }

  var jsonUrl = "http://192.168.0.29:5000/static/PMNTN_" + mountainName + "_" + mountainCode + ".json";
  var jsonSpotUrl = "http://192.168.0.29:5000/static/PMNTN_SPOT_" + mountainName + "_" + mountainCode + ".json";

  // 지도를 표시하도록 설정
  document.getElementById('map').style.display = 'block';
  // initMap 함수 호출하여 지도 초기화
  initMap(jsonUrl, jsonSpotUrl);
}

function initMap(jsonUrl, jsonSpotUrl) {
  // 좌표 변환 설정 (ITRF2000_TM -> WGS84)
  proj4.defs([
    [
      'EPSG:5179',
      '+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs'
    ],
    [
      'EPSG:4326',
      '+proj=longlat +datum=WGS84 +no_defs'
    ]
  ]);

  // JSON 파일 로드
  fetch(jsonUrl)
    .then(response => response.json())
    .then(data => {
      var paths = [];
      // 각 feature의 geometry의 paths에 대해 순회
      data.features.forEach(feature => {
        feature.geometry.paths.forEach(path => {
          // 좌표 변환
          var convertedPath = path.map(coord => {
            var [lon, lat] = proj4('EPSG:5179', 'EPSG:4326', [coord[0], coord[1]]);
            return { lat: lat, lng: lon };
          });
          paths.push({ path: convertedPath, attributes: feature.attributes });
        });
      });

      // 변환된 좌표를 사용하여 지도에 Polyline 표시
      map = new google.maps.Map(document.getElementById('map'), {
        zoom: 14,
        center: paths[0].path[0]
      });

//      console.log(paths[0].path[0])
//      console.log(mapToGrid(paths[0].path[0].lat, paths[0].path[0].lng))        //좌표값 출력

      // 이전에 열렸던 인포 윈도우를 저장하는 변수
      var openInfoWindow = null;

      paths.forEach(pathObj => {
        var polyline = new google.maps.Polyline({
          path: pathObj.path,
          geodesic: true,
          strokeColor: '#7BC74D',
          strokeOpacity: 1.0,
          strokeWeight: 4,
          zIndex: 1
        });
        polyline.setMap(map);
        polylines.push(polyline);

          // 마우스 호버 이벤트 추가
        polyline.addListener('mouseover', function() {
          // 선의 굵기를 더 굵게 변경
          polyline.setOptions({ strokeWeight: 6 });
        });
          // 마우스 이탈 이벤트 추가
        polyline.addListener('mouseout', function() {
          // 기본 선의 굵기로 변경
          polyline.setOptions({ strokeWeight: 4 });
        });

        polyline.addListener('click', function() {
          var attributes = pathObj.attributes;
          var infoWindowContent = `
            <div>
              <strong>구간명:</strong> ${attributes.PMNTN_NM}<br>
              <strong>길이(km):</strong> ${attributes.PMNTN_LT}<br>
              <strong>난이도:</strong> ${attributes.PMNTN_DFFL}<br>
              <strong>상행 시간(분):</strong> ${attributes.PMNTN_UPPL}<br>
              <strong>하행 시간(분):</strong> ${attributes.PMNTN_GODN}<br>
            </div>
          `;

          // 이전에 열렸던 인포 윈도우가 있으면 닫기
          if (openInfoWindow) {
            openInfoWindow.close();
          }

          // 인포 윈도우 열기
          var infoWindow = new google.maps.InfoWindow({
            content: infoWindowContent
          });
          infoWindow.setPosition(pathObj.path[0]);
          infoWindow.open(map);
          openInfoWindow = infoWindow;

          // 모든 폴리라인 색상을 초기화
          resetPolylineColors();
          // 현재 클릭된 폴리라인의 색상 변경
          polyline.setOptions({ strokeColor: '#00FF00' });
        });
      });

      // 이전 확대 수준 저장 변수
      let previousZoom = map.getZoom();

      // 점을 그릴 JSON 파일 로드
      fetch(jsonSpotUrl)
        .then(response => response.json())
        .then(data => {
          // 지점 데이터를 순회하면서 원형 도형 추가
          data.features.forEach(feature => {
            var [lon, lat] = proj4('EPSG:5179', 'EPSG:4326', [feature.geometry.x, feature.geometry.y]);
            var circle = new google.maps.Circle({
              strokeColor: '#8B4513',
              strokeOpacity: 0.8,
              strokeWeight: 5,
              fillColor: '#8B4513',
              fillOpacity: 0.8,
              map: map,
              center: { lat: lat, lng: lon },
              radius: 8, // 초기 반경
              zIndex: 2
            });
            circles.push(circle);

            var infoWindow = new google.maps.InfoWindow({
              content: feature.attributes.DETAIL_SPO
            });

            circle.addListener('click', function() {
              // 이전에 열렸던 인포 윈도우가 있으면 닫기
              if (openInfoWindow) {
                openInfoWindow.close();
              }

              // 인포 윈도우 열기
              infoWindow.setPosition({ lat: lat, lng: lon });
              infoWindow.open(map);
              openInfoWindow = infoWindow;
            });
          });
        });

      // 맵 확대 정도가 변경될 때마다 반경을 다시 계산하여 원의 크기를 조절
      google.maps.event.addListener(map, 'zoom_changed', function() {
        let currentZoom = map.getZoom();
        let zoomDifference = currentZoom - previousZoom;

        circles.forEach(circle => {
          let currentRadius = circle.getRadius();
          let newRadius;
          if (zoomDifference > 0) {
            newRadius = currentRadius * 1.06; // 확대할 때 조금씩 크게 만듦
          } else {
            newRadius = currentRadius / 1.06; // 축소할 때 원래 크기로 복원
          }
          circle.setRadius(newRadius);
        });

        previousZoom = currentZoom; // 이전 확대 수준 업데이트
      });

      // 날씨 api
      function getCurrentDateTime() {
        const now = new Date();
        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, '0');
        const date = String(now.getDate()).padStart(2, '0');
        const hours = String(now.getHours()).padStart(2, '0');
        return { year, month, date, hours };
      }

      const currentDateTime = getCurrentDateTime();
      const base_date = currentDateTime.year + currentDateTime.month + currentDateTime.date;
      let base_time = '0200';
//      if (currentDateTime.hours >= '02' && currentDateTime.hours < '05') {
//          base_time = '0200';
//      } else if (currentDateTime.hours >= '05' && currentDateTime.hours < '08') {
//          base_time = '0500';
//      } else if (currentDateTime.hours >= '08' && currentDateTime.hours < '11') {
//          base_time = '0800';
//      } else if (currentDateTime.hours >= '11' && currentDateTime.hours < '14') {
//          base_time = '1100';
//      } else if (currentDateTime.hours >= '14' && currentDateTime.hours < '17') {
//          base_time = '1400';
//      } else if (currentDateTime.hours >= '17' && currentDateTime.hours < '20') {
//          base_time = '1700';
//      } else if (currentDateTime.hours >= '20' || currentDateTime.hours < '23') {
//          base_time = '2000';
//      } else {
//          base_time = '2300';
//      }

      let gridCoordinates = mapToGrid(paths[0].path[0].lat, paths[0].path[0].lng);

      var xhr = new XMLHttpRequest();
      var url = 'http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst'; /*URL*/
      var queryParams = '?' + encodeURIComponent('serviceKey') + '='+'oXiuUFZlJwHCuez6M8dUm7gciRLeffesatjeP9Ot6SHL5FtdiDGtQG7b508Q%2FpEBuP4jSczGQYCaff2LcQsLqQ%3D%3D'; /*Service Key*/
      queryParams += '&' + encodeURIComponent('pageNo') + '=' + encodeURIComponent('1'); /**/
      queryParams += '&' + encodeURIComponent('numOfRows') + '=' + encodeURIComponent('1000'); /**/
      queryParams += '&' + encodeURIComponent('dataType') + '=' + encodeURIComponent('JSON'); /**/
      queryParams += '&' + encodeURIComponent('base_date') + '=' + encodeURIComponent(base_date); /**/
      queryParams += '&' + encodeURIComponent('base_time') + '=' + encodeURIComponent(base_time); /**/
      queryParams += '&' + encodeURIComponent('nx') + '=' + encodeURIComponent(gridCoordinates[0]);
      queryParams += '&' + encodeURIComponent('ny') + '=' + encodeURIComponent(gridCoordinates[1]);
      xhr.open('GET', url + queryParams);
        xhr.onreadystatechange = function () {
            if (this.readyState === 4) {
                if (this.status === 200) {
                    let jsonResponse = JSON.parse(this.responseText);

                    let maxTemperature, minTemperature;

                    let categoriesToPrint = ['POP', 'SKY', 'TMX', 'TMN']; // 필요한 카테고리
                    let categoryNameMap = {
                        'POP': '<i class="fa-solid fa-umbrella"></i>', // 강수량
                        'PTY': '강수형태',
                        'PCP': '1시간 강수량',
                        'REH': '습도',
                        'SNO': '1시간 신적설',
                        'SKY': '하늘상태',
                        'TMP': '1시간 기온', // 기온
                        'TMN': '<i class="fa-solid fa-temperature-three-quarters"></i>',
                        'TMX': '<i class="fa-solid fa-temperature-quarter"></i>',
                        'WSD': '풍속'
                    };

                    let skyCodeMap = {
                        '1': '<i class="fa-regular fa-sun"></i>',
                        '3': '<i class="fa-solid fa-cloud-sun"></i>',
                        '4': '<i class="fa-solid fa-cloud"></i>'
                    };

                    let weatherDiv = document.querySelector('.weather');
                    weatherDiv.innerHTML = ''; // 기존 데이터를 지우고 새로 갱신

                    if (jsonResponse.response && jsonResponse.response.body && jsonResponse.response.body.items) {
                        let items = jsonResponse.response.body.items.item.filter(item => item.fcstTime === '0600' || item.fcstTime === '1500'); // 'fcstTime'이 '0600' 또는 '1500'인 것만 필터링

                        // 날짜를 구분하여 출력
                        let dates = {};
                        items.forEach(item => {
                            if (!dates[item.fcstDate]) {
                                dates[item.fcstDate] = [];
                            }
                            dates[item.fcstDate].push(item);
                        });


                        // 각 날짜별로 데이터를 저장하고 출력
                        for (let date in dates) {
                            let dateDiv = document.createElement('div');
                            dateDiv.className = 'weather-date';
                            dateDiv.innerHTML = `<h3>${date.substring(4, 6)}월 ${date.substring(6, 8)}일</h3>`;
                            weatherDiv.appendChild(dateDiv);

                            // 필터링된 데이터 출력
                            dates[date].forEach(item => {
                                let value = item.fcstValue;
                                let displayValue;

                                // 최고온도와 최저온도를 저장
                                if (item.category === 'TMX') {
                                    maxTemperature = value;
                                } else if (item.category === 'TMN') {
                                    minTemperature = value;
                                } else {
                                    // 각 카테고리에 따라 값을 설정
                                    if (categoriesToPrint.includes(item.category)) {
                                        if (item.category === 'POP' && item.fcstTime === '1500') {
                                            displayValue = categoryNameMap[item.category];
                                            dateDiv.innerHTML += `<p><strong>${displayValue}</strong> ${value}%</p>`;
                                        } else if (item.category === 'SKY' && item.fcstTime === '1500') {
                                            displayValue = skyCodeMap[value] || value;
                                            dateDiv.innerHTML += `<p><strong>${displayValue}</strong></p>`;
                                        }
                                    }
                                }
                            });

                            // 최고온도와 최저온도 출력
                            if (maxTemperature && minTemperature) {
                                dateDiv.innerHTML += `<p><strong>${categoryNameMap['TMN']}</strong> ${minTemperature}° \/ ${maxTemperature}°</p>`;
                            }
                        }
                    }
                } else {
                    console.error('Error fetching data:', this.status, this.statusText);
                }
            }
      };

    xhr.send('');
    });
}

function resetPolylineColors() {
  polylines.forEach(polyline => {
    polyline.setOptions({ strokeColor: '#7BC74D' });
  });
}

// 이 함수를 호출하여 지도를 초기화합니다.
function showPathOnMap(pathIndex) {
  // 선택한 등산로에 해당하는 폴리라인 색상 변경
  resetPolylineColors();
  polylines[pathIndex].setOptions({ strokeColor: '#00FF00' });

  // 선택한 등산로가 보이도록 지도 영역 조정
  var bounds = new google.maps.LatLngBounds();
  polylines[pathIndex].getPath().forEach(function(latLng) {
    bounds.extend(latLng);
  });
  map.fitBounds(bounds);
}

// 리뷰 버튼 클릭 시 BBS 리스트 토글
document.getElementById("review-button").addEventListener("click", function() {
    const bbsList = document.getElementById('bbsList');
    bbsList.classList.toggle('slide-out');

    var button = document.getElementById("review-button");
    button.classList.toggle('rotate'); // rotate 클래스 토글
});

function fetchBbs(element) {
  const mntnCode = element.getAttribute('data-mountain-code');
//  console.log(mntnCode);                   //산 코드 출력

  // BBS 데이터를 가져오는 함수
  fetch(`/service/bbs?mntnCode=${mntnCode}`)
    .then(response => response.json())
    .then(data => {
      const bbsList = document.getElementById('bbsList');
      bbsList.innerHTML = ''; // 기존 내용을 지우고 새로 추가
      if (data.length === 0) {
        bbsList.innerHTML = '<li>게시글 없음</li>';
      } else {
        data.forEach(bbs => {

          const li = document.createElement('li');
          li.classList.add('bbs-item');

          const title = document.createElement('h3');
          title.textContent = bbs.title;

          const content = document.createElement('p');
          content.textContent = bbs.bcontent;

          const date = document.createElement('p');
          date.textContent = new Date(bbs.cdate).toLocaleString();

          li.appendChild(title);
          li.appendChild(content);
          li.appendChild(date);

          li.addEventListener('click', function () {
            console.log(bbs.bbsId); // 클릭된 bbs의 bbsId 출력
            window.location.href = `http://localhost:9080/community/${bbs.bbsId}/detail`;       //url 수정 필요
          });

          bbsList.appendChild(li);
        });
      }
      // 데이터가 표시된 후에 slide-out 클래스를 추가하여 슬라이드 아웃 효과 적용
//      bbsList.classList.add('slide-out');

      const map = document.getElementById('map');
      map.style.display = 'block'; // 지도를 표시
      // 지도 초기화 및 데이터 표시 코드 추가
    })
    .catch(error => console.error('Error fetching BBS:', error));


  // 지역 코드 매핑 객체
  const cityCodeMap = {
    '전국': '00',
    '서울특별시': '11',
    '부산광역시': '26',
    '대구광역시': '27',
    '인천광역시': '28',
    '광주광역시': '29',
    '대전광역시': '30',
    '울산광역시': '31',
    '경기도': '41',
    '강원도': '51',
    '충청북도': '43',
    '충청남도': '44',
    '전라북도': '52',
    '전라남도': '46',
    '경상북도': '47',
    '경상남도': '48',
    '제주도': '50'
  };

  // '서울특별시'가 입력되면 '11' 반환
  function getCityCode(cityName) {
    return cityCodeMap[cityName] || '';
  }

  fetch(`/service/getCityByMntnCode?mntnCode=${mntnCode}`)
    .then(response => response.text()) // JSON 형식이 아닌 문자열로 가져옴
    .then(city => {
//      console.log(getCityCode(city));        //cityCode 불러오기

      // 산불 정보를 가져오는 fetch 요청
      fetch(`http://apis.data.go.kr/1400377/forestPoint/forestPointListSidoSearch?serviceKey=oXiuUFZlJwHCuez6M8dUm7gciRLeffesatjeP9Ot6SHL5FtdiDGtQG7b508Q%2FpEBuP4jSczGQYCaff2LcQsLqQ%3D%3D&pageNo=1&numOfRows=5&_type=json&localAreas=${getCityCode(city)}&excludeForecast=0`)
        .then(response => response.json())
        .then(jsonResponse => {
          let items = jsonResponse.response.body.items.item;

          // analdate와 meanavg 필드를 추출하여 HTML 요소에 표시
          let fireDiv = document.querySelector('.fire-alert');
          fireDiv.innerHTML = ''; // 기존 내용을 지움

          items.forEach(function(item) {
            let analdate = item.analdate;
            let meanavg = item.meanavg;

            // analdate를 원하는 형식으로 변환
            let date = new Date(analdate.substring(0, 4), analdate.substring(5, 7) - 1, analdate.substring(8, 10), analdate.substring(11, 13));
            let formattedDate = `${date.getMonth() + 1}월 ${date.getDate()}일 ${date.getHours()}시`;

            // 새로운 HTML 콘텐츠를 생성하여 삽입
            let content = `<p>${formattedDate}<br>산불위험예보지수 : ${meanavg}</p>`;
            fireDiv.innerHTML += content;
          });
        })
        .catch(error => console.error('Failed to fetch fire data:', error));
    })
    .catch(error => console.error('Error fetching city by mountain code:', error));
}


// 좌표 변환

let NX = 149;           // X축 격자점 수
let NY = 253;           // Y축 격자점 수

let Re = 6371.00877;   // 지도반경
let grid = 5.0;         // 격자간격 (km)
let slat1 = 30.0;       // 표준위도 1
let slat2 = 60.0;       // 표준위도 2
let olon = 126.0;       // 기준점 경도
let olat = 38.0;        // 기준점 위도
let xo = 210 / grid;    // 기준점 X좌표
let yo = 675 / grid;    // 기준점 Y좌표
let first = 0;

let PI, DEGRAD, RADDEG;
let re, sn, sf, ro;

if (first === 0) {
    PI = Math.asin(1.0) * 2.0;
    DEGRAD = PI / 180.0;
    RADDEG = 180.0 / PI;

    re = Re / grid;
    slat1 = slat1 * DEGRAD;
    slat2 = slat2 * DEGRAD;
    olon = olon * DEGRAD;
    olat = olat * DEGRAD;

    sn = Math.tan(PI * 0.25 + slat2 * 0.5) / Math.tan(PI * 0.25 + slat1 * 0.5);
    sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
    sf = Math.tan(PI * 0.25 + slat1 * 0.5);
    sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
    ro = Math.tan(PI * 0.25 + olat * 0.5);
    ro = re * sf / Math.pow(ro, sn);
    first = 1;
}

function mapToGrid(lat, lon, code = 0) {
    let ra = Math.tan(PI * 0.25 + lat * DEGRAD * 0.5);
    ra = re * sf / Math.pow(ra, sn);
    let theta = lon * DEGRAD - olon;
    if (theta > PI) {
        theta -= 2.0 * PI;
    }
    if (theta < -PI) {
        theta += 2.0 * PI;
    }
    theta *= sn;
    let x = (ra * Math.sin(theta)) + xo;
    let y = (ro - ra * Math.cos(theta)) + yo;
    x = Math.floor(x + 1.5);
    y = Math.floor(y + 1.5);
    return [x, y];
}
//
//function gridToMap(x, y, code = 1) {
//    x = x - 1;
//    y = y - 1;
//    let xn = x - xo;
//    let yn = ro - y + yo;
//    let ra = Math.sqrt(xn * xn + yn * yn);
//    if (sn < 0.0) {
//        ra = -ra;
//    }
//    let alat = Math.pow((re * sf / ra), (1.0 / sn));
//    alat = 2.0 * Math.atan(alat) - PI * 0.5;
//    let theta;
//    if (Math.abs(xn) <= 0.0) {
//        theta = 0.0;
//    } else {
//        if (Math.abs(yn) <= 0.0) {
//            theta = PI * 0.5;
//            if (xn < 0.0) {
//                theta = -theta;
//            }
//        } else {
//            theta = Math.atan2(xn, yn);
//        }
//    }
//    let alon = theta / sn + olon;
//    let lat = alat * RADDEG;
//    let lon = alon * RADDEG;
//    return [lat, lon];
//}

function fetchCityByMntnCode(mntnCode) {
    fetch(`/service/getCityByMntnCode?mntnCode=${mntnCode}`)
        .then(response => response.text()) // JSON 형식이 아닌 문자열로 가져옴
        .then(data => {
            // 서버에서 받은 데이터(문자열)를 변수에 저장하거나 처리할 수 있음
            const city = data;
            // 여기에서 city 변수를 활용하여 원하는 작업을 수행할 수 있음
//            console.log(city)       //city 이름
        })
        .catch(error => console.error('Error fetching city by mountain code:', error));
}


