package acms.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import common.SearchParamDTO;
import acms.common.GC;
import acms.common.GF;
import acms.user.UserDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MenuService {
	@Autowired private MenuMapper mapper;
	@Autowired(required=false) private DataSourceTransactionManager transactionManager;
	
	
	
	
	//**************************************************************************
	// 조회
	//**************************************************************************
	
	public List<MenuDTO> select(HashMap<String, Object> mapParam) {
		List<MenuDTO> listReturn = new ArrayList<MenuDTO>();
		
		try {
			// 페이징 조회 여부
			SearchParamDTO searchParam = GF.getDto(mapParam, SearchParamDTO.class);
			if (GF.getString(searchParam.getPagingYn()).equals("Y")) { // 페이징 조회라면
				
				// 전체 레코드수 조회 후 검색 파라미터 설정
				searchParam = GF.setSearchParam(searchParam, mapper.totalCount(mapParam));
				
				// 검색 파라미터 설정된 조건으로 다시 조회
				mapParam.put(GC.SEARCH_PARAM, searchParam);
				listReturn = mapper.select(mapParam);
				if (listReturn == null) {
					listReturn = new ArrayList<MenuDTO>();
				}
				
			} else { // 페이징 조회가 아니라면 일반 조회로
				listReturn = mapper.select(mapParam);
				if (listReturn == null) {
					listReturn = new ArrayList<MenuDTO>();
					
				} else {
					mapParam.put(GC.SEARCH_PARAM, GF.setSearchParam(searchParam, listReturn.size()));
				}
			}
			
			// 그리드 조회 여부
			if (GF.getString(searchParam.getGridYn()).equals("Y")) { // 그리드 조회라면
				
				// 그리드 및 기타 설정값 적용
				long startRownum = searchParam.getStartRownum();
				for (MenuDTO item : listReturn) {
					item.setNo(startRownum++); // 그리드에서 쓰는 no 세팅
					item.setId(item.getMenuSeq() + ""); // 그리드에서 쓰는 id 세팅
					
					// boolean 값 수정
					if (item.getUseYn() == -1) {
						item.setUseYn(0);
					}
					if (item.getDeleteYn() == -1) {
						item.setDeleteYn(0);
					}
				}
			}
			
		} catch (Exception e) {
			listReturn = new ArrayList<MenuDTO>();
			log.error("{}", e.toString());
		}
		
		return listReturn;
	}
	public List<MenuDTO> select(MenuDTO dtoParam) {
		return this.select(GF.getMap(dtoParam));
	}
	public MenuDTO selectOne(HashMap<String, Object> mapParam) {
		MenuDTO dtoReturn = new MenuDTO();
		
		try {
			List<MenuDTO> list = this.select(mapParam);
			if (list.size() == 1) {
				dtoReturn = list.get(0);
			}
			
		} catch (Exception e) {
			dtoReturn = new MenuDTO();
			log.error("{}", e.toString());
		}
		
		return dtoReturn;
	}
	public MenuDTO selectOne(MenuDTO dto) {
		return this.selectOne(GF.getMap(dto));
	}
	public MenuDTO selectPK(long pkCode) {
		MenuDTO dtoReturn = new MenuDTO();
		
		try {
			if (pkCode != 0) {
				MenuDTO param = new MenuDTO();
				param.setMenuSeq(pkCode);
				dtoReturn = this.selectOne(param);
			}
			
		} catch (Exception e) {
			dtoReturn = new MenuDTO();
			log.error("{}", e.toString());
		}
		
		return dtoReturn;
	}
	public HashMap<String, Object> search(HashMap<String, Object> mapParam) {
		HashMap<String, Object> mapReturn = new HashMap<String, Object>();
		
		mapReturn.putAll(mapParam); // 들어온 파라미터 그대로 설정
		mapReturn.put(GC.RESULT_CODE, GC.RESULT_OK);
		mapReturn.put(GC.RESULT_LIST, this.select(mapParam));
		mapReturn.put(GC.SEARCH_PARAM, mapParam.get(GC.SEARCH_PARAM));
		
		return mapReturn;
	}
	public HashMap<String, Object> search(HashMap<String, Object> mapParam, HttpServletRequest request) {
		HashMap<String, Object> mapReturn = new HashMap<String, Object>();
		
		if (GF.getLoginYn(request)) {
			mapReturn = this.search(mapParam);
			
		} else {
			mapReturn.put(GC.RESULT_CODE, GC.RESULT_LOGOUT);
			mapReturn.put(GC.RESULT_MESSAGE, "로그아웃 상태여서 조회되지 않았습니다");
		}
		
		return mapReturn;
	}
	public HashMap<String, Object> maxOrder(HashMap<String, Object> mapParam, HttpServletRequest request) {
		HashMap<String, Object> mapReturn = new HashMap<String, Object>();
		
		try {
			if (GF.getLoginYn(request)) {
				mapReturn.put(GC.RESULT_CODE, GC.RESULT_OK);
				mapReturn.put("maxOrder", mapper.maxOrder(mapParam));
				
			} else {
				mapReturn.put(GC.RESULT_CODE, GC.RESULT_LOGOUT);
				mapReturn.put(GC.RESULT_MESSAGE, "로그아웃 상태여서 조회되지 않았습니다");
			}
			
		} catch (Exception e) {
			log.error("{}", e.toString());
		}
		
		return mapReturn;
	}

	// 트리 조회
	public List<HashMap<String, Object>> tree(HashMap<String, Object> mapParam, HttpServletRequest request) {
		List<HashMap<String, Object>> dataset = new ArrayList<HashMap<String, Object>>();
		
		try {
			if (GF.getLoginYn(request)) {
				dataset = GF.getTree(this.select(mapParam));
			}
			
		} catch(Exception e) {
			log.error("{}", e.toString());
		}
		
		return dataset;
	}
	
	
	
	//**************************************************************************
	// 저장
	//**************************************************************************
	
	public HashMap<String, Object> save(HashMap<String, Object> mapParam, HttpServletRequest request) {
		HashMap<String, Object> mapReturn = new HashMap<String, Object>();
		mapReturn.putAll(mapParam); // 기본적으로 들어온 파라미터 그대로 돌려준다
		
		TransactionStatus transaction = GF.getTransaction(transactionManager);
		boolean commitYN = false;
		
		try {
			UserDTO loginUser = GF.getLoginUser(request);
			if (GF.getLoginYn(loginUser)) { // 로그인 했는지 체크
				
				// 파라미터 설정
				for (String key : mapParam.keySet()) {
					if (key.equals("deleteYn") || key.equals("useYn")) {
						String value = GF.getString(mapParam.get(key));
						if (value.equals("1") || value.equals("true")) {
							mapParam.put(key, 1);
							
						} else {
							mapParam.put(key, -1);
						}
					}
				}
				MenuDTO saveParam = GF.getDto(mapParam, MenuDTO.class);
				
				// 메뉴 주소 설정
				String menuUrl = GF.getString(saveParam.getMenuUrl());
				if (GF.left(menuUrl, 1).equals("/")) { // 메뉴 주소가 / 로 시작하면 context path 가 설정되어 있을시 무시되기때문에 맨앞에 / 삭제
					saveParam.setMenuUrl(menuUrl.substring(1));
				}
				
				// CRUD 구분
				String crudType = GF.getString(saveParam.getCrudType());
				if (crudType.equals("")) {
					switch (request.getMethod()) {
						case "POST":
							crudType = "C";
							break;
							
						case "PUT":
							crudType = "U";
							break;
						
						case "DELETE":
							crudType = "D";
							break;
					}
				}
				if (crudType.equals("C")) { // 등록이라면
					if (saveParam.getUseYn() == 0) {
						saveParam.setUseYn(-1);
					}
					if (saveParam.getDeleteYn() == 0) {
						saveParam.setDeleteYn(-1);
					}
					saveParam.setInsertUserSeq(loginUser.getUserSeq()); // 등록관리자 ID는 로그인 ID로 설정
					if (mapper.insert(saveParam) > 0) { // 등록 되었다면
						commitYN = true;
						mapReturn.put(GC.RESULT_CODE, GC.RESULT_OK);
						mapReturn.put("menuSeq", saveParam.getMenuSeq());
						
					} else { // 등록된 내용이 없다면
						mapReturn.put(GC.RESULT_CODE, GC.RESULT_FAIL);
						mapReturn.put(GC.RESULT_MESSAGE, "추가되지 않았습니다. 다시한번 저장하세요.");
					}
					
				} else if (crudType.equals("U")) { // 수정이라면
					saveParam.setUpdateUserSeq(loginUser.getUserSeq()); // 변경관리자 ID는 로그인 ID로 설정
					if (mapper.update(saveParam) > 0) { // 수정 되었다면
						commitYN = true;
						mapReturn.put(GC.RESULT_CODE, GC.RESULT_OK);
						
					} else { // 수정된 내용 없다면
						mapReturn.put(GC.RESULT_CODE, GC.RESULT_FAIL);
						mapReturn.put(GC.RESULT_MESSAGE, "변경된 내역이 없습니다");
					}
					
				} else if (crudType.equals("D")) { // 삭제라면
					saveParam.setDeleteYn(1);
					saveParam.setDeleteUserSeq(loginUser.getUserSeq());
					if (mapper.delete(saveParam) > 0) { // 삭제 되었다면
						commitYN = true;
						mapReturn.put(GC.RESULT_CODE, GC.RESULT_OK);
						
					} else {
						mapReturn.put(GC.RESULT_CODE, GC.RESULT_FAIL);
						mapReturn.put(GC.RESULT_MESSAGE, "삭제된 내역이 없습니다");
					}
				}
				
			} else {
				mapReturn.put(GC.RESULT_CODE, GC.RESULT_LOGOUT);
				mapReturn.put(GC.RESULT_MESSAGE, "로그아웃 상태여서 저장되지 않았습니다");
			}
			
		} catch(Exception e) {
			mapReturn.put(GC.RESULT_CODE, GC.RESULT_ERROR);
			mapReturn.put(GC.RESULT_MESSAGE, "네트워크 문제로 저장되지 않았습니다");
			GF.setResultSqlError(e, mapReturn);
			log.error("{}", e.toString());
		}
		
		// 트랜젝션 처리
		if (commitYN) {
			transactionManager.commit(transaction); // 커밋
			
		} else {
			transactionManager.rollback(transaction); // 롤백
		}
		
		return mapReturn;
	}
}