package herbaccara.toss.cert.form

/***
 * [봉인인증 요청 API](https://toss.im/tosscert/docs/guides/integration/user#2-%EB%B3%B8%EC%9D%B8%EC%9D%B8%EC%A6%9D-%EC%9A%94%EC%B2%AD-api)
 */
interface AuthRequest {

    val requestType: RequestType
}
