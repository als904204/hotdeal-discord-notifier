## 📖 프로젝트 개요

**핫딜 알림 디스코드 봇**은 국내 주요 커뮤니티(펨코, 알구몬, 뽐뿌, 퀘이사존 등)의 핫딜 정보를 실시간으로 크롤링하고, 사용자가 설정한 키워드에 맞는 정보를 디스코드 개인 DM으로 자동 전송하는 서비스입니다.

- 별도 사이트 회원가입 없이 디스코드 계정만으로 즉시 사용 가능
- 키워드 기반 맞춤형 알림 제공

---

## 🔍 기존 문제점

1. **회원가입/로그인 필요**\
   각 커뮤니티별로 계정 생성 및 로그인 절차가 필요.
2. **사이트 직접 방문 필요**\
   원하는 핫딜 정보를 확인하려면 매번 해당 사이트에 접속해야 함.
3. **수동 확인 불편**\
   알림 설정 이후에도 사용자가 직접 방문하여 최신 정보를 확인해야 함.
4. **다중 사이트 모니터링 어려움**\
   여러 커뮤니티를 동시에 감시하기 위한 별도 툴이나 수작업이 필요.

---

## 💡 해결 방안

- **통합 알림 시스템**: 디스코드를 통해 모든 핫딜 정보를 하나의 채널(DM)로 통합 제공
- **간편한 접근성**: 디스코드 계정만으로 별도 회원가입 불필요
- **자동화된 키워드 매칭**: 서버 측 크롤링 → DB 저장 → 키워드 매칭 → 개인 DM 전송
- **중복 알림 방지** 및 **종료된 핫딜 동기화** 기능 내장

---

## 📝 주요 기능

### 사용자 명령어

| 명령어         | 설명            | 예시          |
| ----------- | ------------- | ----------- |
| `/등록 [키워드]` | 알림 받을 키워드 등록  | `/등록 그래픽카드` |
| `/삭제 [키워드]` | 등록된 키워드 삭제    | `/삭제 SSD`   |
| `/목록`       | 현재 등록된 키워드 확인 | `/목록`       |
| `/도움말`      | 사용 가능한 명령어 안내 | `/도움말`      |

### 시스템 동작

- **멀티 사이트 크롤링**: N분 간격으로 주요 커뮤니티 사이트를 순회
- **키워드-사용자 매칭**: DB에 저장된 키워드와 크롤링 데이터를 실시간 비교
- **개인 DM 알림**: 매칭된 핫딜 정보를 디스코드 개인 DM으로 전송
- **중복 알림 방지**: 이미 발송된 핫딜은 재발송되지 않도록 처리
- **종료된 핫딜 동기화**: 만료된 핫딜 정보는 DB에서 자동 제거 또는 상태 업데이트

---

## ⚙️ 개발 환경

- **언어 및 프레임워크**
    - Java 21
    - Spring Boot 3.4
    - Spring Security 6.4
    - JPA (Hibernate)
- **데이터베이스**
    - MariaDB / MySQL / PostgreSQL (추후 확정)
- **빌드 도구**
    - Gradle
- **외부 라이브러리**
    - Jsoup (HTML 파싱)
    - Discord JDA (디스코드 봇 연동)

---

## 🛠️ 설정 파일

### src/main/resources/application.yml

```yaml
spring:
  profiles:
    active: dev
```

### src/main/resources/application-dev.yml

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mariadb://localhost:3306/YOUR_DB_NAME?allowPublicKeyRetrieval=true
    username: YOUR_DB_USERNAME
    password: YOUR_DB_PASSWORD
    driver-class-name: org.mariadb.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
    show-sql: true
    properties:
      hibernate:
        default_batch_fetch_size: 100
    open-in-view: false

backend:
  api:
    base-url: http://localhost:8080
    keyword-path: /api/keywords

hotDeal:
  cron: "0 */1 * * * *"

community:
  fmkorea:
    url: "https://www.fmkorea.com/index.php?mid=hotdeal"
    pages-to-crawl: 3
    list-item-selector: "div.fm_best_widget ul li.li"
    link-selector: "h3.title a[href^=\"/\"]"
    ended-deal-class: "hotdeal_var8Y"

discord:
  bot:
    token: YOUR_DISCORD_BOT_TOKEN
    channel-id: YOUR_DISCORD_CHANNEL_ID
    cron: "0 */1 * * * *"
    keyword: "키워드"

jsoup:
  user-agent: "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
  timeout-ms: 15000
```

---

## 🙋‍♂️ 문의 및 기여

- **이슈 등록**: GitHub Issue
- **풀 리퀘스트**: `main` 브랜치로 PR 요청
- **연락처**: [min.uuu1117@gmail.com](mailto\:min.uuu1117@gmail.com)

---

궁금하신 사항이나 개선 제안이 있으시면 언제든지 이슈로 등록 부탁드립니다



