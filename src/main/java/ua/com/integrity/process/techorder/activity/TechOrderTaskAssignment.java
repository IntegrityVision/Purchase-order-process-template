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

package ua.com.integrity.process.techorder.activity;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.com.integrity.model.User;
import ua.com.integrity.model.techorder.TechOrder;
import ua.com.integrity.service.orgstructure.OrgStructureHelper;

@Component
public class TechOrderTaskAssignment {

    @Autowired
    private OrgStructureHelper orgStructureHelper;

    public String getDepManager(TechOrder techOrder) {
        String manager = "";
        if (techOrder.getOwner().getDepartmentManager() != null) {
            manager = orgStructureHelper.getUserByName(techOrder.getOwner().getDepartmentManager()).getEmail();
        }
        return manager;
    }

    public List<String> getDeputyGeneralManager() {
        return orgStructureHelper.getDeputyGeneralManager().stream().map(User::getEmail).collect(Collectors.toList());
    }


    public List<String> getITCandidate() {
        return orgStructureHelper.getSysAdmins().stream().map(User::getEmail).collect(Collectors.toList());
    }

    public List<String> getTechCandidate() {
        return orgStructureHelper.getSupportEmployees().stream().map(User::getEmail).collect(Collectors.toList());
    }

    public List<String> getAccountantCandidate() {
        return orgStructureHelper.getAccountants().stream().map(User::getEmail).collect(Collectors.toList());
    }

}
