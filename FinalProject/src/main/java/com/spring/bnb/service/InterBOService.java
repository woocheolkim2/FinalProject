package com.spring.bnb.service;

import java.util.HashMap;
import java.util.List;

import com.spring.bnb.model.RoomVO;

public interface InterBOService {
	
	// 숙소유형 가져오기
	List<String> selectroomtype();
	
	// 건물유형 가져오기
	List<HashMap<String, String>> selectbuildType();
	
	// 건물세부유형 가져오기
	List<HashMap<String,String>> selectbuildTypedetail(String buildType);
	
	// 옵션 가져오기
	List<String> selectoptions();
	
	// 이용규칙 가져오기
	List<String> selectrule();
	
	// 숙소 insert
	int becomehost(RoomVO roomvo);
	
	// 이미지테이블 insert
	void imgList(RoomVO roomvo);
	
	// 옵션 insert
	void myoption(RoomVO roomvo);
	
	// 규칙 insert
	void myrule(RoomVO roomvo);
	
	//침실, 침대 insert
	void insertbedroom(HashMap<String, String> paraMap);

}
