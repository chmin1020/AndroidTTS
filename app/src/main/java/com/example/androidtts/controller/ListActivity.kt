package com.example.androidtts.controller

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidtts.adapter.VoiceListAdapter
import com.example.androidtts.databinding.ActivityListBinding
import com.example.androidtts.model.fileManager.FileManagerProvider
import com.example.androidtts.model.fileManager.ITTSFileManager
import java.io.File

class ListActivity : AppCompatActivity() {

    //-------------------------------------------
    // 인스턴스 영역
    //

    //음성 파일 관리를 위한 model instance (singleton 사용을 위해 Handler 이용해서 가져온다.)
    private val ttsFileManager: ITTSFileManager by lazy{ FileManagerProvider.getInstanceForAOS(applicationContext.filesDir)}

    //뷰 바인딩 instance
    private val binder: ActivityListBinding by lazy { ActivityListBinding.inflate(layoutInflater) }

    //-------------------------------------------
    // 생명 주기
    //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //뷰와 연결
        setContentView(binder.root)

        //음성파일 리스트(recyclerView)를 위한 레이아웃 매니저와 어댑터 설정
        binder.voiceList.layoutManager = LinearLayoutManager(this)
        binder.voiceList.adapter = makeAdapterForRecyclerView()
    }

    //-------------------------------------------
    // 내부 함수 (리스트를 위한 어댑터 설정)
    //

    /* 음성 파일 리스트 어댑터 설정을 위한 함수
        - 파일 리스트를 불러와서 연결하고, 리스트 내 아이템을 위한 이벤트 리스너를 지정한다. */
    private fun makeAdapterForRecyclerView(): VoiceListAdapter {
        //리사이클러뷰에 표시할 파일 리스트 불러와서 저장
        val fileList = arrayListOf<File>()
        ttsFileManager.getFileList()?.also { fileList.addAll(it) }

        //파일 리스트를 어댑터에 적용
        val voiceListAdapter = VoiceListAdapter(fileList)

        //리사이클러뷰 속 각 아이템의 클릭 이벤트 처리
        voiceListAdapter.setEachViewClickListener(object: VoiceListAdapter.OnEachViewClickListener{
            //각 아이템 뷰의 실행 이미지를 누를 경우의 이벤트 (파일에 저장된 음성 실행)
            override fun onPlayClick(file: File) {
                MediaPlayer.create(this@ListActivity, file.toUri()).start()
            }

            //각 아이템 뷰의 삭제 이미지를 누를 경우의 이벤트 (파일을 삭제하고 토스트 메시지 표시)
            override fun onDeleteClick(file: File) {
                ttsFileManager.removeFile(file)
                Toast.makeText(this@ListActivity, "파일이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }
        })

        return voiceListAdapter //설정 완료된 어댑터 반환
    }
}