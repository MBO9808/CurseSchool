package com.example.curseschool.Helpers;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.curseschool.Adapters.CourseDatesAdapter;
import com.example.curseschool.R;

public class CourseDateHelper extends ItemTouchHelper.SimpleCallback {

    private CourseDatesAdapter courseDatesAdapter;

    public CourseDateHelper(CourseDatesAdapter courseDatesAdapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.courseDatesAdapter = courseDatesAdapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int datePositionId = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.LEFT) {
            AlertDialog.Builder builder = new AlertDialog.Builder(courseDatesAdapter.getContext());
            builder.setTitle("Usuń salę");
            builder.setMessage("Czy na pewno chcesz usunąć termin?");
            builder.setPositiveButton("Tak", (dialogInterface, i) -> {
                courseDatesAdapter.deleteCourseDate(datePositionId);
            });
            builder.setNegativeButton("Nie", (dialogInterface, i) -> {
                courseDatesAdapter.notifyItemChanged(datePositionId);
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            courseDatesAdapter.editCourseDate(datePositionId);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        Drawable icon;
        ColorDrawable background;
        View languageDictionaryView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        if (dX > 0) {
            icon = ContextCompat.getDrawable(courseDatesAdapter.getContext(), R.drawable.ic_edit);
            background = new ColorDrawable(ContextCompat.getColor(courseDatesAdapter.getContext(), R.color.white));
        } else {
            icon = ContextCompat.getDrawable(courseDatesAdapter.getContext(), R.drawable.ic_delete);
            background = new ColorDrawable(Color.RED);
        }

        assert icon != null;
        int iconMargin = (languageDictionaryView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = languageDictionaryView.getTop() + (languageDictionaryView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX > 0) {
            int iconLeft = languageDictionaryView.getLeft() + iconMargin;
            int iconRight = languageDictionaryView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(languageDictionaryView.getLeft(), languageDictionaryView.getTop(),
                    languageDictionaryView.getLeft() + ((int) dX) + backgroundCornerOffset, languageDictionaryView.getBottom());
        } else if (dX < 0) {
            int iconLeft = languageDictionaryView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = languageDictionaryView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(languageDictionaryView.getRight() + ((int) dX) - backgroundCornerOffset,
                    languageDictionaryView.getTop(), languageDictionaryView.getRight(), languageDictionaryView.getBottom());
        } else {
            background.setBounds(0, 0, 0, 0);
        }

        background.draw(c);
        icon.draw(c);
    }
}
