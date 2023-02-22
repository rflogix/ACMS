package acms.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.fasterxml.jackson.core.type.TypeReference;

import common.api.ApiDTO;
import acms.menu.MenuDTO;
import acms.menu.MenuService;
import acms.user.UserDTO;
import acms.user.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommonService {
	@Value("${server.error.path}") // application.yml
	private String 에러경로; // 설정된 경로와 jsp 파일명 일치여부 체크할것
	
	@Autowired private MenuService menuService;
	@Autowired private UserService userService;
	
	// 페이지 이동
	public String goToPage(HttpServletRequest request, Model model, String 페이지URL) {
		try {
			// 로그인 여부 체크
			UserDTO loginUser = GF.getLoginUser(request);
			boolean loginYn = GF.getLoginYn(loginUser); // 로그인 되었는지 체크
			if (loginYn == false) { // 로그인 안했다면 SSO 로그인 체크
				
				// SSO 에서 ID 가져오기
				String userId = this.getUserIdFromSso(request);
				if (userId.equals("") == false) { // SSO 에서 ID 가져왔다면
					HashMap<String, Object> param = new HashMap<String, Object>();
					param.put("userId", userId);
					param.put(GC.KEY_SSO_YN, "Y");
					HashMap<String, Object> mapReturn = userService.login(param, request, null); // 로그인 처리
					if (GF.getString(mapReturn.get(GC.RESULT_CODE)).equals(GC.RESULT_OK)) { // 로그인 되었다면
						loginUser = (UserDTO)mapReturn.get(GC.RESULT_DTO);
						loginYn = GF.getLoginYn(loginUser); // 로그인 되었는지 체크
					}
				}
			}
			if (loginYn == false) { // 로그인 안했다면 로그인 페이지로
				페이지URL = "/login";
				
			} else { // 로그인 했다면
				
				// 로그인 되었는데 페이지 주소가 들어오지 않았거나, URL이 로그인 화면이라면
				페이지URL = GF.getString(페이지URL);
				if (페이지URL.equals("") || 페이지URL.equals("/") || 페이지URL.equals("/login")) {
					페이지URL = "/index"; 
				}
				
				// index 화면이 아니라면
				if (페이지URL.equals("/index") == false) { // index 화면은 메뉴 선택 체크하지 않아도 된다
					
					// 조회할 메뉴의 url 세팅
					String urlAddr = GF.getString(request.getRequestURI());
					String contextPath = GF.getString(request.getContextPath());
					if (contextPath.equals("")) {
						contextPath = "/";
					}
					if (contextPath.equals("/") == false) {
						urlAddr = urlAddr.replaceFirst(contextPath, ""); // URI 주소에서 contextPath 제외
					}
					if (GF.left(urlAddr, 1).equals("/")) {
						urlAddr = urlAddr.substring(1); // 메뉴 주소가 / 로 시작하면 context path 까지 넣어야 하기때문에 맨앞에 / 삭제
					}
					if (urlAddr.equals("") == false) {
						// 메뉴 조회
						MenuDTO menu = new MenuDTO();
						menu.setMenuUrl(urlAddr);
						menu.setUseYn(1);
						menu.setDeleteYn(-1);
						menu.setUserSeq(loginUser.getUserSeq());
						menu = menuService.selectOne(menu);
						if (menu.getMenuSeq() != 0) {
							model.addAttribute("MENU", GF.getJson(menu)); // page_script.jsp 에서 사용
							
						} else { // 해당 매뉴가 없다면 권한 없음 페이지
							model.addAttribute(GC.KEY_ERROR_CODE, GC.RESULT_NOT_AUTH);
							페이지URL = 에러경로;
						}
						
					} else { // url 이 없다면 없는 페이지로
						model.addAttribute(GC.KEY_ERROR_CODE, GC.RESULT_NOT_EXIST);
						페이지URL = 에러경로;
					}
				}
			}
			
			// 로그인 페이지라면 별도 처리
			if (페이지URL.equals("/login")) {
				// 쿠키에서 로그인시 저장되는 아이디 가져오기 (rememer me)
				model.addAttribute(GC.COOKIE_USER_ID, GF.decrypt(GF.getCookie("userId", request)));
			}
			
		} catch(Exception e) {
			페이지URL = 에러경로;
			log.error("{}", e.toString());
		}
		
		return 페이지URL;
	}
	
	// SSO 에서 userId 가져오기
	private String getUserIdFromSso(HttpServletRequest request) {
		String userId = "";
		
		try {
			String ssoKey = GF.getCookie("ssoKey", request);
			if (ssoKey.equals("") == false) {
				ApiDTO apiParam = new ApiDTO();
				apiParam.setUrl((GC.SSO_SECURE_YN ? "https://" : "http://") + GC.SSO_URL);
				apiParam.addParam("ssoKey", GF.getCookie("ssoKey", request));
				apiParam.setMethod("get");
				HashMap<String, Object> response = GF.sendApi(apiParam);
				if (GF.getInt(response.get("statusCode")) == 200) {
					HashMap<String, Object> body = GF.getMap(response.get("body"));
					if (GF.getString(body.get("resultCode")).equals("00")) {
						userId = GF.getString(body.get("userId"));
					}
				}
			}
			
		} catch(Exception e) {
			log.error("{}", e.toString());
		}
		
		return userId;
	}
	
	// 그리드를 엑셀로 다운로드
	public ResponseEntity<?> gridExcelDownload(HashMap<String, Object> mapParam) {
		ResponseEntity<?> responseEntity;
		
		Workbook wb = null;
		
		try {
			// 파일명
			String fileName = "다운로드";
			String fileSuffix = ".xlsx";
			
			// 엑셀 워크북, 시트 생성
			wb = new XSSFWorkbook();
			Sheet sheet = wb.createSheet();
			
			// 숫자 포맷
			CellStyle numberCellStyle = wb.createCellStyle();
			numberCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
			
			// 헤더
			Row row = sheet.createRow(0);
			List<HashMap<String, Object>> columns = GF.getObjectMapper().readValue(GF.getString(mapParam.get("columns")), new TypeReference<List<HashMap<String, Object>>>(){});
			for (int colIndex = 0; colIndex < columns.size(); colIndex++) {
				HashMap<String, Object> column = columns.get(colIndex);
				Cell cell = row.createCell(colIndex);
				cell.setCellValue(GF.getString(column.get("name")));
			}
			
			// 바디
			int rowNum = 1;
			List<HashMap<String, Object>> rows = GF.getObjectMapper().readValue(GF.getString(mapParam.get("rows")), new TypeReference<List<HashMap<String, Object>>>(){});
			for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
				HashMap<String, Object> data = rows.get(rowIndex);
				row = sheet.createRow(rowNum++);
				
				for (int colIndex = 0; colIndex < columns.size(); colIndex++) {
					HashMap<String, Object> column = columns.get(colIndex);
					
					String id = GF.getString(column.get("id"));
					String value;
					if (id.indexOf(".") == -1) {
						value = GF.getString(data.get(id));
						
					} else {
						String[] arrId = id.split("\\.");
						
						@SuppressWarnings("unchecked")
						HashMap<String, Object> data2 = (HashMap<String, Object>)data.get(arrId[0]);
						value = GF.getString(data2.get(arrId[1]));
					}
					
					// 마스킹 처리
					if (id.indexOf("spsId") > -1) {
						value = "****";
					}
					
					Cell cell = row.createCell(colIndex);
					String type = GF.getString(column.get("type"));
					if ((type.equals("number") == true) && (value.equals("") == false)) {
						cell.setCellValue(Double.valueOf(value));
						
					} else {
						cell.setCellValue(value);
					}
				}
			}
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			wb.write(outputStream);
			wb.close();
			
			responseEntity = ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + fileSuffix)
				.body(new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray())));
			
		} catch(Exception e) {
			if (wb != null) {
				try {wb.close();} catch (Exception ex) {}
			}
			responseEntity = ResponseEntity.noContent().build();
			log.error("{}", e.toString());
		}
		
		return responseEntity;
	}
}