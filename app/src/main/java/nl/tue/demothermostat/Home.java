package nl.tue.demothermostat;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import org.thermostatapp.util.HeatingSystem;
import org.thermostatapp.util.WeekProgram;

import java.net.ConnectException;


public class Home extends Activity {
    SeekBar slider;
    TextView display;
    TextView status;
    String time; //"HH:MM"
    int displayTemp=210;
    double currentTemp = displayTemp/10.0;
    Thread updateDisplay ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //init common threads
        initThreads();

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
        status = (TextView)findViewById(R.id.status);


        //  init timetracker
        Thread timetracker = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        time = HeatingSystem.get("time");


                    } catch (Exception e) {
                        System.err.println("Error from getdata " + e);
                    }
                    System.out.println("time = " + time);
                    if (time.equals("23:59")) {
                        //update all
                        displayTemp = 210;
                        currentTemp = displayTemp / 10.0;
                        slider.setProgress(displayTemp);

                        status.post(new Runnable() {
                            @Override
                            public void run() {
                                status.setText("");
                            }
                        });
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

        //start timetracker
        timetracker.start();


        // couple the slider to the display
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar slider, int progress, boolean fromUser) {

                // get temperature from seekbar
                currentTemp = progress / 10.0;
                display.setText(String.valueOf(currentTemp + " \u2103"));

                //set temperature in db
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HeatingSystem.put("currentTemperature", String.valueOf(currentTemp));
                        } catch (Exception e) {
                            System.err.println("Error from getdata " + e);
                        }
                    }
                }).start();


            }

            @Override
            public void onStartTrackingTouch(SeekBar slider) {
                // todo: kill all treads to prevent errors
                if(updateDisplay.isAlive()){updateDisplay.interrupt();}
            }

            @Override
            public void onStopTrackingTouch(SeekBar slider) {
                updateDisplay.start();
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

    public void initThreads(){
            updateDisplay = new Thread(new Runnable() {
                double current = 0.0;
                double target = currentTemp;
                boolean b = false;

                @Override
                public void run() {
                    while (true) {
                        try {
                            current = Double.valueOf(HeatingSystem.get("currentTemperature"));
                        } catch (Exception e) {
                            System.err.println("Error from getdata " + e);
                        }

                        status.post(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("t = " + target);
                                System.out.println("c = " + current);
                                if (target < current) {
                                    status.setText(getString(R.string.cooling));
                                } else if (current < target) {
                                    status.setText(getString(R.string.heating));
                                } else {
                                    status.setText("");
                                    b = true;
                                }
                            }
                        });

                        if (b == true) {
                            break;
                        }

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    }
}
