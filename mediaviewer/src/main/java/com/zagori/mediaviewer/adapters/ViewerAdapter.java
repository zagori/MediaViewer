package com.zagori.mediaviewer.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.OnScaleChangedListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.HashSet;
import java.util.List;

public class ViewerAdapter extends RecyclingPagerAdapter<ViewerAdapter.ImageViewHolder> {
    private Context context;
    private List<String> medias;
    private HashSet<ImageViewHolder> holders;
    private boolean isZoomingAllowed;

    public ViewerAdapter(Context context, List<String> medias, boolean isZoomingAllowed) {
        this.context = context;
        this.medias = medias;
        this.holders = new HashSet<>();
        this.isZoomingAllowed = isZoomingAllowed;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PhotoView photoView = new PhotoView(context);
        photoView.setZoomable(isZoomingAllowed);

        ImageViewHolder holder = new ImageViewHolder(photoView);
        holders.add(holder);

        return holder;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return medias.size();
    }

    @Override
    protected void onNotifyItemChanged(ViewHolder viewHolder) {
        super.onNotifyItemChanged(viewHolder);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public boolean isScaled(int index) {
        for (ImageViewHolder holder : holders) {
            if (holder.position == index) {
                return holder.isScaled;
            }
        }
        return false;
    }

    public void resetScale(int index) {
        for (ImageViewHolder holder : holders) {
            if (holder.position == index) {
                holder.resetScale();
                break;
            }
        }
    }

    public String getUrl(int index) {
        return medias.get(index);
    }

    class ImageViewHolder extends ViewHolder implements OnScaleChangedListener {

        private int position = -1;
        private PhotoView photoView;
        private boolean isScaled;

        ImageViewHolder(View itemView) {
            super(itemView);
            photoView = (PhotoView) itemView;
        }

        void bind(int position) {
            this.position = position;

            Glide.with(context)
                    .load(medias.get(position))
                    //.placeholder(R.drawable.ic_placeholder)
                    .into(photoView);

            photoView.setOnScaleChangeListener(this);
        }

        @Override
        public void onScaleChange(float scaleFactor, float focusX, float focusY) {
            isScaled = photoView.getScale() > 1.0f;
        }

        void resetScale() {
            photoView.setScale(1.0f, true);
        }

    }
}
