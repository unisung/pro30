package com.myspring.pro30.board.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.myspring.pro30.board.dao.BoardDAO;
import com.myspring.pro30.board.vo.ArticleVO;

@Service("boardService")
@Transactional(propagation = Propagation.REQUIRED)
public class BoardServiceImpl implements BoardService {
	@Autowired
	BoardDAO boardDAO;
	@Autowired
	ArticleVO articleVO;

	@Override
	public List<ArticleVO> listArticles() throws Exception {
		List list =boardDAO.selectAllArticlesList();
		System.out.println(list.size());
		return list;
	}

	@Override
	public int addNewArticle(Map articleMap) throws Exception {
		return boardDAO.insertNewArticle(articleMap);
	}

	@Override
	public ArticleVO viewArticle(int articleNO) throws Exception {
		return boardDAO.selectArticle(articleNO);
	}

	@Override
	public void modArticle(Map articleMap) throws Exception {
	  boardDAO.updateArticle(articleMap);
	}

	@Override
	public void removeArticle(int articleNO) throws Exception {
	   boardDAO.deleteArticle(articleNO);
	}

	@Override
	public int addReply(Map<String, Object> articleMap) throws Exception {
		return boardDAO.insertNewReply(articleMap);
	}
}
