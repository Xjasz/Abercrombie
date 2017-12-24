package core.reflection.messages;

import com.jaszapps.abercrombie.ItemMain;

public class AdapterMessage<T> extends Message {

    public final int id;
    public final ItemMain itemMain;

    public AdapterMessage(int id, ItemMain itemMain) {
        this.id = id;
        this.itemMain = itemMain;
    }

    @Override
    public String getMessageDescription() {
        return "";
    }
}
