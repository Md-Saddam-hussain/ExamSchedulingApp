package com.amier.modernloginregister.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amier.modernloginregister.Common.Common;
import com.amier.modernloginregister.Interface.IRecyclerItemSelectedListener;
import com.amier.modernloginregister.R;
import com.amier.modernloginregister.model.Centers;

import java.util.ArrayList;
import java.util.List;

public class MyCentersAdapter extends RecyclerView.Adapter<MyCentersAdapter.MyViewHolder> {

    Context context;
    List<Centers> centersList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyCentersAdapter(Context context, List<Centers> centersList) {
        this.context = context;
        this.centersList = centersList;
        cardViewList = new ArrayList<>();
        localBroadcastManager  = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_center,viewGroup,false);
        //return null;
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
            myViewHolder.txt_center_name.setText(centersList.get(position).getName());
            //add for address of center also if needed
        if(!cardViewList.contains(myViewHolder.card_center))
            cardViewList.add(myViewHolder.card_center);

        myViewHolder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //Set white Background for all card not to be selected
                for (CardView cardView:cardViewList)
                    cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));

                //Set selected BG for only selected item
                myViewHolder.card_center.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_light));

                //Send Boardcast to tell Booking Activity enable  Button next
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_CENTER_STORE,centersList.get(pos));
                intent.putExtra(Common.KEY_STEP,1);
                localBroadcastManager.sendBroadcast(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        //return 0;
        return centersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_center_name;
        //address of center you can also add here
        CardView card_center;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_center_name = (TextView)itemView.findViewById(R.id.txt_center_name);
            card_center = (CardView)itemView.findViewById(R.id.card_center);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iRecyclerItemSelectedListener.onItemSelectedListener(view,getAdapterPosition());
        }
    }
}
