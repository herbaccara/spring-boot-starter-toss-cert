package herbaccara.toss.cert.form

import com.fasterxml.jackson.annotation.JsonInclude

/***
 * Case1. 표준창을 이용해서 인증 요청하기
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class TossCertUserNoneAuthRequestForm @JvmOverloads constructor(
    override val successCallbackUrl: String? = null,
    override val failCallbackUrl: String? = null,
    override val nonce: String? = null,
    override val expireSeconds: Int? = null
) : AuthRequestForm {
    override val requestType: RequestType = RequestType.USER_NONE
}
