package herbaccara.toss.cert.model

import com.fasterxml.jackson.annotation.JsonProperty

data class TossCertToken(
    @field:JsonProperty("access_token")
    val accessToken: String,

    val scope: String,

    @field:JsonProperty("token_type")
    private val tokenType: String,

    /**
     * Access Token 만료 시간(초 단위)
     */
    @field:JsonProperty("expires_in")
    private val expiresIn: Long
)
