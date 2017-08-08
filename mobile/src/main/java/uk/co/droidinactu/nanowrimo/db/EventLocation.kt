package uk.co.droidinactu.nanowrimo.db

/**
 * Created by aspela on 01/09/16.
 */
class EventLocation : AbstractDbObj {

    private lateinit var event: Event
    private lateinit var tag: Location

    constructor() {}

    constructor(ebk: Event, tg: Location) {
        this.event = ebk
        this.tag = tg
    }

    override fun toString(): String {
        return "Book [$event] tag [$tag]"
    }

    companion object {
        val COLUMN_NAME_EVENT_ID = "event_id"
        val COLUMN_NAME_LOCATION_ID = "location_id"
    }
}
