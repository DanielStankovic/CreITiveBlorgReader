package com.example.daniel.creitiveblorgreader;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;


import java.util.List;

public class BlogItemAdapter extends ArrayAdapter<BlogItem> {

    private Context context;
    private List<BlogItem> blogItemList;
    private int resourceId;





    public BlogItemAdapter(@NonNull Context context, List<BlogItem> blogItemList, int resourceId) {
        super(context,resourceId, blogItemList);
        this.context = context;
        this.blogItemList = blogItemList;
        this.resourceId = resourceId;
    }

    @Override
    public int getCount() {
        return blogItemList.size();
    }

    @Nullable
    @Override
    public BlogItem getItem(int position) {
        return blogItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ItemHolder holder;

        if(row == null){

            LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
            row = layoutInflater.inflate(resourceId, parent, false);

            holder = new ItemHolder();

            holder.titleTextView = row.findViewById(R.id.titleTextView);
            holder.imageView = row.findViewById(R.id.imageView);
            holder.descriptionTextView = row.findViewById(R.id.descriptionTextView);

            row.setTag(holder);

        }else{

            holder = (ItemHolder) row.getTag();

        }

        BlogItem blogItem = blogItemList.get(position);

        holder.titleTextView.setText(blogItem.getTitle());


        Glide.with(row).load(Uri.parse(blogItem.getImageUri())).into(holder.imageView);

        String descriptionFromJson = blogItem.getDescription();
        String description = descriptionFromJson.substring(descriptionFromJson.indexOf(">") +1);
        holder.descriptionTextView.setText(description);

        return row;
    }

    private static class ItemHolder{

        TextView titleTextView;
        ImageView imageView;
        TextView descriptionTextView;

    }
}
