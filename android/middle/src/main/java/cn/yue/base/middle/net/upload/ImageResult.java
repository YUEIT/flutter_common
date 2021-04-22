package cn.yue.base.middle.net.upload;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 * Created by yue on 2018/7/24
 */

public class ImageResult {


    @SerializedName("size")
    private int size;
    @SerializedName("width")
    private String width;
    @SerializedName("url")
    private String url;
    @SerializedName("error")
    private int error;
    @SerializedName("height")
    private String height;

    public void setSize(int size) {
        this.size = size;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setError(int error) {
        this.error = error;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public int getSize() {
        return size;
    }

    public String getWidth() {
        return width;
    }

    public String getUrl() {
        return url;
    }

    public int getError() {
        return error;
    }

    public String getHeight() {
        return height;
    }


    public static List<String> toListString(List<ImageResult> list){
        List<String> stringList = new ArrayList<>();
        if(list==null||list.size()==0){
            return stringList;
        }

        for(ImageResult imageResult : list){
            stringList.add(imageResult.getUrl());
        }
        return stringList;
    }


    public static String[] parse(List<ImageResult> list){
        String[] results = new String[2];
        StringBuilder url = new StringBuilder();
        StringBuilder size = new StringBuilder();
        for(ImageResult imageResult : list){
            url.append(imageResult.getUrl());
            url.append(";");


            size.append(imageResult.getHeight());
            size.append("*");
            size.append(imageResult.getWidth());
            size.append(";");
        }

        results[0] = url.toString();
        results[1] = size.toString();
        return results;
    }
}
