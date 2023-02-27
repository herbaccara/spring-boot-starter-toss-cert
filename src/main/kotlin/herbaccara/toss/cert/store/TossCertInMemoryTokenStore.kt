package herbaccara.toss.cert.store

import herbaccara.toss.cert.model.TossCertToken
import java.util.concurrent.atomic.AtomicReference

/***
 * 분산 환경에서는 사용 금지
 */
class TossCertInMemoryTokenStore : TossCertTokenStore {

    private val reference: AtomicReference<TossCertToken> = AtomicReference<TossCertToken>()

    override fun save(token: TossCertToken) {
        reference.set(token)
    }

    override fun load(): TossCertToken? {
        return reference.get()
    }
}
