package herbaccara.boot.autoconfigure.toss.cert

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "toss.cert")
@ConstructorBinding
data class TossCertProperties(
    val enabled: Boolean = true,
    val clientId: String,
    val clientSecret: String,
    val failOnUnknownProperties: Boolean = false
)
