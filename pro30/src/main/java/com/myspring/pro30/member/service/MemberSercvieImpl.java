package com.myspring.pro30.member.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.myspring.pro30.member.dao.MemberDAO;
import com.myspring.pro30.member.vo.MemberVO;


@Service("memberService")
// 서비스에 Transaction처리 
@Transactional(propagation = Propagation.REQUIRED)
public class MemberSercvieImpl implements MemberService {
   @Autowired
	private MemberDAO memberDAO;
 
	@Override
	public List listMembers() throws DataAccessException {
		return memberDAO.selectAllMembers();
	}

	@Override
	public int addMember(MemberVO memberVO) throws DataAccessException {
		return memberDAO.insertMember(memberVO);
	}

	@Override
	public int removeMember(String id) throws DataAccessException {
		return memberDAO.deleteMember(id);
	}

	@Override
	public MemberVO getMemberInfo(String id) throws DataAccessException {
		return memberDAO.selectMemberInfo(id);
	}

	@Override
	public int updateMember(MemberVO memberVO) throws DataAccessException {
		return memberDAO.updateMember(memberVO);
	}

	@Override
	public String getMemberDBPwd(MemberVO memberVO) throws DataAccessException {
		return memberDAO.selectMemberDBPwd(memberVO);
	}

	@Override
	public MemberVO login(MemberVO member) throws DataAccessException {
		return memberDAO.selectMemberInfo( member);
	}

}
