package dev.baofeng.com.supermovie.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.huangyong.downloadlib.model.Params;

import java.io.File;
import java.io.IOException;

import dev.baofeng.com.supermovie.R;
import dev.baofeng.com.supermovie.adapter.DownListAdapter;
import dev.baofeng.com.supermovie.domain.AppUpdateInfo;
import dev.baofeng.com.supermovie.services.DownLoadService;
import dev.baofeng.com.supermovie.utils.DownloadUtil;

public class UpdateDialog extends Dialog {

    private AppUpdateInfo info;

    public UpdateDialog(Context context, AppUpdateInfo info) {
        super(context,  R.style.Dialog_Fullscreen);
        this.info = info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_layout);
        TextView title =findViewById(R.id.update_title);
        TextView content =findViewById(R.id.update_content);
        Button confirm =findViewById(R.id.bt_confirm);
        Button cancel =findViewById(R.id.bt_cancel);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String downloadUrl = info.getData().getDownloadUrl();
                if (TextUtils.isEmpty(downloadUrl)){
                    return;
                }
                Intent intent = new Intent(getContext(), DownLoadService.class);
                intent.putExtra(Params.DURL,downloadUrl);
                getContext().startService(intent);
                dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        title.setText("发现新版本："+info.getData().getVersion());

        content.setText(info.getData().getUpdateMsg());



    }


    private void installApp(String savePath) {

        Uri uri = Uri.fromFile(new File(savePath));

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri,"application/vnd.android.package-archive");

        getContext().startActivity(intent);
    }
}
