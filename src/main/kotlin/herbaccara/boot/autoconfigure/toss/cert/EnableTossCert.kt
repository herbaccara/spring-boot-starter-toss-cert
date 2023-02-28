package herbaccara.boot.autoconfigure.toss.cert

import org.springframework.context.annotation.Import
import java.lang.annotation.*

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Import(TossCertAutoConfiguration::class)
annotation class EnableTossCert
