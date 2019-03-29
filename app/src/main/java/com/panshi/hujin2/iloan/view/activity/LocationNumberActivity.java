package com.panshi.hujin2.iloan.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.adapter.abslistview.CommonAdapter;
import com.panshi.hujin2.iloan.bean.CountryEntity;
import com.panshi.hujin2.iloan.presenter.BasePresenter;
import com.panshi.hujin2.iloan.presenter.LocationPresenter;
import com.panshi.hujin2.iloan.util.StringUtil;

import java.util.List;


/**
 * Created by Administrator on 2018/7/25 0025.
 */

public class LocationNumberActivity extends MVPBaseActivity {
    private ListView mrcvList;
    private LocationPresenter locationPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_num_activity);
        initView();
    }

    private void initView() {
        mrcvList = (ListView) findViewById(R.id.rcv_list);
//       / mrcvList.setLayoutManager(new LinearLayoutManager(this));
        final List<CountryEntity> list= StringUtil.getCountry(this);
        /*CommonAdapter commonAdapter = new CommonAdapter<CountryEntity>(this, R.layout.country_code_item, list) {
            @Override
            protected void convert(ViewHolder holder, CountryEntity o, int position) {
                holder.setText(R.id.county, o.getCountryName());
                holder.setText(R.id.counry_code, o.getCountryCode());

            }
        };*/
        CommonAdapter commonAdapter=new CommonAdapter<CountryEntity>(this,R.layout.country_code_item,list) {
            @Override
            protected void convert(com.panshi.hujin2.iloan.adapter.abslistview.ViewHolder viewHolder, CountryEntity item, int position) {
                viewHolder.setText(R.id.county, item.getCountryName());
                viewHolder.setText(R.id.counry_code, item.getCountryCode());
            }
        };

       /* commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=getIntent();
                intent.putExtra("countryCode",list.get(position).getCountryCode());
                intent.putExtra("countryName",list.get(position).getCountryName());
                setResult(Constant.RESONSE_COUNTRY_CODE,intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });*/
        mrcvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=getIntent();
                intent.putExtra("countryCode",list.get(position).getCountryCode());
                intent.putExtra("countryName",list.get(position).getCountryName());
                setResult(Constant.RESONSE_COUNTRY_CODE,intent);
                finish();
            }
        });
        mrcvList.setAdapter(commonAdapter);


    }

    @Override
    protected BasePresenter oncreatePresenter() {
        return locationPresenter=new LocationPresenter();
    }

    @Override
    protected Toolbar getToolbar() {
        return null;
    }
}
