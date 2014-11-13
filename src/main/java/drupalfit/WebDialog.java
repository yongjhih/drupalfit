/**
 * Copyright 2010-present Facebook.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package drupalfit;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.*;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.webkit.CookieSyncManager;
import java.net.URL;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.RetrofitError;

/**
 * A webdialog for external web and redirect url callback
 */
public class WebDialog extends Dialog {
    public static final int DEFAULT_THEME = android.R.style.Theme_Translucent_NoTitleBar;
    private String url;

    private static final String DISPLAY_TOUCH = "touch";
    private static final String USER_AGENT = "user_agent";
    static final String REDIRECT_URI = "fbconnect://success";
    static final String CANCEL_URI = "fbconnect://cancel";
    static final boolean DISABLE_SSL_CHECK_FOR_TESTING = false;

    // width below which there are no extra margins
    private static final int NO_PADDING_SCREEN_WIDTH = 480;
    // width beyond which we're always using the MIN_SCALE_FACTOR
    private static final int MAX_PADDING_SCREEN_WIDTH = 800;
    // height below which there are no extra margins
    private static final int NO_PADDING_SCREEN_HEIGHT = 800;
    // height beyond which we're always using the MIN_SCALE_FACTOR
    private static final int MAX_PADDING_SCREEN_HEIGHT = 1280;

    // the minimum scaling factor for the web dialog (50% of screen size)
    private static final double MIN_SCALE_FACTOR = 0.5;
    // translucent border around the webview

    /**
     * Constructor which can be used to display a dialog with an already-constructed URL.
     *
     * @param context the context to use to display the dialog
     * @param url     the URL of the Web Dialog to display; no validation is done on this URL, but it should
     *                be a valid URL pointing to a Facebook Web Dialog
     */
    public WebDialog(Context context, String url) {
        this(context, url, DEFAULT_THEME);
        Log8.d();
    }

    public WebDialog(Context context, String url, Callback<String> callback) {
        this(context, url, DEFAULT_THEME, callback);
    }

    protected Callback<String> callback;

    public WebDialog(Context context, String url, int theme, Callback<String> callback) {
        this(context, url, theme);
        this.callback = callback;
    }

    /**
     * Constructor which can be used to display a dialog with an already-constructed URL and a custom theme.
     *
     * @param context the context to use to display the dialog
     * @param url     the URL of the Web Dialog to display; no validation is done on this URL, but it should
     *                be a valid URL pointing to a Facebook Web Dialog
     * @param theme   identifier of a theme to pass to the Dialog class
     */
    public WebDialog(Context context, String url, int theme) {
        super(context, theme);
        Log8.d();
        this.url = url;
    }

    @Override
    public void dismiss() {
        Log8.d();
        if (webView != null) {
        Log8.d();
            webView.stopLoading();
        }
        if (!isDetached) {
            if (spinner.isShowing()) {
        Log8.d();
                spinner.dismiss();
            }
        Log8.d();
            super.dismiss();
        }
    }

    @Override
    public void onDetachedFromWindow() {
        isDetached = true;
        Log8.d();
        super.onDetachedFromWindow();
    }

    private boolean isDetached = false;

    @Override
    public void onAttachedToWindow() {
        isDetached = false;
        Log8.d();
        super.onAttachedToWindow();
    }

    private WebView webView;
    private ProgressDialog spinner;
    private FrameLayout contentFrameLayout;
    private boolean listenerCalled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log8.d();
        spinner = new ProgressDialog(getContext());
        spinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        spinner.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log8.d();
                WebDialog.this.dismiss();
            }
        });
        Log8.d();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        contentFrameLayout = new FrameLayout(getContext());

        // First calculate how big the frame layout should be
        getWindow().setGravity(Gravity.CENTER);
        Log8.d();

        // resize the dialog if the soft keyboard comes up
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Log8.d();
        setUpWebView(500);
        Log8.d();
        setContentView(contentFrameLayout);
        Log8.d();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setUpWebView(int margin) {
        Log8.d();
        LinearLayout webViewContainer = new LinearLayout(getContext());
        webView = new WebView(getContext());
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new DialogWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        webView.setVisibility(View.INVISIBLE);
        webView.getSettings().setSavePassword(false);
        webView.getSettings().setSaveFormData(false);

        webViewContainer.setPadding(margin, margin, margin, margin);
        webViewContainer.addView(webView);
        webViewContainer.setBackgroundColor(0xCC000000);
        contentFrameLayout.addView(webViewContainer);
        Log8.d();
    }

    private class DialogWebViewClient extends WebViewClient {
        @Override
        @SuppressWarnings("deprecation")
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (true) {
                Log8.d(url);
                if (url.contains("error")) {
                    // error
                    Log8.d("error");
                    callback.failure(RetrofitError.unexpectedError(url, new RuntimeException()));
                } else {
                    // success
                    callback.success(android.webkit.CookieManager.getInstance().getCookie(url), (Response) null);
                    Log8.d("success");
                }
                WebDialog.this.dismiss();
                return true;
            } else if (url.startsWith(WebDialog.CANCEL_URI)) {
        Log8.d();
                WebDialog.this.dismiss();
                return true;
            } else if (url.contains(DISPLAY_TOUCH)) {
        Log8.d();
                return false;
            }
            // launch non-dialog URLs in a full browser
            getContext().startActivity(
                    new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        Log8.d(failingUrl);
            WebDialog.this.dismiss();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        Log8.d();
                handler.proceed();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log8.d(url);
            if (!isDetached) {
                Log8.d();
                spinner.show();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log8.d(url);
            if (!isDetached) {
                Log8.d();
                spinner.dismiss();
            }
            /*
             * Once web view is fully loaded, set the contentFrameLayout background to be transparent
             * and make visible the 'x' image.
             */
            Log8.d();
            contentFrameLayout.setBackgroundColor(Color.TRANSPARENT);
            webView.setVisibility(View.VISIBLE);
        }
    }
}
