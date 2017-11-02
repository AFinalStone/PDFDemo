package com.afinalstone.androidstudy.pdfdemo;

import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

/**
 * 生成pdf文档到SD卡下，byte.pdf,可以生成中文字符
 * 所用jar包是自己修改过的，将字体植入jar包内
 *
 * @author yt
 * @date 2015-1-15
 */
public class MainActivity extends MyBaseActivity {

    View contextView;
    ScrollView scrollView;

    @Override
    public void initView() {
        contextView = View.inflate(this, R.layout.activity_main, null);
        setContentView(contextView);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("PDF生成器/查阅器");
        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action01:
                        createCurrentViewPDF();
                        break;
                    case R.id.action02:
                        createScrollViewPDF();
                        break;
                    case R.id.action03:
                        createPDFByIText();
                        break;
                }
                return false;
            }
        });

    }

    @Override
    public void initData() {
        //请求相关权限
        requestBasicPermission();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 为toolbar创建Menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private OutputStream getOutputStream(String fileName) throws IOException {
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+fileName;
        File file = new File(filePath);
        if (!file.exists())
            file.createNewFile();
        return new FileOutputStream(filePath);
    }

    private View getCurrentView() {
        return contextView;
    }

    private int getCurrentViewWidth() {
        return contextView.getMeasuredWidth();
    }

    private int getCurrentViewHeight() {
        return contextView.getMeasuredHeight();
    }

    private View getScrollView() {
        return scrollView;
    }

    private int getScrollViewWidth() {
        return scrollView.getMeasuredWidth();
    }

    private int getScrollViewHeight() {
        // 获取listView实际高度
        int totalHeight = 0;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            totalHeight += scrollView.getChildAt(i).getHeight();
        }
        return totalHeight;
    }

    /**
     * 把当前页面的内容以PDF格式保存到SD卡种,这里使用的是系统本身的API，需要API版本不能低于19
     */
    public void createCurrentViewPDF() {

        View view = getCurrentView();
        // create a new document
        PdfDocument document = new PdfDocument();

        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(getCurrentViewWidth(), getCurrentViewHeight(), 1).create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        // draw something on the page
        view.draw(page.getCanvas());

        // finish the page
        document.finishPage(page);
        // add more pages
        // write the document content
        try {
            OutputStream os = getOutputStream("当前页面.pdf");
            document.writeTo(os);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // close the document
        document.close();

    }


    /**
     * 把当前页面ScrollView的全部内容以PDF格式保存到SD卡种,这里使用的是系统本身的API，需要API版本不能低于19
     */
    public void createScrollViewPDF() {

        View view = getScrollView();
        // create a new document
        PdfDocument document = new PdfDocument();

        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(getScrollViewWidth(), getScrollViewHeight(), 1).create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        // draw something on the page
        view.draw(page.getCanvas());

        // finish the page
        document.finishPage(page);
        // add more pages
        // write the document content
        try {
            OutputStream os = getOutputStream("长界面.pdf");
            document.writeTo(os);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // close the document
        document.close();

    }

    private void createPDFByIText() {
        String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/bbbb.pdf";
        //确保这个路径下面存放了一张图片,代码会把该图片添加到PDF中
        String imagepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/bbbb.png";

        int ColumnNums = 6;
        int CellNums = 24;
        Document document = new Document();
        try {

            PdfWriter.getInstance(document, new FileOutputStream(filepath));

            document.open();

            document.addAuthor("王胜利");
            document.addSubject("text");

            document.setPageSize(PageSize.A5);

            document.open();
            document.add(new Paragraph("Just tets...."));
            document.add(new Paragraph("================="));
            Image iamImage=Image.getInstance(imagepath);
            document.add(iamImage);
            document.addCreationDate();
            document.addTitle("网页");
            document.addKeywords("1234");


            PdfPTable table = new PdfPTable(ColumnNums);
            PdfPCell cell = new PdfPCell(new Paragraph("text"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(ColumnNums);
            table.addCell(cell);
            for (int i = 0; i < CellNums; i++) {
                cell = new PdfPCell(new Paragraph(i + " "));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置水平Alignment
                cell.setVerticalAlignment(Element.ALIGN_CENTER); // 设置垂直Alignment
                table.addCell(cell);
            }
            document.add(table);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        document.close();
        System.out.println("执行完毕");
    }





}
