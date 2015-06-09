package nl.tue.demothermostat;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import org.thermostatapp.util.HeatingSystem;
import org.thermostatapp.util.WeekProgram;


public class Home extends Activity {
    SeekBar slider;
    TextView display;
    int displayTemp=210;
    double currentTemp = displayTemp/10.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // init adresses
        HeatingSystem.BASE_ADDRESS = "http://wwwis.win.tue.nl/2id40-ws/26";
        HeatingSystem.WEEK_PROGRAM_ADDRESS = HeatingSystem.BASE_ADDRESS + "/weekProgram";

        // get temp
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("BASE ADDRESS "+HeatingSystem.BASE_ADDRESS+"\n"+
                            		"currentTemperature "+HeatingSystem.get("currentTemperature")
                    );
                    currentTemp= Double.parseDouble((HeatingSystem.get("currentTemperature")));
                    displayTemp=(int)(10.0*currentTemp);
                } catch (Exception e) {
                    System.err.println("Error from getdata "+e);
                }
            }
        }).start();

        //init the elements
        slider = (SeekBar)findViewById(R.id.seekBar2);
        display = (TextView)findViewById(R.id.DisplayTemp);
        display.setText(String.valueOf(currentTemp+ " \u2103"));
        slider.setProgress(displayTemp);



        // couple the slider to the display
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar slider, int progress, boolean fromUser) {
                currentTemp = progress / 10.0;
                display.setText(String.valueOf(currentTemp+ " \u2103"));

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
							/* test*/
                            HeatingSystem.put("currentTemperature", String.valueOf(currentTemp));
                        } catch (Exception e) {
                            System.err.println("Error from getdata "+e);
                        }
                    }
                }).start();
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
