package herbaccara.toss.cert

import herbaccara.boot.autoconfigure.toss.cert.TossCertAutoConfiguration
import herbaccara.toss.cert.form.TossCertUserNoneAuthRequestForm
import herbaccara.toss.cert.form.TossCertUserPersonalAuthRequestForm
import herbaccara.toss.cert.form.TriggerType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.web.client.HttpClientErrorException.BadRequest

@SpringBootTest(
    classes = [
        TossCertAutoConfiguration::class
    ]
)
@TestPropertySource(locations = ["classpath:application.yml"])
class TossCertServiceTest {

    @Autowired
    lateinit var tossCertService: TossCertService

    @Test
    fun token() {
        val token = tossCertService.token()
        println(token)
        assertNotNull(token.accessToken)
    }

    @Test
    fun authRequest1() {
        val token = tossCertService.token()
        val response = tossCertService.authRequest(token.accessToken, TossCertUserNoneAuthRequestForm)
        println(response)
        assertNotNull(response)
    }

    @Test
    fun authRequest2() {
        val token = tossCertService.token()
        val form = TossCertUserPersonalAuthRequestForm(
            TriggerType.PUSH,
            "v1\$c9c5be61-2309-4a54-af02-b12b43de3a51\$tp5EQzbFbKSWx3DAimLAzVOIb7hofTh/Sw==",
            "v1\$c9c5be61-2309-4a54-af02-b12b43de3a51\$bBb0nIhutSUqmy2549+c2pOQ+CGkiarK7539",
            "v1\$c9c5be61-2309-4a54-af02-b12b43de3a51\$bR79nI5styn0Kd4cr2KaeCvbOxmME44y",
            "v1\$c9c5be61-2309-4a54-af02-b12b43de3a51\$P23BeUpZWxV4S3yZwvwawd5biZsusLVjYa2N8t1aMXKovD4GivY7GnLaKWFBwY0yDoB9a8GGascbLoPE6Z/H7fUlUMcoGVD71UhQj56azchKPAtsiarTJO4sICL7aRmqOQMNWvapKRf9wgvXzEJ45xA2g4IFhmTTTKBr0qbEJhkAPf5bRZMNL9pgH9c4KL97qQSpZwfg4PnP/DB7QmG/ckEwl2OouGjNFlyUSqtmSAy0XGoibUUu6wNuf14HTRb5cWcezNeFuyuU6xNW6KpoAHYqHrIc5Ud1wCasokXIWLIz+EAQxdWWoXDUToC6HsDeVLDQJ0zdLmbFNhCaPhIEXe+lLRNMBXqTslJ0unj3alU/BJWdW81EX6J5CsDE/ng7rS/bram7K6KJ8V4OGOF7nNgHEwcMnzptOhBCOmG6+CNv4COL3Sg15mkFhdTwWa8H5gPl0SDZHm5fai/QBizZJhLQhpdiJ04w67lcPlUOJLLYTSVVcqaaaIKHnIzyWKcJ72NaJP8ZOGTs/EXT9zPwWFG57Z4E5wLNjSUG4SRlJ43X0otzYWz0plIWptkLhFW1qsHMWLp5stlqCeG2LLDqG7LJszwxwEhVEHVYo2ZUQnE7D/tiiV37b9v8EWNODNN25d6C5QypOcOF78isz0YY3Ze+RIMjY6j5Swu/eEumcdo="
        )
        val response = tossCertService.authRequest(token.accessToken, form)
        println(response)
        assertNotNull(response)
    }

    @Test
    fun authRequest3() {
        val token = tossCertService.token()
        val authRequest3 = TossCertUserPersonalAuthRequestForm(
            TriggerType.APP_SCHEME,
            "v1\$c9c5be61-2309-4a54-af02-b12b43de3a51\$tp5EQzbFbKSWx3DAimLAzVOIb7hofTh/Sw==",
            "v1\$c9c5be61-2309-4a54-af02-b12b43de3a51\$bBb0nIhutSUqmy2549+c2pOQ+CGkiarK7539",
            "v1\$c9c5be61-2309-4a54-af02-b12b43de3a51\$bR79nI5styn0Kd4cr2KaeCvbOxmME44y",
            "v1\$c9c5be61-2309-4a54-af02-b12b43de3a51\$P23BeUpZWxV4S3yZwvwawd5biZsusLVjYa2N8t1aMXKovD4GivY7GnLaKWFBwY0yDoB9a8GGascbLoPE6Z/H7fUlUMcoGVD71UhQj56azchKPAtsiarTJO4sICL7aRmqOQMNWvapKRf9wgvXzEJ45xA2g4IFhmTTTKBr0qbEJhkAPf5bRZMNL9pgH9c4KL97qQSpZwfg4PnP/DB7QmG/ckEwl2OouGjNFlyUSqtmSAy0XGoibUUu6wNuf14HTRb5cWcezNeFuyuU6xNW6KpoAHYqHrIc5Ud1wCasokXIWLIz+EAQxdWWoXDUToC6HsDeVLDQJ0zdLmbFNhCaPhIEXe+lLRNMBXqTslJ0unj3alU/BJWdW81EX6J5CsDE/ng7rS/bram7K6KJ8V4OGOF7nNgHEwcMnzptOhBCOmG6+CNv4COL3Sg15mkFhdTwWa8H5gPl0SDZHm5fai/QBizZJhLQhpdiJ04w67lcPlUOJLLYTSVVcqaaaIKHnIzyWKcJ72NaJP8ZOGTs/EXT9zPwWFG57Z4E5wLNjSUG4SRlJ43X0otzYWz0plIWptkLhFW1qsHMWLp5stlqCeG2LLDqG7LJszwxwEhVEHVYo2ZUQnE7D/tiiV37b9v8EWNODNN25d6C5QypOcOF78isz0YY3Ze+RIMjY6j5Swu/eEumcdo="
        )
        val response = tossCertService.authRequest(token.accessToken, authRequest3)
        println(response)
        assertNotNull(response)
    }

    @Test
    fun authResult() {
        val sessionKey = tossCertService.generateSessionKey()
        val token = tossCertService.token()
        val authRequest = tossCertService.authRequest(token.accessToken, TossCertUserNoneAuthRequestForm)

        val txId: String = authRequest.txId
        val authStatus = tossCertService.authStatus(token.accessToken, txId)

        assertThrows(BadRequest::class.java) {
            val authResult = tossCertService.authResult(token.accessToken, txId, sessionKey)
            println()
        }
    }
}
