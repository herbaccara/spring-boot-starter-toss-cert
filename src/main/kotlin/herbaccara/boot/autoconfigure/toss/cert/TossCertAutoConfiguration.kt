package herbaccara.boot.autoconfigure.toss.cert

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import herbaccara.toss.cert.TossCertService
import herbaccara.toss.cert.store.TossCertInMemoryTokenStore
import herbaccara.toss.cert.store.TossCertTokenStore
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter
import org.springframework.web.client.RestTemplate
import java.nio.charset.StandardCharsets
import java.util.*

@AutoConfiguration
@EnableConfigurationProperties(TossCertProperties::class)
@ConditionalOnProperty(prefix = "toss.cert", value = ["enabled"], havingValue = "true")
class TossCertAutoConfiguration {

    @Bean("tossCertObjectMapper")
    fun objectMapper(properties: TossCertProperties): ObjectMapper {
        return jacksonObjectMapper().apply {
            findAndRegisterModules()
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, properties.failOnUnknownProperties)
        }
    }

    @Bean("tossCertRestTemplate")
    fun restTemplate(
        @Qualifier("tossCertObjectMapper") objectMapper: ObjectMapper,
        customizers: List<TossCertRestTemplateBuilderCustomizer>,
        interceptors: List<TossCertClientHttpRequestInterceptor>
    ): RestTemplate {
        return RestTemplateBuilder()
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
    }

    @Bean
    @ConditionalOnMissingBean(TossCertTokenStore::class)
    fun tossCertTokenStore(): TossCertTokenStore {
        return TossCertInMemoryTokenStore()
    }

    @Bean
    fun tossCertService(
        @Qualifier("tossCertRestTemplate") restTemplate: RestTemplate,
        @Qualifier("tossCertObjectMapper") objectMapper: ObjectMapper,
        properties: TossCertProperties,
        tokenStore: TossCertTokenStore
    ): TossCertService {
        if (properties.clientId.isEmpty()) throw NullPointerException()
        if (properties.clientSecret.isEmpty()) throw NullPointerException()

        return TossCertService(restTemplate, objectMapper, properties, tokenStore)
    }
}
