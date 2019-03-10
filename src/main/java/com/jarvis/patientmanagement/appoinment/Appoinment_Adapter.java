package com.jarvis.patientmanagement.appoinment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.jarvis.patientmanagement.R;
import com.jarvis.patientmanagement.adapter.DataAdapterAll;

public class Appoinment_Adapter extends RecyclerView.Adapter<Appoinment_Adapter.ViewHolder> {

    Context context;
    List<DataAdapterAll> dataAdapterAlls;

    public Appoinment_Adapter(List<DataAdapterAll> getDataAdapterAll, Context context){

        super();
        this.dataAdapterAlls = getDataAdapterAll;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_appoinment, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        DataAdapterAll dataAdapterAll =  dataAdapterAlls.get(position);
        viewHolder.TextViewID.setText(dataAdapterAll.getName());
        viewHolder.TextViewName.setText(dataAdapterAll.getId());
        viewHolder.TextViewPhoneNumber.setText(dataAdapterAll.getAddress());
        viewHolder.TextViewSubject.setText(dataAdapterAll.getSubject());
    }

    @Override
    public int getItemCount() {
        return dataAdapterAlls.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView TextViewName;
        public TextView TextViewID;
        public TextView TextViewPhoneNumber;
        public TextView TextViewSubject;

        public ViewHolder(View itemView) {

            super(itemView);

            TextViewName = (TextView) itemView.findViewById(R.id.textView4) ;
            TextViewID = (TextView) itemView.findViewById(R.id.textView2) ;
            TextViewPhoneNumber = (TextView) itemView.findViewById(R.id.textView6) ;
            TextViewSubject = (TextView) itemView.findViewById(R.id.textView8) ;
        }
    }
}