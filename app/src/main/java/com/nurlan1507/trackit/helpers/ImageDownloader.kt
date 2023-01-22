//package com.nurlan1507.trackit.helpers
//
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.os.AsyncTask
//import android.util.Log
//import android.widget.ImageView
//import com.google.android.datatransport.runtime.AutoProtoEncoderDoNotUseEncoder
//import java.io.InputStream
//import java.net.URL
//
//
//class ImageDownloader(var bmImage:ImageView?=null): AsyncTask<String, Void, Bitmap> (){
//    override fun onPostExecute(result:Bitmap){
//        bmImage?.setImageBitmap(result)
//    }
//
//    override fun doInBackground(vararg urls:String): Bitmap? {
//        var urlDisplay = urls[0]
//        var mIcon:Bitmap? = null
//        try{
//            var input:InputStream = java.net.URL(urlDisplay).openStream()
//            mIcon = BitmapFactory.decodeStream(input)
//        }catch (e:Exception){
//            Log.e("ImageError", e.message.toString())
//            e.printStackTrace()
//        }
//        return mIcon
//    }
//
//
//
//}