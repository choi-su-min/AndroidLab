package com.example.ch20_firebase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ch20_firebase.databinding.FragmentOneBinding
import com.google.firebase.database.FirebaseDatabase

/** *사용자가 이수한 학점을 계산하여 출력
 * 프래그먼트를 상속받는 OneFragment
 * 파이어베이스 실시간데이터베이스에 등록된 사용자의 과목 정보를 가져옴
 * 해당 과목들의 학점을 계산 및 출력
*/
class OneFragment : Fragment() {
    /**OnFragment에 해당하는 xml파일을 화면에 뿌리기위한 View를 만들어줌.*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentOneBinding.inflate(inflater, container, false)
        val userNum = arguments?.getString("usernum")

        val studentMajorList: ArrayList<CourseListType> = arrayListOf() //사용자가 등록한 전공 리스트를 담을 변수 선언
        val studentLiberalList: ArrayList<CourseListType> = arrayListOf() //사용자가 등록한 교양 리스트를 담을 변수 선언
        val realtimeDB: FirebaseDatabase = FirebaseDatabase.getInstance() //파이어베이스 인스턴스 초기화
        val majorRef = realtimeDB.reference.child(userNum.toString()).child("전공") //파이어베이스에서 사용자 등록 전공과목 참조
        val liberalRef = realtimeDB.reference.child(userNum.toString()).child("교양") //파이어베이스에서 사용자 등록 교양과목 참조

        //사용자가 등록한 전공과목 DB참조
        majorRef.get().addOnSuccessListener {
            var ma1 = 0 //전필
            var ma2 = 0 //전선
            var matot = 0 //전필+전선

            var la1 = 0 //역량교양
            var la2 = 0 //통합교양
            var la3 = 0 //개척교양
            var la4 = 0 //기초과정
            var latot = 0 //역량+통합+개척+기초

            //사용자가 등록한 전공과목 정보 불러오기
            for (courseSnapshot in it.children){
                var getData = courseSnapshot.getValue(CourseListType::class.java)
                studentMajorList.add(getData!!)
            }

            //전필 전선 학점을 각각 계산
            for (data in studentMajorList){
                when(data.division){
                    "전필" -> ma1 = ma1 + data.Course_POINT
                    "전선" -> ma2 = ma2 + data.Course_POINT
                }
                matot = ma1+ma2
            }

            //화면에 해당하는 전공 학점 출력
            binding.ma1.text = ma1.toString()
            binding.ma2.text = ma2.toString()
            binding.matot.text = matot.toString()

            //사용자가 등록한 교양과목 DB참조
            liberalRef.get().addOnSuccessListener {

                //사용자가 등록한 교양과목 정보 불러오기
                for (courseSnapshot in it.children){
                    var getData = courseSnapshot.getValue(CourseListType::class.java)
                    studentLiberalList.add(getData!!)
                }

                // 교양 과목 학점을 각각 계산
                for (data in studentLiberalList){
                    when(data.division){
                        "역량교양" -> la1 = la1 + data.Course_POINT
                        "통합교양" -> la2 = la2 + data.Course_POINT
                        "개척교양" -> la3 = la3 + data.Course_POINT
                        "기초과정" -> la4 = la4 + data.Course_POINT
                    }
                    latot = la1+la2+la3+la4
                }

                //화면에 해당하는 교양 학점 출력
                binding.la1.text = la1.toString()
                binding.la2.text = la2.toString()
                binding.la3.text = la3.toString()
                binding.la4.text = la4.toString()
                binding.latot.text = latot.toString()

                //전공 교양 합한 결과 출력
                var total = latot?.plus(matot!!)
                binding.total.text = total.toString()
            }

        }

        return binding.root
    }

}
