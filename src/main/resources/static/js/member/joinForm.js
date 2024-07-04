// 이메일 인증 완료 여부를 저장할 플래그 변수
let isEmailVerified = false;

// 회원 가입
document.getElementById('signupForm').addEventListener('submit', function(event) {

  if (!validateForm()) {
      return;
  }

  event.preventDefault();

  const form = event.target;
  const formData = new FormData(form);

  // 필요한 필드만 추출
  const partialData = {
    id: document.getElementById('id').value,          // 이메일
    pw: formData.get('pw'),            // 비밀번호
    nickname: formData.get('nickname'),// 닉네임
    tel: formData.get('tel'),          // 전화번호
    gender: formData.get('gender'),    // 성별
    mexp: formData.get('mexp'),         // 등산 경험
    loc: formData.get('loc')        // 지역
  };

  console.log(partialData);

  // Fetch API를 사용하여 AJAX 요청 보내기
  fetch('/members/join', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(partialData)
  })
  .then(response => {
    if (response.ok) {
      // 회원가입 성공시 로그인 페이지로 리디렉션
      window.location.href = '/members/login';
    } else {
      // 회원가입 실패시 메인 페이지로 리디렉션
      window.location.href = '/';
    }
  })
  .catch(error => {
    console.error('Error:', error);
  });
});

// 이메일 인증번호 보내기
document.getElementById('send_btn').addEventListener('click', evt => {

  const email = document.getElementById('id').value;

  // 이메일이 입력되어 있는지 검사
  if (!email) {
    alert('이메일을 입력해주세요.');
    return;
  }

  // AJAX 요청 보내기 - 이메일 존재 여부 및 인증번호 발송
  fetch('/api/members/verify', {
      method: 'POST',
      headers: {
          'Content-Type': 'application/json'
      },
      body: JSON.stringify({ id: email })
  })
  .then(response => response.text())
  .then(data => {
      if (data === "이미 존재하는 이메일입니다.") {
          // 이메일이 이미 존재하는 경우
          alert(data); // 서버에서 반환한 메시지 출력
      } else {
          // 성공적으로 인증번호를 받은 경우
          alert(`인증번호가 ${email}로 전송되었습니다.`);
      }
  })
  .catch(error => {
      console.error('Error:', error);
      alert('인증번호 발송 실패');
  });
});

// 인증번호 체크
document.getElementById('chk_btn').addEventListener('click', evt => {

  const email = document.getElementById('id').value;
  const authCode = document.getElementById('id_chk').value;

    fetch('/api/members/check', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ id: email, authCode: authCode })
    })
    .then(response => {
      if (response.ok) {
        alert('인증 성공!');
        isEmailVerified = true; // 인증 성공 시 플래그 변경
        email.disabled = true;
        document.getElementById('id_chk').disabled = true;
        send_btn.disabled = true;
        chk_btn.disabled = true;
      } else {
        return response.text().then(errorMessage => {
          throw new Error(errorMessage);
        });
      }
    })
    .catch(error => {
      console.error('Error:', error);
      alert('인증 실패: ' + error.message);
    });
});

const pwInput = document.getElementById('pw');
const pwConfirmInput = document.getElementById('pw_chk');
const pwError = document.querySelector('.not_valid');
const pwMatchError = document.querySelector('.not_equal');

// 비밀번호 유효성 체크 함수
function isValidPassword(password) {
  // 비밀번호는 영문, 숫자, 특수문자를 포함하여 8~20자여야 함
  const pattern = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,20}$/;
  return pattern.test(password);
}

// 비밀번호 확인 일치 여부 체크 함수
function passwordsMatch(password, confirmPassword) {
  return password === confirmPassword;
}

// 비밀번호 입력 필드를 떠날 때 유효성 체크
pwInput.addEventListener('blur', function() {
  const password = pwInput.value;
  if (!isValidPassword(password)) {
    pwError.textContent = '비밀번호는 영문, 숫자, 특수문자를 포함하여 8~20자여야 합니다.';
  } else {
    pwError.textContent = ''; // 오류 메시지 초기화
  }
});

// 비밀번호 확인 입력 필드를 떠날 때 일치 여부 체크
pwConfirmInput.addEventListener('blur', function() {
  const password = pwInput.value;
  const confirmPassword = pwConfirmInput.value;
  if (!passwordsMatch(password, confirmPassword)) {
    pwMatchError.textContent = '비밀번호가 일치하지 않습니다.';
  } else {
    pwMatchError.textContent = ''; // 오류 메시지 초기화
  }
});

// 필드 유효성 체크 함수
function validateForm() {
  event.preventDefault();

  const email = document.getElementById('id').value;
  const password = document.getElementById('pw').value;
  const confirmPassword = document.getElementById('pw_chk').value;
  const nickname = document.getElementById('nickname').value;
  const tel = document.getElementById('tel').value;
  const gender = document.querySelector('input[name="gender"]:checked');
  const mexp = document.getElementById('mexp').value;
  const loc = document.getElementById('loc').value;
  const agree = document.getElementById('agree').checked;

  if (!email || !password || !confirmPassword || !nickname || !tel || !gender || !mexp || !loc || !agree) {
    alert('모든 필드를 채워주세요.');
    return false;
  }

  if (!isValidPassword(password)) {
    alert('비밀번호는 영문, 숫자, 특수문자를 포함하여 8~20자여야 합니다.');
    return false;
  }

  if (!passwordsMatch(password, confirmPassword)) {
    alert('비밀번호가 일치하지 않습니다.');
    return false;
  }

  if (!isEmailVerified) {
    alert('이메일 인증을 완료해주세요.');
    return false;
  }

  return true;
}