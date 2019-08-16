package com.myspring.pro30.member.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myspring.pro30.member.service.MemberService;
import com.myspring.pro30.member.vo.MemberVO;

@Controller("memberController")
@EnableAspectJAutoProxy
public class MemberControllerImpl implements MemberController {
	//private static final Logger logger 
    //= LoggerFactory.getLogger(MemberControllerImpl.class);
   @Autowired
	private MemberService memberService;
   @Autowired
    MemberVO memberVO;

	@Override
	@RequestMapping(value="/member/listMembers.do",method=RequestMethod.GET)
	public ModelAndView listMembers(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/* String viewName=getViewName(request); */
		String viewName = (String)request.getAttribute("viewName");
		System.out.println("viewName:"+viewName);
     // logger.info("info 레벨:"+viewName);
     //   logger.debug("debug 레벨:"+viewName);
		//서비스로부터 결과를 받아서 list로 만들고
		List membersList=memberService.listMembers();
		ModelAndView mav = new ModelAndView(viewName);
		//list객체를 view로 넘기기위해 addObject("속성",list);
		mav.addObject("membersList",membersList);
		return mav;
	}

	@Override
	@RequestMapping(value="/member/addMember.do",method=RequestMethod.POST)
	public ModelAndView addMember(@ModelAttribute("member") MemberVO memberVO, 
			                                          HttpServletRequest request, 
			                                          HttpServletResponse response)
			throws Exception {
		System.out.println(memberVO);
	        int result=memberService.addMember(memberVO);
	        ModelAndView mav = new ModelAndView("redirect:/member/listMembers.do");
		return mav;
	}

	@Override
	@RequestMapping("/member/removeMember.do")
	public ModelAndView removeMember(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		  response.setContentType("text/html;charset=utf-8");
		  PrintWriter out =response.getWriter();
		  //회원 삭제
		/*
		 * out.print("<script>"); out.print("var result=confirm('삭제하시겠습니까?');");
		 * out.print("alert(result);"); out.print("if(result!=true){history.back();}");
		 * out.print("</script>");
		 */
		  
		  int result=memberService.removeMember(id);
		  
		  if(result>0) {
			  out.print("<script>");
			  out.print("alert('삭제되었습니다.');");
			  out.print("location.href='http://localhost:8181/pro27/member/listMembers.do'");
			  out.print("</script>");
		  }
		return null;
	}
	
	@RequestMapping(value= {"/member/*Form.do"},
			                   method=RequestMethod.GET)
	public ModelAndView form(@RequestParam(value ="action",required = false) String action,
			                                 @RequestParam(value="result", required = false) String result,
			                                 HttpServletRequest request,
			                                 HttpServletResponse response) throws Exception{
		/* String viewName = getViewName(request); */
		String viewName = (String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		
		if(action!=null ) {
			if(action.equals("update")) {
				String id = request.getParameter("id");
			    MemberVO memberVO =memberService.getMemberInfo(id);
			    mav.addObject("member",memberVO);
			    return mav;
			}else {
				HttpSession session = request.getSession();
				session.setAttribute("action", action);//action의 값이 update가 아닐때
				mav.addObject("result",result);
				mav.setViewName(viewName);
				System.out.println("action="+session.getAttribute("action"));
				return mav;
			}
		}else if(action==null){
			 mav.addObject("result",result);
			 
			 return mav;
		}
		
		return	null;
	}

	@Override
	@RequestMapping(value="/member/modMember.do",method=RequestMethod.POST)
	public ModelAndView modMember(@ModelAttribute("info") MemberVO memberVO, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		    response.setContentType("text/html;charset=utf-8");
		    PrintWriter out = response.getWriter();
		    //회원정보 수정 전 비번확인하기
		    String pwd=memberVO.getPwd();
		    String id=memberVO.getId();
		    String dbPwd=memberService.getMemberDBPwd(memberVO);
		    //비번이 없거나 입력한 비번과 db의 비번이 다르면 되돌아감.
		    if(dbPwd==null || !dbPwd.equals(pwd)) {
		     out.print("<script>");
			 out.print("alert('비번을 다시확인하세요!');");
			 out.print("history.back();");
			 out.print("</script>");
		    }else {
	        int result=memberService.updateMember(memberVO);
			out.print("<script>");
			out.print("alert('회원정보 수정 성공!');");
			out.print("location.href='http://localhost:8181/pro30/member/listMembers.do'");
			out.print("</script>");
		    }
			return null;
		/* return new ModelAndView("redirect:/member/listMembers.do"); */
	}
	
    //로그인 처리
	@Override
	@RequestMapping(value="/member/login.do",method = RequestMethod.POST)
	public ModelAndView login(@ModelAttribute("member") MemberVO member, 
			                                 RedirectAttributes rAttr, //redirect로 이동시 객체 전달
			                                 HttpServletRequest request,
			                                 HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView(getViewName(request));
		//Autowired된 memberVO객체 사용
		memberVO=memberService.login(member);
		if(memberVO!=null) {
			HttpSession session=request.getSession();
			session.setAttribute("member", memberVO);//회원정보를 session에 담기
			session.setAttribute("isLogOn", true);//로그인 여부 true를 session에 담기
			String action =(String)session.getAttribute("action");//로그인처리 후
			session.removeAttribute("action");//action정보는 session에서 삭제
			
			if(action!=null) {
			   mav.setViewName("redirect:"+action);//"redirect:/board/articleForm.do"
			}else {
			mav.setViewName("redirect:/member/listMembers.do");
			}
		}else {
			rAttr.addAttribute("result", "loginFailed");
			mav.setViewName("redirect:/member/loginForm.do");
		}
		return mav;
	}


	@Override
	@RequestMapping(value="/member/logout.do",method=RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    HttpSession session=request.getSession();//세션얻기
	    session.removeAttribute("member");//세션에서 정보 제거 removeAttribute()
	    session.removeAttribute("isLogOn");
	    ModelAndView mav = new ModelAndView();
	    mav.setViewName("redirect:/member/listMembers.do");
		return mav;
	}
	
	
	//request객체로 부터 viewName을 만드는 메소드
	private String getViewName(HttpServletRequest request) throws Exception {
		String contextPath = request.getContextPath();
		String uri = (String) request.getAttribute("javax.servlet.include.request_uri");
		if (uri == null || uri.trim().equals("")) {
			uri = request.getRequestURI();
		}

		int begin = 0;
		if (!((contextPath == null) || ("".equals(contextPath)))) {
			begin = contextPath.length();
		}

		int end;
		if (uri.indexOf(";") != -1) {
			end = uri.indexOf(";");
		} else if (uri.indexOf("?") != -1) {
			end = uri.indexOf("?");
		} else {
			end = uri.length();
		}

		String viewName = uri.substring(begin, end);
		if (viewName.indexOf(".") != -1) {
			viewName = viewName.substring(0, viewName.lastIndexOf("."));
		}
		//action-servlet.xml의 "/WEB-INF/views/" 부분은 그대로 두고 이부분을 수정
		if (viewName.lastIndexOf("/") != -1) {
			viewName = viewName.substring(viewName.lastIndexOf("/",1), viewName.length());
		}
		return viewName;
	}




}
