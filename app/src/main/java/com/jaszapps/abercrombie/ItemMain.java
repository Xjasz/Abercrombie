package com.jaszapps.abercrombie;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import core.Singleton;
import core.reflection.listeners.Listener;
import core.reflection.messages.FragmentChangeMessage;

public class ItemMain {
    private final String TAG = getClass().getSimpleName();
    private String backgroundImage;
    private String topDescription;
    private String itemTitle;
    private String promoMessage;
    private String bottomDescription;
    private String contentTitle1;
    private String contentLink1;
    private String contentTitle2;
    private String contentLink2;

    public ItemMain(String backgroundImage, String topDescription, String itemTitle, String promoMessage, String bottomDescription, String contentTitle1, String contentLink1, String contentTitle2, String contentLink2) {
        this.backgroundImage = backgroundImage;
        this.topDescription = topDescription;
        this.itemTitle = itemTitle;
        this.promoMessage = promoMessage;
        this.bottomDescription = bottomDescription;
        this.contentTitle1 = contentTitle1;
        this.contentLink1 = contentLink1;
        this.contentTitle2 = contentTitle2;
        this.contentLink2 = contentLink2;
    }

    public String getTAG() {
        return TAG;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public String getTopDescription() {
        return topDescription;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public String getPromoMessage() {
        return promoMessage;
    }

    public String getBottomDescription() {
        return bottomDescription;
    }

    public String getContentTitle1() {
        return contentTitle1;
    }

    public String getContentLink1() {
        return contentLink1;
    }

    public String getContentTitle2() {
        return contentTitle2;
    }

    public String getContentLink2() {
        return contentLink2;
    }

    public View getView(final int position, LayoutInflater inflater, View view, ViewGroup parent) {
        final ItemMainHolder holder;
        final ItemMain item = Singleton.AdapterItems.get(position);
        if (view == null) {
            view = inflater.inflate(R.layout.adapter_item_abercrombie, parent, false);
            holder = new ItemMainHolder();
            holder.rlMainItem = view.findViewById(R.id.rlMainItem);
            holder.ivBackgroundImage = (ImageView) view.findViewById(R.id.ivBackgroundImage);
            holder.tvTopDescription = (TextView) view.findViewById(R.id.tvTopDescription);
            holder.tvItemTitle = (TextView) view.findViewById(R.id.tvItemTitle);
            holder.tvPromoMessage = (TextView) view.findViewById(R.id.tvPromoMessage);
            holder.tvBottomDescription = (TextView) view.findViewById(R.id.tvBottomDescription);
            holder.tvContent1 = (TextView) view.findViewById(R.id.tvContent1);
            holder.tvContent2 = (TextView) view.findViewById(R.id.tvContent2);
            view.setTag(holder);
        } else {
            holder = (ItemMainHolder) view.getTag();
        }

        if (item.getBackgroundImage() == null || item.getBackgroundImage().length() < 1) {
            holder.ivBackgroundImage.setVisibility(View.GONE);
            holder.ivBackgroundImage.setImageDrawable(null);
        } else {
            holder.ivBackgroundImage.setVisibility(View.VISIBLE);
            try {
                Picasso.with(Singleton.getAppContext()).load(item.getBackgroundImage())
                    .noFade().placeholder(R.drawable.progress_animation).into(holder.ivBackgroundImage);
            } catch (IllegalStateException e) {
                Log.d(TAG, "negative height");
            }
        }

        holder.tvTopDescription.setVisibility(item.getTopDescription() == null || item.getTopDescription().length() < 1 ? View.GONE : View.VISIBLE);
        holder.tvTopDescription.setText(item.getTopDescription());

        holder.tvItemTitle.setVisibility(item.getItemTitle() == null || item.getItemTitle().length() < 1 ? View.GONE : View.VISIBLE);
        holder.tvItemTitle.setText(item.getItemTitle());

        holder.tvPromoMessage.setVisibility(item.getPromoMessage() == null || item.getPromoMessage().length() < 1 ? View.GONE : View.VISIBLE);
        holder.tvPromoMessage.setText(item.getPromoMessage());

        if (item.getBottomDescription() == null || item.getBottomDescription().length() < 1) {
            holder.tvBottomDescription.setVisibility(View.GONE);
        } else {
            holder.tvBottomDescription.setVisibility(View.VISIBLE);
            holder.tvBottomDescription.setText(Html.fromHtml(item.getBottomDescription()));
            holder.tvBottomDescription.setMovementMethod(LinkMovementMethod.getInstance());

        }


        if (item.getContentTitle1() == null || item.getContentTitle1().length() < 1) {
            holder.tvContent1.setVisibility(View.GONE);
        } else {
            holder.tvContent1.setVisibility(View.VISIBLE);
            holder.tvContent1.setText(Html.fromHtml(item.getContentTitle1()));
            holder.tvContent1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Singleton.ContentToShow = item.getContentLink1();

                    Listener.send(new FragmentChangeMessage(R.id.container_SubFragment,
                        R.anim.slidein_fromright,
                        R.anim.slideout_to_left,
                        new FragmentResult()));

                }
            });
        }

        if (item.getContentTitle2() == null || item.getContentTitle2().length() < 1) {
            holder.tvContent2.setVisibility(View.GONE);
        } else {
            holder.tvContent2.setVisibility(View.VISIBLE);
            holder.tvContent2.setText(Html.fromHtml(item.getContentTitle2()));
            holder.tvContent2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Singleton.ContentToShow = item.getContentLink2();

                    Listener.send(new FragmentChangeMessage(R.id.container_SubFragment,
                        R.anim.slidein_fromright,
                        R.anim.slideout_to_left,
                        new FragmentResult()));

                }
            });
        }

        return view;
    }

}
