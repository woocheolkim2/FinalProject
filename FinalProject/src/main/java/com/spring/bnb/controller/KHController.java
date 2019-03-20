package com.spring.bnb.controller;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.bnb.model.MemberVO;
import com.spring.bnb.model.ReservationVO;
import com.spring.bnb.model.RoomVO;
import com.spring.bnb.service.InterKHService;
import com.spring.common.AES256;
import com.spring.common.KHGoogleMail;


@Controller
public class KHController {
	
	@Autowired
	private InterKHService service;
	
	@Autowired
	private AES256 aes;

	// ***** 숙소이용규칙 확인하기 (예약) **** //
	@RequestMapping(value="/reservationCheck.air", method= {RequestMethod.GET})
	public String requireLogin_reservationCheck (HttpServletRequest req,HttpServletResponse res) throws ParseException {
		
		HttpSession session = req.getSession();
		session.removeAttribute("code");
		//===================================================
		String roomcode = req.getParameter("roomcode");//숙소코드
		String guestCount = req.getParameter("guestCount");//게스트인원
		String babyCount = req.getParameter("babyCount");//유아인원
		String rsv_checkInDate = req.getParameter("rsv_checkInDate");//체크인날짜
		String rsv_checkOutDate = req.getParameter("rsv_checkOutDate");//체크아웃날짜
		
		//받아온 숙소코드를 map에 넣어서 넘겨준다.
		//HashMap으로 안해도 되지만 한번한거 변경하기 귀찮아서 안바꿈.
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("roomcode", roomcode);
		
		// *** 숙소 정보 뽑아오는 메소드 *** //
		RoomVO oneRoom = service.getOneRoomInfo(map);
		
		// *** 리뷰 갯수 가져오기 *** //
		int reviewCount = service.getReviewCount(map);
		
		// *** 평균 요금 구하는 메소드 *** //
		int avgPrice = service.getAvgPrice();
		
		// *** session에 객체를 넣은 이유 => 페이지 이동시에 req로 하면 한 페이지 밖에 정보이동이 안되기때문에
								  // session에 저장을 하여 넘겨주었다.
		
		session.setAttribute("checkin", rsv_checkInDate);
		session.setAttribute("checkout", rsv_checkOutDate);
		
		session.setAttribute("guestCount", guestCount);
		session.setAttribute("babyCount", babyCount);
		session.setAttribute("reviewCount", reviewCount);
		session.setAttribute("oneRoom", oneRoom);
		session.setAttribute("avgPrice", avgPrice);
	
		
		return "reservationAndPay/reservationCheck.notiles";
	}
	
	
	
	// ***** 일행 확인하기 (예약) ***** //
	@RequestMapping(value="/reservationCheckPeople.air", method= {RequestMethod.POST})
	public String reservationCheckPeople (HttpServletRequest req,HttpSession session)  {
		
		String day_between = req.getParameter("day_between");//숙박일수
		String chekin = req.getParameter("chekin");//체크인날짜
		String chekout = req.getParameter("chekout");//체크아웃날짜
		
		session.setAttribute("day_between", day_between);
		session.setAttribute("chekin", chekin);
		session.setAttribute("chekout", chekout);
		
		return "reservationAndPay/reservationCheckPeople.notiles";
	}

	
	// ***** 예약 확인 및 결제하기 (예약) ***** //
	@RequestMapping(value="/reservationCheckAndPay.air", method= {RequestMethod.POST})
	public String reservationCheckAndPay (HttpServletRequest req,HttpSession session) throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException{
		
		String babycount = req.getParameter("babycount");//최종적으로 변경된 유아인원
		String guestcount = req.getParameter("guestcount");//최종적으로 변경된 게스트 인원
		String totalprice = req.getParameter("totalprice");//총금액
		String message = req.getParameter("message");//호스트에게 남기는 메시지
		String totalpeople = req.getParameter("totalpeople");//최종인원
		String price = req.getParameter("price");//숙박일수*1박숙소 금액
		
		System.out.println(babycount);
		
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
		String code = (String)session.getAttribute("code");
		
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("userid", loginuser.getUserid());
		map.put("code",code);
		
		int disCountMoney = 0;
		if(code == null || "".equals(code)) {
			disCountMoney = 0;
		}
		else {
			// *** 쿠폰 사용시 할인금액 가져오기 *** //
			disCountMoney = service.getUseMyCopon(map);
			
		}
		
		//암호화된 이메일과 번호를 복호화 해서 보여줌.
		String email = aes.decrypt(loginuser.getEmail());
		String phone = aes.decrypt(loginuser.getPhone());
		
		session.setAttribute("disCountMoney", disCountMoney);//할인금액
		session.setAttribute("phone", phone);
		session.setAttribute("email", email);
		session.setAttribute("babycount", babycount);
		session.setAttribute("guestcount", guestcount);
		session.setAttribute("totalprice", totalprice);
		session.setAttribute("message", message);
		session.setAttribute("totalpeople", totalpeople);
		session.setAttribute("price", price);
		
		return "reservationAndPay/reservationCheckAndPay.notiles";
	}
	
	
	// ***** 예약 확인 및 결제하기 (결제 창 띄우기) ***** //
	@RequestMapping(value="/paymentGateway.air", method= {RequestMethod.POST})
	public String paymentGateway(HttpServletRequest req,HttpSession session) {
		
		String totalprice = req.getParameter("totalprice");//총 결제금액
		String reName = req.getParameter("name");
		String rePhone = req.getParameter("phone");
		String reEmail = req.getParameter("email");
		
		RoomVO oneroom = (RoomVO)session.getAttribute("oneRoom");
		
		String roomname = oneroom.getRoomName();//결제창에 예약하는 숙소이름을 보여주기 위해 RoomVO에서 가져옴
		
		String roomname1 = roomname.substring(0,2);
		
		session.setAttribute("reservationPermission", "yes");
		//예약확인페이지에서 새로고침시 
		//예약테이블에 계속 insert되는 것을 막기위한 session.
		
		session.setAttribute("reName", reName);
		session.setAttribute("rePhone", rePhone);
		session.setAttribute("reEmail", reEmail);
		
		session.setAttribute("totalprice", totalprice);
		session.setAttribute("roomname1", roomname1);
		session.setAttribute("roomname", roomname);
		
		return "paymentGateway";
	}
	
	
	
	// ***** 예약 확인 및 결제하기 (결제성공) ***** //
	@RequestMapping(value="/reservationFinalConfirm.air", method= {RequestMethod.GET})
	public String reservationFinalConfirm (HttpServletRequest req,HttpSession session) throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {
		
		
		//이전페이지에서 session에 저장한 reservationPermission을 가져온다.
		String reservationPermission = (String)session.getAttribute("reservationPermission");
		String revcode = "";
		
		if(reservationPermission != null && "yes".equals(reservationPermission)) {
					
			RoomVO oneroom = (RoomVO)session.getAttribute("oneRoom");
			MemberVO loginuser  = (MemberVO)session.getAttribute("loginuser");
			
			revcode = getOdrCode();//예약 코드 
			String roomcode = oneroom.getRoomcode();//룸 코드
			String my_userid = loginuser.getUserid();//예약자 아이디
			String guestcount = (String)session.getAttribute("guestcount");//게스트인원
			String babycount = (String)session.getAttribute("babycount");//유아인원
			
			String reName = (String)session.getAttribute("reName");//예약자 이름
			String reEmail = (String)session.getAttribute("reEmail");//예약자 번호
			String rePhone = (String)session.getAttribute("rePhone");//예약자 이메일
			
			String totalprice = (String)session.getAttribute("totalprice");//최종금액
			String message = (String)session.getAttribute("message");//호스트에게 보내는 메시지
			String chekin = (String)session.getAttribute("chekin");//체크인 날짜
			String chekout = (String)session.getAttribute("chekout");//체크아웃 날짜
			String day_between = (String)session.getAttribute("day_between");//숙박일수
			
			HashMap<String,Object> map = new HashMap<String,Object>();
			
			map.put("revcode", revcode);
			map.put("roomcode", roomcode);
			map.put("my_userid", my_userid);
			map.put("guestCount", Integer.parseInt(guestcount));
			//데이터베이스에서 게스트인원 타입이 number이기때문에 integer로 타입 변경
			map.put("babyCount", Integer.parseInt(babycount));
			//데이터베이스에서 유아인원 타입이 number이기때문에 integer로 타입 변경
			map.put("username", reName);
			map.put("phone", aes.encrypt(rePhone));
			map.put("email", aes.encrypt(reEmail));
			
			map.put("checkin", chekin);
			map.put("checkout", chekout);
			map.put("totalprice", Integer.parseInt(totalprice));
			map.put("message", message);
		
			// **** 예약테이블에 insert하는 메소드 **** //
			int finalReservation = service.insertReservation(map);
			
			if(finalReservation == 1) { //결제 완료시 메일보내기 
				try {
					KHGoogleMail gmail = new KHGoogleMail();
					StringBuilder sb = new StringBuilder();
					
					// *** 예약자 정보 가져오기 *** //
					ReservationVO rvo = service.getOneReserve(map);

					sb.append("<img src='https://ci4.googleusercontent.com/proxy/ycoe9yJWtDXnJKHImcia25D30dkyKMUWkev09437rXQjdXs46I5wDsuZuF7jS8OLh8gCCMZeK5PMFzSb8U-6RXj5c2zjwG0sD2DwMJeD2SrOGQzWpsfp52Qg3X29kLGdKZGDzG2YUO2UgNYqbNgRSwFJug=s0-d-e1-ft#https://a1.muscache.com/airbnb/rookery/dls/logo_standard_2x-c0ed1450d18490825bc37dd6cb23e5c5.png' onClick='javascript:location.href="+req.getContextPath()+"/index.air' style='cursor:pointer; width:100px;'/><br><br>");
					sb.append("<h1>비앤비 에어 영수증</h1><br>");
					sb.append("<span style='font-size:12pt; margin-bottom:5%; '><strong>영수증 ID</strong>: "+rvo.getRsvcode()+" , "+rvo.getPaydate()+"</span><br>");
					sb.append("<hr style='border: 1px solid lightgray;'><br>");
					sb.append("<h1>"+oneroom.getRoomSigungu()+"</h1><br>");
					sb.append("<img src='"+oneroom.getRoomMainImg()+"' style='width:150px;'/><br>");
					sb.append("<span style='font-size:12pt; margin-bottom:5%;'>"+oneroom.getRoomSigungu()+"에서 "+day_between+"박 </span><br>");
					sb.append("<hr style='border: 1px solid lightgray;'><br>");
					sb.append("<span style='font-size:12pt;'>"+chekin+"  → "+chekout+"</span><br>");
					sb.append("<span style='font-size:12pt; margin-bottom:5%;'>"+oneroom.getRoomType_name()+". 게스트 "+(Integer.parseInt(guestcount)+Integer.parseInt(babycount))+""+"명</span><br>");
					sb.append("<hr style='border: 1px solid lightgray;'><br>");
					sb.append("<h1>요금내역</h1>");
					sb.append("<h2>총 금액 (KRW) </h2>"+"<span style='font-size:12pt;'>₩"+totalprice+"</span><br>");
					sb.append("<hr style='border: 1px solid lightgray;'><br>");
					sb.append("<button type='button' style='width:50%; font-size:15pt; margin-bottom:3%; background-color:tomato;'><span style='color:white;'>영수증 인쇄하기</span></button><br>");
					sb.append("<hr style='border: 1px solid lightgray;'><br><p style='font-size:12pt;'><strong>BnbAir Payments UK Ltd.</strong><br>\r\n" + 
							"\r\n" + 
							"BnbAir Payments는 호스트의 대금 수령 한정 대리인입니다. 즉, 회원님이 BnbAir Payments를 통해 결제하면 호스트에 대한 지급 의무를 다하게 됩니다. "
							+ "(1) 호스트의 환불 정책(숙소 페이지에서 확인 가능) 또는 "
							+ "(2) 비앤비에어의 게스트 환불 정책 약관(<span style='color:#00a699;'>www.airbnb.com/terms</span>) "
							+ "궁금하신 사항이 있거나 불만 사항을 제기하려면 BnbAir Payments UK Ltd.(전화번호: +44 203 318 1111)에 문의하세요.</p>");
					
					String emailContents = sb.toString();
				
					gmail.sendmail_OrderFinish(reEmail, reName, emailContents);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			session.removeAttribute("reservationPermission");
			//insert를 하고 저장된 세션을 삭제한다.
			//삭제가 되면 새로고침시 insert가 되지 않고, 해당 페이지만 보여준다.
			
		}//end of if-------------
		
		Calendar cal = Calendar.getInstance();
	    //예약 완료한 오늘날짜를 Calendar객체를 사용해서 가져온다.
		//년원일로 나눈 이유는 나눠서 보여주기 위해서이다.
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		req.setAttribute("year", year);
		req.setAttribute("month", month);
		req.setAttribute("day", day);
		req.setAttribute("revcode", revcode);//예약코드
		
		return "reservationAndPay/reservationFinalConfirm.notiles";
	}
	
	// *** 예약 코드 만드는 메소드 *** //
	private String getOdrCode() {
	      
	      Calendar cal = Calendar.getInstance();
	      
	      int year = cal.get(Calendar.YEAR);//년
	      int month = cal.get(Calendar.MONTH)+1;//월
	      int day = cal.get(Calendar.DAY_OF_MONTH);//일
	      
	      // **** 시퀀스 번호 채번해오기 **** //
	      int ordcode = service.getReservCode();
	      
	      return "O"+year+month+day+"-"+ordcode;
   }
	// *** 나의쿠폰 팝업창으로 띄어서 보기 *** //
	@RequestMapping(value="/mycoupon.air", method = {RequestMethod.GET})
	public String mycoupon (HttpServletRequest req, HttpSession session) {
		
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
		String userid = loginuser.getUserid();
		
		//나의 쿠폰 보기
		List<HashMap<String,Object>> mycoupon = service.getmyCoupon(userid);
		req.setAttribute("mycoupon", mycoupon);
		
		return "reservationAndPay/mycoupon.notiles";
	}
	
	// *** 쿠폰 사용하는 메소드 *** //
	@RequestMapping(value="/useMyCoupon.air", method = {RequestMethod.POST})
	public String useCoupon(HttpServletRequest req,HttpSession session){
		
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
		String userid = loginuser.getUserid();
		String code = req.getParameter("code");
		session.setAttribute("code", code);
		
		//System.out.println(userid);
		HashMap<String,String> cpmap = new HashMap<String,String>();
		cpmap.put("userid", userid);
		cpmap.put("code", code);
		
		// 쿠폰 사용하는 메소드 //
		int n = service.useMyCoupon(cpmap);
		
		JSONObject json = new JSONObject();
		json.put("cinsert", n);
		String s_json = json.toString();
		
		req.setAttribute("s_json", s_json);
		
		return "reservationAndPay/couponJson.notiles";
	}
	
	// *** 쿠폰 사용취소하는 메소드 *** //
	@RequestMapping(value="/NouseMyCoupon.air", method = {RequestMethod.POST})
	public String NouseMyCoupon(HttpServletRequest req,HttpSession session){
		
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
		String userid = loginuser.getUserid();
		String code = req.getParameter("code");
		session.setAttribute("code", code);
		
		//System.out.println(userid);
		HashMap<String,String> cpmap = new HashMap<String,String>();
		cpmap.put("userid", userid);
		cpmap.put("code", code);
		
		// 쿠폰 사용취소하는 메소드 //
		int n = service.NouseMyCoupon(cpmap);
		
		JSONObject json = new JSONObject();
		json.put("cinsert", n);
		String s_json = json.toString();
		
		req.setAttribute("s_json", s_json);
		session.removeAttribute("code");
		
		return "reservationAndPay/NocouponJson.notiles";
	}
	

}
