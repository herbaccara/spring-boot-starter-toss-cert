package herbaccara.toss.cert.form

enum class TriggerType {
    /***
     * 토스 앱 푸쉬를 이용해서 고객의 토스 앱으로 직접 푸쉬를 발송하고 인증을 진행하는 방식
     */
    PUSH,

    /***
     * 토스 인증 서버에서 인증 결과로 전달하는 OS별 앱스킴을 고객사 서버에서 직접 호출하고 인증을 진행하는 방식
     */
    APP_SCHEME
}
