package org.port.trade.fragment.function;
/**
 * Created by 超悟空 on 2015/3/3.
 */

import android.annotation.SuppressLint;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.mobile.common.dialog.SpinnerProgressDialog;
import org.mobile.common.webview.MobileWebChromeClient;
import org.mobile.common.webview.MobileWebViewClient;
import org.mobile.model.fragment.BaseIncludeWebFragment;
import org.port.trade.R;

/**
 * 功能界面的内容WebView的Fragment
 *
 * @author 超悟空
 * @version 1.0 2015/3/3
 * @since 1.0
 */
public class FunctionWebViewFragment extends BaseIncludeWebFragment {

    @Override
    protected void onCustomRootView(View rootView) {
        super.onCustomRootView(rootView);

        // 重新设置WebView的WebChromeClient，使用MobileWebChromeClient
        MobileWebChromeClient webChromeClient = new MobileWebChromeClient(getActivity());
        // 设置旋转型进度加载提示窗，同时设置提示窗超时时间
        webChromeClient.setProgressDialog(new SpinnerProgressDialog(getActivity()).setTimeout(getResources().getInteger(R.integer.web_view_progress_dialog_timeout)));

        getWebView().setWebChromeClient(webChromeClient);
        // 打开缓存
        getWebView().getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        // 重新设置WebView的WebViewClient，使用MobileWebViewClient
        MobileWebViewClient webViewClient = new MobileWebViewClient();
        // 添加加载完成回调
        webViewClient.setLoadFinishedCallBack(new MobileWebViewClient.LoadFinishedCallBack() {
            @Override
            public void operate(WebView view, String url) {
                resetHistory();
                addPreClickListner(view);
            }
        });
        getWebView().setWebViewClient(webViewClient);

        // 添加JS交互接口
        addJavascriptInterface();
    }

    /**
     * 添加JS交互接口
     */
    @SuppressLint({"AddJavascriptInterface" , "JavascriptInterface"})
    @JavascriptInterface
    private void addJavascriptInterface() {
        // 添加JS交互接口
        getWebView().addJavascriptInterface(new Javascript(), "androidWebView");
    }

    @Override
    public int initRootViewLayoutID() {
        return R.layout.only_include_web_fragment;
    }

    @Override
    public int initWebViewLayoutID() {
        return R.id.only_webView;
    }

    @Override
    protected String onCreateUrl() {
        return null;
    }

    /**
     * 注入js函数监听
     *
     * @param view WebView对象
     */
    private void addPreClickListner(WebView view) {
        // 这段js函数的功能就是将上一页按钮的响应事件替换成WebView的后退事件
        //view.loadUrl("javascript:$(\"#prev\").unbind(\"click\");$(\"#prev\").click(function(){window.androidWebView.goBack();});");
    }

    /**
     * JS通信接口
     */
    public class Javascript {
        /**
         * 网页后退
         */
        public void goBack() {
            onGoBack();
        }
    }
}
