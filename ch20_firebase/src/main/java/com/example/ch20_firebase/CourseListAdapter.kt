package com.example.ch20_firebase

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.ch20_firebase.databinding.CourseListItemBinding

/*
inner class ViewHolder를 이용하여 list_item에 있는 아이템과 연결해줍니다.

onCreateViewHolder는 RecyclerView의 아이템으로 만들어 두었던 list_item.xml을
LayoutInflater하여 뷰의 형태로 변환시켜 줍니다.

onBindViewHolder는 데이터를 바인딩하여 뷰에 뿌려질 수 있게 뿌려질 데이터를 연결해줍니다.

getItemCount는 화면에 가져올 아이템의 개수를 말하는 것으로 바인딩될 userList.size(개수)를 뜻합니다.

setListData는 후에 observer 패턴을 사용하기 위해 따로 만든 클래스
 */

class CourseListAdapter(var userList: ArrayList<CourseListType>): RecyclerView.Adapter<CourseListAdapter.ViewHolder>(),
    Filterable {
        var filteredMajor: ArrayList<CourseListType> = arrayListOf()
        var itemFilter = ItemFilter()

        init {
            filteredMajor.addAll(userList)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseListAdapter.ViewHolder {
        val view = CourseListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseListAdapter.ViewHolder, position: Int) {
        //val user : User = userList[position]
        holder.bind(position)
    }
    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position:Int)
        fun onDelete(v: View, position: Int)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener

    override fun getItemCount(): Int {
        return filteredMajor.size
    }

    override fun getFilter(): Filter {
        return itemFilter
    }

    inner class ViewHolder(val binding: CourseListItemBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(pos:Int){
            var filteredmajor: CourseListType = filteredMajor[pos]
            binding.name.text = filteredmajor.Course_NAME
            binding.point.text = filteredmajor.Course_POINT.toString()
            binding.deep.text = filteredmajor.deep
            binding.region.text = filteredmajor.division
            binding.specNum.text = filteredmajor.spec_num
            binding.regcheckbox.setOnClickListener {
                itemClickListener.onClick(binding.checkbox, pos)
            }
            binding.checkbox.setOnClickListener {
                itemClickListener.onDelete(binding.regcheckbox, pos)
            }
        }

    }

    inner class ItemFilter : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val filterString = charSequence.toString()
            val results = FilterResults()
            //검색이 필요없을 경우를 위해 원본 배열을 복제
            val filteredList: ArrayList<CourseListType> = ArrayList<CourseListType>()
            //공백제외 아무런 값이 없을 경우 -> 원본 배열
            if (filterString.trim { it <= ' ' }.isEmpty()) {
                results.values = userList
                results.count = userList.size
                return results

            }else {
                for (filteredmajor in userList) {
                    if (filteredmajor.Course_NAME?.contains(filterString) == true
                        || filteredmajor.division?.contains(filterString) == true
                        || filteredmajor.deep?.contains(filterString) == true) {
                        filteredList.add(filteredmajor)
                    }
                }
            }
            results.values = filteredList
            results.count = filteredList.size

            return results
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
            filteredMajor.clear()
            filteredMajor.addAll(filterResults.values as ArrayList<CourseListType>)
            notifyDataSetChanged()
        }
    }
}