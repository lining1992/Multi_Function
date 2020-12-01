package com.baidu.iov.dueros.test.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.LogPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.iov.dueros.test.R;
import com.baidu.iov.dueros.test.view.CustomRecycleView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author v_lining05
 * @date 2020/8/30
 */
public class CustomTouchEventActivity extends AppCompatActivity {

    private CustomRecycleView recyclerView;

    private List<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_toutch_event);

        list.add(0, "音乐");
        list.add(1, "有声");
        list.add(2, "新闻");
        list.add(3, "最近播放 p 1");
        list.add(4, "最近播放 p 2");
        list.add(5, "最近播放 p 3");
        list.add(6, "最近播放 p 4");
        list.add(7, "最近播放 p 5");
        list.add(8, "最近播放 p 6");
        list.add(9, "最近播放 p 7");
        list.add(10, "最近播放 p 8");
        list.add(11, "最近播放 p 9");
        for (int i = 11; i < 50; i++) {
            list.add(i, "音乐 p " + i);
        }

        recyclerView = findViewById(R.id.customRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new CustomAdapter(getApplicationContext(), list));

        LogPrinter customTextVIewActivity = new LogPrinter(Log.DEBUG, "CustomTextVIewActivity");
        try {
            customTextVIewActivity.println("cacheDir=" + getCacheDir().getAbsolutePath());
            RandomAccessFile randomAccessFile = new RandomAccessFile(new File(getCacheDir()
                    .getAbsolutePath() + "test.txt"), "rw");
            randomAccessFile.write("RandomAccessFile".getBytes());
            MappedByteBuffer map = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_WRITE,
                    0, "RandomAccessFile2".getBytes().length);
            map.put("RandomAccessFile2".getBytes());
            // reading from memory file in Java
            for (int i = 0; i < "RandomAccessFile2".getBytes().length; i++) {
                customTextVIewActivity.println(String.valueOf(map.get(i)));
            }
            boolean readOnly = map.isReadOnly();
            customTextVIewActivity.println("map position=" + map.position());
            customTextVIewActivity.println("readOnly=" + readOnly);
            randomAccessFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    List<String> mList;
    Context mContext;

    public CustomAdapter(Context context, List<String> list) {
        this.mList = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_item, parent, false);
        return new CustomViewHolder(view).setItemOnClickListener(new CustomViewHolder.ItemOnClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("debugli", "onItemClick==" + mList.get(position));
                Toast.makeText(mContext, mList.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, final int position) {
        holder.customTextView.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}

class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView customTextView;
    private ItemOnClickListener itemOnClickListener;

    public ItemOnClickListener getItemOnClickListener() {
        return itemOnClickListener;
    }

    public CustomViewHolder setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
        return this;
    }

    public CustomViewHolder(@NonNull View itemView) {
        super(itemView);
        customTextView = itemView.findViewById(R.id.customTextView);
        customTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (itemOnClickListener != null) {
            itemOnClickListener.onItemClick(getLayoutPosition());
        }
    }

    interface ItemOnClickListener {
        void onItemClick(int position);
    }
}
