package com.MetroMusic.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class LrcUtil {
	public StringBuffer getUNICODE(String source) {
		StringBuffer sb = new StringBuffer();
		try {
			for (int i = 0; i < source.length(); i++) {
				Character c = Character.toLowerCase(source.charAt(i));
				if (c <= 256) {
					sb.append(Integer.toHexString(c).toUpperCase() + "00");
				}
				else {
					String s = URLEncoder.encode(c.toString(), "UTF-16LE").replace("%", "");
					sb.append(s);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		  return sb;
	}

	private StringBuffer getUTF_8(String source) {
		StringBuffer sb = new StringBuffer();
		try {
			for (int i = 0; i < source.length(); i++) {
				Character c = source.charAt(i);
				if (c <= 256) {
					sb.append(Integer.toHexString(c).toUpperCase());
				} else {
					String s = URLEncoder.encode(c.toString(), "UTF-8")
							.replace("%", "");
					sb.append(s);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;
	}
	public String createCode(String singer, String title, int lrcId)
			   throws UnsupportedEncodingException {
		 StringBuffer qqHexStr = getUTF_8(singer + title);
		 int length = qqHexStr.length() / 2;
		 int[] song = new int[length];
		 for (int i = 0; i < length; i++) {
			 song[i] = Integer.parseInt(qqHexStr.substring(2 * i, 2 * i + 2), 16);
			 }
		 int t1, t2, t3;
		 t1 = t2 = t3 = 0;
		 t1 = (lrcId & 0x0000FF00) >> 8;
		 if ((lrcId & 0x00FF0000) == 0) {
			 t3 = 0x000000FF & ~t1;
		 } else {
			t3 = 0x000000FF & ((lrcId & 0x00FF0000) >> 16);
		 }
		 t3 = t3 | ((0x000000FF & lrcId) << 8);
		 t3 = t3 << 8;
		 t3 = t3 | (0x000000FF & t1);
		 t3 = t3 << 8;
		 if ((lrcId & 0xFF000000) == 0) {
			 t3 = t3 | (0x000000FF & (~lrcId));
		 } else {
			 t3 = t3 | (0x000000FF & (lrcId >> 24));
		 }
		 int j = length - 1;
		 while (j >= 0) {
			 int c = song[j];
			 if (c >= 0x80) c = c - 0x100;
			 t1 = (int) ((c + t2) & 0x00000000FFFFFFFF);
			 t2 = (int) ((t2 << (j % 2 + 4)) & 0x00000000FFFFFFFF);
			 t2 = (int) ((t1 + t2) & 0x00000000FFFFFFFF);
			 j -= 1;
		 }
		 j = 0;
		 t1 = 0;
		 while (j <= length - 1) {
			 int c = song[j];
			 if (c >= 128) c = c - 256;
			 int t4 = (int) ((c + t1) & 0x00000000FFFFFFFF);
			 t1 = (int) ((t1 << (j % 2 + 3)) & 0x00000000FFFFFFFF);
			 t1 = (int) ((t1 + t4) & 0x00000000FFFFFFFF);
			 j += 1;
		 }
		 int t5 = (int) conv(t2 ^ t3);
		 t5 = (int) conv(t5 + (t1 | lrcId));
		 t5 = (int) conv(t5 * (t1 | t3));
		 t5 = (int) conv(t5 * (t2 ^ lrcId));
		 long t6 = (long) t5;
		 if (t6 > 2147483648L)
			 t5 = (int) (t6 - 4294967296L);
		 return String.valueOf(t5);
	}
	private long conv(int i) {
		 long r = i % 4294967296L;
		 if (i >= 0 && r > 2147483648L) r = r - 4294967296L;
		 if (i < 0 && r < 2147483648L)
			 r = r + 4294967296L;
		 return r;
	}
}
