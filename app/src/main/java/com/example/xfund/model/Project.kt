package com.example.xfund.model

import android.os.Parcel
import android.os.Parcelable
import java.util.Date


data class Project(
    val cover: Int,
    val name: String,
    val start_date: Date,
    val end_date: Date,
    val fund_target: Int,
    var fund_collected: Int,
    val description: String
) : Parcelable {
    val percentage: Int
        get() = ((fund_collected.toFloat() / fund_target) * 100).toInt()

    val percentageValue: String = percentage.toString()

    // Parcelable implementation
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        Date(parcel.readLong()),
        Date(parcel.readLong()),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(cover)
        parcel.writeString(name)
        parcel.writeLong(start_date.time)
        parcel.writeLong(end_date.time)
        parcel.writeInt(fund_target)
        parcel.writeInt(fund_collected)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Project> {
        override fun createFromParcel(parcel: Parcel): Project {
            return Project(parcel)
        }

        override fun newArray(size: Int): Array<Project?> {
            return arrayOfNulls(size)
        }
    }
}