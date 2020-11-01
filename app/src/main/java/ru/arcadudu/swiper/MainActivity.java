package ru.arcadudu.swiper;

import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ILongClickCallBack , ClickCallback, Linkable{


    // recycler
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<Model> recyclerModelList = new ArrayList<>();
    private List<Model> masterModelList = new ArrayList<>();

    // bottom sheet
    private ConstraintLayout bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private ImageView bs_image;
    private TextView bs_title, bs_description;
    private View transparentBackground;

    // floating buttons
    private FloatingActionButton fabMain, fabRefresh, fabDeleteAll;
    private boolean fabMenuOpened = false;
    private final Float translationY = 100f;
    private final long fabDuration = 200;
    private ImageView fabIcon;

    // misc
    private Model chosenModel = null;
    private TextView tv_emptyListSign;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTransparentStatusBar();

        setFabs();

        packMasterModelList();
        packModelList();

        tv_emptyListSign = findViewById(R.id.tv_listIsEmpty);
        checkIfListIsEmpty(); // string will appear if list is empty

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(recyclerModelList, this, this, this, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
//        adapter.setLongClickCallBack(this);

        // bottom_sheet
        bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bs_image = findViewById(R.id.bs_imageView);
        bs_title = findViewById(R.id.bs_title);
        bs_description = findViewById(R.id.bs_description);
        bottomSheet.setOnLongClickListener(view -> false);
        transparentBackground = findViewById(R.id.tr);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    fabMain.show();
                } else {
                    fabMain.hide();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.e("AA", "offset = " + slideOffset);
                if (slideOffset <= 0) {
                    transparentBackground.setAlpha(slideOffset + 1);
                }
            }
        });

    }

    // touchHelper
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            // if item is regular
            if (viewHolder.getClass() == MyAdapter.ViewHolderRegular.class) {
                int position = viewHolder.getAdapterPosition();
                // left - delete:
                if (direction == ItemTouchHelper.LEFT) {
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
                }
            }

        }

        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            // if item is regular
            if (viewHolder.getClass() == MyAdapter.ViewHolderRegular.class) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        // left - delete - red - delete icon
                        .addBackgroundColor(R.color.white)
                        .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                        .create()
                        .decorate();
            }
        }
    };

    // interface
    @Override
    public void longClick(Model model) {
        Log.d("model", "longClick: mainActivity model is null? " + (model == null));
        bs_image.setImageResource(model.getImage());
        bs_title.setText(model.getTitle());
        bs_description.setText(model.getDescription());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void click(Model model) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("selected_model", model);
//        intent.putExtra("title", model.getTitle());
//        intent.putExtra("description", model.getDescription());
//        intent.putExtra("image", model.getImage());
        startActivity(intent);
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

    private void setTransparentStatusBar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setNavigationBarColor(getResources().getColor(android.R.color.transparent, getTheme()));
        window.setStatusBarColor(getResources().getColor(android.R.color.transparent, getTheme()));

    }

    private void setFabs() {
        fabMain = findViewById(R.id.fab_main);
        fabRefresh = findViewById(R.id.fab_refresh);
        fabDeleteAll = findViewById(R.id.fab_delete_all);
        fabIcon = findViewById(R.id.fab_main_icon);

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

    private void packMasterModelList() {
        String content = " " + getString(R.string.progLang);
        String plainItem = " (this is an item too)";
        //plain model
        masterModelList.add(new Model(getString(R.string.app_name) + plainItem));

        masterModelList.add(new Model(getString(R.string.java), getString(R.string.java) + content, R.drawable.icon_java, getString(R.string.java_description)));
        masterModelList.add(new Model(getString(R.string.javascript), getString(R.string.javascript) + content, R.drawable.icon_javascript, getString(R.string.java_script_description)));
        masterModelList.add(new Model(getString(R.string.haskell), getString(R.string.haskell) + content, R.drawable.icon_haskel, getString(R.string.haskell_description)));
        //ads model
        masterModelList.add(new Model("https://brandbook.dodopizza.info/BillboardMockup.a2ad115b.jpg", true, "https://dodopizza.ru/novorossiysk"));

        masterModelList.add(new Model(getString(R.string.python), getString(R.string.python) + content, R.drawable.icon_python, getString(R.string.python_description)));
        masterModelList.add(new Model(getString(R.string.csharp), getString(R.string.csharp) + content, R.drawable.icon_c_sharp, getString(R.string.csharp_description)));
        masterModelList.add(new Model(getString(R.string.ruby), getString(R.string.ruby) + content, R.drawable.icon_ruby, getString(R.string.ruby_description)));
        masterModelList.add(new Model("Размер списка: " + countDownItems(masterModelList)+ plainItem));

    }

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

//        masterModelList.add(new Model("Размер списка: " + masterModelList.size() + plainItem));



    }

    private int countDownItems(List<Model> list){
        int count = 0;
        for (Model model : list) {
            if(model.getContent()!=null){
                count+=1;
            }
        }
        return count;
    }

    private void checkIfListIsEmpty() {
        if (recyclerModelList.isEmpty()) {
            tv_emptyListSign.setVisibility(View.VISIBLE);
        } else {
            tv_emptyListSign.setVisibility(View.GONE);
        }
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
        fabIcon.animate().rotation(180f).setDuration(fabDuration).start();

        fabRefresh.animate().alpha(1f).translationY(0f).setDuration(fabDuration).start();
        fabDeleteAll.animate().alpha(1f).translationY(0f).setDuration(fabDuration).start();

        fabRefresh.setEnabled(true);
        fabDeleteAll.setEnabled(true);
    }

    public void closeFabMenu() {
        fabMenuOpened = !fabMenuOpened;
        fabIcon.animate().rotation(0f).setDuration(fabDuration).start();

        fabRefresh.animate().alpha(0f).translationY(translationY).setDuration(fabDuration).start();
        fabDeleteAll.animate().alpha(0f).translationY(translationY).setDuration(fabDuration).start();

        fabRefresh.setEnabled(false);
        fabDeleteAll.setEnabled(false);
    }


    @Override
    public void goToSource(Model model) {
        String linkSource = model.getSourceLink();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkSource));
        startActivity(browserIntent);
    }
}