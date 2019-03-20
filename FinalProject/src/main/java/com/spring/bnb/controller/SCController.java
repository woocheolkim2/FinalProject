package com.spring.bnb.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.spring.bnb.model.MemberVO;
import com.spring.bnb.model.ReviewVO;
import com.spring.bnb.model.RoomVO;
import com.spring.bnb.service.InterSCService;

@Controller
public class SCController {

	@Autowired
	private InterSCService service; 

	@RequestMapping(value = "/hostroomList.air", method = RequestMethod.GET)
	public String hostroomList(HttpServletRequest req) {
		List<RoomVO> roomList = null;
		String userid = null;
		HttpSession session = req.getSession(); 
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser"); 
		
		if(loginuser != null) { 
			userid = loginuser.getUserid(); 
		}
		roomList = service.getRoomList(userid);
		
		req.setAttribute("roomList", roomList);
		return "host/hostroomList.hosttiles";
	}

	// 호스트 등록된 숙소 수정하기
	@RequestMapping(value = "/hostRoomEdit.air", method = { RequestMethod.GET })
	public String hostRoomEdit(HttpServletRequest req) {
		List<RoomVO> roomList = null;
		String roomcode = req.getParameter("roomcode");
		//System.out.println("roomcode1 : " + roomcode);

		HttpSession session = req.getSession(); 
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser"); 
		String userid = null;
		if(loginuser != null) { 
			userid = loginuser.getUserid(); 
		}
		
		roomList = service.getRoomList(userid);
		RoomVO roomvo = (RoomVO) service.getRoomInfo(roomcode);

		req.setAttribute("roomList", roomList);
		req.setAttribute("roomvo", roomvo);

		return "hostRoomEdit/hostRoomEdit.hosttiles_nofooter";
	}
	
	// 숙소 수정페이지에서 검색 
	@RequestMapping(value = "/roomnameSearch.air", method = { RequestMethod.POST })
	public String roomnameSearch(HttpServletRequest req) {
		List<RoomVO> roomList = null;
		String searchWord = req.getParameter("searchWord");
		System.out.println("searchWord:"+searchWord);
		
		HttpSession session = req.getSession(); 
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser"); 
		String userid = null;
		if(loginuser != null) { 
			userid = loginuser.getUserid(); 
		}
		HashMap<String,String> paraMap = new HashMap<String,String>();
		paraMap.put("userid", userid);
		paraMap.put("searchWord", searchWord);
		
		roomList = service.roomnameSearch(paraMap); 
		
		JSONArray jsonArr = new JSONArray(); // [] null이 아니다.
		
		for(int i=0; i<roomList.size(); i++) {
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("roomcode", roomList.get(i).getRoomcode());
			jsonobj.put("roomName", roomList.get(i).getRoomName());
			jsonobj.put("roomstatus", roomList.get(i).getRoomstatus());
			jsonobj.put("roomMainImg", roomList.get(i).getRoomMainImg());
			System.out.println(jsonobj);
			jsonArr.put(jsonobj);
		}
		String str_json = jsonArr.toString();
		System.out.println(str_json);
		req.setAttribute("str_json", str_json);
		return "JSON";
	}

	// 호스트 숙소사진 수정
	@RequestMapping(value = "/hrPhotoEdit.air", method = { RequestMethod.GET })
	public String hrPhotoEdit(HttpServletRequest req) {

		String roomcode = req.getParameter("roomcode");
	//	System.out.println("roomcode2 : " + roomcode);
		RoomVO roomvo = (RoomVO) service.getRoomInfo(roomcode);
		req.setAttribute("roomvo", roomvo);
		return "hostRoomEdit/hrPhotoEdit.hosttiles_nofooter";
	}
	
	// 이미지파일 추가
	@RequestMapping(value = "/imgfileupload.air", method = { RequestMethod.POST })
	public String imgfileupload(MultipartHttpServletRequest req) throws IllegalStateException, IOException {
		
		String roomcode = req.getParameter("roomcode");
		//System.out.println("roomcode3 : "+roomcode);
		
		// 저장 경로 설정
		HttpSession session = req.getSession();
		String root = session.getServletContext().getRealPath("/");
		String path = root + "resources" + File.separator+ "images" +File.separator+"becomehost";		
		//System.out.println("root"+root);
		//System.out.println(path);
		File dir = new File(path);
		if (!dir.isDirectory()) {
			dir.mkdir();
		}
		
		List<MultipartFile> mfList = req.getFiles("imgfile");
		int n =0;
		for(int i=0; i<mfList.size(); i++) {
			String filename = mfList.get(i).getOriginalFilename();
			//System.out.println(filename);
			
			String newFilename = null;
			String fileExt = filename.substring(filename.lastIndexOf(".")); 
			if(fileExt == null || fileExt.equals("")) 
				return null;
			// 서버에 저장할 새로운 파일명을 만든다.
			newFilename = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", 
					         Calendar.getInstance());
			newFilename += System.nanoTime();
			newFilename += fileExt;			
			//System.out.println(newFilename);
			
			mfList.get(i).transferTo(new File(path+File.separator+newFilename));
			HashMap<String,String> paraMap = new HashMap<String,String>();
			paraMap.put("roomcode", roomcode);
			paraMap.put("newFilename", newFilename);
			
			n = service.setRoomImg(paraMap);
		}
		
		if(n==1) {
			RoomVO roomvo = service.getRoomInfo(roomcode);
			List<String> roomimgList = roomvo.getRoomimgList();
			
			JSONArray jsonArr = new JSONArray(); // [] null이 아니다.
			//JSONObject jsonobj = new JSONObject();
			for(int i=0; i<roomimgList.size(); i++) {
				//jsonobj.put("roomimg", roomimgList.get(i));
				//System.out.println(jsonobj);
				jsonArr.put(roomimgList.get(i));
			}
			String str_json = jsonArr.toString();
			req.setAttribute("str_json", str_json);
		}
		
		return "JSON";
		
		//return "hostRoomEdit/hrPhotoEdit.hosttiles_nofooter";
	}
	
	// 이미지파일 삭제 
	@RequestMapping(value = "/imgfiledelete.air", method = { RequestMethod.POST })
	public String imgfiledelete(HttpServletRequest req){
		
		String deleteFilename = req.getParameter("img");
		String roomcode = req.getParameter("roomcode");
		System.out.println(deleteFilename);
		System.out.println(roomcode);
		
		HttpSession session = req.getSession();
		String root = session.getServletContext().getRealPath("/");
		String path = root + "resources" + File.separator+ "images"+File.separator+"becomehost";	
		File file = new File(path+File.separator+deleteFilename);
		System.out.println(file);
		
		if(file.exists()) {
			file.deleteOnExit();
			service.deleteFile(deleteFilename);
			
			RoomVO roomvo = service.getRoomInfo(roomcode);
			List<String> roomimgList = roomvo.getRoomimgList();
			
			JSONArray jsonArr = new JSONArray(); // [] null이 아니다.
			//JSONObject jsonobj = new JSONObject();
			for(int i=0; i<roomimgList.size(); i++) {
				//jsonobj.put("roomimg", roomimgList.get(i));
				//System.out.println(jsonobj);
				jsonArr.put(roomimgList.get(i));
			}
			//System.out.println(jsonArr);
			String str_json = jsonArr.toString();
			req.setAttribute("str_json", str_json);
		}
		
		return "JSON";
	}
	
	// 커버이미지 변경 하는 메소드 
	@RequestMapping(value="/coverChange.air", method= {RequestMethod.GET})
	public String coverChange(HttpServletRequest req) {
		String imgFilename = req.getParameter("imgFilename");
		System.out.println(imgFilename);
		
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("imgFilename", imgFilename);
		
		String str_json = jsonobj.toString();
		req.setAttribute("str_json", str_json);
		return "JSON";
	}
	
	@RequestMapping(value="/saveCover.air", method= {RequestMethod.GET})
	public String saveCover(HttpServletRequest req) {
		String roomcode = req.getParameter("roomcode");
		String changeImg = req.getParameter("changeImg");
		String mainImg = req.getParameter("mainImg");
		HashMap<String,String> paraMap = null;
		System.out.println(roomcode);
		System.out.println(changeImg);
		System.out.println(mainImg);
		
		int n = 0; 
		int n1 = 0; 
		int n2 = 0;
		
		paraMap = new HashMap<String,String>();
		paraMap.put("roomcode", roomcode);
		paraMap.put("changeImg", changeImg);
		n = service.updateCoverImg(paraMap);
		
		if(n==1) {
			n1 = service.deleteFile(changeImg);
		}
		
		if(n1==1) {
			paraMap = new HashMap<String,String>();
			paraMap.put("roomcode", roomcode);
			paraMap.put("newFilename", mainImg);

			n2 = service.setRoomImg(paraMap);
		}
		
		
		RoomVO roomvo = service.getRoomInfo(roomcode);
		req.setAttribute("roomvo", roomvo);
	
		return "hostRoomEdit/hrPhotoEdit.hosttiles_nofooter";
	}
	
	// 호스트 숙소 제목 수정 페이지 이동
	@RequestMapping(value = "/hrTitleEdit.air", method = { RequestMethod.GET })
	public String hrTitleEdit(HttpServletRequest req) {
		String roomcode = req.getParameter("roomcode");
		RoomVO roomvo = (RoomVO) service.getRoomInfo(roomcode);
		req.setAttribute("roomvo", roomvo);
		return "hostRoomEdit/hrTitleEdit.hosttiles_nofooter";
	}
	
	// 호스트 숙소 제목 저장 취소시 기존 제목과 내용 불러오기 
	@RequestMapping(value = "/savecancel.air", method = { RequestMethod.GET })
	public String savecancel(HttpServletRequest req) {
		String roomcode = req.getParameter("roomcode");
		System.out.println(roomcode);
		RoomVO roomvo = (RoomVO) service.getRoomInfo(roomcode);
	
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("roomcode", roomvo.getRoomcode());
		jsonobj.put("roomname", roomvo.getRoomName());
		jsonobj.put("roomInfo", roomvo.getRoomInfo());

		String str_json = jsonobj.toString();
		req.setAttribute("str_json", str_json);
		return "JSON";
	}
	
	// 호스트 숙소 제목 바꾸기
	@RequestMapping(value = "/changeRoomInfo.air", method = { RequestMethod.POST })
	public String changeRoomInfo(HttpServletRequest req) {
		
		String roomcode = req.getParameter("roomcode");
		String roomname = req.getParameter("roomname");
		String roomInfo = req.getParameter("roomInfo");
		HashMap<String,String> paraMap = new HashMap<String,String>();
		paraMap.put("roomcode", roomcode);
		paraMap.put("roomname", roomname);
		paraMap.put("roomInfo", roomInfo);
		
		int result = service.changeRoomtitle(paraMap);
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("result", result);
		if(result == 1) {
			RoomVO roomvo = (RoomVO)service.getRoomInfo(roomcode);
			jsonobj.put("roomcode", roomvo.getRoomcode());
			jsonobj.put("roomname", roomvo.getRoomName());
			jsonobj.put("roomInfo", roomvo.getRoomInfo());
		}
		String str_json = jsonobj.toString();
		req.setAttribute("str_json", str_json);
		return "JSON";
	}
	// 호스트 숙소 침실 수정
	@RequestMapping(value = "/bedroomEdit.air", method = { RequestMethod.GET })
	public String bedroomEdit(HttpServletRequest req) {
		String roomcode = req.getParameter("roomcode");
		RoomVO roomvo = service.getRoomInfo(roomcode);
		
		List<HashMap<String, String>> buildTypeList = service.selectbuildType();// 건물유형 가져오기
		List<String> roomtype = service.selectroomtype();// 숙소유형 가져오기
		
		req.setAttribute("roomtype", roomtype);
		req.setAttribute("buildTypeList", buildTypeList);  
		req.setAttribute("roomvo", roomvo);
		return "hostRoomEdit/bedroomEdit.hosttiles_nofooter";
	}
	
	@RequestMapping(value="/getRoomtypeList.air", method={RequestMethod.GET})
	public String roomtypeJSON(HttpServletRequest req) {
      
		String buildType = req.getParameter("buildType");
		JSONArray jsonArr = new JSONArray();
		if(buildType != null && !buildType.trim().isEmpty()) {
			List<HashMap<String,String>> buildTypeList = service.selectbuildTypedetail(buildType);// 건물세부유형 가져오기
			if(buildTypeList != null && buildTypeList.size() > 0) {
				for(HashMap<String,String> map : buildTypeList) {
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("buildtype_detail_idx", map.get("buildtype_detail_idx")); // 키값, xml에서 읽어온 키값
					jsonObj.put("buildtype_detail_name", map.get("buildtype_detail_name"));
					jsonArr.put(jsonObj);
				}// end of for
			}// end of if
		}
		String str_json = jsonArr.toString();
		req.setAttribute("str_json", str_json);
	  
		return "JSON";      
	} 
	@RequestMapping(value="/changebedroomcount.air", method={RequestMethod.GET})
	public String changebedroomcount(HttpServletRequest req) {
      
		String bedroom = req.getParameter("bedroom");
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("bedroom",bedroom);
		String str_json = jsonObj.toString();
		req.setAttribute("str_json", str_json);
		return "JSON";      
	}
	
	@RequestMapping(value = "/bedAndPersonEdit.air", method = { RequestMethod.POST })
	public String bedAndPersonEdit(HttpServletRequest req) {
		String roomcode = req.getParameter("roomcode");
		String fk_buildType_detail_idx = req.getParameter("fk_buildType_detail_idx");
		String fk_roomType_idx = req.getParameter("fk_roomType_idx");
		String basic_person = req.getParameter("basic_person");
		String max_person = req.getParameter("max_person");
		String roomCount = req.getParameter("roomCount");
		String bathCount = req.getParameter("bathCount");
		
		HashMap<String,String> paraMap = new HashMap<String,String>();
		paraMap.put("roomcode", roomcode);
		paraMap.put("fk_buildType_detail_idx", fk_buildType_detail_idx);
		paraMap.put("fk_roomType_idx", fk_roomType_idx);
		paraMap.put("basic_person", basic_person);
		paraMap.put("max_person", max_person);
		paraMap.put("roomCount", roomCount);
		paraMap.put("bathCount", bathCount);
		int n = service.roomUpdate(paraMap);
		RoomVO roomvo = service.getRoomInfo(roomcode);;
		System.out.println(n);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("n",n);
		jsonObj.put("roomcode",roomvo.getRoomcode());

		String str_json = jsonObj.toString();
		req.setAttribute("str_json", str_json);
		return "JSON";
	}
	
	// 숙소 페이지
	@RequestMapping(value = "/hostroomPage.air", method = { RequestMethod.GET })
	public String hostroomPage() {
		return "host/hostroomPage.hosttiles";
	}

	// 숙소 평점, 수입, 성취도 페이지
	@RequestMapping(value = "/hostroomMark.air", method = { RequestMethod.GET })
	public String hostroomMark(HttpServletRequest req) {
		List<RoomVO> roomList = null;
		HttpSession session = req.getSession(); 
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser"); 
		String userid = null;
		if(loginuser != null) { 
			userid = loginuser.getUserid(); 
		}
		roomList = service.getRoomList(userid);
		req.setAttribute("roomList", roomList);
		return "host/hostroomMark.hosttiles";
	}
	
	@RequestMapping(value = "/rankShowJSON.air", method = { RequestMethod.POST })
	public String rankShowJSON(HttpServletRequest req, HttpServletResponse res) {
		
		String roomcode = req.getParameter("roomcode");
		//System.out.println("char roomcode:"+roomcode);
		RoomVO roomvo = service.getRoomInfo(roomcode);
		List<HashMap<String, String>> countList = service.getPoint(roomcode);
		
		JSONArray jsonArr = new JSONArray();
		for(int i=0; i<countList.size(); i++) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("roomname", roomvo.getRoomName());
			jsonObj.put("avgGrade", countList.get(i).get("avgGrade"));
			jsonObj.put("gradeCount", countList.get(i).get("gradeCount"));
			//System.out.println(jsonObj);
			jsonArr.put(jsonObj);
		}
		String str_json = jsonArr.toString();
		req.setAttribute("str_json", str_json);
		return "JSON";
	}
	
	@RequestMapping(value = "/showreview.air", method = { RequestMethod.POST })
	public String showreview(HttpServletRequest req, HttpServletResponse res) {
		
		String roomcode = req.getParameter("roomcode");
		System.out.println("roomcode:"+roomcode);
		
		List<ReviewVO> reviewList = service.getReview(roomcode);
		JSONArray jsonArr = new JSONArray();
		for(ReviewVO reviewvo : reviewList) {
			JSONObject jsonObj = new JSONObject();
			System.out.println("reviewvo:"+reviewvo.getFk_userid());
			jsonObj.put("userid", reviewvo.getFk_userid());
			jsonObj.put("review_content", reviewvo.getReview_content());
			jsonObj.put("review_writedate", reviewvo.getReview_writedate());
			
			jsonArr.put(jsonObj);
		}
		String str_json = jsonArr.toString();
		req.setAttribute("str_json", str_json);

		return "JSON";
	}
	

	@RequestMapping(value = "/allReservation.air", method = { RequestMethod.POST })
	public String allReservation(HttpServletRequest req, HttpServletResponse res) {
		String userid = req.getParameter("userid");
		int year = Calendar.getInstance().get(Calendar.YEAR);
		HashMap<String,String> paraMap = new HashMap<String,String>();
		paraMap.put("userid", userid);
		paraMap.put("year", Integer.toString(year));
		
		int sumReservation = service.allReservation(paraMap);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("sumReservation", sumReservation);
		jsonObj.put("year", year);
		String str_json = jsonObj.toString();
		req.setAttribute("str_json", str_json);
		
		return "JSON";
	}
	
	@RequestMapping(value = "/monthReservation.air", method = { RequestMethod.POST })
	public String monthReservation(HttpServletRequest req, HttpServletResponse res) {
		String userid = req.getParameter("userid");
		int year = Calendar.getInstance().get(Calendar.YEAR);
		String month = req.getParameter("month");
		int sumReservation =0;
		
		HashMap<String,String> paraMap = new HashMap<String,String>();
		paraMap.put("userid", userid);
		paraMap.put("year", Integer.toString(year));
		if(month.equals("allmonth")) {
			System.out.println(paraMap);
			sumReservation = service.allReservation(paraMap);
		} else {
			paraMap.put("month", month);
			sumReservation = service.monthReservation(paraMap);
		
		}
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("sumReservation", sumReservation);
		jsonObj.put("year", year);
		jsonObj.put("month", month);
		String str_json = jsonObj.toString();
		req.setAttribute("str_json", str_json);
		
		return "JSON";
	}
	
	@RequestMapping(value = "/roomViewCount.air", method = { RequestMethod.POST })
	public String roomViewCount(HttpServletRequest req, HttpServletResponse res) {
		String roomcode = req.getParameter("roomcode");
		System.out.println("roomcode:"+roomcode);
		JSONObject jsonObj = new JSONObject();
		String str_json = "";
		if(roomcode.equals("selectRoom")) {
			jsonObj.put("roomcode",roomcode);
			str_json = jsonObj.toString();
		} else {
			HashMap<String,String> countMap = service.getViewAndReservationCount(roomcode);
			
			jsonObj.put("reservationCount", countMap.get("reservationCount"));
			jsonObj.put("viewCount", countMap.get("viewCount"));	
			jsonObj.put("roomcode", countMap.get("roomcode"));
			str_json = jsonObj.toString();
		}
		
		req.setAttribute("str_json", str_json);
		return "JSON";
	}
	// ***** 호스트 등록된 숙소 수정하기(편의시설 및 이용규칙 수정) ***** //
	@RequestMapping(value="/changeConvenienceAndRule.air", method= {RequestMethod.POST})
	public String changeConvenienceAndRule(HttpServletRequest req) {
		String roomcode = req.getParameter("roomcode");
		//System.out.println(roomcode);
		List<String> options = service.getOptionList();// 옵션 가져오기
		List<String> rule = service.getRuleList();// 이용규칙 가져오기
		
		req.setAttribute("roomcode", roomcode);
		req.setAttribute("options", options);
		req.setAttribute("rule", rule);
		
		return "hostRoomEdit/changeConvenienceAndRule.hosttiles_nofooter";
	}
	
	
	@RequestMapping(value="/changeOptionAndRule.air", method= {RequestMethod.GET})
	public String changeOptionAndRule(HttpServletRequest req) {
		String roomcode = req.getParameter("roomcode");
		String[] option = req.getParameterValues("optionchk");
		String[] rule = req.getParameterValues("rulechk");
		if(option != null && rule != null) {
			int n = service.deleteOptionAndRule(roomcode);
			
			HashMap<String,String> paraMap = new HashMap<String, String>();
			for(int i=0; i<option.length; i++) {
				paraMap.put("option", option[i]);
				paraMap.put("roomcode", roomcode);
				service.insertOption(paraMap);
			}
			
			for(int i=0; i<rule.length; i++) {
				paraMap.put("rule", rule[i]);
				paraMap.put("roomcode", roomcode);
				service.insertRule(paraMap);
			}
			
		}
		
		HttpSession session = req.getSession(); 
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser"); 
		String userid = null;
		if(loginuser != null) { 
			userid = loginuser.getUserid(); 
		}
		List<RoomVO> roomList = null;
		roomList = service.getRoomList(userid);
		
		RoomVO roomvo = service.getRoomInfo(roomcode);
		
		req.setAttribute("roomList", roomList);
		req.setAttribute("roomvo", roomvo);
		return "hostRoomEdit/hostRoomEdit.hosttiles_nofooter";
	}
	///////////////////////////////////////////////////////////////////////////////////
	
	// ***** 호스트 등록된 숙소 수정하기(기본요금 수정) ***** //
	@RequestMapping(value="/changeDefaultRoomCharge.air", method= {RequestMethod.GET})
	public String changeDefaultRoomCharge (HttpServletRequest req) {
		return "hostRoomEdit/changeDefaultRoomCharge.hosttiles_nofooter";
	}
	
	// ***** 호스트 등록된 숙소 수정하기(추가요금 수정) ***** //
	@RequestMapping(value="/changePlusRoomCharge.air", method= {RequestMethod.GET})
	public String changePlusRoomCharge (HttpServletRequest req) {
		return "hostRoomEdit/changePlusRoomCharge.hosttiles_nofooter";
	}
	
	// ***** 호스트 등록된 숙소 수정하기(체크인과 체크아웃 수정) ***** //
	@RequestMapping(value="/changeCheckInCheckOut.air", method= {RequestMethod.GET})
	public String changeCheckInCheckOut (HttpServletRequest req) {
		return "hostRoomEdit/changeCheckInCheckOut.hosttiles_nofooter";
	}


}
