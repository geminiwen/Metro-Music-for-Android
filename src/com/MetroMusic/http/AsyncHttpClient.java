package com.MetroMusic.http;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.SyncBasicHttpContext;

import android.content.Context;

import com.MetroMusic.activity.R;
import com.MetroMusic.dao.CookieDAO;
import com.MetroMusic.dbhelper.DataBaseHelper;


public class AsyncHttpClient extends DefaultHttpClient{
	private static final int DEFAULT_MAX_CONNECTIONS = 10;
	private static final int DEFAULT_MAX_RETRIES = 3;
	private static final int DEFAULT_SOCKET_TIMEOUT = 8000;
	private static final String ENCODING_GZIP = "gzip";
	private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	private static final String VERSION = "0.1";
	private static int maxConnections = 10;
	private static int socketTimeout = 8000;
	private ThreadPoolExecutor threadPool;
	private OnRequestCompletionListener listener = null;
	
	private DefaultHttpClient client = null ;
	private HttpContext httpContext;
	
	private static AsyncHttpClient instance = null;

	public AsyncHttpClient()
	{
		BasicHttpParams localBasicHttpParams = new BasicHttpParams();
	    ConnPerRouteBean localConnPerRouteBean = new ConnPerRouteBean(maxConnections);
	    ConnManagerParams.setMaxConnectionsPerRoute(localBasicHttpParams, localConnPerRouteBean);
	    ConnManagerParams.setMaxTotalConnections(localBasicHttpParams, 10);
	    
	    HttpConnectionParams.setSoTimeout(localBasicHttpParams, socketTimeout);
	    HttpConnectionParams.setTcpNoDelay(localBasicHttpParams, true);
	    
	    Object[] arrayOfObject = new Object[1];
	    arrayOfObject[0] = "1.3.1";
	    //String str = String.format("Mozilla/5.0 Coffee-Client/%s)", arrayOfObject);
	    String str   = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)";
	    HttpProtocolParams.setUserAgent(localBasicHttpParams, str);
	    SchemeRegistry localSchemeRegistry = new SchemeRegistry();
	    PlainSocketFactory localPlainSocketFactory = PlainSocketFactory.getSocketFactory();
	    Scheme localScheme1 = new Scheme("http", localPlainSocketFactory, 80);
	    localSchemeRegistry.register(localScheme1);
	    SSLSocketFactory localSSLSocketFactory = SSLSocketFactory.getSocketFactory();
	    Scheme localScheme2 = new Scheme("https", localSSLSocketFactory, 443);
	    localSchemeRegistry.register(localScheme2);
	    
	    ThreadSafeClientConnManager localThreadSafeClientConnManager = new ThreadSafeClientConnManager(localBasicHttpParams, localSchemeRegistry);
	    BasicHttpContext localBasicHttpContext = new BasicHttpContext();
	    this.httpContext = new SyncBasicHttpContext(localBasicHttpContext);
	    this.client = new DefaultHttpClient(localThreadSafeClientConnManager, localBasicHttpParams);
	    this.client.addRequestInterceptor(new AsyncHttpClient$1());
	    this.client.addResponseInterceptor(new AsyncHttpClient$2());
	    this.threadPool = (ThreadPoolExecutor)Executors.newCachedThreadPool();
	}
	
	
	public synchronized HttpResponse executeSync(HttpUriRequest request) throws ClientProtocolException, IOException
	{
		return client.execute(request);
	}
	
	public CookieStore getCustomCookieStore()
	{
		return client.getCookieStore();
	}
	
	public void setCustomeCookieStore(CookieStore cookieStore)
	{
		client.setCookieStore(cookieStore);
	}
	
	public void executeAsync(HttpUriRequest request)
	{
		WorkerRunnable worker = new WorkerRunnable(request);
		this.threadPool.submit(worker);
	}
	
	public void connectionClosed()
	{
		client.getConnectionManager().closeExpiredConnections();
	}
	
	class AsyncHttpClient$1 implements HttpRequestInterceptor
	{
		@Override
		public void process(HttpRequest paramHttpRequest, HttpContext arg1)
				throws HttpException, IOException {
			// TODO Auto-generated method stub
			  if (!paramHttpRequest.containsHeader("Accept-Encoding"))
			      paramHttpRequest.addHeader("Accept-Encoding", "gzip");
		}
	}
	
	class AsyncHttpClient$2 implements HttpResponseInterceptor
	{

		@Override
		public void process(HttpResponse response, HttpContext context)
				throws HttpException, IOException {
			// TODO Auto-generated method stub
			HttpEntity entity = response.getEntity();
            Header ceheader = entity.getContentEncoding();
            if (ceheader != null) {
                HeaderElement[] codecs = ceheader.getElements();
                for (int i = 0; i < codecs.length; i++) {
                    if (codecs[i].getName().equalsIgnoreCase("gzip")) {
                        response.setEntity(
                                new GzipDecompressingEntity(response.getEntity()));
                        return;
                    }
                }
            }
		}
		
	}
	
	
	public static interface OnRequestCompletionListener
	{
		public void OnCompletion(boolean success);
	}
	
	public void setOnRequestCompletionListener(OnRequestCompletionListener listener)
	{
		this.listener = listener;					
	}
	
	
	
	class WorkerRunnable implements Runnable
	{
		HttpUriRequest request;
		
		public WorkerRunnable(HttpUriRequest request)
		{
			this.request = request;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				client.execute(request);
				if(listener!= null)
				{
					listener.OnCompletion(true);
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				listener.OnCompletion(false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				listener.OnCompletion(false);
			}
		}
	}
	
	public static AsyncHttpClient getInstance(Context context)
	{
		makeInstance(context);
		return instance;
	}
	
	private static void makeInstance(Context context)
	{
		if( null == instance )
		{
			String app_name = context.getResources().getString(R.string.app_name);
			CookieDAO	cookieDAO = new CookieDAO(new DataBaseHelper(context,app_name).getWritableDatabase());
			CookieStore cookieStore = cookieDAO.getLastCookieStore();
			instance = new AsyncHttpClient();
			instance.setCustomeCookieStore(cookieStore);
			cookieDAO.dbClose();
			HttpGet httpGet = new HttpGet("http://douban.fm");
			try {
				instance.executeSync(httpGet);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
}
