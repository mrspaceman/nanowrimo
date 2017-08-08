package uk.co.droidinactu.nanowrimo.db

/**
 * Created by aspela on 11/04/17.
 */
class ChatMessage : AbstractDbObj {

    var text: String? = null
    var name: String? = null
    var photoUrl: String? = null
    var imageUrl: String? = null


    private val sender: String? = null
    private val recipient: String? = null
    private val datetime: String? = null
    private val msg: String? = null


    constructor() {}

    constructor(text: String, name: String, photoUrl: String, imageUrl: String) {
        this.text = text
        this.name = name
        this.photoUrl = photoUrl
        this.imageUrl = imageUrl
    }

    companion object {
        private val LOG_TAG = ChatMessage::class.java.getSimpleName() + ":"
    }
}
