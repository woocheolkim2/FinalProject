package com.spring.bnb.model;

public class ReservationVO {
	private String rsvcode;
	private String fk_roomcode;
	private String fk_userid;
	private int guestCount; 
	private int babyCount;
	private String rsv_name;
	private String rsv_phone;
	private String rsv_email;
	private String rsv_checkInDate;
	private String rsv_checkOutDate;
	private String rsv_msg;
	private String paydate;
	private int totalprice; 
	private int dcprice;
	private String rsv_cancledate;
	
	public ReservationVO() {}
	public ReservationVO(String rsvcode, String fk_roomcode, String fk_userid, int guestCount, int babyCount,
			String rsv_name, String rsv_phone, String rsv_email, String rsv_checkInDate, String rsv_checkOutDate,
			String rsv_msg, String paydate, int totalprice, int dcprice, String rsv_cancledate) {
		super();
		this.rsvcode = rsvcode;
		this.fk_roomcode = fk_roomcode;
		this.fk_userid = fk_userid;
		this.guestCount = guestCount;
		this.babyCount = babyCount;
		this.rsv_name = rsv_name;
		this.rsv_phone = rsv_phone;
		this.rsv_email = rsv_email;
		this.rsv_checkInDate = rsv_checkInDate;
		this.rsv_checkOutDate = rsv_checkOutDate;
		this.rsv_msg = rsv_msg;
		this.paydate = paydate;
		this.totalprice = totalprice;
		this.dcprice = dcprice;
		this.rsv_cancledate = rsv_cancledate;
	}
	public String getRsvcode() {
		return rsvcode;
	}
	public void setRsvcode(String rsvcode) {
		this.rsvcode = rsvcode;
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
	public int getGuestCount() {
		return guestCount;
	}
	public void setGuestCount(int guestCount) {
		this.guestCount = guestCount;
	}
	public int getBabyCount() {
		return babyCount;
	}
	public void setBabyCount(int babyCount) {
		this.babyCount = babyCount;
	}
	public String getRsv_name() {
		return rsv_name;
	}
	public void setRsv_name(String rsv_name) {
		this.rsv_name = rsv_name;
	}
	public String getRsv_phone() {
		return rsv_phone;
	}
	public void setRsv_phone(String rsv_phone) {
		this.rsv_phone = rsv_phone;
	}
	public String getRsv_email() {
		return rsv_email;
	}
	public void setRsv_email(String rsv_email) {
		this.rsv_email = rsv_email;
	}
	public String getRsv_checkInDate() {
		return rsv_checkInDate;
	}
	public void setRsv_checkInDate(String rsv_checkInDate) {
		this.rsv_checkInDate = rsv_checkInDate;
	}
	public String getRsv_checkOutDate() {
		return rsv_checkOutDate;
	}
	public void setRsv_checkOutDate(String rsv_checkOutDate) {
		this.rsv_checkOutDate = rsv_checkOutDate;
	}
	public String getRsv_msg() {
		return rsv_msg;
	}
	public void setRsv_msg(String rsv_msg) {
		this.rsv_msg = rsv_msg;
	}
	public String getPaydate() {
		return paydate;
	}
	public void setPaydate(String paydate) {
		this.paydate = paydate;
	}
	public int getTotalprice() {
		return totalprice;
	}
	public void setTotalprice(int totalprice) {
		this.totalprice = totalprice;
	}
	public int getDcprice() {
		return dcprice;
	}
	public void setDcprice(int dcprice) {
		this.dcprice = dcprice;
	}
	public String getRsv_cancledate() {
		return rsv_cancledate;
	}
	public void setRsv_cancledate(String rsv_cancledate) {
		this.rsv_cancledate = rsv_cancledate;
	}
}
