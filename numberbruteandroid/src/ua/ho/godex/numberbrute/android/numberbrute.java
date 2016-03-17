package ua.ho.godex.numberbrute.android;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import ua.ho.godex.numberbrute.MathBrute;

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

    private Handler mHandler = new Handler();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressText = (TextView) findViewById(R.id.progressText);
        console = (EditText) findViewById(R.id.console);
        inputText = (EditText) findViewById(R.id.inputText);
        allRes = (CheckBox) findViewById(R.id.allRes);
        run=(Button) findViewById(R.id.run);
        inputText.setText("книга+книга+книга=наука");
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

    class calculate extends AsyncTask<Void, Object,Void> {
        long timer;
        StringBuilder stringBuilder = new StringBuilder();
        MathBrute res;
        String vuraz;
        boolean allResTmp;
        Integer full;


        @Override
        protected void onPreExecute() {
            run.setEnabled(false);

            vuraz = inputText.getText().toString();

            timer = System.currentTimeMillis();
            allResTmp=allRes.isChecked();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            res = new MathBrute(vuraz, MathBrute.Type.CHAR, allResTmp);
            res.mathResultStr();

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
            stringBuilder=new StringBuilder();
            for (String s : res.ressultArray) {
                stringBuilder.append(s + "\n");
            }
            progressText.setText("Results="+res.ressultArray.size()/2);
            console.setText(stringBuilder + "\n" + "time=" + (System.currentTimeMillis() - timer));/**/
            run.setEnabled(true);
        }
    }
}
