package core.reflection.messages;

import android.support.v4.app.Fragment;

public class FragmentChangeMessage extends Message {

    public final int containerID;
    public final int inAnimation;
    public final int outAnimation;
    public final Fragment fragment;


    public FragmentChangeMessage(int containerID, int inAnimation, int outAnimation,
                                 Fragment fragment) {
        super();
        this.containerID = containerID;
        this.inAnimation = inAnimation;
        this.outAnimation = outAnimation;
        this.fragment = fragment;
    }

    @Override
    public String getMessageDescription() {
        return "";
    }
}
