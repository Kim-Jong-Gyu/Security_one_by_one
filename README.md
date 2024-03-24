
## 👋🏻 주제

---

- 소켓을 이용한 간단한 1:1 채팅
- 암호화 복호화 적용
- 대화 전송 기능 + 파일 첨부 기능


## 💪🏻 Project Structure

---

### 1. 서버

- Socket을 이용
    - 소켓은 프로세스가 네트워크를 통해 데이터를 송수신할 수 있도록 하는 창구이며, 떨어져 있는 두 호스트를 연결해주는 인터페이스의 역할을 하기 떄문에 사용했습니다.
    - 서버 측에서 서버 소켓을 생성함으로써, 소켓 생성 및 클라이언트의 소켓을 받을 준비를 했습니다.
        
       ![스크린샷 2022-05-30 오전 4 43 58 (1)](https://github.com/Kim-Jong-Gyu/Security_one_by_one/assets/62927374/e370dcdd-6eec-4d51-b8f5-a793ca74981d)

        
    - 클라이언트 측에서 서버를 생성하고, 포트 번호를 같게 함으로써 연결을 시켜줬습니다.

      ![스크린샷 2022-05-30 오전 4 43 58 (2)](https://github.com/Kim-Jong-Gyu/Security_one_by_one/assets/62927374/47bbca37-3afc-4395-8af9-d8a568a5a17c)
  

### 3. 데이터 수신

- Thread 사용
    - 쓰레드를 사용함으로써 데이터를 받는 하나의 실행 흐름을 만들었습니다.
        
        ![스크린샷 2022-05-30 오전 4 47 06 (1)](https://github.com/Kim-Jong-Gyu/Security_one_by_one/assets/62927374/0bc3aa3d-f843-4eea-a56c-c49dbe7054ea)

        
- 데이터 구조
    - 데이터 폴더를 따로 만들고, 데이터 송/수신 되는 타입별로 object를 만들어 처리하였습니다.
        
        ![스크린샷 2022-05-30 오전 4 54 36 (1)](https://github.com/Kim-Jong-Gyu/Security_one_by_one/assets/62927374/238c9fbf-d642-4e70-9a5b-d1b6d3d26389)

        
- 데이터 송/수신
    - 스트림을 사용하여 서로 데이터를 주고 받았습니다.
        - 스트림은 데이터가 드나드는 채널이라고 생각을 했습니다.
        - ObjectOutputStream/ ObjectInputStream
    - Obejct를 이용해 하나의 실행 흐름에 커맨드라는 String 변수를 통해 Method를 구분 시켜줬습니다.
        
       <img width="251" alt="Untitled (3)" src="https://github.com/Kim-Jong-Gyu/Security_one_by_one/assets/62927374/612dad62-0f13-49be-a2bb-895644a704ff">

        

### 4. 암호화

- 채팅 송/수신
    
    <img width="141" alt="Untitled (4)" src="https://github.com/Kim-Jong-Gyu/Security_one_by_one/assets/62927374/9a00ad6f-3f4f-4af7-af88-c1b08531f36e">

    
    - 서버의 public key를 전송을 하고 클라이언트에서 그 public key를 이용해 AES 대칭키를 암호화 시킨후 서버 측으로 다시 전송
    - 서버 측에서 클라이언트가 전송한 대칭키를 RSA 복호화를 통해 해독한 후 대칭키를 소유
    - 공유된 대칭키를 이용해 채탕 내용을 암호화 및 복호화 작업 수행
- 파일 송/수신
    
    <img width="266" alt="Untitled (5)" src="https://github.com/Kim-Jong-Gyu/Security_one_by_one/assets/62927374/ec01eb58-583c-4d98-bb35-06709ed6fb9b">

    
    - JFileChooser 라는 라이브러리를 이용해 다이얼로그 생성 및 파일 선택
        - 선택 시 변수에 저장
    - AES 대칭키를 이용해 파일과 파일 이름을 암호화
    - RSA 전자서명을 이용해 해쉬화 작업 수행
    - 송신 측에서 RSA 대칭키를 이용해 전자서명 해쉬화 복구
    - 데이터가 같은지 비교
    - 데이터가 같을 경우 저장 할 수 있다.
