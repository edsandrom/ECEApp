package com.example.eceapp;

import android.os.AsyncTask;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RetrievePDFStream extends AsyncTask<String, Void, InputStream> {

    //Coded By Ethan Steeves Feb 12/2020
    //PdfView is needed to visually show the PDF that InputStream renders
    //This class outputs PDF with -> new RetrievePDFStream(pdf).execute(url); pdf is the pdfView, url is the website where the pdf if stored
    private PDFView pdfView;

    public RetrievePDFStream(PDFView pdf){
        this.pdfView = pdf;
    }

    @Override
    protected InputStream doInBackground(String... strings) {
        InputStream inputStream = null;

        try{
            URL url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            if(urlConnection.getResponseCode() == 200){
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
            }
        }
        catch(IOException ex){
            return null;
        }

        return inputStream;
    }

    @Override
    protected void onPostExecute(InputStream inputStream){
        pdfView.fromStream(inputStream).load();
    }


}
