package herbaccara.toss.cert.model

import com.fasterxml.jackson.annotation.JsonCreator

data class Error(
    val errorType: Int,
    val errorCode: ErrorCode,
    val reason: String? = null,
    val data: Map<String, Any>? = null,
    val title: String? = null
) {

    /**
     * [에러코드](https://toss.im/tosscert/docs/guides/errorcode#%EB%B3%B8%EC%9D%B8%EC%9D%B8%EC%A6%9D-%EB%B0%8F-%EC%A0%84%EC%9E%90%EC%84%9C%EB%AA%85-%EC%97%90%EB%9F%AC%EC%BD%94%EB%93%9C)
     */
    enum class ErrorCode {
        UNKNOWN, CE0001, CE0002, CE0003, CE1000, CE1001, CE3000, CE3001, CE3002, CE3003, CE3100, CE3101, CE3102, CE3103, CE3200, CE5000;

        companion object {
            /***
             * 정의 되지 않은 타입의 "문자열" 값이 들어온 경우 UNKNOWN 로 맵핑 처리
             */
            @JsonCreator
            fun of(s: String): ErrorCode {
                return values().firstOrNull { it.name == s } ?: UNKNOWN
            }
        }
    }
}
