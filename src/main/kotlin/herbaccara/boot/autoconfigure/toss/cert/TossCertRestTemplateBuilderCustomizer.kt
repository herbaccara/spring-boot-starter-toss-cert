package herbaccara.boot.autoconfigure.toss.cert

import org.springframework.boot.web.client.RestTemplateBuilder

interface TossCertRestTemplateBuilderCustomizer {

    fun customize(restTemplateBuilder: RestTemplateBuilder)
}
