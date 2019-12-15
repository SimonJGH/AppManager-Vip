package com.simon.appmanager_vip.https;

/**
 * 请求回调
 *
 * @param <T>
 */
public interface CommonCallBack<T> {

    void requestSuccess(T result);

    void requestError(T errorMsg);

    void requestFinished();
}

