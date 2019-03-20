package com.spring.bnb.controller;
 
import java.util.*;
 
import javax.servlet.http.*;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import com.spring.bnb.model.*;
import com.spring.bnb.service.InterWCService;
import com.spring.common.AES256;
import com.spring.common.MyUtil;

@Controller
public class WCController {

	@Autowired
	private InterWCService service;
	 
	@Autowired
	private AES256 aes;
	 
	@RequestMapping(value = "/index.air", method = RequestMethod.GET)
	public String index(HttpServletRequest req) {
		// 메인페이지 요청 시 모든 숙소 리스트 가져오기
		List<RoomVO> roomList = service.getRecommandRoomList();
		
		List<ReviewVO> reviewList = service.getBestReviewList(); 
		
		req.setAttribute("roomList", roomList);
		req.setAttribute("reviewLeft", reviewList.get(0));
		req.setAttribute("reviewRight", reviewList.get(1));
		return "main/index";   
	} 
	
	@RequestMapping(value = "/admin.air", method = RequestMethod.GET)
	public String admin_index(HttpServletRequest req) {
		MemberVO loginuser = (MemberVO)req.getSession().getAttribute("loginuser");
		
		if(loginuser == null) {
			String msg = "로그인이 필요합니다";
			String loc = "index.air";
			
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			return "msg";
			
		}else if(!"admin".equalsIgnoreCase(loginuser.getUserid())) {
			String msg = "관리자만 이용가능한 페이지입니다.";
			String loc = "index.air";
			
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			return "msg";
		}else {
			return "admin/index.admintiles";
		}
		 
	}
	// admin 로그아웃 세션에 저장된  loginuser 삭제하기
	@RequestMapping(value = "/adminLogout.air", method = RequestMethod.GET)
	public String logout(HttpServletRequest req) {
		HttpSession session = req.getSession();
		session.invalidate();
		req.setAttribute("msg", "로그아웃 되었습니다");
		req.setAttribute("loc", "index.air");
		return "msg";
	}
	
	@RequestMapping(value = "/lodgingManage.air", method = RequestMethod.GET)
	public String lodgingManage(HttpServletRequest req, HttpServletResponse res) {
		
		//List<RoomVO> roomList = service.getAllRoomList();
			
		List<RoomVO> roomList = null;
		
	    /* 페이징 처리 */
	    String colname = req.getParameter("colname");
	    String search = req.getParameter("search");
	 
	    HashMap<String, String> paraMap = new HashMap<String, String>();
	    paraMap.put("COLNAME", colname);
	    paraMap.put("SEARCH", search);
		String str_currentShowPageNo = req.getParameter("currentShowPageNo"); 
		
		int totalCount = 0;         // 총게시물 건수
		int sizePerPage = 5;       // 한 페이지당 보여줄 게시물 건수 
		int currentShowPageNo = 0;  // 현재 보여주는 페이지번호로서, 초기치로는 1페이지로 설정함.
		int totalPage = 0;          // 총 페이지수(웹브라우저상에 보여줄 총 페이지 갯수)
		
		int startRno = 0;           // 시작 행 번호
		int endRno = 0;             // 끝 행 번호
		
		int blockSize = 10;         // "페이지바" 에 보여줄 페이지의 갯수 
		
		if(search != null &&
		  !search.trim().equals("") && 
		  !search.trim().equals("null")) {
		  // 검색이 있는 경우(페이징 처리 한 것임)
		  totalCount = service.getLodgingTotalCountWithSearch(paraMap); 
		}
		
		else {
		  // 검색이 없는 경우(페이징 처리 한 것임)
		  totalCount = service.getLodgingTotalCountNoSearch();	
		}

		totalPage = (int)Math.ceil((double)totalCount/sizePerPage);
		if(str_currentShowPageNo == null) {
			// 게시판 초기화면일 경우
			currentShowPageNo = 1;
		}else {
		    // 특정페이지를 보고자 조회한 경우 
			try {
				currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
				
				if(currentShowPageNo < 1 || currentShowPageNo > totalPage) {
					currentShowPageNo = 1;
				}
			} catch(NumberFormatException e) {
				currentShowPageNo = 1;
			}
		}
		
		startRno = ((currentShowPageNo-1)*sizePerPage) + 1;
		endRno = startRno + sizePerPage - 1;
		
		paraMap.put("STARTRNO", String.valueOf(startRno));
		paraMap.put("ENDRNO", String.valueOf(endRno));
		
		roomList = service.lodgingListPaging(paraMap);
		
		String pagebar = "<ul>";
		pagebar += MyUtil.getPageBarWithSearch(sizePerPage, blockSize, totalPage, currentShowPageNo, colname, search, null, "lodgingManage.air");  
		pagebar += "</ul>";
		req.setAttribute("colname", colname); // view단에서 검색어를 유지시키려고 보낸다. 
		req.setAttribute("search", search);   // view단에서 검색어를 유지시키려고 보낸다.
	    req.setAttribute("pagebar", pagebar); // view단으로 페이지바 넘기기
		  
	      
	    /* 특정 글제목을 클릭하여 상세내용을 본 이후 페이징 처리된 해당 페이지로 그대로 돌아가기 위해
	   	      돌아갈 페이지를 위해서 gobackURL을 뷰단으로 넘겨준다. */
	    String gobackURL = MyUtil.getCurrentURL(req);
	    req.setAttribute("gobackURL", gobackURL);
		req.setAttribute("roomList", roomList);
		return "admin/lodging_manage.admintiles";
	}
	
	@RequestMapping(value = "/lodgingRegistManage.air", method = RequestMethod.GET)
	public String lodgingRegistManage(HttpServletRequest req, HttpServletResponse res) {
		List<RoomVO> roomList = null;
		/*
	    // 페이징처리
	    String colname = req.getParameter("colname");
	    String search = req.getParameter("search");
	
	    HashMap<String, String> paraMap = new HashMap<String, String>();
	    paraMap.put("COLNAME", colname);
	    paraMap.put("SEARCH", search);
		String str_currentShowPageNo = req.getParameter("currentShowPageNo"); 
		
		int totalCount = 0;         // 총게시물 건수
		int sizePerPage = 5;       // 한 페이지당 보여줄 게시물 건수 
		int currentShowPageNo = 0;  // 현재 보여주는 페이지번호로서, 초기치로는 1페이지로 설정함.
		int totalPage = 0;          // 총 페이지수(웹브라우저상에 보여줄 총 페이지 갯수)
		
		int startRno = 0;           // 시작 행 번호
		int endRno = 0;             // 끝 행 번호
		
		int blockSize = 10;         // "페이지바" 에 보여줄 페이지의 갯수 
		
		if(search != null &&
		  !search.trim().equals("") && 
		  !search.trim().equals("null")) {
		  // 검색이 있는 경우(페이징 처리 한 것임)
		  totalCount = service.getLodgingTotalCountWithSearchBeforePermission(paraMap); 
		}
		
		else {
		  // 검색이 없는 경우(페이징 처리 한 것임)
		  totalCount = service.getLodgingTotalCountNoSearch();	
		}

		totalPage = (int)Math.ceil((double)totalCount/sizePerPage);
		if(str_currentShowPageNo == null) {
			// 게시판 초기화면일 경우
			currentShowPageNo = 1;
		}else {
		    // 특정페이지를 보고자 조회한 경우 
			try {
				currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
				
				if(currentShowPageNo < 1 || currentShowPageNo > totalPage) {
					currentShowPageNo = 1;
				}
			} catch(NumberFormatException e) {
				currentShowPageNo = 1;
			}
		}
		
		startRno = ((currentShowPageNo-1)*sizePerPage) + 1;
		endRno = startRno + sizePerPage - 1;
		
		paraMap.put("STARTRNO", String.valueOf(startRno));
		paraMap.put("ENDRNO", String.valueOf(endRno));
		
		roomList = service.lodgingListPaging(paraMap);
		
		String pagebar = "<ul>";
		pagebar += MyUtil.getPageBarWithSearch(sizePerPage, blockSize, totalPage, currentShowPageNo, colname, search, null, "lodgingManage.air");  
		pagebar += "</ul>";
		req.setAttribute("colname", colname); // view단에서 검색어를 유지시키려고 보낸다. 
		req.setAttribute("search", search);   // view단에서 검색어를 유지시키려고 보낸다.
	    req.setAttribute("pagebar", pagebar); // view단으로 페이지바 넘기기
		  
	      
	    // 특정 글제목을 클릭하여 상세내용을 본 이후 페이징 처리된 해당 페이지로 그대로 돌아가기 위해
	   	//      돌아갈 페이지를 위해서 gobackURL을 뷰단으로 넘겨준다. 
	    String gobackURL = MyUtil.getCurrentURL(req);
	    req.setAttribute("gobackURL", gobackURL);
	    */
		
		roomList = service.toPermitLodgingList();
		req.setAttribute("roomList", roomList);
		
		
		return "admin/lodging_regist_manage.admintiles";
	} 
	
	@RequestMapping(value = "/roomdelete.air", method = RequestMethod.POST)
	public String roomdelete(HttpServletRequest req) {
		
		String roomcode = req.getParameter("roomcode");
		
		int result = service.deleteRoomByRoomcode(roomcode);
		
		String msg = "";
		String loc = "";
		
		if(result == 1) {
			msg = "해당 숙소를 영업정지 시켰습니다.";
			loc = "lodgingManage.air";
			
			
		}else {
			msg = "해당 숙소 영업정지에 실패하였습니다.";
			loc = "javascript:history.back();";
		
		}
		
		req.setAttribute("msg", msg);
		req.setAttribute("loc", loc);
		
		return "msg";
		
		
	}
	@RequestMapping(value = "/roomcontinue.air", method = RequestMethod.POST)
	public String roomcontinue(HttpServletRequest req) {
		
		String roomcode = req.getParameter("roomcode");
		
		int result = service.continueRoomByRoomcode(roomcode);
		
		String msg = "";
		String loc = "";
		
		if(result == 1) {
			msg = "해당 숙소를 영업활성화 시켰습니다.";
			loc = "lodgingManage.air";
			
			
		}else {
			msg = "해당 숙소 영업활성화에 실패하였습니다.";
			loc = "javascript:history.back();";
		
		}
		
		req.setAttribute("msg", msg);
		req.setAttribute("loc", loc);
		
		return "msg";
		
		
	}
	
	@RequestMapping(value = "/roompermit.air", method = RequestMethod.POST)
	public String roompermit(HttpServletRequest req) {
		
		String roomcode = req.getParameter("roomcode");
		
		int result = service.continueRoomByRoomcode(roomcode);
		
		String msg = "";
		String loc = "";
		
		if(result == 1) {
			msg = "해당 숙소의 영업을 허가하였습니다.";
			loc = "lodgingRegistManage.air"; 
			
		}else {
			msg = "해당 숙소의 영업을 허가에 실패하였습니다.";
			loc = "javascript:history.back();";
		
		}
		
		req.setAttribute("msg", msg);
		req.setAttribute("loc", loc);
		
		return "msg";
		
		
	} 
	
} 