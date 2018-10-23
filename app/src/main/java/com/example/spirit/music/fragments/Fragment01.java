package com.example.spirit.music.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.spirit.music.R;
import com.example.spirit.music.activvity.Main2Activity;
import com.example.spirit.music.bean.MusicBean;
import com.example.spirit.music.tools.SearchLocalMusic;

import java.util.ArrayList;

public class Fragment01 extends Fragment {

    private ArrayList<MusicBean> musicBeans;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment01, container, false);
        initView(view);

        return view;
    }

    private void initView(View view) {
        ListView lvMusic = view.findViewById(R.id.lv_music);

        musicBeans = SearchLocalMusic.getSearch().search(getContext());

        lvMusic.setAdapter(new MyAdapter(musicBeans));

        final Main2Activity main2Activity = (Main2Activity) getActivity();
        if (main2Activity == null) return;

        lvMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Bitmap bitmap = SearchLocalMusic.getSearch().loadBitMapFromMediaStore(getContext
                            (), Long.parseLong(musicBeans.get(position).getAumbleId()));
                    main2Activity.setMusicInfo(bitmap, musicBeans.get(position).getTitle(),
                            musicBeans.get(position).getArtist());

                    Intent play = new Intent("play");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("musicBean", musicBeans.get(position));
                    play.putExtras(bundle);
                    getActivity().sendBroadcast(play);
                    getActivity().getSharedPreferences("music", Context.MODE_PRIVATE).edit().putInt
                            ("position", position).apply();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class MyAdapter extends BaseAdapter {
        private ArrayList<MusicBean> musicBeans;
        private ImageView musicIcon;
        private TextView musicTitle;
        private TextView musicArtist;
        private ImageView icoMore;

        MyAdapter(ArrayList<MusicBean> musicBeans) {
            this.musicBeans = musicBeans;
        }

        @Override
        public int getCount() {
            return musicBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return musicBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.music_list, null);
                initView(convertView);
                holder = new ViewHolder();
                holder.iconMore = icoMore;
                holder.musicArtist = musicArtist;
                holder.musicIcon = musicIcon;
                holder.musicTitle = musicTitle;
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            MusicBean musicBean = musicBeans.get(position);
            holder.musicTitle.setText(musicBean.getTitle());
            holder.musicArtist.setText(musicBean.getArtist());

            holder.musicIcon.setImageBitmap(SearchLocalMusic.getSearch().loadBitMapFromMediaStore
                    (getContext(), Long.parseLong(musicBean.getAumbleId())));
            return convertView;
        }

        private void initView(View view) {
            musicIcon = view.findViewById(R.id.musicIcon);
            musicTitle = view.findViewById(R.id.musicTitle);
            musicArtist = view.findViewById(R.id.musicArtist);
            icoMore = view.findViewById(R.id.icoMore);
        }
    }


    class ViewHolder {
        ImageView musicIcon;
        TextView musicTitle;
        TextView musicArtist;
        ImageView iconMore;
    }
}
