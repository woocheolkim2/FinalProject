package com.spring.bnb.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.bnb.dao.InterWCDAO;
import com.spring.bnb.model.ReviewVO;
import com.spring.bnb.model.RoomVO;

@Service
public class WCService implements InterWCService {

	@Autowired
	private InterWCDAO dao;

	@Override
	public List<RoomVO> getRecommandRoomList() { 
		return dao.getRecommandRoomList();
	}

	@Override
	public List<ReviewVO> getBestReviewList() { 
		return dao.getBestReviewList();
	}

	@Override
	public List<RoomVO> getAllRoomList() {
		
		return dao.getAllRoomList();
	}

	@Override
	public int getLodgingTotalCountWithSearch(HashMap<String, String> paraMap) {
		
		return dao.getLodgingTotalCountWithSearch(paraMap);
	}

	@Override
	public int getLodgingTotalCountNoSearch() {
		
		return dao.getLodgingTotalCountNoSearch();
	}

	@Override
	public List<RoomVO> lodgingListPaging(HashMap<String, String> paraMap) {
		
		return dao.lodgingListPaging(paraMap);
	}

	@Override
	public int deleteRoomByRoomcode(String roomcode) {
		
		return dao.deleteRoomByRoomcode(roomcode);
	}

	
	@Override
	public int continueRoomByRoomcode(String roomcode) {		
		return dao.continueRoomByRoomcode(roomcode);
	}

	@Override
	public int getLodgingTotalCountWithSearchBeforePermission(HashMap<String, String> paraMap) {
		return dao.getLodgingTotalCountWithSearchBeforePermission(paraMap);
	}

	@Override
	public List<RoomVO> toPermitLodgingList() {
		
		return dao.toPermitLodgingList();
	}

	
	
}
