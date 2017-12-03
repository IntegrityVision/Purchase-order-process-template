/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ua.com.integrity.service.orgstructure;

import java.util.Map;
import java.util.Set;

import ua.com.integrity.model.OrgUnit;
import ua.com.integrity.model.Position;
import ua.com.integrity.model.User;

public interface OrgStructure {

    Set<User> getUsers();

    Map<String, String> getUserPass(Set<User> users);

    Set<OrgUnit> getDepartments();

    Set<OrgUnit> getUnits();

    Set<Position> getPositions();

    void update();

    Set<User> getNewUsers(Set<User> currentUsers);

    Set<User> getFiredUsers(Set<User> currentUsers);

}
