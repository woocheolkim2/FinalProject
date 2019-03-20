package com.spring.bnb.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.bnb.model.ReservationVO;
import com.spring.bnb.model.RoomVO;


@Repository
public class SWDAO implements InterSWDAO {
	
	@Autowired
	private SqlSessionTemplate sqlsession; // SqlSessionTemplate --> root-context.xml  #15.

	@Override
	public List<String> getBuildList() {
		
		List<String> buildList = sqlsession.selectList("sw.getBuildList");
		
		return buildList;
	}

	@Override
	public List<String> getOptionList() {

		List<String> optionList = sqlsession.selectList("sw.getOptionList");
		
		return optionList;
	}

	@Override
	public List<String> getRoomType() {

		List<String> roomType = sqlsession.selectList("sw.getRoomType");
		
		return roomType;
	}

	@Override
	public List<String> getRoomRule() {
		
		List<String> roomRule = sqlsession.selectList("sw.getRoomRule");
		
		return roomRule;
	}

	@Override
	public List<String> getBuildDetailList(String buildName1) {
		
		List<String> buildDetailList = sqlsession.selectList("sw.getBuildDetailList", buildName1);
		
		return buildDetailList;
	}

	@Override
	public List<RoomVO> getRoomList(HashMap<String,String> paraMap) {

		List<RoomVO> roomList = sqlsession.selectList("sw.getRoomList", paraMap);
		/*for(RoomVO room : roomList) {
			List<HashMap<String,String>> optionList = sqlsession.selectList("sw.getRoomOptionList",room.getRoomcode());
			room.setOptionList(optionList);
			List<HashMap<String,String>> ruleList = sqlsession.selectList("sw.getRuleList",room.getRoomcode());
			room.setRuleList(ruleList);
		}*/
		return roomList;
	}

	@Override
	public List<ReservationVO> getReservationList(String userid) {

		List<ReservationVO> reservationList = sqlsession.selectList("sw.getReservationList", userid);
		
		return reservationList;
	}
	
	@Override
	public List<RoomVO> getSWOptionList(HashMap<String,Object> paraMap) {

		String rulename = (String)paraMap.get("RULENAME");
		String roomtype_name = (String)paraMap.get("ROOMTYPE_NAME");
		String optionname = (String)paraMap.get("OPTIONNAME");
				
		String[] rulenameArr = rulename.split(",");
		String[] roomtypenameArr = roomtype_name.split(",");
		String[] optionnameArr = optionname.split(",");
		
		paraMap.put("RULENAME", rulenameArr);
		paraMap.put("ROOMTYPE_NAME", roomtypenameArr);
		paraMap.put("OPTIONNAME", optionnameArr);		
		
		List<RoomVO> optionByHomeList = sqlsession.selectList("sw.getSWOptionList", paraMap);
		
		return optionByHomeList;
	}

	@Override
	public List<RoomVO> getHomeListByOption(HashMap<String, String> paraMap) {

		List<RoomVO> homeListByOption = sqlsession.selectList("sw.getHomeListByOption", paraMap);
		
		return homeListByOption;
	}

	@Override
	public List<RoomVO> getAllHomeList() {
		
		List<RoomVO> allHomeList = sqlsession.selectList("sw.getAllHomeList");
		
		return allHomeList;
	}

	
	
}
	

