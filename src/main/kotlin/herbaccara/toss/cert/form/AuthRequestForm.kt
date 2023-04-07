package herbaccara.toss.cert.form

/**
 * [봉인인증 요청 API](https://toss.im/tosscert/docs/guides/integration/user#2-%EB%B3%B8%EC%9D%B8%EC%9D%B8%EC%A6%9D-%EC%9A%94%EC%B2%AD-api)
 */
interface AuthRequestForm {

    val requestType: RequestType

    /**
     * 토스 앱 인증이 성공적으로 완료되고 돌아갈 고객사 앱스킴 주소
     */
    val successCallbackUrl: String?

    /**
     * 토스 앱 인증이 실패 했을 때 돌아갈 고객사 앱스킴 주소
     */
    val failCallbackUrl: String?

    /**
     * 중복 요청 방지를 위한 옵셔널 파라미터
     * 전자서명 원문에 포함시킬 값으로 고객사에서 자체 생성하는 트랜잭션아이디의 기능처럼 활용 가능합니다.
     */
    val nonce: String?

    /**
     * 인증을 할 수 있는 유효시간으로 최대 1800(초)으로 설정 가능
     */
    val expireSeconds: Int?
}
