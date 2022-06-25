package ir.hm.studentmanager.recycler

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Student(

    var name: String,
    var course: String,
    var score: Int

):Parcelable