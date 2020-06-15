package com.example.qunxin.erp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.qunxin.erp.R;

import java.util.ArrayList;
import java.util.List;

import swipedelete.ListViewAdapter;
import swipedelete.TestBean;
import swipedelete.utils.CommonAdapter;
import swipedelete.utils.ViewHolder;
import swipedelete.view.SwipeMenuLayout;

/**
 * Created by qunxin on 2019/7/31.
 */

public class IndexFragment_Previous extends Fragment {

    View view;
    private ListView listView;
    private ListViewAdapter adapter;
    private List<TestBean> lists;
    private SwipeMenuLayout swipeMenuLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_index_previous,container,false);
        indexTodo();

        return  view;
    }


    void indexTodo(){
        listView = (ListView) view.findViewById(R.id.indexListView);
        //模拟数据
        lists = new ArrayList<>();
        for(int i=0;i<20;i++){
            TestBean bean = new TestBean();
            bean.setName("采购历史"+i);
            //bean.setInfo("向供应商预定进货商品");
            bean.setImgRes(R.mipmap.caigoulishi);
            lists.add(bean);
        }
        //自己测试写的ListViewAdapter
        /*adapter = new ListViewAdapter(this,lists);
        listView.setAdapter(adapter);*/

        listView.setAdapter(new CommonAdapter<TestBean>(getContext(),lists, R.layout.item_index_previous) {

            @Override
            public void convert(final ViewHolder holder, TestBean testBean, final int position, View convertView) {
                holder.setText(R.id.listview_tv,testBean.getName());
                holder.setImageResource(R.id.listview_iv,testBean.getImgRes());

                //可以根据自己需求设置一些选项(这里设置了IOS阻塞效果以及item的依次左滑、右滑菜单)
                ((SwipeMenuLayout)holder.getConvertView()).setIos(true).setLeftSwipe(true);
                //监听事件
                holder.setOnClickListener(R.id.ll_content, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(MainActivity.this,"点击了："+position, Toast.LENGTH_SHORT).show();
                    }
                });
                holder.setOnClickListener(R.id.btn_zd, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(MainActivity.this,"点击了置顶选项", Toast.LENGTH_SHORT).show();
                        //在ListView里，点击侧滑菜单上的选项时，如果想让侧滑菜单同时0关闭，调用这句话
                        ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                        TestBean bean=lists.get(position);
                        lists.add(0,bean);
                        lists.remove(position+1);
                        notifyDataSetChanged();
                    }
                });
                holder.setOnClickListener(R.id.btn_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // Toast.makeText(MainActivity.this,"点击了删除选项", Toast.LENGTH_SHORT).show();
                        //在ListView里，点击侧滑菜单上的选项时，如果想让侧滑菜单同时关闭，调用这句话
                        ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                        //删除操作
                        lists.remove(position);
                        notifyDataSetChanged();
                    }
                });
                //长按监听
                holder.setOnLongClickListener(R.id.ll_content, new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                       // Toast.makeText(MainActivity.this,"正在进行长按操作", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
            }
        });
    }
}
