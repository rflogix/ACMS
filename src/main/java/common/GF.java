package common;

import java.io.BufferedReader;
import java.io.File;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import acms.common.GC;
import common.api.ApiDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GF {
	
	// **************************************************************************
	// 문자열 관련
	// **************************************************************************

	// getString 함수
	public static String getString(Object p_Object) {
		String strReturn = "";

		try {
			if (p_Object != null) {
				strReturn = StringUtils.trimToEmpty(p_Object.toString());
			}

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return strReturn;
	}

	// left 함수
	public static String left(Object p_Object, int p_Count) {
		String strReturn = "";

		try {
			if (p_Object != null) {
				strReturn = StringUtils.left(getString(p_Object.toString()), p_Count);
			}

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return strReturn;
	}

	// right 함수 (오른쪽부터 일정개수 자르기)
	public static String right(Object p_Object, int p_Count) {
		String strReturn = "";

		try {
			if (p_Object != null) {
				strReturn = StringUtils.right(getString(p_Object.toString()), p_Count);
			}

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return strReturn;
	}

	// mid 함수 (가운데부터 일정개수 자르기)
	public static String mid(Object p_Object, int p_StartPosition, int p_Count) {
		String strReturn = "";

		try {
			if (p_Object != null) {
				strReturn = StringUtils.mid(getString(p_Object.toString()), p_StartPosition, p_Count);
			}

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return strReturn;
	}

	public static String mid(Object p_Object, int p_StartPosition) {
		String strReturn = "";

		try {
			if (p_Object != null) {
				strReturn = p_Object.toString();
				int intLength = strReturn.length();
				if (p_StartPosition > 0) {
					strReturn = (intLength < p_StartPosition) ? "" : strReturn.substring(p_StartPosition - 1);
				}
			}

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return strReturn;
	}

	// JSON 으로 변환
	public static String getJson(Object p_obj) {
		String strReturn = "";

		try {
			if (p_obj != null) {
				strReturn = GF.getString(GF.getObjectMapper().writeValueAsString(p_obj));
			}

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return strReturn;
	}
	public static String getJsonPretty(Object p_obj) {
		String strReturn = "";

		try {
			if (p_obj != null) {
				strReturn = GF.getString(GF.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(p_obj));
			}

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return strReturn;
	}
	

	// String(json) 을 HashMap 으로 변환
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> getMap(String p_obj) {
		HashMap<String, Object> mapReturn = new HashMap<String, Object>();

		try {
			String json = GF.getString(p_obj);
			if (json.equals("") == false) {
				mapReturn = GF.getObjectMapper()
						// .enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT) // 비어있는 배열이면 NULL 로 처리
						// .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT) // 비어있는 String 이면 NULL 로 처리
						.readValue(json, HashMap.class);
			}

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return mapReturn;
	}




	// **************************************************************************
	// 숫자 관련
	// **************************************************************************

	// getInt 함수
	public static int getInt(Object p_Object) {
		int intReturn = 0;

		try {
			if (p_Object != null) {
				String strReturn = StringUtils.trimToEmpty(p_Object.toString());
				intReturn = Integer.parseInt(strReturn.equals("") ? "0" : strReturn);
			}

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return intReturn;
	}

	// getLong 함수
	public static long getLong(Object p_Object) {
		long lngReturn = 0;

		try {
			if (p_Object != null) {
				String strReturn = StringUtils.trimToEmpty(p_Object.toString());
				lngReturn = Long.parseLong(strReturn.equals("") ? "0" : strReturn);
			}

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return lngReturn;
	}

	// getBigDecimal 함수
	public static BigDecimal getBigDecimal(Object p_Object) {
		BigDecimal bdcReturn = new BigDecimal("0");

		try {
			if (p_Object != null) {
				String number = GF.getString(p_Object);
				if (number.equals("") == false) {
					bdcReturn = new BigDecimal(number);
				}
			}

		} catch (Exception e) {
			bdcReturn = new BigDecimal("0");
			log.debug("{}", e.toString());
		}

		return bdcReturn;
	}

	// 숫자인지 체크하는 함수
	public static boolean isNumber(String p_Number) {
		boolean result = true;

		try {
			String strCheck = p_Number;
			if ((strCheck == null) || (strCheck.equals(""))) {
				result = false;
			} else {
				// 콤마, 쩜 떼고 검사
				strCheck = strCheck.replaceAll(",", "");
				strCheck = strCheck.replaceAll("\\.", "");
				// strCheck = strCheck.replace(".", "");

				// 마이너스 값이라면 마이너스 떼고 검사
				if (strCheck.substring(0, 1).equals("-") == true) {
					strCheck = strCheck.substring(1);
				}

				for (int i = 0; i < strCheck.length(); i++) {
					char c = strCheck.charAt(i);
					if (c < 48 || c > 59) {
						result = false;
						break;
					}
				}
			}

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return result;
	}




	// **************************************************************************
	// HashMap 관련
	// **************************************************************************

	// getMap 함수
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> getMap(Object p_Object) {
		HashMap<String, Object> mapReturn = new HashMap<String, Object>();

		try {
			if (p_Object != null) {
				if (p_Object.getClass().getName().indexOf("HashMap") > -1) {
					mapReturn = (HashMap<String, Object>) p_Object;

				} else {
					mapReturn = GF.getObjectMapper().convertValue(p_Object, HashMap.class);
				}
			}

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return mapReturn;
	}
	
	// Multipart Request 를 HashMap 으로 변환
	public static HashMap<String, Object> getMap(MultipartHttpServletRequest request) {
		HashMap<String, Object> mapParam = new HashMap<String, Object>();

		try {
			// parameter 추가
			Enumeration<String> params = request.getParameterNames();
			while (params.hasMoreElements()) {
				String key = params.nextElement();
				mapParam.put(key, request.getParameter(key));
			}

			// 파일 추가
			Iterator<String> fileNames = request.getFileNames();
			while (fileNames.hasNext()) {
				String key = fileNames.next();
				List<MultipartFile> fileList = request.getFiles(key);
				if (fileList.size() == 1) { // 파일이 1개만 있다면 MultipartFile 로 세팅
					MultipartFile file = fileList.get(0);
					if (file.isEmpty() == false) {
						mapParam.put(key, fileList.get(0));
					}

				} else { // 파일이 여러개라면 List<MultipartFile> 로 세팅
					mapParam.put(key, fileList);
				}
			}

		} catch (Exception e) {
			mapParam = new HashMap<String, Object>();
			log.debug("{}", e.toString());
		}

		return mapParam;
	}




	// **************************************************************************
	// 날짜 관련
	// **************************************************************************

	// 날짜 조정 - ex) 2016-01-01 13:04:45 형태로 들어오면 2016-03-03 13:04:45 형태로 반환
	public static String addTime(String p_Date, int p_AddType, int p_AddTime) {
		String strReturn = "";

		try {
			Calendar calDate = Calendar.getInstance();
			int intYear, intMonth, intDay, intHour, intMinute, intSecond;

			if ((p_Date != null) && (p_Date.trim().equals("") == false)) {
				// 현재 날짜 세팅
				strReturn = p_Date.replaceAll("-", "").replaceAll("\\.", "").replaceAll("/", "").replaceAll(":", "").replaceAll(" ", "");
				if (strReturn.length() == 8) {
					strReturn = strReturn + "000000";
				}
				strReturn = left(strReturn, 14);
				if (strReturn.length() == 14) {
					intYear = Integer.parseInt(strReturn.substring(0, 4));
					intMonth = Integer.parseInt(strReturn.substring(4, 6)) - 1; // 1월이 0 부터 시작하므로
					intDay = Integer.parseInt(strReturn.substring(6, 8));
					intHour = Integer.parseInt(strReturn.substring(8, 10));
					intMinute = Integer.parseInt(strReturn.substring(10, 12));
					intSecond = Integer.parseInt(strReturn.substring(12, 14));

					// 날짜 수정
					calDate.set(intYear, intMonth, intDay, intHour, intMinute, intSecond);
					calDate.add(p_AddType, p_AddTime);

					// 반환 날짜 세팅
					intYear = calDate.get(Calendar.YEAR);
					intMonth = calDate.get(Calendar.MONTH) + 1; // 1월이 0 부터 시작하므로
					intDay = calDate.get(Calendar.DATE);
					intHour = calDate.get(Calendar.HOUR_OF_DAY);
					intMinute = calDate.get(Calendar.MINUTE);
					intSecond = calDate.get(Calendar.SECOND);

					strReturn = Integer.toString(intYear) + "-" + right("0" + intMonth, 2) + "-" + right("0" + intDay, 2) + " " + right("0" + intHour, 2) + ":" + right("0" + intMinute, 2) + ":"
							+ right("0" + intSecond, 2);
				} else {
					strReturn = "";
				}

			} else {
				strReturn = "";
			}

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return strReturn;
	}

	public static String addTime_Year(String p_Date, int p_AddTime) {
		return addTime(p_Date, Calendar.YEAR, p_AddTime);
	}

	public static String addTime_Month(String p_Date, int p_AddTime) {
		return addTime(p_Date, Calendar.MONTH, p_AddTime);
	}

	public static String addTime_Day(String p_Date, int p_AddTime) {
		return addTime(p_Date, Calendar.DATE, p_AddTime);
	}

	public static String addTime_Second(String p_Date, int p_AddTime) {
		return addTime(p_Date, Calendar.SECOND, p_AddTime);
	}

	// 날짜 형식 (YYYY-MM-DD HH:MM:SS)
	public static String formatDateTime(String p_Date) {
		String dateTime = GF.getString(p_Date).replaceAll("[^0-9]", "");
		dateTime = left(dateTime + "000000", 14);
		if (dateTime.length() == 14) {
			dateTime = dateTime.substring(0, 4) + "-" + dateTime.substring(4, 6) + "-" + dateTime.substring(6, 8) + " " + dateTime.substring(8, 10) + ":" + dateTime.substring(10, 12) + ":"
					+ dateTime.substring(12, 14);

		} else {
			dateTime = "";
		}

		return dateTime;
	}

	// 날짜 형식 (YYYY-MM-DD)
	public static String formatDate(String p_Date) {
		String dateTime = GF.getString(p_Date).replaceAll("[^0-9]", "");
		dateTime = left(dateTime, 8);
		if (dateTime.length() == 8) {
			dateTime = dateTime.substring(0, 4) + "-" + dateTime.substring(4, 6) + "-" + dateTime.substring(6, 8);

		} else {
			dateTime = "";
		}

		return dateTime;
	}
	
	// 날짜 비교
	public static int compareDateTime(String date1, String date2) {
		int result = -99;
		
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime localdate1 = LocalDateTime.parse(GF.formatDateTime(date1), formatter);
			LocalDateTime localdate2 = LocalDateTime.parse(GF.formatDateTime(date2), formatter);
			result = localdate1.compareTo(localdate2);
			
		} catch(Exception e) {
			log.error("{}", e.toString());
		}
		
		return result;
	}
	public static int compareDate(String date1, String date2) {
		int result = -99;
		
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate localdate1 = LocalDate.parse(GF.formatDate(date1), formatter);
			LocalDate localdate2 = LocalDate.parse(GF.formatDate(date2), formatter);
			result = localdate1.compareTo(localdate2);
			
		} catch(Exception e) {
			log.error("{}", e.toString());
		}
		
		return result;
	}
	
	
	
	
	// **************************************************************************
	// 보안 관련
	// **************************************************************************

	// base64 로 만들기
	public static String base64(byte[] binaryData) {
		String base64 = "";

		try {
			if (binaryData != null) {
				base64 = new String(Base64.getEncoder().encode(binaryData), "UTF-8");
			}

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return base64;
	}

	public static String base64(String strData) {
		String base64 = "";

		if (strData != null) {
			base64 = GF.base64(strData.getBytes());
		}

		return base64;
	}

	// 복호화 가능한 암호화
	public static String encrypt(String text) {
		String encrypt = "";

		try {
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.ENCRYPT_MODE, GF.getSecretKeySpec(), new IvParameterSpec(GF.getSecretKey().getBytes()));
			byte[] encryptByte = c.doFinal(text.getBytes("UTF-8"));
			encrypt = new String(Base64.getEncoder().encode(encryptByte));

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return encrypt;
	}

	// 복호화
	public static String decrypt(String encrypt) {
		String decrypt = "";

		try {
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, GF.getSecretKeySpec(), new IvParameterSpec(GF.getSecretKey().getBytes()));
			byte[] decryptByte = Base64.getDecoder().decode(encrypt.getBytes());
			decrypt = new String(c.doFinal(decryptByte), "UTF-8");

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return decrypt;
	}

	// 암호화 key 가져오기
	private static String getSecretKey() {
		return GC.ENCRYPT_KEY.substring(0, GC.ENCRYPT_KEY_SIZE); // 최소 16자리
	}

	// 암호화 spec 가져오기
	private static Key getSecretKeySpec() {
		Key key = null;

		try {
			byte[] keyBytes = new byte[GC.ENCRYPT_KEY_SIZE];
			byte[] b = GC.ENCRYPT_KEY.getBytes("UTF-8");
			int len = b.length;
			if (len > keyBytes.length) {
				len = keyBytes.length;
			}
			System.arraycopy(b, 0, keyBytes, 0, len);
			key = new SecretKeySpec(keyBytes, "AES");

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return key;
	}

	// 비밀번호 해싱
	public static String makePassword(String password, String salt) {
		String newPassword = "";

		try {
			if ((GF.getString(password).equals("") == false) && (GF.getString(salt).equals("") == false)) {
				MessageDigest md = MessageDigest.getInstance("SHA-256"); // SHA-256 해시함수를 사용
				byte[] passwordByte = password.getBytes();

				// key-stretching
				for (int i = 0; i < 10000; i++) {
					String temp = byteToString(passwordByte) + salt; // 패스워드와 Salt 를 합쳐 새로운 문자열 생성
					md.update(temp.getBytes()); // temp 의 문자열을 해싱하여 md 에 저장해둔다
					passwordByte = md.digest(); // md 객체의 다이제스트를 얻어 password 를 갱신한다
				}

				newPassword = byteToString(passwordByte);
			}

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return newPassword;
	}

	// SALT 값 생성
	public static String getSalt(int saltSize) {
		String salt = "";

		try {
			SecureRandom rnd = new SecureRandom();
			byte[] temp = new byte[saltSize];
			rnd.nextBytes(temp);
			salt = byteToString(temp);

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return salt;
	}

	public static String getSalt() {
		return getSalt(GC.PW_SALT_SIZE);
	}

	// 바이트 값을 16진수로 변경해준다
	public static String byteToString(byte[] temp) {
		String strReturn = "";

		try {
			StringBuilder sb = new StringBuilder();
			for (byte a : temp) {
				sb.append(String.format("%02x", a));
			}
			strReturn = sb.toString();

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return strReturn;
	}




	// **************************************************************************
	// 쿠키 관련
	// **************************************************************************

	// 쿠키 설정
	public static void setCookie(String key, String value, HttpServletResponse response, int age) {
		try {
			Cookie cookie = new Cookie(key, value);
			cookie.setMaxAge(age); // 쿠키 유효 기간
			cookie.setPath("/"); // 모든 경로에서 접근 가능하도록 설정
			response.addCookie(cookie);

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}
	}

	public static void setCookie(String key, String value, HttpServletResponse response) {
		setCookie(key, value, response, GC.COOKIE_TIME);
	}

	// 쿠키 가져오기
	public static String getCookie(String key, HttpServletRequest request) {
		String value = "";

		try {
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals(key)) { // 키값이 같은 쿠키 값만 가져오기
						value = GF.getString(cookie.getValue());
						break;
					}
				}
			}

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return value;
	}

	public static HashMap<String, Object> getCookie(HttpServletRequest request) {
		HashMap<String, Object> cookieMap = new HashMap<String, Object>();

		try {
			Cookie[] cookies = request.getCookies(); // 모든 쿠키 가져오기
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					cookieMap.put(cookie.getName(), GF.getString(cookie.getValue()));
				}
			}

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return cookieMap;
	}

	// 쿠키 삭제
	public static void deleteCookie(String key, HttpServletResponse response) {
		try {
			Cookie cookie = new Cookie(key, null);
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}
	}




	// **************************************************************************
	// 파일 관련
	// **************************************************************************

	// makeDir 함수
	public static boolean makeDir(String path) {
		boolean blnReturn = false;

		try {
			path = GF.getString(path);
			if (path.equals("") == false) {
				File file = new File(path);
				file.mkdirs();
				blnReturn = true;
			}

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return blnReturn;
	}

	// MultipartFile 가져오기
	public static MultipartFile getMultipartFile(byte[] bytes) {
		MultipartFile multipartFile = null;

		try {
			int fileSize = (int) 1024 * 1000 * 10;
			String fileName = System.currentTimeMillis() + ".tmp";
			File repository = new File(GF.getRootPath() + "/" + GC.FILE_TEMP_PATH);

			DiskFileItem fileItem = new DiskFileItem("file", null, false, fileName, fileSize, repository);
			fileItem.getOutputStream().write(bytes);
			multipartFile = new CommonsMultipartFile(fileItem);

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return multipartFile;
	}

	public static MultipartFile getMultipartFile(String base64) {
		return GF.getMultipartFile(Base64.getDecoder().decode(base64));
	}

	// 파일 생성
	public static File makeFile(String path, String fileName) {
		File file = null;

		try {
			// 경로 생성
			String fullPath = String.format("%s/%s", GF.getRootPath(), path);
			GF.makeDir(fullPath);

			// 파일 이름, 확장자 설정
			String fileFullName = fileName;
			String extension = "dat";
			int indexDot = fileFullName.indexOf(".");
			if (indexDot > -1) {
				fileName = fileFullName.substring(0, indexDot);
				extension = fileFullName.substring(indexDot + 1);
			}
			fileFullName = String.format("%s.%s", fileName, extension);

			// 파일명 중복 안될때까지 파일명 재생성
			file = new File(String.format("%s/%s", fullPath, fileFullName));
			int index = 0;
			while (file.exists() == true) { // 파일이 존재한다면 파일명 재생성
				fileFullName = String.format("%s_%d.%s", fileName, (index++), extension);
				file = new File(String.format("%s/%s", fullPath, fileFullName));
			}

			// 파일 생성
			file.createNewFile();

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return file;
	}

	public static File makeFile(String fileName) {
		return makeFile(GC.FILE_TEMP_PATH, fileName);
	}

	// 시스템 루트 경로 가져오기
	public static String getRootPath() {
		return (new File("").getAbsolutePath()).replaceAll("\\\\", "/");
	}
	
	
	
	
	// **************************************************************************
	// DB 관련
	// **************************************************************************

	// 트랜잭션 생성
	public static TransactionStatus getTransaction(DataSourceTransactionManager transactionManager) {
		TransactionStatus ts = null;

		try {
			if (transactionManager != null) {
				ts = transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED));
			}

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return ts;
	}



	
	//**************************************************************************
	// api 관련
	//**************************************************************************
	
	// API 전송
	public static HashMap<String, Object> sendApi(ApiDTO apiParam) {
		HashMap<String, Object> mapReturn = new HashMap<String, Object>();
		
		try {
			// url
			String url = GF.getString(apiParam.getUrl());
			if (url.equals("")) { // url 값 최우선
				String uri = GF.getString(apiParam.getUri());
				if (uri.equals("") == false) {
					HttpServletRequest request = apiParam.getRequest();
					if (request != null) {
						// getRequestURL() : http://test.rflogix.com:8080/user/logout, getRequestURI() : /user/logout
						url = request.getRequestURL().toString().replaceFirst(request.getRequestURI(), "") + uri;
						
					} else {
						url = uri; // request 가 없다면 uri 로만 설정
					}
				}
			}
			
			// header
			HashMap<String, Object> headerMap = apiParam.getHeader();
			if (headerMap == null) {
				headerMap = new HashMap<String, Object>();
			}
			headerMap.putIfAbsent(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			HttpHeaders header = new HttpHeaders();
			for (String key : headerMap.keySet()) {
				header.add(key, GF.getString(headerMap.get(key)));
			}
			
			// Content-Type
			MediaType contentType = apiParam.getContentType();
			if (contentType == null) {
				contentType = MediaType.APPLICATION_JSON;
			}
			header.setContentType(contentType);
			
			// param
			HashMap<String, Object> param = GF.getMap(apiParam.getParam());
			
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<?> response;
			
			// GET 일때
			if (GF.getString(apiParam.getMethod()).toUpperCase().equals("GET")) {
				
				// param url 설정
				UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
				for (String key : param.keySet()) {
					String value = GF.getString(param.get(key));
					if (value.equals("") == false) {
						uriBuilder.queryParam(key, value); // URLEncoder.encode(value, "UTF-8")
					}
				}
				url = uriBuilder.build().encode().toUri().toString();
				
				response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(header), HashMap.class);
				
			// DELETE 일때
			} else if (GF.getString(apiParam.getMethod()).toUpperCase().equals("DELETE")) {
				response = restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(header), HashMap.class);
				
			// POST 일때
			} else {
				// body
				if (contentType == MediaType.APPLICATION_JSON) {
					response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(param, header), HashMap.class);
					
				} else {
					MultiValueMap<String, Object> body = new LinkedMultiValueMap<String, Object>();
					if (param == null) {
						param = new HashMap<String, Object>();
					}
					for (String key : param.keySet()) {
						// 파일이 1개만 있을때
						if (param.get(key).getClass().getName().indexOf("MultipartFile") > -1) {
							MultipartFile multipartFile = (MultipartFile)param.get(key);
							ByteArrayResource contentsAsResource = new ByteArrayResource(multipartFile.getBytes()){
								@Override
								public String getFilename(){
									return multipartFile.getOriginalFilename();
								}
							};
							body.add(key, contentsAsResource);
					
						} else {
							body.add(key, param.get(key));
						}
					}
					response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body, header), HashMap.class);
				}
			}
			
			mapReturn.put("statusCode", response.getStatusCodeValue());
			HashMap<String, Object> body = GF.getMap(response.getBody());
			if (body.keySet().size() > 0) {
				mapReturn.put("body", body);
			}
			
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			mapReturn.put("statusCode", e.getRawStatusCode());
			mapReturn.put("statusText", GF.getString(e.getStatusText()));
			HashMap<String, Object> body = GF.getMap(e.getResponseBodyAsString());
			if (body.keySet().size() > 0) {
				mapReturn.put("body", body);
			}
			log.debug("{}", e.toString());
			
		} catch(Exception e) {
			mapReturn.put("statusCode", 999);
			mapReturn.put("statusText", "기타 오류");
			log.debug("{}", e.toString());
		}
		
		return mapReturn;
	}
	
	
	

	// **************************************************************************
	// request 관련
	// **************************************************************************

	// request 에서 에러코드 가져오기
	public static String getErrorCode(HttpServletRequest request) {
		String strReturn = "";

		try {
			strReturn = getString(request.getAttribute(GC.KEY_ERROR_CODE));

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return strReturn;
	}

	// URL 루트 구하기
	public static String getUrlRoot(HttpServletRequest request) {
		String strReturn = "";

		try {
			String url = request.getRequestURL().toString(); // http://test.rflogix.com:8080/contextPath/user/login
			String servletPath = request.getServletPath(); // /user/login
			strReturn = url.replaceFirst(servletPath, ""); // http://test.rflogix.com:8080/contextPath;

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return strReturn;
	}

	// Client IP 가져오기
	public static String getClientIP(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null) {
			ip = request.getHeader("Proxy-Client-IP");
			if (ip == null) {
				ip = request.getHeader("WL-Proxy-Client-IP");
				if (ip == null) {
					ip = request.getHeader("HTTP_CLIENT_IP");
					if (ip == null) {
						ip = request.getHeader("HTTP_X_FORWARDED_FOR");
						if (ip == null) {
							ip = request.getRemoteAddr();
						}
					}
				}
			}
		}

		return GF.getString(ip);
	}

	// Local Ip 가져오기
	public static String getLocalIp() {
		String ip = "";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return ip;
	}
	
	// localhost 인지 체크
	public static boolean isLocahostUrl(String url) {
		boolean localhostYn = false;
		
		try {
			String checkUrl = GF.getString(url).replaceFirst("http://", "").replaceFirst("https://", "");
			String[] arrUrl = checkUrl.split("\\/");
			if (arrUrl.length > 0) {
				checkUrl = arrUrl[0];
				
				// 포트 분리
				arrUrl = checkUrl.split(":");
				if (arrUrl.length > 0) {
					checkUrl = arrUrl[0];
				}
				
				// 로컬 호스트 체크
				if (checkUrl.equals("localhost")) {
					localhostYn = true;
				}
			}
			
		} catch (Exception e) {
			log.debug("{}", e.toString());
		}
		
		return localhostYn;
	}

	// 리소스 여부 체크
	public static boolean isResourceUri(String uri) {
		boolean 리소스여부 = false;
		
		try {
			String 요청URI = uri;
			for (String 리소스경로 : GC.RESOURCE_PATHS) {
				if (요청URI.indexOf("/" + 리소스경로 + "/") > -1) {
					리소스여부 = true;
					break;
				}
			}
			
		} catch (Exception e) {
			log.debug("{}", e.toString());
		}
		
		return 리소스여부;
	}
	
	
	
	
	// **************************************************************************
	// 세션 관련
	// **************************************************************************
/*
	// session 에서 변수 hashmap 가져오기
	public static HashMap<String, Object> getSessionMap(HttpSession session) {
		HashMap<String, Object> mapSession = new HashMap<String, Object>();

		try {
			if (session != null) {
				mapSession = GF.getMap(session.getAttribute(GC.KEY_SESSION));
				if (mapSession == null) {
					mapSession = new HashMap<String, Object>();
				}
			}

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return mapSession;
	}
	public static HashMap<String, Object> getSessionMap(HttpServletRequest request) {
		return getSessionMap(request.getSession());
	}

	// session 에 변수 hashmap 세팅
	public static HashMap<String, Object> setSessionMap(HttpSession session, HashMap<String, Object> mapSession) {
		try {
			if (mapSession == null) {
				mapSession = new HashMap<String, Object>();
			}
			if (session != null) {
				session.setAttribute(GC.KEY_SESSION, mapSession);
			}

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return mapSession;
	}
	public static HashMap<String, Object> setSessionMap(HttpServletRequest request, HashMap<String, Object> mapSession) {
		return setSessionMap(request.getSession(), mapSession);
	}
	*/
	// request parameter 를 hashmap 으로
	public static HashMap<String, Object> getParameterMap(HttpServletRequest request) {
		HashMap<String, Object> mapParam = new HashMap<String, Object>();

		try {
			// parameter 추가
			Enumeration<String> params = request.getParameterNames();
			while (params.hasMoreElements()) {
				String key = params.nextElement();
				mapParam.put(key, request.getParameter(key));
			}

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return mapParam;
	}
	
	// request body 를 hashmap 으로
	public static HashMap<String, Object> getBodyMap(HttpServletRequest request) {
		HashMap<String, Object> mapParam = new HashMap<String, Object>();
		
		BufferedReader bufferedReader = null;
		
		try {
			StringBuilder stringBuilder = new StringBuilder();
			bufferedReader = request.getReader();
			char[] charBuffer = new char[128];
			int bytesRead = -1;
			while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
				stringBuilder.append(charBuffer, 0, bytesRead);
			}
			mapParam = GF.getMap(stringBuilder.toString());
			
		} catch (Exception e) {
			log.debug("{}", e.toString());
			
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
					
				} catch (Exception e) {
					log.debug("{}", e.toString());
				}
			}
		}
		
		return mapParam;
	}
	
	// request header 를 hashmap 으로
	public static HashMap<String, Object> getHeaderMap(HttpServletRequest request) {
		HashMap<String, Object> mapParam = new HashMap<String, Object>();

		try {
			Enumeration<String> values = request.getHeaderNames();
			if (values != null) {
				while (values.hasMoreElements()) {
					String key = values.nextElement();
					/*Enumeration<String> headers = request.getHeaders(key);
					while (headers.hasMoreElements()) {
						mapParam.put(key, headers.nextElement());
					}*/
					mapParam.put(key, request.getHeader(key));
				}
			}

		} catch (Exception e) {
			log.debug("{}", e.toString());
		}

		return mapParam;
	}
	


	// **************************************************************************
	// jackson 관련
	// **************************************************************************

	// objectMapper 는 전역으로 두고 쓴다 - 생성 비용이 비싸기때문
	private static ObjectMapper objectMapper = new ObjectMapper();

	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public static void setObjectMapper(ObjectMapper p_objectMapper) {
		if (p_objectMapper != null) {
			objectMapper = p_objectMapper;
		}
	}

	// Object 를 DTO 로 변환
	public static <T> T getDto(Object fromValue, Class<T> toValueType) {
		return GF.getObjectMapper().convertValue(fromValue, toValueType);
	}
}
