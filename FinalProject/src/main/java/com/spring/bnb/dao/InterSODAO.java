package com.spring.bnb.dao;

import java.util.HashMap;
import java.util.List;

import com.spring.bnb.model.MemberVO;
import com.spring.bnb.model.ReservationVO;
import com.spring.bnb.model.ReviewVO;

public interface InterSODAO {

	// 나의 쿠폰 리스트 가져오기
	List<HashMap<String, String>> getMyCoupon(HashMap<String,String> paraMap);

	//나의 정보 가져오기
	MemberVO getMyInfo(String loginuser);
	
	//나의 정보 수정하기
	void memberUpdate(MemberVO member);

	//나의 예약 내역 가져오기
	List<HashMap<String,String>> getMemberReservationList(String loginuser);

	//나의 예약 상세 내역 가져오기
	HashMap<String, String> getMemberReservationDetail(HashMap<String,String> paraMap);
	
	// 나의 투숙 예약 취소하기
	int goCancelMyRsv(HashMap<String, String> map);
	
	// 나의 예약 숙소 위치정보 가져오기
	HashMap<String, String> getMap(HashMap<String, String> paraMap);
		
	//내가 작성한 후기
	List<ReviewVO> getMyReview(String userid);

	//나에게 작성한 후기
	List<HashMap<String,String>> getHostReview(HashMap<String,String> paraMap);
	
	// 후기없는 예약코드 가져오기
	List<HashMap<String,String>> getMyRsvCode(String userid);
	
	// 쿠폰 등록하기
	int addCoupon(HashMap<String, String> map);
	// 쿠폰 존재확인하기
	int getCoupon(String coupon);

	// *** 나의 쿠폰 리스트 페이징 처리를 위한 전체 갯수 불러오기 ***
	int getTotalCount(String userid);

	// *** 나의 사용한 쿠폰 리스트 ***
	List<HashMap<String, String>> getMyUseCoupon(HashMap<String, String> paraMap);

	// *** 사용한 쿠폰 총 갯수 ***
	int getUseTotalCount(String userid);
	// *** 나의 예약 침대 타입 가져오기 ***
	List<HashMap<String,String>> getBedType(String roomcode);

	//*** 나의 예약 빌딩 타입 ***
	HashMap<String, String> getBuildType(String roomcode);

	// *** 호스트 리뷰 갯수 가져오기  
	int getTotalHostReviewCount(String userid);
	// *** 예약 확인 메일 발송하기 ***
	List<HashMap<String, String>> getReservationList();

	// *** 회원정보 수정 전화번호 확인 *** 
	int getCheckPhone(String phone);



	
}
