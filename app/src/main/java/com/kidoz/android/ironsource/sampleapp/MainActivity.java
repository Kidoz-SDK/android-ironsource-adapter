package com.kidoz.android.ironsource.sampleapp;

import android.os.Bundle;
import android.util.Log;
import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ironsource.adapters.custom.kidoz.KidozSDKInfo;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.InterstitialListener;
import com.ironsource.mediationsdk.sdk.RewardedVideoManualListener;
import com.kidoz.sdk.api.KidozInterstitial;
import com.kidoz.sdk.api.KidozSDK;
import com.kidoz.sdk.api.interfaces.SDKEventListener;
import com.kidoz.sdk.api.ui_views.interstitial.BaseInterstitial;
import com.kidoz.sdk.api.ui_views.kidoz_banner.KidozBannerListener;
import com.kidoz.sdk.api.ui_views.new_kidoz_banner.BANNER_POSITION;
import com.kidoz.sdk.api.ui_views.new_kidoz_banner.KidozBannerView;

public class MainActivity extends Activity implements InterstitialListener, RewardedVideoManualListener {

    public static final String APP_KEY = "11cfc977d"; // TODO Replace with your ironSource App Key here

    // The following parameters are relevant for calling KIDOZ adds directly without ironSource mediation
    public static final String KIDOZ_DIRECT_TEST_PUBLISHER_ID = "5"; // TODO Replace with your KIDOZ Publisher ID
    public static final String KIDOZ_DIRECT_TEST_TOKEN = "i0tnrdwdtq0dm36cqcpg6uyuwupkj76s";// TODO Replace with your KIDOZ Token

    public static final String TAG = MainActivity.class.getSimpleName();

    private KidozBannerView mKidozBannerView;
    private ProgressBar progressBar;

    View loadIronSourceRewardedBtn, showIronSourceRewardedBtn,loadKidozBannerBtn, showKidozBannerBtn, loadIronSourceInterstitialBtn, showIronSourceInterstitialBtn;
    private View initSDKBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityTitle();
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);

        initUIButtons();
        initIronSource();
    }

    private void setActivityTitle() {
        String title =  getString(R.string.app_name) + " v" + KidozSDKInfo.SDK_VERSION_NAME + ":" + KidozSDKInfo.ADAPTER_VERSION_NAME;
        setTitle(title);
    }

    public void initUIButtons() {

        // ironSource Mediation
        loadIronSourceInterstitialBtn = findViewById(R.id.ironSource_is_load_button);
        loadIronSourceInterstitialBtn.setOnClickListener(v -> loadIronSourceInterstitial());

        showIronSourceInterstitialBtn = findViewById(R.id.ironSource_is_show_button);
        showIronSourceInterstitialBtn.setOnClickListener(v -> showIronSourceInterstitial());

        initSDKBtn = findViewById(R.id.init_sdk_button);
        initSDKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initializing KIDOZ SDK directly
                initKidozSDK();
            }
        });

        // KIDOZ direct calls for banners and rewarded
        loadIronSourceRewardedBtn = findViewById(R.id.ironSource_rewarded_load_button);
        loadIronSourceRewardedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadIronSourceRewarded();
            }
        });
        showIronSourceRewardedBtn = findViewById(R.id.ironSource_rewarded_show_button);
        showIronSourceRewardedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIronSourceRewarded();
            }
        });

        loadKidozBannerBtn = findViewById(R.id.banner_load_button);
        loadKidozBannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadKidozBanner();

            }
        });

        showKidozBannerBtn = findViewById(R.id.banner_show_button);
        showKidozBannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKidozBanner();
            }
        });
    }

    ///////////////////////////////
    // ironSource Mediation
    ///////////////////////////////

    private void initIronSource(){

        IronSource.setInterstitialListener(this);
        IronSource.setManualLoadRewardedVideo(this);
        // init the IronSource SDK
        IronSource.init(this, APP_KEY);

    //  Uncomment for extra debug info
    //  IronSource.setAdaptersDebug(true);

    }

    // ****************************
    // ironSource Interstitial
    // ****************************

    @Override
    public void onInterstitialAdReady() {
        log("ironSource onInterstitialAdReady");
        showIronSourceInterstitialBtn.setEnabled(true);
        loadIronSourceInterstitialBtn.setEnabled(false);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onInterstitialAdLoadFailed(IronSourceError ironSourceError) {
        runOnUiThread(new Runnable() {
            public void run() {
                showIronSourceInterstitialBtn.setEnabled(false);
                loadIronSourceInterstitialBtn.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                log("ironSource onInterstitialAdLoadFailed:: " + getErrorLog(ironSourceError));
            }
        });
    }

    @Override
    public void onInterstitialAdOpened() {
        log("ironSource onInterstitialAdOpened");
    }

    @Override
    public void onInterstitialAdClosed() {
        showIronSourceInterstitialBtn.setEnabled(false);
        loadIronSourceInterstitialBtn.setEnabled(true);
        log("ironSource onInterstitialAdClosed");
    }

    @Override
    public void onInterstitialAdShowSucceeded() {
        showIronSourceInterstitialBtn.setEnabled(false);
        loadIronSourceInterstitialBtn.setEnabled(true);
        log("ironSource onInterstitialAdShowSucceeded");
    }

    @Override
    public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {
        showIronSourceInterstitialBtn.setEnabled(false);
        loadIronSourceInterstitialBtn.setEnabled(true);
        log("ironSource onInterstitialAdClosed:: " + getErrorLog(ironSourceError));
    }

    @Override
    public void onInterstitialAdClicked() {
        log("ironSource onInterstitialAdClicked");
    }

    @Override
    public void onRewardedVideoAdOpened() {
        log("ironSource onRewardedVideoAdOpened");
    }

    @Override
    public void onRewardedVideoAdClosed() {
        log("ironSource onRewardedVideoAdClosed");
    }

    // ****************************
    // ironSource Rewarded
    // ****************************

    @Override
    public void onRewardedVideoAvailabilityChanged(boolean ready) {
        if(ready) {
            log("ironSource onRewardedAdReady");
        }
        else{
            log("ironSource onRewardedAdNotReady");
        }
        showIronSourceRewardedBtn.setEnabled(ready);
        loadIronSourceRewardedBtn.setEnabled(!ready);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onRewardedVideoAdStarted() {
        log("ironSource onRewardedVideoAdStarted");
        showIronSourceRewardedBtn.setEnabled(false);
        loadIronSourceRewardedBtn.setEnabled(true);
    }

    @Override
    public void onRewardedVideoAdEnded() {
        log("ironSource onRewardedVideoAdEnded");
        showIronSourceRewardedBtn.setEnabled(false);
        loadIronSourceRewardedBtn.setEnabled(true);
    }

    @Override
    public void onRewardedVideoAdRewarded(Placement placement) {
        log("ironSource onRewardedVideoAdRewarded");
    }

    @Override
    public void onRewardedVideoAdShowFailed(IronSourceError ironSourceError) {
        showIronSourceRewardedBtn.setEnabled(false);
        loadIronSourceRewardedBtn.setEnabled(true);
        log("ironSource onRewardedVideoAdShowFailed:: " + getErrorLog(ironSourceError));
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onRewardedVideoAdClicked(Placement placement) {
        log("ironSource onRewardedVideoAdClicked");
    }

    @Override
    public void onRewardedVideoAdReady() {
        log("ironSource onRewardedVideoAdReady");
        showIronSourceRewardedBtn.setEnabled(true);
        loadIronSourceRewardedBtn.setEnabled(false);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onRewardedVideoAdLoadFailed(IronSourceError ironSourceError){
        showIronSourceRewardedBtn.setEnabled(false);
        loadIronSourceRewardedBtn.setEnabled(true);
        log("ironSource onRewardedVideoAdShowFailed:: " + getErrorLog(ironSourceError));
        progressBar.setVisibility(View.GONE);
    }

    ///////////////////////////////
    // Kidoz Direct
    ///////////////////////////////

    private void initKidozSDK(){
        if(KidozSDK.isInitialised()){
            // If load ironSource interstitial or Rewarded was called previously Kidoz SDK should be already initialized
            onDirectKidozSDKInit();
            log("KIDOZ SDK already initialized");
        }
        else {
            KidozSDK.setSDKListener(new SDKEventListener() {
                @Override
                public void onInitSuccess() {
                    log("KIDOZ SDK onInitSuccess");
                    onDirectKidozSDKInit();

                }

                @Override
                public void onInitError(String error) {
                    log("KIDOZ SDK onInitError");
                    progressBar.setVisibility(View.GONE);

                }
            });

            progressBar.setVisibility(View.VISIBLE);

            KidozSDK.initialize(this, KIDOZ_DIRECT_TEST_PUBLISHER_ID, KIDOZ_DIRECT_TEST_TOKEN);
            log("SDK Init..");
        }
    }

    private void onDirectKidozSDKInit() {

        initKidozBanners();
        initSDKBtn.setEnabled(false);
        loadKidozBannerBtn.setEnabled(true);
        progressBar.setVisibility(View.GONE);
    }

    // Rewarded

    private void loadIronSourceRewarded(){
        IronSource.loadRewardedVideo();
        loadIronSourceRewardedBtn.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void showIronSourceRewarded(){
        IronSource.showRewardedVideo();
        loadIronSourceRewardedBtn.setEnabled(true);
    }

    private void loadIronSourceInterstitial() {
        IronSource.loadInterstitial();
        loadIronSourceInterstitialBtn.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void showIronSourceInterstitial() {
        IronSource.showInterstitial();
        showIronSourceInterstitialBtn.setEnabled(false);
    }


    private void initKidozBanners(){
        mKidozBannerView = KidozSDK.getKidozBanner(MainActivity.this);
        //mKidozBannerView.setSmartBanner(true);
        mKidozBannerView.setBannerPosition(BANNER_POSITION.BOTTOM_CENTER);

        mKidozBannerView.setKidozBannerListener(new KidozBannerListener()
        {
            @Override
            public void onBannerViewAdded()
            {
                log("KIDOZ onBannerViewAdded");
            }

            @Override
            public void onBannerReady()
            {
                log("KIDOZ onBannerReady");
                loadKidozBannerBtn.setEnabled(false);
                showKidozBannerBtn.setEnabled(true);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onBannerError(String errorMsg)
            {
                log("KIDOZ onBannerError:: " + errorMsg );
                loadKidozBannerBtn.setEnabled(true);
                showKidozBannerBtn.setEnabled(false);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onBannerClose()
            {
                log("KIDOZ onBannerClose");
                loadKidozBannerBtn.setEnabled(true);
                showKidozBannerBtn.setEnabled(false);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onBannerNoOffers()
            {
                log("KIDOZ onBannerNoOffers");
                loadKidozBannerBtn.setEnabled(true);
                showKidozBannerBtn.setEnabled(false);
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    private void loadKidozBanner(){
        mKidozBannerView.load();
        loadKidozBannerBtn.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void showKidozBanner(){
        mKidozBannerView.show();
        showKidozBannerBtn.setEnabled(false);
        loadKidozBannerBtn.setEnabled(true);
    }

    private void log(String message){
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        Log.d(TAG,message);
    }

    private String getErrorLog(IronSourceError ironSourceError){
        return ironSourceError.getErrorMessage() + "::" + ironSourceError.getErrorCode();
    }
}
