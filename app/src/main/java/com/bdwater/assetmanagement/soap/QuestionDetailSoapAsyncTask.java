package com.bdwater.assetmanagement.soap;

import android.os.AsyncTask;

import com.bdwater.assetmanagement.model.Image;
import com.bdwater.assetmanagement.model.TroubleNoteDetail;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.json.JSONObject;
import org.kobjects.base64.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by hegang on 17/11/20.
 */

public class QuestionDetailSoapAsyncTask extends AsyncTask<UpdateTroubleNoteDetailArgs, Integer, QuestionDetailSoapAsyncTask.SoapResult> {
    public interface OnCompletedListener {
        void onCompleted(SoapResult result);
    }

    private QuestionDetailSoapAsyncTask.OnCompletedListener listener;
    public QuestionDetailSoapAsyncTask(OnCompletedListener listener) {
        this.listener = listener;
    }

    @Override
    protected QuestionDetailSoapAsyncTask.SoapResult doInBackground(UpdateTroubleNoteDetailArgs... args) {
        UpdateTroubleNoteDetailArgs arg = args[0];
        SoapResult sr = new SoapResult();
        sr.code = 0;
        for(int i = 0; i < arg.images.length; i++) {
            try {
                // check and upload each image
                Image image = arg.images[i];
                if (image.IsOnlyForAdd || image.From == Image.FROM_REMOTE || image.IsUploaded)
                    continue;          // this image is uploaded, skip

                // uploads this image by base64
                String filename = writeImage(image.LocalPath);
                if (!filename.equals("")) {
                    image.RemotePath = filename;
                    image.IsUploaded = true;
                }
                arg.progressDoneLength++;
                int percent = (arg.progressDoneLength / arg.progressLength) * 100;
                publishProgress(percent);
            } catch (Exception e) {
                // it occurs some error, return
                // error message
                sr.code = -2;
                sr.message = e.getMessage();
                return sr;
            }
        }
        try {
            TroubleNoteDetail entry = args[0].entry;
            List<Image> images = new ArrayList<>();
            for(Image image : arg.images)
                if(!image.IsOnlyForAdd)
                    images.add(image);
            entry.Images = images.toArray(new Image[images.size()]);

            Gson gson = new Gson();
            TypeAdapter<TroubleNoteDetail> typeAdapter = gson.getAdapter(TroubleNoteDetail.class);
            String json = typeAdapter.toJson(entry);

            String s = SoapClient.UpdateTroubleNoteDetail(arg.mode, json);
            JSONObject ja = new JSONObject(s);
            int code = ja.getInt("code");
            if(code != 0) {
                // it's failed, return error message
                // and return
                sr.message = ja.getString("message");
                sr.code = -1;
                return sr;
            }
            publishProgress(100);
        }
        catch (Exception e) {
            sr.code = -2;
            sr.message = e.getMessage();
            return sr;
        }
        // it's all completed, return success
        return sr;
    }
    private static String writeImage(String filePath) throws Exception {
        Base64Args args = new Base64Args();
        args.localFileName = filePath;
        while (!args.isEnd) {
            readBase64(args);
            String s = SoapClient.UploadImagePart(args.remoteFileName, args.base64);
            JSONObject ja = new JSONObject(s);
            int code = ja.getInt("code");
            if (code == 0) {
                // it's success, store image name
                args.remoteFileName = ja.getString("message");
            }
            else
                throw new Exception(ja.getString("message"));
        }
        return args.remoteFileName;
    }
    // get base64 string from image file
    private static void readBase64(Base64Args args) throws IOException {
        FileInputStream fis = new FileInputStream(args.localFileName);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 1024 * 2]; // 2m per times
        int count = 0;
        if(args.offset > 0)
            fis.skip(args.offset);
        count = fis.read(buffer);
        if(count >= 0)
            stream.write(buffer);
        args.base64 = new String(Base64.encode(stream.toByteArray()));
        args.isEnd = count < 0;
        args.offset += count;
        fis.close();
        stream.close();
//        while((count = fis.read(buffer)) >= 0){
//            stream.write(buffer, 0, count);
//        }
//        fis.close();
//        return new String(Base64.encode(stream.toByteArray()));
    }

    private void onCompleted(SoapResult result) {
        if (this.listener != null)
            this.listener.onCompleted(result);
    }

    @Override
    protected void onPostExecute(SoapResult soapResult) {
        this.onCompleted(soapResult);
    }

    public class SoapResult {
        public int code;
        public JSONObject data;
        public String message;
    }

}
