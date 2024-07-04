
    document.addEventListener('DOMContentLoaded', function() {
      var loginLink = document.getElementById('loginLink');
      loginLink.addEventListener('click', function(event) {
        event.preventDefault();
        var form = document.createElement('form');
        form.method = 'POST';
        form.action = /*[[@{/members/login}]]*/ '/members/login';
        document.body.appendChild(form);
        form.submit();
      });
    });
