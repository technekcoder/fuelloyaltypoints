package com.technek.parrotnight.ui;

import android.annotation.TargetApi;
import android.content.Context;
import com.technek.parrotnight.R;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.technek.parrotnight.database.DatabaseAccess;
import com.technek.parrotnight.database.MaliplusDatabaseHelper;
import com.technek.parrotnight.models.TaxInfo;
import com.technek.parrotnight.models.TransactionsLedger;
import com.technek.parrotnight.util.Misc;

import net.sqlcipher.database.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.technek.parrotnight.util.Config.SAVE_TRANSACTION_REDEEM;
import static com.technek.parrotnight.util.Config.SAVE_TRANSACTION_SERVER;

public class malimain extends AppCompatActivity {
    RadioButton rbPMS;
    RadioButton rbDisel;
    RadioButton rbKero;
    RadioButton rbQuantity;
    RadioButton rbSalesAmount;
    String itemcode;
    private static final String USERNAME_KEY = "CURRENT_USER";
    public static String ledger_name, ledger_number;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    long rowId;
    Button Btn_Scan_card;
    private Button btnCalculate, btnOk, btnCancel, btnredeeem;
    private EditText txtSalesAmount;
    private EditText txtloyaltyAmountawarded;
    TransactionsLedger g_SelectedOrder;
    public long printId;
    private RadioButton radioresult;
    private EditText txtNonTaxable;
    private TextView tvcustomer, tv_ledger_number;
    FloatingActionButton fab_register_loyalty;
    private EditText txtTotalAmount;
    private EditText txtTaxable;
    public static final String HASH_PHRASE = "#PSL2018";
    private EditText txtPumpPrice;
    private EditText txtEightPercent;
    TextView tvCustomerpoints;
    private EditText txtQuantity;
    private RadioGroup rgfuelchoices;
    private MaliplusDatabaseHelper mydb;
    private DatabaseAccess dbAccess;
    private Context context;
    private static final String SHARED_PREFS = "sharedPrefs";
    public String currentuser;
    private Spinner spinnerlocation;
    double loyalty_redeem_amount;
    int counter = 0;
    View alertLayout;
    double balanceafterredeeming;
    double redeem_amount;
    double redeem_balance;

    enum ITEM_TYPES {
        AGO, DPK, PMS
    }

    public Double display_ledger_points;
    String display_ledger_name;
    double display_credit_points;
    double display_debit_points;
    double loyalty_points_amount;
    private static final String TAG = malimain.class.getSimpleName();
    String fetch_config;
    TaxInfo info;
    int DECIMAL_PLACES = 2;
    public double pay_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mali_mainpage);
        context = this;

        mydb = new MaliplusDatabaseHelper(this);
        dbAccess = DatabaseAccess.getInstance(context);
        info = new TaxInfo();
        dbAccess.locationMatchEnforcer();
        Log.d("NULL_T", "DN");
        initWidgets();
        toggleAmountQuantity();
        refreshUIData();

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        currentuser = sharedPreferences.getString(USERNAME_KEY, "");
        if (currentuser.equals("SUPER")) {
            tvCustomerpoints.setVisibility(View.VISIBLE);
            btnredeeem.setVisibility(View.VISIBLE);
        }

        Bundle bundle = getIntent().getExtras();
        try {
            String ledger_number = getIntent().getExtras().getString("ledger_number");
            display_ledger_name = getIntent().getStringExtra("ledger_name");
            display_ledger_points = Double.valueOf(getIntent().getStringExtra("ledger_customer_points"));

            tvcustomer.setText(display_ledger_name);
            tv_ledger_number.setText(ledger_number);
            tvCustomerpoints.setText(String.valueOf(display_ledger_points));
        } catch (NullPointerException e) {
            tvcustomer.setText("CASH_SALES");
            tv_ledger_number.setText("CASH_SALES");
        }

        sharedPrefs = getSharedPreferences(getString(R.string.sharedPrefs), Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        fetch_config = sharedPrefs.getString(getString(R.string.fetch_config), null);
        setconfig(fetch_config);
    }

    private void setconfig(String fetch_config) {
        try {
            JSONObject jsonObject = new JSONObject(fetch_config);
            JSONArray result = jsonObject.getJSONArray("company_settings");


            int i = 0;
            while (i < result.length()) {
                JSONObject dataobj = result.getJSONObject(i);

                loyalty_points_amount = dataobj.getDouble("VALUE1");
                loyalty_redeem_amount = dataobj.getDouble("VALUE2");
                Log.d("NULL_T", "" + loyalty_redeem_amount);
                i++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void initWidgets() {
        fab_register_loyalty = findViewById(R.id.fab_create_order);
        txtloyaltyAmountawarded = findViewById(R.id.loyaltypointamount);
        spinnerlocation = findViewById(R.id.spinnerlocation);
        txtSalesAmount = findViewById(R.id.fuelConsumed);
        btnCalculate = findViewById(R.id.calculate);
        txtTaxable = findViewById(R.id.taxableamount);
        tvCustomerpoints = findViewById(R.id.tvcustomerpoints);
        tv_ledger_number = findViewById(R.id.tv_ledger_number);
        txtNonTaxable = findViewById(R.id.nontaxableamount);
        txtPumpPrice = findViewById(R.id.pumpprice);
        txtEightPercent = findViewById(R.id.eightpercentamount);
        txtTotalAmount = findViewById(R.id.totalamount);
        txtQuantity = findViewById(R.id.quantityconsumed);
        btnOk = findViewById(R.id.btnok);
        btnredeeem = findViewById(R.id.btnredeeem);
        Btn_Scan_card = findViewById(R.id.Btn_scan_card);
        btnCancel = findViewById(R.id.btncancel);
        rgfuelchoices = findViewById(R.id.typefuel);
        rbPMS = findViewById(R.id.radiopms);
        rbDisel = findViewById(R.id.radiodiesel);
        rbKero = findViewById(R.id.radiokero);
        rbQuantity = findViewById(R.id.radioquantity);
        rbSalesAmount = findViewById(R.id.radiosaleprice);
        tvcustomer = findViewById(R.id.tvcustomername);
        txtSalesAmount.setNextFocusDownId(R.id.calculate);
        refreshSpinnerData();
        setListeners();
        tvCustomerpoints.setVisibility(View.GONE);
        btnredeeem.setVisibility(View.GONE);
        btnCalculate.setVisibility(View.GONE);


    }

    private void refreshSpinnerData() {
        ArrayList<String> locationSpinner = dbAccess.getLocations();
        Log.d("DISTINCT", "Loc Size--: " + locationSpinner.size());

        for (String loc : locationSpinner) {
            Log.d("DISTINCT", "Loc --: " + loc);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, locationSpinner);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerlocation.setAdapter(dataAdapter);

        spinnerlocation.setSelection(0);
    }

    private void setListeners() {
//TO MAKE TEXT CHANGE LISTENER COMPATIBLE WITH ALL PHONE,IF ERROR OCCURS REMOVE THIS TEXT CHANGE LISTENER

       /* txtQuantity.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                quantityKeyEvent();
                return true;
            }
            return false;
        });
        txtSalesAmount.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                salesAmountKeyEvent();
                return true;
            }
            return false;
        });*/
        fab_register_loyalty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity(activitybarcoderegister.class);
            }
        });

        btnCalculate.setOnClickListener(e -> calculate());

        btnCancel.setOnClickListener(e -> cancel());

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    saveTransaction();
                    //showAddItemDialog(context);
                }
                //  showAddItemDialog(context);
            }
        });
        btnredeeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRedeemItemDialog(context);
            }
        });

        tvCustomerpoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // tvCustomerpoints.setOnClickListener(v -> nextActivity(Profileactivity.class));
        Btn_Scan_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivity(activitybarcodescanner.class);
            }
        });
        rbDisel.setOnClickListener((view) -> refreshUIData());
        rbKero.setOnClickListener((view) -> refreshUIData());
        rbPMS.setOnClickListener((view) -> refreshUIData());

        rbSalesAmount.setOnClickListener((view) -> toggleAmountQuantity());
        rbQuantity.setOnClickListener((view) -> toggleAmountQuantity());

        spinnerlocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshUIData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void salesAmountKeyEvent() {
        double pumpprice = Misc.removeNull(txtPumpPrice.getText().toString());
        double salesamount = Misc.removeNull(txtSalesAmount.getText().toString());
        if (pumpprice == 0) {
            txtPumpPrice.setText(String.valueOf(Misc.round(info.getUnitPrice(), DECIMAL_PLACES)));
            pumpprice = info.getUnitPrice();
        }
        if (salesamount == 0) {
            txtPumpPrice.setText(String.valueOf(Misc.round(info.getUnitPrice(), DECIMAL_PLACES)));
            salesamount = info.getUnitPrice();
        }
        double litres = salesamount / pumpprice;
        txtQuantity.setText(String.valueOf(Misc.round(litres, DECIMAL_PLACES)));
    }

    private void quantityKeyEvent() {
        double quantity = Misc.removeNull(txtQuantity.getText().toString());
        double unitprice = Misc.removeNull(txtPumpPrice.getText().toString());

        if (quantity == 0) {
            //txtQuantity.setText(String.valueOf(1));
            //quantity = 1d;
        }
        if (unitprice == 0) {
            txtPumpPrice.setText(String.valueOf(info.getUnitPrice()));
            unitprice = info.getUnitPrice();
        }
        double fullprice = quantity * unitprice;
        txtSalesAmount.setText(String.valueOf(Misc.round(fullprice, DECIMAL_PLACES)));
    }

    private void refreshUIData() {
        String selectedLocation = spinnerlocation.getSelectedItem().toString();

        if (rbPMS.isChecked()) {
            rbPMS.setTypeface(null, Typeface.BOLD_ITALIC);
            rbDisel.setTypeface(null, Typeface.NORMAL);
            rbKero.setTypeface(null, Typeface.NORMAL);

            setItemDetails(ITEM_TYPES.PMS, selectedLocation);
        } else if (rbKero.isChecked()) {
            rbDisel.setTypeface(null, Typeface.NORMAL);
            rbPMS.setTypeface(null, Typeface.NORMAL);
            rbKero.setTypeface(null, Typeface.BOLD_ITALIC);
            setItemDetails(ITEM_TYPES.AGO, selectedLocation);
        } else {
            rbKero.setTypeface(null, Typeface.NORMAL);
            rbPMS.setTypeface(null, Typeface.NORMAL);
            rbDisel.setTypeface(null, Typeface.BOLD_ITALIC);
            setItemDetails(ITEM_TYPES.DPK, selectedLocation);
        }
    }

    private void setItemDetails(ITEM_TYPES type, final String locationDetails) {
        info.clear();
        info = dbAccess.getInfoByItemAndLocation(type.name(), locationDetails);
        //double eightPercent = 0.08 * info.getTaxable();
        txtPumpPrice.setText(String.valueOf(info.getUnitPrice()));
        txtSalesAmount.setText(String.valueOf(info.getUnitPrice()));
        txtQuantity.setText(String.valueOf(1));
        txtTaxable.setText(String.valueOf(info.getTaxable()));
        txtNonTaxable.setText(String.valueOf(info.getLevies()));
        txtloyaltyAmountawarded.setText("0");
        txtEightPercent.setText(String.valueOf(info.getLoyaty_points_amount()));
        txtTotalAmount.setText(String.valueOf(info.getUnitPrice()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        if (!currentuser.equals("SUPER")) {
            MenuItem Import = menu.findItem(R.id.customerslist);
            Import.setVisible(false);
        }return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
          int id = item.getItemId();
        if (id == R.id.settings) {
            Intent i = new Intent(getApplicationContext(), settings.class);
            startActivity(i);
        } else if (id == R.id.print) {
            Intent i = new Intent(getApplicationContext(), print.class);
            startActivity(i);
        }else if (id == R.id.customerslist) {
            Intent i = new Intent(getApplicationContext(), activitycustomerlist.class);
            startActivity(i);
        }
        else if (id == R.id.logout) {
            SharedPreferences sharedpreferences;
            sharedpreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            if (sharedpreferences.contains("CURRENT_USER")) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.remove("CURRENT_USER");
                editor.apply();
            }
            finish();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    private void toggleAmountQuantity() {
        if (rbSalesAmount.isChecked()) {
            txtSalesAmount.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                    try {
                        if (getCurrentFocus() == txtSalesAmount) {

                            Log.d("EVENT", "salesAmountKeyEvent: ");
                            double pumpprice = Misc.removeNull(txtPumpPrice.getText().toString());
                            double salesamount = Misc.removeNull(txtSalesAmount.getText().toString());
                       /* if (pumpprice == 0) {
                            txtPumpPrice.setText(String.valueOf(Misc.round(info.getUnitPrice(), DECIMAL_PLACES)));
                            pumpprice = info.getUnitPrice();
                        }
                        if (salesamount == 0) {
                            txtPumpPrice.setText(String.valueOf(Misc.round(info.getUnitPrice(), DECIMAL_PLACES)));
                            salesamount = info.getUnitPrice();
                        }*/
                            double litres = salesamount / pumpprice;
                            double EightPercent = salesamount - (salesamount / (1 + 0.8));
                            Log.d("NULL_T", "HELLO" + salesamount + pumpprice+EightPercent);

                            txtloyaltyAmountawarded.setText(String.valueOf(Misc.round(litres, DECIMAL_PLACES)));
                            txtEightPercent.setText(String.valueOf(Misc.round(EightPercent, DECIMAL_PLACES)));
                            txtloyaltyAmountawarded.setEnabled(false);
                            txtQuantity.setText(String.valueOf(Misc.round(litres, DECIMAL_PLACES)));
                            txtPumpPrice.setEnabled(false);
                        }
                    } catch (NumberFormatException e) {
                        s.clear();
                        s.insert(0, "0");
                        // The method will be recalled since s was changed
                    }
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
            });


        } else if (rbQuantity.isChecked()) {
            txtQuantity.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                    try {
                        if (getCurrentFocus() == txtQuantity) {

                            double pumpprice = Misc.removeNull(txtPumpPrice.getText().toString());
                            double litres = Misc.removeNull(txtQuantity.getText().toString());

                            double totalprice = litres * pumpprice;
                            double EightPercent = totalprice - (totalprice / (1 + 0.08));
                            Log.d("NULL_T", "HELLO" + totalprice + pumpprice+EightPercent);
                            txtEightPercent.setText(String.valueOf(Misc.round(EightPercent, DECIMAL_PLACES)));
                            txtEightPercent.setEnabled(false);
                            txtSalesAmount.setText(String.valueOf(Misc.round(totalprice, DECIMAL_PLACES)));
                            txtloyaltyAmountawarded.setText(String.valueOf(Misc.round(litres, DECIMAL_PLACES)));
                            txtloyaltyAmountawarded.setEnabled(false);
                            txtPumpPrice.setEnabled(false);
                        }
                    } catch (NumberFormatException e) {
                        s.clear();
                        s.insert(0, "0");
                        // The method will be recalled since s was changed
                    }
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
            });

        }
    }

    private void calculate() {
        salesAmountKeyEvent();
        if (validate()) {
            double quantity = Misc.removeNull(txtQuantity.getText().toString());
            double salesAmount = Misc.removeNull(txtSalesAmount.getText().toString());
            double pumpPrice = Misc.removeNull(txtPumpPrice.getText().toString());
            double vatable = info.getTaxable();
            double nonVatable = info.getLevies();
            double eightPercent = info.getTaxRate();
            double totalSalesAmount = salesAmount;
            double loyaltyawarded = 0;
            double totalVatable = 0;
            double totalEightPercent = 0;
            double totallevies = 0;

            if (rbQuantity.isChecked()) {
                totalSalesAmount = quantity * pumpPrice;
                totalVatable = quantity * vatable;
                //VATABLEAMOUNT-(VATABLEAMOUNT/(1+0.08))
                totalEightPercent = totalVatable - (totalVatable / (1 + eightPercent));
                totallevies = quantity * nonVatable;
                //  loyaltyawarded = (salesAmount / loyalty_points_amount);
                loyaltyawarded = (quantity);
            } else {
                quantity = salesAmount / pumpPrice;
                totalVatable = quantity * vatable;
                totalEightPercent = totalVatable - (totalVatable / (1.08));
                totallevies = quantity * nonVatable;
                loyaltyawarded = salesAmount / loyalty_points_amount;
            }
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            StringBuilder info = new StringBuilder();
            info.append("decimal:" + sharedPreferences.getString("decimal", ""));
            String showtext = info.toString();
            String substr = showtext.substring(8);
            DECIMAL_PLACES = substr == null ? 2 : substr.isEmpty() ? 2 : Integer.valueOf(substr);
            double totalAmount = totalSalesAmount;
            txtTaxable.setText(String.valueOf(Misc.round(totalVatable, DECIMAL_PLACES)));
            txtNonTaxable.setText(String.valueOf(Misc.round(totallevies, DECIMAL_PLACES)));
            txtEightPercent.setText(String.valueOf(Misc.round(totalEightPercent, DECIMAL_PLACES)));
            txtTotalAmount.setText(String.valueOf(Misc.round(totalAmount, DECIMAL_PLACES)));

            txtTaxable.setEnabled(false);
            txtEightPercent.setEnabled(false);
            txtNonTaxable.setEnabled(false);
            txtTotalAmount.setEnabled(false);
            btnOk.setEnabled(true);
        }
    }

    private boolean validate() {
        String message = "";
        boolean result = true;

        if (txtSalesAmount.getText().equals("")) {
            message = "Enter sales amount";
            txtSalesAmount.requestFocus();
        }
        if (txtQuantity.getText().equals("")) {
            message = "Fill in the number of litres";
            txtQuantity.requestFocus();
        }

        if (!message.isEmpty()) {
            Toast.makeText(malimain.this, message, Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private long saveTransaction() {

        counter++;
        Calendar calendar = Calendar.getInstance();
        String time = String.valueOf(calendar.get(Calendar.AM_PM));
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        SimpleDateFormat month_date = new SimpleDateFormat("MMMMMM");
        String currentmonth = String.valueOf(calendar.get(Calendar.MONTH));
        int selectedId = rgfuelchoices.getCheckedRadioButtonId();
        radioresult = findViewById(selectedId);
        if (validateSaveTransaction()) {
            String itemcode = radioresult.getText().toString();
            double fuelamount = Misc.removeNull(txtSalesAmount.getText().toString());
            double soldlitres = Misc.removeNull(txtQuantity.getText().toString());
            double tax = Misc.removeNull(txtTaxable.getText().toString());
            double nontax = Misc.removeNull(txtNonTaxable.getText().toString());
            String location = spinnerlocation.getSelectedItem().toString();
            double eightpercent = Misc.removeNull(txtEightPercent.getText().toString());
            double amounttotal = Misc.removeNull(String.valueOf(pay_amount));
            double loyalty_awarded = Misc.removeNull(txtloyaltyAmountawarded.getText().toString());
            String customer_ledger_number = tv_ledger_number.getText().toString();
            String timestamp = time;
            Calendar cal = Calendar.getInstance();
            String fiscal_month = currentmonth;
            String fiscal_year = year;
            double current_points = Misc.removeNull(tvCustomerpoints.getText().toString());
            double Closing_loyalty_points = current_points + loyalty_awarded;
            double display_debit_points;
            double display_credit_points;
            double debit_points;
            double credit_points;
            debit_points = 0 + loyalty_awarded;
            credit_points = 0;
            String processed_by = currentuser;
            rowId = dbAccess.insertTransactions(itemcode, location, amounttotal, fuelamount, soldlitres, loyalty_awarded, tax, nontax, eightpercent, "Ltrs", timestamp, fiscal_month, fiscal_year, processed_by, "NEW");
            String referencenumber = "CS0";
            sendtoPrimesoftserver(rowId, referencenumber, customer_ledger_number, ledger_name, itemcode, location,
                    fuelamount, loyalty_awarded, Closing_loyalty_points, debit_points, credit_points, amounttotal, soldlitres, tax, nontax, eightpercent, fiscal_month,
                    fiscal_year, processed_by);
            txtSalesAmount.setText("");
            txtQuantity.setText("");
            txtTaxable.setText("");
            txtNonTaxable.setText("");
            txtEightPercent.setText("");
            txtTotalAmount.setText("");

        }

        List<TransactionsLedger> lst = dbAccess.getAllTransactions();
        for (TransactionsLedger t : lst) {
        }
        return rowId;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void PrintReceipt(long printtrnid) {
        /*
            printId = printtrnid;
            Log.d("NULL_T", "print transaction id" + printId);
            // @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            PrintManager printManager = (PrintManager) this
                    .getSystemService(Context.PRINT_SERVICE);

            String jobName = this.getString(R.string.app_name) +
                    " Document";

            printManager.print(jobName, new malimain.MyPrintDocumentAdapter(this),
                    null);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public class MyPrintDocumentAdapter extends PrintDocumentAdapter {
            Context context;
            private int pageHeight;
            private int pageWidth;
            public PdfDocument myPdfDocument;
            public int totalpages = 1;

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
                            .Builder("Receipt1" + printId + ".pdf")
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
                TransactionsLedger trn = dbAccess.getCurrentTransaction(printId);
                Log.d("NULL_T",""+trn);
                // for (int i = 0; i < ; i++) {
                //if (pageInRange(pageRanges, i)) {
                PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pageWidth,
                        pageHeight, 0).create();

                PdfDocument.Page page =
                        myPdfDocument.startPage(newPage);

                if (cancellationSignal.isCanceled()) {
                    callback.onWriteCancelled();
                    myPdfDocument.close();
                    myPdfDocument = null;
                    return;
                }

                drawPage(page, trn);
                myPdfDocument.finishPage(page);
                //}
                //}

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
            }

            private void drawPage(PdfDocument.Page page, TransactionsLedger t) {
                Canvas canvas = page.getCanvas();

                //pagenumber++; // Make sure page numbers start at 1

                int titleBaseLine = 72;
                int leftMargin = 14;

                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setTextSize(30);
                canvas.drawText("Receipt  ",
                        leftMargin,
                        titleBaseLine,
                        paint);
                Date date = new Date();
                String strDateFormat = "hh:mm:ss a";
                DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
                String formattedDate = dateFormat.format(date);
                String entry = t.toString();
                paint.setTextSize(24);
                Path baseline = new Path();
                baseline.moveTo(0, 280);
                baseline.lineTo(550, 280);
                int customercode=100;
                String businessname = "MALIPLUS PETROL STATION";
                String address="P.O.BOX 123-20100";
                String location = "NAKURU";
                String physicaladdress="along nairobi-nakuru highway";
                String pinnumber="PIN:A123456789O";
                SpannableString spanString = new SpannableString(businessname);
                spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
                spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
                spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
                ((Canvas) canvas).drawText(businessname, 180, 90, paint);
                canvas.drawText(address, 180, 115, paint);
                canvas.drawText(location, 180, 140, paint);
                canvas.drawText(physicaladdress,180,165,paint);
                canvas.drawText(pinnumber,180,190,paint);
                canvas.drawText("Date " + formattedDate, 360, 230, paint);
                canvas.drawText("CS0/"+customercode +"/"+ t.getUserName(), 50, 270, paint);
                canvas.drawText("TRN :" + t.getId(), 420, 270, paint);
                canvas.drawPath(baseline, paint);
                canvas.drawText("Item", 50, 320, paint);
                canvas.drawText("Quantity", 170, 320, paint);
                canvas.drawText("PRICE", 300, 320, paint);
                canvas.drawText("AMOUNT", 420, 320, paint);
                canvas.drawText("" + t.getItemcode(), 50, 360, paint);
                canvas.drawText("" + t.getQuantity(), 180, 400, paint);
                canvas.drawText("" + t.getPumpPrice(), 300, 400, paint);
                canvas.drawText("" + t.getSalesAmount(), 420, 400, paint);
                canvas.drawLine(10, 415, 550, 415, paint);
                canvas.drawText("Cash", 50, 450, paint);
                canvas.drawText("" + t.getSalesAmount(), 420, 450, paint);
                canvas.drawLine(10, 465, 625, 465, paint);
                canvas.drawText("LTRS", 50, 490, paint);
                canvas.drawText("" + t.getQuantity(), 180, 490, paint);
                canvas.drawText("CODE", 50, 530, paint);
                canvas.drawText("RATE", 170, 530, paint);
                canvas.drawText(" ", 270, 530, paint);
                canvas.drawText("VAT AMT", 420, 530, paint);
                canvas.drawText("A", 50, 570, paint);
                canvas.drawText("Vatable", 180, 570, paint);
                canvas.drawText("", 300, 570, paint);
                canvas.drawText("" + t.getTaxable(), 420, 570, paint);
                canvas.drawText("B", 50, 610, paint);
                canvas.drawText("Non-Vat", 180, 610, paint);
                canvas.drawText("" , 300, 610, paint);
                canvas.drawText("" + t.getNonVatable(), 420, 610, paint);
                canvas.drawText("TOTAL", 50, 640, paint);
                canvas.drawText("" + t.getSalesAmount(), 420, 640, paint);
                canvas.drawLine(10, 655, 625, 655, paint);
                canvas.drawText("C", 50, 690, paint);
                canvas.drawText("8%", 180, 690, paint);
                canvas.drawText("", 300, 690, paint);
                canvas.drawText("" + t.getTaxRate(), 420, 690, paint);
                canvas.drawText("**********************************************************************", 10, 720, paint);
                canvas.drawText("THANK YOU FOR FUELING WITH US", 100, 750, paint);
                canvas.drawText("**********************************************************************", 10, 780, paint);


                paint.setColor(Color.RED);

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
                return false;
*/
    }


    private void sendtoPrimesoftserver(long rowId, String referencenumber, String customer_ledger_number, String ledger_name, String itemcode, String location,
                                       double fuelamount, double loyalty_awarded, double Closing_loyalty_points,
                                       double debit_points, double credit_points,
                                       double amounttotal, double soldlitres, double tax, double nontax, double eightpercent, String fiscal_month,
                                       String fiscal_year, String processed_by) {
        if (CheckNetworkConnection()) {

            StringRequest postRequest = new StringRequest(Request.Method.POST, SAVE_TRANSACTION_SERVER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                               JSONObject jsonObject = new JSONObject(response);
                                    String poststatus = jsonObject.getString("posted");
                                String NEW_POINTS = jsonObject.getString("NEW_POINTS");
                                showAlertDialog(NEW_POINTS);

                                if (poststatus.equals("PST")) {
                                    Log.d("NULL_Tuuu", poststatus+NEW_POINTS);
                                   // Transactionupdated(rowId);
                                }
                            } catch (Exception e) {
                                Toast.makeText(malimain.this, "Try again ", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }, error -> {
                Log.d("NULL_Tgtfcuyv", error.getMessage());
            }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("TRN_TYPE", "CSO");
                    params.put("ORDER_NUMBER", String.valueOf(rowId));
                    params.put("LEDGER_NUMBER", customer_ledger_number);
                    params.put("DESCRIPTION", customer_ledger_number);
                    params.put("SUOM", "LTS");
                    params.put("POINTS_EARNED", String.valueOf(loyalty_awarded));
                    params.put("ITEM_CODE", itemcode);
                    params.put("CLOSING", String.valueOf(Closing_loyalty_points));
                    params.put("DEBIT", String.valueOf(debit_points));
                    params.put("CREDIT", String.valueOf(credit_points));
                    params.put("POINTS_REDEEMED", String.valueOf(redeem_amount));
                    params.put("ITEM_LOCATION", location);
                    params.put("REFERENCE_NUMBER", referencenumber);
                    params.put("QUANTITY", String.valueOf(soldlitres));
                    params.put("AMOUNT", String.valueOf(fuelamount));
                    params.put("TAX_AMOUNT", String.valueOf(tax));
                    params.put("ORDER_AMOUNT", String.valueOf(amounttotal));
                    params.put("VALUE1", String.valueOf(eightpercent));
                    params.put("VALUE2", String.valueOf(nontax));
                    params.put("FISCAL_MONTH", fiscal_month);
                    params.put("FISCAL_YEAR", fiscal_year);
                    params.put("MODIFIED_BY", "SUPER");
                    Log.d("NULL_Tuuu", "" + params);

                    return params;
                }
            };
            postRequest.setRetryPolicy(new

                    DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(postRequest);
        } else {
        }
    }

    private void showAlertDialog(String new_points) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("customer name:"+display_ledger_name+"\n"+"previous points:"+display_ledger_points+"\nNEW POINTS:"
                +new_points);
        builder1.setCancelable(true);

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void Transactionupdated(long rowId) {
        SQLiteDatabase database = mydb.getReadableDatabase(HASH_PHRASE);
        Cursor c = database.rawQuery("UPDATE Transactions WHERE ID=" + rowId + "SET post_flag=PST", null);
    }

    private boolean CheckNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null & networkInfo.isConnected());
    }

    private boolean validateSaveTransaction() {

        return true;
    }


    private void cancel() {
        txtSalesAmount.setText("");
        txtEightPercent.setText("");
        txtTotalAmount.setText("");
        txtNonTaxable.setText("");
        txtTaxable.setText("");
    }

    private void nextActivity(Class nextClass) {
        overridePendingTransition(0, 0);
        startActivity(new Intent(context, nextClass));
        overridePendingTransition(0, 0);
    }


    public void customtoast() {
        Toast toast = Toast.makeText(this, "Data sent!", Toast.LENGTH_LONG);
        View toastView = toast.getView();
        TextView toastMessage = toastView.findViewById(android.R.id.message);
        toastMessage.setTextSize(15);
        toastMessage.setTextColor(Color.BLACK);
        toastMessage.setGravity(Gravity.CENTER);
        toastMessage.setCompoundDrawablePadding(10);
        toastView.setBackgroundColor(Color.LTGRAY);
        toast.show();
    }

    private void showAddItemDialog(Context context) {
        LayoutInflater inflater = getLayoutInflater();
        alertLayout = inflater.inflate(R.layout.payment_controller, null);
        EditText txt_balance_amount = alertLayout.findViewById(R.id.txt_balance_amount);
        EditText txt_amount_paid = alertLayout.findViewById(R.id.txt_amount_paid);
        EditText txt_amounttopay = alertLayout.findViewById(R.id.txt_amounttopay);
        EditText txt_redeem_amount = alertLayout.findViewById(R.id.txt_redeem_amount);
        final CheckBox cbToggle = alertLayout.findViewById(R.id.chkboxcalculate);

        txt_amounttopay.setText(txtSalesAmount.getText().toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(malimain.this);
        builder.setTitle("To pay   Ksh." + txtSalesAmount.getText().toString());
        builder.setMessage("minimum points to redeem must be >" + loyalty_points_amount);
        builder.setView(alertLayout);
        builder.setIcon(R.drawable.tick);
        builder.setPositiveButton("SUBMIT",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            saveTransaction();
                        }
                    }
                });
        builder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        txt_amount_paid.setText("");
                        txt_amounttopay.setText("");
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        txt_amount_paid.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    //s.clear();
                    redeem_amount = Misc.removeNull(txt_redeem_amount.getText().toString());
                    double paid_amount = Misc.removeNull(txt_amount_paid.getText().toString());
                    if (paid_amount == 0) {
                        txt_amount_paid.setText(txtSalesAmount.getText().toString());
                    } else {
                        double order_amount = Misc.removeNull(txtSalesAmount.getText().toString());
                        pay_amount = order_amount - redeem_amount;
                        double balance = paid_amount - order_amount;
                        txt_balance_amount.setText(String.valueOf(Misc.round(balance, DECIMAL_PLACES)));
                    }
                    txt_balance_amount.setEnabled(false);
                    txt_amounttopay.setEnabled(false);
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                } catch (NumberFormatException e) {
                    s.clear();
                    s.insert(0, "0");
                }
            }
        });
        cbToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txt_amount_paid.setText(txtSalesAmount.getText().toString());
                }
            }
        });

    }

    private void showRedeemItemDialog(Context context) {
        LayoutInflater inflater = getLayoutInflater();
        alertLayout = inflater.inflate(R.layout.layout_redeem, null);
        EditText txt_redeem_balance = alertLayout.findViewById(R.id.txt_redeem_balance);
        EditText txt_balance_amount = alertLayout.findViewById(R.id.txt_balance_amount);
        EditText txt_redeem_amount = alertLayout.findViewById(R.id.txt_redeem_amount);
        txt_redeem_balance.setEnabled(false);

        txt_redeem_balance.setText(tvCustomerpoints.getText().toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(malimain.this);
        builder.setTitle("Redeem");
        builder.setMessage("minimum points to redeem must be >" + loyalty_points_amount);
        builder.setView(alertLayout);
        builder.setIcon(R.drawable.tick);
        builder.setPositiveButton("SUBMIT",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            StringRequest postRequest = new StringRequest(Request.Method.POST, SAVE_TRANSACTION_REDEEM,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.d("NULL_Tuuu", response);
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                String poststatus = jsonObject.getString("posted");
                                                String NEW_POINTS = jsonObject.getString("NEW_POINTS");
                                                showAlertDialog(NEW_POINTS);
                                            } catch (Exception e) {
                                                Toast.makeText(malimain.this, "Try again ", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }, error -> {
                                Log.d("NULL_Tgtfcuyv", error.getMessage());
                            }
                            ) {
                                @Override
                                protected Map<String, String> getParams() {
                                    String customer_ledger_number = tv_ledger_number.getText().toString();
                                    double debit_points = 0.0;
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("LEDGER_NUMBER", customer_ledger_number);
                                    params.put("CLOSING", String.valueOf(balanceafterredeeming));
                                    params.put("DEBIT", String.valueOf(debit_points));
                                    params.put("CREDIT", String.valueOf(redeem_amount));
                                    params.put("POINTS_REDEEMED", String.valueOf(redeem_amount));

                                    return params;
                                }
                            };
                            postRequest.setRetryPolicy(new

                                    DefaultRetryPolicy(5000,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            requestQueue.add(postRequest);
                        }
                    }
                });
        builder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        txt_redeem_amount.setText("");
                        txt_balance_amount.setText("");
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        redeem_amount = Misc.removeNull(txt_redeem_amount.getText().toString());
        redeem_balance = Misc.removeNull(txt_redeem_balance.getText().toString());

        txt_redeem_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    redeem_amount = Misc.removeNull(txt_redeem_amount.getText().toString());
                    redeem_balance = Misc.removeNull(txt_redeem_balance.getText().toString());
                    balanceafterredeeming = redeem_balance - redeem_amount;
                    txt_balance_amount.setText(String.valueOf(Misc.round(balanceafterredeeming, DECIMAL_PLACES)));
                    txt_balance_amount.setEnabled(false);

                    if(redeem_balance>=redeem_amount){
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);}
                } catch (NumberFormatException e) {
                    s.clear();
                    s.insert(0, "0");
                }
            }
        });
    }
}

