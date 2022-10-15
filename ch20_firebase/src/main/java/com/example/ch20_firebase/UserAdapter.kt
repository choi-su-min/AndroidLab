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

open class UserAdapter(val context: Context): RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    val getlist = intent
    val mylist = mutableListOf<MyList>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item,parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        //val user : User = userList[position]
        holder.Course_Name.text = mylist[position].Course_NAME
        holder.division.text = mylist[position].division
        holder.Course_Point.text = mylist[position].Course_POINT.toString()
        holder.checked.text = mylist[position].checked
    }

    override fun getItemCount(): Int {
        return mylist.size
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val division : TextView = itemView.findViewById(R.id.uregion)
        val Course_Name : TextView = itemView.findViewById(R.id.uname)
        val Course_Point :TextView = itemView.findViewById(R.id.upoint)
        val checked:TextView = itemView.findViewById(R.id.uchecked)
    }


}

object intent {
    override fun toString(): String {
        return super.toString()
    }
}
