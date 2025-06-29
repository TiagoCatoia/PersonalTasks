package com.example.personaltasks.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.personaltasks.model.Constant.INVALID_CONTACT_ID
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Parcelize
@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = INVALID_CONTACT_ID,
    var title: String = "",
    var description: String = "",
    var deadline: String =
        LocalDate.now().format(
            DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())),
    var finished: Boolean = false,
    var removed: Boolean = false,
    var prior: String = ""
): Parcelable
