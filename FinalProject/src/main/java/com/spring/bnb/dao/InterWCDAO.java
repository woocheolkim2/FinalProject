package com.spring.bnb.dao;

import java.util.HashMap;
import java.util.List;

import com.spring.bnb.model.ReviewVO;
import com.spring.bnb.model.RoomVO;

public interface InterWCDAO {

	List<RoomVO> getRecommandRoomList();

	List<ReviewVO> getBestReviewList();

	List<RoomVO> getAllRoomList();

	int getLodgingTotalCountWithSearch(HashMap<String, String> paraMap);

	int getLodgingTotalCountNoSearch();

	List<RoomVO> lodgingListPaging(HashMap<String, String> paraMap);

	int deleteRoomByRoomcode(String roomcode);

	int continueRoomByRoomcode(String roomcode);

	int getLodgingTotalCountWithSearchBeforePermission(HashMap<String, String> paraMap);

	List<RoomVO> toPermitLodgingList();


}
 