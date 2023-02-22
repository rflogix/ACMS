package acms.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import common.SearchParamDTO;
import acms.common.GC;
import acms.common.GF;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
	@Autowired private UserMapper mapper;
	@Autowired(required=false) private DataSourceTransactionManager transactionManager;
	
	
	
	
	//**************************************************************************
	// 조회
	//**************************************************************************
	
	public List<UserDTO> select(HashMap<String, Object> mapParam) {
		List<UserDTO> listReturn = new ArrayList<UserDTO>();
		
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
					listReturn = new ArrayList<UserDTO>();
				}
				
			} else { // 페이징 조회가 아니라면 일반 조회로
				listReturn = mapper.select(mapParam);
				if (listReturn == null) {
					listReturn = new ArrayList<UserDTO>();
					
				} else {
					mapParam.put(GC.SEARCH_PARAM, GF.setSearchParam(searchParam, listReturn.size()));
				}
			}
			
			// 그리드 조회 여부
			if (GF.getString(searchParam.getGridYn()).equals("Y")) { // 그리드 조회라면
				
				// 그리드 및 기타 설정값 적용
				long startRownum = searchParam.getStartRownum();
				for (UserDTO item : listReturn) {
					item.setNo(startRownum++); // 그리드에서 쓰는 no 세팅
					item.setId(item.getUserSeq() + ""); // 그리드에서 쓰는 id 세팅
					
					// boolean 값 수정
					if (item.getDeleteYn() == -1) {
						item.setDeleteYn(0);
					}
				}
			}
			
		} catch (Exception e) {
			listReturn = new ArrayList<UserDTO>();
			log.error("{}", e.toString());
		}
		
		return listReturn;
	}
	public List<UserDTO> select(UserDTO dtoParam) {
		return this.select(GF.getMap(dtoParam));
	}
	public UserDTO selectOne(HashMap<String, Object> mapParam) {
		UserDTO dtoReturn = new UserDTO();
		
		try {
			List<UserDTO> list = this.select(mapParam);
			if (list.size() == 1) {
				dtoReturn = list.get(0);
			}
			
		} catch (Exception e) {
			dtoReturn = new UserDTO();
			log.error("{}", e.toString());
		}
		
		return dtoReturn;
	}
	public UserDTO selectOne(UserDTO dto) {
		return this.selectOne(GF.getMap(dto));
	}
	public UserDTO selectPK(long pkCode) {
		UserDTO dtoReturn = new UserDTO();
		
		try {
			if (pkCode != 0) {
				UserDTO param = new UserDTO();
				param.setUserSeq(pkCode);
				dtoReturn = this.selectOne(param);
			}
			
		} catch (Exception e) {
			dtoReturn = new UserDTO();
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
				UserDTO saveParam = GF.getDto(mapParam, UserDTO.class);
				if (saveParam.getUserPassword() == null) { // saveParam 의 UserPassword DTO 필드 초기화
					saveParam.setUserPassword(new UserPasswordDTO());
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
					if (saveParam.getDeleteYn() == 0) {
						saveParam.setDeleteYn(-1);
					}
					saveParam.setSalt(GF.getSalt()); // 등록할때만 SALT 새로 생성
					saveParam.setInsertUserSeq(loginUser.getUserSeq()); // 등록관리자 ID는 로그인 ID로 설정
					if (mapper.insert(saveParam) > 0) { // 등록 되었다면
						
						// 비밀번호 새로 생성
						UserPasswordDTO userPassword = new UserPasswordDTO();
						userPassword.setUserSeq(saveParam.getUserSeq());
						if (GF.getString(saveParam.getUserPw()).equals("")) { // 비밀번호 설정 안되었다면 아이디를 비밀번호로 설정
							userPassword.setPassword(GF.makePassword(saveParam.getUserId(), saveParam.getSalt()));
							
						} else { // 비밀번호 설정되어 들어왔다면 해당 비밀번호로 설정
							userPassword.setPassword(GF.makePassword(saveParam.getUserPw(), saveParam.getSalt()));
						}
						userPassword.setInsertUserSeq(loginUser.getUserSeq());
						if (mapper.insertPassword(userPassword) > 0) { // 비밀번호 추가 되었다면
							commitYN = true;
							mapReturn.put(GC.RESULT_CODE, GC.RESULT_OK);
							mapReturn.put("userSeq", saveParam.getUserSeq());
							
						} else { // 비밀번호 추가 안되었다면
							mapReturn.put(GC.RESULT_CODE, GC.RESULT_FAIL);
							mapReturn.put(GC.RESULT_MESSAGE, "추가되지 않았습니다. 다시한번 저장하세요.");
						}
						
					} else { // 등록된 내용이 없다면
						mapReturn.put(GC.RESULT_CODE, GC.RESULT_FAIL);
						mapReturn.put(GC.RESULT_MESSAGE, "추가되지 않았습니다. 다시한번 저장하세요.");
					}
					
				} else if (crudType.equals("U")) { // 수정이라면
					saveParam.setUpdateUserSeq(loginUser.getUserSeq()); // 변경관리자 ID는 로그인 ID로 설정
					if ((saveParam.getUserSeq() != 0) && (mapper.update(saveParam) > 0)) { // 수정 되었다면
						
						// 비밀번호 초기화/변경 체크
						boolean blnPasswordCheck = true;
						String resetPasswordYn = GF.getString(mapParam.get("resetPasswordYn"));
						String changePasswordYn = GF.getString(mapParam.get("changePasswordYn"));
						if ((resetPasswordYn.equals("Y")) || (changePasswordYn.equals("Y"))) {
							
							// 비밀번호 새로 생성
							UserPasswordDTO userPassword = new UserPasswordDTO();
							userPassword.setUserSeq(loginUser.getUserSeq());
							if (resetPasswordYn.equals("Y")) { // 비밀번호 리셋이라면 아이디와 동일하게 리셋
								userPassword.setPassword(GF.makePassword(saveParam.getUserId(), loginUser.getSalt()));
								
							} else if (changePasswordYn.equals("Y")) { // 비밀번호 변경이라면
								userPassword.setPassword(GF.makePassword(saveParam.getUserPw(), loginUser.getSalt()));
							}
							userPassword.setInsertUserSeq(loginUser.getUserSeq());
							if (mapper.insertPassword(userPassword) <= 0) {
								blnPasswordCheck = false;
							}
						}
						if (blnPasswordCheck == false) { // 비밀번호 초기화/변경에서 오류가 났다면
							mapReturn.put(GC.RESULT_CODE, GC.RESULT_ERROR);
							mapReturn.put(GC.RESULT_MESSAGE, "네트워크 문제로 저장되지 않았습니다");
							
						} else { // 비밀번호 변경까지 잘 되었다면
							commitYN = true;
							mapReturn.put(GC.RESULT_CODE, GC.RESULT_OK);
						}
						
					} else { // 수정된 내용 없다면
						mapReturn.put(GC.RESULT_CODE, GC.RESULT_FAIL);
						mapReturn.put(GC.RESULT_MESSAGE, "변경된 내역이 없습니다");
					}
					
				} else if (crudType.equals("D")) { // 삭제라면
					saveParam.setDeleteYn(1);
					saveParam.setDeleteUserSeq(loginUser.getUserSeq());
					if ((saveParam.getUserSeq() != 0) && (mapper.delete(saveParam) > 0)) { // 삭제 되었다면
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
	
	
	
	
	//**************************************************************************
	// 로그인
	//**************************************************************************
	
	public HashMap<String, Object> login(HashMap<String, Object> mapParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HashMap<String, Object> mapReturn = new HashMap<String, Object>();
		mapReturn.putAll(mapParam); // 기본적으로 들어온 파라미터 그대로 돌려준다
		
		try {
			
			// ID 로만 조회 후, 가져온 정보로 PW 비교 후 처리
			UserDTO userParam = GF.getDto(mapParam, UserDTO.class);
			if (GF.getString(userParam.getUserId()).equals("") == false) {
				
				// 패스워드 들어왔는지 체크 - SSO 일때는 패스워드 체크 안함
				boolean passwordExist = true;
				boolean ssoYn = GF.getString(mapReturn.get(GC.KEY_SSO_YN)).equals("Y"); // SSO 로그인 여부
				if (ssoYn == false) {
					if (GF.getString(userParam.getUserPw()).equals("")) {
						passwordExist = false;
					}
				}
				if (passwordExist) { // 패스워드 들어왔다면
					
					// ID 조건만으로 사용자 조회
					UserDTO paramId = new UserDTO();
					paramId.setUserId(userParam.getUserId());
					List<UserDTO> arrUser = this.select(paramId);
					if (arrUser.size() == 1) { // 1개만 존재 할때
						UserDTO loginUser = arrUser.get(0);
						if (loginUser.getUserPassword() == null) {
							loginUser.setUserPassword(new UserPasswordDTO());
						}
						
						// 비밀번호 오류 회수 초과 체크
						if (loginUser.getUserPassword().getErrorCount() < GC.PW_OVER_COUNT) {
							
							// 암호 일치 여부 체크
							boolean passwordYn = true;
							if (ssoYn == false) { // SSO 로그인이라면 암호 일치여부는 체크하지 않는다
								String userPasswordOrigin = GF.getString(loginUser.getUserPassword().getPassword());
								String userPasswordNow = GF.makePassword(userParam.getUserPw(), loginUser.getSalt());
								passwordYn = userPasswordOrigin.equals(userPasswordNow);
							}
							if (passwordYn) { // 비밀번호 일치한다면
								
								// 비밀번호 만료일 / 비밀번호 변경일이 모두 있다면
								String expireDt = GF.getString(loginUser.getUserPassword().getExpireDt());
								String updateDt = GF.getString(loginUser.getUserPassword().getUpdateDt());
								if ((expireDt.equals("") == false) && (updateDt.equals("") == false)) {
									
									// 비밀번호 만료일자 지났는지 체크
									if (GF.compareDate(loginUser.getSystemDt(), expireDt) < 0) {
									
										// 권한 체크
										boolean checkAuth = true;
										// TODO : 권한체크 로직
										if (checkAuth == false) { // 권한이 없다면
											mapReturn.put(GC.RESULT_CODE, GC.RESULT_NOT_AUTH);
											mapReturn.put(GC.RESULT_MESSAGE, "권한이 없는 아이디 입니다");
											
										} else { // 권한이 있다면
											
											// 로그인 히스토리 추가
											UserLoginDTO userLoginHistory = new UserLoginDTO();
											userLoginHistory.setUserSeq(loginUser.getUserSeq());
											userLoginHistory.setIpAddress(request.getRemoteAddr());
											userLoginHistory.setBrowserInfo(GF.left(request.getHeader("User-Agent"), 500));
											mapper.login(userLoginHistory);
											
											// 비밀번호 오류 회수 초기화
											UserPasswordDTO userPassword = new UserPasswordDTO();
											userPassword.setUserSeq(loginUser.getUserSeq());
											userPassword.setSno(loginUser.getUserPassword().getSno());
											userPassword.setErrorCount(0);
											mapper.updatePassword(userPassword);
											
											// 사용자 다시 조회하여 설정
											loginUser = this.selectPK(loginUser.getUserSeq());
											
											// 세션 설정하기
											HttpSession session = request.getSession();
											session.setMaxInactiveInterval(GC.SESSION_TIME);
											session.setAttribute(GC.KEY_LOGIN_USER, loginUser);
											session.setAttribute(GC.KEY_URL_ROOT, GF.getUrlRoot(request)); // 로그아웃 uri 호출시 사용
											
											// SSO 로그인 아니라면
											if (ssoYn == false) {
												// 아이디 쿠키에 저장 설정
												if (GF.getString(mapParam.get("saveIdYn")).equals("Y")) {
													GF.setCookie("userId", GF.encrypt(loginUser.getUserId()), response);
													
												} else {
													GF.deleteCookie("userId", response);
												}
											}
											
											mapReturn.put(GC.RESULT_CODE, GC.RESULT_OK);
											mapReturn.put(GC.RESULT_MESSAGE, "로그인 되었습니다");
											mapReturn.put(GC.RESULT_DTO, loginUser);
											log.debug("[{} / {}] 님이 로그인 하였습니다 (세션ID : {})", loginUser.getUserNm(), loginUser.getUserId(), session.getId());
										}
										
									} else { // 비밀번호 만료 일자가 지났다면
										mapReturn.put(GC.RESULT_CODE, GC.RESULT_OVER_PERIOD_PW);
										mapReturn.put(GC.RESULT_MESSAGE, "비밀번호 유효기간이 만료되었습니다");
									}
									
								} else { // 비밀번호 만료일 / 비밀번호 변경일 둘중에 하나라도 없다면
									mapReturn.put(GC.RESULT_CODE, GC.RESULT_NEED_NEW_PW);
									mapReturn.put(GC.RESULT_MESSAGE, "새로운 비밀번호로 변경해 주십시오");
								}
								
							} else { // 비밀번호 틀렸을때 비밀번호 오류 카운트 올린다
								UserPasswordDTO userPassword = new UserPasswordDTO();
								userPassword.setUserSeq(loginUser.getUserSeq());
								userPassword.setSno(loginUser.getUserPassword().getSno());
								userPassword.setErrorCount(loginUser.getUserPassword().getErrorCount() + 1);
								mapper.updatePassword(userPassword);
								
								mapReturn.put(GC.RESULT_CODE, GC.RESULT_NOT_MATCH_PW);
								mapReturn.put(GC.RESULT_MESSAGE, "비밀번호가 일치하지 않습니다");
							}
							
						} else { // 비밀번호 회수제한 초과 되었다면
							mapReturn.put(GC.RESULT_CODE, GC.RESULT_OVER_COUNT_PW);
							mapReturn.put(GC.RESULT_MESSAGE, "비밀번호 입력제한이 " + GC.PW_OVER_COUNT + "회 초과되었습니다");
						}
						
					} else if (arrUser.size() == 0) { // ID 에 해당하는 사용자가 없다면
						mapReturn.put(GC.RESULT_CODE, GC.RESULT_NOT_EXIST_ID);
						mapReturn.put(GC.RESULT_MESSAGE, "가입되지 않은 아이디 입니다");
						
					} else { // ID 에 해당하는 사용자가 많다면
						mapReturn.put(GC.RESULT_CODE, GC.RESULT_DUPLICATE);
						mapReturn.put(GC.RESULT_MESSAGE, "중복 가입된 아이디 입니다");
					}
					
				} else { // PW 가 안들어왔다면
					mapReturn.put(GC.RESULT_CODE, GC.RESULT_EMPTY_PW);
					mapReturn.put(GC.RESULT_MESSAGE, "비밀번호가 입력되지 않았습니다");
				}
				
			} else { // ID 가 안들어왔다면
				mapReturn.put(GC.RESULT_CODE, GC.RESULT_EMPTY_ID);
				mapReturn.put(GC.RESULT_MESSAGE, "아이디가 입력되지 않았습니다");
			}
			
		} catch(Exception e) {
			mapReturn.put(GC.RESULT_CODE, GC.RESULT_ERROR);
			mapReturn.put(GC.RESULT_MESSAGE, "네트워크 문제로 로그인이 되지 않았습니다");
			log.error("{}", e.toString());
		}
		
		return mapReturn;
		//return new ResponseEntity(mapReturn, HttpStatus.OK);
	}
	
	
	
	
	//**************************************************************************
	// 로그아웃
	//**************************************************************************
	
	public HashMap<String, Object> logout(HashMap<String, Object> mapParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HashMap<String, Object> mapReturn = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		
		try {
			session.invalidate();
			
			mapReturn.put(GC.RESULT_CODE, GC.RESULT_OK);
			mapReturn.put(GC.RESULT_MESSAGE, "로그아웃 되었습니다");
			
		} catch(Exception e) {
			mapReturn.put(GC.RESULT_CODE, GC.RESULT_ERROR);
			mapReturn.put(GC.RESULT_MESSAGE, "네트워크 문제로 로그아웃 되지 않았습니다");
			log.error("{}", e.toString());
		}
		
		return mapReturn;
	}
	
	// 로그아웃 히스토리 추가
	public void logoutHistory(HashMap<String, Object> mapParam) {
		try {
			UserLoginDTO paramLogin = new UserLoginDTO();
			paramLogin.setUserSeq(GF.getLong(mapParam.get("userSeq")));
			paramLogin.setSno(GF.getInt(mapParam.get("sno")));
			mapper.logout(paramLogin); // 로그인 히스토리에 로그아웃 날짜 남기기
			
		} catch(Exception e) {
			log.error("{}", e.toString());
		}
	}
	
	
	
	//**************************************************************************
	// 비밀번호변경
	//**************************************************************************
	
	public HashMap<String, Object> changePassword(HashMap<String, Object> mapParam, HttpServletRequest request) {
		HashMap<String, Object> mapReturn = new HashMap<String, Object>();
		mapReturn.putAll(mapParam); // 기본적으로 들어온 파라미터 그대로 돌려준다
		
		TransactionStatus transaction = GF.getTransaction(transactionManager);
		boolean commitYN = false;
		
		try {
			// 파라미터 설정
			UserDTO userParam = GF.getDto(mapParam, UserDTO.class);
			
			// ID 로만 조회
			UserDTO paramId = new UserDTO();
			paramId.setUserId(userParam.getUserId());
			paramId.setDeleteYn(-1);
			UserDTO loginUser = this.selectOne(paramId);
			if (loginUser.getUserSeq() != 0) {
				
				// 기존 비밀번호 일치 여부 체크
				String userPasswordOrigin = GF.getString(loginUser.getUserPassword().getPassword());
				String userPasswordBefore = GF.makePassword(GF.getString(userParam.getUserPwBefore()), loginUser.getSalt());
				if (userPasswordOrigin.equals(userPasswordBefore)) {
					
					// 비밀번호 새로 생성
					UserPasswordDTO userPassword = new UserPasswordDTO();
					userPassword.setUserSeq(userParam.getUserSeq());
					userPassword.setPassword(GF.makePassword(userParam.getUserPw(), loginUser.getSalt()));
					
					// 이전에 같은 비밀번호가 있었는지 체크
					boolean samePasswordYn = false;
					// 이전에 같은 비밀번호가 있었는지 체크 하려면 아래 주석 해제
					//List<UserPasswordDTO> listUserPassword = mapper.selectPassword(userPassword);
					//if (listUserPassword.size() > 0) {
					//	samePasswordYn = true;
					//}
					if (samePasswordYn == false) { // 이전에 사용했던 비밀번호가 아니라면
						userPassword.setInsertUserSeq(loginUser.getUserSeq());
						userPassword.setUpdateUserSeq(loginUser.getUserSeq());
						userPassword.setUpdateDt("AUTO");
						if (mapper.insertPassword(userPassword) > 0) {
							commitYN = true;
							mapReturn.put(GC.RESULT_CODE, GC.RESULT_OK);
							
						} else {
							mapReturn.put(GC.RESULT_CODE, GC.RESULT_ERROR);
							mapReturn.put(GC.RESULT_MESSAGE, "네트워크 문제로 저장되지 않았습니다");
						}
						
					} else { // 같은 비밀번호가 있었다면
						mapReturn.put(GC.RESULT_CODE, GC.RESULT_DUPLICATE);
						mapReturn.put(GC.RESULT_MESSAGE, "이전에 설정하셨던 비밀번호로는 변경하실 수 없습니다");
					}
					
				} else { // 기존 비밀번호와 일치하지 않는다면
					mapReturn.put(GC.RESULT_CODE, GC.RESULT_NOT_MATCH_PW);
					mapReturn.put(GC.RESULT_MESSAGE, "기존의 비밀번호가 일치하지 않습니다");
				}
				
			} else { // 로그인 사용자가 존재하지 않는다면
				mapReturn.put(GC.RESULT_CODE, GC.RESULT_NOT_EXIST);
				mapReturn.put(GC.RESULT_MESSAGE, "존재하지 않는 사용자 입니다");
			}
			
		} catch(Exception e) {
			mapReturn.put(GC.RESULT_CODE, GC.RESULT_ERROR);
			mapReturn.put(GC.RESULT_MESSAGE, "네트워크 문제로 저장되지 않았습니다");
			GF.setResultSqlError(e, mapReturn);
			log.warn("{}", e.toString());
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