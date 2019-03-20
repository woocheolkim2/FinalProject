package com.spring.bnb.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.spring.bnb.model.MemberVO;
import com.spring.bnb.model.ReservationVO;
import com.spring.bnb.model.ReviewVO;
import com.spring.bnb.model.RoomVO;
import com.spring.bnb.service.InterHYService;
import com.spring.common.AES256;
import com.spring.common.SHA256;

@Controller
public class HYController {

	//===== #35. 의존객체주입(DI:Dependency Injection)  =====
	@Autowired
	private InterHYService service; 

	@Autowired
	private AES256 aes;
	
	// 숙소 상세페이지
	@RequestMapping(value = "/homeDetail.air", method = RequestMethod.GET)
	public String index(HttpServletRequest req) {
		String roomcode = req.getParameter("roomcode");
		//if(roomcode==null) roomcode = "10";
		RoomVO roomvo = service.getRoomByCode(roomcode);
		List<RoomVO> recommendRoomList = service.getRecommendRoomList(roomvo.getRoomSigungu());
		int totalReviewCount = roomvo.getReviewList().size();
		HashMap<String,Object> starMap = service.getStarPoint(roomcode);
		service.roomViewCountUp(roomcode);
		req.setAttribute("room", roomvo);
		req.setAttribute("recommendRoomList", recommendRoomList);
		req.setAttribute("totalReviewCount", totalReviewCount);
		req.setAttribute("starMap", starMap);
		return "home/homeDetail.hometiles";
	}
	
	// 페이징 처리한 리뷰 검색
	@RequestMapping(value = "/reviewSearch.air", method = RequestMethod.GET)
	public String reviewSearch(HttpServletRequest req) {
		// 검색어와 숙소 코드 받아오기
		String reviewSearchWord = req.getParameter("reviewSearchWord");
		if(reviewSearchWord==null) reviewSearchWord="";
		String roomcode = req.getParameter("roomcode");
		// 리뷰 페이징 처리
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ROOMCODE", roomcode); // 해당 숙소번호의 모든 리뷰조회
		
		String str_currentShowPageNo = req.getParameter("currentShowPageNo"); 
		int totalCount = 0,currentShowPageNo = 0,sizePerPage = 5,totalPage = 0;
		int startRno = 0,endRno = 0;
		
		totalPage = (int)Math.ceil((double)totalCount/sizePerPage);
		if(str_currentShowPageNo == null) currentShowPageNo = 1;
		else {
			try {
				currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
				if(currentShowPageNo < 1 || currentShowPageNo > totalPage) currentShowPageNo = 1;
			} catch(NumberFormatException e) {currentShowPageNo = 1;}
		}
		startRno = ((currentShowPageNo-1)*sizePerPage) + 1;
		endRno = startRno + sizePerPage - 1;

		// 파라미터로 넘길 HashMap만들기
		paraMap.put("REVIEWSEARCHWORD", reviewSearchWord);
		paraMap.put("STARTRNO", String.valueOf(startRno));
		paraMap.put("ENDRNO", String.valueOf(endRno));
		
		// DB에서 검색하여 결과 가져오기
		List<ReviewVO> reviewList = service.getSearchReview(paraMap);
		// 가져오기
		JSONArray jsonArr = new JSONArray();
		for(ReviewVO review :reviewList) {
			JSONObject jobj = new JSONObject();
			jobj.put("review_idx",review.getReview_idx());
			jobj.put("fk_roomcode",review.getFk_roomcode());
			jobj.put("fk_userid",review.getFk_userid());
			jobj.put("review_content",review.getReview_content());
			jobj.put("review_wrtitedate",review.getReview_writedate());
			jobj.put("userProfileImg",review.getUser().getProfileimg());
			jsonArr.put(jobj);
		}
		String str_json = jsonArr.toString();
		req.setAttribute("str_json", str_json);
		return "JSON";
	}
	
	// DB로 로그인 체크하기
	@RequestMapping(value = "/login.air", method = RequestMethod.POST)
	public String login(HttpServletRequest req ,HttpServletResponse res,MemberVO member) throws ServletException, IOException {
		member.setPwd(SHA256.encrypt(member.getPwd()));
		System.out.println("userid : "+member.getUserid()+"/ pwd : "+member.getPwd());
		MemberVO loginuser = service.logincheck(member); // 로그인 검사하는 메소드
		JSONObject jobj = new JSONObject();
		String logincheck = "";
		if(loginuser==null) logincheck = "false";
		else {
			// 로그인 성공시 세션에 해당 유저정보저장
			logincheck = "true";
			HttpSession session = req.getSession();
			session.setAttribute("loginuser", loginuser);
		}
		jobj.put("logincheck", logincheck);
		jobj.put("userid", loginuser.getUserid());
		String str_json = jobj.toString();
		System.out.println(str_json);
		req.setAttribute("str_json", str_json);
		return "JSON";
	}
	
	// 로그아웃 세션에 저장된  loginuser 삭제하기
	@RequestMapping(value = "/logout.air", method = RequestMethod.POST)
	public String logout(HttpServletRequest req) {
		HttpSession session = req.getSession();
		session.removeAttribute("loginuser");
		JSONObject jobj = new JSONObject();
		jobj.put("msg", "로그아웃 되었습니다.");
		String str_json = jobj.toString();
		req.setAttribute("str_json", str_json);
		return "JSON";
	}

	// 숙소 관심테이블에 저장하기
	@RequestMapping(value = "/likeRoom.air", method = RequestMethod.POST)
	public String requireLogin_likeRoom(HttpServletRequest req,HttpServletResponse res ,MemberVO member) {
		String userid = req.getParameter("userid");
		String roomcode = req.getParameter("roomcode");
		String saveTitle = req.getParameter("saveTitle");
		//System.out.println("roomcode : "+roomcode+"/ userid : "+userid+"/saveTitle : "+saveTitle);
		HashMap<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.put("USERID", userid);
		paraMap.put("ROOMCODE", roomcode);
		paraMap.put("SAVETITLE", saveTitle);
		int n = service.insertLikeRoom(paraMap);
		JSONObject jobj = new JSONObject();
		jobj.put("n", n);
		String str_json = jobj.toString();
		req.setAttribute("str_json", str_json);
		return "JSON";
	}
	
	// 로그인 유저의 관심 숙소 리스트 불러오기
	@RequestMapping(value = "/myLikeRoomList.air", method = RequestMethod.GET)
	public String myLikeRoomList(HttpServletRequest req ,MemberVO member) {
		String userid = req.getParameter("userid");
		List<HashMap<String,Object>> resultMap = service.getMyLikeRoomList(userid);
		JSONArray jsonArr = new JSONArray();
		for(HashMap<String,Object> result :resultMap) jsonArr.put(result);
		String str_json = jsonArr.toString();
		req.setAttribute("str_json", str_json);
		return "JSON";
	}
	
	// 회원가입시 아이디 중복체크
	@RequestMapping(value = "/idDuplicateCheck.air", method = RequestMethod.POST)
	public String idDuplicateCheck(HttpServletRequest req) {
		String userid = req.getParameter("userid");
		int n = service.checkDuplicateID(userid);
		JSONObject json = new JSONObject();
		json.put("n", n);
		String str_json = json.toString();
		req.setAttribute("str_json", str_json);
		return "JSON";
	}
	
	// ajax 회원가입
	@RequestMapping(value = "/joinEnd.air", method = RequestMethod.POST)
	public String joinEnd(MultipartHttpServletRequest req, @RequestParam("file") MultipartFile profile, @ModelAttribute MemberVO member) throws IOException {
		// 프로필 이미지 업로드
		String filename = null; // 파일명 초기화
		if (!profile.isEmpty()) { // 파일 있으면(업로드 했으면)
			String root = req.getSession().getServletContext().getRealPath("/");
			String realPath = root+File.separator+"resources"+File.separator+"images"+File.separator+"profile";
			//String gitrealPath = "C:/Users/user1/git/FinalProject/FinalProject/src/main/webapp/resources/images/profile";
			filename = profile.getOriginalFilename(); // 업로드한 파일명 가져오기
			// 엣지 브라우저 요청 파일이름 처리
			int index = filename.lastIndexOf("\\");
			filename = filename.substring(index + 1);
	        File file = new File(realPath, filename);
	        //File gitfile = new File(gitrealPath, filename);
	        if (file.exists()) { // 해당 경로에 동일한 파일명이 이미 존재하는 경우 파일명 앞에 업로드 시간 밀리초 붙여서 파일명 중복을 방지
	        	filename = System.currentTimeMillis() + "_" + filename;
	        	file = new File(realPath, filename);
	        	//gitfile = new File(gitrealPath, filename);
	        }
	        System.out.println("업로드 경로: " + realPath);
	        System.out.println("업로드 파일명: " + filename);
	        // 업로드 수행
	        IOUtils.copy(profile.getInputStream(), new FileOutputStream(file));
	        //IOUtils.copy(profile.getInputStream(), new FileOutputStream(gitfile));
			member.setProfileimg(filename);
		} 
		else {
			System.out.println("파일이 존재하지 않거나 파일크기가 0 입니다.");
			member.setProfileimg("user.png");
		}
		
		// 생년월일 변환
		String year = req.getParameter("year");
		String month = req.getParameter("month");
		String day = req.getParameter("day");
		if(Integer.parseInt(month)<10) month = "0"+month;
		if(Integer.parseInt(day)<10) day = "0"+day;		
		String birth = year+"-"+month+"-"+day;
		member.setBirthday(birth);
		// 암호화
		try {
			member.setPwd(SHA256.encrypt(member.getPwd()));
			member.setEmail(aes.encrypt(member.getEmail()));
			member.setPhone(aes.encrypt(member.getPhone()));
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		}
		
        int n = service.insertMember(member);
		JSONObject json = new JSONObject();
		json.put("n", n);
		String str_json = json.toString();
		req.setAttribute("str_json", str_json);
		return "JSON";
	}
	
	// 침실과 침대 갯수 가져오기
	@RequestMapping(value = "/checkbedroom.air", method = RequestMethod.POST)
	public String checkbedroom(HttpServletRequest req) {
		String bedroomInfo = req.getParameter("bedroomInfo");
		String[] bedroomInfoArr = bedroomInfo.split("/");
		// for문을돌리면 하나의 침실정보가 String 형태로 나옴
		int n = 0;
		for(String str : bedroomInfoArr) {
			HashMap<String,Object> paraMap = new HashMap<String,Object>();
			JSONObject jsonbedinfo = new JSONObject(str); // 가져온 String 형태를 JSON으로 변환
			Set<String> jsonkeys = jsonbedinfo.keySet(); // JSON으로 변환된 객체의 key들을 가져옴
			paraMap.put("roomcode", "10");
			for(String key :jsonkeys) paraMap.put(key, jsonbedinfo.get(key)); // key값만큼 for문을 돌려서 hash맵 형태로 저장
			n = service.insertbedroom(paraMap);
		}
		JSONObject json = new JSONObject();
		json.put("n", n);
		req.setAttribute("str_json", json.toString());
		return "JSON";
	}
	
	// 작성한 리뷰 DB에 insert하기
	@RequestMapping(value="reviewInsert.air",method=RequestMethod.POST)
	public String reviewInsert(ReviewVO review, HttpServletRequest req) {
		int n = service.insertReview(review);
		JSONObject json = new JSONObject();
		json.put("n", n);
		req.setAttribute("str_json", json.toString());
		return "JSON";
	}
	
	// 작성한 리뷰 DB에 insert하기
	@RequestMapping(value="reservationCheck.air",method=RequestMethod.POST)
	public String reservationCheck(HttpServletRequest req) {
		String roomcode = req.getParameter("roomcode");
		List<ReservationVO> rsvList= service.reservationCheck(roomcode);
		if(rsvList==null||rsvList.size()<1) {
			JSONObject jobj = new JSONObject();
			jobj.put("rsvcheck", "true");
			req.setAttribute("str_json", jobj.toString());
		}
		else {
			JSONArray jsonarr = new JSONArray();
			for(ReservationVO rsv : rsvList) {
				JSONObject json = new JSONObject();
				json.put("checkinDate", rsv.getRsv_checkInDate());
				json.put("checkoutDate", rsv.getRsv_checkOutDate());
				jsonarr.put(json);
			}
			req.setAttribute("str_json", jsonarr.toString());
		}
		return "JSON";
	}
	
	// header에서 숙소 검색하기
	@RequestMapping(value="searchRoomInHeader.air",method=RequestMethod.GET)
	public String searchRoomInHeader(HttpServletRequest req) {
		String searchword = req.getParameter("searchword");
		System.out.println(searchword);
		List<String> searchList = service.getSearchSido(searchword);
		JSONArray jsonArr = new JSONArray();
		for(String str : searchList) {
			JSONObject jobj = new JSONObject();
			jobj.put("sido", str);
			jsonArr.put(jobj);
		}
		req.setAttribute("str_json", jsonArr.toString());
		return "JSON";
	}

	// 호스트 메인페이지
	@RequestMapping(value = "/hostMain.air", method = RequestMethod.GET)
	public String requireLogin_hostMain(HttpServletRequest req,HttpServletResponse res) {
		HttpSession session = req.getSession();
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
		List<HashMap<String,Object>> incomeList = null;
		List<HashMap<String,Object>> testList = null;
		int myroomsize = loginuser.getMyroomList().size();
		if(myroomsize>0) {
			incomeList = service.getHostIncome(loginuser.getUserid());
			testList = new ArrayList<HashMap<String,Object>>();
			for(RoomVO room : loginuser.getMyroomList()) {
				HashMap<String,Object> testMap = new HashMap<String,Object>();
				testMap.put("name", room.getRoomName());
				int[] arr = new int[12];
				for(HashMap<String,Object> income :incomeList) {
					if(income.get("roomcode").equals(room.getRoomcode())) {
						System.out.println("put!!");
						arr[Integer.parseInt((String)income.get("paydate"))] = (int)income.get("totalprice");
					}
				}
				testMap.put("data", Arrays.toString(arr));
				testList.add(testMap);
			}
			
		}
		else{
			req.setAttribute("msg", "호스트만 접근 가능합니다.");
			req.setAttribute("loc", "index.air");
			return "msg";
		}
		System.out.println(testList.size());
		req.setAttribute("testList", testList);
		req.setAttribute("testListsize", testList.size());
		req.setAttribute("incomeList", incomeList);
		req.setAttribute("myroomsize", myroomsize);
		return "host/hostMain.hosttiles";
	}
	@RequestMapping(value = "/MemberCheckByIdAndEmail.air", method = RequestMethod.POST)
	public String MemberCheckByIdAndEmail(HttpServletRequest req) throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {
		String repwdid = req.getParameter("repwdid");
		String repwdemail = req.getParameter("repwdemail");
		HashMap<String,String> paraMap = new HashMap<String,String>();
		paraMap.put("ID", repwdid);
		paraMap.put("EMAIL", aes.encrypt(repwdemail));
		int n = service.MemberCheckByIdAndEmail(paraMap);
		JSONObject jobj = new JSONObject();
		jobj.put("n", n);
		req.setAttribute("str_json", jobj.toString());;
		return "JSON";
	}
	/*@RequestMapping(value = "/sendCodeForPwd.air", method = RequestMethod.POST)
	public String sendCodeForPwd(HttpServletRequest req) throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {
		String email = req.getParameter("email");
		JSONObject jobj = new JSONObject();
		jobj.put("code", code);
		req.setAttribute("str_json", jobj.toString());;
		return "JSON";
	}*/
}

