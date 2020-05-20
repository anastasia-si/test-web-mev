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

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.log4j.Log4j;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;

import edu.dfci.cccb.mev.dataset.domain.contract.InvalidCoordinateException;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractDataSourceValues;

/**
 * @author levk
 * 
 */
@ToString (of = "table")
@EqualsAndHashCode (callSuper = true)
@Log4j
public class JooqBasedDataSourceValues extends AbstractDataSourceValues {

  private final DSLContext context;
  private final Table<?> table;
  private final Field<String> row;
  private final Field<String> column;
  private final Field<Double> value;
  private final boolean isTemporary;

  public JooqBasedDataSourceValues (DSLContext context,
                                    Table<?> table,
                                    Field<String> row,
                                    Field<String> column,
                                    Field<Double> value,
                                    boolean isTemporary) {
    this.context = context;
    this.table = table;
    this.row = row;
    this.column = column;
    this.value = value;
    this.isTemporary = isTemporary;
  }

  @Override
  public double get (String row, String column) throws InvalidCoordinateException {
    try {
      return context.select (value)
                    .from (table)
                    .where (this.row.eq (row))
                    .and (this.column.eq (column))
                    .fetchOne ()
                    .getValue (value);
    } catch (RuntimeException e) {
      throw new RuntimeException (" Failed to fetch row " + row + ", column " + column + " from " + table, e);
    }
  }

  @Override
  public void close () throws Exception {
    if(isTemporary){
      if (log.isDebugEnabled ())
        log.debug ("Dropping table " + table);
      context.query ("DROP TABLE IF EXISTS {0}", table);
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#finalize() */
  @Override
  protected void finalize () throws Throwable {
    close ();
  }
}
