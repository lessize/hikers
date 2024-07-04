// 이미지 요청을 보낼 URL
const code = document.getElementById('code').value;
const bbsId = document.getElementById('bbsid').value;
const tags = document.getElementById('tags').value;
const staring = document.getElementById('staring').value;

function loadImage() {

    console.log(code);
    console.log(bbsId);
    console.log(tags);
    console.log(staring);

    var url = "/images/" + "B" + "/" + bbsId; // 적절한 URL로 변경해야 합니다.
    console.log(url);

    // 이미지 요청 보내기
    fetch(url)
        .then(response => response.blob())
        .then(blob => {
            var img = document.createElement("img");
            img.src = URL.createObjectURL(blob);
            img.alt = "이미지";

            // 이미지를 특정 위치에 추가
            var imageContainer = document.getElementById("imageContainer");
            imageContainer.appendChild(img);
        })
        .catch(error => console.error('Error fetching image:', error));

    // 별점 변환
    let starcod;
    switch (parseInt(staring, 10)) {
        case 1:
            starcod = "☆☆☆☆★";
            break;
        case 2:
            starcod = "☆☆☆★★";
            break;
        case 3:
            starcod = "☆☆★★★";
            break;
        case 4:
            starcod = "☆★★★★";
            break;
        case 5:
            starcod = "★★★★★";
            break;
        default:
            starcod = "별점을 알 수 없음";
            console.log("어떤 값인지 파악이 되지 않습니다.");
    }

    // 태그 변환
    const tagsname = [
        "#주차장있어요", "#경사심해요", "#화장실많아요",
        "#케이블카있음", "#계단많아요", "#급수대많아요"
    ];

    // 빈 배열을 생성하여 조건에 맞는 태그들을 저장
    let result = [];

    // for 문을 이용하여 문자열과 배열을 순회
    for (let i = 0; i < tagsname.length; i++) {
        // 문자열의 현재 인덱스 값이 '1'인 경우 태그를 결과 배열에 추가
        if (tags[i] === '1') {
            result.push(tagsname[i]);
        }
    }

    // 결과 배열을 문자열로 변환하여 출력 (공백으로 구분)
    let resulttags = result.join(' ');
    if(resulttags == ''){
        resulttags = "선택된 태그가 없습니다."
    }
    const finalstarcod = "별점 : "+ starcod;
    const finaltags = "태그 : "+ resulttags;
    console.log(finalstarcod);
    console.log(finaltags);

    // 별점과 태그를 표시할 요소에 추가
    document.getElementById('star').textContent = finalstarcod;
    document.getElementById('tagslog').textContent = finaltags;
}

// 페이지 로드 시 이미지 로드 함수 호출
window.onload = function() {
    loadImage();
}
  //수정버튼
const $modifyBtn = document.getElementById('modifyBtn');
console.log($modifyBtn)
if($modifyBtn != null){
     //수정버튼
    $modifyBtn.addEventListener('click',e=>{
        const bbsid = document.getElementById('bbsid').value;
        var url2 = "/community/"+bbsid+"/edit";
        location.href=url2;
    });
}

const $deleteBtn = document.getElementById('deleteBtn');  //수정버튼
console.log($deleteBtn)
if($deleteBtn != null){
    $deleteBtn.addEventListener('click',e=>{
        const bbsid = document.getElementById('bbsid').value;
        var url2 = "/community/"+bbsid+"/del";
        location.href=url2;
    });
}
  //수정


document.addEventListener('DOMContentLoaded', function() {
    fetchComments();
    if(document.getElementById('submit-comment') != null){
        document.getElementById('submit-comment').addEventListener('click', function() {
            submitComment();
        });
    }
});

function fetchComments() {
    const currentBbsId = bbsId; // 현재 페이지의 bbsId
    fetch('/community/rbbsAll')
        .then(response => response.json())
        .then(data => {
            const commentsContainer = document.getElementById('comments-container');
            commentsContainer.innerHTML = ''; // 컨테이너 비우기
            data.list.forEach(item => {
                if (item.bbsId == currentBbsId) {
                    const commentDiv = document.createElement('div');
                    commentDiv.className = 'comment';
                    commentDiv.textContent = item.comments;
                    commentsContainer.appendChild(commentDiv);
                }
            });
        })
        .catch(error => console.error('Error fetching data:', error));
}

function submitComment() {
    const memberId = document.getElementById('memberId').value;
    const comments = document.getElementById('comments').value;
    console.log(memberId);
    console.log(comments);
    fetch('/community/addrbbs', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
            bbsId: bbsId,
            memberId: memberId,
            comments: comments
        })
    })
    .then(response => {
        if (response.ok) {
            return response.text();
        }
        throw new Error('Network response was not ok.');
    })
    .then(data => {
        console.log('Comment submitted:', data);
        // 댓글 등록 후 새로운 댓글 목록을 다시 불러옵니다.
        fetchComments();
    })
    .catch(error => {
        console.error('Error submitting comment:', error);
    });
}
