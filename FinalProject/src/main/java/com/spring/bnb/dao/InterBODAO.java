package com.spring.bnb.dao;

import java.util.HashMap;
import java.util.List;

import com.spring.bnb.model.RoomVO;

public interface InterBODAO {
	
	//숙소유형가져오기
	List<String> selectroomtype();
	
	//건물유형가져오기
	List<HashMap<String, String>> selectbuildType();
	
	//건물세부유형가져오기
	List<HashMap<String, String>> selectbuildTypedetail(String buildType);
	
	//옵션가져오기
	List<String> selectoptions();
	
	//이용규칙가져오기
	List<String> selectrule();
	
	//숙소 insert
	int becomehost(RoomVO roomvo);
	
	//이미지테이블 insert
	void imgList(RoomVO roomvo);
	
	//옵션테이블 insert
	void myoption(RoomVO roomvo);
	
	//규칙테이블 insert
	void myrule(RoomVO roomvo);
	
	//침실,침대 insert
	void insertbedroom(HashMap<String, String> paraMap);
}
