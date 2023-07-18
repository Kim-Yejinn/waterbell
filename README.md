### 목차

- [**2023-07-17**](#2023-07-17)

<br>

# **2023-07-17**
## 스크럼회의(2023/07/17)

- 기존 서비스를 지하차도에 적용하자는 의견 나옴.
- 건물의 범위를 넓히는 서비스를 한다면 괜찮지만 아예 바꿔야 하는 건 어려울 것 같다.
- 온습도, 미세먼지 줄이고 침수관리 쪽으로 초점
    ⇒ 건물(지하주차장)관리, 지하차도 관리
- 운전자들은 웹사이트를 통해 정보를 조회할 수 있게
- 지하차도에 실제 설치하기는 힘들지만 설치한다는 가정으로 차수판을 활용하자.
- 지자체 대상 추가
    - 지도 어플을 하나 넣는 형식으로 실시간 댐 api를 활용하여 지도에 위험지역 표기해주는 기능
    - LED판에 미세먼지, 침수 경보 등 정보 표시
    - 앞에서 차단을 해준다는것이 차이점(입구에서 안들어가게만 하도록 해도 피해를 줄일 수 있음)
    - 차수판 설치 하고 사이렌 울림(물을 늦출 수 있음) + TTS - 현장 제제 목적

<br>

- 지자체와 기존 건물 관리의 통일성 문제
    - 알림을 서브로 가져가는 건?
    - 지하주차장 → 차수판 올라감 / 지하차도 → 사이렌 / 사이렌 → 지자체 알림 → 차수판 올림

<br>

- 사용자 없애고 재난 대비로 가는 것은?
- 사용자는 있어야 될것 같고 미세먼지와 같은 정보는 지자체가 관리하는 데이터인데 의미가 있을까?
- 입주민 없애고 세대 정보 알고 있고 알림을 주는것
- 지하차도는 차수판과 경고만
- 사용자를 없애고 지자체에게 알림
- 입주민 없애고 건의사항 대신 신고 접수
- 지자체 번호로 보내는 느낌
- 서비스의 볼륨이 없어짐.
- 건물 관리자 측면은 유지,

- 맥락을 벗어남. 대시보드 부실하다고 느끼면,
- 아파트회원 지자체 회원 → 공통 cctv 방송 알림 등
- 어떻게 갈리냐? 온습도 미세먼지 없애고 여러개를 관리하는 느낌으로
- 지자체라 함은 재난대비
- 전국에 있는 현황을 확인하며 통솔하는 입장
- cctv 여러개 봐야함.
- 지하차도 별로 알림이 올 것
- cctv 수치 원격제어 기능
- 신고페이지 괜찬
- 건의사항 빼고 할거면 주민 가입 x 주민 정보 가져오는 방법 다르게 또는 임의로 넣는다고 생각(알림용)

- 정보 제공 받은 입장
- 사용자 입장에서는 gps로 받는게 더 효율적
- 관리자 입장에서는 cctv 활용 할 것 , 진입 차단 시설 → 터널, 지하차도 일부 설치 된 바 있음 → 웹사이트 간단 조작. 제어 기기 있음. → 정보 제공 측면에서 하는 것이 좋을 듯 하다.

<br>

# 기획 회의(주제 변경) (2023/07/17)

1안 : 기존 현행 유지(건물관리만)

2안 : 건물 + 지하차도 (재난 전부)

3안 : 지하차도만

4안 : 건물 + 지하차도 (침수 포커스) 

5안 : 건물 + 지하차도 (관리인만) 

### 최종 결정
**4안 : 건물 + 지하차도 (침수 포커스)**

1. **지하차도**

- 구역 선택 (목록은 서비스 운영자가 제공한다고 가정?)
    - 메인 배너 바로 밑에 위치

<br>

- 지하차도 대시보드(일반인+관리인)

    <img src="/img/지하차도_대쉬보드.jpg" width="600" height="400"/>

    - CCTV(목록에서 선택)
    - 지도(지하차도 좌표 + 하천수위)
    - 신고접수 현황 ⇒ 이것만 일반인 상세 이동. (관리자도 상세 이동 가능)
    - 시간별 침수 높이(그래프)
    - 강수량 데이터(그래프?)
    - 관리자만 제어페이지(상세페이지로 이동, 신고접수만 게시판 나머지는 제어페이지로))
    - 차수판 동작여부(상단 배치)

    <br>

- 신고접수 게시판(일반인+관리인)

    <img src="/img/지하차도_신고접수.jpg" width="600" height="400"/>

    - (초록 또는 파랑) 공지사항
    - 일반인 신고접수
    - 글쓰기 버튼
    - 글쓰기 ⇒ 사진 첨부 기능
    - 수정, 삭제 (같은 휴대폰 번호 입력하면 가능)

    <br>

- 제어 페이지(관리인)

    <img src="/img/지하차도_제어.jpg" width="600" height="400"/>

    - CCTV 2개 (입구 + 내부)
    - 경고등
    - 차수막, 사이렌 동작 버튼
    - TTS
    - 1차 경고등 동작은 자동으로 할 것인가 ? : 일단 자동 아닌 것으로 하기 (추후 바뀔 가능성O)

    <br>

- 시스템

    <img src="/img/지하차도_시스템.jpg" width="600" height="400"/>

    - 일자 선택 (기간) 디폴트는 오늘날짜
    - 세부 테이블 선택
    - 조회하고 싶은 내용 선택 (센서 정보 / 사이렌 동작여부 / TTS 동작여부 / 차수 동작 여부/알림 기록)
    - 수신 알림 내역(이력) (센서 관련 정보와 메뉴 분리, 건물 관리와 ui 동등하게 진행)

    <br>

- 알림함
    - 페이지 상단 종모양.!!
    - 건물 관리랑 동등하게 ui 진행

- 관리
    - 기준치 설정과 메시지 문구(TTS)를 한 화면에 배치

1. **건물**
- 대시보드(관리인 + 입주민)

    <img src="/img/건물_대쉬보드.jpg" width="600" height="400"/>

    - CCTV
    - 수위 그래프
    - 강수 그래프
    - 실시간 온습도 / 미세먼지
    - 신고현황
    - 관리자공지
    - 특보(비 관련만)
    - 차수판 동작여부(상단 배치)

<br>

- 건의사항 → 신고 접수(관리인 + 입주민)
    - 기능 명세서 용어 수정하기

<br>

- 제어 (+ 알림 통합)(관리인)

    <img src="/img/건물_제어.jpg" width="600" height="400"/>

    - CCTV
    - 차수판 동작
    - TTS, 앱, 문자메시지 알림 보내기

<br>

- 시스템(관리인)

    <img src="/img/건물_시스템.jpg" width="600" height="400"/>

    - 일자 선택 (기간) 디폴트는 오늘날짜
    - 세부 테이블 선택
    - 조회하고 싶은 내용 선택 로그(센서 정보 / 사이렌 동작여부 / TTS 동작여부 / 차수 동작 여부) ,알림(수신 알림 기록, 발신 알림 기록)
    - 유용한 로그(통합 테이블) → 좀 더 고민 해보기

<br>

- 관리(관리인)

    <img src="/img/건물_관리.jpg" width="600" height="400"/>

    - 메시지 문구 설정(TTS, 문자, 앱 알림)
    - 기준치 설정(1차 경고, 2차 경고)
    - 입주민 관리(위 2개랑 탭 따로)

<br>

- 건물 / 지하차도 어떻게 구별? 지급된 관리자 계정으로 구별한다고 가정
    <img src="/img/지하차도_건물_로그인선택.jpg" width="600" height="400"/>

<br>

### 기능 명세서 수정

![기능명세서_관리__알림__대시보드_](/uploads/fbe2f4f5790f22086633c5a6c0fef250/기능명세서_관리__알림__대시보드_.PNG)
![기능명세서_로그인__회원가입__로그아웃__알림함_](/uploads/55f62ce12aa5f18b28930a058fbd0d8c/기능명세서_로그인__회원가입__로그아웃__알림함_.PNG)
![기능명세서_시스템페이지__자동알림__제어_](/uploads/9dd0743621e46daaa13f02dea77fad4c/기능명세서_시스템페이지__자동알림__제어_.PNG)
![기능명세서_신고접수__IOT__마이페이지_](/uploads/8e064cdbc4df1f6ffa24ed7be252ebfd/기능명세서_신고접수__IOT__마이페이지_.PNG)

    