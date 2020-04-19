package com.example.eceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class viewDownloadedPdf extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_downloaded_pdf);

        Intent i = getIntent();
        String dir = i.getExtras().getString("dir","");
        String filename = i.getExtras().getString("desc", "");
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), dir); //Directory
        File downloadedFile = new File (mediaStorageDir, filename);
        System.out.println("Filename : " + filename);
        System.out.println(downloadedFile.toString());
        PDFView pdfView = (PDFView) findViewById(R.id.viewPdf);
        pdfView.fromFile(downloadedFile).defaultPage(0).enableSwipe(true).load();
//        pdfView.fromFile(downloadedFile).defaultPage(1).enableSwipe(true).load();
//        pdfView.setVerticalScrollBarEnabled(true);
    }
}
