package com.spring.bnb.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.bnb.dao.InterSCDAO;
import com.spring.bnb.model.ReviewVO;
import com.spring.bnb.model.RoomVO;

@Service
public class SCService implements InterSCService {
	
	@Autowired
	private InterSCDAO dao;
	
	// 해당 유저의 숙소리스트 가져오기 
	@Override
	public List<RoomVO> getRoomList(String userid) {
		List<RoomVO> roomList = dao.getRoomList(userid);
		return roomList;
	}
	// 룸정보 가져오기
	@Override
	public RoomVO getRoomInfo(String roomcode) {
		RoomVO roomvo = dao.getRoomInfo(roomcode);
		return roomvo;
	}
	// 룸이미지 추가하기 
	@Override
	public int setRoomImg(HashMap<String, String> paraMap) {
		int n = dao.setRoomImg(paraMap);
		return n;
	}
	//룸이미지 삭제하기
	@Override
	public int deleteFile(String deleteFilename) {
		int n = dao.deleteFile(deleteFilename);
		return n;
	}
	
	//룸메인이미지 업데이트
	@Override
	public int updateCoverImg(HashMap<String, String> paraMap) {
		int n = dao.updateCoverImg(paraMap);
		return n;
	}
	
	//숙소이름 검색하기
	@Override
	public List<RoomVO> roomnameSearch(HashMap<String, String> paraMap) {
		List<RoomVO> roomList = dao.roomnameSearch(paraMap);
		return roomList;
	}
	
	@Override
	public List<HashMap<String, String>> getPoint(String roomcode) {
		List<HashMap<String, String>> countList = dao.getPoint(roomcode);
		return countList;
	}
	
	@Override
	public List<ReviewVO> getReview(String roomcode) {
		List<ReviewVO> reviewList = dao.getReview(roomcode);
		return reviewList;
	}
	@Override
	public int allReservation(HashMap<String, String> paraMap) {
		int sumReservation = dao.allReservation(paraMap);
		return sumReservation;
	}
	@Override
	public int monthReservation(HashMap<String, String> paraMap) {
		int sumReservation = dao.monthReservation(paraMap);
		return sumReservation;
	}
	@Override
	public HashMap<String, String> getViewAndReservationCount(String roomcode) {
		HashMap<String,String> countMap = dao.getViewAndReservationCount(roomcode);
		return countMap;
	}
	@Override
	public int changeRoomtitle(HashMap<String, String> paraMape) {
		int result = dao.changeRoomtitle(paraMape);
		return result;
	}
	@Override
	public List<HashMap<String, String>> selectbuildType() {
		List<HashMap<String, String>> buildTypeList = dao.selectbuildType();
		return buildTypeList;
	}
	@Override
	public List<String> selectroomtype() {
		List<String> roomtype = dao.selectroomtype();
		return roomtype;
	}
	@Override
	public List<HashMap<String, String>> selectbuildTypedetail(String buildType) {
		List<HashMap<String, String>> buildtypedetailList = dao.selectbuildTypedetail(buildType);
		return buildtypedetailList;
	}
	@Override
	public int roomUpdate(HashMap<String, String> paraMap) {
		int n = dao.roomUpdate(paraMap);
		return n;
	}
	@Override
	public List<String> getOptionList() {
		List<String> optionList = dao.getOptionList();
		return optionList;
	}
	@Override
	public List<String> getRuleList() {
		List<String> ruleList = dao.getRuleList();
		return ruleList;
	}
	@Override
	public int deleteOptionAndRule(String roomcode) {
		int n = dao.deleteOptionAndRule(roomcode);
		return n;
	}
	@Override
	public void insertOption(HashMap<String, String> paraMap) {
		dao.insertOption(paraMap);
		
	}
	@Override
	public void insertRule(HashMap<String, String> paraMap) {
		dao.insertRule(paraMap);
		
	}
}
