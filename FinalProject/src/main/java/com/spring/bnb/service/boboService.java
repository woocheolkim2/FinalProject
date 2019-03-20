package com.spring.bnb.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.bnb.dao.boboDAO;
import com.spring.bnb.model.RoomVO;

@Service
public class boboService implements InterBOService{
	
	@Autowired
	private boboDAO dao;

	// 숙소유형 가져오기
	@Override
	public List<String> selectroomtype() {
		List<String> roomtype = dao.selectroomtype();
		return roomtype;
	}

	// 건물유형 가져오기
	@Override
	public List<HashMap<String, String>> selectbuildType() {
		List<HashMap<String, String>> buildType = dao.selectbuildType();
		return buildType;
	}

	// 건물세부유형 가져오기
	@Override
	public List<HashMap<String, String>> selectbuildTypedetail(String buildType) {
		List<HashMap<String, String>> buildTypedetail = dao.selectbuildTypedetail(buildType);
		return buildTypedetail;
	}

	// 옵션가져오기
	@Override
	public List<String> selectoptions() {
		List<String> options = dao.selectoptions();
		return options;
	}

	// 이용규칙 가져오기
	@Override
	public List<String> selectrule() {
		List<String> rule = dao.selectrule();
		return rule;
	}

	// 숙소 insert
	@Override
	public int becomehost(RoomVO roomvo) {
		int n = dao.becomehost(roomvo);
		return n;
	}

	//이미지 테이블 insert
	@Override
	public void imgList(RoomVO roomvo) {
		dao.imgList(roomvo);
	}

	//옵션 테이블 insert
	@Override
	public void myoption(RoomVO roomvo) {
		dao.myoption(roomvo);
		
	}

	//규칙 테이블 insert
	@Override
	public void myrule(RoomVO roomvo) {
		dao.myrule(roomvo);
		
	}

	// 침실, 침대 insert
	@Override
	public void insertbedroom(HashMap<String, String> paraMap) {
		dao.insertbedroom(paraMap);
		
	}

	


}
