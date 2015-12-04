
package net.wasnot.android.databasetest.Ollie;

import ollie.Model;
import ollie.annotation.Column;
import ollie.annotation.Table;

/**
 * Created by akihiroaida on 15/03/12.
 */
@Table("notes")
public class Note extends Model {

    @Column("title")
    public String title;

    @Column("body")
    public String body;
}
