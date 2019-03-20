package com.spring.bnb.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.bnb.model.MemberVO;
import com.spring.bnb.model.ReservationVO;
import com.spring.bnb.model.ReviewVO;
import com.spring.bnb.model.RoomVO;


@Repository
public class HYDAO implements InterHYDAO {
	
	@Autowired
	private SqlSessionTemplate sqlsession; // SqlSessionTemplate --> root-context.xml  #15.

	@Override
	public RoomVO getRoomByCode(String roomcode) {		
		
		// 숙소정보 가져오기
		RoomVO roomvo = sqlsession.selectOne("hy.getRoomByCode", roomcode);
		// 호스트정보가져오기
		MemberVO host = sqlsession.selectOne("hy.getHost", roomvo.getFk_userid());
		roomvo.setHost(host);
		// 리뷰가져오기
		List<ReviewVO> reviewList = sqlsession.selectList("hy.getReviewListDAO", roomcode);
		roomvo.setReviewList(reviewList);
		// 침실정보 가져오기
		List<HashMap<String,String>> bedroomList = sqlsession.selectList("hy.getBedroomList", roomcode);
		roomvo.setBedroomList(bedroomList);
		// 옵션정보 가져오기
		List<HashMap<String,String>> optionList = sqlsession.selectList("hy.getOptionList", roomcode);
		roomvo.setOptionList(optionList);
		// 관심 수 가져오기
		int likeCount = sqlsession.selectOne("hy.getRoomLikeCount", roomcode);
		roomvo.setLikeCount(likeCount);
		// 이용규칙 리스트 가져오기
		List<HashMap<String,String>> ruleList = sqlsession.selectList("hy.getRuleList", roomcode);
		roomvo.setRuleList(ruleList);
		// 숙소 이미지 리스트 가져오기
		/*List<String> roomimgList = sqlsession.selectList("hy.getRoomImgList", roomcode);
		roomvo.setRoomimgList(roomimgList);*/
		
		return roomvo;
	}
	
	// 로그인
	@Override
	public MemberVO logincheck(MemberVO member) {
		MemberVO loginuser = sqlsession.selectOne("hy.logincheck",member); 
		if(loginuser!=null) {
			//System.out.println("check : "+loginuser.getUserid());
			List<RoomVO> myroom = sqlsession.selectList("hy.checkHostUser",member);
			if(myroom != null) {
				loginuser.setMyroomList(myroom);
			}
		}
		return loginuser;
	}

	@Override
	public int insertLikeRoom(HashMap<String, Object> paraMap) {
		int result = 0;
		int n = sqlsession.selectOne("hy.checkLikeRoom", paraMap);
		if(n==0) result = sqlsession.insert("hy.insertLikeRoom", paraMap);
		System.out.println("n : "+n+"result : "+result);
		return result;
	}

	@Override
	public List<HashMap<String, Object>> getMyLikeRoomList(String userid) {
		List<HashMap<String, Object>> resultMap = sqlsession.selectList("hy.getMyLikeRoomList",userid);
		return resultMap;
	}

	@Override
	public List<ReviewVO> getSearchReview(HashMap<String, String> paraMap) {
		List<ReviewVO> reviewList = sqlsession.selectList("hy.getSearchReview", paraMap);
		for(ReviewVO review : reviewList) {
			MemberVO reviewer = sqlsession.selectOne("hy.getReviewer", review.getFk_userid());
			review.setUser(reviewer);
		}
		return reviewList;
	}

	@Override
	public int checkDuplicateID(String userid) {
		int n = sqlsession.selectOne("hy.checkDuplicateID", userid);
		return n;
	}

	@Override
	public int insertMember(MemberVO member) {
		if(member.getIntroduction()==null) {
			member.setIntroduction("");
		}
		int n = sqlsession.insert("hy.insertMember", member);
		return n;
	}

	@Override
	public List<RoomVO> getRecommendRoomList(String sigungu) {
		List<RoomVO> recommendRoomList = sqlsession.selectList("hy.getRecommendRoomList", sigungu);
		return recommendRoomList;
	}
	
	@Override
	public int insertBedroomInfo(HashMap<String,Object> paraMap) {
		sqlsession.insert("hy.insertbedroom", paraMap.get("roomcode"));
		int bedroom_idx = sqlsession.selectOne("hy.getBedroomIdx");
		
		Set<String> keys = paraMap.keySet();
		for(String key: keys) {
			HashMap<String,String> newhash = new HashMap<String,String>();
			newhash.put("BEDROOMIDX", String.valueOf(bedroom_idx));
			if(key.equals("queenbedCount")) {
				newhash.put("BEDOBJIDX", "1");
				newhash.put("BEDCOUNT", paraMap.get("queenbedCount").toString());
				sqlsession.insert("hy.insertbed", newhash);
			}
			else if(key.equals("doublebedCount")) {
				newhash.put("BEDOBJIDX", "2");
				newhash.put("BEDCOUNT", paraMap.get("doublebedCount").toString());
				sqlsession.insert("hy.insertbed", newhash);
			}
			else if(key.equals("singlebedCount")) {
				newhash.put("BEDOBJIDX", "3");
				newhash.put("BEDCOUNT", paraMap.get("singlebedCount").toString());
				sqlsession.insert("hy.insertbed", newhash);
			}
			else if(key.equals("sofabedCount")) {
				newhash.put("BEDOBJIDX", "4");
				newhash.put("BEDCOUNT", paraMap.get("sofabedCount").toString());
				sqlsession.insert("hy.insertbed", newhash);
			}
		}
		return 0;
	}

	@Override
	public int insertReview(ReviewVO review) {
		int n = sqlsession.insert("hy.insertReview", review);
		return n;
	}

	@Override
	public List<ReservationVO> reservationCheck(String roomcode) {
		List<ReservationVO> rsvList = sqlsession.selectList("hy.reservationCheck", roomcode);
		return rsvList;
	}

	@Override
	public HashMap<String, Object> getStarPoint(String roomcode) {
		HashMap<String, Object> starPoint = sqlsession.selectOne("hy.getStarPoint",roomcode);
		return starPoint;
	}

	@Override
	public void roomViewCountUp(String roomcode) {
		sqlsession.update("hy.roomViewCountUp",roomcode);
	}

	@Override
	public List<String> getSearchSido(String searchword) {
		List<String> searchList = sqlsession.selectList("hy.getSearchSido", searchword);
		return searchList;
	}

	@Override
	public List<HashMap<String, Object>> getHostIncome(String userid) {
		List<HashMap<String,Object>> incomeList = sqlsession.selectList("hy.getHostIncome", userid);
		return incomeList;
	}

	@Override
	public int MemberCheckByIdAndEmail(HashMap<String, String> paraMap) {
		int n = sqlsession.selectOne("hy.MemberCheckByIdAndEmail",paraMap);
		return n;
	}
}
