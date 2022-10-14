package com.health.servicecenter.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.servicecenter.R;
import com.health.servicecenter.adapter.MallSortRightListAdapter;
import com.health.servicecenter.contract.MallSortFragmentContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.CategoryChildModel;
import com.health.servicecenter.presenter.MallSortFragmentPresenter;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.utils.SpUtils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MallSortFragment extends BaseFragment implements MallSortFragmentContract.View, BaseAdapter.OnOutClickListener {

    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private RecyclerView recyclerView;
    private int currenPosition;
    private int categoryId;
    private MallSortFragmentPresenter mallSortFragmentPresenter;
    MallSortRightListAdapter adapter;

    public MallSortFragment() {
        // Required empty public constructor
    }

    public static MallSortFragment newInstance(int categoryId, int currenPosition) {
        MallSortFragment fragment = new MallSortFragment();
        Bundle args = new Bundle();
        args.putInt("categoryId", categoryId);
        args.putInt("currenPosition", currenPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getInt("categoryId");
            currenPosition = getArguments().getInt("currenPosition");
        }
        if (categoryId == 0) {
            return;
        }
        mallSortFragmentPresenter = new MallSortFragmentPresenter(getActivity(), this);
        getData(categoryId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mall_sort;
    }

    @Override
    protected void findViews() {
        recyclerView = findViewById(R.id.sort_section_recycler);
    }


    public void getData(int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id + "");
        map.put("userId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
        map.put(Functions.FUNCTION, "9601");
        mallSortFragmentPresenter.getCategoryList(map);
    }

    public void refresh(int position, int id) {
        currenPosition = position;
        getData(id);
    }

    @Override
    public void onGetCategoryListSuccess(List<CategoryChildModel> list) {
        if (list == null || list.size() == 0) {
            showEmpty();
            return;
        }
        virtualLayoutManager = new VirtualLayoutManager(getActivity());
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerView.setLayoutManager(virtualLayoutManager);
        recyclerView.setAdapter(delegateAdapter);
        for (int i = 0; i < list.size(); i++) {
            adapter = new MallSortRightListAdapter();
            adapter.setModel(list.get(i));
            adapter.setOutClickListener(this);
            delegateAdapter.addAdapter(adapter);
        }
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {

    }
}