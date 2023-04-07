package herbaccara.toss.cert.model

import kotlin.Error

/**
 * Toss 본인 인증 응답은 2xx 일 때만 success 필드, 4xx/5xx 인 경우에만 error 필드가 채워 진다.
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
