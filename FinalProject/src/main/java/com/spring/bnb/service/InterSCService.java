package com.spring.bnb.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.spring.bnb.model.ReviewVO;
import com.spring.bnb.model.RoomVO;
@Service
public interface InterSCService {

	List<RoomVO> getRoomList(String userid); // 해당 유저의 숙소 리스트 가져오기

	RoomVO getRoomInfo(String roomcode); // 룸정보 가져오기 

	int setRoomImg(HashMap<String, String> paraMap);//룸이미지 추가하기

	int deleteFile(String deleteFilename);// 룸이미지 삭제하기 

	int updateCoverImg(HashMap<String, String> paraMap); // 룸메인이미지 업데이트

	List<RoomVO> roomnameSearch(HashMap<String, String> paraMap);// 숙소이름 검색하기 

	List<HashMap<String, String>> getPoint(String roomcode);

	List<ReviewVO> getReview(String roomcode);

	int allReservation(HashMap<String, String> paraMap); // 호스트이 총 수입 

	int monthReservation(HashMap<String, String> paraMap);

	HashMap<String, String> getViewAndReservationCount(String roomcode);

	int changeRoomtitle(HashMap<String, String> paraMap);

	List<HashMap<String, String>> selectbuildType();

	List<String> selectroomtype();

	List<HashMap<String, String>> selectbuildTypedetail(String buildType);

	int roomUpdate(HashMap<String, String> paraMap);

	List<String> getOptionList();

	List<String> getRuleList();

	int deleteOptionAndRule(String roomcode);

	void insertOption(HashMap<String, String> paraMap);

	void insertRule(HashMap<String, String> paraMap);
}
