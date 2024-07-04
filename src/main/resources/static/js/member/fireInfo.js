var xhr = new XMLHttpRequest();
var url = 'http://apis.data.go.kr/1400377/forestPoint/forestPointListSidoSearch'; // API URL 설정
var queryParams = '?' + encodeURIComponent('ServiceKey') + '='+'GjOuV44hgVtQSgSphSIxH8wQ16f2Oi0VbOHNe%2B7T71%2BvSiyt5GmPnwB%2FZiUfntBSaI4qY38pj9Eue09omH8c9A%3D%3D'; // Service Key
queryParams += '&' + encodeURIComponent('pageNo') + '=' + encodeURIComponent('1'); // 페이지 번호
queryParams += '&' + encodeURIComponent('numOfRows') + '=' + encodeURIComponent('10'); // 한 페이지 결과 수
queryParams += '&' + encodeURIComponent('_type') + '=' + encodeURIComponent('xml'); // 응답 형식 (xml)
queryParams += '&' + encodeURIComponent('excludeForecast') + '=' + encodeURIComponent('0'); // 예보 제외 여부

xhr.open('GET', url + queryParams);
xhr.onreadystatechange = function () {
    if (xhr.readyState === XMLHttpRequest.DONE) {
        if (xhr.status === 200) {
            // 성공적으로 데이터를 받아왔을 때 처리
            var responseText = xhr.responseText;
            var parser = new DOMParser();
            var xmlDoc = parser.parseFromString(responseText, 'text/xml');
            // XML 데이터 처리 예시
            var items = xmlDoc.getElementsByTagName('item');

            // 산불 예보지수를 기준으로 내림차순 정렬
            var sortedItems = Array.from(items).sort(function(a, b) {
                var meanavgA = parseFloat(a.getElementsByTagName('meanavg')[0].textContent);
                var meanavgB = parseFloat(b.getElementsByTagName('meanavg')[0].textContent);
                return meanavgB - meanavgA;
            });

            // 이미 출력한 도 이름을 저장할 Set 생성
            var printedNames = new Set();

            // HTML을 추가할 요소 선택
            var fireDiv = document.querySelector('.fire-alert');
            fireDiv.innerHTML = ''; // 기존 내용 초기화

            // 각 산불 정보 항목을 처리하여 HTML로 변환 후 추가 (상위 3개만)
            var count = 0;
            sortedItems.forEach(function(item) {
                if (count >= 3) return; // 상위 3개만 처리

                var doname = item.getElementsByTagName('doname')[0].textContent;
                var meanavg = item.getElementsByTagName('meanavg')[0].textContent;

                // 중복된 도 이름이면 처리하지 않음
                if (printedNames.has(doname)) return;

                // 출력한 도 이름 Set에 추가
                printedNames.add(doname);

                // 분석 일자 포맷팅 (현재 시각 기준으로 처리)
                var date = new Date();
                var formattedDate = `${date.getMonth() + 1}월 ${date.getDate()}일 ${date.getHours()}시`;

                // 산불 정보 HTML 생성 및 추가
                var content = `<ul class="attention">
                                 <li>${doname}</li>
                                 <li>산불 위험 예보 지수 : ${meanavg}</li>
                                 <li>분석 일자 : ${formattedDate}</li>
                               </ul>`;
                fireDiv.innerHTML += content;

                count++;
            });

        } else {
            // 데이터를 가져오지 못했을 때 오류 처리
            console.error('Failed to fetch data. Status:', xhr.status);
        }
    }
};

xhr.send();
