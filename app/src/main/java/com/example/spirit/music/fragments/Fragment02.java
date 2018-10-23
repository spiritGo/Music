package com.example.spirit.music.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.spirit.music.R;
import com.example.spirit.music.activvity.OnLineMusicActivity;
import com.example.spirit.music.bean.BoardBean;
import com.example.spirit.music.interfaces.HttpCallBack;
import com.example.spirit.music.tools.HttpClient;
import com.example.spirit.music.tools.HttpUtil;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Fragment02 extends Fragment {

    //1、新歌榜，2、热歌榜，
//        11、摇滚榜，12、爵士，16、流行
//        21、欧美金曲榜，22、经典老歌榜，23、情歌对唱榜，24、影视金曲榜，25、网络歌曲榜

    private final int[] rankingCode = {2, 1, 21, 24, 16, 11, 22, 23, 12, 25};
    private ArrayList<Integer> rankingCodeSort = new ArrayList<>();
    private final Gson gson = new Gson();
    private final ArrayList<BoardBean> boardBeans = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                BoardBean boardBean = (BoardBean) msg.obj;
                boardBeans.add(boardBean);
            } else if (msg.what == 3) {
                onLineMusic.setAdapter(new MyAdapter(boardBeans));
                System.out.println("boardBeans:" + boardBeans);

                onLineMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long
                            id) {
                        Intent intent = new Intent(getActivity(), OnLineMusicActivity.class);
                        intent.putExtra("type", rankingCodeSort.get(position));
                        startActivity(intent);
                        System.out.println("billboard_type:" + rankingCodeSort.toString());
                    }
                });
            }
        }
    };
    private ListView onLineMusic;
    private int index = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (final int code : rankingCode) {
            HttpUtil.getHttpUtil().getInfo2(getContext(), HttpClient.getHttpClient()
                    .getOnLineMusicListUrl(getContext(), code, 4, 0), new HttpCallBack() {
                @Override
                public void info(String info) {
                    BoardBean boardBean = gson.fromJson(info, BoardBean.class);
                    Message message = Message.obtain();
                    message.obj = boardBean;
                    message.what = 1;
                    handler.sendMessage(message);

                    index++;

                    if (index >= rankingCode.length - 1) {
                        handler.sendEmptyMessage(3);
                    }
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment02, container, false);
        initView(view);

//        HttpClient.getHttpClient().getOnLineMusicList(getContext(), 1, 3, 0);

        handler.sendEmptyMessage(2);
        return view;
    }

    private void initView(View view) {
        onLineMusic = view.findViewById(R.id.onLineMusic);
    }

    class MyAdapter extends BaseAdapter {

        private ArrayList<BoardBean> boardBeans;
        private ImageView rinkIcon;
        private TextView rinkFirst;
        private TextView rinkSecond;
        private TextView rinkThird;

        MyAdapter(ArrayList<BoardBean> boardBeans) {
            this.boardBeans = boardBeans;
        }

        @Override
        public int getCount() {
            return boardBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return boardBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.ranking_layout, null);
                holder = new ViewHolder();
                initView(convertView);

                holder.rinkFirst = rinkFirst;
                holder.rinkIcon = rinkIcon;
                holder.rinkSecond = rinkSecond;
                holder.rinkThird = rinkThird;

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            BoardBean boardBean = boardBeans.get(position);

            if (!TextUtils.isEmpty(boardBean.getBillboard().getPic_s192())) {
                Glide.with(getContext()).load(Uri.parse(boardBean.getBillboard().getPic_s192()))
                        .into(holder.rinkIcon);
            } else {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable
                        .default_cover);
                holder.rinkIcon.setImageBitmap(bitmap);
            }
            holder.rinkFirst.setText(String.format("1.%s", boardBean.getSong_list().get(0)
                    .getTitle()));
            holder.rinkSecond.setText(String.format("2.%s", boardBean.getSong_list().get(1)
                    .getTitle()));
            holder.rinkThird.setText(String.format("3.%s", boardBean.getSong_list().get(2)
                    .getTitle()));

            String billboard_type = boardBean.getBillboard().getBillboard_type();
            System.out.println("billboard_type:" + billboard_type);
            rankingCodeSort.add(Integer.parseInt(billboard_type));

            return convertView;
        }

        private void initView(View view) {
            rinkIcon = view.findViewById(R.id.rinkIcon);
            rinkFirst = view.findViewById(R.id.rinkFirst);
            rinkSecond = view.findViewById(R.id.rinkSecond);
            rinkThird = view.findViewById(R.id.rinkThird);
        }
    }

    private class ViewHolder {
        ImageView rinkIcon;
        TextView rinkFirst;
        TextView rinkSecond;
        TextView rinkThird;
    }
}
