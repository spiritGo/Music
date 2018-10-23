package com.example.spirit.music.activvity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.spirit.music.R;
import com.example.spirit.music.bean.BoardBean;
import com.example.spirit.music.interfaces.HttpCallBack;
import com.example.spirit.music.tools.HttpClient;
import com.example.spirit.music.tools.HttpUtil;
import com.google.gson.Gson;

public class OnLineMusicActivity extends Activity {
    private TextView title;
    private TextView updateTime;
    private TextView detail;
    private ListView lvMusic;
    private ImageView iconGroup;
    private final Gson gson = new Gson();

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            boardBean = (BoardBean) msg.obj;
            if (!TextUtils.isEmpty(boardBean.getBillboard().getPic_s192())) {
                Glide.with(getApplicationContext()).load(boardBean.getBillboard().getPic_s192())
                        .into(iconGroup);
            } else {
                iconGroup.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable
                        .default_cover));
            }

            if (!TextUtils.isEmpty(boardBean.getBillboard().getName())) {
                title.setText(boardBean.getBillboard().getName());
                updateTime.setText(String.format("最近更新：%s", boardBean.getBillboard()
                        .getUpdate_date()));
                detail.setText(boardBean.getBillboard().getComment());
            }


            if (size > 10) {
                myAdapter.setBoardBeans(boardBean);
                myAdapter.notifyDataSetChanged();
                lvMusic.smoothScrollToPosition(myAdapter.getCount() - 10);

            } else {
                myAdapter = new MyAdapter(boardBean);
                lvMusic.setAdapter(myAdapter);
            }

            System.out.println("artist:" + boardBean.getSong_list().get(0).getAuthor());

            lvMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(final AdapterView<?> parent, View view, int position,
                                        long id) {

                    String song_id = boardBean.getSong_list().get(position).getSong_id();
                    String songUrl = HttpClient.getHttpClient().getSongUrl(Long.parseLong(song_id));

                    Intent play = new Intent("online_play");
                    play.putExtra("songUrl", songUrl);
                    sendBroadcast(play);
                    Main2Activity.getActivity().setMusicInfo(boardBean.getSong_list().get
                            (position).getAlbum_500_500(), boardBean.getSong_list().get(position)
                            .getTitle(), boardBean.getSong_list().get(position).getAuthor());
                }
            });

            lvMusic.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int
                        visibleItemCount, int totalItemCount) {
//                    if (firstVisibleItem + visibleItemCount == totalItemCount) {
//                        size += 10;
//                        getBoardBean(size);
//                    }

                    if (firstVisibleItem + visibleItemCount == totalItemCount) {
                        View lastView = lvMusic.getChildAt(lvMusic.getChildCount() - 1);
                        if (lastView != null && lastView.getBottom() == lvMusic.getHeight()) {
                            Log.e("ListView", "##### 滚动到底部 ######");
                            size += 10;
                            getBoardBean(size);
                        }
                    }
                }
            });
        }
    };
    private MyAdapter myAdapter;
    private int type;
    private BoardBean boardBean;
    private int size = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onlinemusic_layout);


        initView();

        type = getIntent().getIntExtra("type", 0);

        String onLineMusicListUrl = HttpClient.getHttpClient().getOnLineMusicListUrl(this, type,
                10, 0);
        HttpUtil.getHttpUtil().getInfo2(this, onLineMusicListUrl, new HttpCallBack() {
            @Override
            public void info(String info) {
                BoardBean boardBean = gson.fromJson(info, BoardBean.class);
                Message message = Message.obtain();
                message.obj = boardBean;
                handler.sendMessage(message);

            }
        });
    }

    private void initView() {
        title = findViewById(R.id.title);
        updateTime = findViewById(R.id.updateTime);
        detail = findViewById(R.id.detail);
        lvMusic = findViewById(R.id.lv_music);
        iconGroup = findViewById(R.id.icon_group);
    }

    private void getBoardBean(int size) {
        String onLineMusicListUrl = HttpClient.getHttpClient().getOnLineMusicListUrl(this, type,
                size, 0);
        HttpUtil.getHttpUtil().getInfo2(this, onLineMusicListUrl, new HttpCallBack() {
            @Override
            public void info(String info) {
                BoardBean boardBean = gson.fromJson(info, BoardBean.class);
                Message message = Message.obtain();
                message.obj = boardBean;
                handler.sendMessage(message);
            }
        });
    }

    class MyAdapter extends BaseAdapter {
        private BoardBean boardBeans;
        private ImageView musicIcon;
        private TextView musicTitle;
        private TextView musicArtist;
        private ImageView icoMore;

        MyAdapter(BoardBean boardBeans) {
            this.boardBeans = boardBeans;
        }


        public void setBoardBeans(BoardBean boardBeans) {
            this.boardBeans = boardBeans;
        }

        @Override
        public int getCount() {
            return boardBeans.getSong_list().size();
        }

        @Override
        public Object getItem(int position) {
            return boardBeans.getSong_list().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.music_list, null);
                holder = new ViewHolder();
                initView(convertView);
                holder.icoMore = icoMore;
                holder.musicArtist = musicArtist;
                holder.musicIcon = musicIcon;
                holder.musicTitle = musicTitle;

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.musicTitle.setText(boardBeans.getSong_list().get(position).getTitle());
            holder.musicArtist.setText(boardBeans.getSong_list().get(position).getAuthor());
            Glide.with(getApplicationContext()).load(boardBeans.getSong_list().get(position)
                    .getAlbum_500_500()).into(holder.musicIcon);
            icoMore.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable
                    .ic_music_list_icon_more));
            return convertView;
        }

        private void initView(View View) {
            musicIcon = View.findViewById(R.id.musicIcon);
            musicTitle = View.findViewById(R.id.musicTitle);
            musicArtist = View.findViewById(R.id.musicArtist);
            icoMore = View.findViewById(R.id.icoMore);
        }
    }

    class ViewHolder {
        ImageView musicIcon;
        TextView musicTitle;
        TextView musicArtist;
        ImageView icoMore;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myAdapter = null;
        boardBean = null;
    }
}
