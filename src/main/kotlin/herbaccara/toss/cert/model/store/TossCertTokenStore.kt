package herbaccara.toss.cert.model.store

import herbaccara.toss.cert.model.TossCertToken

interface TossCertTokenStore {

    fun save(token: TossCertToken)

    fun load(): TossCertToken?
}
