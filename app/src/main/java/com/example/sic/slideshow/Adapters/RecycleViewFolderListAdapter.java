package com.example.sic.slideshow.Adapters;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sic.slideshow.Fragments.SelectFolderFragment;
import com.example.sic.slideshow.Fragments.SettingsFragment;
import com.example.sic.slideshow.R;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class RecycleViewFolderListAdapter extends RecyclerView.Adapter<RecycleViewFolderListAdapter.ViewHolder> {
    static final public String FOLDER_ID = "folderId";
    static final public String FOLDER_NAME = "name";
    ArrayList<SelectFolderFragment.Album> list = new ArrayList<>();
    FragmentActivity fragmentActivity;

    public RecycleViewFolderListAdapter(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_folder_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.folderText.setText(list.get(position).getName());
        Glide.with(fragmentActivity)
                .load(list.get(position).getCoverUri())
                .animate(R.anim.pop_enter)
                .bitmapTransform(new CropCircleTransformation(fragmentActivity))
                .into(holder.folderCover);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsFragment settingsFragment = new SettingsFragment();
                Bundle bundle = new Bundle();
                bundle.putString(FOLDER_ID, list.get(position).getId());
                bundle.putString(FOLDER_NAME, list.get(position).getName());
                settingsFragment.setArguments(bundle);
                fragmentActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                        .replace(R.id.container, settingsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void refreshList(ArrayList<SelectFolderFragment.Album> newList) {
        list = newList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        TextView folderText;
        ImageView folderCover;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            folderText = (TextView) view.findViewById(R.id.folder_text);
            folderCover = (ImageView) view.findViewById(R.id.folder_preview_image);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}