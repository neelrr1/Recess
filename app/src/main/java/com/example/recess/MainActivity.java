package com.example.recess;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs;
    Account user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final Context applicationContext = this.getApplicationContext();
        final Activity activity = this;

        final Button button = findViewById(R.id.login);
        final EditText username = findViewById(R.id.username);
        final EditText password = findViewById(R.id.password);
        final ProgressBar progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String baseURL = prefs.getString("baseURL", "N/A");
                if (baseURL.equals("N/A")) {
                    Toast.makeText(applicationContext, "School not yet set. Please choose your school.", Toast.LENGTH_LONG).show();
                    createAndShowDialog(activity);
                    return;
                }
                hideKeyboard(activity);

                try {
                    progressBar.setVisibility(View.VISIBLE);
                    user = APIFuncs.login(baseURL, username.getText().toString(), password.getText().toString());
                    Intent intent = new Intent(MainActivity.this, CourseView.class);
                    intent.putExtra("user", user);
                    MainActivity.this.startActivity(intent);
                    progressBar.setProgress(100);
                } catch (AuthException e) {
                    Toast.makeText(applicationContext, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        prefs = getSharedPreferences("RecessPrefs", MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!prefs.getBoolean("domainIsSet", false)) {
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            //text.setText(prefs.getString("baseURL", "Not set"));
            //TODO: Handle Clicking out of the Dialog and other shenanigans
            createAndShowDialog(this);
            //prefs.edit().putBoolean("domainIsSet", true).apply();
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void createAndShowDialog(final Context context) {
        final Dialog schoolDialog = new Dialog(context);
        schoolDialog.setContentView(R.layout.choose_school);
        schoolDialog.setTitle("Choose School");

        final AutoCompleteTextView dropdown = schoolDialog.findViewById(R.id.schools);
        final Map<String, String> schoolMap = APIFuncs.getSchools();
        String[] schools = new String[schoolMap.keySet().size()];
        schools = schoolMap.keySet().toArray(schools);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, schools);
        dropdown.setAdapter(adapter);

        Button submit = schoolDialog.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String school = dropdown.getText().toString();
                if (schoolMap.containsKey(school)) {
                    String baseURL = schoolMap.get(school);

                    prefs.edit().putString("baseURL", baseURL).apply();
                    prefs.edit().putBoolean("domainIsSet", true).apply();
                    schoolDialog.dismiss();

                } else {
                    Toast.makeText(context, "Invalid School Selected. Please try again.", Toast.LENGTH_LONG).show();
                    hideKeyboard((Activity) context);
                }
            }
        });

        schoolDialog.show();
    }
}
