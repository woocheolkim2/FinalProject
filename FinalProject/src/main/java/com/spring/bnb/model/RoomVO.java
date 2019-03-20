package com.spring.bnb.model;

import java.util.HashMap;
import java.util.List;

public class RoomVO {
	private String roomcode;
	private String fk_userid; 
	private String fk_buildType_detail_idx;
	private String fk_roomoption_idx;
	private String fk_roomType_idx;
	private String roomName; 
	private String roomTel;
	private String roomInfo; 
	private String roomPost;
	private String roomSigungu; 
	private String roomSido;
	private String roomBname;
	private String roomDetailAddr;
	private String roomPrice;
	private String peakper;
	private String cleanPay; 
	private String basic_person;
	private String max_person;
	private String person_addpay;
	private String roomCount; 
	private String bathCount;
	private String checkInTime; 
	private String checkOutTime; 
	private String latitude; 
	private String longitude;  
	private String viewcount; 
	private String roomstatus;  
	private String room_warnCount;
	private String bedCount;	
	private String bedtype;	
	private String buildtype_idx;
	private String roomMainImg;		  // 진짜 파일명(강아지.png). 사용자가 파일을 업로드 하거나 파일을 다운로드 할때 사용되어지는 파일명
	private String roomImgfilename;   // WAS(톰캣)에 저장될 파일명(20161121324325454354353333432.png)
	
	private List<String> myoption; // 보유옵션
	private List<String> myrule;   //보유규칙
	
	//add(getter,setter 아직 안넣음)
	private String roomType_name;
	private String buildType;
	private String buildType_detail_name;
	private int likeCount;
	private List<String> roomimgList ;
	private List<ReviewVO> reviewList;
	private List<ReservationVO> reservationList;
	private List<HashMap<String,String>> optionList;
	private List<HashMap<String,String>> ruleList;
	private List<HashMap<String,String>> bedroomList;
	private MemberVO host;
	public RoomVO() {}
	
	public String getRoomcode() {
		return roomcode;
	}
	public void setRoomcode(String roomcode) {
		this.roomcode = roomcode;
	}
	public String getFk_userid() {
		return fk_userid;
	}
	public void setFk_userid(String fk_userid) {
		this.fk_userid = fk_userid;
	}
	
	public String getFk_buildType_detail_idx() {
		return fk_buildType_detail_idx;
	}

	public void setFk_buildType_detail_idx(String fk_buildType_detail_idx) {
		this.fk_buildType_detail_idx = fk_buildType_detail_idx;
	}

	public String getFk_roomoption_idx() {
		return fk_roomoption_idx;
	}
	public void setFk_roomoption_idx(String fk_roomoption_idx) {
		this.fk_roomoption_idx = fk_roomoption_idx;
	}
	public String getFk_roomType_idx() {
		return fk_roomType_idx;
	}
	public void setFk_roomType_idx(String fk_roomType_idx) {
		this.fk_roomType_idx = fk_roomType_idx;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
	///////////////////////////////////////////////////////////
	
	public String getRoomMainImg() {
		return roomMainImg;
	}
	public void setRoomMainImg(String roomMainImg) {
		this.roomMainImg = roomMainImg;
	}
	
	public String getRoomImgfilename() {
		return roomImgfilename;
	}

	public void setRoomImgfilename(String roomImgfilename) {
		this.roomImgfilename = roomImgfilename;
	}
	
	public List<String> getMyoption() {
		return myoption;
	}

	public void setMyoption(List<String> myoption) {
		this.myoption = myoption;
	}

	public List<String> getMyrule() {
		return myrule;
	}

	public void setMyrule(List<String> myrule) {
		this.myrule = myrule;
	}
	///////////////////////////////////////////////////////////

	public String getRoomTel() {
		return roomTel;
	}
	public void setRoomTel(String roomTel) {
		this.roomTel = roomTel;
	}
	public String getRoomInfo() {
		return roomInfo;
	}	
	
	public void setRoomInfo(String roomInfo) {
		this.roomInfo = roomInfo;
	}
	public String getRoomPost() {
		return roomPost;
	}
	public void setRoomPost(String roomPost) {
		this.roomPost = roomPost;
	}
	public String getRoomSigungu() {
		return roomSigungu;
	}
	public void setRoomSigungu(String roomSigungu) {
		this.roomSigungu = roomSigungu;
	}
	public String getRoomSido() {
		return roomSido;
	}
	public void setRoomSido(String roomSido) {
		this.roomSido = roomSido;
	}
	public String getRoomBname() {
		return roomBname;
	}
	public void setRoomBname(String roomBname) {
		this.roomBname = roomBname;
	}
	
	public String getRoomDetailAddr() {
		return roomDetailAddr;
	}

	public void setRoomDetailAddr(String roomDetailAddr) {
		this.roomDetailAddr = roomDetailAddr;
	}

	public String getRoomPrice() {
		return roomPrice;
	}
	public void setRoomPrice(String roomPrice) {
		this.roomPrice = roomPrice;
	}
	public String getPeakper() {
		return peakper;
	}
	public void setPeakper(String peakper) {
		this.peakper = peakper;
	}
	public String getCleanPay() {
		return cleanPay;
	}
	public void setCleanPay(String cleanPay) {
		this.cleanPay = cleanPay;
	}
	public String getBasic_person() {
		return basic_person;
	}
	public void setBasic_person(String basic_person) {
		this.basic_person = basic_person;
	}
	public String getMax_person() {
		return max_person;
	}
	public void setMax_person(String max_person) {
		this.max_person = max_person;
	}
	public String getPerson_addpay() {
		return person_addpay;
	}
	public void setPerson_addpay(String person_addpay) {
		this.person_addpay = person_addpay;
	}
	public String getRoomCount() {
		return roomCount;
	}
	public void setRoomCount(String roomCount) {
		this.roomCount = roomCount;
	}
	public String getBathCount() {
		return bathCount;
	}
	public void setBathCount(String bathCount) {
		this.bathCount = bathCount;
	}
	public String getCheckInTime() {
		return checkInTime;
	}
	public void setCheckInTime(String checkInTime) {
		this.checkInTime = checkInTime;
	}
	public String getCheckOutTime() {
		return checkOutTime;
	}
	public void setCheckOutTime(String checkOutTime) {
		this.checkOutTime = checkOutTime;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getViewcount() {
		return viewcount;
	}
	public void setViewcount(String viewcount) {
		this.viewcount = viewcount;
	}
	public String getRoomstatus() {
		return roomstatus;
	}
	public void setRoomstatus(String roomstatus) {
		this.roomstatus = roomstatus;
	}
	public String getRoom_warnCount() {
		return room_warnCount;
	}
	public void setRoom_warnCount(String room_warnCount) {
		this.room_warnCount = room_warnCount;
	}
	public String getBedCount() {
		return bedCount;
	}
	public void setBedCount(String bedCount) {
		this.bedCount = bedCount;
	}
	public String getBedtype() {
		return bedtype;
	}
	public void setBedtype(String bedtype) {
		this.bedtype = bedtype;
	}	
	public String getRuleType_name() {
		return roomType_name;
	}
	public void setRuleType_name(String roomType_name) {
		this.roomType_name = roomType_name;
	}	
	
	// 추가
	public String getRoomType_name() {
		return roomType_name;
	}
	public void setRoomType_name(String roomType_name) {
		this.roomType_name = roomType_name;
	}
	public String getBuildType() {
		return buildType;
	}
	public void setBuildType(String buildType) {
		this.buildType = buildType;
	}
	public String getBuildType_detail_name() {
		return buildType_detail_name;
	}
	public void setBuildType_detail_name(String buildType_detail_name) {
		this.buildType_detail_name = buildType_detail_name;
	}
	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public List<String> getRoomimgList() {
		return roomimgList;
	}
	public void setRoomimgList(List<String> roomimgList) {
		this.roomimgList = roomimgList;
	}
	public List<HashMap<String,String>> getOptionList() {
		return optionList;
	}
	public void setOptionList(List<HashMap<String,String>> optionList) {
		this.optionList = optionList;
	}	
	public List<HashMap<String, String>> getRuleList() {
		return ruleList;
	}
	public void setRuleList(List<HashMap<String, String>> ruleList) {
		this.ruleList = ruleList;
	}

	public List<ReviewVO> getReviewList() {
		return reviewList;
	}
	public void setReviewList(List<ReviewVO> reviewList) {
		this.reviewList = reviewList;
	}

	public MemberVO getHost() {
		return host;
	}

	public void setHost(MemberVO host) {
		this.host = host;
	}

	public List<HashMap<String, String>> getBedroomList() {
		return bedroomList;
	}

	public void setBedroomList(List<HashMap<String, String>> bedroomList) {
		this.bedroomList = bedroomList;
	}

	public List<ReservationVO> getReservationList() {
		return reservationList;
	}

	public void setReservationList(List<ReservationVO> reservationList) {
		this.reservationList = reservationList;
	}
	
	public String getBuildtype_idx() {
		return buildtype_idx;
	}

	public void setBuildtype_idx(String buildtype_idx) {
		this.buildtype_idx = buildtype_idx;
	}

}