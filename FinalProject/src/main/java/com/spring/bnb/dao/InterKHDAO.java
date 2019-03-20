package com.spring.bnb.dao;

import java.util.HashMap;
import java.util.List;

import com.spring.bnb.model.ReservationVO;
import com.spring.bnb.model.RoomVO;

public interface InterKHDAO {
	
	
	// *** 숙소 정보 뽑아오는 메소드 *** //
	RoomVO getOneRoomInfo(HashMap<String,Object> map);
	
	// *** 리뷰 갯수 가져오기 *** //
	int getReviewCount(HashMap<String,Object> map);
	
	// *** 평균 요금 구하는 메소드 *** //
	int getAvgPrice(); 
	
	// *** 예약 시퀀스 채번해오기 *** //
	int getReservCode();
	
	// *** 숙소 예약하는 메소드 *** //
	int insertReservation(HashMap<String,Object> map);
	
	// *** 예약자 정보 가져오기 *** //
	ReservationVO getOneReserve(HashMap<String,Object> map);

	// *** 나의 쿠폰 리스트 보기 *** //
	List<HashMap<String, Object>> getmyCoupon(String userid);
	
	// ** 쿠폰 사용하는 메소드 ** //
	int useMyCoupon(HashMap<String,String> cpmap);
	
	// ** 쿠폰 사용취소하는 메소드 ** //
	int NouseMyCoupon(HashMap<String,String> cpmap);

    // ** 쿠폰사용시 할인 금액 가져오기 ** //
	int getUseMyCopon(HashMap<String, String> map);
}
