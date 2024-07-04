// 로그아웃
const $logout = document.getElementById('logout');
    $logout?.addEventListener('click',evt=>{
      evt.preventDefault(); // 기본 이벤트 제거
      fetch('/members/logout',{method:'POST',body:null})
        .then(res=>res.text())
        .then(res=>{
          if(res==='OK'){
            location.href='/';
          }
         })
        .catch(console.log);
    });

// 메뉴 버튼
document.addEventListener('DOMContentLoaded', function () {
  var menuCheckbox = document.getElementById('menu_checkbox');
  var navMenu = document.querySelector('nav');
  var asideNav = document.getElementById('nav_btn');
  var menuLabel = document.getElementById('menu_label');
  var textBar = document.getElementById('menu_text_bar');
  var searchMenu = document.getElementById('search_menu');

  menuCheckbox.addEventListener('change', function() {
    if(this.checked) {
      navMenu.style.left = '0';
      asideNav.style.transition = 'background-color 0.3s ease-in-out';
      asideNav.style.backgroundColor = 'var(--color-green)';
      menuLabel.style.transition = 'background-color 0.3s ease-in-out';
      menuLabel.style.backgroundColor = 'var(--color-green)';
      textBar.style.transition = 'background-color 0.3s ease-in-out';
      textBar.style.backgroundColor = '#fff';
      searchMenu.style.transition = 'opacity 0.6s ease-in-out';
      searchMenu.style.opacity = 0;
    } else {
      navMenu.style.transition = 'left 0.6s ease-in-out';
      navMenu.style.left = '-500%';
      asideNav.style.transition = 'background-color 0.3s ease-in-out';
      asideNav.style.backgroundColor = 'var(--color-beige)';
      menuLabel.style.transition = 'background-color 0.3s ease-in-out';
      menuLabel.style.backgroundColor = 'var(--color-beige)';
      textBar.style.transition = 'background-color 0.3s ease-in-out';
      textBar.style.backgroundColor = '#333';
      searchMenu.style.opacity = 1;
    }
  });
});

//홈 이동
document.addEventListener('DOMContentLoaded', function() {
  const home = document.querySelector("body > div > header > div > h1 > img");
  home.addEventListener('click', function(){
    window.location.href = '/';
  })
})


// 검색창
document.addEventListener('DOMContentLoaded', function() {
  const fglass = document.querySelector('.fa-magnifying-glass');
  fglass.addEventListener('click', function() {
    window.location.href = '/service/search_mountain';
  });
});
