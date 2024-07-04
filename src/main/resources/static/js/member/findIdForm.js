//$(document).ready(function() {
//  $('#findIdForm').on('submit', function(event) {
//    event.preventDefault(); // 폼의 기본 제출 동작을 막음
//
//    var formData = {
//      nickname: $('input[name="nickname"]').val(),
//      tel: $('input[name="tel"]').val()
//    };
//
//    $.ajax({
//      type: 'POST',
//      url: '/members/findId',
//      contentType: 'application/json',
//      data: JSON.stringify(formData),
//      success: function(response) {
//        if (response.status === 'success') {
//          $('.your_id').text(response.id);
//          $('.existedId').css('visibility', 'visible');
//        } else {
//          alert(response.message);
//          $('.existedId').css('visibility', 'hidden');
//        }
//      },
//      error: function() {
//        alert('An error occurred while processing your request.');
//      }
//    });
//  });
//});

$(document).ready(function() {
  $('#findIdForm').on('submit', function(event) {
    event.preventDefault(); // 폼의 기본 제출 동작을 막음

    var formData = {
      nickname: $('input[name="nickname"]').val(),
      tel: $('input[name="tel"]').val()
    };

    fetch('/members/findId', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(formData)
    })
    .then(response => response.json())
    .then(data => {
      if (data.status === 'success') {
        $('.your_id').text(data.id);
        $('.existedId').css('visibility', 'visible');
      } else {
        alert(data.message);
        $('.existedId').css('visibility', 'hidden');
      }
    })
    .catch(error => {
      console.error('요청 처리 중 오류가 발생했습니다.', error);
      alert('요청 처리 중 오류가 발생했습니다.');
    });
  });
});
