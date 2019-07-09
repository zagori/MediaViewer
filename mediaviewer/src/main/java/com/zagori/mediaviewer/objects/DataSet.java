package com.zagori.mediaviewer.objects;
import com.zagori.mediaviewer.interfaces.Formatter;

import java.util.List;

public class DataSet<T> {
    private List<T> data;
    private Formatter<T> formatter;

    public DataSet(List<T> data) {
        this.data = data;
    }

    public String format(int position) {
        return format(data.get(position));
    }

    public String format(T t) {
        if (formatter == null) return t.toString();
        else return formatter.format(t);
    }

    public List<T> getData() {
        return data;
    }
}
