# BigMap

디자인 UI/UX 확인 링크 -> https://xd.adobe.com/view/bf6167fc-78c9-4765-a8d6-2dbcea0ef45a-ea32/

1. 페이지 명
페이지 역할 : [레이아웃(xml) 이름/java 이름]
- 로그인 페이지 : activity_login/Login
- 메인 페이지(+Navi SDK 시작 페이지) : activity_main/MainActivity -- CoordinatorLayout 형식
- 회원가입(개인정보 입력) : activity_person_inform/PersonIform
- 회원가입 전 개인정보 동의란 : activity_user_check/UserCheck
- 회원가입 후 환영 페이지 : activity_welcome_view/WelcomeView
- 사용자 게시판(건의함) : fragment_board/BoardFragment -- Fragement 형식
- 즐겨찾기 페이지 : fragment_bottom__favorite/Bottom_Favorite -- BottomeSheetFragment 형식
- 홈 페이지 : fragment_bottom__home/Bottom_Hoem -- BottomeSheetFragment 형식
- 마이페이지 : fragement_mypage/MypageFragment

2. values 파일
- array <emailaddr> : 회원가입시 사용할 이메일 작성 스피너 선택 배열
- color : 처음부터 자동으로 맞춰져있던 컬러들. 디자인적으로 마음에 안들면 수정해서 메인 컬러 바꾸기.
- strings <access1>, <access2>, <Certif_messages>: 회원가입 페이지에서 사용하는 문자열.

3. drawable
- 하단 메뉴 이미지 파일들 : baseline_home_24, baseline_person_24,baseline_star_24, baseline_view_list_24
- 모서리가 둥근 사각형 프레임(홈, 즐겨찾기 페이지에서 '집', '회사' 부분의 틀에 사용) : shape
- Top 모서리만 둥근 layout 프레임(홈, 즐겨찾기 layout 프레임): shape_layout
- userimage.jpg : 로그인 화면에서 '이메일' 옆에 있는 사람모양 사진. 다른 사진으로 바꿔도 무방.
- 일반 사각형 틀(로그인, 회원가입, 회원가입 전 개인정보 동의 페이지의 텍스트, editText 프레임으로 사용) : vew_bolder
