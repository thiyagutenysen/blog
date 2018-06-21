package com.awesome.adams.com.Data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.awesome.adams.com.Model.Blog;
import com.awesome.adams.blog.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.Viewholder> {

    private Context context;
    private List<Blog> blogList;

    public BlogRecyclerAdapter(Context context, List<Blog> blogList) {
        this.context = context;
        this.blogList = blogList;
    }

    @NonNull
    @Override
    public BlogRecyclerAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row,parent,false);
        return new Viewholder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        Blog blog = blogList.get(getItemCount()-position-1);
        Log.d("soze",String.valueOf(getItemCount()));
        Log.d("position",String.valueOf(position));
        String imageurl = null;

        holder.title.setText(blog.getTitle());
        holder.desc.setText(blog.getDesc());

        DateFormat dateFormat = DateFormat.getDateInstance();
        String formatteddate = dateFormat.format(new Date(Long.valueOf(blog.getTimestamp())).getTime());

        holder.timestamp.setText(formatteddate);

        imageurl = blog.getImage();

        //use picasso library to load images from its url
        Picasso.with(context)
                .load(imageurl)
                .into(holder.image);


    }



    @Override
    public int getItemCount() {
        return blogList.size();

    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView title,desc,timestamp;
        private ImageView image;
        String userId;
        public Viewholder(View itemView,Context ctx) {
            super(itemView);

            context = ctx;

            title = (TextView) itemView.findViewById(R.id.posttitlelist);
            desc = (TextView) itemView.findViewById(R.id.postdescriptionlist);
            timestamp = (TextView) itemView.findViewById(R.id.timestamplist);
            image = (ImageView) itemView.findViewById(R.id.postimagelist);

            userId = null;
        }
    }
}
