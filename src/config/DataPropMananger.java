package config;


public class DataPropMananger {
	
	public final static int ST_IN = 1;
	public final static int ST_OUT = 0;
	
	public final static int CMD_SYSTEM = 1;
	public final static int SYS_CLOSE = 10;
	public final static int SYS_LOGIN_SUCCESS = 11;
	public final static int SYS_LOGIN_FAILED = 12;
	public final static int SYS_CLIENT_ERROR = 13;
	
	
	public final static int CMD_JMGL = 2;
	public final static int CRIMINAL_UPDATE = 211;
	public final static int CRIMINAL_ADD_RECORD = 212;
	public final static int CRIMINAL_CHANGE_RECORD = 213;
	public final static int CRIMINAL_ADD_IN_OUT = 214;
	public final static int VISITOR_UPDATE = 221;
	public final static int VISITOR_ADD_RECORD = 222;
	public final static int VISITOR_CHANGE_RECORD = 223;
	public final static int CAR_UPDATE = 231;
	public final static int CAR_ADD_RECORD = 232;
	public final static int CAR_CHANGE_RECORD = 233;
	public final static int JM_DUTY_GET_CURRENT = 241;
	public final static int JM_DUTY_CHANGE_DUTY = 242;
	public final static int JM_DUTY_CHANGE_INFO = 243;
	public final static int JM_DUTY_CHANGE_NOTICE = 244;
	public final static int JM_DUTY_CHANGE_SAFE = 245;
	public final static int JM_DUTY_GET_HISTORY = 246;
	
	public final static int CMD_ZHZX = 3;
	public final static int ZHZX_TEL_GET_ALL = 311;
	public final static int ZHZX_TEL_ADD = 312;
	public final static int ZHZX_TEL_UPDATE_NEXT = 313;
	public final static int ZHZX_TEL_UPDATE_BACK = 314;
	public final static int ZHZX_TEL_DEL = 315;
	
	public final static int VIEW_ALL = 0;
	public final static int VIEW_UNFINISH_ONLY = 1;
	public final static int VIEW_FINISH_ONLY = 2;
	public final static int VIEW_IN_ONLY = 3;
	public final static int VIEW_OUT_ONLY =4;
	
	public final static String SQL_SERVER_IP = "127.0.0.1";
	public final static String WEB_URL = "http://127.0.0.1/client/version.txt";
	
	
	public static String getCompanyName(int num){
		String name = "";
		switch (num) {
		case 0:
			name = "监狱指挥中心";
			break;
		case 1:
			name = "一分监狱";
			break;
		case 2:
			name = "二分监狱";
			break;
		case 3:
			name = "三分监狱";
			break;
		case 4:
			name = "四分监狱";
			break;
		case 5:
			name = "五分监狱";
			break;
		case 6:
			name = "中心医院";
			break;
		case 11:
			name = "一分监狱监门管理";
			break;
		case 21:
			name = "二分监狱监门管理";
			break;
		case 31:
			name = "三分监狱监门管理";
			break;
		case 41:
			name = "四分监狱监门管理";
			break;
		case 51:
			name = "五分监狱监门管理";
			break;
		case 61:
			name = "中心医院监门管理";
			break;
		default:
			name = "其他";
			break;
		}
		return name;
	}
	
	public static String getInOutString(int num){
		if(num == ST_IN) return "进监门";
		else if(num == ST_OUT) return "出监门";
		return "";
	}
}
