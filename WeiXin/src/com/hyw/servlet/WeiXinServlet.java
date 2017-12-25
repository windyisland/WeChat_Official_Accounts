package com.hyw.servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

import com.hyw.po.TextMessage;
import com.hyw1.util.CheckUtil;
import com.hyw1.util.MessageUtil;

public class WeiXinServlet extends HttpServlet {
	private static final long serialVersionUID=1L;
	@Override
	protected void doGet(HttpServletRequest req,HttpServletResponse resp)
		throws ServletException,IOException{
		String  signature=req.getParameter("signature");
		String  timestamp=req.getParameter("timestamp");
		String  nonce=req.getParameter("nonce");
		String  echostr=req.getParameter("echostr");
		PrintWriter out=resp.getWriter();
		if(CheckUtil.checkSignature(signature, timestamp, nonce)) {
			out.print(echostr);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req,HttpServletResponse resp)
		throws ServletException,IOException{
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		String message=null;
		PrintWriter out =resp.getWriter();	
		String latitude="";
		String longitude="";
		try {

			Map<String,String> map=MessageUtil.xmlToMap(req);
			System.out.println(map.get("Event"));
			String fromUserName=map.get("FromUserName");
			String toUserName=map.get("ToUserName");
			String msgType=map.get("MsgType");
			String content=map.get("Content");
			//再次加入判断
			if(map.get("Event")!=null)
			{
				if("LOCATION".equals(map.get("Event"))) 
				{
					latitude=map.get("Latitude");
					longitude=map.get("Longitude");
					System.out.println(latitude);
					System.out.println(longitude);
				}
				return;
			}
			if(MessageUtil.MESSAGE_TEXT.equals(msgType)) {//根据消息的内容回复（自动回复部分）
				if("1".equals(content)) {
					message=MessageUtil.initText(toUserName, fromUserName, MessageUtil.firstMenu());
				}else if("2".equals(content)) {
					message=MessageUtil.initText(toUserName, fromUserName, MessageUtil.secondMenu());
				}else if ("?".equals(content) || "？".equals(content)) {
					message=MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
				}else if("3".equals(content)) {
					message=MessageUtil.initNewsMessage(toUserName, fromUserName);
				}else if("4".equals(content)) {
					message=MessageUtil.initImageMessage(toUserName, fromUserName);
				}else if("5".equals(content)) {
					message=MessageUtil.initMusicMessage(toUserName, fromUserName);
				}else {
					message=MessageUtil.initText(toUserName, fromUserName, "尊敬的用户:您好!\n我们已经收到您的消息:"+content+"。\n我们会尽快回复您的消息。感谢您的支持");
				}
			}else if(MessageUtil.MESSAGE_EVENT.equals(msgType)) {//根据事件反应
				String eventType=map.get("Event");
				if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)) {
					message=MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
				}else if(MessageUtil.MESSAGE_CLICK.equals(eventType)) {
					message=MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());	
				}else if(MessageUtil.MESSAGE_VIEW.equals(eventType)) {
					String url=map.get("Eventkey");
					message=MessageUtil.initText(toUserName, fromUserName,url);
				}else if(MessageUtil.MESSAGE_SCANCODE.equals(eventType)) {
					String key=map.get("EventKey");
					message=MessageUtil.initText(toUserName, fromUserName, key);
				}
			}else if(MessageUtil.MESSAGE_LOCATION.equals(msgType)) {
				String label=map.get("Label");
				message=MessageUtil.initText(toUserName, fromUserName, label);
			}
			System.out.println(message);
			out.print(message);
		} catch (DocumentException e) {
			e.printStackTrace();
		}finally{
			out.close();
		}
		
	}
	

}