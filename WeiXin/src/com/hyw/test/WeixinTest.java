package com.hyw.test;

import com.hyw.po.AccessToken;
import com.hyw1.util.WeixinUtil;

import net.sf.json.JSONObject;

public class WeixinTest {
	public static void main(String[] args) {
		try {
			AccessToken token=WeixinUtil.getAccessToken();
			System.out.println("Ʊ�ݣ�"+token.getToken());
			System.out.println("��Чʱ�䣺"+token.getExpiresIn());
			
			//����ͼƬ��ȡid
			//String path="D:/wanqian.jpg";
			//String mediaId=WeixinUtil.upload(path, token.getToken(), "thumb");
			//System.out.println(mediaId);
			
			//�����˵���ť
			String menu=JSONObject.fromObject(WeixinUtil.initMenu()).toString();
			int result=WeixinUtil.createMenu(token.getToken(), menu);
			if (result==0) {
				System.out.println("�����˵��ɹ�");
			}else {
				System.out.println("������"+result);
			}
			
			//�˵���ѯ
			//JSONObject jsonObject=WeixinUtil.queryMenu(token.getToken());
			//System.out.println(jsonObject);
			
			//ɾ���˵�
			//int result=WeixinUtil.deleteMenu(token.getToken());
			//if(result==0) {
			//	System.out.println("�˵�ɾ���ɹ�");
			//}else {
			//	System.out.println(result);
			//}
			
			//��ȡ�û���openid;
			//JSONObject jsonObject=WeixinUtil.getUseropenid("001WyOVf2RjwxB0YoMUf2j7LVf2WyOVJ");
		    //System.out.println(jsonObject);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
