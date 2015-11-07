package devloop0.cs61a.org.cs61a;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by nathr on 10/27/2015.
 */

// http://www.exoguru.com/android/ui/cardview/custom-android-list.html
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    ArrayList<Assignment> assignmentArrayList;
    long currentTimeInMilliseconds = -1;
    final long twoDayLimit = 24 * 3600 * 2 * 1000; // 2 days in milliseconds

    public final static int releasedAssignmentBackgroundColor = Color.parseColor("#E6F3FF");
    public final static int urgentAssignmentBackgroundColor = Color.parseColor("#FFE6FF");
    public final static int completedAssignmentBackgroundColor = Color.parseColor("#E7FFE6");
    public final static int unreleasedAssignmentBackgroundColor = Color.parseColor("#EDEFF0");

    public CardAdapter(AssignmentListGenerator assignmentListGenerator) {
        super();
        assignmentArrayList = assignmentListGenerator.getAssignmentList();
        currentTimeInMilliseconds = Calendar.getInstance().getTimeInMillis();
    }

    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignment_card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Assignment assignment = assignmentArrayList.get(position);
        int colorToSet = unreleasedAssignmentBackgroundColor;
        holder.assignmentName.setText(assignment.getAssignmentName());
        holder.assignmentDescription.setText(assignment.getDescription());
        holder.assignmentReleaseDate.setText(Html.fromHtml("<b>Release Date:</b> " + assignment.getFormattedReleaseDateString()));
        holder.assignmentDueDate.setText(Html.fromHtml("<b>Due Date:</b> " + assignment.getFormattedDueDateString()));
        if(assignment.assignmentIsOpen()) {
            if(currentTimeInMilliseconds >= assignment.getReleaseTime() && currentTimeInMilliseconds < assignment.getDueTime()) {
                if(currentTimeInMilliseconds + twoDayLimit >= assignment.getDueTime())
                    colorToSet = urgentAssignmentBackgroundColor;
                else
                    colorToSet = releasedAssignmentBackgroundColor;
            }
            else
                colorToSet = completedAssignmentBackgroundColor;
        }
        else {
            colorToSet = unreleasedAssignmentBackgroundColor;
        }
        holder.assignmentLinearLayout.setBackgroundColor(colorToSet);
        holder.assignmentLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AssignmentActivity.class);
                intent.putExtra("assignment_description", assignment.getDescription());
                intent.putExtra("assignment_name", assignment.getAssignmentName());
                intent.putExtra("assignment_release_time", assignment.getReleaseTime());
                intent.putExtra("assignment_due_time", assignment.getDueTime());
                intent.putExtra("assignment_link", assignment.getAssignmentLink());
                intent.putExtra("assignment_is_open", assignment.assignmentIsOpen());
                v.getContext().startActivity(intent);
            }
        });
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
        public LinearLayout assignmentLinearLayout;

        public ViewHolder(View viewItem) {
            super(viewItem);
            assignmentName = (TextView) itemView.findViewById(R.id.assignment_name);
            assignmentDescription = (TextView) itemView.findViewById(R.id.assignment_description);
            assignmentReleaseDate = (TextView) itemView.findViewById(R.id.assignment_release_date);
            assignmentDueDate = (TextView) itemView.findViewById(R.id.assignment_due_date);
            assignmentLinearLayout = (LinearLayout) itemView.findViewById(R.id.main_assignment_linear_layout);
        }
    }
}
