package fr.maxime_agusti.weightkeeper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import fr.maxime_agusti.weightkeeper.R;
import fr.maxime_agusti.weightkeeper.data.RecordData;
import fr.maxime_agusti.weightkeeper.database.MySQLHelper;
import fr.maxime_agusti.weightkeeper.dialog.AddRecordDialogFragment;
import fr.maxime_agusti.weightkeeper.entity.Record;
import fr.maxime_agusti.weightkeeper.fragment.RecordDetailFragment;
import fr.maxime_agusti.weightkeeper.dummy.DummyContent;

import java.util.Collections;
import java.util.List;

public class RecordListActivity extends AppCompatActivity implements AddRecordDialogFragment.AddRecordDialogListener {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddRecordDialogFragment dialog = new AddRecordDialogFragment();
                dialog.show(getSupportFragmentManager(), "show");
            }
        });

        View recyclerView = findViewById(R.id.record_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.record_detail_container) != null) {
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter((new RecordData(new MySQLHelper(this))).getAllRecord()));
    }

    @Override
    public void onAddRecord(Record record) {

        (new RecordData(new MySQLHelper(this))).createRecord(record);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.record_list);
        assert recyclerView != null;
        SimpleItemRecyclerViewAdapter adapter = (SimpleItemRecyclerViewAdapter) recyclerView.getAdapter();
        adapter.add(record);
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Record> mValues;

        public SimpleItemRecyclerViewAdapter(List<Record> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.record_list_content, parent, false);
            return new ViewHolder(view);
        }

        public void add(Record ...records) {
            Collections.addAll(mValues, records);
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mWeightView.setText(String.valueOf(mValues.get(position).getWeight()) + " kg");
            holder.mInstantView.setText(mValues.get(position).getInstant());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putLong(RecordDetailFragment.ARG_ITEM_ID, holder.mItem.getId());
                        RecordDetailFragment fragment = new RecordDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.record_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, RecordDetailActivity.class);
                        intent.putExtra(RecordDetailFragment.ARG_ITEM_ID, holder.mItem.getId());

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mWeightView;
            public final TextView mInstantView;
            public Record mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mWeightView = (TextView) view.findViewById(R.id.weight);
                mInstantView = (TextView) view.findViewById(R.id.instant);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mWeightView.getText() + "'";
            }
        }
    }
}
