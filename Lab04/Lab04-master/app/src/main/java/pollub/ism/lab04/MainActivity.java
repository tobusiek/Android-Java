package pollub.ism.lab04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private boolean player1Turn = true;
    private int zajetychPol = 0;
    private final Button[][] buttons = new Button[3][3];
    private String[][] plansza = new String[3][3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                String buttonId = "button_" + i + "_" + j;
                int resourceId = getResources().getIdentifier(buttonId, "id", getPackageName());
                buttons[i][j] = findViewById(resourceId);
                buttons[i][j].setText("");
                plansza[i][j] = "";
            }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("zajetychPol", zajetychPol);

        outState.putStringArray("plansza1Rzad", plansza[0]);
        outState.putStringArray("plansza2Rzad", plansza[1]);
        outState.putStringArray("plansza3Rzad", plansza[2]);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        zajetychPol = savedInstanceState.getInt("zajetychPol");

        plansza[0] = savedInstanceState.getStringArray("plansza1Rzad");
        plansza[1] = savedInstanceState.getStringArray("plansza2Rzad");
        plansza[2] = savedInstanceState.getStringArray("plansza3Rzad");

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                buttons[i][j].setText(plansza[i][j]);
    }

    private boolean isItWin() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                plansza[i][j] = buttons[i][j].getText().toString();

        for (int i = 0; i < 3; i++)
            if (plansza[i][0].equals(plansza[i][1]) &&
                plansza[i][1].equals(plansza[i][2]) &&
                !plansza[i][0].equals(""))
                return true;

        for (int i = 0; i < 3; i++)
            if (plansza[0][i].equals(plansza[1][i]) &&
                plansza[1][i].equals(plansza[2][i]) &&
                !plansza[0][i].equals(""))
                return true;

        if (plansza[0][0].equals(plansza[1][1]) &&
            plansza[1][1].equals(plansza[2][2]) &&
            !plansza[0][0].equals(""))
            return true;

        if (plansza[0][2].equals(plansza[1][1]) &&
            plansza[1][1].equals(plansza[2][0]) &&
            !plansza[0][0].equals(""))
            return true;

        return false;
    }

    private void itIsDraw() {
        Toast.makeText(this, "Remis", Toast.LENGTH_LONG).show();
        resetBoard();
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                plansza[i][j] = "";
            }

        zajetychPol = 0;
        player1Turn = true;
    }

    public void kliknieciePrzycisku(View view) {
        if (!((Button) view).getText().toString().equals(""))
            return;

        if (player1Turn)
            ((Button) view).setText("O");
        else
            ((Button) view).setText("X");
        zajetychPol++;

        if (isItWin() && zajetychPol >= 5) {
            if (player1Turn)
                Toast.makeText(this, "Wygrały O", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "Wygrały X", Toast.LENGTH_LONG).show();
            resetBoard();
        } else if (zajetychPol == 9)
            itIsDraw();
        else
            player1Turn = !player1Turn;
    }
}
