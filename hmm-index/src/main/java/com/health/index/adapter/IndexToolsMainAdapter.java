package com.health.index.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.example.lib_ShapeView.view.ShapeTextView;
import com.health.index.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.interfaces.IRouterLink;
import com.healthy.library.model.AppIndexTopMarketing;
import com.healthy.library.utils.FrameActivityManager;
import com.healthy.library.utils.MARouterUtils;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.TransformUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建日期：2022/1/22 17:06
 *
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.index.adapter
 * 类说明：
 */

public class IndexToolsMainAdapter extends BaseAdapter<String> {

    List<AppIndexTopMarketing> list = new ArrayList<>();
    String type = "-1";

    public IndexToolsMainAdapter(int viewId) {
        super(viewId);
    }

    public IndexToolsMainAdapter() {
        this(R.layout.item_app_index_tools_adapter_layout);
    }

    public void setList(List<AppIndexTopMarketing> list) {
        this.list.clear();
        this.list.addAll(list);
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int getItemCount() {
        return list.size() > 1 ? 1 : 0;
    }

    public IndexToolsMainAdapter(@NonNull ArrayList<String> list, int viewId) {
        super(list, viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        TextView title = (TextView) holder.itemView.findViewById(R.id.title);
        final GridLayout toolsGrid = (GridLayout) holder.itemView.findViewById(R.id.toolsGrid);
        toolsGrid.removeAllViews();
        if (type.equals("-1")) {
            title.setText("其他工具");
        } else if (type.equals("1")) {
            title.setText("备孕工具");
        } else if (type.equals("2")) {
            title.setText("怀孕工具");
        } else if (type.equals("3")) {
            title.setText("育儿工具");
        }
        if (!ListUtil.isEmpty(list)) {
            toolsGrid.post(new Runnable() {
                @Override
                public void run() {
                    int needSize = list.size();
                    int needFix = 4 - (needSize % 4 == 0 ? 4 : needSize % 4);
                    int mMargin = (int) TransformUtil.dp2px(context, 20);
                    int w = ((ScreenUtils.getScreenWidth(context) - mMargin) / 4);
                    for (int i = 0; i < list.size(); i++) {
                        ViewGroup.LayoutParams layoutParams = new GridLayout.LayoutParams();
                        layoutParams.width = w;
                        final AppIndexTopMarketing model = list.get(i);
                        View view = LayoutInflater.from(context).inflate(R.layout.index_mon_function, toolsGrid, false);
                        AppCompatImageView iv_category = view.findViewById(R.id.iv_category);
                        ShapeTextView stv_function_Subscript = view.findViewById(R.id.stv_function_Subscript);
                        AppCompatTextView tv_category = view.findViewById(R.id.tv_category);
                        GlideCopy.with()
                                .load(model.darkIconUrl)
                                .error(R.drawable.img_1_1_default2)
                                .placeholder(R.drawable.img_1_1_default2)
                                .into(iv_category);
                        if (TextUtils.isEmpty(model.subscript)) {
                            stv_function_Subscript.setVisibility(View.INVISIBLE);
                        } else {
                            stv_function_Subscript.setText(model.subscript);
                            stv_function_Subscript.setVisibility(View.VISIBLE);
                        }
                        tv_category.setText(model.settingName);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MARouterUtils.passToTarget(context, model);
                                if(model.initialName.contains("热聊")||model.initialName.equals("母婴商城")){
                                    try {
                                        FrameActivityManager.instance().finishOthersActivity(Class.forName("com.health.client.activity.MainActivity"));
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        toolsGrid.addView(view, layoutParams);
                    }
                }
            });
        }
    }
}
