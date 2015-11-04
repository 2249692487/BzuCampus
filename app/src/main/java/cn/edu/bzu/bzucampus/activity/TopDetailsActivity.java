package cn.edu.bzu.bzucampus.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.GetListener;
import cn.edu.bzu.bzucampus.BaseActivity;
import cn.edu.bzu.bzucampus.R;
import cn.edu.bzu.bzucampus.entity.TopNews;
import cn.edu.bzu.bzucampus.util.ImageLoader;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 新闻内容详情
 * Created by monster on 2015/10/4.
 */
public class TopDetailsActivity extends BaseActivity {

    private Toolbar mToolbar ;
    private String objectId;
    private CircleImageView iv_userPhoto;
    private TextView tv_name,tv_date,tv_title,tv_content;
    private String fileUrl;
    private ImageView iv_newsImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        objectId=getIntent().getStringExtra("objectId");
      //  toast(""+objectId);
        initToolBar();
        initView();
        queryData();
    }

    /**
     * 查询相应id的数据
     */
    private void queryData() {
        BmobQuery<TopNews> query = new BmobQuery<TopNews>();
        query.include("author"); //包含作者
        query.getObject(TopDetailsActivity.this, objectId, new GetListener<TopNews>() {
            @Override
            public void onSuccess(TopNews news) {
                //得到对应的值
                fileUrl = news.getAuthor().getUserPhoto().getFileUrl(TopDetailsActivity.this);
                iv_userPhoto.setTag(fileUrl);
                new ImageLoader().showImageByThread(iv_userPhoto, fileUrl);
                tv_name.setText(news.getAuthor().getNick());
                tv_date.setText(news.getUpdatedAt());
                tv_title.setText("【"+news.getTitle()+"】");
                tv_content.setText(news.getContent());
                BmobFile file = news.getNewsImg();
                if(file!=null){
                    iv_newsImg.setVisibility(View.VISIBLE);
                    String fileUrl  = file.getFileUrl(TopDetailsActivity.this);
                    iv_newsImg.setTag(fileUrl);
                    new ImageLoader().showImageByThread(iv_newsImg,fileUrl);
                }
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(TopDetailsActivity.this,"查询数据失败"+s,Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 初始化视图
     */
    private void initView() {
        iv_userPhoto= (CircleImageView) findViewById(R.id.iv_userPhoto);
        tv_name= (TextView) findViewById(R.id.tv_name);
        tv_date= (TextView) findViewById(R.id.tv_date);
        tv_title= (TextView) findViewById(R.id.tv_title);
        tv_content= (TextView) findViewById(R.id.tv_content);
        iv_newsImg= (ImageView) findViewById(R.id.iv_newsImg);
    }

    /**
     * 初始化ToolBar
     */
    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("新闻详情");  //设置标题
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_menu_back);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.item_test:
                        Toast.makeText(TopDetailsActivity.this, "测试Toast", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id){
//            case R.id.item_test:
//                Toast.makeText(NewsInformActivity.this,"测试Toast",Toast.LENGTH_SHORT).show();
//            break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
