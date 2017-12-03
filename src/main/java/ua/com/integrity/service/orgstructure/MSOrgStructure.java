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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ua.com.integrity.model.OrgUnit;
import ua.com.integrity.model.Position;
import ua.com.integrity.model.User;
import ua.com.integrity.service.MSExcel;

@Component
public class MSOrgStructure implements OrgStructure {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yy", Locale.US);

    @Value("${camunda.msExcelPath}")
    private String filePath;
    private MSExcel msExcel;
    private Set<User> users = new HashSet<>();
    private Set<OrgUnit> departments = new HashSet<>();
    private Set<OrgUnit> units = new HashSet<>();
    private Set<Position> positions = new HashSet<>();

    @PostConstruct
    public void init() {
        msExcel = new MSExcel(filePath);
        update();
    }

    @Override
    public void update() {
        this.users = readUsers();
        this.positions = readPositions();
        this.departments = getOrgUnit(1);
        this.units = getOrgUnit(2);
    }

    private Set<User> readUsers() {
        Set<User> users = new HashSet<>();
        List<List<String>> valueRange = msExcel.getData(0);
        valueRange.remove(0);
        for (List<String> row : valueRange) {
            User user = new User();
            if (!row.isEmpty()) {
                if (row.size() >= 13) {
                    if (row.get(3).equals("WORK")) {
                        user.setName(row.get(0));
                        user.setBirthDate(parseDate(row.get(1)));
                        user.setSex(row.get(2));
                        user.setStatus(row.get(3));
                        user.setHireDate(parseDate(row.get(4)));
                        user.setMobile(row.get(5));
                        user.setSkype(row.get(6));
                        user.setEmail(row.get(7));
                        user.setPosition(row.get(8));
                        user.setDepartment(row.get(9));
                        user.setDepartmentManager(row.get(10));
                        user.setUnit(row.get(11));
                        user.setUnitManager(row.get(12));
                        user.setCompany(row.get(13));
                        users.add(user);
                    }
                }
            }
        }
        return users;
    }

    private Set<Position> readPositions() {
        Set<Position> positions = new HashSet<>();
        List<List<String>> valueRange = msExcel.getData(3);
        valueRange.remove(0);
        for (List<String> row : valueRange) {
            Position position = new Position();
            if (!row.isEmpty()) {
                if (row.size() >= 2) {
                    position.setDepartment(row.get(0));
                    position.setJobTitle(row.get(1));
                    positions.add(position);
                }
            }
        }
        return positions;
    }

    @Override
    public Set<User> getNewUsers(Set<User> currentUsers) {
        Set<User> actualUsers = readUsers();
        actualUsers.removeAll(currentUsers);
        return actualUsers;
    }

    @Override
    public Set<User> getFiredUsers(Set<User> currentUsers) {
        Set<User> copyUsers = new HashSet<>(currentUsers);
        Set<User> actualUsers = readUsers();
        copyUsers.removeAll(actualUsers);
        return copyUsers;
    }

    @Override
    public Set<User> getUsers() {
        return this.users;
    }

    public Map<String, String> getUserPass(Set<User> users) {
        Map<String, String> userPass = new HashMap<>();
        List<List<String>> valueRange = msExcel.getData(0);
        valueRange.remove(0);
        for (List<String> row : valueRange) {
            if (!row.isEmpty()) {
                if (row.size() >= 14) {
                    if (row.get(3).equals("WORK")) {
                        userPass.put(row.get(7), row.get(14));
                    }
                }
            }
        }
        Map<String, String> result = new HashMap<>();
        for (User user : users) {
            result.put(user.getEmail(), userPass.get(user.getEmail()));
        }
        return result;
    }

    @Override
    public Set<OrgUnit> getDepartments() {
        return this.departments;
    }

    @Override
    public Set<OrgUnit> getUnits() {
        return this.units;
    }

    private Set<OrgUnit> getOrgUnit(int sheetNumber) {
        Set<OrgUnit> orgUnits = new HashSet<>();
        List<List<String>> valueRange = msExcel.getData(sheetNumber);
        valueRange.remove(0);
        for (List<String> row : valueRange) {
            OrgUnit orgUnit = new OrgUnit();
            if (!row.isEmpty()) {
                if (row.size() >= 2) {
                    orgUnit.setName(row.get(0));
                    orgUnit.setManager(row.get(1));
                    orgUnits.add(orgUnit);
                }
            }
        }
        return orgUnits;
    }

    @Override
    public Set<Position> getPositions() {
        return this.positions;
    }

    private Date parseDate(String value) {
        try {
            return value != null ? DATE_FORMAT.parse(value) : null;
        } catch (ParseException e) {
            return null;
        }
    }

    public MSExcel getMsExcel() {
        return msExcel;
    }

    public void setMsExcel(MSExcel msExcel) {
        this.msExcel = msExcel;
    }

}
