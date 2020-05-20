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
package edu.dfci.cccb.mev.hcl.domain.mock;

import static java.util.Arrays.asList;

import java.util.HashSet;

import edu.dfci.cccb.mev.hcl.domain.contract.Branch;
import edu.dfci.cccb.mev.hcl.domain.contract.Leaf;
import edu.dfci.cccb.mev.hcl.domain.contract.Node;
import edu.dfci.cccb.mev.hcl.domain.contract.NodeBuilder;

/**
 * @author levk
 * 
 */
public class MockNodeBuilder implements NodeBuilder {

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.hcl.domain.contract.NodeBuilder#leaf(java.lang.String) */
  @Override
  public Leaf leaf (String name) {
    return new MockLeaf (name);
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.hcl.domain.contract.NodeBuilder#branch(double,
   * edu.dfci.cccb.mev.hcl.domain.contract.Node[]) */
  @Override
  public Branch branch (double distance, Node... children) {
    return new MockBranch (distance, new HashSet<> (asList (children)));
  }
}
