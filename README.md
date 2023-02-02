# 🥕 Waffle-Market 🥕
## 🥕 당근마켓 클론 코딩 와플마켓 API 서버
- 당근마켓 기능을 클론 코딩한 와플마켓 API 서버입니다.
- 단순 게시판 기능 외에 예약, 거래, 채팅 등의 부가 기능이 더해진 서비스를 구현해보면서 구성원들의 개발 실력 성장을 이루고자 했고,
서비스 기획에 인풋을 줄이기 위해 기존에 있는 서비스를 기반으로 클론 코딩을 하였습니다.


## 😀 유저 플로우 및 서비스 




## 👨‍👩‍👧‍👦 누가 만들었나요?

|                           [김좌훈(JwahoonKim)](https://github.com/JwahoonKim)                                                                           |                                                    [김준형(leeeryboy)](https://github.com/leeeryboy)                                                    |                                    [곽민지(Joanne19-drive)](https://github.com/Joanne19-drive)                                  |
|:-----------------------------------------------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------------------------------:|
| <a href="https://github.com/JwahoonKim"><img src="https://avatars.githubusercontent.com/u/72662822?v=4" width="160px" style='border-radius: 50%'></a> | <a href="https://github.com/leeeryboy"><img src="https://avatars.githubusercontent.com/u/62370411?v=4" width="160px" style='border-radius: 50%'></a> | <a href="https://github.com/Joanne19-drive"><img src="https://avatars.githubusercontent.com/u/84649662?v=4" width="160px" style='border-radius: 50%'></a> |



## 📚 기술 스택
<div align=center> 
    <img src="https://img.shields.io/badge/kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white">
    <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=Springboot&logoColor=white">
    <br>
    <img src="https://img.shields.io/badge/Spring data jpa-6DB33F?style=for-the-badge&logo=Databricks&logoColor=white">
    <img src="https://img.shields.io/badge/QueryDSL-0389CF?style=for-the-badge&logo=SingleStore&logoColor=white">
    <br>
    <img src="https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
    <img src="https://img.shields.io/badge/Mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
    <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
    <img src="https://img.shields.io/badge/amazon aws-232F3E?style=for-the-badge&logo=amazon aws&logoColor=white">
</div>


## 🧑‍💻Convention
- 프로젝트를 시작 전 아래와 같은 컨벤션을 정함으로써 코드 관리에 효율성을 높였습니다.


### 👔 Code Convention

- ktlint 썼던거 관련 내용 적기

### 😎 Commit Convention
- 아래와 같은 커밋 컨벤션을 통해 서로의 작업물이 어떤 유형의 것인지 파악하기 쉽도록 하였습니다.
<img width="600" alt="스크린샷 2023-02-01 오후 11 26 44" src="https://user-images.githubusercontent.com/72662822/216070103-f68194a6-0ecb-4539-912b-f47107b4f500.png">

### ⛙ Git Branch 전략 및 Merge 전략
<img width="759" alt="스크린샷 2023-02-01 오후 11 43 46" src="https://user-images.githubusercontent.com/72662822/216074373-cc670c46-8f34-44c6-b153-06221d125330.png">

- Github-flow를 사용했습니다.
  - 크지 않은 사이즈의 토이프로젝트였기 때문에 간단한 Branching 전략인 Github-flow를 사용했습니다.
  - 각자의 작업물은 feature 브랜치를 만들어 관리했으며, 완성한 작업물은 main 브랜치에 Squash Merge 전략을 사용해 합쳤습니다.  
- 아래와 같은 근거로 Merge 전략은 Squash Merge 전략을 택했습니다.
  - 각자 작업한 결과물의 완성본이 main 브랜치의 관심사였던 점
  - PR을 날리는 단위를 작게 하자고 미리 협의한 점
  - 각자 작업물의 히스토리는 main 브랜치에 남기기보다는 로컬에 남겨 관리하는 것이 편하겠다고 판단한 점


## 📖 기술적인 이슈 및 개발 과정을 겪으며 정리한 글
1. [채팅을 웹소켓으로 구현하며..](https://kjhoon0330.tistory.com/entry/Spring-Dev-%EC%9B%B9%EC%86%8C%EC%BC%93%EC%97%90-%EB%8C%80%ED%95%98%EC%97%AC)
2. [채팅 읽은 사람 표시 기능 구현 및 어려웠던 점, 추후 개선해야할 사항](https://kjhoon0330.tistory.com/entry/Spring-%EC%B1%84%ED%8C%85-%EC%9D%BD%EC%9D%8C-%ED%99%95%EC%9D%B8-%EA%B8%B0%EB%8A%A5-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0)
3. [깃 머지 전략](https://kjhoon0330.tistory.com/entry/Git-Merge-%EC%A0%84%EB%9E%B5%EC%97%90-%EB%8C%80%ED%95%98%EC%97%AC)
4. [회원 가입에 Redis를 쓰게된 계기](https://leeeryboy.tistory.com/2)
5. [N + 1 문제 해결](https://kjhoon0330.tistory.com/entry/Spring-JPA-N-1-%EB%AC%B8%EC%A0%9C%EC%97%90-%EB%8C%80%ED%95%98%EC%97%AC)
6. [동네 주변 글은 어떻게 찾을 수 있었는가](https://kjhoon0330.tistory.com/entry/Spring-Dev-%EC%9B%B9%EC%86%8C%EC%BC%93%EC%97%90-%EB%8C%80%ED%95%98%EC%97%AC)
7. [Docker를 활용한 배포 과정](https://kjhoon0330.tistory.com/entry/Spring-Dev-%EC%9B%B9%EC%86%8C%EC%BC%93%EC%97%90-%EB%8C%80%ED%95%98%EC%97%AC)
8. [JWT Token 관련 issue](https://kjhoon0330.tistory.com/entry/Spring-Dev-%EC%9B%B9%EC%86%8C%EC%BC%93%EC%97%90-%EB%8C%80%ED%95%98%EC%97%AC)


## DB Diagram
<img width="1035" alt="스크린샷 2023-01-31 오후 5 04 13" src="https://user-images.githubusercontent.com/72662822/216069474-a4bd448a-ed4b-456a-bad7-9ceeb04fb220.png">



## 🎉 개발 후기
