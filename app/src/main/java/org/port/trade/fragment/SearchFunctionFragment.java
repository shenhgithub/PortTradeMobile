package org.port.trade.fragment;
/**
 * Created by 超悟空 on 2015/1/12.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import org.mobile.model.operate.BackHandle;
import org.port.trade.R;
import org.port.trade.activity.FunctionListActivity;
import org.port.trade.util.StaticValue;

/**
 * 查询功能页的Fragment片段
 *
 * @author 超悟空
 * @version 1.0 2015/1/22
 * @since 1.0
 */
public class SearchFunctionFragment extends Fragment implements BackHandle {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 当前功能片段布局
        View rootView = inflater.inflate(R.layout.function_fragment, container, false);

        // 初始化功能布局
        initGridView(rootView);

        return rootView;
    }

    /**
     * 初始化功能表格布局
     *
     * @param rootView 根布局
     */
    private void initGridView(View rootView) {

        // 得到功能id对
        int[] imageButtonIds = getImageButtonId();
        int[] functionIds = getFunctionId();

        // 实现的功能数
        int functionCount = imageButtonIds.length >= functionIds.length ? functionIds.length : imageButtonIds.length;

        // 生成功能按钮
        for (int i = 0; i < functionCount; i++) {
            createImageButton(imageButtonIds[i], functionIds[i], rootView);
        }
    }

    /**
     * 获取功能按钮ID，与{@link #getFunctionId()}对应
     *
     * @return ID数组
     */
    private int[] getImageButtonId() {
        return new int[]{R.id.function_imageButton_1 , R.id.function_imageButton_2 , R.id.function_imageButton_3 , R.id.function_imageButton_4 , R.id.function_imageButton_5 , R.id.function_imageButton_6};
    }

    /**
     * 获取功能ID，与{@link #getImageButtonId()}对应
     *
     * @return ID数组
     */
    private int[] getFunctionId() {
        return new int[]{1 , 2 , 3 , 4 , 5 , 6};
    }

    /**
     * 生成一个功能ImageButton
     *
     * @param imageButtonId 按钮ID
     * @param functionId    功能ID
     * @param rootView      根布局
     */
    private void createImageButton(int imageButtonId, final int functionId, View rootView) {

        // 找到一个按钮
        ImageButton imageButton = (ImageButton) rootView.findViewById(imageButtonId);

        // 添加点击事件
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FunctionListActivity.class);
                // 添加功能ID
                intent.putExtra(StaticValue.FUNCTION_BUTTON_ID, functionId);
                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
