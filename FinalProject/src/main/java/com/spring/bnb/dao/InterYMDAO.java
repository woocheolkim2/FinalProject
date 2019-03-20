package com.spring.bnb.dao;

import java.util.HashMap;

import com.spring.bnb.model.MemberVO;

public interface InterYMDAO {

	MemberVO loginMember(HashMap<String, String> map);

	void memberJoin(MemberVO membervo);

}
