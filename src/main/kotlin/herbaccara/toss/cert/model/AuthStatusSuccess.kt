package herbaccara.toss.cert.model

import java.time.OffsetDateTime

data class AuthStatusSuccess(
    /***
     * 상태를 조회한 인증 요청 트랜잭션 아이디
     */
    override val txId: String,

    /***
     * 인증 상태의 종류
     */
    val status: Status,

    val requestedDt: OffsetDateTime
) : AuthSuccess
