package com.spring.bnb.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.bnb.model.MemberVO;
import com.spring.bnb.model.ReservationVO;
import com.spring.bnb.model.RoomVO;

@Repository
public class KHDAO implements InterKHDAO {

	@Autowired
	private SqlSessionTemplate sqlsession;
    //======================================================================
	// *** 숙소 정보 뽑아오는 메소드 *** //
	@Override
	public RoomVO getOneRoomInfo(HashMap<String,Object> map) {
		// *** 숙소 정보 가져오기 *** //
		RoomVO oneRoom = sqlsession.selectOne("kh.getOneRoomInfo",map);// 숙소 정보 가져오기
		
		// *** 호스트 정보 가져오기 *** //
		MemberVO host = sqlsession.selectOne("kh.getOneHost",oneRoom.getFk_userid());// 호스트 정보 가져오기
		oneRoom.setHost(host);
		
		// *** 룸 규칙 *** //
		List<HashMap<String,String>> ruleList = sqlsession.selectList("kh.getruleList",map);
		oneRoom.setRuleList(ruleList);
		
		// *** 룸 옵션 *** //
		List<HashMap<String,String>> optionList = sqlsession.selectList("kh.roomOptionList", map);
		oneRoom.setOptionList(optionList);
		
		return oneRoom;
	}
	
	// *** 리뷰 갯수 가져오기 *** //
	@Override
	public int getReviewCount(HashMap<String, Object> map) {
		int count = sqlsession.selectOne("kh.getReviewCount", map);
		return count;
	}
	
	// *** 평균 요금 구하는 메소드 *** //
	@Override
	public int getAvgPrice() {
		int avg = sqlsession.selectOne("kh.getAvgPrice");
		return avg;
	}
	
	// *** 예약 시퀀스 채번해오기 *** //
	@Override
	public int getReservCode() {
		int avg = sqlsession.selectOne("kh.getReservCode");
		return avg;
	}
	
	// *** 숙소 예약하는 메소드 *** //
	@Override
	public int insertReservation(HashMap<String, Object> map) {
		int reservation = sqlsession.insert("kh.insertReservation",map);
		return reservation;
	}
	
	// *** 예약자 정보 가져오기 *** //
	@Override
	public ReservationVO getOneReserve(HashMap<String, Object> map) {
		ReservationVO rvo = sqlsession.selectOne("kh.getOneReserve", map);
		return rvo;
	}

	// *** 나의 쿠폰 리스트 보기 *** //
	@Override
	public List<HashMap<String, Object>> getmyCoupon(String userid) {
		List<HashMap<String, Object>> mycoupon = sqlsession.selectList("kh.getmyCoupon", userid);
		return mycoupon;
	}
	
	// ** 쿠폰 사용하는 메소드 ** //
	@Override
	public int useMyCoupon(HashMap<String, String> cpmap) {
		int n = sqlsession.update("kh.useMyCoupon", cpmap);
		return n;
	}

	@Override
	public int getUseMyCopon(HashMap<String, String> map) {
		int disMoney = sqlsession.selectOne("kh.getUseMyCopon",map);
		return disMoney;
	}

	@Override
	public int NouseMyCoupon(HashMap<String, String> cpmap) {
		int n = sqlsession.update("kh.NouseMyCoupon", cpmap);
		return n;
	}
	
}
