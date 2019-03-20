package com.spring.bnb.model;

public class ReportVO {
	
	private int report_idx;
	private String fk_userid;
	private String reporttype;
	private String report_content;
	private String report_date;
	private int report_status;
	private String report_subject;
	private int rno;

	private int viewcnt;
	
	private String previousseq;      // 이전글번호
 	private String previoussubject;  // 이전글제목
 	private String nextseq;          // 다음글번호
 	private String nextsubject;
 	private String previoustitle; 
 	private String nexttitle;
 	
 	private int commentCount;

 	
	public ReportVO() {}
	
	public ReportVO(int report_idx, String fk_userid, String reporttype, String report_content, String report_date,
			int report_status, String report_subject, int rno, int viewcnt, String previousseq, String previoussubject,
			String nextseq, String nextsubject, String previoustitle, String nexttitle, int commentCount) {
		super();
		this.report_idx = report_idx;
		this.fk_userid = fk_userid;
		this.reporttype = reporttype;
		this.report_content = report_content;
		this.report_date = report_date;
		this.report_status = report_status;
		this.report_subject = report_subject;
		this.rno = rno;
		this.viewcnt = viewcnt;
		this.previousseq = previousseq;
		this.previoussubject = previoussubject;
		this.nextseq = nextseq;
		this.nextsubject = nextsubject;
		this.previoustitle = previoustitle;
		this.nexttitle = nexttitle;
		this.commentCount = commentCount;
	}

	public int getRno() {
		return rno;
	}

	public void setRno(int rno) {
		this.rno = rno;
	}

	public int getReport_idx() {
		return report_idx;
	}

	public void setReport_idx(int report_idx) {
		this.report_idx = report_idx;
	}

	public String getFk_userid() {
		return fk_userid;
	}

	public void setFk_userid(String fk_userid) {
		this.fk_userid = fk_userid;
	}

	public String getReporttype() {
		return reporttype;
	}

	public void setReporttype(String reporttype) {
		this.reporttype = reporttype;
	}

	public String getReport_content() {
		return report_content;
	}

	public void setReport_content(String report_content) {
		this.report_content = report_content;
	}

	public String getReport_date() {
		return report_date;
	}

	public void setReport_date(String report_date) {
		this.report_date = report_date;
	}

	public int getReport_status() {
		return report_status;
	}

	public void setReport_status(int report_status) {
		this.report_status = report_status;
	}

	public String getReport_subject() {
		return report_subject;
	}

	public void setReport_subject(String report_subject) {
		this.report_subject = report_subject;
	}

	public int getViewcnt() {
		return viewcnt;
	}

	public void setViewcnt(int viewcnt) {
		this.viewcnt = viewcnt;
	}

	public String getPreviousseq() {
		return previousseq;
	}

	public void setPreviousseq(String previousseq) {
		this.previousseq = previousseq;
	}

	public String getPrevioussubject() {
		return previoussubject;
	}

	public void setPrevioussubject(String previoussubject) {
		this.previoussubject = previoussubject;
	}

	public String getNextseq() {
		return nextseq;
	}

	public void setNextseq(String nextseq) {
		this.nextseq = nextseq;
	}

	public String getNextsubject() {
		return nextsubject;
	}

	public void setNextsubject(String nextsubject) {
		this.nextsubject = nextsubject;
	}
	
	public String getPrevioustitle() {
		return previoustitle;
	}


	public void setPrevioustitle(String previoustitle) {
		this.previoustitle = previoustitle;
	}


	public String getNexttitle() {
		return nexttitle;
	}


	public void setNexttitle(String nexttitle) {
		this.nexttitle = nexttitle;
	}
	
	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
}
