package ua.ho.godex.numberbrute.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import ua.ho.godex.parsedString;

public class numberbrute extends Activity {
    /**
     * Called when the activity is first created.
     */
    EditText console;
    EditText inputtext;
    TextView progressText;
    ProgressBar progressBar;
    CheckBox allRes;


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
        console.setText("");
        Toast.makeText(this, "Зачем вы нажали?", Toast.LENGTH_SHORT).show();
        long timer = System.currentTimeMillis();
        System.out.println("\nInput vuraz a+b=c");
        String vuraz = inputtext.getText().toString();
        parsedString res = new parsedString(vuraz, parsedString.Type.CHAR,allRes.isChecked());

        res.mathResultstr();
        StringBuilder stringBuilder = new StringBuilder();
for (String s :res.ressultarray) {
    stringBuilder.append(s+"\n");
}
        console.setText( stringBuilder+ "\n" + "time=" + (System.currentTimeMillis() - timer));/**/
    }
}
