package herbaccara.toss.cert.form

enum class RequestType {
    /**
     * 토스 표준창을 호출하는 방식으로 토스 웹 페이지에서 고객 정보를 입력받아 인증 요청 (표준창 사용에는 triggerType 선언 불필요)
     */
    USER_NONE,

    /**
     * 고객의 이름, 생년월일, 전화번호 정보를 기반으로 인증 요청 (암호화된 userName, userPhone, userBirthday 정보 필수)
     */
    USER_PERSONAL
}
