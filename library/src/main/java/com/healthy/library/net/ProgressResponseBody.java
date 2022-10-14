package com.healthy.library.net;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * @author Li
 * @date 2019/07/16 11:11
 * @des
 */
public class ProgressResponseBody extends ResponseBody {
    private ResponseBody mResponseBody;
    private ProgressListener mProgressListener;
    private BufferedSource mBufferedSource;


    public ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
        mResponseBody = responseBody;
        mProgressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (mBufferedSource == null) {
            mBufferedSource = Okio.buffer(source(mResponseBody.source()));
        }
        return mBufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                mProgressListener.onProgressChanged(totalBytesRead,
                        mResponseBody.contentLength(), bytesRead == -1);
                return bytesRead;
            }
        };
    }
}
