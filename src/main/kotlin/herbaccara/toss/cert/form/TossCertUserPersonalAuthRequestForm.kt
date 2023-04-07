package herbaccara.toss.cert.form

import com.fasterxml.jackson.annotation.JsonInclude

/**
 * Case 2. 앱 푸쉬를 이용해서 인증 요청하기
 * Case 3. 토스 서버에서 생성하는 앱스킴을 이용해서 인증 요청하기
 *
 * <a href="https://toss.im/tosscert/docs/guides/common#%EA%B0%9C%EC%9D%B8%EC%A0%95%EB%B3%B4-%EC%95%94%EB%B3%B5%ED%98%B8%ED%99%94">개인정보 암복호화</a>
 * <a href="https://toss.im/tosscert/docs/guides/common#%EC%84%B8%EC%85%98-%ED%82%A4-%EC%83%9D%EC%84%B1-%EB%B0%8F-%EC%95%94%ED%98%B8%ED%99%94-sdk-%EC%82%AC%EC%9A%A9-%EC%98%88%EC%A0%9Cgcm">세션키 생성 방법</a>
 *
 * @param triggerType
 * @param userName 고객의 이름
 * @param userPhone 고객의 휴대폰번호로 문자 포함없이 순수한 숫자형태로 전달 필요
 * @param userBirthday 고객의 생년월일 8자리 YYYYMMDD형식
 * @param sessionKey API에서 사용자의 개인정보 전달이 필요한 경우
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class TossCertUserPersonalAuthRequestForm @JvmOverloads constructor(
    val triggerType: TriggerType,
    val userName: String,
    val userPhone: String,
    val userBirthday: String,
    val sessionKey: String,

    override val successCallbackUrl: String? = null,
    override val failCallbackUrl: String? = null,
    override val nonce: String? = null,
    override val expireSeconds: Int? = null
) : AuthRequestForm {
    override val requestType: RequestType = RequestType.USER_PERSONAL
}
