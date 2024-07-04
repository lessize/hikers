let map = null;
const markers = [];
const infowindows = [];
let initialZoom = 7;

function getLocation() {
  return new Promise((resolve, reject) => {
    if (!navigator.geolocation) {
      reject(new Error('Geolocation 이 브라우저에서 지원되지 않음!'));
    } else {
      navigator.geolocation.getCurrentPosition(resolve, reject);
    }
  });
}

async function fetchLocation() {
  try {
    const position = await getLocation();
    const lat = position.coords.latitude;
    const lot = position.coords.longitude;
    console.log(`위도:${lat}, 경도:${lot}`);
    window.mylat = lat;
    window.mylot = lot;
    makeMap(lat, lot);
    addMountainMarkers();
  } catch (err) {
    console.error(`오류: ${err.message}`);
  }
}

window.addEventListener('load', fetchLocation);

function makeMap(lat, lot) {
  map = new naver.maps.Map('map', {
    center: new naver.maps.LatLng(lat, lot),
    zoom: initialZoom
  });

  window.addEventListener('keydown', function (event) {
    if (event.key === 'Escape') {
      map.setZoom(initialZoom);
    }
  });
}

function addMountainMarkers() {
  const rows = document.querySelectorAll('.cont_list li');
  rows.forEach(row => {
    const lat = parseFloat(row.getAttribute('data-lat'));
    const lot = parseFloat(row.getAttribute('data-lot'));
    const mntnNm = row.querySelector('span:first-child').textContent;
    const mntnCode = row.getAttribute('data-mountain-id');
    const marker = new naver.maps.Marker({
      position: new naver.maps.LatLng(lat, lot),
      map: map
    });
    const infowindow = new naver.maps.InfoWindow({
      content: `<div style="padding:10px;">${mntnNm}</div>`
    });

    naver.maps.Event.addListener(marker, 'click', function () {
      if (infowindow.getMap()) {
        infowindow.close();
      } else {
        infowindow.open(map, marker);
      }
    });

    row.addEventListener('click', function () {
      window.location.href = `http://localhost:9080/service/search?mntnCode=${mntnCode}`;
    });

    markers.push(marker);
    infowindows.push(infowindow);
  });
}
