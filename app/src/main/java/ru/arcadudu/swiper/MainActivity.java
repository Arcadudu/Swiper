package ru.arcadudu.swiper;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<Model> recyclerModelList = new ArrayList<>();
    private List<Model> masterModelList = new ArrayList<>();
//    private List<Model> archivedModels = new ArrayList<>();


    private Model chosenModel = null;


    private TextView tv_emptyListSign;

    // bottom sheet
    private ConstraintLayout bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;

    private ImageView bs_image;
    private TextView bs_title, bs_description;

    // floating buttons
    private FloatingActionButton fabMain, fabRefresh, fabDeleteAll;
    private boolean fabMenuOpened = false;
    private Float translationY = 100f;
    private final long fabDuration = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFabs();
        packMasterModelList();
        packModelList();

        tv_emptyListSign = findViewById(R.id.tv_listIsEmpty);
        checkIfListIsEmpty();

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(recyclerModelList, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);

        // bottom_sheet
        bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bs_image = findViewById(R.id.bs_imageView);
        bs_title = findViewById(R.id.bs_title);
        bs_description = findViewById(R.id.bs_description);


    }




    private void packMasterModelList() {
        String content = " " + getString(R.string.progLang);
        masterModelList.add(new Model(getString(R.string.java), getString(R.string.java) + content, R.drawable.icon_java, getString(R.string.java_description)));
        masterModelList.add(new Model(getString(R.string.javascript), getString(R.string.javascript) + content, R.drawable.icon_javascript, getString(R.string.java_script_description)));
        masterModelList.add(new Model(getString(R.string.haskell), getString(R.string.haskell) + content, R.drawable.icon_haskel, getString(R.string.haskell_description)));
        masterModelList.add(new Model(getString(R.string.python), getString(R.string.python) + content, R.drawable.icon_python, getString(R.string.python_description)));
        masterModelList.add(new Model(getString(R.string.csharp), getString(R.string.csharp) + content, R.drawable.icon_c_sharp, getString(R.string.csharp_description)));
        masterModelList.add(new Model(getString(R.string.ruby), getString(R.string.ruby) + content, R.drawable.icon_ruby, getString(R.string.ruby_description)));
    }

    private void checkIfListIsEmpty() {
        if (recyclerModelList.isEmpty()) {
            tv_emptyListSign.setVisibility(View.VISIBLE);
        } else {
            tv_emptyListSign.setVisibility(View.GONE);
        }
    }

    private void setFabs() {
        fabMain = findViewById(R.id.fab_main);
        fabRefresh = findViewById(R.id.fab_refresh);
        fabDeleteAll = findViewById(R.id.fab_delete_all);

        fabRefresh.setAlpha(0f);
        fabDeleteAll.setAlpha(0f);

        fabRefresh.setTranslationY(translationY);
        fabDeleteAll.setTranslationY(translationY);

        fabMain.setOnClickListener(this);
        fabRefresh.setOnClickListener(this);
        fabDeleteAll.setOnClickListener(this);

        fabRefresh.setEnabled(false);
        fabDeleteAll.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_main:
                checkIfFabMenuOpened();
                break;
            case R.id.fab_refresh:
                closeFabMenu();
                packModelList();
                adapter.notifyDataSetChanged();
                break;
            case R.id.fab_delete_all:
                closeFabMenu();
                recyclerModelList.clear();
                adapter.notifyDataSetChanged();
                break;
        }
        checkIfListIsEmpty();
    }

    private void checkIfFabMenuOpened() {
        if (fabMenuOpened) {
            closeFabMenu();

        } else {
            openFabMenu();

        }
    }

    public void openFabMenu() {
        fabMenuOpened = !fabMenuOpened;
        fabMain.animate().rotation(180f).setDuration(fabDuration).start();

        fabRefresh.animate().alpha(1f).translationY(0f).setDuration(fabDuration).start();
        fabDeleteAll.animate().alpha(1f).translationY(0f).setDuration(fabDuration).start();

        fabRefresh.setEnabled(true);
        fabDeleteAll.setEnabled(true);
    }

    public void closeFabMenu() {
        fabMenuOpened = !fabMenuOpened;
        fabMain.animate().rotation(0f).setDuration(fabDuration).start();

        fabRefresh.animate().alpha(0f).translationY(translationY).setDuration(fabDuration).start();
        fabDeleteAll.animate().alpha(0f).translationY(translationY).setDuration(fabDuration).start();

        fabRefresh.setEnabled(false);
        fabDeleteAll.setEnabled(false);
    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            switch (direction) {
                // left - delete:
                case ItemTouchHelper.LEFT:
                    chosenModel = recyclerModelList.get(position);
                    recyclerModelList.remove(chosenModel);
                    adapter.notifyDataSetChanged();
                    Snackbar.make(recyclerView, chosenModel.getTitle() + " удалено", Snackbar.LENGTH_LONG)
                            .setAction("отмена", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    recyclerModelList.add(position, chosenModel);
                                    adapter.notifyDataSetChanged();
                                }
                            }).show();
                    break;
                // right - archive:
//                case ItemTouchHelper.RIGHT:
//                    chosenModel = recyclerModelList.get(position);
//                    archivedModels.add(chosenModel);
//                    adapter.notifyDataSetChanged();
//                    Snackbar.make(recyclerView, chosenModel.getTitle() + " архивировано", Snackbar.LENGTH_LONG)
//                            .setAction("отмена", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    archivedModels.remove(chosenModel);
//                                }
//                            }).show();
//                    break;
            }
        }

        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    // left - delete - red - delete icon
                    .addBackgroundColor(R.color.red_delete)
//                    .addSwipeLeftBackgroundColor(R.color.red_delete)
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    // right - archive - blue - archive icon
//                    .addSwipeRightBackgroundColor(R.color.blue_archive)
                    //.addSwipeRightActionIcon(R.drawable.ic_baseline_archive_24)
                    .create()
                    .decorate();

        }
    };

    private void packModelList() {
        if (recyclerModelList.isEmpty()) {
            recyclerModelList.addAll(masterModelList);
        } else if (recyclerModelList.size() == masterModelList.size()) {
            Toast.makeText(this, "Список уже полон", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < masterModelList.size(); i++) {
                if (!recyclerModelList.contains(masterModelList.get(i)))
                    recyclerModelList.add(i, masterModelList.get(i));
            }

        }

    }


}