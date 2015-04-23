package ea.photography.activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


import java.util.LinkedHashMap;
import java.util.List;

import ea.photography.PostAdapter;
import ea.photography.R;
import ea.photography.domain.Post;


public class MainActivity extends FragmentActivity {

    private static final String TAG = "PostsActivity";

    private EditText ipAddressEditText;
    private Button viewPostsBtn;
    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;

        ipAddressEditText = (EditText) findViewById(R.id.ipAddressText);
        viewPostsBtn = (Button) findViewById(R.id.viewPostsBtn);

        viewPostsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipAddress = ipAddressEditText.getText().toString();
                Log.i("ipaddress", ipAddress);
                if(checkEmptyStringMessage(ipAddress)) {
                    return;
                }

                //TODO: Implement request here
                PostFetcher fetcher = new PostFetcher();
                fetcher.execute(ipAddress);

            }
        });


    }

    public boolean checkEmptyStringMessage(String ipAddress) {
        if(ipAddress.isEmpty()) {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, "Please enter IP address", duration);
            toast.show();
            return true;
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    private class PostFetcher extends AsyncTask<String, Void, List> {

        private static final String TAG = "PostFetcher";
        public static final String SERVER_URL_HTTP = "http://";
        public static final String SERVER_URL_CONTEXT = ":8080/TheUnicornsPhotography/WSList";

        @Override
        protected List doInBackground(String... params) {
            try {

                StringBuilder url = new StringBuilder(SERVER_URL_HTTP);
                url.append(params[0]).append(SERVER_URL_CONTEXT);
                Log.i("url", url.toString());

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                List posts = restTemplate.getForObject(url.toString(), List.class);
                return posts;

            } catch(Exception ex) {
                Log.e(TAG, "Failed to send HTTP POST request due to: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(List posts) {
            Log.i("onPostExecute", "onPostExecute");
            if (posts != null) {
                // Construct the data source
                List<LinkedHashMap> postList = posts;
                // Create the adapter to convert the array to views
                PostAdapter adapter = new PostAdapter(activity.getApplicationContext(), postList);
                // Attach the adapter to a ListView
                ListView listView = (ListView) findViewById(R.id.postListView);
                listView.setAdapter(adapter);
                //super.onPostExecute(posts);
            }

        }
    }


}
