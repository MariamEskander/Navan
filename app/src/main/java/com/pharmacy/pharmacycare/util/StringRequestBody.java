package com.pharmacy.pharmacycare.util;

import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;

/**
 * Created by norhan.elsawi on 1/14/2018.
 */

public abstract class StringRequestBody extends RequestBody {

    private String data;

    public StringRequestBody(String data) {
        this.data = data;
    }

    public static RequestBody create(String content) {
        Charset charset = Util.UTF_8;
        MediaType contentType = MediaType.parse("text/plain");
        if (MediaType.parse("text/plain") != null) {
            charset = MediaType.parse("text/plain").charset();
            if (charset == null) {
                charset = Util.UTF_8;
                contentType = MediaType.parse(MediaType.parse("text/plain") + "; charset=utf-8");
            }
        }
        byte[] bytes = content.getBytes(charset);
        return create(contentType, bytes);
    }
}
