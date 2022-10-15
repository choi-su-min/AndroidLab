package com.example.ch20_firebase

import java.io.Serializable

data class MyList(
    val Course_NAME: String="" ,
    val division: String="",
    val Course_POINT: Int = 0,
    val checked : String="",
    val details: String ="",
    val spec_num: String=""
):Serializable{

}
