import {Pagination} from '/js/community/common.js'
    //페이징 객체 생성
    const pagination = new Pagination(10, 10); // 한페이지에 보여줄 레코드건수,한페이지에 보여줄 페이지수
    //총 레코드 건수
    const totalCnt = window.totalCnt;
    const cpgs = window.cpgs;
    const cp = window.cp;

    pagination.setCurrentPageGroupStart(cpgs); //페이지 그룹 시작번호
    pagination.setCurrentPage(cp); //현재 페이지
    pagination.setTotalRecords(totalCnt); //총레코드 건수
    pagination.displayPagination(nextPage);

    function nextPage(){
      const reqPage = pagination.currentPage;   //요청 페이지
      const reqCnt = pagination.recordsPerPage; //페이지당 레코드수

      const cpgs = pagination.currentPageGroupStart; //페이지 그룹 시작번호
      const cp = pagination.currentPage;            //현재 페이지

      location.href = `/community/contactAll?reqPage=${reqPage}&reqCnt=${reqCnt}&cpgs=${cpgs}&cp=${cp}`;
    }

    const $rows = document.getElementById('rows');
              $rows.addEventListener('click',evt=>{
                //1) input요소 이면서 checkbox는 제외
                if(evt.target.tagName === 'INPUT' &&  evt.target.getAttribute('type') == 'checkbox') {
                  return;
                };
                const $row = evt.target.closest('.row');
                const inquiryId = $row.dataset.managerId;
                console.log(inquiryId);
                location.href = `/community/${inquiryId}/contactdetail`;    // GET http://localhost:9080/상품번호/detail
              });