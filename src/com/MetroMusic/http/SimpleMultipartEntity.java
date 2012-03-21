package com.MetroMusic.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

public class SimpleMultipartEntity implements HttpEntity{
	private static final char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	private String boundary = null;
	private ByteArrayOutputStream out;
	private boolean isSetFirst;
	private boolean isSetLast;
	public SimpleMultipartEntity()
	{
		out = new ByteArrayOutputStream();
		isSetFirst = false;
		isSetLast  = false;
		StringBuffer localStringBuffer = new StringBuffer();
		Random localRandom = new Random();
		int localObject = 0;
		while(localObject ++ < 30)
		{
			char[] arrayOfChar = MULTIPART_CHARS;
		    int i = MULTIPART_CHARS.length;
		    int j = localRandom.nextInt(i);
		    char c = arrayOfChar[j];
		    localStringBuffer.append(c);
		}
		this.boundary = localStringBuffer.toString();
	}
	
	public void writeLastBoundaryIfNeeds() {
		if (this.isSetLast)
			;
		while (true) {
			try {
				ByteArrayOutputStream localByteArrayOutputStream = this.out;
				StringBuilder localStringBuilder = new StringBuilder()
						.append("\r\n--");
				String str = this.boundary;
				byte[] arrayOfByte = (str + "--\r\n").getBytes();
				localByteArrayOutputStream.write(arrayOfByte);
				this.isSetLast = true;
				return;
			} catch (IOException localIOException) {
				localIOException.printStackTrace();

			}
		}
	}
	
	@Override
	public void consumeContent() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InputStream getContent() throws IOException, IllegalStateException {
		// TODO Auto-generated method stub
		byte[] arrayOfByte = this.out.toByteArray();
		return new ByteArrayInputStream(arrayOfByte);
	}

	@Override
	public Header getContentEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getContentLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Header getContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isChunked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRepeatable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStreaming() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeTo(OutputStream outstream) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
