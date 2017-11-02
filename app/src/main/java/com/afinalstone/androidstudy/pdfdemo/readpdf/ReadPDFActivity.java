package com.afinalstone.androidstudy.pdfdemo.readpdf;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.afinalstone.androidstudy.pdfdemo.MyBaseActivity;
import com.afinalstone.androidstudy.pdfdemo.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadPDFActivity extends MyBaseActivity {

    private String pdfFilePath;
    public static final String fileName = "pdfFileName";
    List<Bitmap> mlist = new ArrayList<Bitmap>();  //此处职位示例，正式项目请使用懒加载，否则可能导致OOM
    RecyclerView mRecyclerView;
    AllCommonAdapter<Bitmap> adapt;

    @Override
    public void initView() {
        setContentView(R.layout.activity_read_pdf);
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
    }

    @Override
    public void initData() {
        pdfFilePath = getIntent().getStringExtra(fileName);
        if (pdfFilePath == null) {
            Toast.makeText(this, "请先生成pdf", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    public void onReadPDF(View view) throws IOException {
        File filePDF = new File(pdfFilePath);
        if (filePDF.exists()) {
            Toast.makeText(this, "文件存在", Toast.LENGTH_SHORT).show();
        }
        ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.open(filePDF, ParcelFileDescriptor.MODE_READ_WRITE);
        // create a new renderer
        PdfRenderer renderer = new PdfRenderer(parcelFileDescriptor);

        // let us just render all pages
        final int pageCount = renderer.getPageCount();
        for (int i = 0; i < pageCount; i++) {
            PdfRenderer.Page mCurrentPage = renderer.openPage(i);

            //Bitmap必须是ARGB，不可以是RGB
            Bitmap bitmap = Bitmap.createBitmap(mCurrentPage.getWidth(), mCurrentPage.getHeight(),
                    Bitmap.Config.ARGB_8888);
         /*
         * 调用PdfRender.Page的render方法渲染bitmap
         *
         * render的参数说明：
         * destination : 要渲染的bitmap对象
         * destClip ：传一个矩形过去 矩形的尺寸不能大于bitmap的尺寸 最后渲染的pdf会是rect的大小 可为null
         * transform : 一个Matrix bitmap根据该Matrix图像进行转换
         * renderMode ：渲染模式 可选2种 RENDER_MODE_FOR_DISPLAY 和 RENDER_MODE_FOR_PRINT
         */
            mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            // close the page
            mCurrentPage.close();

            mlist.add(bitmap);
        }

        // close the renderer
        renderer.close();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapt = new AllCommonAdapter<Bitmap>(this, R.layout.item_image, mlist) {
            @Override
            public void convert(ViewHolder holder, Bitmap bitmap) {
                holder.setImageBitmap(R.id.imageView, bitmap);
            }
        };
        mRecyclerView.setAdapter(adapt);
    }


}
