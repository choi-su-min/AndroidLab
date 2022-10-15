package com.example.ch20_firebase

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/*
inner class ViewHolder를 이용하여 list_item에 있는 아이템과 연결해줍니다.

onCreateViewHolder는 RecyclerView의 아이템으로 만들어 두었던 list_item.xml을
LayoutInflater하여 뷰의 형태로 변환시켜 줍니다.

onBindViewHolder는 데이터를 바인딩하여 뷰에 뿌려질 수 있게 뿌려질 데이터를 연결해줍니다.

getItemCount는 화면에 가져올 아이템의 개수를 말하는 것으로 바인딩될 userList.size(개수)를 뜻합니다.

setListData는 후에 observer 패턴을 사용하기 위해 따로 만든 클래스
 */

class ListAdapter(val context: Context): RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    var userList = mutableListOf<User>()

    fun setListData(data:MutableList<User>){
        userList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item,parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
        //val user : User = userList[position]
        holder.Course_Name.text = userList[position].Course_NAME
        holder.division.text = userList[position].division
        holder.Course_Point.text = userList[position].Course_POINT.toString()
        holder.checked.text = userList[position].checked
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it,position)
        }
        holder.checkbox.setOnClickListener{
            itemClickListener.onDelete(it,position)
        }
    }
    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
        fun onDelete(v: View, position: Int)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val division : TextView = itemView.findViewById(R.id.region)
        val Course_Name : TextView = itemView.findViewById(R.id.name)
        val Course_Point :TextView = itemView.findViewById(R.id.point)
        val checked:TextView = itemView.findViewById(R.id.checked)
        val checkbox:TextView = itemView.findViewById(R.id.checkbox)
    }


}