package uk.co.droidinactu.nanowrimo.db

/**
 * Created by aspela on 11/04/17.
 */
class Location : AbstractDbObj {

    var name: String? = null
    var latitude: Double = 0.toDouble()
    var longitude: Double = 0.toDouble()
    var address: String? = null
    var website: String? = null


    constructor() {}

    constructor(aName: String, aLat: Double, aLong: Double) {
        name = aName.trim { it <= ' ' }
        latitude = aLat
        longitude = aLong
    }

    companion object {
        private val LOG_TAG = Location::class.java.getSimpleName() + ":"
    }
}
