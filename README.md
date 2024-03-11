# 간단한 1:1 채팅

## 👋🏻 주제

---

- 소켓을 이용한 간단한 1:1 채팅
- 암호화 복호화 적용
- 대화 전송 기능 + 파일 첨부 기능

## 📋 사용 방법

---

- ServerChat.java 파일을 실행
    
    ![스크린샷 2022-05-30 오전 4.23.01.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/bc5b4f45-5297-4fcc-b571-97efce1d02db/스크린샷_2022-05-30_오전_4.23.01.png)
    
- ClientChat.java 파일을 실행
    
    ![스크린샷 2022-05-30 오전 4.23.43.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/3fc93f65-3305-4d9d-9abf-3868971fa280/스크린샷_2022-05-30_오전_4.23.43.png)
    
- 1:1 채팅
    - 서버 GUI에서 Key 생성 버튼 클릭 → 서버의 public key와 private key가 생성이 된다
    
    ![스크린샷 2022-05-30 오전 4.24.59.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/4db99a8f-c1d0-48a7-ab9d-7aaee9c9c520/스크린샷_2022-05-30_오전_4.24.59.png)
    
    - 서버 GUI에서 Send Public key button 클릭 → 서버의 public key가 클라이언트로 전송된다.
    
    ![스크린샷 2022-05-30 오전 4.25.43.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/7bb1ae00-8c40-49fc-b116-e6d94f7d3ed9/스크린샷_2022-05-30_오전_4.25.43.png)
    
    - 클라이언트 GUI에서 Send Secret key button 클릭 → AES Key 공유
    
    ![스크린샷 2022-05-30 오전 4.26.20.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/660cba11-b4a8-4880-9f57-67424ab5edaf/스크린샷_2022-05-30_오전_4.26.20.png)
    
    - 원하는 메세지 작성 후 Send 버튼 클릭
    
    ![스크린샷 2022-05-30 오전 4.27.40.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/268c8dbb-4530-4635-87db-fb43b9a5ad51/스크린샷_2022-05-30_오전_4.27.40.png)
    
- 파일 전송
    - Secret Key 교환이 끝나야 가능
    - 클라이언트 GUI나 서버 GUI 둘 중에서 전송을 원하는 쪽에서 Send file 버튼 클릭 → 클릭시 파일 선택가능
    
    ![스크린샷 2022-05-30 오전 4.28.24.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c63f4bd8-d3f9-4c30-83e5-09dc7d8d26bf/스크린샷_2022-05-30_오전_4.28.24.png)
    
    - 받는 GUI에서 Sava FIle 버튼 클릭 → 클릭시 저장위치 선택 가능
    
    ![스크린샷 2022-05-30 오전 4.29.09.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c5436b34-a12d-4376-83c1-54dd6d90e5d8/스크린샷_2022-05-30_오전_4.29.09.png)
    

## 💪🏻 Project Structure

---

### 1. 서버

- Socket을 이용
    - 소켓은 프로세스가 네트워크를 통해 데이터를 송수신할 수 있도록 하는 창구이며, 떨어져 있는 두 호스트를 연결해주는 인터페이스의 역할을 하기 떄문에 사용했습니다.
    - 서버 측에서 서버 소켓을 생성함으로써, 소켓 생성 및 클라이언트의 소켓을 받을 준비를 했습니다.
        
        ![스크린샷 2022-05-30 오전 4.41.35.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c3aa2b10-96eb-40a5-8c60-88852a7b7e25/스크린샷_2022-05-30_오전_4.41.35.png)
        
    - 클라이언트 측에서 서버를 생성하고, 포트 번호를 같게 함으로써 연결을 시켜줬습니다.
        
        ![스크린샷 2022-05-30 오전 4.43.58.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/bc82e44d-de79-4b46-b266-cb70f7bef4f6/스크린샷_2022-05-30_오전_4.43.58.png)
        

### 3. 데이터 수신

- Thread 사용
    - 쓰레드를 사용함으로써 데이터를 받는 하나의 실행 흐름을 만들었습니다.
        
        ![스크린샷 2022-05-30 오전 4.47.06.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/0fdadb60-d2d8-4683-83a5-30f1f5f339bb/스크린샷_2022-05-30_오전_4.47.06.png)
        
- 데이터 구조
    - 데이터 폴더를 따로 만들고, 데이터 송/수신 되는 타입별로 object를 만들어 처리하였습니다.
        
        ![스크린샷 2022-05-30 오전 4.54.36.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/f5fddf99-f8b8-4625-963d-9c1819c7b724/스크린샷_2022-05-30_오전_4.54.36.png)
        
- 데이터 송/수신
    - 스트림을 사용하여 서로 데이터를 주고 받았습니다.
        - 스트림은 데이터가 드나드는 채널이라고 생각을 했습니다.
        - ObjectOutputStream/ ObjectInputStream
    - Obejct를 이용해 하나의 실행 흐름에 커맨드라는 String 변수를 통해 Method를 구분 시켜줬습니다.
        
        ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/21112d59-80fe-43db-a1fa-3d83a0acb3d0/Untitled.png)
        

### 4. 암호화

- 채팅 송/수신
    
    ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/1bce4978-9a10-4435-9ca9-94cac2155ba4/Untitled.png)
    
    - 서버의 public key를 전송을 하고 클라이언트에서 그 public key를 이용해 AES 대칭키를 암호화 시킨후 서버 측으로 다시 전송
    - 서버 측에서 클라이언트가 전송한 대칭키를 RSA 복호화를 통해 해독한 후 대칭키를 소유
    - 공유된 대칭키를 이용해 채탕 내용을 암호화 및 복호화 작업 수행
- 파일 송/수신
    
    ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/3dce1902-1c3e-4e78-8d63-9c29100598e0/Untitled.png)
    
    - JFileChooser 라는 라이브러리를 이용해 다이얼로그 생성 및 파일 선택
        - 선택 시 변수에 저장
    - AES 대칭키를 이용해 파일과 파일 이름을 암호화
    - RSA 전자서명을 이용해 해쉬화 작업 수행
    - 송신 측에서 RSA 대칭키를 이용해 전자서명 해쉬화 복구
    - 데이터가 같은지 비교
    - 데이터가 같을 경우 저장 할 수 있다.

## 🙅🏻부족한 점

---

### 1. 파일 수신시 메모장을 이용한 파일 제외하고는 파일 내용이 깨진다.

- 암호화 과정에서 bit단위의 파일을 String으로 전환 후 다시 복구하는 작업에서 문제가 발생한 것이라고 생각합니다.
