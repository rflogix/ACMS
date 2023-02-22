package acms.menu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import acms.common.CommonDTO;

@Data
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown=true)
@SuppressWarnings("serial")
public class MenuDTO extends CommonDTO {
	private long menuSeq;
	
	private long parentSeq;
	private String menuNm;
	private String menuUrl;
	private int menuLevel;
	private int menuOrder;
	private int useYn;
	
	// 상위 메뉴
	private MenuDTO parentMenu;
	
	// 기타
	private long userSeq; // 사용자별 권한에 따른 메뉴 체크위해 사용
	private int childCount; // 자식 개수
}