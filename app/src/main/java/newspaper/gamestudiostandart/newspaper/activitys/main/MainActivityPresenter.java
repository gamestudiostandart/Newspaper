package newspaper.gamestudiostandart.newspaper.activitys.main;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;

import newspaper.gamestudiostandart.newspaper.model.Category;
import newspaper.gamestudiostandart.newspaper.model.FragmentNewsModel;
import newspaper.gamestudiostandart.newspaper.repository.getfromstorege.DBHelperNews;
import newspaper.gamestudiostandart.newspaper.repository.getfromstorege.DBHelperResoursesInteractor;

@InjectViewState
public class MainActivityPresenter extends MvpPresenter<MainActivityView> implements
        DBHelperResoursesInteractor.SetReadableListener {

    private DBHelperResoursesInteractor dbHelperResoursesInteractor;
    private Category category;

    public Category getCategory() {
        return category;
    }

    private ArrayList<FragmentNewsModel> listCheckedModels;

    public ArrayList<FragmentNewsModel> getChackList() {
        return listCheckedModels;
    }

    MainActivityPresenter() {
        dbHelperResoursesInteractor = DBHelperNews.getInstance();
        category = Category.GENERAL;
    }

    public void getListRresourses() {
        dbHelperResoursesInteractor.getTableResourses(this, category);
    }

    public void getListRresourses(Category category) {
        this.category = category;
        dbHelperResoursesInteractor.getTableResourses(this, category);
    }

    @Override
    public void getListFromResourseListner(ArrayList<FragmentNewsModel> list) {
        if (listCheckedModels == null) {
            listCheckedModels = new ArrayList<>();
        }
        listCheckedModels.clear();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isCheck()) {
                listCheckedModels.add(list.get(i));
            }
        }
        getViewState().setList(listCheckedModels);
    }
}