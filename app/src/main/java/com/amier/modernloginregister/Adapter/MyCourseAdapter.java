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
import com.amier.modernloginregister.model.Courser;

import java.util.ArrayList;
import java.util.List;

public class MyCourseAdapter extends RecyclerView.Adapter<MyCourseAdapter.MyViewHolder> {

    Context context;
    List<Courser> courserList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyCourseAdapter(Context context, List<Courser> courserList) {
        this.context = context;
        this.courserList = courserList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //return null;
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_course,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.txt_course_name.setText(courserList.get(position).getName());
        //rating
        if(!cardViewList.contains(myViewHolder.card_course));
        cardViewList.add(myViewHolder.card_course);

        myViewHolder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //Set white Background for all card not to be selected
                for (CardView cardView:cardViewList) {
                    cardView.setCardBackgroundColor(context.getResources()
                            .getColor(android.R.color.white));
                }

                //Set selected BG for only selected item
                myViewHolder.card_course.setCardBackgroundColor(context.getResources()
                        .getColor(android.R.color.holo_orange_light));

                //Send local Boardcast to tell enable  Button next
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_COURSE_SELECTED,courserList.get(pos));
                intent.putExtra(Common.KEY_STEP,2);
                localBroadcastManager.sendBroadcast(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        //return 0;
        return courserList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_course_name;
        CardView card_course;
        //Rating also u can add or feedback type

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_course_name = (TextView)itemView.findViewById(R.id.txt_course_name);
            card_course = (CardView) itemView.findViewById(R.id.card_course);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iRecyclerItemSelectedListener.onItemSelectedListener(view,getAdapterPosition());
        }
    }
}
