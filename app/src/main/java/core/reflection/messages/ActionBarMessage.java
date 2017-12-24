package core.reflection.messages;

public class ActionBarMessage<T> extends Message {

    public final T id;
    public T progressMax;
    public T progress;
    public T results;

    public ActionBarMessage(T id, T progressMax, T progress, T results) {
        this.id = id;
        this.progressMax = progressMax;
        this.progress = progress;
        this.results = results;
    }

    public ActionBarMessage(T id) {
        this.id = id;
    }

    @Override
    public String getMessageDescription() {
        return "";
    }
}
