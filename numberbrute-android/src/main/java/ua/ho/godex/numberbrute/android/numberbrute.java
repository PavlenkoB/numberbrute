package ua.ho.godex.numberbrute.android;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import ua.ho.godex.mblogic.classes.MathBrute;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class numberbrute extends Activity {
    /**
     * Called when the activity is first created.
     */
    EditText console;
    EditText inputText;
    TextView progressText;
    ProgressBar progressBar;
    CheckBox allRes;
    Button run;
    TextView optionsTV;
    TextView version;

    MathBrute mathBrute;
    boolean allResTmp;

    private Handler mHandler = new Handler();
    private Runnable calculate = new Runnable() {
        public void run() {
            System.out.println("\nInput vuraz a+b=c");
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressText = (TextView) findViewById(R.id.progressText);
        console = (EditText) findViewById(R.id.console);
        inputText = (EditText) findViewById(R.id.inputText);
        allRes = (CheckBox) findViewById(R.id.allRes);
        run = (Button) findViewById(R.id.run);
        optionsTV = (TextView) findViewById(R.id.options);
        optionsTV.setText("__REMOVED__");
        inputText.setText("книга+книга+книга=наука");

    }

    public void brute(View view) {
        calculate calc = new calculate();
        calc.execute();
    }

    public void Update(String apkurl){
        try {
            URL url = new URL(apkurl);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();

            String PATH = Environment.getExternalStorageDirectory() + "/download/";
            File file = new File(PATH);
            file.mkdirs();
            File outputFile = new File(file, "app.apk");
            FileOutputStream fos = new FileOutputStream(outputFile);

            InputStream is = c.getInputStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();//till here, it works fine - .apk is download to my sdcard in download file

            Intent promptInstall = new Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse(PATH+"app.apk"))
                    .setType("application/android.com.app");
            startActivity(promptInstall);//installation is not working

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Update error!", Toast.LENGTH_LONG).show();
        }
    }

    class calculate extends AsyncTask<Void, Object, Void> {
        long timer;
        StringBuilder stringBuilder = new StringBuilder();

        String vuraz;
        Integer full;


        @Override
        protected void onPreExecute() {
            run.setEnabled(false);
            vuraz = inputText.getText().toString();
            timer = System.currentTimeMillis();
            allResTmp = allRes.isChecked();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mathBrute = new MathBrute(vuraz, allResTmp);
            mathBrute.startCalculation();

            /*full = ((Number) Math.pow(10, res.simbols.size())).intValue();
            while (res.simbols.get(res.simbols.size() - 1).getValue() < 10) {
                res.progress++;
                if (res.ressultArray.size() != 0 && !res.allAnswers) {
                    break;
                }
                res.charInc(0);
                publishProgress(res.simbols,full,res.progress);
                boolean colision = false;
                for (Spec spec : res.simbols) {
                    for (Spec spec1 : res.simbols) {
                        if (spec1.getValue() == spec.getValue() && spec1.getCharacter() != spec.getCharacter()) {
                            colision = true;
                            break;
                        }
                    }
                    if (colision)
                        break;
                }
                if (colision)
                    continue;
                res.copyStrToInt();
                //iteration++;
                if (res.mathResult().equals(res.intResult)) {
                    stringBuilder=new StringBuilder();
                    for (Spec spec : res.simbols) {
                        stringBuilder.append(spec.getCharacter()+":"+spec.getValue()+"|");
                    }

                    res.ressultArray.add(stringBuilder.toString());
                    res.ressultArray.add(res.inputString + "->" + res.intString);
                }
            }/**/
            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
            /*ArrayList<Spec> specs= (ArrayList<Spec>) values[0];
            stringBuilder = new StringBuilder();
            for (Spec spec : specs)
                stringBuilder.append(spec.getValue());
            progressText.setText(stringBuilder.toString());*/
            //progressBar.setProgress((int) (res.progress/full*100));

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            stringBuilder = new StringBuilder();
            stringBuilder.append(mathBrute.getResultOfCalculation()).append("\n");

            progressText.setText("__REMOVED__");
            console.setText(stringBuilder + "\n" + "time=" + (System.currentTimeMillis() - timer));/**/
            run.setEnabled(true);
        }
    }
}
