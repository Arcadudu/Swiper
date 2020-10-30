package ru.arcadudu.swiper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter {

    private List<Model> modelList = new ArrayList<>();
    private Context context;

    ILongClickCallBack longClickCallBack;


    public MyAdapter(List<Model> modelList, Context context, ILongClickCallBack iLongClickCallBack) {
        this.modelList = modelList;
        this.context = context;
        this.longClickCallBack = iLongClickCallBack;
    }

    @Override
    public int getItemViewType(int position) {
        Model model = modelList.get(position);

        // ads
        if (model.isAds()) {
            return 0;
        }

        // plain
        if (model.getContent() == null) {
            return 1;
        }

        // regular
        return 2;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;


        // ads
        if (viewType == 0) {
            view = inflater.inflate(R.layout.model_item_layout_ads, parent, false);
            return new ViewHolderAds(view);
            // plain
        } else if (viewType == 1) {
            view = inflater.inflate(R.layout.model_item_layout_plain, parent, false);
            return new ViewHolderPlain(view);

        }
        // regular
        view = inflater.inflate(R.layout.model_item_layout_regular, parent, false);
        return new ViewHolderRegular(view);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Model model = modelList.get(position);

        if(model.isAds()){
            ViewHolderAds viewHolderAds = (ViewHolderAds) holder;
                Glide.with(context).load(model.getLink()).into(viewHolderAds.adsImage);
        }else{
            if(model.getContent()==null){
                ViewHolderPlain viewHolderPlain = (ViewHolderPlain) holder;
                viewHolderPlain.title.setText(model.getTitle());
            }else{
                ViewHolderRegular viewHolderRegular = (ViewHolderRegular) holder;
                viewHolderRegular.title.setText(model.getTitle());
                viewHolderRegular.content.setText(model.getContent());
                viewHolderRegular.image.setImageResource(model.getImage());
            }
        }
//        // ads model
//        if (model.getLink() != null ) {
//            if(model.isAds()){
//                ViewHolderAds viewHolderAds = (ViewHolderAds) holder;
//                Glide.with(context).load(model.getLink()).into(viewHolderAds.adsImage);
//            }else{
//                ViewHolderPlain viewHolderPlain = (ViewHolderPlain) holder;
//                viewHolderPlain.title.setText(model.getTitle());
//            }
//
//        } else {
//            ViewHolderRegular viewHolderRegular = (ViewHolderRegular) holder;
//            viewHolderRegular.title.setText(model.getTitle());
//            viewHolderRegular.content.setText(model.getContent());
//            viewHolderRegular.image.setImageResource(model.getImage());
//        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class ViewHolderRegular extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView title, content;

        public ViewHolderRegular(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            title = itemView.findViewById(R.id.tv_title);
            content = itemView.findViewById(R.id.tv_content);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    openBottomSheet(position);
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    goToDetails(position);
                }
            });

        }

        private void openBottomSheet(int position) {
            longClickCallBack.longClick(modelList.get(position));
        }

        private void goToDetails(int position){
            longClickCallBack.click(modelList.get(position));
        }
    }

    class ViewHolderPlain extends RecyclerView.ViewHolder {

        private TextView title;

        public ViewHolderPlain(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_plain_text);
        }
    }

    class ViewHolderAds extends RecyclerView.ViewHolder {

        private ImageView adsImage;

        public ViewHolderAds(@NonNull View itemView) {
            super(itemView);
            adsImage = itemView.findViewById(R.id.image_ads);
        }
    }


}

//        private void openBottomSheet(int position) {
//            longClickCallBack.click(modelList.get(position));
//        }

//itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    int position = getAdapterPosition();
//                    openBottomSheet(position);
//                    return true;
//                }
//            });


//    private void openBottomSheet(int position) {
//            longClickCallBack.click(modelList.get(position));
//        }

