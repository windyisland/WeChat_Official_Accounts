package com.hyw1.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.hyw.menu.Button;
import com.hyw.menu.ClickButton;
import com.hyw.menu.Menu;
import com.hyw.menu.ViewButton;
import com.hyw.po.AccessToken;

import net.sf.json.JSONObject;

public class WeixinUtil {
	private static final String APPID="wx35e22031c92e2e14";
	private static final String APPSECRET="d6a93cabcf50a54ee52ca21adec1d409";
	private static final String ACCESS_TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	private static final String UPLOAD_URL="https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	private static final String CREATE_MENU_URL="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	private static final String QUERY_MENU_URL="https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	private static final String DELETE_MENU_URL="https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	private static final String WANGYESHOUQUAN="https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
	private static final String WANGYEACCESS_TOKEN="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	//get请求
	public static JSONObject doGetStr(String url) {
		DefaultHttpClient httpClient=new DefaultHttpClient();
		HttpGet httpGet=new HttpGet(url);
		JSONObject jsonObject=null;
		try {
			HttpResponse response=httpClient.execute(httpGet);
			HttpEntity entity=response.getEntity();
			if(entity!=null) {
				String result=EntityUtils.toString(entity,"UTF-8");
				jsonObject=JSONObject.fromObject(result);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	//post请求
	public static JSONObject doPostStr(String url,String outStr) {
		DefaultHttpClient httpClient=new DefaultHttpClient();
		HttpPost httpPost=new HttpPost(url);
		JSONObject jsonObject=null;
		try {
			httpPost.setEntity(new StringEntity(outStr,"UTF-8"));
			HttpResponse response=httpClient.execute(httpPost);
			String result=EntityUtils.toString(response.getEntity(),"UTF-8");
			jsonObject = JSONObject.fromObject(result);
			} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	//获取access_token
	public static AccessToken getAccessToken() {
		AccessToken token=new AccessToken();
		String url=ACCESS_TOKEN_URL.replace("APPID",APPID).replace("APPSECRET",APPSECRET);
		JSONObject jsonObject=doGetStr(url);
		if(jsonObject!=null) {
			token.setToken(jsonObject.getString("access_token"));
			token.setExpiresIn(jsonObject.getInt("expires_in"));
		}
		return token;
	}
	
	//文件上传的方法
	
	public static String upload(String filepath,String accessToken,String type)throws IOException,NoSuchAlgorithmException{
		File file=new File(filepath);
		if(!file.exists() || !file.isFile()) {
			throw new IOException("文件不存在");
		}
		String url=UPLOAD_URL.replace("ACCESS_TOKEN",accessToken).replace("TYPE",type);
		URL urlObj=new URL(url);
		//连接
		HttpURLConnection con=(HttpURLConnection)urlObj.openConnection();
		
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);
		
		//设置请求头信息
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");
		
		//设置边界
		String BOUNDARY="----------"+System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data;boundary="+BOUNDARY);
		
		StringBuilder sb=new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition:form-data;name=\"file\";filename=\""+file.getName()+"\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");
		
		byte[] head=sb.toString().getBytes("utf-8");
		
		//获得输出流
		OutputStream out=new DataOutputStream(con.getOutputStream());
		//输出表头
		out.write(head);
		
		//文件正文部分
		//把文件以流文件的方式推入到url中
		DataInputStream in=new DataInputStream(new FileInputStream(file));
		int bytes=0;
		byte[] bufferOut=new byte[1024];
		while((bytes=in.read(bufferOut))!=-1) {
			out.write(bufferOut,0,bytes);
		}
		in.close();
		
		//结尾部分
		byte[] foot=("\r\n--"+BOUNDARY+"--\r\n").getBytes("utf-8");//定义最后数据分割线
		out.write(foot);
		out.flush();
		out.close();
		
		StringBuffer buffer=new StringBuffer();
		BufferedReader reader=null;
		String result=null;
		try {
			//定义BufferedReader输入流来读取url的响应
			reader=new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line=null;
			while((line=reader.readLine())!=null){
				buffer.append(line);
			}
			if(result==null) {
				result=buffer.toString();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			if(reader!=null) {
				reader.close();
			}
		}
		
		JSONObject jsonObj=JSONObject.fromObject(result);
		System.out.println(jsonObj);
		String mediaId=jsonObj.getString("media_id");
		return mediaId;
	}
	
	
	
	//组装button
	public static Menu initMenu() {
		Menu menu=new Menu();
		ViewButton button11=new ViewButton();
		ViewButton button12=new ViewButton();
		ViewButton button13=new ViewButton();
		ViewButton button14=new ViewButton();
		button11.setName("商会首页");
		button11.setType("view");
		button11.setUrl("http://www.cygsl.org/");
		
		button12.setName("联系我们");
		button12.setType("view");
		button12.setUrl("http://www.cygsl.org/html/20130829/381.html");
		
		button13.setName("商务信息");
		button13.setType("view");
		button13.setUrl("http://www.cygsl.org/list.aspx?nid=6");
		
		button14.setName("会员服务");
		button14.setType("view");
		button14.setUrl("http://www.cygsl.org/list.aspx?nid=9");
		//组装第一个子菜单、
		Button button1=new Button();
		button1.setName("商会相关");
		button1.setSub_button(new Button[] {button11,button12,button13,button14});
		
		ViewButton button21=new ViewButton();
		button21.setName("生活服务");
		button21.setType("view");
		String url=WANGYESHOUQUAN.replace("APPID",APPID).replace("REDIRECT_URI","http://csss.ngrok.xiaomiqiu.cn/wxgduse/wxgddh.jsp").replace("SCOPE","snsapi_userinfo");
		button21.setUrl(url);
		
		ViewButton button22=new ViewButton();
		button22.setName("您的评价");
		button22.setType("view");
		String url1=WANGYESHOUQUAN.replace("APPID",APPID).replace("REDIRECT_URI","http://csss.ngrok.xiaomiqiu.cn/wxgduse/user_own_all_assess.jsp").replace("SCOPE","snsapi_userinfo");
		button22.setUrl(url1);
		
		ViewButton button23=new ViewButton();
		button23.setName("网页授权调试");
		button23.setType("view");
		String url2=WANGYESHOUQUAN.replace("APPID",APPID).replace("REDIRECT_URI","http://csss.ngrok.xiaomiqiu.cn/wxgduse/wysq.jsp").replace("SCOPE","snsapi_userinfo");
		button23.setUrl(url2);
		
		//组装第而个子菜单、
		Button button2=new Button();
		button2.setName("网页测试");
		button2.setSub_button(new Button[] {button21,button22,button23});
		
		
		ClickButton button31=new ClickButton();
		button31.setName("扫码");
		button31.setType("scancode_push");
		button31.setKey("31");
		
		ClickButton button32=new ClickButton();
		button32.setName("地理位置");
		button32.setType("location_select");
		button32.setKey("32");
		
		ClickButton button33=new ClickButton();
		button33.setName("单纯点击");
		button33.setType("click");
		button33.setKey("33");
		
		Button button3=new Button();
		button3.setName("其他功能");
		button3.setSub_button(new Button[]{button31,button32,button33});
		
		menu.setButton(new Button[]{button1,button2,button3});
		return menu;
	}
	
	public static int createMenu(String token,String menu) throws IOException {
		int result=0;
		String url=CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject=doPostStr(url,menu);
		if(jsonObject !=null) {
			result=jsonObject.getInt("errcode");
		}
		return result;
	}
	//菜单查询，get方式提交
	public static JSONObject queryMenu(String token) {
		String url=QUERY_MENU_URL.replace("ACCESS_TOKEN",token);
		JSONObject jsonObject=doGetStr(url);
		return jsonObject;
	}
	//菜单删除 get方式提交
	public static int deleteMenu(String token) {
		String url=DELETE_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject=doGetStr(url);
		int result=0;
		if(jsonObject !=null ) {
			result=jsonObject.getInt("errcode");
		}
		return result;
	}
	
	public static JSONObject queryroad() {
		String url="//m.amap.com/navi/?start=116.403124,39.940693&dest=116.481488,39.990464&destName=mytest&naviBy=car&key=（您的Key）";
		JSONObject jsonObject=doGetStr(url);
		return jsonObject;
	}
	
	public static JSONObject getUseropenid(String code) {
		String url=WANGYEACCESS_TOKEN.replace("CODE",code).replace("APPID", APPID).replace("SECRET", APPSECRET);
		JSONObject jsonObject=doGetStr(url);
		return jsonObject;
	}
	
	

}
