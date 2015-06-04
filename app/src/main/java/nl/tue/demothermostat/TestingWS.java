/**
 * Class for testing Web Service (http://wwwis.win.tue.nl/2id40-ws), 
 * gives a few examples of 
 * getting data from and uploading data to the server
 */
package nl.tue.demothermostat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.thermostatapp.util.*;

public class TestingWS extends Activity {

    Button getdata, putdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testing_ws);
		/* Use BASE_ADDRESS dedicated for your group, 
		 * change 101 to you group number
		 */
        HeatingSystem.BASE_ADDRESS = "http://wwwis.win.tue.nl/2id40-ws/26";
        HeatingSystem.WEEK_PROGRAM_ADDRESS = HeatingSystem.BASE_ADDRESS + "/weekProgram";

        getdata = (Button)findViewById(R.id.getdata);
        putdata = (Button)findViewById(R.id.putdata);

        getdata.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("BASE ADDRESS "+HeatingSystem.BASE_ADDRESS+"\n"+
                                    "Day "+HeatingSystem.get("day")/*+
									"time "+HeatingSystem.get("time")+
									"currentTemperature "+HeatingSystem.get("currentTemperature")+
									"dayTemperature "+HeatingSystem.get("dayTemperature")+
									"nightTemperature "+HeatingSystem.get("nightTemperature")+
									"weekProgramState "+HeatingSystem.get("weekProgramState")*/
                            );
							/* To work with program use 
							 * WeekProgram wpg = HeatingSystem.getWeekProgram();
							 */
                        } catch (Exception e) {
                            System.err.println("Error from getdata "+e);
                        }
                    }
                }).start();
            }
        });

        putdata.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
							/* test*/
                            HeatingSystem.put("time", "15:30");
                            WeekProgram wpg = HeatingSystem.getWeekProgram();
                            wpg.setDefault();
                            HeatingSystem.setWeekProgram(wpg);
                        } catch (Exception e) {
                            System.err.println("Error from getdata "+e);
                        }
                    }
                }).start();

            }
        });
    }
}