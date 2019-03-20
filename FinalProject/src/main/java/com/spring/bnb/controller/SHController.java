package com.spring.bnb.controller;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.bnb.model.CommentVO;
import com.spring.bnb.model.MemberVO;
import com.spring.bnb.model.PhotoVO;
import com.spring.bnb.model.ReportVO;
import com.spring.bnb.service.InterSHService;
import com.spring.common.AES256;
import com.spring.common.FileManager;
import com.spring.common.LargeThumbnailManager;
import com.spring.common.MyUtil;

@Controller
@Component
public class SHController {

	@Autowired
	private InterSHService service;
	
	@Autowired
	private AES256 aes;
	
	@Autowired
	private FileManager fileManager;
	
	@Autowired
	private LargeThumbnailManager largeThumbnailManager;
	
	// 관리자 회원관리 페이지(페이징처리 전)
	@RequestMapping(value="/adminMember.air", method= {RequestMethod.GET})
	public String adminMember(HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
		
		if(loginuser == null) {
			return "main/index";
		} 
		else {
			if(!loginuser.getUserid().equals("admin")) {
				return "main/index";
			}
			
			return "admin/adminMember.admintiles";
		}

	}
	
	
	// 관리자 회원관리 페이지(ajax)
	@RequestMapping(value="/adminMemberJSON.air", method= {RequestMethod.GET})
	public String adminMemberJSON(HttpServletRequest req) {
		
		HashMap<String, String> paraMap = null;
		
		String currentShowPageNo = req.getParameter("currentShowPageNo");
		String searchWord = req.getParameter("searchWord");
		String searchType = req.getParameter("searchType");
		
		if(searchWord == null) {
			searchWord = "";
		}
		
		if(!"username".equals(searchType) &&
		   !"userid".equals(searchType) &&
		   !"addr".equals(searchType) &&
		   searchType == null) {
			searchType = "username";
		}
		
		if(currentShowPageNo == null || "".equals(currentShowPageNo)) {
			currentShowPageNo = "1";
		}
		
		int sizePerPage = 10;	// 한 페이지당 보여줄 갯수
		
		int rno1 = Integer.parseInt(currentShowPageNo)*sizePerPage - (sizePerPage-1);	// 공식!!!
		int rno2 = Integer.parseInt(currentShowPageNo)*sizePerPage;	
		// System.out.println(sizePerPage);
		// System.out.println(rno1);
		// System.out.println(rno2);
		
		
		paraMap = new HashMap<String, String>();
		paraMap.put("RNO1", String.valueOf(rno1));
		paraMap.put("RNO2", String.valueOf(rno2));

		paraMap.put("searchWord", searchWord);
		paraMap.put("searchType", searchType);
		
		List<MemberVO> searchMember = service.getSearchMember(paraMap);
		
		for(int i=0; i<searchMember.size(); i++) {
			try {
				searchMember.get(i).setPhone(aes.decrypt(searchMember.get(i).getPhone()));
			} catch (UnsupportedEncodingException | GeneralSecurityException e) {
				e.printStackTrace();
			}
		} 
		
		JSONArray jsonArr = new JSONArray();
			
		for(int i=0; i<searchMember.size(); i++) {
			
			JSONObject jsonObj = new JSONObject();
			
			jsonObj.put("USERID", searchMember.get(i).getUserid());
			jsonObj.put("USERNAME", searchMember.get(i).getUsername());
			jsonObj.put("BIRTHDAY", searchMember.get(i).getBirthday());
			jsonObj.put("GENDER", searchMember.get(i).getGender());
			jsonObj.put("PHONE", searchMember.get(i).getPhone());
			jsonObj.put("ADDR", searchMember.get(i).getAddr());
			jsonObj.put("DETAILADDR", searchMember.get(i).getDetailAddr());
			jsonObj.put("WARNCOUNT", searchMember.get(i).getWarnCount());
			
			jsonArr.put(jsonObj);			
			
		}
		
		String str_json = jsonArr.toString();
		req.setAttribute("str_json", str_json);

		return "JSON";
	}
	
	
	@RequestMapping(value="/getTotalPages.air", method={RequestMethod.GET})
	public String getCommentTotalPage(HttpServletRequest req) {
		
		HashMap<String, String> paraMap = null;
		
		String currentShowPageNo = req.getParameter("currentShowPageNo");
		String sizePerPage = "10";
		
		if(currentShowPageNo == null || "".equals(currentShowPageNo)) {
			currentShowPageNo = "1";
		}
		
		paraMap = new HashMap<String, String>();
		
		String searchWord = req.getParameter("searchWord");
		String searchType = req.getParameter("searchType");

		if(searchWord== null) {
			searchWord = "";
		}
		
		if(!"username".equals(searchType) &&
		   !"userid".equals(searchType) &&
		   !"addr".equals(searchType) &&
		   searchType == null) {
			searchType = "username";
		}
		
		paraMap.put("searchWord", searchWord);
		paraMap.put("searchType", searchType);
		paraMap.put("sizePerPage", sizePerPage);
		// System.out.println(searchWord);
		// System.out.println(searchType);
		
		
		int totalCount = service.getTotalCount(paraMap);
		// 원글 글번호에 해당하는 댓글의 총 갯수를 알아온다.
		// System.out.println(totalCount);
		// System.out.println(sizePerPage);
		// 총 페이지 수 구하기
		int totalPage = (int)Math.ceil((double)totalCount/Integer.parseInt(sizePerPage));
		/*
		 	57.0(행갯수)/10(sizePerPage) ==> 5.7 ==> 6.0 ==> 6
		 	57.0(행갯수)/5(sizePerPage) ==> 11.4 ==> 12.0 ==> 12
		 	57.0(행갯수)/3(sizePerPage) ==> 19.0 ==> 19.0 ==> 19
		*/
		// System.out.println("총페이지 보여주세요:"+totalPage);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("totalPage", totalPage);
		
		String totalPageJSON = jsonObj.toString();
		
		req.setAttribute("totalPage", totalPageJSON);
		return "totalPageJSON";
		
	}
	
	// 관리자 회원상세 페이지
	@RequestMapping(value="/memberDetail.air", method= {RequestMethod.GET})
	public String memberDetail(HttpServletRequest req) {
		
		String userid = req.getParameter("userid");
		// System.out.println("userid : "+userid);
		
		if(userid != null) {
			MemberVO membervo = service.getMemberDetail(userid);
			// System.out.println("membervo : "+membervo);
			// System.out.println(membervo.getProfileimg());
			try {
				membervo.setPhone(aes.decrypt(membervo.getPhone()));
				membervo.setEmail(aes.decrypt(membervo.getEmail()));
			} catch (UnsupportedEncodingException | GeneralSecurityException e) {
				e.printStackTrace();
			}
		
			List<HashMap<String, String>> reservation = service.getReservation(userid);
			// System.out.println("reservation : "+reservation);
			
			List<HashMap<String, String>> mycoupon = service.getMycoupon(userid);			
			// System.out.println("mycoupon : "+mycoupon);
			
			req.setAttribute("membervo", membervo);
			req.setAttribute("reservation", reservation);
			req.setAttribute("mycoupon", mycoupon);

			return "admin/memberDetail.admintiles";
			
		}
		
		else {
			return "admin/adminMember.admintiles";
		}
	
	}
	
	@RequestMapping(value="/adminMemberDel.air", method= {RequestMethod.GET})
	public String adminMemberDel(HttpServletRequest req) {
		
		String userid = req.getParameter("useridDel");
		
		service.adminDeleteMember(userid);
		
		String msg = "삭제성공!";
		String loc = req.getContextPath()+"/adminMember.air";
		
		req.setAttribute("msg", msg);
		req.setAttribute("loc", loc);
		
		return "msg";
	}
	
	@RequestMapping(value="/adminMemberWarn.air", method= {RequestMethod.GET})
	public String adminMemberWarn(HttpServletRequest req) {
		
		String userid = req.getParameter("useridDel");
		// System.out.println(userid);
		
		service.adminWarnMember(userid);
		
		String msg = "경고 성공!";
		String loc = req.getContextPath()+"/adminMember.air";
		
		req.setAttribute("msg", msg);
		req.setAttribute("loc", loc);
		
		return "msg";
	}
	
	@RequestMapping(value="/board_report.air", method= {RequestMethod.GET})
	public String adminVan(HttpServletRequest req) {

		return "home/board_report.hometiles";
	}
	
	// 관리자 신고관리 페이지
	@RequestMapping(value="/board_reportJSON.air", method= {RequestMethod.GET})
	public String board_reportJSON(HttpServletRequest req) {
		
		HashMap<String, String> paraMap = null;
		
		String currentShowPageNo = req.getParameter("currentShowPageNo");
		String searchWord = req.getParameter("searchWord");
		String searchType = req.getParameter("searchType");
		/*System.out.println(searchWord);
		System.out.println(searchType);*/
		if(searchWord == null) {
			searchWord = "";
		}
		
		if(!"rno".equals(searchType) &&
		   !"fk_userid".equals(searchType) &&
		   searchType == null) {
			searchType = "rno";
		}
		
		if(currentShowPageNo == null || "".equals(currentShowPageNo)) {
			currentShowPageNo = "1";
		}
		
		int sizePerPage = 5;	// 한 페이지당 보여줄 갯수
		
		int rno1 = Integer.parseInt(currentShowPageNo)*sizePerPage - (sizePerPage-1);	// 공식!!!
		int rno2 = Integer.parseInt(currentShowPageNo)*sizePerPage;	
		/*System.out.println(sizePerPage);
		System.out.println(rno1);
		System.out.println(rno2);*/
		
		
		paraMap = new HashMap<String, String>();
		paraMap.put("RNO1", String.valueOf(rno1));
		paraMap.put("RNO2", String.valueOf(rno2));

		paraMap.put("searchWord", searchWord);
		paraMap.put("searchType", searchType);
		
		List<ReportVO> reportvo = service.getReport(paraMap);
		// System.out.println(reportvo);
		
		JSONArray jsonArr = new JSONArray();
			
		for(int i=0; i<reportvo.size(); i++) {
			
			JSONObject jsonObj = new JSONObject();
			
			jsonObj.put("fk_userid", reportvo.get(i).getFk_userid());
			jsonObj.put("report_idx", reportvo.get(i).getReport_idx());
			jsonObj.put("reporttype", reportvo.get(i).getReporttype());
			jsonObj.put("report_content", reportvo.get(i).getReport_content());
			jsonObj.put("report_date", reportvo.get(i).getReport_date());
			jsonObj.put("report_status", reportvo.get(i).getReport_status());
			jsonObj.put("report_subject", reportvo.get(i).getReport_subject());
			jsonObj.put("rno", reportvo.get(i).getRno());
			jsonObj.put("commentcount", reportvo.get(i).getCommentCount());
			
			jsonArr.put(jsonObj);			
			
		}
		
		HttpSession session = req.getSession();
		session.setAttribute("readCountPermission", "yes");
		
		String str_json = jsonArr.toString();
		req.setAttribute("str_json", str_json);

		return "JSON";
	}
	
	@RequestMapping(value="/getTotalPagess.air", method={RequestMethod.GET})
	public String getCommentTotalPages(HttpServletRequest req) {
		
		HashMap<String, String> paraMap = null;
		
		String currentShowPageNo = req.getParameter("currentShowPageNo");
		String sizePerPage = "5";
		
		if(currentShowPageNo == null || "".equals(currentShowPageNo)) {
			currentShowPageNo = "1";
		}
		
		paraMap = new HashMap<String, String>();
		
		String searchWord = req.getParameter("searchWord");
		String searchType = req.getParameter("searchType");

		if(searchWord== null) {
			searchWord = "";
		}
		
		if(!"rno".equals(searchType) &&
		   !"fk_userid".equals(searchType) &&
		   searchType == null) {
			searchType = "rno";
		}
		
		paraMap.put("searchWord", searchWord);
		paraMap.put("searchType", searchType);
		paraMap.put("sizePerPage", sizePerPage);
		// System.out.println(searchWord);
		// System.out.println(searchType);
		
		
		int totalCount = service.getTotalCounts(paraMap);
		// 원글 글번호에 해당하는 댓글의 총 갯수를 알아온다.
		// System.out.println(totalCount);
		// System.out.println(sizePerPage);
		// 총 페이지 수 구하기
		int totalPage = (int)Math.ceil((double)totalCount/Integer.parseInt(sizePerPage));
		/*
		 	57.0(행갯수)/10(sizePerPage) ==> 5.7 ==> 6.0 ==> 6
		 	57.0(행갯수)/5(sizePerPage) ==> 11.4 ==> 12.0 ==> 12
		 	57.0(행갯수)/3(sizePerPage) ==> 19.0 ==> 19.0 ==> 19
		*/
		// System.out.println("총페이지 보여주세요:"+totalPage);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("totalPage", totalPage);
		
		String totalPageJSON = jsonObj.toString();
		
		req.setAttribute("totalPage", totalPageJSON);
		return "totalPageJSON";
		
	}
	
	// 게시판 글쓰기 페이지 요청
	@RequestMapping(value="/vanWrite.air", method= {RequestMethod.GET})
	public String vanWrite(HttpServletRequest req) {

		
		return "home/vanWrite.hometiles";
	}
	
	// 게시판 글쓰기 등록하기 완료
	@RequestMapping(value="/vanWriteEnd.air", method= {RequestMethod.POST})
	public String vanWriteEnd(HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
		
		int n = 0;
		if(loginuser != null) {

			String fk_userid = loginuser.getUserid();

			String reporttype = req.getParameter("reporttype");
			String report_subject = req.getParameter("report_subject");
			String report_content = req.getParameter("report_content");
			
			HashMap<String, String> paramap = new HashMap<String, String>();
			paramap.put("report_content", report_content);
			paramap.put("reporttype", reporttype);
			paramap.put("report_subject", report_subject);
			paramap.put("fk_userid", fk_userid);

			n = service.vanAdd(paramap);
		}

		String loc = "";
		if(n==1) {	
			loc = req.getContextPath()+"/board_report.air";
			// getContextPath() => /board
		}
		else {		
			loc = req.getContextPath()+"/vanWrite.air";
		}
		
		req.setAttribute("n", n);
		req.setAttribute("loc", loc);
		
		return "home/vanWriteEnd.hometiles";
	}
	
	// 쿠폰등록 페이지 요청
	@RequestMapping(value="/couponRegs.air", method= {RequestMethod.GET})
	public String couponReg(HttpServletRequest req) {

		HttpSession session = req.getSession();
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
		
		if(loginuser == null) {
			return "main/index";
		} 
		else {
			if(!loginuser.getUserid().equals("admin")) {
				return "main/index";
			}
			
			return "admin/couponRegs.admintiles";
		}

	}
	
	// 쿠폰등록 완료 요청
	@RequestMapping(value="/couponRegsEnd.air", method= {RequestMethod.POST})
	public String couponRegEnd(HttpServletRequest req) {
		
		String cpname = req.getParameter("cpname");
		String dcmoney = req.getParameter("dcmoney");
		
		String randomNo = "";
		for(int i=0; i<6; i++) {
			randomNo += Integer.toString(MyUtil.myRandom(1, 9));
		}

		HashMap<String, String> paramap = new HashMap<String, String>();
		paramap.put("randomNo", randomNo);
		paramap.put("cpname", cpname);
		paramap.put("dcmoney", dcmoney);
		
		int n = 0;
		n = service.cpAdd(paramap);
		
		String loc = "";
		if(n==1) {	
			loc = req.getContextPath()+"/couponRegs.air";
		}
		else {		
			loc = req.getContextPath()+"/admin.air";
		}
		
		req.setAttribute("n", n);
		req.setAttribute("loc", loc);
		
		return "admin/couponRegsEnd.admintiles";
	}
	
	@RequestMapping(value="/reportDetail.air", method= {RequestMethod.GET}) 
	public String reportDetail(HttpServletRequest req) {
		
		int report_idx = Integer.parseInt(req.getParameter("report_idx"));
		// System.out.println(report_idx);
		
		ReportVO reportvo = new ReportVO();
		List<CommentVO> commentList = new ArrayList<CommentVO>();
		
		HttpSession session = req.getSession();
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
		// System.out.println(loginuser+"1");
		
		String readCountPermission = (String)session.getAttribute("readCountPermission");
		// System.out.println(readCountPermission+"2");
		
		String userid = null;
		
		commentList = service.getComment(report_idx);
		
		if(readCountPermission != null && "yes".equals(readCountPermission)) {
			
			if(loginuser != null) {
				userid = loginuser.getUserid();
			}
			
			reportvo = service.getReportDetail(report_idx, userid);
			session.removeAttribute("readCountPermission");
		}
		else {
			reportvo = service.getReportDetailNo(report_idx);
			
		}
		
		req.setAttribute("reportvo", reportvo);	
		req.setAttribute("report_idx", report_idx);
		req.setAttribute("commentList", commentList);
		
		return "home/reportDetail.hometiles";
	}
	
	@RequestMapping(value="/deleteReport.air", method= {RequestMethod.GET})
	public String deleteReport(HttpServletRequest req) {
		
		int report_idx = Integer.parseInt(req.getParameter("report_idx"));
		// System.out.println(report_idx+"1");
		
		int n = service.deleteReport(report_idx);
		// System.out.println(userid);
		
		return "home/board_report.hometiles";
	}
	
	
	
	// ==== #스마트에디터1. 단일사진 파일업로드 ====
	@RequestMapping(value="/image/phothUpload.action", method= {RequestMethod.GET})
	public String photoUpload(PhotoVO photovo, HttpServletRequest req) {
		
		String callback = photovo.getCallback();
	    String callback_func = photovo.getCallback_func();
	    String file_result = "";
	    
		if(!photovo.getFiledata().isEmpty()) {
			// 파일이 존재한다라면
			
			/*
			   1. 사용자가 보낸 파일을 WAS(톰캣)의 특정 폴더에 저장해주어야 한다.
			   >>>> 파일이 업로드 되어질 특정 경로(폴더)지정해주기
			        우리는 WAS 의 webapp/resources/photo_upload 라는 폴더로 지정해준다.
			 */
			
			// WAS 의 webapp 의 절대경로를 알아와야 한다. 
			HttpSession session = req.getSession();
			String root = session.getServletContext().getRealPath("/"); 
			String path = root + "resources"+File.separator+"photo_upload";
			// path 가 첨부파일들을 저장할 WAS(톰캣)의 폴더가 된다. 
			
		//	System.out.println(">>>> 확인용 path ==> " + path); 
			// >>>> 확인용 path ==> C:\springworkspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\Board\resources\photo_upload
			
			// 2. 파일첨부를 위한 변수의 설정 및 값을 초기화한 후 파일올리기
			String newFilename = "";
			// WAS(톰캣) 디스크에 저장할 파일명 
			
			byte[] bytes = null;
			// 첨부파일을 WAS(톰캣) 디스크에 저장할때 사용되는 용도 
						
			try {
				bytes = photovo.getFiledata().getBytes(); 
				// getBytes()는 첨부된 파일을 바이트단위로 파일을 다 읽어오는 것이다. 
				/* 2-1. 첨부된 파일을 읽어오는 것
					    첨부한 파일이 강아지.png 이라면
					    이파일을 WAS(톰캣) 디스크에 저장시키기 위해
					    byte[] 타입으로 변경해서 받아들인다.
				*/
				// 2-2. 이제 파일올리기를 한다.
				String original_name = photovo.getFiledata().getOriginalFilename();
				//  photovo.getFiledata().getOriginalFilename() 은 첨부된 파일의 실제 파일명(문자열)을 얻어오는 것이다. 
				newFilename = fileManager.doFileUpload(bytes, original_name, path);
				
		//      System.out.println(">>>> 확인용 newFileName ==> " + newFileName); 
				
				int width = fileManager.getImageWidth(path+File.separator+newFilename);
		//		System.out.println("확인용 >>>>>>>> width : " + width);
				
				if(width > 600) {
					width = 600;
					newFilename = largeThumbnailManager.doCreateThumbnail(newFilename, path);
				}
		//		System.out.println("확인용 >>>>>>>> width : " + width);
				
				String CP = req.getContextPath();  // board
				file_result += "&bNewLine=true&sFileName="+newFilename+"&sWidth="+width+"&sFileURL="+CP+"/resources/photo_upload/"+newFilename; 
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else {
			// 파일이 존재하지 않는다라면
			file_result += "&errstr=error";
		}
	    
		return "redirect:" + callback + "?callback_func="+callback_func+file_result;
	}
	
	// ==== #스마트에디터4. 드래그앤드롭을 사용한 다중사진 파일업로드 ====
	@RequestMapping(value="/image/multiplePhotoUpload.air", method={RequestMethod.POST})
	public void multiplePhotoUpload(HttpServletRequest req, HttpServletResponse res) {
	    
		/*
		   1. 사용자가 보낸 파일을 WAS(톰캣)의 특정 폴더에 저장해주어야 한다.
		   >>>> 파일이 업로드 되어질 특정 경로(폴더)지정해주기
		        우리는 WAS 의 webapp/resources/photo_upload 라는 폴더로 지정해준다.
		 */
		
		// WAS 의 webapp 의 절대경로를 알아와야 한다. 
		HttpSession session = req.getSession();
		String root = session.getServletContext().getRealPath("/"); 
		String path = root + "resources"+File.separator+"photo_upload";
		// path 가 첨부파일들을 저장할 WAS(톰캣)의 폴더가 된다. 
		
	//	System.out.println(">>>> 확인용 path ==> " + path); 
		  
		
		File dir = new File(path);
		if(!dir.exists())
			dir.mkdirs();
		
		String strURL = "";
		
		try {
			if(!"OPTIONS".equals(req.getMethod().toUpperCase())) {
	    		String filename = req.getHeader("file-name"); //파일명을 받는다 - 일반 원본파일명
	    		
	    //		System.out.println(">>>> 확인용 filename ==> " + filename); 
	    		// >>>> 확인용 filename ==> berkelekle%ED%8A%B8%EB%9E%9C%EB%94%9405.jpg
	    		
	    		InputStream is = req.getInputStream();
	    	/*
	          	요청 헤더의 content-type이 application/json 이거나 multipart/form-data 형식일 때,
	          	혹은 이름 없이 값만 전달될 때 이 값은 요청 헤더가 아닌 바디를 통해 전달된다. 
	          	이러한 형태의 값을 'payload body'라고 하는데 요청 바디에 직접 쓰여진다 하여 'request body post data'라고도 한다.

               	서블릿에서 payload body는 Request.getParameter()가 아니라 
            	Request.getInputStream() 혹은 Request.getReader()를 통해 body를 직접 읽는 방식으로 가져온다. 	
	    	 */
	    		String newFilename = fileManager.doFileUpload(is, filename, path);
	    	
				int width = fileManager.getImageWidth(path+File.separator+newFilename);
       //		System.out.println(">>>> 확인용 width ==> " + width);
				
				if(width > 600) {
					width = 600;
					newFilename = largeThumbnailManager.doCreateThumbnail(newFilename, path);
				}
		//		System.out.println(">>>> 확인용 width ==> " + width);
				// >>>> 확인용 width ==> 600
				// >>>> 확인용 width ==> 121
	    	
				String CP = req.getContextPath(); // board
			
				strURL += "&bNewLine=true&sFileName="; 
            	strURL += newFilename;
            	strURL += "&sWidth="+width;
            	strURL += "&sFileURL="+CP+"/resources/photo_upload/"+newFilename;
	    	}
		
	    	/// 웹브라우저상에 사진 이미지를 쓰기 ///
			PrintWriter out = res.getWriter();
			out.print(strURL);
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}// end of void multiplePhotoUpload(HttpServletRequest req, HttpServletResponse res)---------------- 
	
	// 글 수정하기 페이지
	@RequestMapping(value="/boardEdit.air", method= {RequestMethod.GET})
	public String boardEdit(HttpServletRequest req) {

		HttpSession session = req.getSession();
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");

		if(loginuser != null) {
			int report_idx = Integer.parseInt(req.getParameter("report_idx"));
			
			// 글 정보 가져오기
			ReportVO reportvo = service.getReportDetailNo(report_idx);
			
			/*System.out.println(report_idx);
			System.out.println(report_subject);
			System.out.println(report_content);*/
			
			// 자신이 쓴 글이 아니라면
			if (!loginuser.getUserid().equals(reportvo.getFk_userid())) {
				String msg = "자신의 글만 수정이 가능합니다 ^^";
				String loc = "javascript:history.back();";
	
				req.setAttribute("msg", msg);
				req.setAttribute("loc", loc);
	
				return "msg";
			}
			// 자신이 쓴 글이라면
			else {
				req.setAttribute("report_idx", report_idx);
				req.setAttribute("reportvo", reportvo);
				
				return "home/boardEdit.hometiles";
			}
		}
		else {
			return "home/board_report.hometiles";
		}
		
	}
	
	// 글 수정하기 페이지 완료
	@RequestMapping(value="/boardEditEnd.air", method= {RequestMethod.POST})
	public String boardEditEnd(HttpServletRequest req) {
		
		String report_idx = req.getParameter("reportidx");
		// System.out.println("수정하기"+report_idx);
		
		String reporttype = req.getParameter("reporttype");
		String report_subject = req.getParameter("report_subject");
		String report_content = req.getParameter("report_content");
		
		HashMap<String, String> paramap = new HashMap<String, String>();
		paramap.put("report_content", report_content);
		paramap.put("reporttype", reporttype);
		paramap.put("report_subject", report_subject);
		paramap.put("report_idx", report_idx);

		service.writeEdit(paramap);
		
		return "home/board_report.hometiles";
	}
	
	// 댓글쓰기
	@RequestMapping(value = "/insertComment.air", method = {RequestMethod.POST})
	public String insertComment(HttpServletRequest req) {

		String report_idx = req.getParameter("reportidx");
		String parentSeq = req.getParameter("reportidx");
		String content = req.getParameter("content");
		String name = req.getParameter("name");
		String fk_userid = req.getParameter("fk_userid");
		
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("parentSeq", parentSeq);
		paraMap.put("report_idx", report_idx);
		paraMap.put("content", content);
		paraMap.put("name", name);
		paraMap.put("fk_userid", fk_userid);
		
		int n = service.insertComment(paraMap);
		
		if(n == 1) {
			service.addCommentCount(paraMap);
		}
		else {
			String msg = "글쓰기에 실패했습니다.";
			String loc = "javascript:history.back();";
			
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);

			return "msg";
		}
		
		return "home/board_report.hometiles";
		
	}
}
