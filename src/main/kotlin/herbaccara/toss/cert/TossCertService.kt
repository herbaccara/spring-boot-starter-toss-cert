package herbaccara.toss.cert

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import herbaccara.boot.autoconfigure.toss.cert.TossCertProperties
import herbaccara.toss.cert.form.AuthRequestForm
import herbaccara.toss.cert.model.AuthRequestSuccess
import herbaccara.toss.cert.model.AuthResultSuccess
import herbaccara.toss.cert.model.AuthStatusSuccess
import herbaccara.toss.cert.model.TossCertToken
import herbaccara.toss.cert.store.TossCertTokenStore
import im.toss.cert.sdk.TossCertSession
import im.toss.cert.sdk.TossCertSessionGenerator
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.HttpClientErrorException.Unauthorized
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject
import java.util.*

class TossCertService(
    private val restTemplate: RestTemplate,
    private val objectMapper: ObjectMapper,
    private val properties: TossCertProperties,
    private val tokenStore: TossCertTokenStore
) {
    private val certSessionGenerator by lazy { TossCertSessionGenerator() }

    fun generateSession(): TossCertSession = certSessionGenerator.generate()

//    fun toError(throwable: Throwable): Optional<Error> {
//        return if (throwable is HttpStatusCodeException) {
//            Try.of {
//                val response: JsonNode =
//                    objectMapper.readValue<JsonNode>(
//                        throwable.getResponseBodyAsString(),
//                        JsonNode::class.java
//                    )
//                objectMapper.readValue(
//                    Objects.requireNonNull(
//                        response["error"]
//                    ).toString(), Error::class.java
//                )
//            }.toJavaOptional()
//        } else Optional.empty()
//    }

    fun token(): TossCertToken = token(properties.clientId, properties.clientSecret)

    /***
     * Access Token ??????. ????????? ????????? 3600??? (1??????).
     * ?????? ????????? ????????? ????????? ?????? ???????????? ????????? ?????? ?????? API ??? ?????? ?????? x
     */
    fun token(clientId: String, clientSecret: String): TossCertToken {
        val uri = "https://oauth2.cert.toss.im/token"

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
        }

        val form = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", "client_credentials")
            add("client_id", clientId)
            add("client_secret", clientSecret)
            add("scope", "ca")
        }

        val httpEntity = HttpEntity<MultiValueMap<String, String>>(form, headers)

        return restTemplate.postForObject<TossCertToken>(uri, httpEntity).also { token ->
            tokenStore.save(token)
        }
    }

    private inline fun <R, reified T> postForObject(uri: String, accessToken: String, body: R): T {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            setBearerAuth(accessToken)
        }

        val httpEntity: HttpEntity<R> = HttpEntity<R>(body, headers)

        val response: JsonNode =
            restTemplate.exchange(uri, HttpMethod.POST, httpEntity, JsonNode::class.java).body!!

        val success = response["success"]

        return objectMapper.readValue(success.toString(), T::class.java)
    }

    /***
     * ???????????? ??????
     */
    fun authRequest(accessToken: String, authRequestForm: AuthRequestForm): AuthRequestSuccess {
        val uri = "https://cert.toss.im/api/v2/sign/user/auth/request"

        return postForObject(uri, accessToken, authRequestForm)
    }

    /***
     * ???????????? ????????????
     */
    fun authStatus(accessToken: String, txId: String): AuthStatusSuccess {
        val uri = "https://cert.toss.im/api/v2/sign/user/auth/status"
        val form = mapOf("txId" to txId)

        return postForObject(uri, accessToken, form)
    }

    /***
     * ???????????? ????????????. ???????????? API??? ?????? ???????????? ?????? 2????????? ????????? ??????
     */
    @JvmOverloads
    fun authResult(
        accessToken: String,
        txId: String,
        sessionKey: String = generateSession().sessionKey
    ): AuthResultSuccess {
        val uri = "https://cert.toss.im/api/v2/sign/user/auth/result"
        val form = mapOf("txId" to txId, "sessionKey" to sessionKey)

        return postForObject(uri, accessToken, form)
    }

    /***
     * 401 ????????? ?????? token ????????? ?????? (1???)
     */
    private fun <T> recoverAuth(block: (String) -> T): T {
        // token ?????? ??? UUID ???????????? ?????? ??????. ????????? ????????? ?????? ????????????.
        // token validate api ??? ?????? ????????? 401 ????????? ???????????? ??? ??? ?????? ??????.
        val accessToken: () -> String = {
            tokenStore.load()?.accessToken ?: UUID.randomUUID().toString()
        }

        return runCatching { block.invoke(accessToken()) }
            .recoverCatching { exception ->
                if (exception is Unauthorized) {
                    block.invoke(token().accessToken)
                }
                throw exception
            }
            .getOrThrow()
    }

    fun authRequest(authRequestForm: AuthRequestForm): AuthRequestSuccess {
        return recoverAuth { accessToken -> authRequest(accessToken, authRequestForm) }
    }

    fun authStatus(txId: String): AuthStatusSuccess {
        return recoverAuth { accessToken -> authStatus(accessToken, txId) }
    }

    fun authResult(txId: String): AuthResultSuccess {
        return recoverAuth { accessToken -> authResult(accessToken, txId) }
    }
}
