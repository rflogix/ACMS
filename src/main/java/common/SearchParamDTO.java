package common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchParamDTO {
	private String pagingYn; // 페이징 조회 여부
	private String pagingShowYn; // 페이징 노출 여부
	private String gridYn; // 그리드 조회 여부
	
	private int nowPage; // 현재 페이지
	private int showCount; // 화면 그리드에 표시될 레코드수
	private int totalCount; // 총 검색결과 레코드수
	private int totalPage; // 총 페이지 수
	private String sortCol; // 정렬 컬럼
	private String sortType; // 정렬 타입 - ASC, DESC
	
	private int offsetRownum; // 페이징할때 건너뛸 rownum
	private int startRownum; // 페이징할때 시작되는 rownum
	private int endRownum; // 페이징할때 끝나는 rownum
}