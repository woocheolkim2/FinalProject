package com.spring.bnb.service;

import java.util.HashMap;
import java.util.List;

import com.spring.bnb.model.MemberVO;
import com.spring.bnb.model.ReservationVO;
import com.spring.bnb.model.ReviewVO;

public interface InterSOService {
	
	
	// 나의 쿠폰 리스트 가져오기
	List<HashMap<String, String>> getMyCoupon(HashMap<String, String> paraMap);

	// 쿠폰 등록하기 
	int addCoupon(HashMap<String, String> map);
	
	// 나의 쿠폰 페이징 처리를 위한 총 쿠폰 갯수 구하기
	int getTotalCount(String userid);
	
	// 쿠폰 존재 확인하기 
	int getCoupon(String coupon);
	
	//나의 개인정보 가져오기
	MemberVO getMyInfo(String loginuser);
	
	//나의 정보 수정
	void memberUpdate(MemberVO member);
	
	//나의 예약 내역 가져오기
	List<HashMap<String,String>> getMemberReservationList(String loginuser);

	//나의 예약 상세 내역 가져오기
	HashMap<String, String> getMemberReservationDetail(HashMap<String,String> paraMap);
	
	// *** 예약 상세보기 침대 타입 가져오기 ***
	List<HashMap<String,String>> getBedType(String roomcode);

	// *** 투숙 예약 취소하기 ***
	int goCancelMyRsv(HashMap<String, String> map);

	//나의 예약 위치 가져오기
	HashMap<String, String> getMap(HashMap<String,String> paraMap);
	
	
	// *** 내가 작성한 후기  ***
	List<ReviewVO> getMyReview(String userid);
	
	// *** 나에게 쓴 후기 ***
	List<HashMap<String,String>> getHostReview(HashMap<String,String> userid);


	// *** 후기 없는 나의 예약코드 리스트 받아오기 ***
	List<HashMap<String,String>> getMyRsvCode(String userid);

	// *** 사용한 쿠폰 리스트 ***
	List<HashMap<String, String>> getMyUserCoupon(HashMap<String, String> paraMap);
	// *** 사용한 쿠폰 총 갯수 ***
	int getUseTotalCount(String userid);
	// *** 예약한 빌딩 유형 ***
	HashMap<String, String> getBuildType(String roomcode);
	// *** 호스트 리뷰 갯수 **
	int getTotalHostReviewCount(String userid);
	
	// *** 예약 일주일전 예약 확인 메일 발송 ***
	void scheduleTestEmailSending() throws Exception; // Spring Scheduler(스프링 스케줄러)를 사용한 email 발송하기 예제 
	
	// *** 회원정보 전화번호 확인 ***
	int getCheckPhone(String phone);

	




	

	


}
