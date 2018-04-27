package com.example.mytest.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

/**
 * SharedPreferences 的处理类
 */
public class ExSharedPrefs {

    private SharedPreferences mSharedPrefs;

    public ExSharedPrefs(Context context, String name) {

        mSharedPrefs = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public Editor editor() {

        return mSharedPrefs.edit();
    }

    public boolean putString(String key, String value) {

        Editor editor = mSharedPrefs.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public String getString(String key) {

        return getString(key, null);
    }

    public String getString(String key, String defaultValue) {

        return mSharedPrefs.getString(key, defaultValue);
    }

    public boolean putInt(String key, int value) {

        Editor editor = mSharedPrefs.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public int getInt(String key) {

        return getInt(key, -1);
    }

    public int getInt(String key, int defaultValue) {

        return mSharedPrefs.getInt(key, defaultValue);
    }

    public boolean putLong(String key, long value) {

        Editor editor = mSharedPrefs.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public long getLong(String key) {

        return getLong(key, -1);
    }

    public long getLong(String key, long defaultValue) {

        return mSharedPrefs.getLong(key, defaultValue);
    }

    public boolean putFloat(String key, float value) {

        Editor editor = mSharedPrefs.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    public float getFloat(String key) {

        return getFloat(key, -1);
    }

    public float getFloat(String key, float defaultValue) {

        return mSharedPrefs.getFloat(key, defaultValue);
    }

    public boolean putBoolean(String key, boolean value) {

        Editor editor = mSharedPrefs.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public boolean getBoolean(String key) {

        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {

        return mSharedPrefs.getBoolean(key, defaultValue);
    }

    public boolean putSerializable(String key, Serializable value) {

        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {

            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            String dataString = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            putString(key, dataString);
            return true;

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            IOUtil.closeOutStream(oos);
            IOUtil.closeOutStream(baos);
        }

        return false;
    }

    /**
     * @param key
     * @return 没有值的时候返回null
     */
    public Serializable getSerializable(String key) {

        String objBase64 = getString(key, "");
        if (TextUtils.isEmpty(objBase64))
            return null;

        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {

            byte[] base64Bytes = Base64.decode(objBase64.getBytes(), Base64.DEFAULT);
            bais = new ByteArrayInputStream(base64Bytes);
            ois = new ObjectInputStream(bais);
            return (Serializable) ois.readObject();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            IOUtil.closeInStream(ois);
            IOUtil.closeInStream(bais);
        }

        return null;
    }

    /**
     * 批量保存
     *
     * @param params
     * @return
     */
    @SuppressWarnings("rawtypes")
    public boolean putBatch(Map<String, Object> params) {

        if (params == null || params.size() == 0)
            return false;

        Editor editor = mSharedPrefs.edit();
        try {

            Iterator iterator = params.entrySet().iterator();
            String key = null;
            Object value = null;
            while (iterator.hasNext()) {

                Map.Entry entry = (Map.Entry) iterator.next();
                key = entry.getKey().toString();
                value = entry.getValue();
                if (value instanceof Integer) {
                    editor.putInt(key, (Integer) value);
                } else if (value instanceof String) {
                    editor.putString(key, value.toString());
                } else if (value instanceof Float) {
                    editor.putFloat(key, (Float) value);
                } else if (value instanceof Long) {
                    editor.putLong(key, (Long) value);
                } else if (value instanceof Boolean) {
                    editor.putBoolean(key, (Boolean) value);
                }
            }

            return editor.commit();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    public boolean contains(String key) {

        return mSharedPrefs.contains(key);
    }

    public boolean removeKey(String key) {

        Editor editor = mSharedPrefs.edit();
        editor.remove(key);
        return editor.commit();
    }

    /**
     * 批量删除
     *
     * @param key
     * @return
     */
    public boolean removeKeys(String... key) {

        if (key == null || key.length == 0)
            return false;

        Editor editor = mSharedPrefs.edit();
        for (String string : key) {
            editor.remove(string);
        }
        return editor.commit();
    }

    public boolean removeAllKey() {

        Editor editor = mSharedPrefs.edit();
        editor.clear();
        return editor.commit();
    }
}
