function toggleOrder() {
  const mntnList = document.querySelector('.mntn_list');
  const mntnItems = Array.from(mntnList.children);
  const currentOrder = mntnList.getAttribute('data-order') || 'DESC';
  const newOrder = currentOrder === 'ASC' ? 'DESC' : 'ASC';
  mntnList.setAttribute('data-order', newOrder);
  const toggleButton = document.querySelector('.toggle-button');

  toggleButton.textContent = newOrder === 'ASC' ? '별점 높은 순' : '별점 낮은 순';

  mntnItems.sort((a, b) => {
    const aRate = parseFloat(a.querySelector('.star-rating').getAttribute('data-rating'));
    const bRate = parseFloat(b.querySelector('.star-rating').getAttribute('data-rating'));
    const aName = a.querySelector('.mntn_name').innerText;
    const bName = b.querySelector('.mntn_name').innerText;

    if (aRate === bRate) {
      if (newOrder === 'ASC') {
        return aName.localeCompare(bName, 'ko');
      } else {
        return bName.localeCompare(aName, 'ko');
      }
    }

    return newOrder === 'ASC' ? aRate - bRate : bRate - aRate;
  });

  mntnItems.forEach(item => mntnList.appendChild(item));
}

document.addEventListener("DOMContentLoaded", function () {
  const starRatings = document.querySelectorAll('.star-rating');
  starRatings.forEach(rating => {
    const ratingValue = Math.round(parseFloat(rating.getAttribute('data-rating')));
    const starsInner = rating.querySelector('.stars-inner');
    const percentage = (ratingValue / 5) * 100;
    starsInner.style.width = percentage + '%';
  });

  const mntnList = document.querySelector('.mntn_list');
  mntnList.setAttribute('data-order', 'DESC');
  document.querySelector('.toggle-button').textContent = '별점 낮은 순';

  const mntnItems = document.querySelectorAll('.mntn_item');
  mntnItems.forEach(item => {
    item.addEventListener('click', function () {
      const mntnCode = item.getAttribute('data-mountain-id');
      window.location.href = `http://localhost:9080/service/search?mntnCode=${mntnCode}`;
    });
  });
});