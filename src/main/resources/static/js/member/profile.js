
//function updateProfile() {
//    var formData = $("#profileForm").serialize();
//
//    $.ajax({
//        type: "POST",
//        url: "/members/profile/update",
//        data: formData,
//        success: function(response) {
//            // 응답이 성공적일 경우, 화면을 새로고침하지 않고 값을 업데이트
//            $("span[th\\:text='${session.loginMember.tel}']").text($("input[name='tel']").val());
//            $("span[th\\:text='${session.loginMember.nickname}']").text($("input[name='nickname']").val());
//
//            // 회원 경험 값을 텍스트로 변환하여 업데이트
//            var mexpText = response.mexp == 0 ? '초급' : (response.mexp == 1 ? '중급' : '고급');
//            $("span[th\\:text=\"${session.loginMember.mexp == 0 ? '초급' : (session.loginMember.mexp == 1 ? '중급' : '고급')}\"]").text(mexpText);
//
//            // 지역 업데이트
//            $("span[th\\:text='${session.loginMember.loc}']").text($("#loc").val());
//
//            alert("프로필이 성공적으로 업데이트되었습니다.");
//        },
//        error: function(xhr, status, error) {
//            alert("프로필 업데이트에 실패하였습니다.");
//        }
//    });
//}

function updateProfile() {
    var formData = new FormData(document.getElementById('profileForm'));

    fetch('/members/profile/update', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        // 응답이 성공적일 경우, 화면을 새로고침하지 않고 값을 업데이트

        var telElement = document.querySelector("span[th\\:text='${session.loginMember.tel}']");
        if (telElement) {
            telElement.textContent = document.querySelector("input[name='tel']").value;
        }

        var nicknameElement = document.querySelector("span[th\\:text='${session.loginMember.nickname}']");
        if (nicknameElement) {
            nicknameElement.textContent = document.querySelector("input[name='nickname']").value;
        }

        // 회원 경험 값을 텍스트로 변환하여 업데이트
        var mexpText = data.mexp == 0 ? '초급' : (data.mexp == 1 ? '중급' : '고급');
        var mexpElement = document.querySelector("span[th\\:text=\"${session.loginMember.mexp == 0 ? '초급' : (session.loginMember.mexp == 1 ? '중급' : '고급')}\"]");
        if (mexpElement) {
            mexpElement.textContent = mexpText;
        }

        // 지역 업데이트
        var locElement = document.querySelector("span[th\\:text='${session.loginMember.loc}']");
        if (locElement) {
            locElement.textContent = document.getElementById('loc').value;
        }

        alert("프로필이 성공적으로 업데이트되었습니다.");
    })
    .catch(error => {
        console.error('프로필 업데이트에 실패하였습니다.', error);
        alert("프로필 업데이트에 실패하였습니다.");
    });
}
