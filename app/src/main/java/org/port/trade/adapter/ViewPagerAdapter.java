package org.port.trade.adapter;
/**
 * Created by 超悟空 on 2015/1/12.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.mobile.util.ContextUtil;
import org.port.trade.R;
import org.port.trade.fragment.SearchFunctionFragment;
import org.port.trade.fragment.IndexFragment;
import org.port.trade.fragment.MessageFragment;
import org.port.trade.fragment.MineFragment;

/**
 * 主界面ViewPager适配器
 *
 * @author 超悟空
 * @version 1.0 2015/1/12
 * @since 1.0
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new IndexFragment();
            case 1:
                return new SearchFunctionFragment();
            case 2:
                return new MessageFragment();
            case 3:
                return new MineFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return ContextUtil.getContext().getString(R.string.pager_title_index);
            case 1:
                return ContextUtil.getContext().getString(R.string.pager_title_function);
            case 2:
                return ContextUtil.getContext().getString(R.string.pager_title_message);
            case 3:
                return ContextUtil.getContext().getString(R.string.pager_title_other);
        }
        return null;
    }

    /**
     * 获取pager的标题图片ID数组
     *
     * @return 图片ID数组
     */
    public int[] getImageIds() {
        return new int[]{R.drawable.index_icon , R.drawable.function_icon , R.drawable.message_icon , R.drawable.user_center_icon};
    }
}
