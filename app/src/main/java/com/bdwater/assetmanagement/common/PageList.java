package com.bdwater.assetmanagement.common;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hegang on 17/11/2.
 */

public class PageList<T> {
    List<T> list;
    int pageSize;
    boolean isEnding;
    boolean isEmpty;
    public PageList(int pageSize) {
        this.list = new ArrayList<>();
        this.pageSize = pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }
    public boolean getIsEnding() {
        return isEnding | pageSize == -1;
    }
    public int size() {
        return list.size();
    }
    public T get(int index) {
        return list.get(index);
    }
    public void clear() {
        list.clear();
        this.isEmpty = false;
        this.isEnding = false;
    }
    public void addAll(@NonNull List<T> addList) {
        if(addList.size() == 0) {
            isEnding = true;
            isEmpty = list.size() == 0;
            return;
        }
        list.addAll(addList);
        if(pageSize != -1)
            this.isEnding = addList.size() < pageSize;
    }
    public boolean getIsEmpty() {
        return isEmpty;
    }
}
