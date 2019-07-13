package com.larlf.general.core;

/**
 * <h1>常量表</h1>
 * 
 * @author Larlf.Wang
 */
public class Constants
{
	static public String MODE = "develop"; // 系统运行模式，可以是 "develop" 或 "product"
	// 可以使用的常量
	final static public String DevelopMode = "develop"; // 开发模式
	final static public String ProductMode = "product";  // 产品模式

	static public String WEB_ROOT = "";  // 存放页面文件的根路径，需要初始化
	static public String WEB_PLATFORM_PATH = "platform";  // 用于存放Platform所用到资源的目录

	static public String ENCODING = "ISO_8859_1"; // 系统默认的字符集
	static public String ENCODING_PARAMETER = "_ENCODING_"; // 用于存放系统默认字符集的变量名
	static public String LANGUAGE = "en"; // 系统默认的语言
	static public String LANGUAGE_PARAMETER = "_LANGUAGE_"; // 用于存放系统默认语言的变量名
	static public String COUNTRY = "US"; // 系统默认的国家
	static public String COUNTRY_PARAMETER = "_COUNTRY_"; // 用于存放系统默认国家的变量名

	static public String I18N_MESSAGES_MAP = "_i18n_messages_map_"; // 国际化时放在PageContext中的存放国际化文字内容的MAP

	static public String LAYOUT_REQUEST_MAP = "com.opesoft.layout"; // Layout组件中使用，存放在Request中Map的名称

	static public String MVC_SESSION_TOKENS = "_mvc_session_tokens_";  // 存放在Session里的令牌类的字名
	static public String MVC_TOKEN = "_mvc_token_";  // 存放Token的字段名

	static public String VALIDATE_INPUT_PATH = "_input_path_"; // 表单验证中出错返回页的字段名
	static public String VALIDATE_CONFIG = "_validate_config_"; // 表单验证中存放验证规则的配置文件名
	static public String VAIDATE_I18N = "_validate_i18n_";  // 表单中存放国际化信息的字段名
	static public String VALIDATE_STATE = "_validate_state_"; // 表单验证中存放状态的字段名
	static public String VALIDATE_ERRORS = "_validate_errors_"; // 表单验证中存放错误信息的Map
}
