package com.myspring.pro30.member.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.myspring.pro30.member.vo.MemberVO;



public interface MemberDAO {
	public List selectAllMembers() throws DataAccessException;
	public int insertMember(MemberVO memberVO) throws DataAccessException;
	public int deleteMember(String id) throws DataAccessException;
	public MemberVO selectMemberInfo(String id) throws DataAccessException;
	public int updateMember(MemberVO memberVO) throws DataAccessException;
	public String selectMemberDBPwd(MemberVO memberVO)throws DataAccessException;
	public MemberVO selectMemberInfo(MemberVO memberVO) throws DataAccessException;
}
