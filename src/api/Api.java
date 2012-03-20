package api;

public class Api {
	  public static final String API_CHANNEL = "http://www.douban.com/j/app/radio/channels";
	  public static final String API_CHECK_HEARTS = "http://www.douban.com/j/app/radio/can_use_offline_channel";
	  public static final String API_HEARTS = "http://www.douban.com/j/app/radio/liked_songs";
	  public static final String API_LOGIN_DOUBAN = "https://www.douban.com/j/app/login";
	  public static final String API_PLAYLIST = "http://www.douban.com/j/app/radio/people";
	  public static final String API_REPORT = "http://www.douban.com/j/app/radio/people";
	  public static final String API_REPORT_OFFLINE = "http://www.douban.com/j/app/radio/play_record";
	  public static final String API_SHARE = "http://www.douban.com/j/app/radio/share_to_douban";
	  public static final String API_SHARE_CHECK = "http://www.douban.com/j/app/radio/can_share_douban_broadcast";
	  public static final String API_SINA_CALLBACK_PREFIX = "http://www.douban.com/j/app/radio/partner/sina/callback";
	  public static final String API_VERIFY_EMAIL = "https://www.douban.com/j/app/radio/bind_email";
	  public static final String API_THIRD_PART_RADIO = "http://douban.fm/j/mine/playlist";
	  private static String CLIENT_INFO;
	  public static final String COOKIE_DOMAIN = "douban.com";
	  private static String DISTRIBUTION_CHANNEL;
	  public static final String DOUBAN_BIND_EMAIL = "http://douban.fm/settings/profile/bind_email";
	  public static final String OP_CANCEL_LIKE = "u";
	  public static final String OP_END = "p";
	  public static final String OP_HATE = "b";
	  public static final String OP_LIKE = "r";
	  public static final String OP_NEW = "n";
	  public static final String OP_PLAY = "p";
	  public static final String OP_REPORT = "e";
	  public static final String OP_SKIP = "s";
	  private static final String TAG = "Api";
	  
}
