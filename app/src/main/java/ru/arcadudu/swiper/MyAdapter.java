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
        if (model.getLink() != null) { // ads
            return 0;
        }
        return model.getContent() == null ? 1 : 2; // plain item : regular item
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
        }
        if (viewType == 1) {
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

        // ads model
        if (model.getLink() != null) {
            ViewHolderAds viewHolderAds = (ViewHolderAds) holder;
            Glide.with(context).load(model.getLink()).into(viewHolderAds.adsImage);
            // plain model
        } else if (model.getContent() == null) {
            ViewHolderPlain viewHolderPlain = (ViewHolderPlain) holder;
            viewHolderPlain.title.setText(model.getTitle());
            // regular model
        } else {
            ViewHolderRegular viewHolderRegular = (ViewHolderRegular) holder;
            viewHolderRegular.title.setText(model.getTitle());
            viewHolderRegular.content.setText(model.getContent());
            viewHolderRegular.image.setImageResource(model.getImage());
        }
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

        }

        private void openBottomSheet(int position) {
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

