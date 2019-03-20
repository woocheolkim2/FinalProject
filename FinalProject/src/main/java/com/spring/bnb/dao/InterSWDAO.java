package com.spring.bnb.dao;

import java.util.HashMap;
import java.util.List;

import com.spring.bnb.model.ReservationVO;
import com.spring.bnb.model.RoomVO;

public interface InterSWDAO {

	List<String> getBuildList();  // 숙소유형(대)

	List<String> getOptionList();  // 옵션종류

	List<String> getRoomType();  // 건물유형

	List<String> getRoomRule();  // 이용규칙

	List<String> getBuildDetailList(String buildName1);  // 숙소유형(소)
		
	List<RoomVO> getRoomList(HashMap<String,String> paraMap);  // 숙소 리스트보기

	List<ReservationVO> getReservationList(String userid);  // 예약자 현황 보기
	
	List<RoomVO> getSWOptionList(HashMap<String,Object> paraMap);

	List<RoomVO> getHomeListByOption(HashMap<String, String> paraMap);  // 옵션에 따른 숙소리스트 받아오기

	List<RoomVO> getAllHomeList();  // 검색조건 초기화시 예약되어있는 숙소를 제외한 모든 숙소리스트

	

	
}
