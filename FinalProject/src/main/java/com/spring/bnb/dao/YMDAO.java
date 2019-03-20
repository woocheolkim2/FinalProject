package com.spring.bnb.dao;

import java.util.HashMap;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.bnb.model.MemberVO;

@Repository
public class YMDAO implements InterYMDAO {
	
	@Autowired
	private SqlSessionTemplate sqlsession; // SqlSessionTemplate --> root-context.xml  #15.

	@Override
	public MemberVO loginMember(HashMap<String, String> map) {

		MemberVO loginuser = sqlsession.selectOne("ym.loginMember", map);
		return loginuser;
	}

	@Override
	public void memberJoin(MemberVO membervo) {

		sqlsession.insert("ym.memberJoin", membervo);
		
	}

}
