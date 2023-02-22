//**********************************************************************************
// ○ 파일	: menu_summary.js
// ● 설명	: 메뉴 관리 js
//**********************************************************************************


//**********************************************************************************
// 화면 설정
//**********************************************************************************

let layout;
let toolbar;
let grid;
$(document).ready(function () {
	// 레이아웃 생성
	$('body').append(($('<div>').attr('id', 'layout').attr('class', 'layout'))
		.append($('<div>').attr('id', 'header').attr('class', 'header'))
		.append($('<div>').attr('id', 'content').attr('class', 'content'))
	);
	
	// 상단바 설정
	let header_toolbar = new dhx.Toolbar('header', {
		css: 'toolbar'
		,data: [
			{
				id: 'title'
				,type: 'customHTML'
				,html: `${GLOBAL.MENU.menuNm}`
			}
			,{
				type: 'spacer'
			}
			,{
				id: 'home'
				,icon: 'fas fa-home'
			}
			,{
				id: 'history'
				,type: 'title'
				,value: `${GLOBAL.MENU.parentMenu.menuNm} > ${GLOBAL.MENU.menuNm}`
			}
			,{
				id: 'menual'
				,css: 'menual'
				,icon: 'fab fa-elementor'
			}
		]
	});
	header_toolbar.events.on('click', function (id) {
		switch (id) {
			case 'menual':
				alert('메뉴얼');
				break;
		}
	});
	
	// 메인 레이아웃 생성
	layout = new dhx.Layout('content', {
		rows: [
			{
				id: 'search'
				,css: 'search'
				,height: 'content'
			}
			,{
				id: 'result'
				,css: 'result'
			}
		]
	});
	
	// 검색 툴바 추가
	toolbar = new dhx.Toolbar(null, {
		css: 'toolbar'
		,data: [
			{
				id: 'title'
				,type: 'title'
				,value: ''
			}
			,{
				type: 'spacer'
			}
			,{
				id: 'excel'
				,value: '엑셀 다운로드'
				,icon: 'fas fa-file-excel'
			}
			,{
				type: 'separator'
			}
			,{
				id: 'search'
				,value: '새로고침'
				,icon: 'fas fa-sync-alt'
			}
			,{
				type: 'separator'
			}
			,{
				id: 'insert'
				,value: '추가'
				,icon: 'fas fa-plus'
			}
			,{
				id: 'delete'
				,value: '삭제'
				,icon: 'fas fa-trash-alt'
			}
			,{
				id: 'save'
				,value: '저장'
				,icon: 'fas fa-edit'
			}
		]
	});
	toolbar.events.on('click', function (id) {
		switch (id) {
			case 'search':
				post({
					url: 'menu/search'
					,data: {gridYn: 'Y'}
					,success: function(data) {
						grid.data.parse(data[RESULT_LIST]); // DB 에서 가져온 JSON 설정
					}
				});
				break;
				
			case 'insert':
				grid.data.add({});
				break;
				
			case 'delete':
				let cells = grid.selection.getCells();
				if ((cells != undefined) && (cells.length > 0)) {
					let delRowIds = [];
					for (let cell of cells) {
						delRowIds.push(cell.row.id);
					}
					for (let rowId of delRowIds) {
						grid.data.remove(rowId);
					}
					
				} else {
					alert('삭제할 행을 선택해주세요');
				}
				break;
			
			case 'save':
				if ((grid.data._changes != undefined) && (grid.data._changes.order != undefined) && (grid.data._changes.order.length > 0)) {
					grid.data.save('menu/save');
					if (grid.data.saveData != undefined) {
						grid.data.saveData.then(function () { // 저장 후 재조회
							toolbar.events.fire('click', ['search']);
						});
						
					} else {
						alert('저장할 내역이 없습니다');
					}
					
				} else {
					alert('저장할 내역이 없습니다');
				}
				break;
				
			case 'excel':
				grid.export.xlsx({
					url: "https://export.dhtmlx.com/excel"
				});
				break;
		}
	});
	layout.getCell('search').attach(toolbar);
	
	// 그리드 추가
	grid = new dhx.Grid(null, {
		columns: [
			{id: 'no', width: 100, header: [{text: 'NO'}]}
			,{id: 'menuSeq', width: 100, header: [{text: '메뉴코드'}]}
			,{id: 'parentSeq', width: 100, header: [{text: '부모코드'}]}
			,{id: 'menuNm', width: 200, header: [{text: '메뉴명'}]}
			,{id: 'menuUrl', width: 200, header: [{text: '메뉴URL'}]}
			,{id: 'menuLevel', width: 100, header: [{text: '메뉴레벨'}]}
			,{id: 'menuOrder', width: 100, header: [{text: '메뉴순서'}]}
			,{id: 'useYn', type: "boolean", width: 100, header: [{text: '사용여부'}]}
			,{id: 'deleteYn', type: "boolean", width: 100, header: [{text: '삭제여부'}]}
		]
		,dragItem: 'column'
		,editable: true
		,selection: 'row' // cell, row, complex
		,multiselection: true
		,resizable: true
		,dragCopy: true
	});
	layout.getCell('result').attach(grid);
	
	// 검색
	toolbar.events.fire('click', ['search']);
});