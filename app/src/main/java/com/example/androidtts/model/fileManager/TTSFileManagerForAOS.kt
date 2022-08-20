package com.example.androidtts.model.fileManager

import java.io.File

class TTSFileManagerForAOS(private val filePath: File): ITTSFileManager {

    /* 현재 저장된 음성 파일을 리스트로 전달하는 함수 */
    override fun getFileList(): List<File>? {
        //mp3 확장자만 리스트에 담아서 반환
        return filePath.listFiles()?.filter{ it.extension == "mp3" }
    }

    /* 새로운 음성 파일을 추가하고자 할 때 호출하는 함수 */
    override fun makeFile(newFileName: String): File {
        return File(filePath, newFileName)
    }

    /* 기존 음성 파일 중 하나를 삭제하고자 할 때 호출하는 함수 */
    override fun removeFile(removedFile: File) {
        removedFile.delete()
    }
}