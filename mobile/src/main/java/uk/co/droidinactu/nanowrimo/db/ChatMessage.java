package uk.co.droidinactu.nanowrimo.db;

/**
 * Created by aspela on 11/04/17.
 */
public class ChatMessage extends AbstractDbObj {
    private static final String LOG_TAG = ChatMessage.class.getSimpleName() + ":";

    private String text;
    private String name;
    private String photoUrl;
    private String imageUrl;


    private String sender;
    private String recipient;
    private String datetime;
    private String msg;


    public ChatMessage() {
    }

    public ChatMessage(String text, String name, String photoUrl, String imageUrl) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.imageUrl = imageUrl;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
