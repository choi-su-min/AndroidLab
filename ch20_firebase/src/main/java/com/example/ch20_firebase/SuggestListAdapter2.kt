package com.example.ch20_firebase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ch20_firebase.databinding.UserCourseItemBinding

/*
inner class ViewHolder를 이용하여 list_item에 있는 아이템과 연결해줍니다.

onCreateViewHolder는 RecyclerView의 아이템으로 만들어 두었던 list_item.xml을
LayoutInflater하여 뷰의 형태로 변환시켜 줍니다.

onBindViewHolder는 데이터를 바인딩하여 뷰에 뿌려질 수 있게 뿌려질 데이터를 연결해줍니다.

getItemCount는 화면에 가져올 아이템의 개수를 말하는 것으로 바인딩될 userList.size(개수)를 뜻합니다.

setListData는 후에 observer 패턴을 사용하기 위해 따로 만든 클래스
 */

class SuggestListAdapter2(private val valuelist: ArrayList<CourseListType>): RecyclerView.Adapter<SuggestListAdapter2.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = UserCourseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return valuelist.size
    }

    inner class ViewHolder(val binding: UserCourseItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(pos:Int){
            binding.name.text = valuelist[pos].Course_NAME
            binding.point.text = valuelist[pos].Course_POINT.toString()
            binding.deep.text = valuelist[pos].deep
            binding.region.text = valuelist[pos].division
            binding.specNum.text = valuelist[pos].spec_num
        }
    }
}
