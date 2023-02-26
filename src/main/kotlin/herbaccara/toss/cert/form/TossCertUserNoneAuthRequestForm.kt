package herbaccara.toss.cert.form

/***
 * Case1. 표준창을 이용해서 인증 요청하기
 */
object TossCertUserNoneAuthRequestForm : AuthRequest {
    override val requestType: RequestType = RequestType.USER_NONE
}
