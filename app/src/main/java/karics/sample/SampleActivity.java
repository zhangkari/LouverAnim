package karics.sample;

import karics.anim.louveranim.LouverAnimator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class SampleActivity extends Activity {

    private ToggleButton mToggle;
    private View mBtn;
    private View mContainer;

    final static int COUNT = 200;
    private List<String> mDataSource;

    private LouverAnimator mAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        prepareVariables();
        findViews();
        setListeners();
    }

    private void prepareVariables() {
        mDataSource = new ArrayList<>();
        for (int i = 0; i < COUNT; ++i) {
            int value = (int) (Math.random() * 1000) % 900 + 100;
            mDataSource.add(value + "");
        }

        mAnimator = new LouverAnimator();
    }

    private void findViews() {
        mContainer = findViewById(R.id.container);
        mBtn = findViewById(R.id.btn);
        mToggle = (ToggleButton) findViewById(R.id.toggle);
        GridView gridView = (GridView) findViewById(R.id.gridview);
        GridAdapter adapter = new GridAdapter();
        gridView.setAdapter(adapter);
        findViewById(R.id.singleLineText).setSelected(true);
    }

    private void setListeners() {
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContainer.getVisibility() == View.VISIBLE) {
                    mAnimator.setTarget(mContainer);
                    mAnimator.dismiss();
                } else {
                    mAnimator.setTarget(mContainer);
                    mAnimator.show();
                }
            }
        });

        mToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    boolean status = mAnimator.setDirection(LouverAnimator.Direction.HORIZONTAL);
                    if (!status) {
                        Toast.makeText(SampleActivity.this, "Failed set direction to horizontal", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    boolean status = mAnimator.setDirection(LouverAnimator.Direction.VERTICAL);
                    if (!status) {
                        Toast.makeText(SampleActivity.this, "Failed set direction to vertical", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDataSource.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataSource.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView text = null;
            if (convertView == null) {
                text = new TextView(SampleActivity.this);
                convertView = text;
            } else {
                text = (TextView) convertView;
            }
            text.setText(mDataSource.get(position));

            return convertView;
        }
    }
}
