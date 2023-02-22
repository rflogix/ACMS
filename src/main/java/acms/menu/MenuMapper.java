package acms.menu;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenuMapper {
	int insert(MenuDTO param) throws Exception;
	int update(MenuDTO param) throws Exception;
	int delete(MenuDTO param) throws Exception;
	List<MenuDTO> select(HashMap<String, Object> param) throws Exception;
	int totalCount(HashMap<String, Object> param) throws Exception;
	int maxOrder(HashMap<String, Object> param) throws Exception;
}