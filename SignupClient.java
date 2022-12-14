import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import javax.swing.*;



import java.io.*;
import java.net.*;
import java.util.HashMap;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class SignupClient {
	public static String signup(ObjectInputStream inFromServer, ObjectOutputStream outToServer, String _id, String _pw, String _name, String _nickname, String _email, String _birth) throws Exception{
		
		// 사용자에게 회원가입 성공 또는 실패 결과를 알려주기 위한 변수
		String message = new String();
		String check = "fail";
		AES256 aes256 = new AES256();
		
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("request", "register");
		
		User myinfo = new User(_id, aes256.encrypt(_pw), _name, _nickname, _email, _birth);
		Protocol protocol = new Protocol(header, myinfo);
		
		// 유저 객체 생성해서 서버로 데이터 전송하기 & 결과 메세지 받기
		outToServer.writeObject(protocol);	
		if(inFromServer.readObject() != null) {
			check = "success";
		}
		
		System.out.println("FROM SERVER : " + message);
		return check;
		
	}

}