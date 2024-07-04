document.addEventListener("DOMContentLoaded", function() {
    const boxes = document.querySelectorAll('#reco_sec ul li');

    boxes.forEach(box => {
        box.addEventListener('click', () => {
            boxes.forEach(b => {
                b.style.width = '200px';
                const div = b.querySelector('div');
                div.style.transform = 'translate(-50%, -50%) rotate(-0.25turn)';
                div.style.bottom = '40%';
                div.style.left = '70%';
                const recoCont = b.querySelector('.reco_cont');
                recoCont.style.display = 'none';
            });

            box.style.width = '1000px';
            const div = box.querySelector('div');
            div.style.transform = 'translate(-50%, -50%) rotate(0)';
            div.style.bottom = '0%';
            div.style.left = '30%';
            const recoCont = box.querySelector('.reco_cont');
            recoCont.style.display = 'block';
        });
    });

    // 세션의 지역 데이터 가져오기
    var loc = document.querySelector('.weather').getAttribute('data-loc');

    // 지역별 좌표 매핑
    var locationCoordinates = {
        '서울특별시': ['60', '127'],
        '부산광역시': ['98', '76'],
        '대구광역시': ['89', '90'],
        '인천광역시': ['55', '124'],
        '광주광역시': ['58', '74'],
        '대전광역시': ['67', '100'],
        '울산광역시': ['102', '85'],
        '세종특별자치시': ['66', '103'],
        '경기도': ['60', '120'],
        '충청북도': ['68', '103'],
        '충청남도': ['68', '100'],
        '강원도': ['73', '134'],
        '경상북도': ['89', '91'],
        '경상남도': ['71', '89'],
        '전라북도': ['63', '89'],
        '전라남도': ['51', '68'],
        '제주특별자치도': ['52', '38']
    };

    // 기본 좌표 설정 (서울특별시)
    var defaultCoordinates = ['60', '127'];

    // 세션 데이터가 있으면 해당 지역 좌표로 설정
    var gridCoordinates;
    if (loc && locationCoordinates[loc]) {
        gridCoordinates = locationCoordinates[loc];
    } else {
        // 세션 데이터가 없거나 잘못된 경우, 기본 좌표로 설정
        gridCoordinates = defaultCoordinates;
    }

    // 오늘의 날짜 구하기
    var today = new Date();
    var year = today.getFullYear();
    var month = ('0' + (today.getMonth() + 1)).slice(-2); // 월은 0부터 시작하므로 +1, 두 자리로 표시
    var day = ('0' + today.getDate()).slice(-2); // 날짜를 두 자리로 표시

    var base_date = year + month + day; // 날짜
    var base_time = '0200'; // 시간

    // 필터링할 카테고리 배열 정의
    var categoriesToPrint = ['POP', 'SKY', 'TMX', 'TMN'];

    // 날씨 정보 가져오기
    fetchWeatherForecast(base_date, base_time, gridCoordinates, categoriesToPrint);
});

// 기상 예보를 가져와서 화면에 출력하는 함수
function fetchWeatherForecast(base_date, base_time, gridCoordinates, categoriesToPrint) {
    var xhr = new XMLHttpRequest();
    var url = 'http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst'; // API 엔드포인트 URL
    var queryParams = '?' + encodeURIComponent('serviceKey') + '=' + 'GjOuV44hgVtQSgSphSIxH8wQ16f2Oi0VbOHNe%2B7T71%2BvSiyt5GmPnwB%2FZiUfntBSaI4qY38pj9Eue09omH8c9A%3D%3D'; // 서비스 키
    queryParams += '&' + encodeURIComponent('pageNo') + '=' + encodeURIComponent('1'); // 페이지 번호
    queryParams += '&' + encodeURIComponent('numOfRows') + '=' + encodeURIComponent('1000'); // 한 페이지 결과 수
    queryParams += '&' + encodeURIComponent('dataType') + '=' + encodeURIComponent('JSON'); // 데이터 타입
    queryParams += '&' + encodeURIComponent('base_date') + '=' + encodeURIComponent(base_date); // 발표 일자
    queryParams += '&' + encodeURIComponent('base_time') + '=' + encodeURIComponent(base_time); // 발표 시간
    queryParams += '&' + encodeURIComponent('nx') + '=' + encodeURIComponent(gridCoordinates[0]); // 격자 X 좌표
    queryParams += '&' + encodeURIComponent('ny') + '=' + encodeURIComponent(gridCoordinates[1]); // 격자 Y 좌표

    xhr.open('GET', url + queryParams);
    xhr.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            var jsonResponse = JSON.parse(this.responseText);
            displayWeather(jsonResponse, categoriesToPrint);
        } else if (this.status !== 200) {
            console.error('Error fetching data:', this.status, this.statusText);
        }
    };

    xhr.send();
}

// 날씨 데이터를 화면에 출력하는 함수
function displayWeather(jsonResponse, categoriesToPrint) {
    var weatherDiv = document.querySelector('.weather');
    weatherDiv.innerHTML = ''; // 기존 데이터를 지우고 새로 갱신

    // 스카이 코드에 대한 매핑 객체
    var skyCodeMap = {
        '1': '<i class="fa-regular fa-sun"></i>',
        '3': '<i class="fa-solid fa-cloud-sun"></i>',
        '4': '<i class="fa-solid fa-cloud"></i>'
    };

    var categoryNameMap = {
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

    if (jsonResponse.response && jsonResponse.response.body && jsonResponse.response.body.items) {
        var items = jsonResponse.response.body.items.item.filter(function (item) {
            return item.fcstTime === '0600' || item.fcstTime === '1500'; // 'fcstTime'이 '0600' 또는 '1500'인 것만 필터링
        });

        var dates = {};
        var maxTemperature, minTemperature; // 최고온도와 최저온도를 저장할 변수

        items.forEach(function (item) {
            if (!dates[item.fcstDate]) {
                dates[item.fcstDate] = [];
            }
            dates[item.fcstDate].push(item);
        });

        // 필터링된 데이터 출력
        for (let date in dates) {
            var dateDiv = document.createElement('div');
            dateDiv.className = 'weather-date';
            dateDiv.innerHTML = '<h3>' + date.substring(4, 6) + '월 ' + date.substring(6, 8) + '일</h3>';

            dates[date].forEach(function (item) {
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

            weatherDiv.appendChild(dateDiv);
        }
    }
}
