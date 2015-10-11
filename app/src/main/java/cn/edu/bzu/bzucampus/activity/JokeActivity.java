package cn.edu.bzu.bzucampus.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import cn.edu.bzu.bzucampus.R;
import cn.edu.bzu.bzucampus.adapter.JokeRecyclerAdapter;
import cn.edu.bzu.bzucampus.entity.Joke;

/**
 * 笑话大全
 * @tag  http://apistore.baidu.com/apiworks/servicedetail/864.html
 * @test http://apistore.baidu.com/astore/toolshttpproxy?apiId=usy0yw&isAworks=1
 * Created by monster on 2015/10/11.
 */
public class JokeActivity extends AppCompatActivity {
    private Toolbar mToolBar;
    private RecyclerView mRecyclerView;
    private JokeRecyclerAdapter mAdapter;
    private List<Joke> mList;
    private LinearLayoutManager layoutManager;

    private String httpUrl = "http://apis.baidu.com/showapi_open_bus/showapi_joke/joke_text";
    private String httpArg = "page=1";

    private final static int MSG_SUCCESS = 0;
    private final static int MSG_FAILURE = 1;

    private Thread mThread;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_SUCCESS:
                    List<Joke> list = (List<Joke>) msg.obj;
                    mAdapter=new JokeRecyclerAdapter(list,JokeActivity.this);
                    mRecyclerView.setAdapter(mAdapter);

                    //设置RecyclerView的布局管理
                    layoutManager=new LinearLayoutManager(JokeActivity.this,LinearLayoutManager.VERTICAL,false);
                    mRecyclerView.setLayoutManager(layoutManager);

                    //设置RecyclerView的item间的分割线
                    // recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

                    //设置RecyclerView的动画
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    break;
                case MSG_FAILURE:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);
        initView();

        if(mThread==null){
            mThread=new Thread(runnable);
            mThread.start();
        }

    }

    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            mList=new ArrayList<>();
            String jsonResult = request(httpUrl, httpArg);
            //Log.e("TAG",jsonResult);
            if(jsonResult!=null){
                /**
                 * Json数据解析
                 * 使用方式:Gson
                 */
                JsonParser parser=new JsonParser(); //创建Json解析器
                JsonObject jsonObject= (JsonObject) parser.parse(jsonResult);//解析成json对象
                int resultCode = jsonObject.get("showapi_res_code").getAsInt();
                if(resultCode==0){
                    //正确解析
                    JsonObject resultBody=jsonObject.get("showapi_res_body").getAsJsonObject();
                    JsonArray resultArray=resultBody.get("contentlist").getAsJsonArray();
                    for (int i=0;i<resultArray.size();i++){
                        Joke joke=new Joke();
                        JsonObject subObject = resultArray.get(i).getAsJsonObject();
                        joke.setTitle(subObject.get("title").getAsString());
                        joke.setCt(subObject.get("ct").getAsString());
                        joke.setText(subObject.get("text").getAsString());
                        joke.setType(subObject.get("type").getAsString());
                        mList.add(joke);
                    }
                }else{
                    Log.e("ERROR",""+resultCode);
                }

                mHandler.obtainMessage(MSG_SUCCESS,mList).sendToTarget();
            }else {
                mHandler.obtainMessage(MSG_FAILURE).sendToTarget();;
            }
        }
    };

    /**
     * 得到json数据
     * @param httpUrl 请求接口
     * @param httpArg 参数
     * @return json 数据
     */
    private String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey",  "3db47c23d875eeb385f62e7c334c2c35");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mRecyclerView= (RecyclerView) findViewById(R.id.jokeRecy);
        mToolBar= (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitle("笑话大全");
        mToolBar.setNavigationIcon(R.mipmap.icon_back);
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}



