package org.shirdrn.tuscany.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpUtils {

	private static final Log LOG = LogFactory.getLog(HttpUtils.class);
	private static final String CHARSET = "UTF-8";
	
	public static String request(URL u) throws IOException {
		InputStream in = null;
        BufferedReader reader = null;
        try {
        	in = u.openStream();
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuffer sb = new StringBuffer();
            String str;
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
            return sb.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
	
	public static String request(InputStream in) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuffer sb = new StringBuffer();
            String str;
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
            return sb.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
	
    public static String request(URL u, String postData) throws IOException {
    	HttpURLConnection httpConn = (HttpURLConnection) u.openConnection();
		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		httpConn.setUseCaches(false);
		httpConn.setAllowUserInteraction(false);
		httpConn.setRequestProperty("Content-type", "text/json");
		httpConn.setFixedLengthStreamingMode(postData.getBytes(CHARSET).length);
		OutputStream out = httpConn.getOutputStream();
		pipe(stringToStream(postData), out);
		if(out!=null) {
			out.close();
		}
		InputStream in = null;
		int responseCode = httpConn.getResponseCode();
		if (HttpURLConnection.HTTP_OK != responseCode) {
			LOG.error("Server error: " + httpConn.getResponseCode() + " " + httpConn.getResponseMessage());
		}
		in = httpConn.getInputStream();
		if(httpConn!=null) {
			httpConn.disconnect();
		}
		return request(in);
	}
	
	private static void pipe(InputStream dataIn, OutputStream dataOut) throws IOException {
		byte[] buf = new byte[1024];
		int read = 0;
		while ((read = dataIn.read(buf)) >= 0) {
			if (null != dataOut) {
				dataOut.write(buf, 0, read);
			}
		}
		if (null != dataOut) {
			dataOut.flush();
		}
	}
	
	public static InputStream stringToStream(String s) {
		InputStream is = null;
		try {
			is = new ByteArrayInputStream(s.getBytes(CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return is;
	}
}
