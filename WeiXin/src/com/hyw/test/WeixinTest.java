package com.hyw.test;

import com.hyw.po.AccessToken;
import com.hyw1.util.WeixinUtil;

import net.sf.json.JSONObject;

public class WeixinTest {
	public static void main(String[] args) {
		try {
			AccessToken token=WeixinUtil.getAccessToken();
			System.out.println("票据："+token.getToken());
			System.out.println("有效时间："+token.getExpiresIn());
			
			//传输图片获取id
			//String path="D:/wanqian.jpg";
			//String mediaId=WeixinUtil.upload(path, token.getToken(), "thumb");
			//System.out.println(mediaId);
			
			//创建菜单按钮
			String menu=JSONObject.fromObject(WeixinUtil.initMenu()).toString();
			int result=WeixinUtil.createMenu(token.getToken(), menu);
			if (result==0) {
				System.out.println("创建菜单成功");
			}else {
				System.out.println("错误码"+result);
			}
			
			//菜单查询
			//JSONObject jsonObject=WeixinUtil.queryMenu(token.getToken());
			//System.out.println(jsonObject);
			
			//删除菜单
			//int result=WeixinUtil.deleteMenu(token.getToken());
			//if(result==0) {
			//	System.out.println("菜单删除成功");
			//}else {
			//	System.out.println(result);
			//}
			
			//获取用户的openid;
			//JSONObject jsonObject=WeixinUtil.getUseropenid("001WyOVf2RjwxB0YoMUf2j7LVf2WyOVJ");
		    //System.out.println(jsonObject);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
