package com.tbxx.wpct.util.wx;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class SocketHttpRequester {

	/**
	 * 发送请求
	 * @param path 请求路径
	 * @param params 请求参数 key为参数名称 value为参数值
	 */
	public static String post(String path, Map<String, Object> params){
		String response = null;
		StringBuilder parambuilder = new StringBuilder("");
		if(params!=null && !params.isEmpty()){
			for(Map.Entry<String, Object> entry : params.entrySet()){
				Object value = entry.getValue();
				String strValue = value==null?"":value.toString();
				parambuilder.append(entry.getKey()).append("=")
					.append(StringUtil.encodeUrl(strValue)).append("&");
			}
			parambuilder.deleteCharAt(parambuilder.length()-1);
		}
		byte[] data = parambuilder.toString().getBytes();
		
		HttpURLConnection conn = null;
		try {
			URL url = new URL(path);
			conn = (HttpURLConnection)url.openConnection();
			conn.setDoOutput(true);//允许对外发送请求参数
			conn.setUseCaches(false);//不进行缓存
			conn.setConnectTimeout(5 * 1000);
			conn.setReadTimeout(20*1000);
			conn.setRequestMethod("POST");
			//下面设置http请求头
			conn.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			conn.setRequestProperty("Accept-Language", "zh-CN");
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", String.valueOf(data.length));
			conn.setRequestProperty("Connection", "close");
			
			//发送参数
			DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
			outStream.write(data);//把参数发送出去
			outStream.flush();
			outStream.close();
			if(conn.getResponseCode()==200){
				response = new String(readStream(conn.getInputStream()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(conn!=null){
				conn.disconnect();
			}
		}
		return response;
	}
	/**
	 * 发送请求
	 * @param path 请求路径
	 */
	public static String get(String path){
		String response = null;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(path);
			conn = (HttpURLConnection)url.openConnection();
			conn.setDoInput(true);
			conn.setUseCaches(false);//不进行缓存
			conn.setConnectTimeout(5 * 1000);
			conn.setReadTimeout(20000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Connection", "close");
			//下面设置http请求头
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			if(conn.getResponseCode()==200){
				response = new String(readStream(conn.getInputStream()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(conn!=null){
				conn.disconnect();
			}
		}
		return response;
	}
	
	/**
	 * 读取流
	 * @param inStream
	 * @return 字节数组
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception{
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while( (len=inStream.read(buffer)) != -1){
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}
	/**
	 * 下载文件
	 * @param downloadUrl 请求路径
	 * @param dir 本地存储文件
	 * @param fileName 请求参数的编码
	 */
	public static boolean download(String downloadUrl,File dir,String fileName){
		boolean isOk = false;
		HttpURLConnection conn = null;
		InputStream inStream = null;
		try {
			URL url = new URL(downloadUrl);
			conn = (HttpURLConnection)url.openConnection();
			conn.setDoInput(true);
			conn.setUseCaches(false);//不进行缓存
			conn.setConnectTimeout(5 * 1000);
			conn.setReadTimeout(60000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Connection", "close");
			//下面设置http请求头
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			if(conn.getResponseCode()==200){
				inStream = conn.getInputStream();
				isOk = saveLocalFile(inStream,dir,fileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(inStream!=null){
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(conn!=null){
				conn.disconnect();
			}
		}
		return isOk;
	}
	private static boolean saveLocalFile(InputStream inStream,File dirFile,String fileName)
	{
		boolean isOk = false;
		if(inStream!=null){
			FileOutputStream fOut = null;
			try {
				if(!dirFile.exists()){
					dirFile.mkdirs();
				}
				File localFile = new File(dirFile, fileName);
				if(localFile.exists()){
					localFile.delete();
				}
				fOut = new FileOutputStream(localFile);
				byte[] buffer = new byte[1024];
				int len = 0;
				while( (len = inStream.read(buffer)) != -1){
					fOut.write(buffer, 0, len);
					fOut.flush();
				}
				isOk = true;
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if(fOut!=null){
						fOut.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return isOk;
	}
}

