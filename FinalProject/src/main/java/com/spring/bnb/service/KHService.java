package com.spring.bnb.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.bnb.dao.InterKHDAO;
import com.spring.bnb.model.ReservationVO;
import com.spring.bnb.model.RoomVO;

@Service
public class KHService implements InterKHService {
	
	@Autowired
	private InterKHDAO dao;
	
	
	// *** 숙소 정보 가져오기 *** //
	@Override
	public RoomVO getOneRoomInfo(HashMap<String,Object> map) {
		RoomVO oneRoomList = dao.getOneRoomInfo(map);
		return oneRoomList;
	}
	
	// *** 리뷰 갯수 가져오기 *** //
	@Override
	public int getReviewCount(HashMap<String, Object> map) {
		int count = dao.getReviewCount(map);
		return count;
	}
	
	// *** 평균 요금 구하는 메소드 *** //
	@Override
	public int getAvgPrice() {
		int avg = dao.getAvgPrice(); 
		return avg;
	}
	
	// *** 예약 시퀀스 채번해오기 *** //
	@Override
	public int getReservCode() {
		int seq = dao.getReservCode();
		return seq;
	}

	// *** 숙소 예약하는 메소드 *** //
	@Override
	public int insertReservation(HashMap<String, Object> map) {
		int reservation = dao.insertReservation(map);
		return reservation;
	}
	
	// *** 예약자 정보 가져오기 *** //
	@Override
	public ReservationVO getOneReserve(HashMap<String, Object> map) {
		ReservationVO rvo = dao.getOneReserve(map);
		return rvo;
	}

	// *** 나의 쿠폰보기 *** //
	@Override
	public List<HashMap<String, Object>> getmyCoupon(String userid) {
		List<HashMap<String, Object>> mycoupon = dao.getmyCoupon(userid);
		return mycoupon;
	}
	
	// *** 쿠폰 사용하는 메소드 *** //
	@Override
	public int useMyCoupon(HashMap<String, String> cpmap) {
		int n = dao.useMyCoupon(cpmap);
		return n;
	}
	
	// *** 쿠폰 사용시 금액가져오는 메소드 *** //
	@Override
	public int getUseMyCopon(HashMap<String, String> map) {
		int dismoney = dao.getUseMyCopon(map);
		return dismoney;
	}

	@Override
	public int NouseMyCoupon(HashMap<String, String> cpmap) {
		int n = dao.NouseMyCoupon(cpmap);
		return n;
	}

	
}
