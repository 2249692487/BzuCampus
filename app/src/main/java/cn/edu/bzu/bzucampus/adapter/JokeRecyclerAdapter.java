package cn.edu.bzu.bzucampus.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.edu.bzu.bzucampus.R;
import cn.edu.bzu.bzucampus.activity.JokeActivity;
import cn.edu.bzu.bzucampus.entity.Joke;

/**
 * Created by monster on 2015/10/11.
 */
public class JokeRecyclerAdapter extends RecyclerView.Adapter<JokeViewHolder>{

    private LayoutInflater mInflate;
    private List<Joke> mList;
    private Context mContext;

    public JokeRecyclerAdapter(List<Joke> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
        mInflate=LayoutInflater.from(mContext);
    }

    @Override
    public JokeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflate.inflate(R.layout.joke_item,parent,false);
        JokeViewHolder jokeViewHolder=new JokeViewHolder(view);
        return jokeViewHolder;
    }

    @Override
    public void onBindViewHolder(JokeViewHolder holder, int position) {
        holder.tv_ct.setText(mList.get(position).getCt());
        holder.tv_title.setText(mList.get(position).getTitle());
        holder.tv_text.setText(mList.get(position).getText());
        holder.tv_type.setText(mList.get(position).getType());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}

class JokeViewHolder extends RecyclerView.ViewHolder{
    TextView tv_ct,tv_title,tv_text,tv_type;

    public JokeViewHolder(View itemView) {
        super(itemView);
        tv_ct= (TextView) itemView.findViewById(R.id.tv_ct);
        tv_title= (TextView) itemView.findViewById(R.id.tv_title);
        tv_text= (TextView) itemView.findViewById(R.id.tv_text);
        tv_type= (TextView) itemView.findViewById(R.id.tv_type);
    }
}
