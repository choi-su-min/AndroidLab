package com.example.ch20_firebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ch20_firebase.databinding.CourseNameBinding

/*
inner class ViewHolder를 이용하여 list_item에 있는 아이템과 연결해줍니다.

onCreateViewHolder는 RecyclerView의 아이템으로 만들어 두었던 list_item.xml을
LayoutInflater하여 뷰의 형태로 변환시켜 줍니다.

onBindViewHolder는 데이터를 바인딩하여 뷰에 뿌려질 수 있게 뿌려질 데이터를 연결해줍니다.

getItemCount는 화면에 가져올 아이템의 개수를 말하는 것으로 바인딩될 userList.size(개수)를 뜻합니다.

setListData는 후에 observer 패턴을 사용하기 위해 따로 만든 클래스
 */

class SuggestListAdapter(private val valuelist: List<ArrayList<CourseListType>>, private val keylist:List<String>): RecyclerView.Adapter<SuggestListAdapter.MyView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        val view = CourseNameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyView(view)
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return keylist.size
    }

    inner class MyView(private val binding: CourseNameBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.layout01.setOnClickListener {
                if (binding.recycler02.visibility == View.VISIBLE) {
                    binding.recycler02.visibility = View.GONE
                    binding.layoutBtn01.animate().apply {
                        duration = 200
                        rotation(0f)
                    }
                } else {
                    binding.recycler02.visibility = View.VISIBLE
                    binding.layoutBtn01.animate().apply {
                        duration = 200
                        rotation(180f)
                    }
                }
            }
        }

        val recyclerview = binding.recycler02

        fun bind(pos: Int) {
            binding.textView01.text = keylist[pos]
            recyclerview.setLayoutManager(LinearLayoutManager(binding.recycler02.context, LinearLayoutManager.VERTICAL, false))
            val adapter = SuggestListAdapter2(valuelist[pos])
            recyclerview.layoutManager = LinearLayoutManager(binding.recycler02.context)
            recyclerview.adapter = adapter
        }
    }

}