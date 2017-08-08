package uk.co.droidinactu.nanowrimo.db

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

/**
 * Stores the number of words written on a specified date
 *
 *
 * Created by aspela on 12/07/17.
 */
class DayWordCount : AbstractDbObj {

    var date: String? = null
    var wordcount: Int = 0
        private set // number of words written that day

    constructor() {}

    constructor(pDate: String, pWrdCount: Int) {
        this.date = pDate
        this.wordcount = pWrdCount
    }

    fun setDaysWordCount(pWrdCount: Int) {
        this.wordcount = pWrdCount
    }

    val dayNumber: Int
        get() {
            val formatter = DateTimeFormat.forPattern("dd/MM/yyyy")
            val dt = formatter.parseDateTime(date!!)
            return dt.dayOfMonth
        }

    val monthNumber: Int
        get() {
            val formatter = DateTimeFormat.forPattern("dd/MM/yyyy")
            val dt = formatter.parseDateTime(date!!)
            return dt.monthOfYear
        }

    val yearNumber: Int
        get() {
            val formatter = DateTimeFormat.forPattern("dd/MM/yyyy")
            val dt = formatter.parseDateTime(date!!)
            return dt.year
        }

    companion object {
        private val LOG_TAG = DayWordCount::class.java.getSimpleName() + ":"
    }

}
