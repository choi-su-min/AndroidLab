package com.example.ch20_firebase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ch20_firebase.databinding.FragmentFourBinding
import com.google.firebase.database.FirebaseDatabase

class FourFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val binding:FragmentFourBinding by lazy { FragmentFourBinding.inflate(layoutInflater) }
        val recyclerview = binding.recycler01
        recyclerview.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false))
        var adapter:SuggestListAdapter?

        val userNum = arguments?.getString("usernum") //사용자 id(학번)
        val year = arguments?.getString("year") //입학년도

        //-------------------------전공 구분 지어주는 리스트------------------------
        var majorList: ArrayList<CourseListType> = arrayListOf() //전공 전체 과목

        var major1: ArrayList<CourseListType> = arrayListOf() //전필
        var major2: ArrayList<CourseListType> = arrayListOf() //전선
        var major3: ArrayList<CourseListType> = arrayListOf() //전공선택심화
        var major4: ArrayList<CourseListType> = arrayListOf() //전공선택

        //-------------------------교양 구분 지어주는 리스트------------------------
        var liberalList: ArrayList<CourseListType> = arrayListOf() //교양 전체 과목

        var liberal1: ArrayList<CourseListType> = arrayListOf() //기초과정
        var rand_take_la1: ArrayList<CourseListType> = arrayListOf() //기초과정 최종 필터 리스트

        var liberal2: ArrayList<CourseListType> = arrayListOf() //통합
        var liberal2_1: ArrayList<CourseListType> = arrayListOf() //통합1 : 문학과 문화
        var liberal2_2: ArrayList<CourseListType> = arrayListOf() //통합2 : 역사와 철학
        var liberal2_3: ArrayList<CourseListType> = arrayListOf() //통합3 : 인간과 사회
        var liberal2_4: ArrayList<CourseListType> = arrayListOf() //통합4 : 생명과 환경
        var liberal2_5: ArrayList<CourseListType> = arrayListOf() //통합5 : 과학과 기술
        var liberal2_6: ArrayList<CourseListType> = arrayListOf() //통합6 : 예술과 체육
        var liberal2_7: ArrayList<CourseListType> = arrayListOf() //통합7 : 융복합영역
        var rand_take_la2_1: ArrayList<CourseListType> = arrayListOf() //통합교양 최종 필터 리스트
        var rand_take_la2_2: ArrayList<CourseListType> = arrayListOf() //통합교양 최종 필터 리스트
        var rand_take_la2_3: ArrayList<CourseListType> = arrayListOf() //통합교양 최종 필터 리스트
        var rand_take_la2_4: ArrayList<CourseListType> = arrayListOf() //통합교양 최종 필터 리스트
        var rand_take_la2_5: ArrayList<CourseListType> = arrayListOf() //통합교양 최종 필터 리스트
        var rand_take_la2_6: ArrayList<CourseListType> = arrayListOf() //통합교양 최종 필터 리스트
        var rand_take_la2_7: ArrayList<CourseListType> = arrayListOf() //통합교양 최종 필터 리스트

        var liberal3: ArrayList<CourseListType> = arrayListOf() //개척교양
        var rand_take_la3: ArrayList<CourseListType> = arrayListOf() //개척교양 최종 필터 리스트

        var liberal4: ArrayList<CourseListType> = arrayListOf() //역량교양
        //---------------------------------------------------------------------

        val realtimeDB: FirebaseDatabase = FirebaseDatabase.getInstance()

        val liberalRef = realtimeDB.reference.child("LiberalArts")
        val userLiberalRef = realtimeDB.reference.child(userNum.toString()).child("교양")

        val majorRef = realtimeDB.reference.child("Major")
        val majorRef21 = realtimeDB.reference.child("Major21")
        val userMajorRef = realtimeDB.reference.child(userNum.toString()).child("전공")

        if (year == "2021" || year == "2022"){ //21년 22년 과목 필터

            //전공 과목 필터
            majorRef21.get().addOnSuccessListener {

                var majorData21: ArrayList<CourseListType> = arrayListOf()
                for (courseSnapshot in it.children){
                    var getData = courseSnapshot.getValue(CourseListType::class.java)
                    majorData21.add(getData!!)
                }

                userMajorRef.get().addOnSuccessListener {

                    var userData21: ArrayList<CourseListType> = arrayListOf()
                    for (courseSnapshot in it.children){
                        var getData = courseSnapshot.getValue(CourseListType::class.java)
                        userData21.add(getData!!)
                    }

                    majorList.addAll(majorData21)
                    majorList.removeAll(userData21)

                    major1 = majorList.filter { it.division == "전필" } as ArrayList<CourseListType>

                    major2 = majorList.filter { it.division == "전선" } as ArrayList<CourseListType>
                    major3 = major2.filter { it.deep == "심화" } as ArrayList<CourseListType>
                    major4 = major2.filter { it.deep == "선택" } as ArrayList<CourseListType>

                    //교양 과목 필터
                    liberalRef.get().addOnSuccessListener {

                        var liberalData21: ArrayList<CourseListType> = arrayListOf()
                        for (courseSnapshot in it.children){
                            var getData = courseSnapshot.getValue(CourseListType::class.java)
                            liberalData21.add(getData!!)
                        }

                        userLiberalRef.get().addOnSuccessListener {

                            var userData21: ArrayList<CourseListType> = arrayListOf()
                            for (courseSnapshot in it.children) {
                                var getData =
                                    courseSnapshot.getValue(CourseListType::class.java)
                                userData21.add(getData!!)
                            }

                            liberalList.addAll(liberalData21)
                            liberalList.removeAll(userData21)

                            liberal1 = liberalList.filter { it.division == "기초과정" } as ArrayList<CourseListType>
                            liberal1.shuffle()

                            //문학과 문화, 역사와 철학, 인간과 사회, 생명과 환경, 과학과 기술, 예술과 체육, 융복합영역
                            liberal2 = liberalList.filter { it.division == "통합교양" } as ArrayList<CourseListType>
                            liberal2.shuffle()
                            liberal2_1 = liberal2.filter { it.deep == "문학과문화" } as ArrayList<CourseListType>
                            liberal2_2 = liberal2.filter { it.deep == "역사와철학" } as ArrayList<CourseListType>
                            liberal2_3 = liberal2.filter { it.deep == "인간과사회" } as ArrayList<CourseListType>
                            liberal2_4 = liberal2.filter { it.deep == "생명과환경" } as ArrayList<CourseListType>
                            liberal2_5 = liberal2.filter { it.deep == "과학과기술" } as ArrayList<CourseListType>
                            liberal2_6 = liberal2.filter { it.deep == "예술과체육" } as ArrayList<CourseListType>
                            liberal2_7 = liberal2.filter { it.deep == "융·복합 영역" } as ArrayList<CourseListType>

                            liberal3 = liberalList.filter { it.division == "개척교양" } as ArrayList<CourseListType>
                            liberal3.shuffle()

                            liberal4 = liberalList.filter { it.division == "역량교양" } as ArrayList<CourseListType>

                            //통합교양 각 영역별로 5개씩 짜름.
                            rand_take_la1 = liberal1.take(5) as ArrayList<CourseListType>

                            rand_take_la2_1 = liberal2_1.take(5) as ArrayList<CourseListType>
                            rand_take_la2_2 = liberal2_2.take(5) as ArrayList<CourseListType>
                            rand_take_la2_3 = liberal2_3.take(5) as ArrayList<CourseListType>
                            rand_take_la2_4 = liberal2_4.take(5) as ArrayList<CourseListType>
                            rand_take_la2_5 = liberal2_5.take(5) as ArrayList<CourseListType>
                            rand_take_la2_6 = liberal2_6.take(5) as ArrayList<CourseListType>
                            rand_take_la2_7 = liberal2_7.take(5) as ArrayList<CourseListType>

                            rand_take_la3 = liberal3.take(5) as ArrayList<CourseListType>

                            //문학과 문화, 역사와 철학, 인간과 사회, 생명과 환경, 과학과 기술, 예술과 체육, 융복합영역
                            var keylist = listOf(
                                "전공필수","심화선택","전공선택",
                                "기초과정",
                                "통합1 : 문학과 문화","통합2 : 역사와 철학","통합3 : 인간과 사회","통합4 : 생명과 환경","통합5 : 과학과 기술","통합6 : 예술과 체육","통합7 : 융복합영역",
                                "개척교양",
                                "역량교양"
                            )

                            var valuelist = listOf(
                                major1,major3,major4,
                                rand_take_la1,
                                rand_take_la2_1,rand_take_la2_2,rand_take_la2_3,rand_take_la2_4,rand_take_la2_5,rand_take_la2_6,rand_take_la2_7,
                                rand_take_la3,
                                liberal4
                            )

                            adapter = SuggestListAdapter(valuelist, keylist)
                            recyclerview.layoutManager = LinearLayoutManager(context)
                            recyclerview.adapter = adapter
                        }
                    }

                }

            }

        }
        else{ //21년도 이전 과목 필터
            //전공 과목 필터
            majorRef.get().addOnSuccessListener {

                var majorData: ArrayList<CourseListType> = arrayListOf()
                for (courseSnapshot in it.children){
                    var getData = courseSnapshot.getValue(CourseListType::class.java)
                    majorData.add(getData!!)
                }

                userMajorRef.get().addOnSuccessListener {

                    var userData: ArrayList<CourseListType> = arrayListOf()
                    for (courseSnapshot in it.children){
                        var getData = courseSnapshot.getValue(CourseListType::class.java)
                        userData.add(getData!!)
                    }

                    majorList.addAll(majorData)
                    majorList.removeAll(userData)

                    major1 = majorList.filter { it.division == "전필" } as ArrayList<CourseListType>

                    major2 = majorList.filter { it.division == "전선" } as ArrayList<CourseListType>
                    major3 = major2.filter { it.deep == "심화" } as ArrayList<CourseListType>
                    major4 = major2.filter { it.deep == "선택" } as ArrayList<CourseListType>

                    //교양 과목 필터
                    liberalRef.get().addOnSuccessListener {

                        var liberalData: ArrayList<CourseListType> = arrayListOf()
                        for (courseSnapshot in it.children){
                            var getData = courseSnapshot.getValue(CourseListType::class.java)
                            liberalData.add(getData!!)
                        }

                        userLiberalRef.get().addOnSuccessListener {

                            var userData: ArrayList<CourseListType> = arrayListOf()
                            for (courseSnapshot in it.children) {
                                var getData =
                                    courseSnapshot.getValue(CourseListType::class.java)
                                userData.add(getData!!)
                            }

                            liberalList.addAll(liberalData)
                            liberalList.removeAll(userData)

                            liberal1 = liberalList.filter { it.division == "기초과정" } as ArrayList<CourseListType>
                            liberal1.shuffle()
                            liberal2 = liberalList.filter { it.division == "통합교양" } as ArrayList<CourseListType>
                            liberal2.shuffle()

                            //문학과 문화, 역사와 철학, 인간과 사회, 생명과 환경, 과학과 기술, 예술과 체육, 융복합영역
                            liberal2_1 = liberal2.filter { it.deep == "문학과문화" } as ArrayList<CourseListType>
                            liberal2_2 = liberal2.filter { it.deep == "역사와철학" } as ArrayList<CourseListType>
                            liberal2_3 = liberal2.filter { it.deep == "인간과사회" } as ArrayList<CourseListType>
                            liberal2_4 = liberal2.filter { it.deep == "생명과환경" } as ArrayList<CourseListType>
                            liberal2_5 = liberal2.filter { it.deep == "과학과기술" } as ArrayList<CourseListType>
                            liberal2_6 = liberal2.filter { it.deep == "예술과체육" } as ArrayList<CourseListType>
                            liberal2_7 = liberal2.filter { it.deep == "융·복합 영역" } as ArrayList<CourseListType>

                            liberal3 = liberalList.filter { it.division == "개척교양" } as ArrayList<CourseListType>
                            liberal3.shuffle()
                            liberal4 = liberalList.filter { it.division == "역량교양" } as ArrayList<CourseListType>

                            rand_take_la1 = liberal1.take(5) as ArrayList<CourseListType>

                            rand_take_la2_1 = liberal2_1.take(5) as ArrayList<CourseListType>
                            rand_take_la2_2 = liberal2_2.take(5) as ArrayList<CourseListType>
                            rand_take_la2_3 = liberal2_3.take(5) as ArrayList<CourseListType>
                            rand_take_la2_4 = liberal2_4.take(5) as ArrayList<CourseListType>
                            rand_take_la2_5 = liberal2_5.take(5) as ArrayList<CourseListType>
                            rand_take_la2_6 = liberal2_6.take(5) as ArrayList<CourseListType>
                            rand_take_la2_7 = liberal2_7.take(5) as ArrayList<CourseListType>

                            rand_take_la3 = liberal3.take(5) as ArrayList<CourseListType>

                            //문학과 문화, 역사와 철학, 인간과 사회, 생명과 환경, 과학과 기술, 예술과 체육, 융복합영역
                            var keylist = listOf(
                                "전공필수","심화선택","전공선택",
                                "기초과정",
                                "통합1 : 문학과 문화","통합2 : 역사와 철학","통합3 : 인간과 사회","통합4 : 생명과 환경","통합5 : 과학과 기술","통합6 : 예술과 체육","통합7 : 융복합영역",
                                "개척교양",
                                "역량교양"
                            )

                            var valuelist = listOf(
                                major1,major3,major4,
                                rand_take_la1,
                                rand_take_la2_1,rand_take_la2_2,rand_take_la2_3,rand_take_la2_4,rand_take_la2_5,rand_take_la2_6,rand_take_la2_7,
                                rand_take_la3,
                                liberal4
                            )

                            adapter = SuggestListAdapter(valuelist, keylist)
                            recyclerview.layoutManager = LinearLayoutManager(context)
                            recyclerview.adapter = adapter
                        }
                    }

                }

            }
        }

        return binding.root
    }

}
