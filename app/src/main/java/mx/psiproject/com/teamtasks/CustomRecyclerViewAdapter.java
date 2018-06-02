package mx.psiproject.com.teamtasks;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder>
{
    public static class ElementData
    {
        public String text;
        public int id;

        public ElementData(String text, int id)
        {
            this.text = text;
            this.id = id;
        }
    }

    public abstract static class OnClickListener
    {
        public abstract void onClick(ElementData data);
    }

    private ElementData [] dataset;
    private OnClickListener onClickListener;

    public CustomRecyclerViewAdapter(ElementData [] dataset, OnClickListener onClickListener)
    {
        this.dataset = dataset;
        this.onClickListener = onClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ViewHolder(TextView view) {
            super(view);
            textView = view;
        }
    }

    @Override
    public CustomRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_element, parent, false);
        ViewHolder viewHolder = new ViewHolder(textView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(dataset [position].text);
        holder.textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                onClickListener.onClick(dataset [position]);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return dataset.length;
    }
}
