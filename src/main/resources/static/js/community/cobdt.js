const $modifyBtn = document.getElementById('modifyBtn');  //수정버튼
console.log($modifyBtn)
      //수정

if($modifyBtn != null){
    $modifyBtn.addEventListener('click',e=>{
        const inquiryId = document.getElementById('inquiryId').value;
        var url2 = "/community/"+inquiryId+"/contactedit";
        location.href=url2;
    });
}