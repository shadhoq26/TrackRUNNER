package aston.cs3mmd.trackrunner;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//This adapter is used for the routes recycler view.
public class AnalysisRecyclerAdapter extends RecyclerView.Adapter<AnalysisRecyclerAdapter.ViewHolder> {

    Context context;
    ArrayList<Run> runs;
    private final AnalysisRecyclerViewInterface analysisRecyclerViewInterface;
    //this is the constructer for the class, it will ser the run data.
    public AnalysisRecyclerAdapter(Context context, ArrayList<Run> runs, AnalysisRecyclerViewInterface analysisRecyclerViewInterface) {
        this.context = context;
        this.runs = runs;
        this.analysisRecyclerViewInterface = analysisRecyclerViewInterface;
    }
    //This function is for setting the layout of data.
    @NonNull
    @Override
    public AnalysisRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.analysis_run_item, parent, false);
        return new AnalysisRecyclerAdapter.ViewHolder(view, analysisRecyclerViewInterface);
    }
    //this function is for binding all the text for each item in the list.
    @Override
    public void onBindViewHolder(@NonNull AnalysisRecyclerAdapter.ViewHolder holder, int position) {
        holder.time.setText(runs.get(position).getTime());
        holder.miles.setText(runs.get(position).getMiles());
        holder.KCAL.setText(runs.get(position).getKCAL());
        holder.speed.setText(runs.get(position).getSpeed());
        holder.route.setText("Route " + String.valueOf(position+1));
    }
    //this will return the size of the list.
    @Override
    public int getItemCount() {
        return runs.size();
    }
    //this is a class for each item in the list.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView miles;
        TextView KCAL;
        TextView speed;
        TextView route;
        Button deleteButton;
        //This is a constructor for the class that sets the item data.
        public ViewHolder(@NonNull View itemView, AnalysisRecyclerViewInterface analysisRecyclerViewInterface) {
            super(itemView);
            time = itemView.findViewById(R.id.runItemTime);
            miles = itemView.findViewById(R.id.runItemDistance);
            KCAL = itemView.findViewById(R.id.runItemKCAL);
            speed = itemView.findViewById(R.id.runItemSpeed);
            route = itemView.findViewById(R.id.runItemRoute);
            deleteButton = itemView.findViewById(R.id.runItemDeleteButton);

            //this is for when you click on an item.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (analysisRecyclerViewInterface != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            analysisRecyclerViewInterface.onClick(position);
                        }
                    }
                }
            });

            //this is for when you click the delete button for an item.
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (analysisRecyclerViewInterface != null) {
                        int position = getAdapterPosition();
                        analysisRecyclerViewInterface.onDelete(position);
                    }
                }
            });
        }
    }
}
