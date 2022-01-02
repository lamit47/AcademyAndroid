package com.example.academy_project.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.academy_project.R;
import com.example.academy_project.adapter.CoursesAdapter;
import com.example.academy_project.apis.ApiService;
import com.example.academy_project.apis.RetrofitClient;
import com.example.academy_project.database.CourseDB;
import com.example.academy_project.entities.Course;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoursesFragment extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_courses, container, false);

        if (isNetworkAvailable()) {
            getListCourseFromAPI();
        } else {
            getListCourseFromDB();
        }

        final SwipeRefreshLayout srl = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListCourseFromAPI();
                if (srl.isRefreshing()) {
                    srl.setRefreshing(false);
                }
            }
        });
        return view;

    }

    private void getListCourseFromDB() {
        List<Course> list = CourseDB.getInstance(getActivity()).courseDAO().getAllCourse();
        setDataToAdapter(list);
    }

    private void nukeCourses() {
        CourseDB.getInstance(getActivity()).courseDAO().nukeCourses();
    }

    private void addCourse(Course course) {
        if (course.getId() == 0 || TextUtils.isEmpty(course.getTitle()) || TextUtils.isEmpty(course.getPicturePath())) {
            return;
        }
        CourseDB.getInstance(getActivity()).courseDAO().insertCourse(course);
    }

    private void getListCourseFromAPI() {
        RetrofitClient.getInstance()
                .create(ApiService.class)
                .getListCourse()
                .enqueue(new Callback<List<Course>>() {
                    @Override
                    public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                        if (response.isSuccessful()) {
                            List<Course> listcourse = response.body();
                            setDataToAdapter(listcourse);

                            nukeCourses();
                            for (Course c : listcourse) {
                                addCourse(c);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<List<Course>> call, Throwable t) {
                        Toast.makeText(getActivity(), "Kết nối đến máy chủ thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setDataToAdapter(List<Course> list) {
        if (list.size() < 1) {
            Toast.makeText(getActivity(), "Danh sách bài học trống!", Toast.LENGTH_LONG).show();
            return;
        }
        CoursesAdapter coursesAdapter = new CoursesAdapter(new ArrayList<Course>(list));

        ListView lvCourses = view.findViewById(R.id.lvCourses);
        lvCourses.setAdapter(coursesAdapter);

        lvCourses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Bundle bundle = new Bundle();
                    bundle.putInt("courseId", (int) id);
                    TrackStepsFragment trackStepsFragment = new TrackStepsFragment();
                    trackStepsFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flContent, trackStepsFragment).addToBackStack(null).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
