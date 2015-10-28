package devloop0.cs61a.org.cs61a;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    }

    @Override
    public int getItemCount() {
        return assignmentArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView assignmentName;

        public ViewHolder(View viewItem) {
            super(viewItem);
            assignmentName = (TextView) itemView.findViewById(R.id.assignment_name);
        }
    }
}
