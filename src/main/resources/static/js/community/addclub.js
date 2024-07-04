
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
    document.getElementById("date").value = new Date().getTimezoneOffset();
    var mntnCode = document.getElementById("mntnLoc").value;
    var title = document.getElementById("title").value;
    var memberId = document.getElementById("memberId").value;
    var ccontent = document.getElementById("bcontent").value;
    var personnel = document.getElementById("personnel").value;
    var code = "C"
    var file = document.getElementById("file").value;

    // 폼 데이터 생성
    var formData = new FormData();
    formData.append("date", date);
    formData.append("mntnCode", mntnCode);
    formData.append("title", title);
    formData.append("memberId", memberId);
    formData.append("bcontent", ccontent);
    formData.append("personnel", personnel);
    formData.append("code", code);
    formData.append("file", file);

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


//검색
document.addEventListener("DOMContentLoaded", function() {
  const searchInput = document.getElementById("mntnNm");
  const searchButton = document.getElementById("searchButton");
  const searchResults = document.getElementById("searchResults");
  const selectElement = document.querySelector("select[name='mntnLoc']");

  searchButton.addEventListener("click", function(event) {
    event.preventDefault();
    var query = searchInput.value.trim();
    if (query) {
      fetch(`/community/selectmntn?mntnNm=${query}`)
        .then(response => response.json())
        .then(data => {
          selectElement.innerHTML = ""; // 기존 옵션을 모두 제거

          // 기본 옵션 추가
          const defaultOption = document.createElement("option");
          defaultOption.value = "";
          defaultOption.text = "==지역선택==";
          selectElement.appendChild(defaultOption);

          if (data.length > 0) {
            data.forEach(mountain => {
              const option = document.createElement("option");
              option.value = mountain.mntnCode;
              option.text = `${mountain.mntnNm} - ${mountain.mntnLoc}`;
              selectElement.appendChild(option);
            });
          } else {
            const noResultOption = document.createElement("option");
            noResultOption.value = "000000000";
            noResultOption.text = "결과 없음";
            selectElement.appendChild(noResultOption);
          }
        })
        .catch(error => {
          console.error("Error fetching data:", error);
          selectElement.innerHTML = ""; // 기존 옵션을 모두 제거

          // 기본 옵션 추가
          const defaultOption = document.createElement("option");
          defaultOption.value = "";
          defaultOption.text = "==지역선택==";
          selectElement.appendChild(defaultOption);

          // 오류 발생 시 "결과 없음" 옵션 추가
          const errorOption = document.createElement("option");
          errorOption.value = "1";
          errorOption.text = "데이터를 가져오는 중 오류가 발생했습니다.";
          selectElement.appendChild(errorOption);
        });
    }
  });
});
