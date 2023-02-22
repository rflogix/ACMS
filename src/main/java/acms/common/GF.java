package acms.common;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import lombok.extern.slf4j.Slf4j;
import acms.user.UserDTO;
import common.SearchParamDTO;

@Slf4j
@Configuration
public class GF extends common.GF {

	// **************************************************************************
	// 로그인 관련
	// **************************************************************************
	
	// 로그인 사용자 가져오기
	public static UserDTO getLoginUser(HttpSession session) {
		UserDTO loginUser = new UserDTO();
		
		try {
			Object objUser = session.getAttribute(GC.KEY_LOGIN_USER);
			if (objUser != null) {
				loginUser = (UserDTO)objUser;
			}
			
		} catch(Exception e) {
			log.error("{}", e.toString());
		}
		
		return loginUser;
	}
	public static UserDTO getLoginUser(HttpServletRequest request) {
		return getLoginUser(request.getSession());
	}
	
	// 로그인 여부
	public static boolean getLoginYn(UserDTO userDto) {
		boolean loginYn = false;
		if (userDto != null) {
			if (userDto.getUserSeq() != 0) {
				loginYn = true;
			}
		}
		return loginYn;
	}
	public static boolean getLoginYn(HttpSession session) {
		return getLoginYn(getLoginUser(session));
	}
	public static boolean getLoginYn(HttpServletRequest request) {
		return getLoginYn(request.getSession());
	}
	
	
	
	
	
	// **************************************************************************
	// 검색 관련
	// **************************************************************************
	
	// 검색 파라미터 설정
	public static SearchParamDTO setSearchParam(SearchParamDTO searchParam, int totalCount) {
		try {
			// 페이징 DTO 기본값 세팅
			int showCount = searchParam.getShowCount(); // 화면에 보여질 레코드 수
			int nowPage = 1; // 현재 페이지
			int totalPage = 1; // 총 페이지수
			int startRownum = 1; // 페이징하려는 rownum 시작
			int endRownum = -1; // 페이징하려는 rownum 종료
			int offsetRownum = 0;
			
			if (showCount > 0) { // 보여지는 레코드수가 0보다 크다면
				// 총 페이지수 설정
				totalPage = 0;
				if (totalCount > 0) { // 전체 조회된 레코드수를 화면에 보여질 레코드 수로 나눠서 총 페이지수 구한다
					totalPage = (int)Math.ceil((double)totalCount / (double)showCount);
				}
				if (totalPage <= 0) {
					totalPage = 1;
				}
				
				// 현재 페이지 설정
				nowPage = searchParam.getNowPage();
				if (nowPage > totalPage) { // 현재 페이지가 총 페이지수 보다 크다면 총 페이지수로 조정
					nowPage = totalPage;
				}
				if (nowPage <= 0) {
					nowPage = 1;
				}
				
				// 페이징하려는 ROWNUM 시작/끝 설정
				offsetRownum = (nowPage - 1) * showCount;
				startRownum = offsetRownum + 1;
				endRownum = offsetRownum + showCount;
				
			} else { // 보여지는 레코드수가 0보다 작거나 작다면 -1로 설정하여 모두 검색되게 수정
				showCount = -1;
			}
			
			// 페이징 관련 파라미터 재설정
			searchParam.setNowPage(nowPage);
			searchParam.setShowCount(showCount);
			searchParam.setTotalCount(totalCount);
			searchParam.setTotalPage(totalPage);
			searchParam.setOffsetRownum(offsetRownum);
			searchParam.setStartRownum(startRownum);
			searchParam.setEndRownum(endRownum);
			
		} catch(Exception e) {
			log.error("{}", e.toString());
		}
		
		return searchParam;
	}
	
	// 리턴값에 SQL ERROR 처리 - MS SQL 에 맞는 에러코드 설정
	public static void setResultSqlError(Exception exception, HashMap<String, Object> mapReturn) {
		try {
			if (exception instanceof DataAccessException) {
				SQLException se = (SQLException) ((DataAccessException) exception).getRootCause();
				if (se != null) {
					if (se.getErrorCode() == GC.SQL_ERROR_DUPLICATE) { // PK 중복
						mapReturn.put(GC.RESULT_CODE, GC.RESULT_DUPLICATE);
						
					} else if (se.getErrorCode() == GC.SQL_ERROR_NOT_NULL) { // NOT NULL 컬럼에 NULL 값
						mapReturn.put(GC.RESULT_CODE, GC.RESULT_NOT_NULL);
					}
				}
			}
		
		} catch(Exception e) {
			log.error("{}", e.toString());
		}
	}
	
	
	
	
	// **************************************************************************
	// 트리 관련
	// **************************************************************************
	
	// 트리 조회
	public static List<HashMap<String, Object>> getTree(List<?> list, String colId, String colValue, String colParentId, String colLevel, String colUrl) {
		List<HashMap<String, Object>> dataset = new ArrayList<HashMap<String, Object>>();
		
		try {
			if ((list != null) && (list.size() > 0)
				&& (GF.getString(colId).equals("") == false) && (GF.getString(colValue).equals("") == false) && (GF.getString(colLevel).equals("") == false)
			) {
				int level = 1;
				Class<?> classDTO = list.get(0).getClass();
				while (list.size() > 0) {
					List<Integer> arrDelete = new ArrayList<Integer>();
					for (int index = 0; index < list.size(); index++) {
						Object object = list.get(index);
						int objectLevel = GF.getInt(classDTO.getMethod("get" + colLevel).invoke(object));
						if (objectLevel <= level) {
							long objectId = GF.getLong(classDTO.getMethod("get" + colId).invoke(object));
							if (objectId != 0) {
								long objectParentId = GF.getLong(classDTO.getMethod("get" + colParentId).invoke(object));
								String objectValue = GF.getString(classDTO.getMethod("get" + colValue).invoke(object));
								String objectUrl = GF.getString(classDTO.getMethod("get" + colUrl).invoke(object));
								
								HashMap<String, Object> data = new HashMap<String, Object>();
								data.put("id", "" + objectId); // dhtmlx 필수 : 반드시 string 형태여야 오류가 나지 않는다
								data.put("value", objectValue); // dhtmlx 필수
								data.put("level", objectLevel);
								data.put("url", objectUrl);
								if (objectParentId != 0) { // 부모데이터 찾아서 
									HashMap<String, Object> parent = GF.findTree(dataset, "" + objectParentId);
									if (parent != null) { // 찾았다면 items 밑에 추가
										if (parent.get("items") == null) {
											parent.put("items", new ArrayList<HashMap<String, Object>>());
										}
										@SuppressWarnings("unchecked")
										List<HashMap<String, Object>> parentItems = (List<HashMap<String, Object>>)parent.get("items");
										parentItems.add(data);
									}
									
								} else { // 부모가 없다면 dataset에 추가
									dataset.add(data);
								}
							}
							arrDelete.add(0, index); // 삭제 리스트에 추가 (index 큰게 앞으로 오도록)
						}
					}
					for (int index : arrDelete) {
						list.remove(index);
					}
					level++;
				}
			}
		
		} catch(Exception e) {
			dataset = new ArrayList<HashMap<String, Object>>();
			log.error("{}", e.toString());
		}
		
		return dataset;
	}
	public static List<HashMap<String, Object>> getTree(List<?> list) {
		String colId = "";
		String colValue = "";
		String colParentId = "";
		String colLevel = "";
		String colUrl = "";
		
		if ((list != null) && (list.size() > 0)) {
			Class<?> classDTO = list.get(0).getClass();
			String[] paths = classDTO.getName().split("\\.");
			if (paths.length > 0) {
				String className = (paths[paths.length - 1]).replace("DTO", "");
				colId = className + "Seq";
				colValue = className + "Nm";
				colParentId = "ParentSeq";
				colLevel = className + "Level";
				colUrl  = className + "Url";
			}
		}
		
		return getTree(list, colId, colValue, colParentId, colLevel, colUrl);
	}
	
	// 조회 조건에 맞는 tree data 찾기
	private static HashMap<String, Object> findTree(List<HashMap<String, Object>> dataset, String findSeq) {
		HashMap<String, Object> mapData = null;
		
		try {
			findSeq = GF.getString(findSeq);
			for (HashMap<String, Object> data : dataset) {
				String seq = GF.getString(data.get("id"));
				if (seq.equals(findSeq)) { // 찾았다면 나가기
					mapData = data;
					break;
					
				} else { // 못찾았다면 items 있는지 체크
					if (data.get("items") != null) {
						@SuppressWarnings("unchecked")
						HashMap<String, Object> check = findTree((List<HashMap<String, Object>>)data.get("items"), findSeq);
						if (check != null) { // 찾았다면
							mapData = check;
							break;
						}
					}
				}
			}
			
		} catch(Exception e) {
			log.error("{}", e.toString());
		}
		
		return mapData;
	}
}