package ea.photography;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.List;

import ea.photography.domain.Post;

/**
 * Created by caatrin on 04/22/2015.
 */
public class PostAdapter extends ArrayAdapter<LinkedHashMap> {


    public PostAdapter(Context context, List<LinkedHashMap> posts) {
        super(context, 0, posts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        LinkedHashMap post = (LinkedHashMap) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_post, parent, false);
        }
        // Lookup view for data population
        TextView title = (TextView) convertView.findViewById(R.id.postTitleTextView);
        TextView description = (TextView) convertView.findViewById(R.id.postDescriptionTextView);
        // Populate the data into the template view using the data object
        title.setText((String) post.get("title"));
        description.setText((String) post.get("description"));
        // Return the completed view to render on screen
        return convertView;
    }
}
