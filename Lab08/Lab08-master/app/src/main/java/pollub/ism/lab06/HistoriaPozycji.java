package pollub.ism.lab06;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Historia")
public class HistoriaPozycji {
    @PrimaryKey(autoGenerate = true)
    public int _id;
    public String NAME;
    public int OLDQUANTITY;
    public int NEWQUANTITY;
    public String DATEOFCHANGE;
}