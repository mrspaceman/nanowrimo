package uk.co.droidinactu.nanowrimo.db


/**
 * Created by aspela on 11/04/17.
 */
class Event : AbstractDbObj() {

    var name: String? = null
    var dateTime: String? = null
    var repeats: RepeatSchedule? = null
    var description: String? = null
    var location: Location? = null
    var invited: List<String>? = null
    var goingYes: List<String>? = null
    var goingMbe: List<String>? = null
    var goingNo: List<String>? = null

    companion object {
        private val LOG_TAG = Event::class.java.getSimpleName() + ":"
    }
}
