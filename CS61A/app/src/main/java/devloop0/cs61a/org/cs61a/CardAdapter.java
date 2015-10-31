package devloop0.cs61a.org.cs61a;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;

/**
 * Created by nathr on 10/27/2015.
 */

// http://www.exoguru.com/android/ui/cardview/custom-android-list.html
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    ArrayList<Assignment> assignmentArrayList;

    public CardAdapter(AssignmentListGenerator assignmentListGenerator) {
        super();
        assignmentArrayList = assignmentListGenerator.getAssignmentList();
    }

    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignment_card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Assignment assignment = assignmentArrayList.get(position);
        holder.assignmentName.setText(assignment.getAssignmentName());
        //holder.assignmentDescription.setText(assignment.getDescription());
        holder.assignmentReleaseDate.setText(Html.fromHtml("<b>Release Date:</b> " + assignment.getFormattedReleaseDateString()));
        holder.assignmentDueDate.setText(Html.fromHtml("<b>Due Date:</b> " + assignment.getFormattedDueDateString()));
    }

    @Override
    public int getItemCount() {
        return assignmentArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView assignmentName;
        public TextView assignmentDescription;
        public TextView assignmentReleaseDate;
        public TextView assignmentDueDate;

        public ViewHolder(View viewItem) {
            super(viewItem);
            assignmentName = (TextView) itemView.findViewById(R.id.assignment_name);
            assignmentDescription = (TextView) itemView.findViewById(R.id.assignment_description);
            assignmentReleaseDate = (TextView) itemView.findViewById(R.id.assignment_release_date);
            assignmentDueDate = (TextView) itemView.findViewById(R.id.assignment_due_date);
        }
    }
}
