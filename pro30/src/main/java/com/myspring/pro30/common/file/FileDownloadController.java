package com.myspring.pro30.common.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FileDownloadController {
  private static final String ARTICLE_IMAGE_REPO="c:\\board\\article_image";
  
  @RequestMapping("/download.do")
  protected void download(@RequestParam("imageFileName") String imageFileName,
		     @RequestParam("articleNO") String articleNO,
		     HttpServletRequest reqeust,
		     HttpServletResponse response
		  )throws Exception{
  //file(byte단위) 출력 스트림 생성			  
   OutputStream out=response.getOutputStream();
   String downfile = ARTICLE_IMAGE_REPO+"\\"+articleNO+"\\"+imageFileName;
   File file = new File(downfile);
   System.out.println(file.toString()) ;
   //head
   response.setHeader("Cache-Control", "no-cache");
   response.addHeader("Content-disposition", "attachment; fileName="+imageFileName);
   
   FileInputStream in = new FileInputStream(file);
   byte[] buffer = new byte[1024*8];
   while(true) {
	   int count = in.read(buffer);
	   if(count==-1)//파일의 끝일때 -1을 리턴
		   break;
	   out.write(buffer,0,count);//buffer에 저장된 길이만큼 file에 기록
   }
   in.close();
   out.close();
  }
  
}
