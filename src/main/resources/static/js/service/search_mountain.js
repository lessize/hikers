document.addEventListener("DOMContentLoaded", function() {
    const textBox = document.getElementById("stb");
    const showRegionButton = document.getElementById("showRegion");
    const hiddenSearchDiv = document.getElementById("hiddenSearch");
    const hiddenSelectDiv = document.getElementById("hiddenSelect");
    let mountains = [];

    // Fetch all mountains data from server on page load
    fetch('/service/api/allMountains')
        .then(response => response.json())
        .then(data => {
            mountains = data;
        });

    textBox.addEventListener("click", function() {
        hiddenSearchDiv.style.display = "block";
        hiddenSelectDiv.style.display = "none";
    });

    showRegionButton.addEventListener("click", function() {
        hiddenSearchDiv.style.display = "none";
        hiddenSelectDiv.style.display = "block";
    });

    textBox.addEventListener("input", function() {
        autoComplete();
    });

    function autoComplete() {
        const query = textBox.value.toLowerCase();
        hiddenSearchDiv.innerHTML = '';
        if (query.length > 0) {
            const filteredMountains = mountains.filter(mountain =>
                mountain.mntnNm.toLowerCase().includes(query)
            );
            if (filteredMountains.length > 0) {
                filteredMountains.forEach(mountain => {
                    const div = document.createElement('div');
                    div.textContent = `${mountain.mntnNm} (${mountain.city})`;
                    div.classList.add('autocomplete-item');
                    div.addEventListener('click', function() {
                        const mntnCode = mountain.mntnCode;
                        if (mntnCode) {
                            // Redirect to search page with mntnCode parameter
                            console.log(mntnCode);
                            window.location.href = `/service/search?mntnCode=${mntnCode}`;
                        }
                    });
                    hiddenSearchDiv.appendChild(div);
                });
                hiddenSearchDiv.style.display = 'block';
            } else {
                hiddenSearchDiv.style.display = 'none';
            }
        } else {
            hiddenSearchDiv.style.display = 'none';
        }
    }

    document.getElementById("searchForm").addEventListener("submit", function(event) {
      const searchText = document.getElementById("stb").value.trim(); // 입력 필드의 값 가져오기 및 공백 제거
      if (searchText === "") {
        alert("산 이름을 입력하세요!"); // 입력 필드가 비어 있는 경우 경고창 표시
        event.preventDefault(); // 폼 제출 방지
      }
    });
});