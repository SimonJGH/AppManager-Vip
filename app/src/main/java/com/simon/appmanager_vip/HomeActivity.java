package com.simon.appmanager_vip;

import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.simon.appmanager_vip.adapter.CommonAdapter;
import com.simon.appmanager_vip.adapter.CommonViewHolder;
import com.simon.appmanager_vip.https.ApiConstanse;
import com.simon.appmanager_vip.https.CommonCallBack;
import com.simon.appmanager_vip.https.HttpUtils;
import com.simon.appmanager_vip.https.entity.UpdateApkInfoInputBean;
import com.simon.appmanager_vip.https.entity.UpdateApkInfoOutputBean;
import com.simon.appmanager_vip.https.entity.UploadApkInfoOutputBean;
import com.simon.appmanager_vip.utils.PopupWindowUtils;
import com.simon.appmanager_vip.utils.ToastUtils;
import com.simon.appmanager_vip.view.LoadingDialog;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@ContentView(R.layout.activity_main)
@SuppressWarnings("all")
public class HomeActivity extends BaseActivity {
    @ViewInject(R.id.tv_apk_info)
    TextView mTv_apk_info;

    @ViewInject(R.id.tv_app_name)
    TextView mTv_app_name;

    @ViewInject(R.id.tv_apk_version)
    TextView mTv_apk_version;

    @ViewInject(R.id.et_apk_version)
    EditText mEt_apk_version;

    @ViewInject(R.id.et_app_introduce)
    EditText mEt_app_introduce;

    private String apkInfo = "";//app名称
    private String appName = "";//app名称
    private String apkType = "";//apk版本
    private boolean isExit = false;// 是否退出
    private String apkPath = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/Pictures/";
    private List<String> mFileNameList = new ArrayList<>();
    private String apkInfoUrl;//apk上传路径
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoadingDialog = new LoadingDialog(HomeActivity.this);

        initApkInfo();
    }

    @Event(value = {R.id.tv_apk_info, R.id.tv_app_name, R.id.tv_apk_version, R.id.tv_apk_upload, R.id.tv_apk_update})
    private void clickButton(View view) {
        switch (view.getId()) {
            case R.id.tv_apk_info:
                if (mFileNameList.isEmpty()) {
                    ToastUtils.getInstance().showShortToast("apk不存在，请查看文件管理！");
                } else {
                    chooseApk();
                }
                break;
            case R.id.tv_app_name:
                if (TextUtils.isEmpty(apkInfo)) {
                    ToastUtils.getInstance().showShortToast("请选择上传apk！");
                } else {
                    chooseApp();
                }
                break;
            case R.id.tv_apk_version:
                if (TextUtils.isEmpty(appName)) {
                    ToastUtils.getInstance().showShortToast("请选择上传app名称！");
                } else {
                    chooseVersion();
                }
                break;
            case R.id.tv_apk_upload:
                uploadApk();
                break;
            case R.id.tv_apk_update:
                updateApk();
                break;
        }
    }

    /**
     * 初始化apk信息
     */
    private void initApkInfo() {
        File file = new File(apkPath);
        if (file.exists()) {
            File[] fileFolders = file.listFiles();
            for (File fileItem : fileFolders) {
                String itemName = fileItem.getName();
                if (itemName.endsWith("apk")) {
                    mFileNameList.add(itemName);
                }
            }
        } else {
            ToastUtils.getInstance().showShortToastBottom("文件夹不存在！");
        }
    }

    /**
     * 选择apk
     */
    private void chooseApk() {
        View inflate = LayoutInflater.from(HomeActivity.this).inflate(R.layout.layout_pop_apk, null);

        RecyclerView mRecyclerView = inflate.findViewById(R.id.recyclerview);
        LinearLayoutManager llm = new LinearLayoutManager(HomeActivity.this);
        mRecyclerView.setLayoutManager(llm);
        // 如果Item够简单，高度是确定的，打开FixSize将提高性能
        mRecyclerView.setHasFixedSize(true);
        // 设置Item默认动画，加也行，不加也行
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setNestedScrollingEnabled(false);

        CommonAdapter mCommonAdapter = new CommonAdapter(HomeActivity.this, R.layout.layout_app_item, mFileNameList);
        mCommonAdapter.setItemDataListener(new CommonAdapter.ItemDataListener<String>() {
            @Override
            public void setItemData(CommonViewHolder holder, String dataBean) {
                TextView tv_apk_name = holder.getView(R.id.tv_apk_name);
                tv_apk_name.setText(dataBean);
            }
        });

        mCommonAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {

            @Override
            public void setOnItemClickListener(View view, int position) {
                apkInfo = mFileNameList.get(position);
                mTv_apk_info.setText(apkInfo);
                PopupWindowUtils.getInstance().closePop();
            }

            @Override
            public void setOnItemLongClickListener(View view, int position) {
                //  Log.i("Simon", "setOnItemLongClickListener = " + position);
            }
        });

        mCommonAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mCommonAdapter);
        PopupWindowUtils.getInstance().createScalePopupWindow(HomeActivity.this, inflate, mTv_apk_info);
    }

    /**
     * 选择app
     */
    private void chooseApp() {
        View inflate = LayoutInflater.from(HomeActivity.this).inflate(R.layout.layout_pop_app_name, null);
        inflate.findViewById(R.id.tv_hq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appName = "hq_";
                mTv_app_name.setText("红旗");
                PopupWindowUtils.getInstance().closePop();
            }
        });
        inflate.findViewById(R.id.tv_bty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appName = "bty_";
                mTv_app_name.setText("奔腾苑");
                PopupWindowUtils.getInstance().closePop();
            }
        });
        inflate.findViewById(R.id.tv_bty_teacher).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appName = "bty_teacher_";
                mTv_app_name.setText("奔腾苑-讲师端");
                PopupWindowUtils.getInstance().closePop();
            }
        });
        inflate.findViewById(R.id.tv_bty_manager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appName = "bty_manager_";
                mTv_app_name.setText("奔腾苑-管理端");
                PopupWindowUtils.getInstance().closePop();
            }
        });
        inflate.findViewById(R.id.tv_wmhk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appName = "wmhk_";
                mTv_app_name.setText("维玛荟客");
                PopupWindowUtils.getInstance().closePop();
            }
        });

        PopupWindowUtils.getInstance().createScalePopupWindow(HomeActivity.this, inflate, mTv_app_name);
    }

    /**
     * 选择版本
     */
    private void chooseVersion() {
        View inflate = LayoutInflater.from(HomeActivity.this).inflate(R.layout.layout_pop_apk_version, null);
        inflate.findViewById(R.id.tv_version_publish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apkType = "publish";
                mTv_apk_version.setText("正式版");
                PopupWindowUtils.getInstance().closePop();
            }
        });
        inflate.findViewById(R.id.tv_version_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apkType = "test";
                mTv_apk_version.setText("测试版");
                PopupWindowUtils.getInstance().closePop();
            }
        });
        inflate.findViewById(R.id.tv_version_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apkType = "chat";
                mTv_apk_version.setText("聊聊版");
                PopupWindowUtils.getInstance().closePop();
            }
        });

        PopupWindowUtils.getInstance().createScalePopupWindow(HomeActivity.this, inflate, mTv_apk_version);
    }

    /**
     * 上传apk
     */
    private void uploadApk() {
        if (TextUtils.isEmpty(apkInfo)) {
            ToastUtils.getInstance().showShortToast("请选择上传apk！");
            return;
        }
        mLoadingDialog.show();
        HttpUtils.getInstance().uploadFile(ApiConstanse.upload_apk, apkPath + apkInfo, null, new CommonCallBack<String>() {
            @Override
            public void requestSuccess(String result) {
                UploadApkInfoOutputBean uploadApkInfo = new Gson().fromJson(result, UploadApkInfoOutputBean.class);
                apkInfoUrl = uploadApkInfo.getUrl();
                ToastUtils.getInstance().showShortToast(uploadApkInfo.getMsg());
                Log.i("Simon", "上传apk：requestSuccess = " + result);
            }

            @Override
            public void requestError(String errorMsg) {
                mLoadingDialog.dismiss();
                ToastUtils.getInstance().showShortToast(errorMsg);
                Log.i("Simon", "上传apk：requestError = " + errorMsg);
            }

            @Override
            public void requestFinished() {
                mLoadingDialog.dismiss();
                Log.i("Simon", "上传apk：requestFinished ");
            }
        });


    }

    /**
     * 更新apk
     */
    private void updateApk() {
        if (TextUtils.isEmpty(apkInfoUrl)) {
            ToastUtils.getInstance().showShortToast("请上传apk到服务器！");
            return;
        }
        if (TextUtils.isEmpty(appName)) {
            ToastUtils.getInstance().showShortToast("请选择上传app名称！");
            return;
        }
        if (TextUtils.isEmpty(apkType)) {
            ToastUtils.getInstance().showShortToast("请选择上传app类型！");
            return;
        }
        String apk_version = mEt_apk_version.getText().toString();
        if (TextUtils.isEmpty(apk_version)) {
            ToastUtils.getInstance().showShortToast("请输入apk版本号！");
            return;
        }
        String app_introduce = mEt_app_introduce.getText().toString();
        if (TextUtils.isEmpty(app_introduce)) {
            ToastUtils.getInstance().showShortToast("请输入app版本介绍！");
            return;
        }

        UpdateApkInfoInputBean inputBean = new UpdateApkInfoInputBean();
        inputBean.setFile(apkInfoUrl);
        inputBean.setType(appName + apkType);
        inputBean.setVresion(apk_version);
        inputBean.setIntroduce(app_introduce);
        Log.i("Simon", "更新apk：inputBean = " + inputBean.toString());

        HttpUtils.getInstance().postMethod(ApiConstanse.update_apk, inputBean, new CommonCallBack<String>() {
            @Override
            public void requestSuccess(String result) {
                UpdateApkInfoOutputBean updateApkInfo = new Gson().fromJson(result, UpdateApkInfoOutputBean.class);
                apkInfo = "";
                apkInfoUrl="";
                appName="";
                apkType="";
                mTv_apk_info.setText("请选择");
                mTv_app_name.setText("请选择");
                mTv_apk_version.setText("请选择");
                mEt_apk_version.setText("");
                mEt_apk_version.setHint("请输入apk版本号");
                mEt_app_introduce.setText("");
                mEt_app_introduce.setHint("请输入app更新介绍");
                ToastUtils.getInstance().showShortToast(updateApkInfo.getMsg());
                Log.i("Simon", "更新apk：requestSuccess = " + result);
            }

            @Override
            public void requestError(String errorMsg) {
                ToastUtils.getInstance().showShortToast(errorMsg);
                Log.i("Simon", "更新apk：requestError = " + errorMsg);
            }

            @Override
            public void requestFinished() {
                Log.i("Simon", "更新apk：requestFinished");
            }
        });

    }

    /*双击退出*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();// 这里也可以弹出对话框
        }
        return false;
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            ToastUtils.getInstance().showShortToastBottom("再按一次退出程序");
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            finishActivity();
        }
    }

}
