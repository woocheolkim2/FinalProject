package com.spring.bnb.controller;
 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.spring.bnb.model.MemberVO;
import com.spring.bnb.model.ReviewVO;
import com.spring.bnb.service.InterSOService;
import com.spring.common.AES256;
import com.spring.common.FileManager;
import com.spring.common.GoogleMail;
import com.spring.common.MyUtil;

@Controller
public class SOController {
	@Autowired
	private InterSOService service;  
	
	@Autowired
	private AES256 aes;
	
	@Autowired
	private FileManager fileManager;
	
	@RequestMapping(value = "/myCoupon.air", method = RequestMethod.GET)
	public String requireMyPageLogin_myCoupon(HttpServletRequest req,HttpServletResponse res) {		
		HttpSession session = req.getSession();
		MemberVO mvoUser = (MemberVO)session.getAttribute("loginuser");
		String userid = mvoUser.getUserid();
		
		String date = MyUtil.getNowTime();				
			
		HashMap<String,String> paraMap = new HashMap<String,String>();
		paraMap.put("userid", userid);

		req.setAttribute("date", date);
		return "mypage/myCouponAjax.hometiles";			
		}
		
	// 보유쿠폰 리스트 가져오기
	@RequestMapping(value = "/possessionCoupon.air", method = {RequestMethod.POST,RequestMethod.GET})
	public String requireMyPageLogin_RequpossessionCoupon(HttpServletRequest req,HttpServletResponse res) {
		HttpSession session = req.getSession();
		MemberVO mvoUser = (MemberVO)session.getAttribute("loginuser");
		String userid = mvoUser.getUserid();
		
		
		String currentShowPageNo = req.getParameter("currentShowPageNo");
		
		int sizePerPage =10; 
		int startRno=Integer.parseInt(currentShowPageNo)*sizePerPage -(sizePerPage-1) ;  
		int endRno=Integer.parseInt(currentShowPageNo)*sizePerPage;

		HashMap<String,String> paraMap = new HashMap<String,String>();
		paraMap.put("userid", userid);
		paraMap.put("startRno", String.valueOf(startRno));
		paraMap.put("endRno",String.valueOf(endRno));
		List<HashMap<String,String>> myCoupon = service.getMyCoupon(paraMap);
		
		JSONArray jsonArr = new JSONArray(); // []
		
			if(myCoupon != null && myCoupon.size()>0) {
				
				for(HashMap<String,String> map : myCoupon) {
					JSONObject jsonobject = new JSONObject();
					
					jsonobject.put("cpcode",map.get("cpcode")); 
					jsonobject.put("userid",map.get("userid"));
					jsonobject.put("dcmoney",map.get("dcmoney"));
					jsonobject.put("cpexpire",map.get("cpexpire"));
					jsonobject.put("cpname",map.get("cpname"));
					jsonobject.put("usedate",map.get("usedate"));
					
					jsonArr.put(jsonobject);
				}
			}
			String str_json = jsonArr.toString();
			req.setAttribute("str_json", str_json);							
		return "JSON";
	}
	
	// 보유쿠폰 중 사용한 쿠폰 리스트 가져오기
	@RequestMapping(value="/useCoupon.air",method={RequestMethod.POST,RequestMethod.GET})
	public String requireMyPageLogin_useCoupon(HttpServletRequest req,HttpServletResponse res) {
		
		HttpSession session = req.getSession();
		MemberVO mvoUser = (MemberVO)session.getAttribute("loginuser");
		String userid = mvoUser.getUserid();
		
		String currentShowPageNo = req.getParameter("currentShowPageNo");
		int sizePerPage =10; 		
		int startRno=Integer.parseInt(currentShowPageNo)*sizePerPage -(sizePerPage-1) ;  
		int endRno=Integer.parseInt(currentShowPageNo)*sizePerPage;
		
		HashMap<String,String> paraMap = new HashMap<String,String>();
		paraMap.put("userid", userid);
		paraMap.put("startRno", String.valueOf(startRno));
		paraMap.put("endRno",String.valueOf(endRno));

		List<HashMap<String,String>> myUseCoupon = service.getMyUserCoupon(paraMap);
		 
		JSONArray jsonArr = new JSONArray();
		
		if(myUseCoupon  != null && myUseCoupon.size()>0) {
			for(HashMap<String,String> map : myUseCoupon) {
				JSONObject jobj = new JSONObject();
				
				jobj.put("cpcode", map.get("cpcode"));
				jobj.put("dcmoney", map.get("dcmoney"));
				jobj.put("cpexpire", map.get("cpexpire"));
				jobj.put("cpname", map.get("cpname"));
				jobj.put("usedate", map.get("usedate"));
				jsonArr.put(jobj);
			}
		}
		String str_json = jsonArr.toString();
		req.setAttribute("str_json", str_json);	
		return "JSON";
	}
	
	
	@RequestMapping(value="/getTotalPage.air",method= {RequestMethod.GET})
	public String requireMyPageLogin_getTotalPage(HttpServletRequest req,HttpServletResponse res){
		HttpSession session = req.getSession();
		MemberVO mvoUser = (MemberVO)session.getAttribute("loginuser");
		String userid = mvoUser.getUserid();
		
		int totalCount =0;
		String sizePerPage = req.getParameter("sizePerPage");
		String page = req.getParameter("page");
		
		JSONObject jobj = new JSONObject();
		int totalPage = 0;
		if("1".equals(page)) {
		
			totalCount = service.getTotalCount(userid); // 미사용 쿠폰  총 갯수
			totalPage = (int)Math.ceil((double)totalCount/Integer.parseInt(sizePerPage));
			
			jobj.put("totalPage", totalPage);
			
		}else {
			
			totalCount = service.getUseTotalCount(userid);
			totalPage = (int)Math.ceil((double)totalCount/Integer.parseInt(sizePerPage));
									
			jobj.put("totalPage", totalPage);
		}

		String str_json = jobj.toString();
		req.setAttribute("str_json", str_json);
		
		return "JSON";
		
	}
	
	
	@RequestMapping(value="/myEdit.air", method = RequestMethod.GET)
	public String requireMyPageLogin_myEditShowInfo(HttpServletRequest req, HttpServletResponse res) {
		
		HttpSession session = req.getSession();
		MemberVO loginMember = (MemberVO)session.getAttribute("loginuser");
				
		if(loginMember != null) {
			try {
				loginMember.setEmail(aes.decrypt(loginMember.getEmail()));
				loginMember.setPhone(aes.decrypt(loginMember.getPhone()));
			} catch (UnsupportedEncodingException | GeneralSecurityException e) {
				loginMember.setEmail(loginMember.getEmail());
				loginMember.setPhone(loginMember.getPhone());
			}
			int gender = loginMember.getGender();
			String str_gender = "";
			switch (gender) {
			case 1:
				str_gender = "Male";
				break;
			case 2:
				str_gender = "Female";
				break;

			default: str_gender = "Other";
				break;
			}
			String birthday= loginMember.getBirthday();
			ServletContext application = req.getServletContext();
			String realPath = application.getRealPath("/resources/images/profile");
			
			String birthdayYY = birthday.substring(0, 4);
			String birthdayMM = birthday.substring(5,7);
			String birthdayDD = birthday.substring(8,10);
			req.setAttribute("realPath", realPath);
			req.setAttribute("birthdayYY", birthdayYY);
			req.setAttribute("birthdayMM", birthdayMM);
			req.setAttribute("birthdayDD", birthdayDD);
			req.setAttribute("str_gender", str_gender);
			req.setAttribute("loginMember", loginMember);
				
		} 
		return "mypage/myEdit.hometiles";		  
	}


	@RequestMapping(value = "/emailChange.air", method = RequestMethod.POST)		
	public String emailChange(HttpServletRequest req,HttpServletResponse res) {
	
		HttpSession session = req.getSession();
		MemberVO loginMember = (MemberVO)session.getAttribute("loginuser");
		String userid = loginMember.getUserid();

		JSONObject jobj = new JSONObject();
		
		jobj.put("ok", "ok");
		String str_json = jobj.toString();
		req.setAttribute("str_json", str_json);
		return "JSON";
	}
	
	@RequestMapping(value = "/verifyCertification.air", method = RequestMethod.GET)		
	public String verifyCertification(HttpServletRequest req,HttpServletResponse res) {
		
	HttpSession session = req.getSession();
	MemberVO loginMember = (MemberVO)session.getAttribute("loginuser");
	String userid = loginMember.getUserid();
	
	String userCertificationCode = req.getParameter("userCertificationCode");
	String certificationCode = (String)session.getAttribute("certificationCode");
	String msg ="";
	String loc ="";
	
	if(certificationCode.equalsIgnoreCase(userCertificationCode)) {
		session.setAttribute("userid", userid);
		
		System.out.println("인증 성공 ! : "+userid);
		
		msg = "인증에 성공하였습니다";
		loc = req.getContextPath()+"/myEidt.air";

	}else {
		msg = "발급된 인증코드를 입력하세요";
		
		loc = req.getContextPath()+"/myEidt.air";
	}
	session.setAttribute("userid", userid);

	req.setAttribute("msg",msg);
	req.setAttribute("loc",loc);
	
	session.removeAttribute("certificationCode");

	JSONObject jobj = new JSONObject();
	
	jobj.put("ok", "ok");
	String str_json = jobj.toString();
	req.setAttribute("str_json", str_json);
	
	return "JSON";

}
	@RequestMapping(value = "/sendCode.air", method = RequestMethod.POST)
	public String sendCode(HttpServletRequest req,HttpServletResponse res) {
		
		String method = req.getMethod();
		HttpSession session = req.getSession();
		MemberVO loginMember = (MemberVO)session.getAttribute("loginuser");
		String userid = loginMember.getUserid();
		String email = req.getParameter("changeEmail");

		GoogleMail gmail = new GoogleMail();

		Random rnd = new Random();
		
		String certificationCode="";
				
		char randchar=' ';
		
		for(int i=0;i<5;i++) {
	
			randchar = (char)(rnd.nextInt('z'-'a'+1)+'a');
			
			certificationCode +=randchar;					
		}				
		int randint=0;
		for(int i=0;i<7;i++) {
			randint = rnd.nextInt(9-0+1)+0;
			certificationCode +=randint;
		}
					
			try {			
					gmail.sendmail(email, certificationCode);
								
					session.setAttribute("certificationCode", certificationCode);
			} catch (Exception e) {
					e.printStackTrace();
					
					req.setAttribute("sendFailmsg", "메일발송이 실패했습니다");
			}

			req.setAttribute("userid", userid);
			req.setAttribute("email", email);

			JSONObject jobj = new JSONObject();
			jobj.put("ok", "ok");
			String str_json = jobj.toString();
			req.setAttribute("str_json", str_json);
		req.setAttribute("method",method);
		return "JSON";

	}
	
	// 나의 정보 수정
	@RequestMapping(value = "/myEditEnd.air", method = RequestMethod.POST)
	public String myEditEnd(HttpServletRequest req,HttpServletResponse res,@RequestParam("file") MultipartFile multipartFile, MultipartRequest mtreq) throws FileNotFoundException, IOException {
			String method = req.getMethod();
			
			HttpSession session = req.getSession();
			MemberVO loginMember = (MemberVO)session.getAttribute("loginuser");
			String userid = loginMember.getUserid();		
		
			String email = req.getParameter("email");
			String phone = req.getParameter("phoneEdit");			
			String introduction = req.getParameter("introduction");
			String str_post = req.getParameter("post");
			int post = Integer.parseInt(str_post);
			String addr = req.getParameter("address");
			String detailAddr = req.getParameter("detailAddr");
			
			if(!"POST".equals(method)) {
				String msg="비정상적인 경로입니다.";
				String loc="javascript:history.back();";
				
				req.setAttribute("msg", msg);
				req.setAttribute("loc", loc);
				return "msg";
			}else {				
						String filename = "";
						if(!multipartFile.isEmpty()) {
							ServletContext application = req.getServletContext();
							String realPath = application.getRealPath("/resources/images/profile");
							System.out.println(realPath);
							filename = multipartFile.getOriginalFilename();
							
							int index = filename.lastIndexOf("\\");
							filename = filename.substring(index +1);
							
							File file = new File(realPath,filename);
							if(file.exists()) {
								filename = System.currentTimeMillis()+"_"+filename;
								file = new File(realPath,filename);
							}						
							
								IOUtils.copy(multipartFile.getInputStream(), new FileOutputStream(file));
								loginMember.setProfileimg(filename);
								
								req.setAttribute("realPath",realPath);
						}else {
							filename = req.getParameter("profileimg");
							
							loginMember.setProfileimg(filename);
							System.out.println("파일이 존재하지 않거나 파일 크기가 0입니다.");
						}				
						try {
							loginMember.setEmail(aes.encrypt(email));
							loginMember.setPhone(aes.encrypt(phone));
						} catch (GeneralSecurityException e) {
							System.out.println("암호화 실패!");
						}					
		
						loginMember.setIntroduction(introduction);
						loginMember.setPost(post);
						loginMember.setAddr(addr);
						loginMember.setDetailAddr(detailAddr);
						loginMember.setUserid(userid);
						
						service.memberUpdate(loginMember);
					}		
					
				String msg="회원정보 수정 성공!";
				String loc="/bnb/myEdit.air";
				req.setAttribute("msg", msg);
				req.setAttribute("loc", loc);
				
			return "msg"; 
		}
	
	@RequestMapping(value = "/phoneCheck.air", method = RequestMethod.GET)
	public String phoneCheck (HttpServletRequest req) {
		
		String phone = req.getParameter("phone");
		
		int check = service.getCheckPhone(phone);
		
		JSONObject jobj = new JSONObject();
		jobj.put("check",check);
		
		String str_json = jobj.toString();
		req.setAttribute("str_json", str_json);
		req.setAttribute("check", check);
		return "JSON";
	}
	
	@RequestMapping(value = "/myReservation.air", method = RequestMethod.GET)
	public String requireMyPageLogin_myReservation(HttpServletRequest req, HttpServletResponse res) {

		HttpSession session = req.getSession();
		MemberVO loginMember = (MemberVO)session.getAttribute("loginuser");
		String userid = loginMember.getUserid();

		List<HashMap<String,String>> memberResList = service.getMemberReservationList(userid);
		//회원 예약 내용 가져오기
		req.setAttribute("memberResList", memberResList);
		req.setAttribute("userid", userid);
		
		return "mypage/myReservation.hometiles";
	}
	
	@RequestMapping(value = "/myReservationCancelDetail.air", method = RequestMethod.GET)
	public String myReservationCancelDetail(HttpServletRequest req, HttpServletResponse res) {
	
		return "mypage/myReservationCancelDetail.hometiles";
	}
	
	// 투숙 완료예약 상세보기
	@RequestMapping(value = "/myReservationDetail.air", method = RequestMethod.GET)
	public String requireMyPageLogin_myReservationDetail(HttpServletRequest req, HttpServletResponse res) {
		HttpSession session = req.getSession();
		MemberVO loginMember = (MemberVO)session.getAttribute("loginuser");
		String userid = loginMember.getUserid();
		String rsvcode = req.getParameter("rsvcode");
		
		HashMap<String,String> paraMap = new HashMap<String,String>();
		paraMap.put("rsvcode", rsvcode);
		paraMap.put("userid",userid);
		
		
		HashMap<String,String> resDetail = service.getMemberReservationDetail(paraMap);
		
		String roomcode = resDetail.get("roomcode");
			
		List<HashMap<String,String>> bedtype = service.getBedType(roomcode);
		HashMap<String,String> buildtype = service.getBuildType(roomcode);
				
		String email="";
		String phone="";
		try {
			System.out.println("하하하");
			email = aes.decrypt(resDetail.get("email"));
			phone = aes.decrypt(resDetail.get("phone"));
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			System.out.println("예약 상세보기  이메일/전화번호 복호화 실패!");
		}		 
		
		resDetail.put("email", email);
		resDetail.put("phone", phone);
		System.out.println(resDetail.get("rsvcode"));
		req.setAttribute("buildtype",buildtype);
		req.setAttribute("bedtype", bedtype);		
		req.setAttribute("resDetail", resDetail);
		return "mypage/myReservationDetail.hometiles";
	}
	// 투숙 예정 예약 상세보기
	@RequestMapping(value = "/myReservationScheduleDetail.air", method = RequestMethod.GET)
	public String requireMyPageLogin_myReservationScheduleDetail(HttpServletRequest req, HttpServletResponse res) {
		//	*** 아이디 정보 가져오기 ***
		HttpSession session = req.getSession();
		MemberVO loginMember = (MemberVO)session.getAttribute("loginuser");
		String userid = loginMember.getUserid();
		
		String rsvcode = req.getParameter("rsvcode");		
			
		HashMap<String,String> paraMap = new HashMap<String,String>();
		paraMap.put("userid", userid);
		paraMap.put("rsvcode", rsvcode);
		
		HashMap<String, String> myReservationScheduleDetail = service.getMemberReservationDetail(paraMap);
		//bed type 가져오기
		String roomcode = myReservationScheduleDetail.get("roomcode");
		
		List<HashMap<String,String>> bedtype = service.getBedType(roomcode);
		HashMap<String,String> buildtype = service.getBuildType(roomcode);

		String email="";
		String phone="";
		String rsv_email="";
		String rsv_phone="";
		try {
			
			email = aes.decrypt(myReservationScheduleDetail.get("email") );
			phone = aes.decrypt(myReservationScheduleDetail.get("phone"));
			rsv_email = aes.decrypt(myReservationScheduleDetail.get("rsv_email"));
			rsv_phone = aes.decrypt(myReservationScheduleDetail.get("rsv_phone"));
			myReservationScheduleDetail.put("email", email);
			myReservationScheduleDetail.put("phone", phone);
			myReservationScheduleDetail.put("rsv_phone", rsv_phone);
			myReservationScheduleDetail.put("rsv_email", rsv_email);
			
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			System.out.println("예약 상세보기  이메일/전화번호 복호화 실패!");
		}		 		
		req.setAttribute("buildtype", buildtype);
		req.setAttribute("bedtype", bedtype);
		req.setAttribute("myRsvDetail", myReservationScheduleDetail);
		
	return "mypage/myReservationScheduleDetail.hometiles";
}
	@RequestMapping(value="messageSend.air", method = RequestMethod.GET)
	public String messageSend(HttpServletRequest req, HttpServletResponse res) {
		HttpSession session = req.getSession();
		MemberVO loginMember = (MemberVO)session.getAttribute("loginuser");
		String userid = loginMember.getUserid();
		String rsvcode = req.getParameter("rsvcode");
		String roomcode = req.getParameter("roomcode");
		
		return "mypage/messageSend.notiles";
	}
	// 투숙 예약 취소하기
	@RequestMapping(value = "/goCancel.air", method = {RequestMethod.POST, RequestMethod.GET})
	public String requireMyPageLogin_myReservationScheduleCancel(HttpServletRequest req, HttpServletResponse res) {
		HttpSession session = req.getSession();
		MemberVO loginMember = (MemberVO)session.getAttribute("loginuser");
		String userid = loginMember.getUserid();
		String rsvcode = req.getParameter("rsvcode");
		
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("userid", userid);
		map.put("rsvcode",rsvcode);
		
		int n = service.goCancelMyRsv(map);
		if(n==1) {
			String msg="예약이 취소되었습니다!";
			String loc="/bnb/myReservation.air";
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			return "msg";
		}else {
			String msg="예약이 취소가 실패되었습니다!";
			String loc="javascript:history.back();";
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			return "msg";			
		}
				
	}
	// 나의 후기 보기
	@RequestMapping(value = "/review.air", method = RequestMethod.GET)
	public String requireMyPageLogin_review(HttpServletRequest req, HttpServletResponse res) {
		//	*** 아이디 정보 가져오기 ***
		HttpSession session = req.getSession();
		MemberVO loginMember = (MemberVO)session.getAttribute("loginuser");
		String userid = loginMember.getUserid();
				
		// *** 나에게 쓴 후기 ***
		
		String str_currnetShowPageNo = req.getParameter("currnetShowPageNo");
		
		int totalCount = 0; // 조건에 맞는 총 게시물 건수
		int sizePerPage = 10; // 한페이지당 보여줄 게시물 건수
		int currentShowPageNo =0; // 현재 보여주는 페이지 번호로서, 초기치로는 1페이지로 한다.
		int totalPage = 0; // 총 페이지 수 (웹 브라우저 상의 총 페이지 갯수)
		int startRno = 0; // 시작 행 번호
		int endRno = 0; // 끝 행 번호
		int blockSize = 10; // 페이지 바 에 보여줄 페이지의 갯수 

		totalCount = service.getTotalHostReviewCount(userid); // 총 게시물 갯수 
		totalPage = (int)Math.ceil((double)totalCount/sizePerPage); // 총 페이지 수 
			
		if(str_currnetShowPageNo == null) { 
			currentShowPageNo =1;
		}else { 
			try {
					currentShowPageNo = Integer.parseInt(str_currnetShowPageNo);
					if(currentShowPageNo <1 || currentShowPageNo>totalPage) {
						currentShowPageNo =1;	
					}
					
				} catch (NumberFormatException e) {
					currentShowPageNo =1;
				}
			}
		HashMap<String,String> paraMap = new HashMap<String,String>();
		startRno = ((currentShowPageNo - 1)*sizePerPage)+1;
		endRno = startRno+(sizePerPage-1);
		
		paraMap.put("startRno", String.valueOf(startRno));
		paraMap.put("endRno",String.valueOf(endRno));
		paraMap.put("userid",userid);
		
		List<HashMap<String,String>> myReadReview = service.getHostReview(paraMap);
		
		String pageBar = "<ul>";
		pageBar += MyUtil.getPageBar(sizePerPage, blockSize, totalPage, currentShowPageNo, "review.air");
		pageBar += "</ul>";
				
		//	*** 내가 쓴 후기 ***
		List<ReviewVO> myWriteReview = service.getMyReview(userid);
		
		// *** 작성해야 할 후기 ***
		// *** 후기 없는 나의 예약 코드 받아오기 ***
		List<HashMap<String,String>> myRsvList= service.getMyRsvCode(userid);
		
		req.setAttribute("totalCount", totalCount);
		req.setAttribute("pageBar", pageBar);
		req.setAttribute("myRsvList", myRsvList);
		req.setAttribute("myReadReview", myReadReview);
		req.setAttribute("myWriteReview", myWriteReview);
		return "mypage/review.hometiles";
	}
	
	// 나의 쿠폰 등록하기 
	@RequestMapping(value = "/couponReg.air", method = {RequestMethod.GET,RequestMethod.POST})
	public String couponReg(HttpServletRequest req, HttpServletResponse res) {
		
		return "mypage/couponReg.notiles";
	}
	
	@RequestMapping(value = "/couponRegEnd.air", method = {RequestMethod.GET,RequestMethod.POST})
	
	
	public String couponRegEnd(HttpServletRequest req, HttpServletResponse res) {
		//	*** 아이디 정보 가져오기 ***
		HttpSession session = req.getSession();
		MemberVO loginMember = (MemberVO)session.getAttribute("loginuser");
		String userid = loginMember.getUserid();
		String coupon = req.getParameter("coupon");
		
		//*** 쿠폰 정보 존재 확인 ***	
		
		if(coupon == null || ("").equals(coupon.trim())) {
			
			String msg = "쿠폰번호를 등록해 주세요!";
			String loc = "javascript:history.back()";
			
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			return "msg";
		}else {
			
			int n= service.getCoupon(coupon);
					
			if(n==1) {
				n = 1;
				
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("userid", userid);
				map.put("coupon",coupon);
				
				// *** 쿠폰 등록하기 ***
				int couponAdd = service.addCoupon(map);
				req.setAttribute("couponAdd", couponAdd);				
			}else {
				n=0;
			}
			req.setAttribute("n", n);
			return "mypage/couponRegEnd.notiles";
		}
				
	}
	
	// 나의 예약 지도 보기 
	@RequestMapping(value = "/myReservationMAP.air", method = RequestMethod.GET)
	public String myReservationMAP(HttpServletRequest req, HttpServletResponse res) {
		HttpSession session = req.getSession();
		MemberVO memberLogin= (MemberVO)session.getAttribute("loginuser");
		String userid = memberLogin.getUserid();
		String rsvcode = req.getParameter("rsvcode");
		System.out.println("지도"+rsvcode);
		HashMap<String,String>  paraMap = new HashMap<String,String>();		
		paraMap.put("rsvcode", rsvcode);
		paraMap.put("userid", userid);
		
		HashMap<String,String> rsvLocation = service.getMap(paraMap);
		//HashMap<String,String> rsvLocation = service.getMemberReservationDetail(paraMap);
		req.setAttribute("rsvLocation", rsvLocation);
		return "mypage/myReservationMAP.notiles";
	}
	
/*	@RequestMapping(value = "/ddd.air", method = RequestMethod.GET)
	public String ddd(HttpServletRequest req, HttpServletResponse res) {

		return "hostAd/ddd.hosttiles";
	}
	
	@RequestMapping(value = "/dddd.air", method = RequestMethod.GET)
	public String dddd(HttpServletRequest req, HttpServletResponse res) {

		return "mypage/dddd.hometiles";
	}*/
	
	
}
