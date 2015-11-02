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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.edu.bzu.bzucampus.R;
import cn.edu.bzu.bzucampus.activity.DetailsActivity;
import cn.edu.bzu.bzucampus.adapter.TopNewsRecyclerViewAdapter;
import cn.edu.bzu.bzucampus.entity.TopNews;


/**
 * Created by monster on 2015/9/13.
 */
public class TopNewsFragment extends Fragment {

    private RecyclerView mRrecyclerView;
    private TopNewsRecyclerViewAdapter mAdapter;
    private List<TopNews> mList = new ArrayList<>();
    private Context mContext;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager layoutManager;
    private View mView;
    private BmobQuery<TopNews> query;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_topnews,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mView=view;
        checkData();
    }

    /**
     * 从数据库中取得数据
     */
    private void checkData() {
        query=new BmobQuery<TopNews>();
        query.include("author");
                /*
                   第一次进入应用的时候，设置其查询的缓存策略为CACHE_ELSE_NETWORK,当用户执行上拉或者下拉刷新操作时，设置查询的缓存策略为NETWORK_ELSE_CACHE
                 */
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);

        query.setLimit(40);  //返回40条数据，如果不加上这条语句，默认返回10条数据
        query.findObjects(mContext, new FindListener<TopNews>() {

            @Override
            public void onSuccess(List<TopNews> list) {
               initView(mView,list);
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(mContext,"发生错误"+s,Toast.LENGTH_SHORT).show();
            }
        });
    }



    /**
     * 初始化视图
     */
    private void initView(View view,List<TopNews> list) {
        mRrecyclerView= (RecyclerView) view.findViewById(R.id.rc_topNews);
        mAdapter=new TopNewsRecyclerViewAdapter(list,mContext);
        mRrecyclerView.setAdapter(mAdapter);

        //设置RecyclerView的布局管理
        layoutManager=new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        mRrecyclerView.setLayoutManager(layoutManager);

        //设置RecyclerView的item间的分割线
        // recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

        //设置RecyclerView的动画
        mRrecyclerView.setItemAnimator(new DefaultItemAnimator());

        // boolean isInCache = query.hasCachedResult(mContext,TopNews.class); //检查缓存数据

        //设置监听事件
        mAdapter.setOnItemClickListener(new TopNewsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position, View view) {
                View v = mRrecyclerView.getChildAt(position);
                TextView tv_objectId = (TextView) v.findViewById(R.id.tv_objectId);
                String objectId=tv_objectId.getText().toString();
                Intent detailIntent = new Intent(mContext, DetailsActivity.class);
                detailIntent.putExtra("objectId",objectId);
                startActivity(detailIntent);
            }

            @Override
            public void OnItemLongClick(int position, View view) {
                Toast.makeText(mContext, "Long click: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 借助SwipeRefreshLayout实现下拉刷新的操作
         */
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
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

        /**
         * 滚动上拉刷新
         */
        mRrecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 4 && dy > 0) {

                    //TODO 在这个地方控制上啦刷新是否有新的内容产生
                    boolean isLoadingMore=true;
                    if(isLoadingMore){
                        Toast.makeText(mContext,"无更多内容",Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(mContext,"有更多内容",Toast.LENGTH_SHORT).show();
                        isLoadingMore = false;
                    }
                }

            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext=activity;
    }
}
