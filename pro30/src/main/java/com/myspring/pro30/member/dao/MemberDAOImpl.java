package com.myspring.pro30.member.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.myspring.pro30.member.vo.MemberVO;


//mybatis메소드
//여러건의 객체 얻기  List  selectList(매퍼아이디,파라미터);
//한건의 객체 얻기  Object selectOne(매퍼아이디,파라미터);
//입력 int insert(매퍼아이디,파라미터);
//수정 int update(매퍼아이디,파라미터);
//삭제 int delete(매퍼아이디,파라미터);

@Repository("memberDAO")
public class MemberDAOImpl implements MemberDAO {
    @Autowired
	private SqlSession sqlSession;
  
	@Override
	public List selectAllMembers() throws DataAccessException {
		return sqlSession.selectList("mapper.member.listAllMembers");
	}

	@Override
	public int insertMember(MemberVO memberVO) throws DataAccessException {
		return sqlSession.insert("mapper.member.insertMember", memberVO);
	}

	@Override
	public int deleteMember(String id) throws DataAccessException {
		return sqlSession.delete("mapper.member.deleteMember", id);
	}
	
	@Override
	public MemberVO selectMemberInfo(String id) throws DataAccessException {
		return (MemberVO) sqlSession.selectOne("mapper.member.selectMemberInfo", id);
	}

	@Override
	public int updateMember(MemberVO memberVO) throws DataAccessException {
		return sqlSession.update("mapper.member.updateMemberInfo", memberVO);
	}

	@Override
	public String selectMemberDBPwd(MemberVO memberVO) throws DataAccessException {
		return (String) sqlSession.selectOne("mapper.member.selectMemberDBPwd",memberVO);
	}

	@Override
	public MemberVO selectMemberInfo(MemberVO memberVO) throws DataAccessException {
		return sqlSession.selectOne("mapper.member.selectMemberInfoBymemberVO", memberVO);
	}

}
