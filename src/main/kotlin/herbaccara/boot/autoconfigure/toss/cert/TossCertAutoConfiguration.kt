package herbaccara.boot.autoconfigure.toss.cert

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import herbaccara.toss.cert.TossCertService
import herbaccara.toss.cert.store.TossCertInMemoryTokenStore
import herbaccara.toss.cert.store.TossCertTokenStore
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter
import java.nio.charset.StandardCharsets
import java.util.*

@AutoConfiguration
@EnableConfigurationProperties(TossCertProperties::class)
@ConditionalOnProperty(prefix = "toss.cert", value = ["enabled"], havingValue = "true")
class TossCertAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    fun objectMapper(): ObjectMapper {
        return jacksonObjectMapper().apply {
            findAndRegisterModules()
        }
    }

    @Bean
    @ConditionalOnMissingBean
    fun tossCertTokenStore(): TossCertTokenStore {
        return TossCertInMemoryTokenStore()
    }

    @Bean
    fun tossCertService(
        objectMapper: ObjectMapper,
        customizers: List<TossCertRestTemplateBuilderCustomizer>,
        interceptors: List<TossCertClientHttpRequestInterceptor>,
        properties: TossCertProperties,
        tokenStore: TossCertTokenStore
    ): TossCertService {
        if (properties.clientId.isEmpty()) throw NullPointerException()
        if (properties.clientSecret.isEmpty()) throw NullPointerException()

        val restTemplate = RestTemplateBuilder()
            .additionalInterceptors(*interceptors.toTypedArray())
            .messageConverters(
                StringHttpMessageConverter(StandardCharsets.UTF_8),
                AllEncompassingFormHttpMessageConverter(),
                MappingJackson2HttpMessageConverter(objectMapper)
            )
            .also { builder ->
                for (customizer in customizers) {
                    customizer.customize(builder)
                }
            }
            .build()

        return TossCertService(restTemplate, objectMapper, properties, tokenStore)
    }
}
