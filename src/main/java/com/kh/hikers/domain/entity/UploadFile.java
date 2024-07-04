package com.kh.hikers.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UploadFile {
  // 파일 아이디
  private Long UPLOADFILE_ID;       //	NUMBER(10,0)
  // 코드
  private String CODE;              //	VARCHAR2(11 BYTE)
  // 참조 번호
  private String RID;               //	VARCHAR2(10 BYTE)
  // 서버 보관 파일명
  private String STORE_FILENAME;    //	VARCHAR2(100 BYTE)
  // 업로드 파일명
  private String UPLOAD_FILENAME;   //	VARCHAR2(100 BYTE)
  // 업로드 파일 크기
  private String FSIZE;             //	VARCHAR2(45 BYTE)
  // 파일 유형
  private String FTYPE;             //	VARCHAR2(100 BYTE)
  // 등록 일시
  private LocalDateTime CDATE;      //	TIMESTAMP(6)
  // 수정 일시
  private LocalDateTime UDATE;      //	TIMESTAMP(6)
}
