package com.spring.bnb.dao;

import java.util.HashMap;
import java.util.List;

import com.spring.bnb.model.CommentVO;
import com.spring.bnb.model.MemberVO;
import com.spring.bnb.model.ReportVO;

public interface InterSHDAO {

	// 검색없이 회원목록 보여주기
	// List<MemberVO> getMemberList(HashMap<String, String> paraMap);

	// 검색해서 회원목록 보여주기
	List<MemberVO> getSearchMember(HashMap<String, String> paraMap);

	// 회원 상세페이지(MemberVO)
	MemberVO getMemberDetail(String userid);

	// 회원 상세페이지(예약코드)
	List<HashMap<String, String>> getReservation(String userid);

	// 회원 상세페이지(보유쿠폰)
	List<HashMap<String, String>> getMycoupon(String userid);
	
	// 신고글 가져오기
	List<ReportVO> getReport(HashMap<String, String> paraMap);

	// 검색조건에 만족하는 회원수를 알아오기
	int getTotalCountWithSearch(HashMap<String, String> paraMap);

	// 검색조건이 없는 회원수를 알아오기
	int getTotalCountNoSearch();

	// 검색조건이 없는것 또는 검색조건이 있는 회원전체목록 가져오기
	List<MemberVO> memberlistPaging(HashMap<String, String> paraMap);

	// 신고 글쓰기 insert하기
	int add(ReportVO reportvo);

	// 쿠폰 등록하기
	int cpAdd(HashMap<String, String> paramap);

	// 신고 글쓰기 insert하기
	int vanAdd(HashMap<String, String> paramap);

	// 검색조건에 맞는 회원 수 알아오기
	int getTotalCount(HashMap<String, String> paraMap);

	// memberList에서 회원삭제하기
	void adminDeleteMember(String userid);

	// 신고게시판의 글 총 갯수 알아오기
	int getTotalCounts(HashMap<String, String> paraMap);

	// 신고게시글 상세보기 -> 조회수 증가(x 도 포함)
	ReportVO getReportDetail(int report_idx);

	// 조회수 증가시켜주기
	void upCount(int report_idx);

	// 신고게시글 삭제하기
	int deleteReport(int report_idx);

	// 회원 경고주기
	void adminWarnMember(String userid);

	// 글 수정하기
	void writeEdit(HashMap<String, String> paraMap);

	// 댓글 등록하기
	int insertComment(HashMap<String, String> paraMap);

	// commentcount 올려주기
	void addCommentCount(HashMap<String, String> paraMap);

	// comment 가져오기
	List<CommentVO> getComment(int report_idx);
}
