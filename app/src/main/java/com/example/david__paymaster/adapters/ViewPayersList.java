package com.example.david__paymaster.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.david__paymaster.CollectTaxActivity;
import com.example.david__paymaster.RegisterPayersActivity;
import com.example.david__paymaster.SinglePayerActivity;
import com.example.david__paymaster.ViewPayersActivity;
import com.example.david__paymaster.igrpay.R;
import com.example.david__paymaster.model.People;
import com.example.david__paymaster.utils.Tools;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;
//import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewPayersList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<People> items = new ArrayList<>();
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private FirebaseFirestore firestoreDB;

    public interface OnItemClickListener {
        void onItemClick(View view, People obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public ViewPayersList(Context context, List<People> items, FirebaseFirestore firestoreDB) {
        this.items = items;
        ctx = context;
        this.firestoreDB = firestoreDB;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public CircularImageView imagey;
        public TextView name, occupation;
        public View lyt_parent;
        public LinearLayout viewlay, collecLay;

        public OriginalViewHolder(View v) {
            super(v);
            imagey = (CircularImageView) v.findViewById(R.id.image);
            name = (TextView) v.findViewById(R.id.name);
            occupation = (TextView) v.findViewById(R.id.description);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            viewlay = (LinearLayout) v.findViewById(R.id.viewLay);
            collecLay = (LinearLayout) v.findViewById(R.id.collectLay);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_people_chat_too, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            final People p = items.get(position);
            view.name.setText(p.name);
            view.occupation.setText(p.occupation);
//            view.imageUrl.(p.imageUrl);
//            Tools.displayImageRound(ctx, view.imageUrl, p.image);
//            Tools.displayImageOriginal(ctx, view.imagey, p.imageUrl);
            Picasso.get()
                    .load(p.getImageUrl())
                    .placeholder(R.drawable.default_user)
                    .fit()
                    .centerCrop()
                    .into(view.imagey);

            view.viewlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String getNameString = p.name.toString();
                    ctx.startActivity(new Intent(ctx, SinglePayerActivity.class).putExtra("Name", getNameString).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });
            view.collecLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ctx.startActivity(new Intent(ctx, CollectTaxActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}