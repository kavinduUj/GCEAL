package com.appflavorsz.gceal.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.appflavorsz.gceal.Config;
import com.appflavorsz.gceal.R;
import com.appflavorsz.gceal.model.GetAllData;
import com.facebook.ads.AdError;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdsManager;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class dataModelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private static List<Object> objectList;
    private NativeAdsManager nativeAdsManager;
    private List<NativeAd> nativeAds = new ArrayList<>();

    private static int ITEM_TYPE = 0;
    private static int ADS_TYPE = 1;

    private int AD_POSITION = Prefs.getInt("nativePosi",4);
    private int AD_POSITION_EVERY_COUNT = Prefs.getInt("native_interval",5);

    public static String pdfUrl;

    OnClick onClick;

    public dataModelAdapter(Context context, List<Object> objectList, int ListSize,OnClick onClick) {
        this.context = context;
        this.objectList = objectList;
        int no_of_ad_req = ListSize / (AD_POSITION_EVERY_COUNT - 1);
        nativeAdsManager = new NativeAdsManager(context, Config.FB_Native, no_of_ad_req);
        this.onClick = onClick;
    }


    public void initNativeAds() {
        Log.d("TEST_APP", "Size1: " + objectList.size());
        if (Prefs.getBoolean("adEnable",true)){
            nativeAdsManager.setListener(new NativeAdsManager.Listener() {
                @Override
                public void onAdsLoaded() {
                    int count = nativeAdsManager.getUniqueNativeAdCount();
                    for (int i = 0; i < count; i++) {
                        NativeAd ad = nativeAdsManager.nextNativeAd();
                        addNativeAd(i, ad);
                    }
                }

                @Override
                public void onAdError(AdError adError) {
                    Log.d("TEST_APP", "" + adError.getErrorMessage());
                }
            });
            nativeAdsManager.loadAds();
        }else {

        }

    }

    public void addNativeAd(int i, NativeAd ad) {
        if (ad == null) {
            return;
        }
        if (this.nativeAds.size() > i && this.nativeAds.get(i) != null) {
            this.nativeAds.get(i).unregisterView();
            this.objectList.remove(AD_POSITION + (i * AD_POSITION_EVERY_COUNT));
            this.nativeAds = null;
            this.notifyDataSetChanged();
        }
        this.nativeAds.add(i, ad);

        if (objectList.size() > (AD_POSITION + (i * AD_POSITION_EVERY_COUNT))) {
            objectList.add(AD_POSITION + (i * AD_POSITION_EVERY_COUNT), ad);
            notifyItemInserted(AD_POSITION + (i * AD_POSITION_EVERY_COUNT));
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("TEST_APP", "Size2: " + objectList.size());
        if (viewType == ADS_TYPE) {
            return new NativeViewHolder(LayoutInflater.from(context).inflate(R.layout.tauseef_native_ad_row, parent, false));
        } else {
            return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapterview, parent, false));
        }

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewHolder) {
            GetAllData mainModel = (GetAllData) objectList.get(position);
            MyViewHolder myViewHolder = (MyViewHolder) holder;

            String xx = mainModel.getImageUrl();
            pdfUrl = mainModel.getPdfUrl();
            Prefs.putString("getPdfUrl", pdfUrl);

            if (xx.isEmpty()) {

            } else {
                Picasso.get().load(mainModel.getImageUrl()).fit().centerInside().into(myViewHolder.imageView);

            }
            myViewHolder.title.setText(mainModel.getTitle());
            myViewHolder.name.setText(mainModel.getName());

//            myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(context, Webview.class);
//                    i.putExtra("pdfUrl", mainModel.getPdfUrl());
//                    i.putExtra("getTitle", mainModel.getTitle());
//                    context.startActivity(i);
//                    showAd();
//                }
//            });
        }
        else {
            NativeAd nativeAd = (NativeAd) objectList.get(position);
            NativeViewHolder nativeViewHolder = (NativeViewHolder) holder;

            nativeViewHolder.nativeAdTitle.setText(nativeAd.getAdvertiserName());
            nativeViewHolder.nativeAdBody.setText(nativeAd.getAdBodyText());
            nativeViewHolder.nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
            nativeViewHolder.nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
            nativeViewHolder.nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
            nativeViewHolder.sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(nativeViewHolder.nativeAdTitle);
            clickableViews.add(nativeViewHolder.nativeAdCallToAction);

            //Register the Title and CTA button to listen for clicks.
            nativeAd.registerViewForInteraction(nativeViewHolder.itemView.getRootView(),
                    nativeViewHolder.nativeAdMedia,
                    nativeViewHolder.nativeAdIcon, clickableViews);

//            nativeAd.registerViewForInteraction(
//                    nativeAd, nativeViewHolder.nativeAdMedia, nativeViewHolder.nativeAdIcon, clickableViews);
        }
    }




    @Override
    public int getItemViewType(int position) {
        if (objectList.get(position) instanceof NativeAd) {
            return ADS_TYPE;
        } else {
            return ITEM_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        Log.d("TEST_APP", "Sizeb: " + objectList.size());
        return objectList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        TextView title;
        TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.getDetails);
            imageView = itemView.findViewById(R.id.getProfile);
            title = itemView.findViewById(R.id.getTitle);
            name = itemView.findViewById(R.id.getName);


            itemView.setOnClickListener(view -> {

                onClick.OnItemClick((GetAllData) objectList.get(getAdapterPosition()));

            });
        }
    }

    static class NativeViewHolder extends RecyclerView.ViewHolder {

        MediaView nativeAdIcon;
        TextView nativeAdTitle;
        MediaView nativeAdMedia;
        TextView nativeAdSocialContext;
        TextView nativeAdBody;
        TextView sponsoredLabel;
        Button nativeAdCallToAction;


        public NativeViewHolder(@NonNull View itemView) {
            super(itemView);

            nativeAdIcon = itemView.findViewById(R.id.native_ad_icon);
            nativeAdTitle = itemView.findViewById(R.id.native_ad_title);
            nativeAdMedia = itemView.findViewById(R.id.native_ad_media);
            nativeAdSocialContext = itemView.findViewById(R.id.native_ad_social_context);
            nativeAdBody = itemView.findViewById(R.id.native_ad_body);
            sponsoredLabel = itemView.findViewById(R.id.native_ad_sponsored_label);
            nativeAdCallToAction = itemView.findViewById(R.id.native_ad_call_to_action);
        }
    }
}

