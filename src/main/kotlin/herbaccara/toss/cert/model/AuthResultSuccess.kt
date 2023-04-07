package herbaccara.toss.cert.model

import java.time.OffsetDateTime

data class AuthResultSuccess(
    /**
     * 결과를 조회한 인증 요청 트랜잭션 아이디
     */
    override val txId: String,

    /**
     * 결과 조회가 정상적으로 됐을 때 반환되는 상태 (COMPLETED 고정)
     */
    val status: Status,

    @Deprecated("현재 버전에서 미사용으로 null 고정")
    val userIdentifier: String? = null,

    @Deprecated("현재 버전에서 미사용으로 null 고정")
    val userCiToken: String? = null,

    /**
     * 사용자가 서명한 전자서명 정보
     * Base64인코딩된 DER값으로 사용자 식별을 위해 txId와 함께 반드시 저장 관리 해야 합니다.
     * [인증서 유효성 확인하는 방법 알아보기](https://toss.im/tosscert/docs/guides/validity)
     */
    val signature: String,

    @Deprecated("현재 버전에서 미사용으로 null 고정")
    val randomValue: String? = null,

    /**
     * 사용자가 인증을 끝마친 시간
     */
    val completedDt: OffsetDateTime,

    /**
     * 최초 인증 요청된 시각
     */
    val requestedDt: OffsetDateTime,

    /**
     * 인증을 진행한 사용자의 개인정보입니다.
     */
    val personalData: PersonalData
) : AuthSuccess {

    data class PersonalData(
        /**
         * 암호화된 사용자의 ci
         */
        val ci: String,

        /**
         * 암호화된 사용자의 이름
         */
        val name: String,

        /**
         * 암호화된 사용자의 휴대폰번호
         */
        val phone: String,

        /**
         * 암호화된 사용자의 생년월일 8자리
         */
        val birthday: String,

        /**
         * 암호화된 사용자의 성별정보로 MALE 또는 FEMALE
         */
        val gender: String,

        /**
         * 암호화된 사용자의 국적정보로 LOCAL 또는 FOREIGNER
         */
        val nationality: String,

        @Deprecated("예측할 수 없는 상황에서 ci 유출 대응을 위한 임시 파리미터로 null 고정")
        private val ci2: String? = null,

        /**
         * 암호화된 사용자의 di
         */
        private val di: String,

        @Deprecated("예측할 수 없는 상황에서 ci 유출 대응을 위한 임시 파리미터로 null 고정")
        private val ciUpdate: String? = null,

        /**
         * 토스 앱 ‘내 정보‘에 등록된 개인정보 중 주소를 제공합니다.
         * 내 정보는 사용자가 직접 토스 앱에서 등록하는 정보이며 없으면 null 값으로 전달하고 있으면 암호화된 값으로 전달합니다.
         * 이 정보를 활용할 때 유의할 점은 사용자의 최신 정보를 보장할 수 있는 개인정보가 아닙니다.
         */
        private val address: String? = null,

        /**
         * 토스 앱 ‘내 정보‘에 등록된 개인정보 중 주소를 제공합니다. 상세한 집주소 입니다.
         */
        private val addressDetails: String? = null,

        /**
         * 토스 앱 ‘내 정보‘에 등록된 개인정보 중 집주소 우편번호를 제공합니다.
         */
        private val zipCode: String? = null,

        /***
         * 토스 앱 ‘내 정보’에 등록된 개인정보 중 이메일 주소를 제공합니다.
         */
        private val email: String? = null
    ) {
        enum class Gender {
            MALE, FEMALE
        }

        enum class Nationality {
            LOCAL, FOREIGNER
        }
    }
}
