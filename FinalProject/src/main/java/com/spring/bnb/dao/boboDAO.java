package com.spring.bnb.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.bnb.model.RoomVO;

@Repository
public class boboDAO implements InterBODAO{
	
	@Autowired
	private SqlSessionTemplate sqlsession;

	// 숙소유형 가져오기
	@Override
	public List<String> selectroomtype() {	
		List<String> roomtype = sqlsession.selectList("bobo.selectroomtype");
		return roomtype;
	}

	// 건물유형 가져오기
	@Override
	public List<HashMap<String, String>> selectbuildType() {
		List<HashMap<String, String>> buildType = sqlsession.selectList("bobo.selectbuildType");
		return buildType;
	}

	// 건물세부유형 가져오기
	@Override
	public List<HashMap<String, String>> selectbuildTypedetail(String buildType) {
		List<HashMap<String, String>> buildTypedetail = sqlsession.selectList("bobo.selectbuildTypedetail", buildType);
		return buildTypedetail;
	}

	// 옵션 가져오기
	@Override
	public List<String> selectoptions() {
		List<String> options = sqlsession.selectList("bobo.selectoptions");
		return options;
	}

	// 이용규칙 가져오기
	@Override
	public List<String> selectrule() {
		List<String> rule = sqlsession.selectList("bobo.selectrule");
		return rule;
	}

	// 숙소 insert
	@Override
	public int becomehost(RoomVO roomvo) {
		int n = sqlsession.insert("bobo.becomehost", roomvo);
		return n;
	}

	// 이미지 테이블 insert
	@Override
	public void imgList(RoomVO roomvo) {
		// 숙소 시퀀스 채번
		String roomseq = sqlsession.selectOne("bobo.roomseq");
		
		HashMap<String,String> paraMap = new HashMap<String,String>();	
		paraMap.put("roomseq", roomseq);
		paraMap.put("buildType_detail_idx",roomvo.getFk_buildType_detail_idx());
		paraMap.put("roomType_idx",roomvo.getFk_roomType_idx());		
		for(String str : roomvo.getRoomimgList()) {
			paraMap.put("img", str); 
			sqlsession.insert("bobo.insertRoomImgList",paraMap);
		}
	}

	// 옵션 테이블 insert
	@Override
	public void myoption(RoomVO roomvo) {
		// 숙소 시퀀스 채번
		String roomseq = sqlsession.selectOne("bobo.roomseq");
		
		HashMap<String,String> paraMap = new HashMap<String,String>();	
		paraMap.put("roomseq", roomseq);
		paraMap.put("buildType_detail_idx",roomvo.getFk_buildType_detail_idx());
		paraMap.put("roomType_idx",roomvo.getFk_roomType_idx());		
		for(String str : roomvo.getMyoption()) {
			paraMap.put("option", str); 
			sqlsession.insert("bobo.optioninsert",paraMap);
		}

	}

	// 규칙 테이블 insert
	@Override
	public void myrule(RoomVO roomvo) {
		// 숙소 시퀀스 채번
		String roomseq = sqlsession.selectOne("bobo.roomseq");
		
		HashMap<String,String> paraMap = new HashMap<String,String>();	
		paraMap.put("roomseq", roomseq);
		paraMap.put("buildType_detail_idx",roomvo.getFk_buildType_detail_idx());
		paraMap.put("roomType_idx",roomvo.getFk_roomType_idx());		
		for(String str : roomvo.getMyrule()) {
			paraMap.put("rule", str); 
			sqlsession.insert("bobo.ruleinsert",paraMap);
		}
		
	}

	// 침실, 침대 insert
	@Override
	public void insertbedroom(HashMap<String, String> paraMap) {
		// 숙소 시퀀스 채번
		String roomseq = sqlsession.selectOne("bobo.roomseq");
		paraMap.put("roomseq", roomseq);
		
		// 침실 insert
		sqlsession.insert("bobo.insertbedroom", paraMap);
		
		// 침실 시퀀스 채번
		int bedroom_idx = sqlsession.selectOne("bobo.getBedroomIdx");
		
		Set<String> keys = paraMap.keySet();
		for(String key: keys) {
			HashMap<String,String> newhash = new HashMap<String,String>();
			newhash.put("BEDROOMIDX", String.valueOf(bedroom_idx));
			if(key.equals("queenbedCount")) {
				newhash.put("BEDOBJIDX", "1");
				newhash.put("BEDCOUNT", paraMap.get("queenbedCount").toString());
				sqlsession.insert("bobo.insertbed", newhash);
			}
			else if(key.equals("doublebedCount")) {
				newhash.put("BEDOBJIDX", "2");
				newhash.put("BEDCOUNT", paraMap.get("doublebedCount").toString());
				sqlsession.insert("bobo.insertbed", newhash);
			}
			else if(key.equals("singlebedCount")) {
				newhash.put("BEDOBJIDX", "3");
				newhash.put("BEDCOUNT", paraMap.get("singlebedCount").toString());
				sqlsession.insert("bobo.insertbed", newhash);
			}
			else if(key.equals("sofabedCount")) {
				newhash.put("BEDOBJIDX", "4");
				newhash.put("BEDCOUNT", paraMap.get("sofabedCount").toString());
				sqlsession.insert("bobo.insertbed", newhash);
			}
		}
		
	}
	
	

}
