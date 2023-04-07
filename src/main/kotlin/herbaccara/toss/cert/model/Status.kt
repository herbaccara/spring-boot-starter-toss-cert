package herbaccara.toss.cert.model

enum class Status {
    /**
     * 토스 인증서버에서 사용자의 토스 앱으로 인증이 요청된 상태
     */
    REQUESTED,

    /**
     * 사용자가 인증을 진행 중인 상태
     */
    IN_PROGRESS,

    /**
     * 고객이 인증을 완료한 상태
     */
    COMPLETED,

    /**
     * 유효시간 만료로 인증 진행이 불가한 상태
     */
    EXPIRED
}
