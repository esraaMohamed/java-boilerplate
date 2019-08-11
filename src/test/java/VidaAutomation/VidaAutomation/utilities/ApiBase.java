package VidaAutomation.VidaAutomation.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ApiBase {

	public static Properties properties;
	public enum RESPONSE_CODES {
		RESPONSE_CODE_100(100, "Continue"),
		RESPONSE_CODE_101(101, "Switching Protocols"),
		RESPONSE_CODE_102(102, "Processing"),
		RESPONSE_CODE_200(200, "OK"),
		RESPONSE_CODE_201(201, "Created"),
		RESPONSE_CODE_202(202, "Accepted"),
		RESPONSE_CODE_203(203, "Non-Authoritative Information"),
		RESPONSE_CODE_204(204, "No Content"),
		RESPONSE_CODE_205(205, "Reset Content"),
		RESPONSE_CODE_206(206, "Partial Content"),
		RESPONSE_CODE_207(207, "Multi-Status"),
		RESPONSE_CODE_208(208, "Already Reported"),
		RESPONSE_CODE_226(226, "IM Used"),
		RESPONSE_CODE_300(300, "Multiple Choices"),
		RESPONSE_CODE_301(301, "Moved Permanently"),
		RESPONSE_CODE_302(302, "Found"),
		RESPONSE_CODE_303(303, "See Other"),
		RESPONSE_CODE_304(304, "Not Modified"),
		RESPONSE_CODE_305(305, "Use Proxy"),
		RESPONSE_CODE_306(306, "(Unused error)"),
		RESPONSE_CODE_307(307, "Temporary Redirect"),
		RESPONSE_CODE_308(308, "Permanent Redirect"),
		RESPONSE_CODE_400(400, "Bad Request"),
		RESPONSE_CODE_401(401, "Unauthorized"),
		RESPONSE_CODE_402(402, "Payment Required"),
		RESPONSE_CODE_403(403, "Forbidden"),
		RESPONSE_CODE_404(404, "Not Found"),
		RESPONSE_CODE_405(405, "Method Not Allowed"),
		RESPONSE_CODE_406(406, "Not Acceptable"),
		RESPONSE_CODE_407(407, "Proxy Authentication Required"),
		RESPONSE_CODE_408(408, "Request Timeout"),
		RESPONSE_CODE_409(409, "Conflict"),
		RESPONSE_CODE_410(410, "Gone"),
		RESPONSE_CODE_411(411, "Length Required"),
		RESPONSE_CODE_412(412, "Precondition Failed"),
		RESPONSE_CODE_413(413, "Request Entity Too Large"),
		RESPONSE_CODE_414(414, "Request-URI Too Long"),
		RESPONSE_CODE_415(415, "Unsupported Media Type"),
		RESPONSE_CODE_416(416, "Requested Range Not Satisfiable"),
		RESPONSE_CODE_417(417, "Expectation Failed"),
		RESPONSE_CODE_418(418, "I'm a teapot (RFC 2324)"),
		RESPONSE_CODE_420(420, "Enhance Your Calm (Twitter)"),
		RESPONSE_CODE_422(422, "Unprocessable Entity"),
		RESPONSE_CODE_423(423, "Locked"),
		RESPONSE_CODE_424(424, "Failed Dependency"),
		RESPONSE_CODE_425(425, "Reserved for WebDAV"),
		RESPONSE_CODE_426(426, "Upgrade Required"),
		RESPONSE_CODE_428(428, "Precondition Required"),
		RESPONSE_CODE_429(429, "Too Many Requests"),
		RESPONSE_CODE_431(431, "Request Header Fields Too Large"),
		RESPONSE_CODE_444(444, "No Response (Nginx)"),
		RESPONSE_CODE_449(449, "Retry With (Microsoft)"),
		RESPONSE_CODE_450(450, "Blocked by Windows Parental Controls (Microsoft)"),
		RESPONSE_CODE_451(451, "Unavailable For Legal Reasons"),
		RESPONSE_CODE_499(499, "Client Closed Request (Nginx)"),
		RESPONSE_CODE_500(500, "Internal Server Error"),
		RESPONSE_CODE_501(501, "Not Implemented"),
		RESPONSE_CODE_502(502, "Bad Gateway"),
		RESPONSE_CODE_503(503, "Service Unavailable"),
		RESPONSE_CODE_504(504, "Gateway Timeout"),
		RESPONSE_CODE_505(505, "HTTP Version Not Supported"),
		RESPONSE_CODE_506(506, "Variant Also Negotiates (Experimental)"),
		RESPONSE_CODE_507(507, "Insufficient Storage"),
		RESPONSE_CODE_508(508, "Loop Detected"),
		RESPONSE_CODE_509(509, "Bandwidth Limit Exceeded"),
		RESPONSE_CODE_510(510, "Not Extended"),
		RESPONSE_CODE_511(511, "Network Authentication Required"),
		RESPONSE_CODE_589(598, "Network read timeout error"),
		RESPONSE_CODE_599(599, "Network connect timeout error");

		private final int errorCode;
		private final String errorMessage;

		private RESPONSE_CODES(final int errorCode, final String errorMessage) {
			this.errorCode = errorCode;
			this.errorMessage = errorMessage;
		}

		public int getErrorCode() {
			return errorCode;
		}

		public String getErrorMessage() {
			return errorMessage;
		}
	}
	public static void setup() {
		properties = new Properties();
		FileInputStream ip;
		try {
			ip = new FileInputStream(System.getProperty("user.dir")+ File.separator + "resources" + File.separator + "config.properties");
			properties.load(ip);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
