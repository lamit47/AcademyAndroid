package com.example.academy_project.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.academy_project.R;
import com.example.academy_project.adapter.QuestionAdapter;
import com.example.academy_project.apis.ApiService;
import com.example.academy_project.apis.RetrofitClient;
import com.example.academy_project.entities.Question;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionsFragment extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_questions, container, false);
        getListQuestion();
        return view;

    }

    public void getListQuestion() {
        RetrofitClient.getInstance().create(ApiService.class).getListQuestions().enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if(response.isSuccessful()){
                    List<Question> listQuestion = response.body();
                    QuestionAdapter questionAdapter = new QuestionAdapter(new ArrayList<Question>(listQuestion));

                    ListView listView = view.findViewById(R.id.lvQuestion);
                    listView.setAdapter(questionAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Question q = (Question) questionAdapter.getItem(position);
                                    //Làm gì đó khi chọn sản phẩm (ví dụ tạo một Activity hiện thị chi tiết, biên tập ..)

                                    try {
                                        QuestionFragment.setId(q.getId());
                                        Class fragmentClass = QuestionFragment.class;
                                        Fragment fragment = (Fragment) fragmentClass.newInstance();
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {

            }
        });
//        RetrofitClient.getInstance()
//                .create(ApiService.class)
//                .getListQuestion()
//                .enqueue(new Callback<List<Question>>() {
//                    @Override
//                    public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
//                        if (response.isSuccessful()) {
//                            List<Question> lsitQuestion = response.body();
//                            CoursesAdapter coursesAdapter = new CoursesAdapter(new ArrayList<Course>(listcourse));
//
//                            ListView lvCourses = view.findViewById(R.id.lvCourses);
//                            lvCourses.setAdapter(coursesAdapter);
//
//                            lvCourses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                    Course c = (Course) coursesAdapter.getItem(position);
//                                    //Làm gì đó khi chọn sản phẩm (ví dụ tạo một Activity hiện thị chi tiết, biên tập ..)
//
//                                    try {
//                                        TrackStepsFragment.setCourseId(c.getId());
//                                        Class fragmentClass = TrackStepsFragment.class;
//                                        Fragment fragment = (Fragment) fragmentClass.newInstance();
//                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//
//                                }
//                            });
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<Course>> call, Throwable t) {
//                        System.out.println(t.toString());
//                    }
//                });
    }
}
