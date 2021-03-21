package com.technek.parrotnight.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.print.PrintManager;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.widget.Button;
import android.os.Bundle;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.graphics.pdf.PdfDocument;

import androidx.annotation.RequiresApi;

import com.technek.parrotnight.database.DatabaseAccess;
import com.technek.parrotnight.models.TransactionsLedger;
import com.technek.parrotnight.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class print extends Activity {
    private Button btnprint;
    private DatabaseAccess dbaccess;
    private Context context;
    private static final String USERNAME_KEY = "CURRENT_USER";
    private static final String SHARED_PREFS = "sharedPrefs";
    String login;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    String companyname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.printlayout);
//btnprint=(Button)findViewById(R.id.print);
        context = this;
        dbaccess = DatabaseAccess.getInstance(context);
        SharedPreferences sharedpreferences;
        sharedpreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

         login = sharedpreferences.getString(USERNAME_KEY, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void printDocument(View view) {
        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        String jobName = this.getString(R.string.app_name) +
                " Document";

        printManager.print(jobName, new MyPrintDocumentAdapter(this),
                null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public class MyPrintDocumentAdapter extends PrintDocumentAdapter {
        Context context;
        private int pageHeight;
        private int pageWidth;
        public PdfDocument myPdfDocument;
        public int totalpages = 10;

        public MyPrintDocumentAdapter(Context context) {
            this.context = context;
        }

        @Override
        public void onLayout(PrintAttributes oldAttributes,
                             PrintAttributes newAttributes,
                             CancellationSignal cancellationSignal,
                             LayoutResultCallback callback,
                             Bundle metadata) {

            myPdfDocument = new PrintedPdfDocument(context, newAttributes);

            pageHeight =
                    newAttributes.getMediaSize().getHeightMils() / 1000 * 72;
            pageWidth =
                    newAttributes.getMediaSize().getWidthMils() / 1000 * 72;

            if (cancellationSignal.isCanceled()) {
                callback.onLayoutCancelled();
                return;
            }

            if (totalpages > 0) {
                PrintDocumentInfo.Builder builder = new PrintDocumentInfo
                        .Builder("Receipt1.pdf")
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(totalpages);

                PrintDocumentInfo info = builder.build();
                callback.onLayoutFinished(info, true);
            } else {
                callback.onLayoutFailed("Page count is zero.");
            }
        }

        @Override
        public void onWrite(final PageRange[] pageRanges,
                            final ParcelFileDescriptor destination,
                            final CancellationSignal cancellationSignal,
                            final WriteResultCallback callback) {
            try {
                List<TransactionsLedger> lst = dbaccess.getAllTransactions();

                for (int i = 0; i < lst.size(); i++) {
                    if (pageInRange(pageRanges, i)) {
                        PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pageWidth,
                                pageHeight, i).create();

                        PdfDocument.Page page =
                                myPdfDocument.startPage(newPage);

                        if (cancellationSignal.isCanceled()) {
                            callback.onWriteCancelled();
                            myPdfDocument.close();
                            myPdfDocument = null;
                            return;
                        }

                        drawPage(page, i, lst.get(i));
                        myPdfDocument.finishPage(page);
                    }
                }

                try {
                    myPdfDocument.writeTo(new FileOutputStream(
                            destination.getFileDescriptor()));
                } catch (IOException e) {
                    callback.onWriteFailed(e.toString());
                    return;
                } finally {
                    myPdfDocument.close();
                    myPdfDocument = null;
                }

                callback.onWriteFinished(pageRanges);
            } catch (Exception e) {
                callback.onWriteFailed(e.getMessage());
            }
        }
        private void drawPage(PdfDocument.Page page, int pagenumber, TransactionsLedger t) {
            Canvas canvas = page.getCanvas();

            pagenumber++; // Make sure page numbers start at 1

            int titleBaseLine = 72;
            int leftMargin = 14;

            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(30);
            canvas.drawText("Receipt Number  " + pagenumber,
                    leftMargin,
                    titleBaseLine,
                    paint);
            Date date = new Date();
            String strDateFormat = "hh:mm:ss a";
            DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
            String formattedDate= dateFormat.format(date);
            Log.d("TRN", t.toString());
            String entry = t.toString();
            paint.setTextSize(24);
            Path baseline = new Path();
            baseline.moveTo(0, 290);
            baseline.lineTo(550, 290);
            sharedPrefs = getSharedPreferences(getString(R.string.sharedPrefs), Context.MODE_PRIVATE);
            editor = sharedPrefs.edit();
            String fetch_config = sharedPrefs.getString(getString(R.string.fetch_config), null);
            setconfig(fetch_config);
            String businessname=companyname;
            String VAT="VAT #:";
            SpannableString spanString = new SpannableString(businessname);
            spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
            spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
            spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
            canvas.drawText(  businessname, 180, 120, paint);
            canvas.drawText(  VAT, 180, 145, paint);
            canvas.drawText(  "PIN: ", 180, 170, paint);
            canvas.drawText("Date " + formattedDate, 360, 230, paint);
            canvas.drawText(""+login,  50, 270, paint);
            canvas.drawText(""+t.getId(),420,270,paint);
            canvas.drawPath(baseline,paint);
            canvas.drawText("Item", 50, 350, paint);
            canvas.drawText("Quantity", 170, 350, paint);
            canvas.drawText("PRICE", 300, 350, paint);
            canvas.drawText("AMOUNT", 420, 350, paint);
            canvas.drawText(""+t.getItemcode(),50,400,paint);
            canvas.drawText(""+t.getQuantity(),180,400,paint);
            canvas.drawText(""+t.getSale_price(),300,400,paint);
            canvas.drawText(""+t.getTotalamount(),420,400,paint);
            canvas.drawLine(10,415,550,415,paint);
            canvas.drawText("TOTAL", 50, 450, paint);
            canvas.drawText(""+t.getTotalamount(), 420, 450, paint);
            canvas.drawText("Cash", 50, 490, paint);
            canvas.drawText(""+t.getTotalamount(), 420, 490, paint);
            canvas.drawLine(10,515,450,515,paint);
            canvas.drawText("LTRS", 50, 550, paint);
            canvas.drawText(""+t.getQuantity(), 180, 550, paint);
            canvas.drawText("CODE", 50, 590, paint);
            canvas.drawText("RATE", 170, 590, paint);
            canvas.drawText("VATABLE ", 270, 590, paint);
            canvas.drawText("VAT AMT", 420, 590, paint);
            canvas.drawText("A", 50, 620, paint);
            canvas.drawText("16%", 180, 620, paint);
            canvas.drawText(""+t.getTaxable(), 300, 620, paint);
            canvas.drawText(""+t.getTaxable(), 420, 620, paint);
            canvas.drawText("B", 50, 655, paint);
            canvas.drawText("levies", 180, 655, paint);
            canvas.drawText(""+t.getLevies(), 300, 655, paint);
            canvas.drawText(""+t.getLevies(), 420, 655, paint);
            canvas.drawText("C", 50, 690, paint);
            canvas.drawText("8%", 180, 690, paint);
            canvas.drawText(""+t.getTaxable(), 300, 690, paint);
            canvas.drawText(""+t.getTaxRate(), 420, 690, paint);
            canvas.drawText("**********************************************************************",10,720,paint);
            canvas.drawText("THANK YOU", 200, 750, paint);
            canvas.drawText("**********************************************************************",10,780,paint);

            if (pagenumber % 2 == 0)
                paint.setColor(Color.RED);
            else
                paint.setColor(Color.GREEN);

            PdfDocument.PageInfo pageInfo = page.getInfo();


            //canvas.drawCircle(pageInfo.getPageWidth() / 2,
            //      pageInfo.getPageHeight() / 2,
            //    150,
            //  paint);


        }

        private boolean pageInRange(PageRange[] pageRanges, int i) {
            for (i = 0; i < pageRanges.length; i++) {
                int page = 1;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if ((page >= pageRanges[i].getStart()) &&
                            (page <= pageRanges[i].getEnd()))
                        return true;
                }
            }
            return false; }
        }
    private void setconfig(String fetch_config) {
        try {
            JSONObject jsonObject = new JSONObject(fetch_config);
            JSONArray result = jsonObject.getJSONArray("company_settings");


            int i = 0;
            while (i < result.length()) {
                JSONObject dataobj = result.getJSONObject(i);


                companyname = dataobj.getString("FULL_NAME");
                i++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}