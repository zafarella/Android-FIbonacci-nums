package com.iheart.infinitelyscrollablefibonacci.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * created by z1
 */
public class MainActivity extends Activity {


    // adapter for the list
    private ArrayAdapter<String> adapter;

    // initial Fibonacci numbers - by default they will not come to UI as next is calculated.
    private static BigInteger fib1 = BigInteger.ZERO, fib2=BigInteger.ONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll()
                .build());

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);

        // will use default list view with simple text vew as a row.
        adapter = new ArrayAdapter(this, R.layout.activity_fibonaci_list, R.id.rowTextView);

        // Fill initial Fibonacci 50 items
        for (short i = 0; i < 50; i++) {
            adapter.add(getFibNum().toString());
        }

        listView.setAdapter(adapter);

        // Attach the listener to the AdapterView onCreate
        listView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                new GenerateNextNumbers().execute();
            }
        });
    }

    /**
     *  Returns next Fibonacci number. Two previous are stored in the class fields fib1 and fib2.
     * @return Next Fibonacci number in current session.
     */
    private static BigInteger getFibNum() {
        BigInteger result = fib1.add(fib2);
        fib1 = fib2;
        fib2 = result;
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    /**
     * Will generate next numbers on separate thread
     */
    private class GenerateNextNumbers extends AsyncTask<Void, Void, Void> {
        private String[] numbers;

        @Override
        protected Void doInBackground(Void... params) {
               numbers = new String[3];
            for (short i = 0; i < 3; i++) {
                numbers[i] = getFibNum().toString();
                if (isCancelled()) {
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.addAll(Arrays.asList(numbers));
            adapter.notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }
    }
}
