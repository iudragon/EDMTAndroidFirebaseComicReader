package dragon.bakuman.iu.edmtandroidfirebasecomicreader;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Filter;

import dragon.bakuman.iu.edmtandroidfirebasecomicreader.Adapter.MyComicAdapter;
import dragon.bakuman.iu.edmtandroidfirebasecomicreader.Common.Common;
import dragon.bakuman.iu.edmtandroidfirebasecomicreader.Model.Comic;

public class FilterSearchActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    RecyclerView recycler_filter_search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);

        recycler_filter_search = findViewById(R.id.recycler_filter_search);
        recycler_filter_search.setHasFixedSize(true);
        recycler_filter_search.setLayoutManager(new GridLayoutManager(this, 2));

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.inflateMenu(R.menu.main_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.action_filter:

                        showFiltersDialog();

                        break;
                    case R.id.action_search:

                        showSearchDialog();

                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void showSearchDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FilterSearchActivity.this);
        alertDialog.setTitle("Search");

        final LayoutInflater inflater = this.getLayoutInflater();
        View search_layout = inflater.inflate(R.layout.dialog_search, null);

        final EditText edit_search = search_layout.findViewById(R.id.edit_search);

        alertDialog.setView(search_layout);

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                dialogInterface.dismiss();

            }
        });

        alertDialog.setPositiveButton("SEARCH", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                fetchSearchComic(edit_search.getText().toString());

            }
        });

        alertDialog.show();

    }

    private void fetchSearchComic(String query) {

        List<Comic> comic_search = new ArrayList<>();
        for (Comic comic : Common.comicList) {

            if (comic.Name.contains(query)) {

                comic_search.add(comic);
            }
        }
        if (comic_search.size() > 0) {

            recycler_filter_search.setAdapter(new MyComicAdapter(getBaseContext(), comic_search));
        } else {
            Toast.makeText(this, "NO RESULT HAHA.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showFiltersDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FilterSearchActivity.this);
        alertDialog.setTitle("Select Category");

        final LayoutInflater inflater = this.getLayoutInflater();
        View filter_layout = inflater.inflate(R.layout.dialog_options, null);

        final AutoCompleteTextView txt_category = filter_layout.findViewById(R.id.txt_category);
        final ChipGroup chipGroup = filter_layout.findViewById(R.id.chipGroup);

        //Create autoComplete
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, Common.categories);

        txt_category.setAdapter(adapter);
        txt_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                txt_category.setText("");

                //Create Tags
                final Chip chip = (Chip) inflater.inflate(R.layout.chip_item, null, false);
                chip.setText(((TextView) view).getText());
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chipGroup.removeView(view);
                    }
                });

                chipGroup.addView(chip);
            }
        });

        alertDialog.setView(filter_layout);
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                dialogInterface.dismiss();

            }
        });

        alertDialog.setPositiveButton("FILTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<String> filter_key = new ArrayList<>();
                StringBuilder filter_query = new StringBuilder("");
                for (int j = 0; j < chipGroup.getChildCount(); j++) {

                    Chip chip = (Chip) chipGroup.getChildAt(j);
                    filter_key.add(chip.getText().toString());
                }

                // Because in our activity; Category will be sorted from A -> E and split by ,
                // So we need sort our filter key
                Collections.sort(filter_key);
                //Convert list to String
                for (String key : filter_key) {

                    filter_query.append(key).append("'");

                }

                // Remove last ","
                filter_query.setLength(filter_query.length() - 1);

                //Filter by this query
                fetchFilterCategory(filter_query.toString());
            }
        });

        alertDialog.show();
    }

    private void fetchFilterCategory(String query) {

        List<Comic> comic_filtered = new ArrayList<>();
        for (Comic comic : Common.comicList) {

            if (comic.Category != null) {

                if (comic.Category.contains(query)) {
                    comic_filtered.add(comic);
                }
            }


        }

        if (comic_filtered.size() > 0) {

            recycler_filter_search.setAdapter(new MyComicAdapter(getBaseContext(), comic_filtered));
        } else {
            Toast.makeText(this, "NO RESULT HAHA.", Toast.LENGTH_SHORT).show();
        }
    }

}
