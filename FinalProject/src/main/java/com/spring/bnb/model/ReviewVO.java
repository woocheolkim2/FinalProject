package com.spring.bnb.model;

public class ReviewVO {
	private int review_idx;
	private String fk_roomcode;
	private String fk_userid;
	private int correct;
	private int communicate; 
	private int clean;
	private int position; 
	private int checkin; 
	private int value;
	private String review_content;
	private String review_writedate;
	private String hostAnswer;
	
	private MemberVO user;
	private RoomVO room;
	
	public ReviewVO() {}
	public ReviewVO(int review_idx, String fk_roomcode, String fk_userid, int correct, int communicate, int clean,
			int position, int checkin, int value, String review_content, String review_writedate, String hostAnswer) {
		super();
		this.review_idx = review_idx;
		this.fk_roomcode = fk_roomcode;
		this.fk_userid = fk_userid;
		this.correct = correct;
		this.communicate = communicate;
		this.clean = clean;
		this.position = position;
		this.checkin = checkin;
		this.value = value;
		this.review_content = review_content;
		this.review_writedate = review_writedate;
		//this.hostAnswer = hostAnswer;
	}
	public int getReview_idx() {
		return review_idx;
	}
	public void setReview_idx(int review_idx) {
		this.review_idx = review_idx;
	}
	public String getFk_roomcode() {
		return fk_roomcode;
	}
	public void setFk_roomcode(String fk_roomcode) {
		this.fk_roomcode = fk_roomcode;
	}
	public String getFk_userid() {
		return fk_userid;
	}
	public void setFk_userid(String fk_userid) {
		this.fk_userid = fk_userid;
	}
	public int getCorrect() {
		return correct;
	}
	public void setCorrect(int correct) {
		this.correct = correct;
	}
	public int getCommunicate() {
		return communicate;
	}
	public void setCommunicate(int communicate) {
		this.communicate = communicate;
	}
	public int getClean() {
		return clean;
	}
	public void setClean(int clean) {
		this.clean = clean;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getCheckin() {
		return checkin;
	}
	public void setCheckin(int checkin) {
		this.checkin = checkin;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getReview_content() {
		return review_content;
	}
	public void setReview_content(String review_content) {
		this.review_content = review_content;
	}
	public String getReview_writedate() {
		return review_writedate;
	}
	public void setReview_writedate(String review_writedate) {
		this.review_writedate = review_writedate;
	}
	public String getHostAnswer() {
		return hostAnswer;
	}
	public void setHostAnswer(String hostAnswer) {
		this.hostAnswer = hostAnswer;
	}
	public MemberVO getUser() {
		return user;
	}
	public void setUser(MemberVO user) {
		this.user = user;
	}
	public RoomVO getRoom() {
		return room;
	}
	public void setRoom(RoomVO room) {
		this.room = room;
	}
	
	
}
