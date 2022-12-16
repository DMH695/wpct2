package com.tbxx.wpct.util.wx;


import com.tbxx.wpct.config.Constant;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;

@SuppressWarnings("deprecation")
public class CertUtil {

	/**
	 * 加载证书
	 */
	public static SSLConnectionSocketFactory initCert() throws Exception {
		InputStream instream = null;
		KeyStore keyStore = KeyStore.getInstance("PKCS12");

		URL httpUrl = new URL(Constant.CERT_PATH);
		HttpURLConnection conn = (HttpURLConnection)httpUrl.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5 * 1000);
		//InputStream inStream = conn.getInputStream();//通过输入流获取图片数据



		instream = conn.getInputStream();//new FileInputStream(new File(Constant.CERT_PATH));
		keyStore.load(instream, Constant.MCH_ID.toCharArray());

		if (null != instream) {
			instream.close();
		}

		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, Constant.MCH_ID.toCharArray()).build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

		return sslsf;
	}
}
