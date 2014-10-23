package com.example.epital.tablettestapplication.dashboard.DailyMeasurement.Questions;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.epital.tablettestapplication.R;
import com.example.epital.tablettestapplication.dashboard.DailyMeasurement.DailyMeasurementFragmentCommunication;

import java.lang.reflect.Field;

/**
 * Created by oscarandersen on 20/10/14.
 */
public class DailyMeasurementQuestionFragment extends Fragment implements View.OnClickListener {

    int question_id, question_number;

    //String headline, question, answer_yes, answer_no;
    String[] questionContainer;
    TextView headline, question;
    Button buttonYes, buttonNo;
    DailyMeasurementFragmentCommunication comm;

    public DailyMeasurementQuestionFragment(int question_id, int question_number) {
        this.question_id = question_id;
        this.question_number = question_number;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.daily_measurement_question, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    public void init() {
        comm = (DailyMeasurementFragmentCommunication) getActivity();
        headline = (TextView) getActivity().findViewById(R.id.question_headline);
        question = (TextView) getActivity().findViewById(R.id.question_question);
        buttonYes = (Button) getActivity().findViewById(R.id.question_yes);
        buttonYes.setOnClickListener(this);
        buttonNo = (Button) getActivity().findViewById(R.id.question_no);
        buttonNo.setOnClickListener(this);
        if (question_id == 1) {
            questionContainer = getResources().getStringArray(R.array.question_1);
        } else if (question_id == 2) {
            questionContainer = getResources().getStringArray(R.array.question_2);
        } else if (question_id == 3) {
            questionContainer = getResources().getStringArray(R.array.question_3);
        }
        headline.setText(questionContainer[0]);
        question.setText(questionContainer[1]);
        buttonYes.setText(questionContainer[2]);
        buttonNo.setText(questionContainer[3]);


        //TODO: Lav en dynamisk / generisk hentning af string id'et. Koden nedenfor er begyndelse, men det virker ikke.
        /*
        String resource_name = "question_"+Integer.toString(question_id);
        //get data array
        System.out.println(getActivity().getApplicationContext().getPackageName());
        //int resource_id = Resources.getSystem().getIdentifier("question_1", "", getActivity().getApplicationContext().getPackageName(););
        int hej = getActivity().getResources().getIdentifier("question_1", "string", "com.example.epital.tablettestapplication");
        System.out.println("Virker det? " + getString(hej));
        //questionContainer = Resources.getSystem().getStringArray(resource_id);*/
    }

    private void setQuestion(boolean answer){
        if (question_id == 1) {
            comm.setQuestion1(answer);
        } else if (question_id == 2) {
            comm.setQuestion2(answer);
        } else if (question_id == 3) {
            comm.setQuestion3(answer);
        }
    }

    @Override
    public void onClick(View button) {
        int next = question_number + 1;
        if (button == buttonYes) {
            setQuestion(true);
            comm.changeDailyMeasurementContent(next);
        } else if (button == buttonNo) {
            setQuestion(false);
            comm.changeDailyMeasurementContent(next);
        }
    }
}
