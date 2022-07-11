package pollub.ism.lab06;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PozycjaMagazynowaDAO {
    @Insert //Automatyczna kwerenda wystarczy
    public void insert(PozycjaMagazynowa pozycja);

    @Insert //Automatyczna kwerenda wystarczy
    public void insert(HistoriaPozycji historiaPozycji);

    @Update //Automatyczna kwerenda wystarczy
    void update(PozycjaMagazynowa pozycja);

    @Query("SELECT QUANTITY FROM Warzywniak WHERE NAME= :wybraneWarzywoNazwa") //Nasza kwerenda
    int znajdzIloscPoNazwie(String wybraneWarzywoNazwa);

    @Query("UPDATE Warzywniak SET QUANTITY = :wybraneWarzywoNowaIlosc WHERE NAME= :wybraneWarzywoNazwa")
    void aktualizujIloscPoNazwie(String wybraneWarzywoNazwa, int wybraneWarzywoNowaIlosc);

    @Query("SELECT COUNT(*) FROM Warzywniak") //Ile jest rekordow w tabeli
    int size();

    @Query("SELECT * FROM Historia WHERE NAME= :wybraneWarzywoNazwa")
    HistoriaPozycji[] pokazHistoriePoNazwie(String wybraneWarzywoNazwa);

    @Query("SELECT DATEOFLASTCHANGE FROM Warzywniak WHERE NAME= :wybraneWarzywoNazwa")
    String znajdzDatePoNazwie(String wybraneWarzywoNazwa);

    @Query("UPDATE Warzywniak SET DATEOFLASTCHANGE = :wybraneWarzywoNowaDataZmiany WHERE NAME= :wybraneWarzywoNazwa")
    void aktualizujDatePoNazwie(String wybraneWarzywoNowaDataZmiany, String wybraneWarzywoNazwa);
}
