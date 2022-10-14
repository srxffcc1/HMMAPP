package com.health.second.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.second.R;
import com.health.second.model.SecondType;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.widget.AutoClickImageView;

import java.util.List;

public class SecondAllTypeAdapter extends BaseAdapter<SecondType> {




    @Override
    public int getItemViewType(int position) {
        return 29;
    }

    public SecondAllTypeAdapter() {
        this(R.layout.item_second_all_function_bar);
    }

    private SecondAllTypeAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        SecondType secondType=getDatas().get(i);

         ConstraintLayout parentCategory;
         TextView functionBigTitle;
         LinearLayout functionLL;
         GridLayout functionGrid;
        parentCategory = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.parent_category);
        functionBigTitle = (TextView) baseHolder.itemView. findViewById(R.id.functionBigTitle);
        functionLL = (LinearLayout)  baseHolder.itemView.findViewById(R.id.functionLL);
        functionGrid = (GridLayout) baseHolder.itemView. findViewById(R.id.functionGrid);
        functionBigTitle.setText(secondType.name);
        if(secondType.secondTypeMenuList!=null){
            addFunctions(context, functionGrid, secondType.secondTypeMenuList);
        }
    }

    private void addFunctions(final Context context, final GridLayout gridLayout, final List<SecondType.SecondTypeMenu> modelList) {
        gridLayout.post(new Runnable() {
            private void initView() {

            }
            @Override
            public void run() {
                gridLayout.removeAllViews();
                int column = 5;
                int needSize = modelList.size();
                if (modelList.size() > 6) {
//                    needSize = 6;
                }
                int needfixzzz = column - (needSize % column == 0 ? column : needSize % column);
                ViewGroup.LayoutParams gridlayoutparm = gridLayout.getLayoutParams();
                gridLayout.setLayoutParams(gridlayoutparm);
                gridLayout.setColumnCount(column);
                for (int i = 0; i < needSize; i++) {
                    final SecondType.SecondTypeMenu secondTypeMenu=modelList.get(i);
                    View view = LayoutInflater.from(context).inflate(R.layout.item_second_function_menu, gridLayout, false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        GridLayout.LayoutParams param = new GridLayout.LayoutParams(GridLayout.spec(
                                GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                                GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f));
                        view.setLayoutParams(param);
                    }
                    TextView tvCategory;
                    AutoClickImageView ivCategory;
                    ivCategory = (AutoClickImageView) view.findViewById(R.id.iv_category);
                    tvCategory = (TextView) view.findViewById(R.id.tv_category);


                    tvCategory.setText(secondTypeMenu.name);
                    com.healthy.library.businessutil.GlideCopy.with(context)
                            .load(secondTypeMenu.iconUrl)
                            .placeholder(R.drawable.second_all_type)
                            .error(R.drawable.second_all_type)

                            .into(ivCategory);
                    ivCategory.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(moutClickListener!=null){
                                moutClickListener.outClick("服务分类",secondTypeMenu);
                            }
                        }
                    });
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(moutClickListener!=null){
                                moutClickListener.outClick("服务分类",secondTypeMenu);
                            }
                        }
                    });
                    gridLayout.addView(view);
                }
                if (needfixzzz > 0) {
                    for (int i = 0; i < needfixzzz; i++) {
                        View view = LayoutInflater.from(context).inflate(R.layout.item_second_function_menu, gridLayout, false);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            GridLayout.LayoutParams param = new GridLayout.LayoutParams(GridLayout.spec(
                                    GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                                    GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f));
                            view.setLayoutParams(param);
                        }
                        view.setVisibility(View.INVISIBLE);
                        gridLayout.addView(view);
                    }
                }
            }
        });
    }


    private void initView() {


    }
}
