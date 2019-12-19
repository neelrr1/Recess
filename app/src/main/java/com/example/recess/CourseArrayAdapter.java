package com.example.recess;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CourseArrayAdapter extends ArrayAdapter<Course> {
    private final Context context;
    private final Course[] values;
    private int MAX_GRADE_WIDTH;
    private int MAX_SCORE_WIDTH;

    /**
     *
     * @param context - Graphical display framework.
     * @param values - Values for display.
     */
    public CourseArrayAdapter(Context context, Course[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;

        int grade_width = 0;
        int score_width = 0;
        for(Course course : values) {
            grade_width = Math.max(course.grade.length(), grade_width);
            score_width = Math.max(course.score.length(), score_width);
        }
        MAX_GRADE_WIDTH = grade_width;
        MAX_SCORE_WIDTH = score_width;
    }

    /**
     * Returns the array view.
     * @param position - Position on the screen.
     * @param convertView - View to shift to.
     * @param parent - Parent view, main screen.
     * @return Returns the current array view.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View courseView = inflater.inflate(R.layout.course, parent, false);
        TextView name = courseView.findViewById(R.id.name);
        TextView teacher = courseView.findViewById(R.id.teacher);
        TextView grade = courseView.findViewById(R.id.grade);
        TextView score = courseView.findViewById(R.id.score);

        Course curr = values[position];
        curr.grade = String.format("%-"+MAX_GRADE_WIDTH+"s", curr.grade);
        curr.score = String.format("%-"+MAX_SCORE_WIDTH+"s", curr.score);

        name.setText(curr.name);
        teacher.setText(curr.teacher);
        grade.setText(curr.grade);
        score.setText(curr.score);

        return courseView;
    }
}
