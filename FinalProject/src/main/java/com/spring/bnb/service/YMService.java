package com.spring.bnb.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import com.spring.bnb.dao.InterYMDAO;
import com.spring.bnb.model.MemberVO;

@Service
public class YMService implements InterYMService {
	
	@Autowired
	private InterYMDAO dao;

	@Override
	public MemberVO loginMember(HashMap<String, String> map) {
		
		MemberVO loginuser = dao.loginMember(map);
		
		return loginuser;
	}

	@Override
	public void memberJoin(MemberVO membervo) {
		
		dao.memberJoin(membervo);
		
		
		
	} 
}