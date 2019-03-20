package com.spring.bnb.service;

import java.util.HashMap;

import com.spring.bnb.model.MemberVO;

public interface InterYMService {

	MemberVO loginMember(HashMap<String, String> map);

	void memberJoin(MemberVO membervo);

}
