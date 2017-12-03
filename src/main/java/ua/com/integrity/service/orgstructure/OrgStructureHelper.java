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

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import ua.com.integrity.config.PositionsNames;
import ua.com.integrity.model.User;

@Component
public class OrgStructureHelper {

    @Resource(name = "MSOrgStructure")
    private OrgStructure orgStructure;

    @Resource
    private PositionsNames positionsNames;

    public User getUnitManager(String userId) {
        return this.orgStructure.getUsers().stream().filter(user -> user.getEmail()
                .equals(userId))
                .findFirst()
                .flatMap(curUser -> this.orgStructure.getUsers().stream().filter(user -> user.getName().equals(curUser.getUnitManager())).findFirst())
                .orElse(null);
    }

    public User getDepartmentManager(String userId) {
        return this.orgStructure.getUsers().stream().filter(user -> user.getEmail()
                .equals(userId))
                .findFirst()
                .flatMap(curUser -> this.orgStructure.getUsers().stream().filter(user -> user.getName().equals(curUser.getDepartmentManager())).findFirst())
                .orElse(null);
    }

    public User getUser(String userId) {
        return this.orgStructure.getUsers().stream().filter(user -> user.getEmail().equals(userId))
                .findFirst()
                .orElse(null);
    }

    // it's not good method because users could have equal full name
    public User getUserByName(String userName) {
        return this.orgStructure.getUsers().stream().filter(user -> user.getName().equals(userName))
                .findFirst()
                .orElse(null);
    }

    public Set<User> getSupportEmployees() {
        return this.orgStructure.getUsers().stream()
                .filter(user -> positionsNames.getSupportPositions().contains(user.getPosition().toLowerCase()))
                .collect(Collectors.toSet());
    }

    public Set<User> getAccountants() {
        return this.orgStructure.getUsers().stream()
                .filter(user -> positionsNames.getAccountantPositions().contains(user.getPosition().toLowerCase()))
                .collect(Collectors.toSet());
    }

    public Set<User> getSysAdmins() {
        return this.orgStructure.getUsers().stream()
                .filter(user -> positionsNames.getItPositions().contains(user.getPosition().toLowerCase()))
                .collect(Collectors.toSet());
    }

    public Set<User> getOfficeManagers() {
        return this.orgStructure.getUsers().stream()
                .filter(user -> positionsNames.getOmPosition().equals(user.getPosition().toLowerCase()))
                .collect(Collectors.toSet());
    }

    public Set<User> getGeneralManager() {
        return this.orgStructure.getUsers().stream()
                .filter(user -> positionsNames.getGeneralManagerPosition().equals(user.getPosition().toLowerCase()))
                .collect(Collectors.toSet());
    }

    public Set<User> getHR() {
        return this.orgStructure.getUsers().stream()
                .filter(user -> positionsNames.getHrPosition().equals(user.getPosition().toLowerCase()))
                .collect(Collectors.toSet());
    }

    public Set<User> getPMs() {
        return this.orgStructure.getUsers().stream()
                .filter(user -> positionsNames.getPmPosition().equals(user.getPosition().toLowerCase()))
                .collect(Collectors.toSet());
    }

    public Set<User> getDeputyGeneralManager() {
        return this.orgStructure.getUsers().stream()
                .filter(user -> positionsNames.getDeputyGeneralManagerPosition().equals(user.getPosition().toLowerCase()))
                .collect(Collectors.toSet());
    }

    public Set<User> getColleagues(String userId) {
        Set<User> result = new HashSet<>();
        if (userId != null && !userId.isEmpty()) {
            User curUser = getUser(userId);
            result = this.orgStructure.getUsers().stream().filter(colleague -> !colleague.getEmail().equals(curUser.getEmail())
                    && colleague.getUnit() != null
                    && colleague.getUnit().equals(curUser.getUnit()))
                    .collect(Collectors.toSet());
            if (result.size() == 0) {
                result = this.orgStructure.getUsers().stream().filter(colleague -> !colleague.getEmail().equals(curUser.getEmail())
                        && colleague.getDepartment() != null
                        && colleague.getDepartment().equals(curUser.getDepartment()))
                        .collect(Collectors.toSet());
            }
        }
        return result;
    }

}
