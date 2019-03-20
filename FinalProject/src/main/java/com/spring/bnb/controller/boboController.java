package com.spring.bnb.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.spring.bnb.model.MemberVO;
import com.spring.bnb.model.RoomVO;
import com.spring.bnb.service.boboService;
import com.spring.common.FileManager;

@Controller
public class boboController {
   
   @Autowired
   private boboService service;
   
   @Autowired
   private FileManager fileManager;
   
   @RequestMapping(value="/roomstep1.air", method={RequestMethod.GET})
   public String roomstep1(HttpServletRequest req) {
      
	  HttpSession session = req.getSession();
	  MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
	  
	  if(loginuser == null) {
		  return "become-host/error";
	  }
	   
      /*// 세션에 입력값을 넣어서 한번에 insert하려고 RoomVO를 세션값에 저장
      HttpSession session = req.getSession();      
      RoomVO roomvo = new RoomVO();
      session.setAttribute("roomvo", roomvo);*/
      
      return "become-host/room-step1";
   }
   
   @RequestMapping(value="/roomstep1page.air", method={RequestMethod.GET})
   public String roomstep1page(HttpServletRequest req) {
      
	  HttpSession session = req.getSession();
	  MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
	  
	  if(loginuser == null) {
		  return "become-host/error";
	  }	   
	   
      List<HashMap<String, String>> buildTypeList = service.selectbuildType();// 건물유형 가져오기
      req.setAttribute("buildTypeList", buildTypeList);      

      List<String> roomtype = service.selectroomtype();// 숙소유형 가져오기
      req.setAttribute("roomtype", roomtype);
      
      List<String> options = service.selectoptions();// 옵션 가져오기
      req.setAttribute("options", options);
      
      List<String> rule = service.selectrule();// 이용규칙 가져오기
      req.setAttribute("rule", rule);
      
      return "become-host/room-step1-page";
   }
   
   @RequestMapping(value="/roomtypeJSON.air", method={RequestMethod.GET})
   public String roomtypeJSON(HttpServletRequest req) {
      
      String buildType = req.getParameter("buildType");

      JSONArray jsonArr = new JSONArray();
      
      if(buildType != null && !buildType.trim().isEmpty()) {
         List<HashMap<String,String>> buildTypeList = service.selectbuildTypedetail(buildType);// 건물세부유형 가져오기
         
         if(buildTypeList != null && buildTypeList.size() > 0) {
            for(HashMap<String,String> map : buildTypeList) {
               JSONObject jsonObj = new JSONObject();
               
               jsonObj.put("BUILDTYPE_DETAIL_IDX", map.get("BUILDTYPE_DETAIL_IDX")); // 키값, xml에서 읽어온 키값
               jsonObj.put("BUILDTYPE_DETAIL_NAME", map.get("BUILDTYPE_DETAIL_NAME"));
               
               jsonArr.put(jsonObj);
      
            }// end of for
         }// end of if
         
      }
      
      String str_json = jsonArr.toString();
      req.setAttribute("str_json", str_json);
      
      return "JSON";      
   }   
   
   // 침실과 침대 갯수 가져오기
   @RequestMapping(value = "/bedroom.air", method = RequestMethod.POST)
   public String bedroom(HttpServletRequest req) {
      
      String bedroomInfo = req.getParameter("bedroomInfo");
      
      HttpSession session = req.getSession();
      session.setAttribute("bedroomInfo", bedroomInfo);
   
      JSONObject json = new JSONObject();
      //json.put("n", n);
      req.setAttribute("str_json", json.toString()); 
      return "JSON";
   }
   
   // 주소를 받아와서 지도에 값을 넣기위해서 씀
   @RequestMapping(value="/mapJSON.air", method={RequestMethod.GET})
   public String mapJSON(HttpServletRequest req) {
      
      String address = req.getParameter("address");
      JSONArray jsonArr = new JSONArray();
      
      jsonArr.put(address);
      
      String str_json = jsonArr.toString();
      req.setAttribute("str_json", str_json);
      
      return "JSON";      
   }   
   
   @RequestMapping(value="/roomstep2.air", method={RequestMethod.GET})
   public String roomstep2(RoomVO roomvo, HttpServletRequest req) {
      
      HttpSession session = req.getSession();   
	  MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
	  
	  if(loginuser == null) {
		  return "become-host/error";
	  }
      
      String[] optionArr = req.getParameterValues("optionchk");
      
      if(optionArr != null) {
         // 배열형태의 옵션들을 리스트형태로 바꿈
         List<String> optionchk = new ArrayList<>(Arrays.asList(optionArr));   
         roomvo.setMyoption(optionchk);
/*         for(String val : optionchk) {
            System.out.println(val);
         }*/
      }
      
      String[] ruleArr = req.getParameterValues("rulechk");
      if(ruleArr != null) {
         // 배열형태의 옵션들을 리스트형태로 바꿈
         List<String> rulechk = new ArrayList<>(Arrays.asList(ruleArr));
         roomvo.setMyrule(rulechk);
/*         for(String val : rulechk) {
            System.out.println(val);
         }*/
      }
      
      // roomvo에 들어간 값들을 세션에 저장한다 
      session.setAttribute("roomvo", roomvo);
            
      return "become-host/room-step2";
   }
   
   @RequestMapping(value="/roomstep2page.air", method={RequestMethod.GET})
   public String roomstep2page(HttpServletRequest req) {
      
	  HttpSession session = req.getSession();
	  MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
	  
	  if(loginuser == null) {
		  return "become-host/error";
	  }
	   
      return "become-host/room-step2-page";
   }
   
   // 이미지 파일 가져오기
   @RequestMapping(value="/fileListJOSN.air", method={RequestMethod.POST})
   public String fileListJOSN(MultipartHttpServletRequest req) throws Exception {
      
      List<MultipartFile> fileList = req.getFiles("file");
      
      HttpSession session = req.getSession();
      RoomVO roomvo = (RoomVO) session.getAttribute("roomvo");
      if(fileList != null) {  // 이미지첨부 다중파일을 받아왔다라면 
         // 이미지첨부 다중파일을 업로드할 WAS 의 webapp 의 절대경로를 알아와야 한다. 
         String root = req.getServletContext().getRealPath("/");
           //System.out.println(root);
           String realPath = root+"resources"+File.separator+"images"+File.separator+"becomehost";
           //System.out.println(realPath);
         // path 가 첨부파일들을 저장할 WAS(톰캣)의 폴더가 된다. 
         
         String roomImgfilename = ""; // WAS(톰캣) 디스크에 저장할 파일명 
         
         byte[] bytes = null;    // 첨부파일을 WAS(톰캣) 디스크에 저장할때 사용되는 용도 
         
         List<String> imgList = new ArrayList<String>();
         
         for(int i=1; i<fileList.size(); i++) { 
             bytes = fileList.get(i).getBytes(); // 첨부파일의 내용물(byte)을 읽어옴.
             
             // 파일업로드 한 후 업로드되어진 파일명  가져오기
             roomImgfilename = fileManager.doFileUpload(bytes, fileList.get(i).getOriginalFilename(), realPath);
             // 예를들어 newFileName 에는  2019012519592316420706146795.png 와 같은 것이 들어옴.

             imgList.add(roomImgfilename);

         }// end of for------------------------------------------------

			// file의 첫번째를 메인이미지로 한다.
			String roomMainImg = fileManager.doFileUpload(bytes, fileList.get(0).getOriginalFilename(), realPath);
			roomvo.setRoomMainImg(roomMainImg);
			
			session.setAttribute("imgList", imgList);
			// 나머지 이미지들을 set
			roomvo.setRoomimgList(imgList);
			
		}// end of if---------------------------------------
		
		//System.out.println(roomMainImg);
		
		/*List<String> roomimgList = new ArrayList<String>(); 
		
		for(int i=1; i<roomimgList.size(); i++) {
			String roomimgname = fileList.get(i).getOriginalFilename();
			roomimgList.add(roomimgname);
		}
		roomvo.setRoomimgList(roomimgList);
		*/
		
      // roomvo에 들어간 값들을 세션에 저장한다 
      session.setAttribute("roomvo", roomvo);
		
	  JSONArray jsonArr = new JSONArray();

      jsonArr.put(fileList);
      
      String str_json = jsonArr.toString();
      req.setAttribute("str_json", str_json);
      
      return "JSON";
   }
   
   @RequestMapping(value="/roomstep3.air", method={RequestMethod.GET})
   public String roomstep3(HttpServletRequest req) {
       
      HttpSession session = req.getSession();
	  MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
	  
	  if(loginuser == null) {
		  return "become-host/error";
	  }
      RoomVO roomvo = (RoomVO) session.getAttribute("roomvo");
      
      // 파라미터 값에 RoomVO 안들어가서 이렇게 했다 
      String roomInfo = req.getParameter("roomInfo");
      String roomName = req.getParameter("roomName");
      
      roomvo.setRoomInfo(roomInfo);
      roomvo.setRoomName(roomName);
      
      // roomvo에 들어간 값들을 세션에 저장한다 
      session.setAttribute("roomvo", roomvo);
      
      return "become-host/room-step3";
   }
   
   @RequestMapping(value="/roomstep3page.air", method={RequestMethod.GET})
   public String roomstep3page(HttpServletRequest req) {
	   
	  HttpSession session = req.getSession();
	  MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
	  
	  if(loginuser == null) {
		  return "become-host/error";
	  }
      
      return "become-host/room-step3-page";
   }
   
   @RequestMapping(value="/roomlaststep.air", method={RequestMethod.GET})
   public String roomlaststep(HttpServletRequest req) {
      
      HttpSession session = req.getSession();
	  MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
	  
	  if(loginuser == null) {
		  return "become-host/error";
	  }
      
      RoomVO roomvo = (RoomVO) session.getAttribute("roomvo");
      
      String checkInTime = req.getParameter("checkInTime");
      String checkOutTime = req.getParameter("checkOutTime");
      String roomPrice = req.getParameter("roomPrice");
      String peakper = req.getParameter("peakper");
      String person_addpay = req.getParameter("person_addpay");
      String cleanPay = req.getParameter("cleanPay");
      String roomTel = req.getParameter("roomTel");
      
      roomvo.setCheckInTime(checkInTime);
      roomvo.setCheckOutTime(checkOutTime);
      roomvo.setRoomPrice(roomPrice);
      roomvo.setPeakper(peakper);
      roomvo.setPerson_addpay(person_addpay);
      roomvo.setCleanPay(cleanPay);
      roomvo.setRoomTel(roomTel);
      // roomvo에 들어간 값들을 세션에 저장한다 
      session.setAttribute("roomvo", roomvo);
      
      return "become-host/room-lastStep";
   }
   
   @RequestMapping(value="/roomfinish.air", method={RequestMethod.GET})
   public String roomfinish(HttpServletRequest req) {
      
      HttpSession session = req.getSession();  
	  MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
	  
	  if(loginuser == null) {
		  return "become-host/error";
	  }
      
      RoomVO roomvo = (RoomVO) session.getAttribute("roomvo");
      
      roomvo.setHost(loginuser);
      //System.out.println(roomvo.getHost().getUserid());
      
      int n = service.becomehost(roomvo); // 숙소 insert
      
      List<String> imgList = (List<String>) session.getAttribute("imgList");
      
      //1번째 이미지 메인이미지로 들어감
      if(imgList.size() > 1) {
    	  service.imgList(roomvo);  // 이미지 insert
      }
      
      List<String> option = roomvo.getMyoption();
      if(option != null) {
         service.myoption(roomvo); // 옵션 insert
      }
      
      List<String> rule = roomvo.getMyrule();
      if(rule != null) {
         service.myrule(roomvo);   // 규칙 insert
      }
      
    /*  String bedroomInfo = (String) session.getAttribute("bedroomInfo");
      String[] bedroomInfoArr = bedroomInfo.split("/");

      // for문을돌리면 하나의 침실정보가 String 형태로 나옴
      for(String str : bedroomInfoArr) {
         System.out.println(str);
         HashMap<String,String> paraMap = new HashMap<String,String>();
         JSONObject jsonbedinfo = new JSONObject(str); // 가져온 String 형태를 JSON으로 변환
         Set<String> jsonkeys = jsonbedinfo.keySet(); // JSON으로 변환된 객체의 key들을 가져옴
         paraMap.put("buildType_detail_idx",roomvo.getFk_buildType_detail_idx());
         paraMap.put("roomType_idx",roomvo.getFk_roomType_idx());
         for(String key :jsonkeys) {
        	 paraMap.put(key, String.valueOf(jsonbedinfo.get(key))); // key값만큼 for문을 돌려서 hash맵 형태로 저장
         }
         service.insertbedroom(paraMap);
      }
      */
      if(n ==1) {
    	  
    	  
    	  
         return "host/hostroomList.hosttiles";
      }
      else return "become-host/error";

      
   }
   

   
}
 