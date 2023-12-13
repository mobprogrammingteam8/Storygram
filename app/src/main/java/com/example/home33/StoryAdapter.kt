package com.example.diaryproject

import android.content.Context
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.greenfrvr.hashtagview.HashtagView
import com.bumptech.glide.Glide
import java.io.File
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.FileInputStream


class StoryAdapter(val storyList: ArrayList<StoryItem>, val context: Context) : RecyclerView.Adapter<StoryAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_story, parent, false)

        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                var curPos: Int = adapterPosition
                var story: StoryItem = storyList.get(curPos)
            }
        }
    }

    override fun getItemCount(): Int {
        return storyList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        var imageUri = storyList.get(position).imageurl // 캐시 저장소에 저장되어 있는 이미지 경로
        var imageName =
            imageUri?.lastIndexOf('/')?.plus(1)?.let { imageUri.substring(it, imageUri.length) };
        // var imageName = imageUri.substring(imageUri.lastIndexOf('/') + 1, imageUri.length);

        // 이미지 불러오기
        val file = File(
            Environment.getExternalStoragePublicDirectory(imageUri),
            imageName + ".jpg"
        )
        try { //feed 이미지 설정

            Glide.with(context)
                .load(file)
                .into(holder.feed)
            Log.d("feed", "feed 파일 로드 성공")
        } catch (e: Exception) {
            Log.d("feed", "feed 파일 로드 실패")
        }

        try { // user-image자리에 feed사진 넣기(user이미지 설정 안하므로 feed 사진을 동일하게 쓰도록 함)
            var bitmap: Bitmap? = null
            var options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            bitmap = BitmapFactory.decodeStream(FileInputStream(imageUri), null, options)
            holder.userimage.setImageBitmap(bitmap) // holder에 이미지 씌우려면 R.mipmap....이런 형식이어야해서 bitmap으로 이미지를 넣게 되었습니다.
        } catch (e: Exception) {
            Log.d("userimage", "user-image 파일 로드 실패")
        }
        //holder.feed.setImageResource(R.drawable.feed2)
        //holder.userimage.setImageResource(R.mipmap.ic_launcher_round)
        holder.content.text = storyList.get(position).content
        holder.date.text = storyList.get(position).date
        var tags = storyList.get(position).tag
        var taglist = mutableListOf<String>()
        if (tags != null) {
            for(tag in tags) {
                if(holder.hashtag.addItem(tag)) Log.d("tag,", "tag addItem 추가 성공!!")
                taglist.add(tag)
                Log.d("태그", "tagList에 차곡차곡 tag 넣는중" + tag)

            }
            taglist.clear()
        }

        try {
            holder.hashtag.setData(taglist, Transformers.HASH_SELECTED)

            Log.d("hashtag", "hashtag 로드 성공!!")
        } catch (e: Exception) {
            Log.d("hashtag", "hashtag 로드 실패")
        }


    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userimage = itemView.findViewById<ImageView>(R.id.iv_userimage)
        var feed = itemView.findViewById<ImageView>(R.id.iv_feed)
        var date = itemView.findViewById<TextView>(R.id.tv_date)
        var content = itemView.findViewById<TextView>(R.id.tv_text)
        var hashtag = itemView.findViewById<HashtagView>(R.id.tag1)

    }
}
