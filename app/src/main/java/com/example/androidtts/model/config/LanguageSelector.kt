package com.example.androidtts.model.config

import java.util.*

/**
 * tts 객체의 언어 선택을 담당하는 선택자 객체
 * 사용자가 고른 언어를 Locale 형식으로 바꿔서 전달해준다.
 **/
class LanguageSelector {

    //단어로 쓰인 언어 이름들을 설정에 적합한 Locale 값과 매칭하는 표
    private companion object MatchingTable{
        private val languageTable = mapOf<String, Locale>(
            Pair("한국어", Locale.KOREAN),
            Pair("영어", Locale.ENGLISH)
        )
    }

    //language 시작 값
    val initLanguage: String = "한국어"

    /* 선택자 객체들이 공유하는 표에 따라 변환된 언어가 무엇인지 찾아 반환하는 함수 */
    fun selectNewLanguage(str: String): Locale{
        languageTable[str].also {
            //세팅 상 그럴 수 없지만, 만약 null 나오면 시작 언어로 설정
            if(it == null)
                return languageTable[initLanguage]!!
            return it
        }
    }
}