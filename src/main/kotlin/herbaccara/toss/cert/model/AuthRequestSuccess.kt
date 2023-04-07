package herbaccara.toss.cert.model

import java.time.OffsetDateTime

data class AuthRequestSuccess(
    override val txId: String,
    val appScheme: String?,
    val androidAppUri: String?,
    val iosAppUri: String?,
    val requestedDt: OffsetDateTime,
    /**
     * RequestType 이 USER_NONE 인 경우에만 값이 존재한다.
     */
    val authUrl: String?
) : AuthSuccess
