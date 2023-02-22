package acms.common;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper=false)
@SuppressWarnings("serial")
public abstract class CommonDTO implements Serializable { // 직접 생성해서 사용할일이 없으므로 추상 클래스로
	private String id; // 그리드에서 쓰는 id
	private long no; // 그리드에서 쓰는 no
	
	private String crudType; // 화면단에서 넘어오는 CrudType
	
	private String systemDt; // 현재 시스템 날짜
	
	// 추가, 수정, 삭제 사용자 및 날짜
	private long insertUserSeq;
	private String insertUserNm;
	private String insertDt;
	
	private long updateUserSeq;
	private String updateUserNm;
	private String updateDt;
	
	private long deleteUserSeq;
	private String deleteUserNm;
	private String deleteDt;
	private int deleteYn;
}