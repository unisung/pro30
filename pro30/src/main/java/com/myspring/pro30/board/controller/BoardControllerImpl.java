package com.myspring.pro30.board.controller;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Service.Mode;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.myspring.pro30.board.service.BoardService;
import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.member.vo.MemberVO;

@Controller("boardController")
public class BoardControllerImpl implements BoardController{
	//게시글 이미지 저장소
	private static final String ARTICLE_IMAGE_REPO="c:\\board\\article_image";
	@Autowired
	BoardService boardService;
	@Autowired
	ArticleVO articleVO;
	
	@Override
	@RequestMapping(value="/board/listArticles.do",
	                            method= {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView listArticles(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName=(String)request.getAttribute("viewName");
		List articlesList = boardService.listArticles();
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("articlesList", articlesList);
		System.out.println(articlesList.size());
		return mav;
	}

	//게시글 추가
	@Override
	@RequestMapping(value="/board/addNewArticle.do",method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity addNewArticle(MultipartHttpServletRequest multipartRequest, 
			HttpServletResponse response) throws Exception {
				multipartRequest.setCharacterEncoding("utf-8");
				Map<String,Object> articleMap = new HashMap<String, Object>();
				Enumeration enu=multipartRequest.getParameterNames();
				while(enu.hasMoreElements()){
					String name=(String)enu.nextElement();
					String value=multipartRequest.getParameter(name);
					articleMap.put(name,value);
					System.out.println("name="+name+","+"value="+value);
				}
				
				String imageFileName= upload(multipartRequest);
				HttpSession session = multipartRequest.getSession();
				MemberVO memberVO = (MemberVO) session.getAttribute("member");
				String id = memberVO.getId();
				articleMap.put("level", 0);
				articleMap.put("parentNO", 0);
				articleMap.put("id", id);
				articleMap.put("imageFileName", imageFileName);
				
				String message;
				ResponseEntity resEnt=null;
				HttpHeaders responseHeaders = new HttpHeaders();
				responseHeaders.add("Content-Type", "text/html; charset=utf-8");
				try {
					int articleNO = boardService.addNewArticle(articleMap);
					System.out.println("articleNO:"+articleNO);
					if(imageFileName!=null && imageFileName.length()!=0) {
						File srcFile = new 
						File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
						File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
						FileUtils.moveFileToDirectory(srcFile, destDir,true);
					}
			
					message = "<script>";
					message += " alert('새글을 추가했습니다.');";
					message += " location.href='"+multipartRequest.getContextPath()+"/board/listArticles.do'; ";
					message +=" </script>";
				    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
				}catch(Exception e) {
					File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
					srcFile.delete();
					
					message = " <script>";
					message +=" alert('오류가 발생했습니다. 다시 시도해 주세요');');";
					message +=" location.href='"+multipartRequest.getContextPath()+"/board/articleForm.do'; ";
					message +=" </script>";
					resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
					e.printStackTrace();
				}
				return resEnt;
			}

	//파일 업로드 처리 메소드
	private String upload(MultipartHttpServletRequest multipartRequest) throws Exception {
		String imageFileName=null;
		Iterator<String> fileNames = multipartRequest.getFileNames();
		
		while(fileNames.hasNext()) {
			String fileName=fileNames.next();
			MultipartFile mFile = multipartRequest.getFile(fileName);
			imageFileName=mFile.getOriginalFilename();
			File file = new File(ARTICLE_IMAGE_REPO +"\\"+fileName);
			if(mFile.getSize()!=0){//File Null Check
				if(!file.exists()) {//경로상에 파일이 존재하지 않을 때
					if(file.getParentFile().mkdirs()) {//경로에 해당하는 디렉토리 생성
						file.createNewFile();//파일생성
					}
				}
				mFile.transferTo(new File(ARTICLE_IMAGE_REPO+"\\temp\\"+imageFileName));//
			}
		}
		return imageFileName;
	}

	@Override
	@RequestMapping(value="/board/viewArticle.do",method = RequestMethod.GET)
	public ModelAndView viewArticle(@RequestParam("articleNO") int articleNO, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	 String viewName = (String)request.getAttribute("viewName");
	 articleVO = boardService.viewArticle(articleNO);
	 ModelAndView  mav = new ModelAndView(viewName);
	 mav.addObject("article",articleVO);
	 System.out.println("viewName:"+viewName);
		return mav;
	}

	@Override
	@RequestMapping(value="/board/removeArticle.do",
	                            method=RequestMethod.POST)
    @ResponseBody
	public ResponseEntity removeArticle(@RequestParam("articleNO") int articleNO,
			                                                HttpServletRequest request,
			                                                HttpServletResponse response)
			throws Exception {
	     response.setContentType("text/html;charset=utf-8");
	     String message;
	     ResponseEntity resEnt=null;
	     HttpHeaders responseHeaders=new HttpHeaders();
	     responseHeaders.add("Content-Type", "text/html;charset=utf-8");
	     try {
	    	 //글번호에 해당하는 내용 삭제
	    	    boardService.removeArticle(articleNO);
	    	    //저장된 이미지파일은 글번호의 디렉토리에 저장
	    	    File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
	    	    FileUtils.deleteDirectory(destDir);//글번호로 저장된 디렉토리 삭제
	    	    
	    	    message="<script>";
	    	    message+="alert('삭제완료');";
	    	    message+="location.href='"+request.getContextPath()+"/board/listArticles.do';";
	    	    message+="</script>";
	    	   resEnt = new ResponseEntity(message, responseHeaders,HttpStatus.CREATED);
	     }catch(Exception e) {
	    	    message="<script>";
	    	    message+="alert('작업 중 오류 발생');";
	    	    message+="location.href='"+request.getContextPath()+"/board/listArticles.do';";
	    	    message+="</script>";
	    	   resEnt = new ResponseEntity(message, responseHeaders,HttpStatus.CREATED);
	    	   e.printStackTrace();
	     }
		return resEnt;
	}

	@RequestMapping(value="/board/*Form.do",method= {RequestMethod.GET,RequestMethod.POST})
	private ModelAndView form(HttpServletRequest request, 
			                                  HttpServletResponse response) throws Exception{
		String parentNO=request.getParameter("parentNO");
		/* String level=request.getParameter("level"); */
		HttpSession session = request.getSession();
		if(parentNO!=null) {
			session.setAttribute("parentNO", parentNO);
		}
		/*
		 * if(level!=null) { session.setAttribute("level", level); }
		 */
		String viewName = (String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		
		return mav;
	}

	@Override
	@RequestMapping(value="/board/modArticle.do",
	method=RequestMethod.POST)
	public ResponseEntity modArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response) throws Exception{
	multipartRequest.setCharacterEncoding("utf-8");
	Map<String,Object> articleMap = new HashMap<String, Object>();
	Enumeration enu=multipartRequest.getParameterNames();
	while(enu.hasMoreElements()){
		String name=(String)enu.nextElement();
		String value=multipartRequest.getParameter(name);
		articleMap.put(name,value);
		System.out.println("name="+name+","+"value="+value);
	}
	
	String imageFileName= upload(multipartRequest);
	HttpSession session = multipartRequest.getSession();
	MemberVO memberVO = (MemberVO) session.getAttribute("member");
	String id = memberVO.getId();
	//수정
	articleMap.put("id", id);
	articleMap.put("imageFileName", imageFileName);
	//파라미터로 넘어온 글 번호
	String articleNO=(String)articleMap.get("articleNO");

	String message;
	ResponseEntity resEnt=null;
	HttpHeaders responseHeaders = new HttpHeaders();
	responseHeaders.add("Content-Type", "text/html; charset=utf-8");
	try {
		//db에 수정분 반영
		     boardService.modArticle(articleMap);
		System.out.println("articleNO:"+articleNO);
		if(imageFileName!=null && imageFileName.length()!=0) {
			File srcFile = new 
			File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
			File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
			FileUtils.moveFileToDirectory(srcFile, destDir,true);
			
			String originalFileName =(String)articleMap.get("originalFileName");
			File oldFile = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO+"\\"+originalFileName);
			oldFile.delete();
		}

		message = "<script>";
		message += " alert('글을 수정했습니다.');";
		message += " location.href='"
		                    +multipartRequest.getContextPath()
		                    +"/board/viewArticle.do?articleNO="+articleNO+"'; ";
		message +=" </script>";
	    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
	}catch(Exception e) {
		File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
		srcFile.delete();
		
		message = " <script>";
		message +=" alert('오류가 발생했습니다. 다시 수정해 주세요');');";
		message +=" location.href='"
		                +multipartRequest.getContextPath()
		                +"/board/viewArticle.do?articleNO="+articleNO+"'; ";
		message +=" </script>";
		resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		e.printStackTrace();
	}
	return resEnt;
	}
	
	//답글 추가
		@Override
		@RequestMapping(value="/board/addReply.do",
		                            method = RequestMethod.POST)
		@ResponseBody
		public ResponseEntity addReply(MultipartHttpServletRequest multipartRequest, 
				                                        HttpServletResponse response) throws Exception {
					multipartRequest.setCharacterEncoding("utf-8");
					Map<String,Object> articleMap = new HashMap<String, Object>();
					Enumeration enu=multipartRequest.getParameterNames();
					while(enu.hasMoreElements()){
						String name=(String)enu.nextElement();
						String value=multipartRequest.getParameter(name);
						articleMap.put(name,value);
						System.out.println("name="+name+","+"value="+value);
					}
					
					String imageFileName= upload(multipartRequest);
					HttpSession session = multipartRequest.getSession();
					MemberVO memberVO = (MemberVO) session.getAttribute("member");
					String id = memberVO.getId();
					articleMap.put("parentNO", Integer.parseInt(multipartRequest.getParameter("parentNO")));
					articleMap.put("id", id);
					articleMap.put("title", multipartRequest.getParameter("title"));
					articleMap.put("imageFileName", imageFileName);
					articleMap.put("level", 0);
		/*
		 * articleMap.put("level",Integer.parseInt(multipartRequest.getParameter("level"
		 * ))+1);
		 */
					
					String message;
					ResponseEntity resEnt=null;
					HttpHeaders responseHeaders = new HttpHeaders();
					responseHeaders.add("Content-Type", "text/html; charset=utf-8");
					try {
						int articleNO = boardService.addReply(articleMap);
						System.out.println("articleNO:"+articleNO);
						if(imageFileName!=null && imageFileName.length()!=0) {
							File srcFile = new 
							File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
							File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
							FileUtils.moveFileToDirectory(srcFile, destDir,true);
						}
				
						message = "<script>";
						message += " alert('답글을 추가했습니다.');";
						message += " location.href='"+multipartRequest.getContextPath()+"/board/listArticles.do'; ";
						message +=" </script>";
					    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
					}catch(Exception e) {
						File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
						srcFile.delete();
						
						message = " <script>";
						message +=" alert('오류가 발생했습니다. 다시 시도해 주세요');');";
						message +=" location.href='"+multipartRequest.getContextPath()+"/board/articleForm.do'; ";
						message +=" </script>";
						resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
						e.printStackTrace();
					}
					return resEnt;
				}
	
	
}
