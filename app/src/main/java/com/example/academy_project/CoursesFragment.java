package com.example.academy_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.academy_project.Adapter.CourseListAdapter;
import com.example.academy_project.apis.ApiService;
import com.example.academy_project.apis.RetrofitClient;
import com.example.academy_project.entities.Course;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoursesFragment extends Fragment {
    ArrayList<Course> listCourse;
    CourseListAdapter courseListAdapter;
    ListView listViewCourse;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_courses, container, false);
        getListCourse();
        return view;

    }
    public void getListCourse(){
        RetrofitClient.getInstance().create(ApiService.class).getListCourse().enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful()){
                    List<Course> listcourse = response.body();
                    System.out.println(listcourse.size());
                    courseListAdapter = new CourseListAdapter(new ArrayList<Course>(listcourse));

                    listViewCourse = view.findViewById(R.id.listCourse);
                    listViewCourse.setAdapter(courseListAdapter);

                    listViewCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Course c = (Course) courseListAdapter.getItem(position);
                            //Làm gì đó khi chọn sản phẩm (ví dụ tạo một Activity hiện thị chi tiết, biên tập ..)


                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                System.out.println("abc");
                System.out.println(t.toString());
            }
        });
    }
}
