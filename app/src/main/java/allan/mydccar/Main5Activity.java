package allan.mydccar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;



public class Main5Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
    }

    public void back(View v) {
        goBack();
    }

    private void goBack() {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }
}
