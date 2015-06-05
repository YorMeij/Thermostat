package nl.tue.demothermostat;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;


public class Home extends Activity {
    SeekBar slider;
    TextView display;
    int displayTemp=210;
    double currentTemp = displayTemp/10.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //init the elements
        slider = (SeekBar)findViewById(R.id.seekBar2);
        display = (TextView)findViewById(R.id.DisplayTemp);
        display.setText(String.valueOf(currentTemp));
        slider.setProgress(displayTemp);

        // couple the slider to the display
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar slider, int progress, boolean fromUser) {
                currentTemp = progress / 10.0;
                display.setText(String.valueOf(currentTemp));
            }

            @Override
            public void onStartTrackingTouch(SeekBar slider) {

            }

            @Override
            public void onStopTrackingTouch (SeekBar slider){

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
