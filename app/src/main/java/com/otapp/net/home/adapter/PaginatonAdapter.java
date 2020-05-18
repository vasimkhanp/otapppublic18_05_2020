package com.otapp.net.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.home.Interface.PagerInterface;
import com.otapp.net.home.core.PagerModel;

import java.util.List;

public class PaginatonAdapter extends RecyclerView.Adapter<PaginatonAdapter.MyViewHolder>  {
    Context context;
   List<PagerModel> noOfpage;
   PagerInterface pagerInterface;
   PagerModel pagerModel;

    public PaginatonAdapter(Context context, List<PagerModel> noOfpage, PagerInterface pagerInterface) {
        this.context = context;
        this.noOfpage = noOfpage;
        this.pagerInterface = pagerInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       final View view = LayoutInflater.from(context).inflate(R.layout.layout_booking_pagination,viewGroup,false);
       return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        pagerModel=noOfpage.get(i);
        myViewHolder.tvNoOfPage.setText(noOfpage.get(i).pagecount);

        if(pagerModel.isSelectedPage){
            myViewHolder.tvNoOfPage.setBackgroundResource(R.drawable.circle_backround);
        }else {
            myViewHolder.tvNoOfPage.setBackgroundResource(R.drawable.cricle_gray);
        }
        myViewHolder.tvNoOfPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int j=0;j<noOfpage.size();j++){
                    noOfpage.get(j).isSelectedPage=false;
                }
                noOfpage.get(i).isSelectedPage=true;
                myViewHolder.tvNoOfPage.setBackgroundResource(R.drawable.circle_backround);
              /*  int count=i;
                if(count>=1){
                    count=count-1;
                }*/
                pagerInterface.selectedPage(""+i);
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return noOfpage.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvNoOfPage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNoOfPage=itemView.findViewById(R.id.tvPageNo);
        }
    }
}
