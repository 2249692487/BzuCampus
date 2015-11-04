package cn.edu.bzu.bzucampus.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.edu.bzu.bzucampus.R;
import cn.edu.bzu.bzucampus.entity.GradNews;
import cn.edu.bzu.bzucampus.util.ImageLoader;

/**
 * Created by monster on 2015/11/4.
 */
public class GradNewsAdapter extends RecyclerView.Adapter<GradNewsHolder> {

    private List<GradNews> mData;
    private Context context;
    private LayoutInflater mInflater;
    /**
     * 声明一个接口，用于实现点击事件
     */
    public interface  OnItemClickListener{
        void OnItemClick(int position,View view);
        void OnItemLongClick(int position,View view);
    }

    private OnItemClickListener mOnItemClickListener;

    public  void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener=listener;
    }


    public GradNewsAdapter(List<GradNews> mData, Context context) {
        this.mData=mData;
        this.context=context;
        mInflater=LayoutInflater.from(context);
    }


    @Override
    public GradNewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.topnews_item,parent,false);
        GradNewsHolder viewHolder=new GradNewsHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final GradNewsHolder holder, int position) {
        holder.tv_nick.setText(mData.get(position).getAuthor().getNick());//昵称
        holder.tv_date.setText(mData.get(position).getCreatedAt()); //创建时间
        String url=mData.get(position).getAuthor().getUserPhoto().getFileUrl(context);
        holder.iv_user_img.setTag(url);
        new ImageLoader().showImageByAsyncTask(holder.iv_user_img,url);
        holder.tv_news_title.setText(mData.get(position).getTitle()); //内容
        holder.tv_objectId.setText(mData.get(position).getObjectId());

        if (mOnItemClickListener!=null){

            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int LayoutPosition=holder.getLayoutPosition(); //得到布局的position
                    mOnItemClickListener.OnItemClick(LayoutPosition,holder.itemView);

                }
            });

            //longclickListener
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int LayoutPosition=holder.getLayoutPosition(); //得到布局的position
                    mOnItemClickListener.OnItemLongClick(LayoutPosition,holder.itemView);
                    return false;
                }
            });
        }
        }


    @Override
    public int getItemCount() {
        return mData.size();
    }
}

class GradNewsHolder extends RecyclerView.ViewHolder{

    TextView tv_nick;
    ImageView iv_user_img;
    TextView tv_news_title,tv_date;
    TextView tv_objectId;

    public GradNewsHolder(View itemView) {
        super(itemView);
        /**初始化控件**/
        tv_nick = (TextView) itemView.findViewById(R.id.tv_nick);
        iv_user_img= (ImageView) itemView.findViewById(R.id.iv_user_img);
        tv_date = (TextView) itemView.findViewById(R.id.tv_date);
        tv_news_title= (TextView) itemView.findViewById(R.id.tv_news_title);
        tv_objectId= (TextView) itemView.findViewById(R.id.tv_objectId);
    }
}
