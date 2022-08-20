package com.example.androidtts.model.fileManager

import java.io.File

/**
 * FileManager 모델이 해야하는 역할을 규정하는 인터페이스.
 * 각 환경에 따라 다른 구체화된 객체들이 이 인터페이스를 구현해서 사용한다.
 */
interface ITTSFileManager {
    /* 현재 저장된 음성 파일을 리스트로 전달하는 함수 */
    fun getFileList(): List<File>?

    /* 새로운 음성 파일을 추가하고자 할 때 호출하는 함수 */
    fun makeFile(newFileName: String): File

    /* 기존 음성 파일 중 하나를 삭제하고자 할 때 호출하는 함수 */
    fun removeFile(removedFile: File)
}