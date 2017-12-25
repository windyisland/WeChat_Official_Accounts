package com.hyw1.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.hyw.po.Image;
import com.hyw.po.ImageMessage;
import com.hyw.po.Music;
import com.hyw.po.MusicMessage;
import com.hyw.po.News;
import com.hyw.po.NewsMessage;
import com.hyw.po.TextMessage;
import com.thoughtworks.xstream.XStream;

import net.sf.json.JSONObject;


public class MessageUtil {
	public static final String MESSAGE_TEXT="text";
	public static final String MESSAGE_NEWS="news";
	public static final String MESSAGE_IMAGE="image";
	public static final String MESSAGE_VOICE="voice";
	public static final String MESSAGE_VIDEO="video";
	public static final String MESSAGE_LINK="link";
	public static final String MESSAGE_LOCATION="location";
	public static final String MESSAGE_EVENT="event";
	public static final String MESSAGE_SUBSCRIBE="subscribe";
	public static final String MESSAGE_UNSUBSCRIBE="unsubscribe";
	public static final String MESSAGE_CLICK="CLICK";
	public static final String MESSAGE_VIEW="VIEW";
	public static final String MESSAGE_MUSIC="music";
	public static final String MESSAGE_SCANCODE="scancode_push";
	public static final String USER_LOCATION="LOCATION";
	
	//xml转map，读取微信服务器传过来的消息。
	public static Map<String,String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException{
		Map<String,String> map=new HashMap<String,String>();
		SAXReader reader=new SAXReader();		
		InputStream ins=request.getInputStream();
		Document doc=reader.read(ins);		
		Element root=doc.getRootElement();
		List<Element> list=root.elements();
		for(Element e:list) {
			map.put(e.getName(),e.getText());
		}
		ins.close();
		return map;
	}
	//将文本消息对象转为xml
	public static String textMessageToXml(TextMessage textMessage) {
		XStream xstream=new XStream();
		xstream.alias("xml",textMessage.getClass());
		return xstream.toXML(textMessage);		
	}
	
	//组装文本
	public static String initText(String toUserName,String fromUserName,String content) {
		TextMessage text=new TextMessage();
		text.setToUserName(fromUserName);
		text.setFromUserName(toUserName);
		text.setMsgType(MessageUtil.MESSAGE_TEXT);
		text.setCreateTime(new Date().getTime());
		text.setContent(content);
		return textMessageToXml(text);
	}
	
	
	//主菜单
	public static String menuText(){
		StringBuffer sb=new StringBuffer();
		sb.append("欢迎您的关注，请按照菜单提示进行操作:\n\n");
		sb.append("1、此公众号的作用\n");
		sb.append("2、此公众号归属地\n");
		sb.append("3、展示图文消息\n");
		sb.append("4、展示图片消息\n");
		sb.append("5、给您推荐一首歌曲\n");
		sb.append("回复？调出此菜单。");
		return sb.toString();
		
	}
	
	public static String firstMenu() {
		StringBuffer sb=new StringBuffer();
		sb.append("此公众号用于基于微信公众号开发学习及测试");
		return sb.toString();
	}
	public static String secondMenu() {
		StringBuffer sb=new StringBuffer();
		sb.append("ucashyw");
		return sb.toString();
	}
	
	//图片消息转为xml
	public static String imageMessageToXml(ImageMessage imagemessage) {
		XStream xstream=new XStream();
		xstream.alias("xml",imagemessage.getClass());
		return xstream.toXML(imagemessage);
	}
	
	//图文消息转为xml
	public static String newsMessageToXml(NewsMessage newsmessage) {
		XStream xstream=new XStream();
		xstream.alias("xml",newsmessage.getClass());
		xstream.alias("item",new News().getClass());
		return xstream.toXML(newsmessage);
	}
	
	//音乐消息转为xml
	public static String musicMessageToXml(MusicMessage musicmessage) {
		XStream xstream=new XStream();
		xstream.alias("xml",musicmessage.getClass());
		return xstream.toXML(musicmessage);
	}
	
	
	//图文消息的组装
	public static String initNewsMessage(String toUserName,String fromUserName) {
		String message=null;
		List<News> newsList=new ArrayList<News>();
		NewsMessage newsMessage=new NewsMessage();
		
		News news =new News();
		news.setTitle("这是一个baby");
		news.setDescription("猜猜他几岁了？");
		news.setPicUrl("http://csss.ngrok.xiaomiqiu.cn/WeiXin/image/baby.jpg");
		news.setUrl("www.baidu.com");
        newsList.add(news);
        
        newsMessage.setToUserName(fromUserName);
        newsMessage.setFromUserName(toUserName);
        newsMessage.setCreateTime(new Date().getTime());
        newsMessage.setMsgType(MESSAGE_NEWS);
        newsMessage.setArticles(newsList);
        newsMessage.setArticleCount(newsList.size());
        
        message=newsMessageToXml(newsMessage);
        return message;
	}
	
	//组装图片消息
	public static String initImageMessage(String toUserName,String fromUserName) {
		String message=null;
		Image image=new Image();
		image.setMediaId("NH9GfRYbFUj22MrvyzZvxJpABU2-UvslD8QMOeLYHilr7AIjr5x_zqjKDjbUhFrd");
		ImageMessage imageMessage=new ImageMessage();
		imageMessage.setFromUserName(toUserName);
		imageMessage.setToUserName(fromUserName);
		imageMessage.setMsgType(MESSAGE_IMAGE);
		imageMessage.setCreateTime(new Date().getTime());
		imageMessage.setImage(image);
		message=imageMessageToXml(imageMessage);
		return message;
	}
	
	//组装音乐消息
	public static String initMusicMessage(String toUserName,String fromUserName) {
		String message=null;
		Music music=new Music();
		music.setThumbMediaId("rOO75mlqP6-ohHTavA_HJoHrIXHG4T-3ED15WxMrSNeEkmslRsAl3juUudz0jyNM");
		music.setTitle("see you again");
		music.setDescription("超级好听的一首音乐歌");
		music.setMusicUrl("http://csss.ngrok.xiaomiqiu.cn/WeiXin/resource/Seeyouagain.mp3");
		music.setHQMusicUrl("http://csss.ngrok.xiaomiqiu.cn/WeiXin/resource/Seeyouagain.mp3");
		MusicMessage musicMessage=new MusicMessage();
		musicMessage.setFromUserName(toUserName);
		musicMessage.setToUserName(fromUserName);
		musicMessage.setMsgType(MESSAGE_MUSIC);
		musicMessage.setCreateTime(new Date().getTime());
		musicMessage.setMusic(music);
		message=musicMessageToXml(musicMessage);
		return message;
	}
	
	

}
