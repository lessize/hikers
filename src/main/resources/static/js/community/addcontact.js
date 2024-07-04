function prepareData() {
            // 모든 체크박스를 선택합니다.
            let checkboxes = document.querySelectorAll('input[type="checkbox"]');
            let data = '';
            checkboxes.forEach((checkbox) => {
                // 체크된 경우 1, 체크되지 않은 경우 0을 data 문자열에 추가합니다.
                data += checkbox.checked ? '1' : '0';
            });
            console.log("test");
            // 변환된 데이터를 숨겨진 입력 필드에 설정합니다.
            document.getElementById('checkboxData').value = data;
        }

document.addEventListener('DOMContentLoaded', function() {
    let checkboxes = document.querySelectorAll('input[type="checkbox"]');

    function prepareData() {
        let data = '';
        checkboxes.forEach((checkbox) => {
            data = checkbox.checked ? '1' : '0';
        });
        console.log(data);
        document.getElementById('checkboxData').value = data;
    }

    checkboxes.forEach((checkbox) => {
        checkbox.addEventListener('change', prepareData);
    });

    // 초기 데이터 설정
    prepareData();
});


function handleFileSelect(event) {
            // 파일 입력 필드에서 선택된 파일들 가져오기
            let files = event.target.files;
            let output = document.getElementById('fileList');

            // 기존 파일 목록 지우기
            output.innerHTML = '';

            // 선택된 파일들 목록을 보여주기
            for (let i = 0; i < files.length; i++) {
                let file = files[i];
                let listItem = document.createElement('li');
                listItem.textContent = file.name;
                output.appendChild(listItem);
            }
        }

function submitForm() {
    // 사용자가 입력한 값 가져오기
    var title = document.getElementById("title").value;
    var memberId = document.getElementById("memberId").value;
    var ccontent = document.getElementById("bcontent").value;
    var personnel = document.getElementById("checkboxData").value;

    // 폼 데이터 생성
    var formData = new FormData();
    formData.append("title", title);
    formData.append("memberId", memberId);
    formData.append("bcontent", ccontent);
    formData.append("hidden", hidden);

    // 서버로 데이터 전송
    fetch('/community/addclub', {
      method: 'POST',
      body: formData
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      return response.json();
    })
    .then(data => {
      // 서버로부터 받은 응답 처리
      console.log(data);
      // 여기에 서버로부터 받은 응답에 따른 동작 추가
    })
    .catch(error => {
      console.error('There was a problem with your fetch operation:', error);
    });
  }