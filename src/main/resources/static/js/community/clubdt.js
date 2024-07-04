import {Pagination} from '/js/community/common.js'

const clubId = document.getElementById('clubId').value;

function loadImage() {
    // 이미지 요청을 보낼 URL
    const code = document.getElementById('code').value;

    console.log(code);
    console.log(clubId);

    var url = "/images/C/" + clubId; // 적절한 URL로 변경해야 합니다.
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
        }

// 페이지 로드 시 이미지 로드 함수 호출
window.onload = function() {
    loadImage();
};

const $modifyBtn = document.getElementById('modifyBtn');  //수정버튼
console.log($modifyBtn)
if($modifyBtn != null){
    $modifyBtn.addEventListener('click',e=>{
        const clubId = document.getElementById('clubId').value;
        var url2 = "/community/"+clubId+"/clubedit";
        location.href=url2;
    });
}

const $deleteBtn = document.getElementById('deleteBtn');  //수정버튼
console.log($deleteBtn)
if($deleteBtn != null){
    $deleteBtn.addEventListener('click',e=>{
        const clubId = document.getElementById('clubId').value;
        var url2 = "/community/"+clubId+"/clubdel";
        location.href=url2;
    });
}


document.addEventListener('DOMContentLoaded', function() {
    fetchComments();
    if(document.getElementById('submit-comment') != null){
        document.getElementById('submit-comment').addEventListener('click', function() {
            submitComment();
        });
    }
});



function fetchComments() {
    const currentclubId = clubId; // 현재 페이지의 clubId
    fetch('/community/rclubAll')
        .then(response => response.json())
        .then(data => {
            const commentsContainer = document.getElementById('comments-container');
            commentsContainer.innerHTML = ''; // 컨테이너 비우기
            data.list.forEach(item => {
                if (item.clubId == currentclubId) {
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
    fetch('/community/addrclub', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
            clubId: clubId,
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
