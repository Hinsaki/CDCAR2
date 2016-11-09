package allan.mydccar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;



public class Main2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void sc(View v) {
        goStart();
    }

    private void goStart() {
        Intent intent = new Intent(this, buttoncontrol.class);
        startActivity(intent);
    }

    public void photo(View v) {
        Intent intent = new Intent(this, Main4Activity.class);
        startActivity(intent);
    }

    public void data(View v) {
        Intent intent = new Intent(this, Main5Activity.class);
        startActivity(intent);
    }
}
