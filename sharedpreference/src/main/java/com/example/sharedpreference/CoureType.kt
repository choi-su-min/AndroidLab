package com.example.sharedpreference

import android.os.Parcel
import android.os.Parcelable

data class CoureType(
    val Course_NAME: String?="",
    val division: String?="",
    val Course_POINT: Int = 0

):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(Course_NAME)
        parcel.writeString(division)
        parcel.writeInt(Course_POINT)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CoureType> {
        override fun createFromParcel(parcel: Parcel): CoureType {
            return CoureType(parcel)
        }

        override fun newArray(size: Int): Array<CoureType?> {
            return arrayOfNulls(size)
        }
    }
}
