package com.example.leiwang.messagecardview.controller;

import android.support.annotation.NonNull;

import com.example.leiwang.messagecardview.model.NewsMessage;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lei.wang on 4/4/2017.
 */

public class MyMessageMap implements Map<String, List<NewsMessage>> {


    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object o) {
        return false;
    }

    @Override
    public boolean containsValue(Object o) {
        return false;
    }

    @Override
    public List<NewsMessage> get(Object o) {
        return null;
    }

    @Override
    public List<NewsMessage> put(String s, List<NewsMessage> newsMessages) {
        return null;
    }

    @Override
    public List<NewsMessage> remove(Object o) {
        return null;
    }

    @Override
    public void putAll(@NonNull Map<? extends String, ? extends List<NewsMessage>> map) {

    }

    @Override
    public void clear() {

    }

    @NonNull
    @Override
    public Set<String> keySet() {
        return null;
    }

    @NonNull
    @Override
    public Collection<List<NewsMessage>> values() {
        return null;
    }

    @NonNull
    @Override
    public Set<Entry<String, List<NewsMessage>>> entrySet() {
        return null;
    }
}
