package com.spring.bnb.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Update;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.bnb.model.MemberVO;
import com.spring.bnb.model.ReservationVO;
import com.spring.bnb.model.ReviewVO;
import com.spring.bnb.model.RoomVO;


//===== #32. DAO 선언  =====
@Repository
//DB Exception Translation
public class SODAO implements InterSODAO {
	
	//===== #33. 의존 객체 주입하기 (DI:Dependency Injection) =====
	@Autowired
	private SqlSessionTemplate sqlsession;

	// 나의 쿠폰 리스트 가져오기
	@Override
	public List<HashMap<String, String>> getMyCoupon(HashMap<String,String> paraMap) {
		
		List<HashMap<String, String>> getMyCoupon = sqlsession.selectList("cso.getMyCoupon",paraMap);
		return getMyCoupon;
	}

	@Override
	public MemberVO getMyInfo(String loginuser) {
		MemberVO myInfo = sqlsession.selectOne("cso.getMyInfo", loginuser);
		return myInfo;
	}
	
	// 나의 정보수정 하기
	@Override
	public void memberUpdate(MemberVO member) {
		/*int n =*/ sqlsession.update("cso.memberUpdate",member);
		/*return n;*/
	}
	
	//나의 예약 내역 가져오기
	@Override
	public List<HashMap<String,String>> getMemberReservationList(String loginuser) {
		List<HashMap<String,String>> memberReservation = sqlsession.selectList("cso.getMemberReservationList",loginuser);
		return memberReservation;
	}

	//나의 예약 상세 내역 가져오기
	@Override
	public HashMap<String, String> getMemberReservationDetail(HashMap<String,String> paraMap) {
		 HashMap<String, String> resDetail = sqlsession.selectOne("cso.getMemberReservationDetail", paraMap);
		 
		return resDetail;
	}

	@Override
	public HashMap<String, String> getMap(HashMap<String, String> paraMap) {
		HashMap<String, String> rsvLocation = sqlsession.selectOne("cso.getMap",paraMap);
		return rsvLocation;
	}
	// 후기없는 예약코드 가져오기
	@Override
	public List<HashMap<String,String>> getMyRsvCode(String userid) {
		List<HashMap<String,String>> myRsvList = sqlsession.selectList("cso.getMyRsvCode", userid);
		return myRsvList;
	}
	// 내가 작성한 후기 
	@Override
	public List<ReviewVO>  getMyReview(String userid) {
		List<HashMap<String, Object>> myWriteReview= sqlsession.selectList("cso.getMyReview", userid);
		
		List<ReviewVO> myReviewVO = new ArrayList<ReviewVO>();
		for(HashMap<String,Object> map : myWriteReview) {
			int review_idx = (int)map.get("review_idx");
			int correct = (int)map.get("correct");
			int communicate = (int)map.get("communicate");
			int clean=(int)map.get("clean");
			int position=(int)map.get("position");
			int checkin=(int)map.get("checkin");
			int value=(int)map.get("value");
			String review_content=(String) map.get("review_content");
			String hostAnswer =(String) map.get("hostanswer");
			String review_writedate=(String) map.get("review_writedate");
			String roomName = (String) map.get("roomname");
			String roomcode = (String) map.get("roomcode");
			String fk_userid= (String) map.get("fk_userid");
			
			RoomVO room = new RoomVO();
			ReviewVO reviewvo = new ReviewVO();
			
			room.setRoomcode(roomcode);
			room.setFk_userid(fk_userid);
			room.setRoomName(roomName);
			
			reviewvo.setReview_idx(review_idx);
			reviewvo.setCorrect(correct);
			reviewvo.setCommunicate(communicate);
			reviewvo.setClean(clean);
			reviewvo.setPosition(position);
			reviewvo.setCheckin(checkin);
			reviewvo.setValue(value);
			reviewvo.setReview_content(review_content);
			reviewvo.setHostAnswer(hostAnswer);
			reviewvo.setReview_writedate(review_writedate);
			reviewvo.setRoom(room);		

			myReviewVO.add(reviewvo);
			
			
		}
		return myReviewVO;
		
	}

	// 나에게 작성한 후기
	@Override
	public List<HashMap<String,String>> getHostReview(HashMap<String,String> paraMap) {
		List<HashMap<String,String>> hostReview = sqlsession.selectList("cso.hostReview",paraMap);
		
		return hostReview;
	}

	// 쿠폰 등록하기 
	@Override
	public int addCoupon(HashMap<String, String> map) {
		int couponAdd= sqlsession.insert("cso.addCoupon",map);
		return couponAdd;
	}
	// 쿠폰 존재 확인하기
	@Override
	public int getCoupon(String coupon) {
		
		int n = sqlsession.selectOne("cso.getCoupon",coupon);
		
		return n;
	}

	// 나의 투숙 예약 취소하기
	@Override
	public int goCancelMyRsv(HashMap<String, String> map) {
		 int n = sqlsession.update("cso.goCancelMyRsv",map);

		return n;
	}

	
	// 나의 쿠폰 리스트 페이징 처리를 위한 전체 갯수 불러오기
	@Override
	public int getTotalCount(String userid) {
		int count = sqlsession.selectOne("cso.getTotalCount",userid);
		return count;
	}
	// 나의 사용한 쿠폰 리스트
	@Override
	public List<HashMap<String, String>> getMyUseCoupon(HashMap<String, String> paraMap) {
		List<HashMap<String,String>> myUseList = sqlsession.selectList("cso.getMyUseCoupon",paraMap);
		return myUseList;
	}

	// *** 사용한 쿠폰 총 갯수 ***
	@Override
	public int getUseTotalCount(String userid) {
		int count = sqlsession.selectOne("cso.getUseTotalCount", userid);
		return count;
	}

	
	// *** 나의 예약 침대 타입 가져오기 ***
	@Override
	public List<HashMap<String,String>> getBedType(String roomcode) {
		List<HashMap<String,String>> bedtype = sqlsession.selectList("cso.getBedType",roomcode); 
		return bedtype;
	}

	// *** 나의 예약 빌딩 타입 ***
	@Override
	public HashMap<String, String> getBuildType(String roomcode) {
		HashMap<String, String> buildtype = sqlsession.selectOne("cso.getBuildType", roomcode);
		return buildtype;
	}

	// *** 호스트 리뷰 갯수 가져오기 ***
	@Override
	public int getTotalHostReviewCount(String userid) {
		int count = sqlsession.selectOne("cso.getTotalHostReviewCount", userid);	
		return count;
	}

	// *** 예약 확인 메일 발송하기 ***
	@Override
	public List<HashMap<String, String>> getReservationList() {
		List<HashMap<String, String>> reservation = sqlsession.selectList("cso.getReservationList");
		return reservation;
	}


	// *** 회원정보 수정 전화번호 확인 ***
	@Override
	public int getCheckPhone(String phone) {
		int check = sqlsession.selectOne("cso.getCheckPhone",phone);
		return check;
	}






	
}
