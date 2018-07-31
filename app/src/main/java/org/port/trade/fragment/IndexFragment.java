package org.port.trade.fragment;
/**
 * Created by 超悟空 on 2015/1/12.
 */

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.handmark.pulltorefresh.library.PullToRefreshWebView;

import org.mobile.common.webview.MobileWebChromeClient;
import org.mobile.model.fragment.BaseIncludeWebFragment;
import org.mobile.model.operate.TitleHandle;
import org.port.trade.R;
import org.port.trade.activity.CommonFunctionActivity;
import org.port.trade.util.StaticValue;

/**
 * 首页咨询的功能片段，
 * 仅包含WebView的Fragment片段
 *
 * @author 超悟空
 * @version 1.0 2015/1/22
 * @since 1.0
 */
public class IndexFragment extends BaseIncludeWebFragment implements TitleHandle {

    /**
     * 保存当前的标题
     */
    private String currentTitle = null;

    /**
     * 带有下拉刷新的WebView控件
     */
    private PullToRefreshWebView pullRefreshWebView = null;

    @Override
    protected void onCustomRootView(View rootView) {
        // 开启追加菜单项
        setHasOptionsMenu(true);
        // 启用缓存
        //getWebView().getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        // 重新设置WebView的WebChromeClient，使用MobileWebChromeClient
        getWebView().setWebChromeClient(new MobileWebChromeClient(getActivity()) {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                // 执行title回调
                if (title != null && title.length() > 0) {

                    if (title.equals(getString(org.mobile.mobilelibrary.R.string
                            .web_view_load_error_title))) {
                        // 当前页面为错误页面，替换title为本功能页名称
                        title = getString(R.string.pager_title_index);
                    }

                    // 修改应用标题栏
                    getActivity().setTitle(title);

                    // 保存标题
                    currentTitle = title;
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    pullRefreshWebView.onRefreshComplete();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.common_function:
                // 跳转到常用功能页
                Intent intent = new Intent(getActivity(), CommonFunctionActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_index, menu);
    }

    @Override
    protected int initRootViewLayoutID() {
        return R.layout.web_view_with_pull_refresh;
    }

    @Override
    protected WebView onInitWebView(View rootView) {
        // 使用带有下拉刷新的WebView
        pullRefreshWebView = (PullToRefreshWebView) rootView.findViewById(R.id
                .with_pull_refresh_webView);

        return pullRefreshWebView.getRefreshableView();
    }

    @Override
    @Deprecated
    protected int initWebViewLayoutID() {
        return R.id.only_webView;
    }

    @Override
    protected String onCreateUrl() {
        return StaticValue.INDEX_URL;
    }

    @Override
    public String getTitle() {
        return currentTitle;
    }

    @Override
    public void setTitle(CharSequence title) {

    }
}
