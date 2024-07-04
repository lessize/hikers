document.addEventListener('DOMContentLoaded', init);

function init() {
  document.getElementById('chkBtn').addEventListener('click', evt => chkValid(evt));
}

function chkValid(evt) {
  evt.preventDefault(); // 버튼의 기본 동작을 막음
  const id = document.getElementById('id');
  const nickname = document.getElementById('nickname');

  if (id.value.trim().length === 0 || nickname.value.trim().length === 0) {
    alert('아이디와 닉네임을 모두 입력하세요.');
    return;
  }
  postData(id.value, nickname.value);
}

async function postData(id, nickname) {
  const payload = {
    'id': id,
    'nickname': nickname
  };

  try {
    const response = await fetch('/members/findPwd', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload)
    });

    const result = await response.json();
    if (response.ok) {
      if (result.status === 'success') {
        alert(result.message);
      } else {
        alert(result.message);
      }
    } else {
      console.error(result.message);
      alert(result.message);
    }
  } catch (error) {
    console.error('Error:', error);
    alert('요청 처리 중 오류가 발생했습니다.');
  }
}
