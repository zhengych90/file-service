package com.smc.file.utils;

/**
 * @version 1.0
 * @Author yuchaozh
 * @description 自定义响应码
 */
public class ResponseCode {

	protected ResponseCode() {

	}

	//成功
	public static final int SUCCESS = 200;
	//系统错误
	public static final String ERROR_SYS = "001";
	//数据库操作出错
	public static final String ERROR_ACCESS_DB = "002";
	//用户认证失败
	public static final String ERROR_USER_AUTH = "003";
	//用户名或密码错误
	public static final String ERROR_USERNAME_AUTHCODE_ERROR = "004";
	//会话已过期，请重新登录
	public static final String ERROR_UNAUTH = "005";
	//
	public static final String ERROR_AUTH_EXPIRED = "006";
	//操作对象不存在
	public static final String ERROR_OBJECT_NOT_EXIST = "007";
	//操作对象已存在
	public static final String ERROR_OBJECT_EXIST = "008";
	//服务不可用
	public static final int ERROR_SERVICE_UNAVAILABLE = 405;
	//请求参数无效
	public static final String ERROR_INVALID_PARAMS = "010";
	/**
	 * 缺少请求参数:{0}
	 **/
	public static final String ERROR_MISS_PARAM = "011";
	/**
	 * 超出文件大小限制：{0}
	 **/
	public static final String ERROR_FILE_OVER_MAXSIZE = "012";
	//接收文件出错
	public static final String ERROR_CREATE_LOCAL_FILE = "016";

}
