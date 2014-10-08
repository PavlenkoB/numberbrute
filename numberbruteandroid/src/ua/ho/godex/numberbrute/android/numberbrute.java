package ua.ho.godex.numberbrute.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import ua.ho.godex.parsedString;

public class numberbrute extends Activity {
    /**
     * Called when the activity is first created.
     */
    EditText console;
    EditText inputtext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        console=(EditText)findViewById(R.id.console);
        inputtext=(EditText)findViewById(R.id.inputtext);
    }

    public void brute(View view) {
        console.setText("");
        Toast.makeText(this, "Зачем вы нажали?", Toast.LENGTH_SHORT).show();
        long timer=System.currentTimeMillis();
        System.out.println("\nInput vuraz a+b=c");
        String vuraz = inputtext.getText().toString();
        parsedString res = new parsedString(vuraz, parsedString.Type.CHAR);

        res.mathResultstr();
        StringBuilder stringBuilder = new StringBuilder();
/*for (String s :res.ressultarray) {
    res.ressultarray.toString();
    stringBuilder.append(console.getText() + s+"\n");
}*/
        console.setText(res.ressultarray.toString()+"\n"+"time="+(System.currentTimeMillis()-timer));/**/
    }
}
