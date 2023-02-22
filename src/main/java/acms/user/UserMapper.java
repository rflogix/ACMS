package acms.user;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
	int insert(UserDTO param) throws Exception;
	int update(UserDTO param) throws Exception;
	int delete(UserDTO param) throws Exception;
	List<UserDTO> select(HashMap<String, Object> param) throws Exception;
	int totalCount(HashMap<String, Object> param) throws Exception;
	
	// 비밀번호 관련
	List<UserPasswordDTO> selectPassword(UserPasswordDTO param) throws Exception;
	int insertPassword(UserPasswordDTO param) throws Exception; // 비밀번호 추가
	int updatePassword(UserPasswordDTO param) throws Exception; // 비밀번호 변경
	
	// 로그인 관련
	int login(UserLoginDTO param) throws Exception;
	int logout(UserLoginDTO param) throws Exception;
}
