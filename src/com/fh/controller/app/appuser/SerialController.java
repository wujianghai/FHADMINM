package com.fh.controller.app.appuser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fh.controller.base.BaseController;


/**@author WJH
  * 序列化与反序列化 
 */
@Controller
@RequestMapping(value="/serial")
public class SerialController extends BaseController {
	
	
	static Integer pos = 0;

	/**序列化(对象转字节流)
	 * @param zhangsan 
	 * @return 
	 * @throws IOException 
	 */
	@RequestMapping(value="/serialize")
	@ResponseBody
	public byte[] serialize(Object zhangsan) throws IOException{
		//定义一个字节数组输出流
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		//对象输出流
		ObjectOutputStream out = new ObjectOutputStream(os);
		//将对象写入到字节数组输出，进行序列化
		out.writeObject(zhangsan);
		byte[] zhangsanByte = os.toByteArray();
		return zhangsanByte;
	}
	
	/**反序列化(字节流转对象)
	 * @param zhangsan 
	 * @return 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	@RequestMapping(value="/deserialize")
	@ResponseBody
	public Object deserialize(byte[] zhangsanByte) throws IOException, ClassNotFoundException{
		//定义一个字节数组输入流
		ByteArrayInputStream is = new ByteArrayInputStream(zhangsanByte);
		//执行反序列化，从流中读取数据
		ObjectInputStream in = new ObjectInputStream(is);
		Object ob =in.readObject();
		return ob;
	}
	
	public static void main(String[] args) throws IOException {
		//AtomicInteger是一个提供原子操作的Integer类，通过线程安全的方式操作加减
		AtomicInteger  a=new AtomicInteger();
		a.incrementAndGet();
		a.decrementAndGet();
		
		//读取编码  
        byte[] encodeByte = new byte[1];  
        InputStream input = new ByteArrayInputStream(encodeByte);
        input.read(encodeByte);  
        byte encode = encodeByte[0];  
        
        //读取命令长度
        byte[] commandLengthBytes = new byte[4];
        input.read(commandLengthBytes);
        int commandLength = bytes2Int(commandLengthBytes);
        
        //读取命令
        byte[] commandBytes = new byte[commandLength];
        input.read(commandBytes);
        String command = "";
//        if(Encode.GBK.getValue() == encode){
//        	command = new String(commandBytes,"GBK");
//        }else{
//        	command = new String(commandBytes,"UTF8");
//        }
        
        //组装请求返回
        SerialController s = new SerialController();
        Request request = s.new Request();
        request.setCommand(command);
        request.setEncode(encode);
        request.setCommandLength(commandLength);
        
        
        testRoundRobin();
        System.out.println(testRoundRobin());
        Random random = new Random();
        int randomPos = random.nextInt(20);
//        Map<String , Integer> serverMap =new HashMap<String , Integer>();
        System.out.println(randomPos);
	}

	private static int bytes2Int(byte[] bytes) {
		int num = bytes[3] & 0xFF;
		num |=((bytes[2] << 8) & 0xFF00);
		num |=((bytes[1] << 16) & 0xFF0000);
		num |=((bytes[0] << 24) & 0xFF000000);
		return num;
	}
	
	/** 请求
	 * @author Administrator
	 */
	public class Request{
		//编码
		private byte encode;
		
		//命令
		private String command;
		
		//命令长度
		private int commandLength;

		public byte getEncode() {
			return encode;
		}

		public void setEncode(byte encode) {
			this.encode = encode;
		}

		public String getCommand() {
			return command;
		}

		public void setCommand(String command) {
			this.command = command;
		}

		public int getCommandLength() {
			return commandLength;
		}

		public void setCommandLength(int commandLength) {
			this.commandLength = commandLength;
		}
	}
	
	/** 响应
	 * @author Administrator
	 */
	public class Response{
		//编码
		private byte encode;
		
		//响应
		private String response;
		
		//响应长度
		private int responseLength;

		public byte getEncode() {
			return encode;
		}

		public void setEncode(byte encode) {
			this.encode = encode;
		}

		public String getResponse() {
			return response;
		}

		public void setResponse(String response) {
			this.response = response;
		}

		public int getResponseLength() {
			return responseLength;
		}

		public void setResponseLength(int responseLength) {
			this.responseLength = responseLength;
		}

	}
	
	/**
	 * 轮询
	 * @return
	 */
	private static String testRoundRobin(){
		Map<String,Integer> serverMap = new HashMap<String, Integer>();
		serverMap.put("192.168.1.100", 1);
		serverMap.put("192.168.1.101", 1);
		serverMap.put("192.168.1.102", 2);
		serverMap.put("192.168.1.103", 1);
		serverMap.put("192.168.1.104", 3);
		
		
		//创建一个map，避免出现由于服务器上线和下线导致的并发问题
		Map<String,Integer> map = new HashMap<String, Integer>();
		map.putAll(serverMap);
		
		//取得IP地址
		Set<String> keySet = serverMap.keySet();
		ArrayList<String> keyList = new ArrayList<String>();
		keyList.addAll(keySet);
		//加权
		Iterator<String> it = keySet.iterator();
		List<String> serverList = new ArrayList<String>();
		while(it.hasNext()){
			String server = it.next();
			Integer weight = serverMap.get(server);
			for(int i =0; i<=weight; i++){
				serverList.add(server);
			}
		}
		
		String server = null;
		synchronized (pos){
			if(pos >= keySet.size()){
				pos = 0;
			}
			server = keyList.get(pos);
			//加权
//			if(pos >= serverList.size()){
//				pos = 0;
//			}
//			server = serverList.get(pos);
			pos++;
		}
		
		
		
		
		return server;
	}
	
}
	
 