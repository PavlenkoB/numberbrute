package ua.ho.godex.numberbrute.android;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import ua.ho.godex.parsedString;
import ua.ho.godex.parsedString.*;

import java.util.ArrayList;

public class numberbrute extends Activity {
    /**
     * Called when the activity is first created.
     */
    EditText console;
    EditText inputtext;
    TextView progressText;
    ProgressBar progressBar;
    CheckBox allRes;

    private Handler mHandler = new Handler();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressText = (TextView) findViewById(R.id.progressText);
        console = (EditText) findViewById(R.id.console);
        inputtext = (EditText) findViewById(R.id.inputtext);
        allRes = (CheckBox) findViewById(R.id.allRes);
    }

    public void brute(View view) {
        calculate calc = new calculate();
        calc.execute();
    }

    private Runnable calculate = new Runnable() {
        public void run() {

            System.out.println("\nInput vuraz a+b=c");


        }
    };

    class calculate extends AsyncTask<Void, ArrayList<Spec>,Void> {
        long timer;
        StringBuilder stringBuilder = new StringBuilder();
        parsedString res;
        String vuraz;
        boolean allResTmp;
        Double full;


        @Override
        protected void onPreExecute() {
            vuraz = inputtext.getText().toString();

            timer = System.currentTimeMillis();
            allResTmp=allRes.isChecked();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            res = new parsedString(vuraz, parsedString.Type.CHAR, allResTmp);

            full = Math.pow(10, res.simbols.size());
            while (res.simbols.get(res.simbols.size() - 1).getValue() < 10) {
                //res.progress++;
                if (res.ressultarray.size() != 0 && !res.allAnswers) {
                    break;
                }
                res.charinc(0);
                publishProgress(res.simbols);
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
                    res.ressultarray.add(res.inputString + "->" + res.intString);
                }/**/
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(ArrayList<Spec>... values) {
            super.onProgressUpdate(values);
            ArrayList<Spec> specs=values[0];
            stringBuilder = new StringBuilder();
            for (Spec spec : specs)
                stringBuilder.append(spec.getValue());
            progressText.setText(stringBuilder.toString());
            progressBar.setProgress((int) (full/Integer.parseInt(stringBuilder.toString())/100));

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            stringBuilder=new StringBuilder();
            for (String s : res.ressultarray) {
                stringBuilder.append(s + "\n");
            }
            progressText.setText("Results="+res.ressultarray.size());
            console.setText(stringBuilder + "\n" + "time=" + (System.currentTimeMillis() - timer));/**/
        }
    }
}
