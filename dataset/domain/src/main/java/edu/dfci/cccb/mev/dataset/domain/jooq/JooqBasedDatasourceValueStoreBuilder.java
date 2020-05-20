/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.dfci.cccb.mev.dataset.domain.jooq;

import static edu.dfci.cccb.mev.dataset.domain.prototype.AbstractDataSourceValues.COLUMN_FIELD_NAME;
import static edu.dfci.cccb.mev.dataset.domain.prototype.AbstractDataSourceValues.ROW_FIELD_NAME;
import static edu.dfci.cccb.mev.dataset.domain.prototype.AbstractDataSourceValues.VALUE_FIELD_NAME;
import static java.util.UUID.randomUUID;
import static org.jooq.impl.DSL.fieldByName;
import static org.jooq.impl.DSL.tableByName;
import static org.jooq.impl.DSL.using;

import java.sql.SQLException;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.ToString;
import lombok.extern.log4j.Log4j;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;

import edu.dfci.cccb.mev.dataset.domain.contract.ValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.ValueStoreException;
import edu.dfci.cccb.mev.dataset.domain.contract.Values;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractValueStoreBuilder;

/**
 * @author levk
 * 
 */
@ToString (exclude = "context")
@Log4j
public class JooqBasedDatasourceValueStoreBuilder extends AbstractValueStoreBuilder {

  private final DSLContext context;
  private final Table<?> table;
  private final Field<String> row;
  private final Field<String> column;
  private final Field<Double> value;
  private final boolean isTemporary;
  private final String id;
  private final int BATCH_SIZE=1000;
  @Inject
  public JooqBasedDatasourceValueStoreBuilder (@Named("mev-datasource")DataSource dataSource) throws SQLException {
    this(dataSource, randomUUID ().toString (), true);
  }
  
  @Inject
  public JooqBasedDatasourceValueStoreBuilder (@Named("mev-datasource")DataSource dataSource, String id, boolean isTemporary) throws SQLException {    
    this.id = id;
    this.isTemporary = isTemporary;
    context = using (dataSource.getConnection ());
    table = tableByName (id);
    row = fieldByName (String.class, ROW_FIELD_NAME);
    column = fieldByName (String.class, COLUMN_FIELD_NAME);
    value = fieldByName (Double.class, VALUE_FIELD_NAME);          
    context.query ("CREATE "+ (isTemporary ? "TEMPORARY" : "") + " TABLE IF NOT EXISTS {0}({1} VARCHAR(255), {2} VARCHAR(255), {3} DOUBLE)",
                   table,
                   row,
                   column,
                   value).execute ();
    context.query ("CREATE UNIQUE INDEX IF NOT EXISTS {0} ON {0}({1}, {2})",
                   table,
                   row,
                   column).execute ();
    if (log.isDebugEnabled ())
      log.debug ("Created " + table + " with " + row + ", " + column + ", " + value);
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.ValueStoreBuilder#add(double,
   * java.lang.String, java.lang.String) */
  @Override
  public ValueStoreBuilder add (double value, String row, String column) throws ValueStoreException {
    context.insertInto (table).set (this.row, row).set (this.column, column).set (this.value, value).execute ();
    return this;
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.contract.ValueStoreBuilder#build() */
  @Override
  public Values build () {
    return new JooqBasedDataSourceValues (context, table, row, column, value, isTemporary);
  }

  @Override
  public Values build (Map<String,Integer> row, Map<String, Integer> columns) {
    //TODO:fix - this is needed by the FlatFileValueStoreBuilder, 
    //Here, we just do the same as the build() method - need to find a nice way of doing this 
    return build();
  }
}
