
# 🎮 Android 리듬 게임 프로젝트

## 📌 게임 컨셉

건반형 리듬 게임으로, 위에서 아래로 떨어지는 노트를 터치하여 연주하는 방식입니다.  
**Perfect, Good, Miss**의 판정 시스템을 통해 점수와 콤보가 누적되며,  
**곡 선택 → 플레이 → 결과 화면**까지의 전형적인 리듬 게임 흐름을 구현합니다.

---

## 🔧 핵심 메카닉 및 기능 요약

| 기능 | 설명 |
|------|------|
| 🎵 노트 낙하 시스템 | 노트가 일정한 속도로 위에서 아래로 낙하 |
| ⌨️ 입력 판정 | Charming / Normal / Miss로 판정하여 점수 계산 |
| 📈 점수 및 콤보 | 정확도에 따른 점수 증가 및 콤보 유지 시스템 |
| 🎼 곡 선택 시스템 | UI에서 곡 선택 및 미리듣기 기능 제공 |
| 🕹️ 플레이 UI | 건반, 판정 라인, 점수 및 콤보 시각화 |
---

## 🎵 노트 타입

- 일반 노트
- 슬라이드 노트

---

## 🎼 플레이 가능한 곡 수

- 1곡 제공 (Deemo - nightfall)


---

## 🖥️ UI 구성

- 메인 메뉴
- 곡 선택 화면
- 플레이 화면
---

## 🧩 예상 게임 흐름

1. **곡 선택**
2. **게임 플레이**
   - 터치 입력 시 타격 이펙트 발생
   - Charming 판정 및 콤보 시각적으로 출력

> 🎨 배경은 간단한 이미지 사용

---

## 🗓️ 개발 일정 (8주간 진행)

| 주차 | 기간 | 개발 내용 | 진척도|
|------|------|-----------|-----------|
| 1주차 | 4/8 ~ 4/14 | 게임 기획 및 화면 구성 정리 | 100%
| 2주차 | 4/15 ~ 4/21 | 노트 낙하 로직, 터치 입력 및 판정 처리 |100%
| 3주차 | 4/22 ~ 4/28 | 스코어 및 콤보 시스템 구현 |100%
| 4주차 | 4/29 ~ 5/5 | 플레이 UI 구성 및 적용 |100%
| 5주차 | 5/6 ~ 5/12 | 곡 선택 화면 및 미리듣기 기능 구현 |100%
| 6주차 | 5/13 ~ 5/19 | 결과 화면 및 그래픽/효과음 추가 |0%
| 7주차 | 5/20 ~ 5/26 | 전체 통합 테스트 및 버그 수정 |50%
| 8주차 | 5/27 ~ 6/2 | 최종 정리 및 배포 준비 |50%

---

## 2차 발표 내용 
![스크린샷 2025-05-11 214818](https://github.com/user-attachments/assets/38e93a4d-c630-469d-85bc-9322897da22d)
| 주차 | 커밋수 | 
|------|------|
| 1주차 | 3| 
| 2주차 | 1 |
| 3주차 | 0 |
| 4주차 | 12 |
| 5주차 | 9 |


## 3차 발표 내용 

사용된 기술 

ViewPager2를 통한 캐러셀 뷰 구현 
JSON Reader - 수업 자료 차용
슬라이드 입력 처리

아쉬운 점 및 어려웠던 점

1. 롱노트 구현에 실패했다
   => 단일 스프라이트를 사용해서 표현하려고 하니 이미지가 왜곡되는 현상
      판정을 진행할 때 판정처리가 완료된 노트를 삭제해버리기 때문에 롱노트가 통째로 사라져 우리가 흔히 상상하는 롱노트의 모습과는 거리가 멀었음.
   => 롱노트를 머리, 중앙, 꼬리로 3등분해서 접근해보면 해결할 수 있지 않을까.
2. 슬라이딩 노트 구현에서의 어려움
   => 슬라이딩 노트와 관련해서 버그가 많이 발생했었음. 중복 판정, 노트 판정이 먹통이 되는 등등. 관련 이슈 대응에 시간을 많이 소모함.
3. 메모리 누수
   => ViewPager로 페이지를 변경할 때마다 새로운 MediaPlayer를 생성하면서 메모리 누수가 발생했었음.

수업에 관한 이야기

기본적인 게임 프레임워크부터 쌓아올리는 수업을 통해서 게임을 제작할 때 어느 부분을 고려해야 되는지 특정 게임을 제작할 때는 어떤 것을 고려해야하는지 배울 수 있어서 유익한 수업이었다. 

