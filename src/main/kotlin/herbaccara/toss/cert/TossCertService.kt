package herbaccara.toss.cert

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import herbaccara.boot.autoconfigure.toss.cert.TossCertProperties
import herbaccara.toss.cert.form.AuthRequest
import herbaccara.toss.cert.model.AuthRequestSuccess
import herbaccara.toss.cert.model.AuthResultSuccess
import herbaccara.toss.cert.model.AuthStatusSuccess
import herbaccara.toss.cert.model.TossCertToken
import herbaccara.toss.cert.model.store.TossCertTokenStore
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

    /***
     * Access Token 발급. 토큰의 수명은 3600초 (1시간).
     * 이미 유효한 토큰을 가지고 있는 상태에서 새로운 토큰 발급 API 를 반복 호출 x
     */
    fun token(): TossCertToken {
        val uri = "https://oauth2.cert.toss.im/token"

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
        }

        val form = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", "client_credentials")
            add("client_id", properties.clientId)
            add("client_secret", properties.clientSecret)
            add("scope", "ca")
        }

        val httpEntity = HttpEntity<MultiValueMap<String, String>>(form, headers)

        return restTemplate.postForObject<TossCertToken>(uri, httpEntity).also { token ->
            tokenStore.save(token)
        }
    }

    private inline fun <R, reified T> postForObject(uri: String, accessToken: String, body: R): T {
        return try {
            val headers = HttpHeaders().apply {
                contentType = MediaType.APPLICATION_JSON
                setBearerAuth(accessToken)
            }

            val httpEntity: HttpEntity<R> = HttpEntity<R>(body, headers)

            val response: JsonNode =
                restTemplate.exchange(uri, HttpMethod.POST, httpEntity, JsonNode::class.java).body!!

            val success = response["success"]
            objectMapper.readValue(success.toString(), T::class.java)
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }
    }

    /***
     * 본인인증 요청
     */
    fun authRequest(accessToken: String, authRequest: AuthRequest): AuthRequestSuccess {
        val uri = "https://cert.toss.im/api/v2/sign/user/auth/request"

        return postForObject(uri, accessToken, authRequest)
    }

    /***
     * 본인인증 상태조회
     */
    fun authStatus(accessToken: String, txId: String): AuthStatusSuccess {
        val uri = "https://cert.toss.im/api/v2/sign/user/auth/status"
        val form = mapOf("txId" to txId)

        return postForObject(uri, accessToken, form)
    }

    /***
     * 본인인증 결과조회. 결과조회 API는 성공 기준으로 최대 2회까지 조회가 가능
     */
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
     * 401 에러인 경우 token 재발급 처리 (1회)
     */
    private fun <T> recoverAuth(block: (String) -> T): T {
        // token 없을 시 UUID 랜덤으로 생성 처리. 의도된 잘못된 토큰 정보이다.
        // token validate api 가 따로 없어서 401 에러를 어떻게든 볼 수 밖에 없다.
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

    fun authRequest(authRequest: AuthRequest): AuthRequestSuccess {
        return recoverAuth { accessToken -> authRequest(accessToken, authRequest) }
    }

    fun authStatus(txId: String): AuthStatusSuccess {
        return recoverAuth { accessToken -> authStatus(accessToken, txId) }
    }

    fun authResult(txId: String): AuthResultSuccess {
        return recoverAuth { accessToken -> authResult(accessToken, txId) }
    }
}
