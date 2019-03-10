package com.jarvis.patientmanagement.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;
import java.util.List;
import com.android.volley.toolbox.ImageLoader;
import com.jarvis.patientmanagement.R;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    List<DataAdapter> dataAdapters;
    ImageLoader imageLoader;

    public RecyclerViewAdapter(List<DataAdapter> getDataAdapter, Context context){

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_hospital, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        DataAdapter dataAdapterOBJ =  dataAdapters.get(position);

        //Viewholder.TextViewID.setText(String.valueOf(dataAdapterOBJ.getId()));
        Viewholder.Hospital_Location.setText(dataAdapterOBJ.getHospitalLocation());
        Viewholder.Hospital_Phone.setText(dataAdapterOBJ.getHospitalPhone());
        Viewholder.Hospital_Address.setText(dataAdapterOBJ.getHospitalAddress());

        imageLoader = ImageAdapter.getInstance(context).getImageLoader();

        imageLoader.get(dataAdapterOBJ.getHospitalImageUrl(), ImageLoader.getImageListener(
                        Viewholder.Hospital_Image, R.drawable.ic_vector_loading, R.drawable.ic_vector_error));

        Viewholder.Hospital_Image.setImageUrl(dataAdapterOBJ.getHospitalImageUrl(), imageLoader);
        Viewholder.Hospital_Name.setText(dataAdapterOBJ.getHospitalName());

        Viewholder.Hospital_Location.setText(dataAdapterOBJ.getHospitalLocation());
//        Viewholder.TextViewID.setText(dataAdapterOBJ.getId());
        Viewholder.Hospital_Phone.setText(dataAdapterOBJ.getHospitalPhone());
        Viewholder.Hospital_Address.setText(dataAdapterOBJ.getHospitalAddress());
//        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataAdapters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView Hospital_Name;
        public NetworkImageView Hospital_Image;
        public TextView Hospital_Location;
        public TextView TextViewID;
        public TextView Hospital_Phone;
        public TextView Hospital_Address;

        public ViewHolder(View itemView) {

            super(itemView);

            Hospital_Name = (TextView) itemView.findViewById(R.id.appoinment_name) ;
            Hospital_Image = (NetworkImageView) itemView.findViewById(R.id.hospital_image);
            Hospital_Location = (TextView) itemView.findViewById(R.id.diseases_cause) ;
            //TextViewID = (TextView) itemView.findViewById(R.id.textView4) ;
            Hospital_Phone = (TextView) itemView.findViewById(R.id.diseases_diagnosis) ;
            Hospital_Address = (TextView) itemView.findViewById(R.id.diseases_treatment) ;
        }
    }
}