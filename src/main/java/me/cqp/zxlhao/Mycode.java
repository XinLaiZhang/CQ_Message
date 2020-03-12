package me.cqp.zxlhao;

public class Mycode
{
	/**
	 * 对字符串,进行解码
	 * 
	 * @param str
	 * @return
	 */
	public static String enMycode(String str)
	{
		return str.replaceAll("%26",",").replaceAll("%25", "%");
	}
	/**
	 * 对,进行编码
	 * 使用%26替代,使用%25替代%
	 * @param str
	 * @return
	 */
	public static String deMycode(String str)
	{
		return str.replaceAll(",","%26").replaceAll("%", "%25");
	}
}
