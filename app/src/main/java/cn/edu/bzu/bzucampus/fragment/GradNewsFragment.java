package cn.edu.bzu.bzucampus.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.edu.bzu.bzucampus.R;
import cn.edu.bzu.bzucampus.activity.GraduDetailsActivity;
import cn.edu.bzu.bzucampus.activity.TopDetailsActivity;
import cn.edu.bzu.bzucampus.adapter.GradNewsAdapter;
import cn.edu.bzu.bzucampus.entity.GradNews;


/**
 * Created by monster on 2015/9/13.
 */
public class GradNewsFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rc_news;
    private ProgressWheel progress;

    private Context mContext;
    private View mView;

    private BmobQuery<GradNews> query;

    private GradNewsAdapter adapter;
    private LinearLayoutManager layoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_topnews,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       this.mView=view;
        progress= (ProgressWheel) mView.findViewById(R.id.progress_wheel);
        progress.setVisibility(View.VISIBLE);  //进度条显示
        checkData();

    }

    /**
     * 从数据库中检查数据源
     */
    private void checkData() {
        query=new BmobQuery<GradNews>();
        query.include("author");
        query.setLimit(40);  //返回40条数据，如果不加上这条语句，默认返回10条数据
       // query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.findObjects(mContext, new FindListener<GradNews>() {
            @Override
            public void onSuccess(List<GradNews> list) {
                initView(mView, list);
            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }

    private void initView(View mView, List<GradNews> list) {
        swipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipeRefreshLayout);
        rc_news= (RecyclerView) mView.findViewById(R.id.rc_news);
        adapter=new GradNewsAdapter(list,mContext);
        rc_news.setAdapter(adapter);

        //设置RecyclerView的布局管理
        layoutManager=new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        rc_news.setLayoutManager(layoutManager);

        //设置RecyclerView的item间的分割线
        // recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

        //设置RecyclerView的动画
        rc_news.setItemAnimator(new DefaultItemAnimator());
        progress.setVisibility(View.INVISIBLE);  //进度条关闭
        //设置监听事件
        adapter.setOnItemClickListener(new GradNewsAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position, View view) {
                View v = rc_news.getChildAt(position);
                TextView tv_objectId = (TextView) v.findViewById(R.id.tv_objectId);
                String objectId = tv_objectId.getText().toString();
                Intent detailIntent = new Intent(mContext, GraduDetailsActivity.class);
                detailIntent.putExtra("objectId", objectId);
                startActivity(detailIntent);
            }

            @Override
            public void OnItemLongClick(int position, View view) {
                Toast.makeText(mContext, "Long click: " + position, Toast.LENGTH_SHORT).show();
            }
        });


        //设置刷新时动画的颜色
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkData();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext=activity;
    }
}
