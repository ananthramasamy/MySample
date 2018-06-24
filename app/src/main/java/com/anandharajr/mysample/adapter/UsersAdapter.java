package com.anandharajr.mysample.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.anandharajr.mysample.R;
import com.anandharajr.mysample.interfaces.OnItemUsersClickListener;
import com.anandharajr.mysample.model.Datum;
import com.squareup.picasso.Picasso;

import java.util.List;
/**
 * Created by anandharajr on 23-06-18.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private final List<Datum> items;
    private final OnItemUsersClickListener listener;
    private Context mContext;

    public UsersAdapter(Context mContext, List<Datum> items, OnItemUsersClickListener listener) {
        this.mContext = mContext;
        this.items = items;
        this.listener = listener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

      class ViewHolder extends RecyclerView.ViewHolder {

        private TextView FirstNameTV, LastNameTV;
        private ImageView AvatarIV;
        private Button ViewMoreBTN;


        private ViewHolder(View itemView) {
            super(itemView);
            FirstNameTV = itemView.findViewById(R.id.first_name_tv);
            LastNameTV = itemView.findViewById(R.id.last_name_tv);
            AvatarIV = itemView.findViewById(R.id.avatar_iv);
            ViewMoreBTN=itemView.findViewById(R.id.view_more_btn);

        }

        private void bind(final Datum item, final OnItemUsersClickListener listener) {
            FirstNameTV.setText(item.getFirst_name());
            LastNameTV.setText(item.getLast_name());
            if (item.getBitmapAvatar() == null) {

               /* Glide.with(mContext)
                        .load(item.getAvatar())
                        .apply(RequestOptions.circleCropTransform())
                        .into(AvatarIV);*/
                Picasso.with(mContext).load(item.getAvatar())
                        .fit().centerInside()
                        .placeholder(R.drawable.ic_autore_new)
                        .error(R.drawable.ic_autore_new)
                        .into(AvatarIV);
            } else {
                AvatarIV.setImageBitmap(item.getBitmapAvatar());
            }
            ViewMoreBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });

        }
    }


}