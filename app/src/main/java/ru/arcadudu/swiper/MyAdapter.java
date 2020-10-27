package ru.arcadudu.swiper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Model> modelList = new ArrayList<>();
    private Context context;

    ILongClickCallBack longClickCallBack;

    public void setLongClickCallBack(ILongClickCallBack longClickCallBack) {
        this.longClickCallBack = longClickCallBack;
    }

    public MyAdapter(List<Model> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Model model = modelList.get(position);
        holder.tv_title.setText(model.getTitle());
        holder.tv_content.setText(model.getContent());
        holder.iv_image.setImageResource(model.getImage());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_title, tv_content;
        private ImageView iv_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_content);
            iv_image = itemView.findViewById(R.id.image_view);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    openBottomSheet(v, position);
                    return true;
                }
            });
        }

        @Override
        public void onClick(View v) {
        }

        private void openBottomSheet(View view, int position) {
            longClickCallBack.click(modelList.get(position));
        }
    }
}
