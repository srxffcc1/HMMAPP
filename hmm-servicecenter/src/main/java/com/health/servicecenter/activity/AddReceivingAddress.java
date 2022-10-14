package com.health.servicecenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.servicecenter.R;
import com.health.servicecenter.contract.AddAddressContract;
import com.healthy.library.model.AddressListModel;
import com.healthy.library.model.AddressModel;
import com.health.servicecenter.presenter.AddAddressPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.SpKey;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.CheckUtils;
import com.healthy.library.utils.SpUtils;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;

import java.util.HashMap;
import java.util.Map;

@Route(path = ServiceRoutes.SERVICE_ADD_ADDRESS)
public class AddReceivingAddress extends BaseActivity implements AddAddressContract.View {
    @Autowired
    boolean isFirstAddress;

    private ImageView img_back, img_to_map;
    private EditText user_name_edit, user_phone_edit, user_house_number_edit;
    private TextView user_address_edit, submit_address_txt;
    private Switch defaultSwitch;
    private AddressModel addressModel;
    private Map<String, Object> map = new HashMap<>();
    private AddAddressPresenter addAddressPresenter;
    private int type = 0;//用来区分是新增地址还是修改地址
    private AddressListModel addressListModel;
    private Boolean isDefault = false;//是否是默认地址
    private TextView nearbyStoreTitle;
    private TextView delete_address;
    private String addressHint = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_receivingaddress;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        img_to_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddReceivingAddress.this, AddressSearch.class);
                if (type == 1) {
                    intent.putExtra("model", addressListModel);
                }
                startActivityForResult(intent, 1);
            }
        });
        user_address_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddReceivingAddress.this, AddressSearch.class);
                if (type == 1) {
                    intent.putExtra("model", addressListModel);
                }
                startActivityForResult(intent, 1);
            }
        });
        submit_address_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    if (type == 1) {
                        update();
                    } else {
                        submit();
                    }
                }

            }
        });
        user_name_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 10) {
                    showToast("收货人名称不能超过10个字符");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        user_house_number_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 20) {
                    showToast("门牌号不能超过20个字符");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        defaultSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!buttonView.isPressed()) {
                    return;
                }
                isDefault = isChecked;
            }
        });
        if (isFirstAddress) {
            defaultSwitch.setChecked(true);
            isDefault = true;
        }
        delete_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addressListModel != null) {
                    isAgree(addressListModel.getId() + "");
                }
            }
        });
    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(user_name_edit.getText().toString())) {
            showToast("请填写收货人！");
            return false;
        }
        if (TextUtils.isEmpty(user_address_edit.getText().toString())) {
            showToast("请选择收货地址！");
            return false;
        }
        if (TextUtils.isEmpty(user_phone_edit.getText().toString())) {
            showToast("请填手机号码！");
            return false;
        }
        if (TextUtils.isEmpty(user_house_number_edit.getText().toString())) {
            showToast("请填写详细门牌号！");
            return false;
        }

        if (!CheckUtils.checkPhone(user_phone_edit.getText().toString())) {
            showToast("请输入正确的手机号！");
            return false;
        }
        return true;
    }

    private void submit() {
        String houseNum = user_house_number_edit.getText().toString();
        if (type == 1) {
            map.put("id", addressListModel.getId() + "");
        } else {
            map.put("id", "");
        }
        map.put("consigneeName", user_name_edit.getText().toString());
        map.put("configneePhone", user_phone_edit.getText().toString());
        map.put("province", addressModel.getProvince());
        map.put("provinceName", addressModel.getProvinceName());
        map.put("city", addressModel.getCity());
        map.put("cityName", addressModel.getCityName());
        map.put("district", addressModel.getDistrict());
        map.put("districtName", addressModel.getDistrictName());
        map.put("address", addressHint);
        if (houseNum.contains(addressHint)){
            map.put("houseNum", houseNum.substring(addressHint.length()));
        }else{
            map.put("houseNum", houseNum);
        }
        map.put("lat", addressModel.getLat());
        map.put("lng", addressModel.getLng());
        if (isDefault) {
            map.put("orderBy", "-1");//-1默认地址 0正常
        } else {
            map.put("orderBy", "0");
        }
        addAddressPresenter.putAddress(map);
    }

    private void update() {
        if (addressModel == null) {
            String houseNum = user_house_number_edit.getText().toString();
            map.put("id", addressListModel.getId() + "");
            map.put("consigneeName", user_name_edit.getText().toString());
            map.put("configneePhone", user_phone_edit.getText().toString());
            map.put("province", addressListModel.getProvince());
            map.put("provinceName", addressListModel.getProvinceName());
            map.put("city", addressListModel.getCity());
            map.put("cityName", addressListModel.getCityName());
            map.put("district", addressListModel.getDistrict());
            map.put("districtName", addressListModel.getDistrictName());
            map.put("address", addressHint);
            if (houseNum.contains(addressHint)){
                map.put("houseNum", houseNum.substring(addressHint.length()));
            }else{
                map.put("houseNum", houseNum);
            }
            map.put("lat", addressListModel.getLat());
            map.put("lng", addressListModel.getLng());
            if (isDefault) {
                map.put("orderBy", "-1");//-1默认地址 0正常
            } else {
                map.put("orderBy", "0");
            }
            addAddressPresenter.putAddress(map);
        } else {
            submit();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    return;
                }
                addressModel = (AddressModel) data.getSerializableExtra("model");
                if (addressModel != null) {
                    user_address_edit.setText(addressModel.getProvinceName() + addressModel.getCityName() + addressModel.getDistrictName());
                    addressHint = addressModel.getAddress();
                    user_house_number_edit.setHint(addressModel.getAddress() + "");
                    user_house_number_edit.setText(addressModel.getAddress() + "");
                }
            }
        }
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
        addAddressPresenter = new AddAddressPresenter(this, this);
        img_back = findViewById(R.id.img_back);
        img_to_map = findViewById(R.id.img_to_map);
        user_name_edit = findViewById(R.id.user_name_edit);
        user_phone_edit = findViewById(R.id.user_phone_edit);
        user_address_edit = findViewById(R.id.user_address_edit);
        user_house_number_edit = findViewById(R.id.user_house_number_edit);
        submit_address_txt = findViewById(R.id.submit_address_txt);
        defaultSwitch = findViewById(R.id.defaultSwitch);
        delete_address = findViewById(R.id.delete_address);

        if (getIntent().getSerializableExtra("listModel") != null) {
            type = getIntent().getIntExtra("type", 0);//1表示修改地址,否则就是新增地址
            addressListModel = (AddressListModel) getIntent().getSerializableExtra("listModel");
            user_name_edit.setText(addressListModel.getConsigneeName());
            user_phone_edit.setText(addressListModel.getConfigneePhone());
            user_address_edit.setText(addressListModel.getProvinceName()+addressListModel.getCityName()+addressListModel.getDistrictName());
            user_house_number_edit.setText(addressListModel.getAddress()+addressListModel.getHouseNum());
            addressHint = addressListModel.getAddress();
            if (addressListModel.getOrderBy() == -1) {
                defaultSwitch.setChecked(true);
                isDefault = true;
            }
            if (type == 1) {
                nearbyStoreTitle.setText("修改地址");
                delete_address.setVisibility(View.VISIBLE);
            } else {
                nearbyStoreTitle.setText("新增地址");
                delete_address.setVisibility(View.GONE);

            }

        } else {
            user_phone_edit.setText(SpUtils.getValue(this, SpKey.PHONE));
            //user_phone_edit.setHint(SpUtils.getValue(this, SpKey.PHONE).replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
        }

    }

    @Override
    public void onPutAddressSuccess() {
        if (type == 1) {
            showToast("修改成功");
        }else{
            showToast("添加成功");
        }

        finish();
    }

    @Override
    public void onDeleteAddressSuccess() {
        showToast("删除成功");
        finish();
    }

    private void initView() {
        nearbyStoreTitle = (TextView) findViewById(R.id.nearby_store_title);
    }

    private void isAgree(final String id) {
        StyledDialog.init(this);
        StyledDialog.buildIosAlert("提示", "\n" + "是否确定删除该地址？", new MyDialogListener() {
            @Override
            public void onFirst() {
            }

            @Override
            public void onThird() {
                super.onThird();
            }

            @Override
            public void onSecond() {
                addAddressPresenter.deleteAddress(id);
            }
        }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("取消", "确定").show();
    }

}