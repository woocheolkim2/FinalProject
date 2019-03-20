package com.spring.bnb.model;

import java.util.List;

public class MemberVO {

	private String userid; 
	private String profileimg;
	private String username;
	private String pwd;
	private String email; 
	private String phone;
	private int post;
	private String addr;
	private String detailAddr; 
	private int gender;
	private String birthday;  
	private String introduction; 
	private int memberStatus;
	private int warnCount;
	private String regDate;
	private String lastLoginDate;
	
	//add
	private List<RoomVO> myroomList;
	
	public MemberVO() {}

	public MemberVO(String userid, String profileimg, String username, String pwd, String email, String phone, int post,
			String addr, String detailAddr, int gender, String birthday, String introduction, int memberStatus,
			int warnCount, String regDate) {
		super();
		this.userid = userid;
		this.profileimg = profileimg;
		this.username = username;
		this.pwd = pwd;
		this.email = email;
		this.phone = phone;
		this.post = post;
		this.addr = addr;
		this.detailAddr = detailAddr;
		this.gender = gender;
		this.birthday = birthday;
		this.introduction = introduction;
		this.memberStatus = memberStatus;
		this.warnCount = warnCount;
		this.regDate = regDate;
	}
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getProfileimg() {
		return profileimg;
	}
	public void setProfileimg(String profileimg) {
		this.profileimg = profileimg;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getPost() {
		return post;
	}
	public void setPost(int post) {
		this.post = post;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getDetailAddr() {
		return detailAddr;
	}
	public void setDetailAddr(String detailAddr) {
		this.detailAddr = detailAddr;
	}
	
	public List<RoomVO> getMyroomList() {
		return myroomList;
	}
	public void setMyroomList(List<RoomVO> myroomList) {
		this.myroomList = myroomList;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public int getMemberStatus() {
		return memberStatus;
	}
	public void setMemberStatus(int memberStatus) {
		this.memberStatus = memberStatus;
	}
	public int getWarnCount() {
		return warnCount;
	}
	public void setWarnCount(int warnCount) {
		this.warnCount = warnCount;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(String lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
}

