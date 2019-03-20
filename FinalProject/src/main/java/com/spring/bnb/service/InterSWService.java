package com.spring.bnb.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.spring.bnb.model.ReservationVO;
import com.spring.bnb.model.RoomVO;

@Service
public interface InterSWService {

	List<String> getBuildList();  // 숙소유형(대)

	List<String> getOptionList();  // 옵션종류

	List<String> getRoomType();  // 건물유형

	List<String> getRoomRule();  // 이용규칙

	List<String> getBuildDetailList(String buildName1);  // 숙소유형(소)	

	List<RoomVO> getRoomList(HashMap<String,String> paraMap);  // 숙소리스트 보기

	List<ReservationVO> getReservationList(String userid);  // 예약자 현황보여주기

	List<RoomVO> getSWOptionList(HashMap<String,Object> paraMap);

	List<RoomVO> getHomeListByOption(HashMap<String, String> paraMap);  // 옵션에 따른 숙소리스트

	List<RoomVO> getAllHomeList();  // 검색조건 초기화시 예약되어있는 숙소를 제외한 모든 숙소리스트

	
	
}
