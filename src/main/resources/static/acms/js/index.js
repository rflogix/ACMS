//**********************************************************************************
// ○ 파일	: index.js
// ● 설명	: 인덱스 js
//**********************************************************************************


//**********************************************************************************
// 화면 설정
//**********************************************************************************

let layout;
let toolbar;
let tabbar;
let menu;
let tree;
$(document).ready(function () {
	// 레이아웃 div 생성
	$('body').append($('<div>').attr('id', 'layout').attr('css', 'layout' + (GLOBAL.LOGIN_USER.menuType == MENU_TYPE_VERTICAL ? 'left' : '')));
	
	// bottom div 설정
	let divBottom = ($('<div>').attr('id', 'bottom'))
		// copyright
		.append($('<div>').attr('class', 'copyright').html('Copyright@HKCMC Co.,Ltd. All rights reserved'))
		// info
		.append($('<div>').attr('class', 'info').html(`User : ${GLOBAL.LOGIN_USER.userNm} | Last Login Time : ${GLOBAL.LOGIN_USER.loginHistory.loginDt}`));
	
	// tabbar div 설정
	let divTabbar = ($('<div>').attr('id', 'tabbar'))
		// 탭바 - 홈 버튼
		.append($('<div>').attr('class', 'tabbar_home fas fa-home flex').attr('onclick', 'homeTabbar();'))
		// 탭바 - 전체닫기 버튼
		.append($('<div>').attr('class', 'tabbar_close fas fa-times flex').attr('onclick', 'closeTabbar();'));
	
	// 가로 구성 레이아웃
	if (GLOBAL.LOGIN_USER.menuType == MENU_TYPE_HORIZON) {
		// 레이아웃
		layout = new dhx.Layout('layout', {
			rows: [
				{
					id: 'header'
					,height: 'content'
				}
				,{
					id: 'menu'
					,height: 'content'
				}
				,{
					id: 'tabbar'
					,height: 'content'
					,html: $(divTabbar)[0].outerHTML
				}
				,{
					id: 'iframe'
					,html: '<div id="iframe"></div>'
				}
				,{
					id: 'bottom'
					,height: 'content'
					,html: $(divBottom)[0].outerHTML
				}
			]
		});
		
		// 상단 메뉴
		menu = new dhx.Menu(layout.getCell('menu'), {
			css: 'menu top_menu'
			//,navigationType: 'click'
		});
		menu.events.on('click', function (id) {
			let menuItem = menu.data.getItem(id); // 메뉴에 하위 메뉴 있는지 체크
			if (menuItem.hasOwnProperty('items') == false) {
				// 아직 추가 안된 탭이라면 추가
				if (tabbar.getCell(menuItem.id) == undefined) {
					tabbar.addTab({
						id: menuItem.id
						,tab: menuItem.value
						,url: menuItem.url
					});
				}
				tabbar.setActive(menuItem.id); // 탭 활성화
			}
		});
		
		// 메뉴 데이터 설정
		post({
			url: 'menu/tree'
			,success: function(data) {
				menu.data.parse(data); // DB 에서 가져온 JSON 설정
			}
		});
	}
	
	// 세로 구성 레이아웃
	if (GLOBAL.LOGIN_USER.menuType == MENU_TYPE_VERTICAL) {
		// 레이아웃
		layout = new dhx.Layout('layout', {
			rows: [
				{
					id: 'header'
					,height: 'content'
				}
				, {
					cols : [
						{
							id: 'menu'
							,width: 'content'
							,html: '<div id="menu">'
									+ '<div class="menu_toggle fas flex" onclick="toggleMenu()">'
								+ '</div>'
						}
						,{
							rows: [
								{
									id: 'tabbar'
									,height: 'content'
									,html: $(divTabbar)[0].outerHTML
								}
								,{
									id: 'iframe'
									,css: 'iframe'
									,html: '<div id="iframe"></div>'
								}
							]
						}
					]
				}
				,{
					id: 'bottom'
					,height: 'content'
					,html: $(divBottom)[0].outerHTML
				}
			]
		});
		
		// 좌측 트리메뉴
		tree = new dhx.Tree('menu', {
			css: 'menu left_menu'
			,icon: {
				folder: "fas fa-book" // 폴더접힘
				,openFolder: "fas fa-book-open" // 폴더열림
				,file: "fas fa-book" // 말단
			}
		});
		tree.events.on('itemDblClick', function (id) {
			if (isMenuClose() == false) { // 메뉴가 접혀있지 않다면 트리 토글
				tree.toggle(id);
			}
		});
		tree.events.on('itemClick', function (id) {
			if (isMenuClose() == false) { // 메뉴가 접혔는지 체크
				let menuItem = tree.data.getItem(id); // 메뉴에 하위 메뉴 있는지 체크
				if (menuItem.hasOwnProperty('items') == false) {
					// 아직 추가 안된 탭이라면 추가
					if (tabbar.getCell(menuItem.id) == undefined) {
						tabbar.addTab({
							id: menuItem.id
							,tab: menuItem.value
							,url: menuItem.url
						});
					}
					tabbar.setActive(menuItem.id); // 탭 활성화
					
				} else { // 하위 노드 있을때 클릭하면 트리 토글
					tree.toggle(id);
				}
			}
		});
		
		// 메뉴 데이터 설정
		post({
			url: 'menu/tree'
			,success: function(data) {
				tree.data.parse(data); // DB 에서 가져온 JSON 설정
			}
		});
	}
	
	// 헤더 툴바
	toolbar = new dhx.Toolbar(layout.getCell('header'), {
		css: 'header' + (GLOBAL.LOGIN_USER.menuType == MENU_TYPE_HORIZON ? '' : ' dark')
		,data: [
			{
				id: 'logo'
				,type: 'customHTML'
				,html: '<img class="logo" src="acms/image/common/header_logo.jpg"/>'
			}
			,{
				type: 'spacer'
			}
			,{
				id: 'language'
				,type: 'selectButton'
				,value: '한국어'
				,icon: 'fas fa-globe-asia'
				,items: [
					{
						id: 'kor'
						,value: '한국어'
						,icon: 'fas fa-globe-asia'
					}
					,{
						id: 'eng'
						,value: 'English'
						,icon: 'fas fa-globe-asia'
					}
				]
			}
			,{
				id: 'time'
				,value: '46:28'
				,icon: 'fas fa-hourglass-half'
			}
			,{
				id: 'guide'
				,value: 'User guide'
				,icon: 'fas fa-map-signs'
			}
			,{
				id: 'setup'
				,value: 'SET UP'
				,icon: 'fas fa-cog'
			}
			,{
				id: 'notice'
				,value: 'Notice'
				,icon: 'fas fa-clipboard-list'
			}
			,{
				type: 'separator'
			}
			,{
				id: 'logout'
				,value: 'Logout'
				,icon: 'fas fa-sign-out-alt'
			}
			,{
				id: 'alarm'
				,icon: 'fas fa-bell'
				,circle: true
				,twoState: true
			}
		]
	});
	toolbar.events.on('click', function (id) {
		switch (id) {
			case 'logo':
				location.href = GLOBAL.CONTEXT_PATH;
				break;
				
			case 'logout':
				logout();
				break;
		}
	});
	
	// tabbar
	tabbar = new dhx.Tabbar('tabbar', {
		css: 'tabbar'
		,noContent: true
		,closable: true
		,tabHeight: 20
	});
	tabbar.events.on('change', function (id, beforeId) {
		if (id != beforeId) {
			if ($(`#iframe_${id}`).length == 0) { // 아직 iframe 이 만들어 지지 않았다면 추가
				$('#iframe').append($('<iframe>').attr('id', `iframe_${id}`).attr('src', tabbar.getCell(id).config.url));
			}
			$(`#iframe_${id}`).show(); // 현재 iframe 보이고
			$(`#iframe_${beforeId}`).hide(); // 이전 iframe 닫히고
			
			// 세로 구성일때 메뉴 선택
			if (GLOBAL.LOGIN_USER.menuType == MENU_TYPE_VERTICAL) {
				if (id == 'home') { // 홈탭이면 메뉴 선택 해제
					tree.selection.remove();
					
				} else {
					tree.focusItem(id); // 해당 id 요소로 포커스 : expand 까지 자동 실행
					tree.selection.add(id); // 해당 요소 선택
				}
			}
		}
	});
	tabbar.events.on('afterClose', function (id) {
		$(`#iframe_${id}`).remove(); // 탭이 닫힐때 iframe 삭제
	});
	tabbar.addTab({ // 기본 탭 추가 (닫기불가탭)
		id: 'home'
		,tab: 'HOME'
		,tabCss: 'home'
		,url: 'home' // TODO : 기본 탭 설정 - DB에서 가져온 값으로 설정하기
	});
});



//**********************************************************************************
// 로그아웃
//**********************************************************************************

function logout() {
	//if (confirm('로그아웃 하시겠습니까?', function() {
		$.post('user/logout', '', function(data) {
			if (data.result == RESULT_OK) { // 로그아웃 했다면
				location.replace(GLOBAL.CONTEXT_PATH);
				
			} else {
				alert(data.message);
			}
			
		}, 'JSON').fail(function() {
			alert('네트워크 문제로 로그아웃 되지 않았습니다');
		});
	//}));
}



//**********************************************************************************
// 왼쪽 메뉴
//**********************************************************************************

// 왼쪽 메뉴 토글
function toggleMenu() {
	if (isMenuClose()) { // 메뉴가 닫혔다면
		$('#layout .menu').removeClass('close');
		$('#layout .menu_toggle').removeClass('close');
		
	} else {
		tree.collapseAll(); // 트리 모두 닫는다
		$('#layout .menu').addClass('close');
		$('#layout .menu_toggle').addClass('close');
	}
}

// 왼쪽 메뉴가 닫혔는지 체크
function isMenuClose() {
	return $('#layout .menu').hasClass('close');
}




//**********************************************************************************
// 탭바
//**********************************************************************************

// 홈 탭바
function homeTabbar() {
	tabbar.setActive('home');
	if (GLOBAL.LOGIN_USER.menuType == MENU_TYPE_VERTICAL) {
		tree.selection.remove(); // 세로 구성일때 트리 메뉴 선택 해제
	}
}

// 탭바 모두 닫기
function closeTabbar() {
	if ((tabbar._cells != undefined) && (tabbar._cells != null)) {
		tabbar._cells.forEach(function(cell) {
			if (cell.id != 'home') {
				tabbar.removeTab(cell.id);
				$(`#iframe_${cell.id}`).remove();
			}
		});
	}
}