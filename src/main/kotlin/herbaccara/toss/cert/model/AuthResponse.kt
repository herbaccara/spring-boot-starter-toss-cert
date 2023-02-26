package herbaccara.toss.cert.model

import kotlin.Error

/***
 * Toss 본인 인증 응답은 2xx 일 때만 success 필드, 4xx/5xx 인 경우에만 error 필드가 채워 진다.
 * Java 와 ObjectMapper 의 환상적인 궁합으로 인해 generic 사용시 스트레스 받기 때문에 사용 금지.
 * 불필요한 AuthResponse 하위 객체 생성 또한 오버헤드가 있다.
 */
@Deprecated("")
data class AuthResponse<T : AuthSuccess>(
    val resultType: ResultType,
    val success: T? = null,
    val error: Error? = null
) {
    enum class ResultType {
        SUCCESS, FAIL
    }
}
