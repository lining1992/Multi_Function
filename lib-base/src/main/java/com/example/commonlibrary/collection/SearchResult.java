package com.example.commonlibrary.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author : leixing
 * @date : 2018/6/27 14:35
 * <p>
 * description : xxx
 */
@SuppressWarnings("WeakerAccess")
public class SearchResult<T> {
    private T result;
    private List<Integer> indexList;

    public SearchResult() {
        indexList = new ArrayList<>();
    }

    public SearchResult(T result, Integer... index) {
        this();
        this.result = result;
        if (index != null) {
            indexList.addAll(Arrays.asList(index));
        }
    }

    public SearchResult(T result, List<Integer> indexList) {
        this();
        this.result = result;
        if (indexList != null) {
            this.indexList.addAll(indexList);
        }
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public List<Integer> getIndexList() {
        return indexList;
    }

    public void setIndexList(List<Integer> indexList) {
        this.indexList = indexList;
    }

    @Override
    public String toString() {
        return "\"SearchResult\": {"
                + "\"result\": \"" + result
                + ", \"indexList\": \"" + indexList
                + '}';
    }
}
