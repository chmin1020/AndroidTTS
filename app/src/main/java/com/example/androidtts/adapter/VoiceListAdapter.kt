package com.example.androidtts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidtts.R
import com.example.androidtts.databinding.VoiceListItemBinding
import java.io.File

/**
 * 음성 파일 리스트를 표현할 리사이클러뷰의 어댑터.
 * 모델로부터 음성 파일들을 리스트로 받아와서 실제 뷰와 연결한다.
 * 리스트를 위한 적절한 커스텀 뷰홀더는 내부 클래스로 설계되어 있다.
 * 추가적으로 클릭 관련 인터페이스를 가져서 각 뷰홀더 터치에 이벤트를 적용할 수 있도록 했다.
 */
class VoiceListAdapter(private val fileList: ArrayList<File>)
    : RecyclerView.Adapter<VoiceListAdapter.VoiceViewHolder>() {

    //--------------------------------------------
    // 아이템 뷰 이벤트 인터페이스 (아이템이 가리키는 음성 파일 실행 & 삭제)
    //

    //이 어댑터가 담당하는 뷰홀더 속 각 뷰에 대한 클릭 이벤트 처리 인터페이스
    interface OnEachViewClickListener{
        fun onPlayClick(file: File)
        fun onDeleteClick(file: File)
    }

    //각 어댑터가 가질 위 인터페이스의 인스턴스
    private lateinit var eachViewClickListener : OnEachViewClickListener

    /* 외부(Activity)에서 인터페이스의 내용을 설정하여 주입하도록 하는 함수 */
    fun setEachViewClickListener(eachViewClickListener: OnEachViewClickListener) {
        this.eachViewClickListener = eachViewClickListener
    }


    //--------------------------------------------
    // 리사이클러뷰 어댑터 오버라이딩 함수
    //

    /* 각 음성 파일을 나타낼 뷰홀더를 만든다. 사용하는 레이아웃은 voice_list_item */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoiceViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.voice_list_item, parent, false)
        return VoiceViewHolder(VoiceListItemBinding.bind(itemView))
    }

    /* 생성된, 혹은 재활용하는 뷰홀더의 내용 및 이벤트 리스너를 지정한다. */
    override fun onBindViewHolder(holder: VoiceViewHolder, position: Int) {
        //각 뷰홀더가 가리킬 음성 파일
        val file = fileList[position]

        //각 뷰홀더 속 플레이 이미지뷰 누를 시 진행되는 이벤트 세팅 (for playImage)
        val playListener = View.OnClickListener {
            eachViewClickListener.onPlayClick(file)
        }

        //각 뷰홀더 속 삭제 이미지뷰 누를 시 진행되는 이벤트 세팅 (for deleteImage)
        val deleteListener = View.OnClickListener {
            eachViewClickListener.onDeleteClick(file)

            //추가적으로 어댑터가 가진 리스트도 갱신하고 이를 notify 시킴
            fileList.remove(file)
            notifyItemRemoved(position)
        }

        //각 홀더 인스턴스에 설정할 내용 전달
        holder.bind(file.name, playListener, deleteListener)
    }

    /* 이 어댑터에서 관리하는 아이템의 개수를 반환한다. */
    override fun getItemCount(): Int {
        return fileList.size
    }


    //--------------------------------------------
    // 커스텀 뷰홀더 클래스
    //

    /* 이 리사이클러뷰에서 사용할 커스텀 뷰홀더 클래스.
     파일 제목을 텍스트로 나타내고, 재생 버튼을 누르면 저장된 음성이 실행된다. */
    class VoiceViewHolder(private val binder: VoiceListItemBinding): RecyclerView.ViewHolder(binder.root){

        /* 가져온 item, listener 객체를 통해 뷰홀더 세부 내용을 설정한다. */
        fun bind(title: String, playListener: View.OnClickListener, deleteListener: View.OnClickListener ){
            binder.tvTitle.text = title //파일 제목 텍스트 설정
            binder.ivPlay.setOnClickListener(playListener) //재생 버튼 클릭 이벤트 설정
            binder.ivDelete.setOnClickListener(deleteListener) //삭제 버튼 클릭 이벤트 설정
        }
    }
}