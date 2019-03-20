package com.spring.bnb.model;


// ===== #82. 댓글쓰기용 VO 생성하기
//            먼저 오라클에서 tblComment 테이블을 생성한다.
//            또한 tblBoard 테이블에 commentCount 컬럼을 추가한다. =====

public class CommentVO {
	
	private String report_seq;          // 댓글번호
	private String fk_userid;    // 사용자ID
	private String name;         // 성명
	private String content;      // 댓글내용
	private String regDate;      // 작성일자
	private String parentSeq;    // 원게시물 글번호
	private String status;       // 글삭제여부
	
	public CommentVO() { }
	
	public CommentVO(String report_seq, String fk_userid, String name, String content, String regDate, String parentSeq, String status) {
		this.report_seq = report_seq;
		this.fk_userid = fk_userid;
		this.name = name;
		this.content = content;
		this.regDate = regDate;
		this.parentSeq = parentSeq;
		this.status = status;
	}

	public String getReport_seq() {
		return report_seq;
	}

	public void setReport_seq(String report_seq) {
		this.report_seq = report_seq;
	}

	public String getFk_userid() {
		return fk_userid;
	}

	public void setFk_userid(String fk_userid) {
		this.fk_userid = fk_userid;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getParentSeq() {
		return parentSeq;
	}

	public void setParentSeq(String parentSeq) {
		this.parentSeq = parentSeq;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
