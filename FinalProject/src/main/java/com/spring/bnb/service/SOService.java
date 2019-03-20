package com.spring.bnb.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.spring.bnb.dao.InterSODAO;
import com.spring.bnb.model.MemberVO;
import com.spring.bnb.model.ReservationVO;
import com.spring.bnb.model.ReviewVO;
import com.spring.common.AES256;
import com.spring.common.GoogleMail;

@Service
public class SOService implements InterSOService{
	@Autowired
	private InterSODAO dao;
	@Autowired
	private AES256 aes;
	@Autowired
	private GoogleMail gmail;
	
	// 나의 쿠폰 리스트 가져오기
	@Override
	public List<HashMap<String, String>> getMyCoupon(HashMap<String,String> paraMap) {
		List<HashMap<String,String>> myCouponList = dao.getMyCoupon(paraMap);
		return myCouponList;
	}
	//나의 개인정보 가져오기
	@Override
	public MemberVO getMyInfo(String loginuser) {
		MemberVO myInfo = dao.getMyInfo(loginuser);
		return myInfo;
	}
	// 나의 정보 수정
	@Override
	public void memberUpdate(MemberVO member) {
	
		/*int n = */ dao.memberUpdate(member);

		/*return n;*/
	}
	
	//나의 예약 내역 가져오기
	@Override
	public List<HashMap<String,String>> getMemberReservationList(String loginuser) {
		
		List<HashMap<String,String>> memberResList = dao.getMemberReservationList(loginuser);
		
		return memberResList;
	}
	
	//나의 예약 상세 내역 가져오기
	@Override
	public HashMap<String, String> getMemberReservationDetail(HashMap<String,String> paraMap) {
		HashMap<String, String>  resDetail = dao.getMemberReservationDetail(paraMap);
		return resDetail ;
	}
	@Override
	public HashMap<String, String> getMap(HashMap<String,String> paraMap) {
		HashMap<String, String> rsvLocation = dao.getMap(paraMap);
		return rsvLocation;
	}
	
	// 내가 작성한 후기 
	@Override
	public List<ReviewVO> getMyReview(String userid) {
		List<ReviewVO> myWriteReview  = dao.getMyReview(userid);
		
		return myWriteReview ;
	}
	
	// *** 나에게 쓴 후기 ***
	@Override
	public List<HashMap<String,String>> getHostReview(HashMap<String,String> userid) {
		List<HashMap<String,String>> hostReview = dao.getHostReview(userid); 
		return hostReview;
	}
	
	// *** 쿠폰 등록하기 ***
	@Override
	public int addCoupon(HashMap<String, String> map) {
		int couponAdd= dao.addCoupon(map);
		return couponAdd;
	}
	// *** 쿠폰 가져오기 ***
	@Override
	public int getCoupon(String coupon) {
		int n= dao.getCoupon(coupon);
		return n;
	}
	// *** 후긱없는 예약코드 가져오기 ***
	@Override
	public List<HashMap<String,String>>  getMyRsvCode(String userid) {
		List<HashMap<String,String>>  myRsvList =  dao.getMyRsvCode(userid);
		return myRsvList;
	}
	
	// *** 나의 투숙예약 취소하기 ***
	@Override
	public int goCancelMyRsv(HashMap<String, String> map) {
		int n = dao.goCancelMyRsv(map);
		return n;
	}
	
	// *** 나의 쿠폰 리스트 페이징 처리를 위한 전체 갯수 불러오기 ***
	@Override
	public int getTotalCount(String userid) {
		int count = dao.getTotalCount(userid);
		return count;
	}
	
	// *** 사용한 쿠폰 리스트  ***
	@Override
	public List<HashMap<String, String>> getMyUserCoupon(HashMap<String, String> paraMap) {
		List<HashMap<String,String>> myUseList = dao.getMyUseCoupon(paraMap);
		return myUseList;
	}
	
	// *** 사용한 쿠폰 총 갯수 ***
	@Override
	public int getUseTotalCount(String userid) {
		int count = dao.getUseTotalCount(userid);
		return count;
	}
	
	// *** 나의 예약 침대 타입 가져오기 ***
	@Override
	public List<HashMap<String,String>> getBedType(String roomcode) {
		List<HashMap<String,String>> bedtype = dao.getBedType(roomcode);
		return bedtype;
	}
	@Override
	public HashMap<String, String> getBuildType(String roomcode) {
		 HashMap<String, String> buildType = dao.getBuildType(roomcode);
		return buildType;
	}
	
	// *** 호스트 리뷰 가져오기 ***
	@Override
	public int getTotalHostReviewCount(String userid) {
		int count = dao.getTotalHostReviewCount(userid); 
		return count;
	}
	// ==== Spring Scheduler(스프링 스케줄러)를 사용한 email 발송하기 예제 ==== //
	@Scheduled(cron="0 0 12 * * *")
	@Override
	public void scheduleTestEmailSending() throws Exception {
		// <<주의>> 스케줄러로 사용되어지는 메소드는 반드시 파라미터가 없어야 한다.!!!!
		
		// === 현재시각 나타내기 === //
		Calendar currentDate = Calendar.getInstance();  // 현재날짜와 시간을 얻어온다.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("현재시각 => " + dateFormat.format(currentDate.getTime()));
		
		List<HashMap<String, String>> reservationList = dao.getReservationList();
		
		// 메일 보내기 //
	
		if(reservationList.size() > 0) {
		   	String[] rsvcodeArr = new String[reservationList.size()]; 
		   	System.out.println(aes.decrypt(reservationList.get(0).get("rsv_email")));
		   	
			for(int i=0; i<reservationList.size(); i++) {
		   		String emailContents = " 예약자명: " + reservationList.get(i).get("rsv_name")+"님의 여행 예약일은 <span style='color:red;'>"+
		   								reservationList.get(i).get("rsv_checkindate") + "</span> 입니다."+
		   								"예약 확인하기 <a href='javascript:/bnb/myReservation.air?rsvcode='"+reservationList.get(i).get("rsv_name"); 
		   								
		   	    gmail.sendmail_Reservation(aes.decrypt(reservationList.get(i).get("rsv_email")), emailContents);
		   	 System.out.println(emailContents);	
		   		rsvcodeArr[i] = reservationList.get(i).get("rsvcode");
		   	}
		   	// 메일 보낸 데이터 표시하기 //
			//HashMap<String, String[]> paraMap = new HashMap<String, String[]>();
			//paraMap.put("rsvcodeArr", rsvcodeArr);
			
		   	//dao.setMailSendCheck(paraMap);
		}


	}
	
	// *** 회원정보수정 전화번호 확인 ***
	@Override
	public int getCheckPhone(String phone) {
		int check = dao.getCheckPhone(phone);
		return check;
	}
}
