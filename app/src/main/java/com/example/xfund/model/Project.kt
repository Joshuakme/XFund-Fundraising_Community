package com.example.xfund.model

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Date


data class Project(

    val cover: String,
    val name: String,
    val description: String,
    val start_date: LocalDateTime,
    val end_date: LocalDateTime,
    var fund_collected: Double,
    val fund_target: Double

) : Parcelable {
    val percentage: Int
        get() = ((fund_collected/ fund_target) * 100).toInt()

    val percentageValue: String = percentage.toString()

    var stringFundTarget: String = "0.00"
        get() = (String.format("%.2f", fund_target))

    // Parcelable implementation
    @RequiresApi(Build.VERSION_CODES.O)
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        Date(parcel.readLong()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
        Date(parcel.readLong()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(cover)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeLong(start_date.toMillis())
        parcel.writeLong(end_date.toMillis())
        parcel.writeDouble(fund_collected)
        parcel.writeDouble(fund_target)
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun LocalDateTime.toMillis(): Long {
        return this.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
    }
}