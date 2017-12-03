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

package ua.com.integrity.api;

import java.util.Set;

import javax.servlet.http.HttpSession;

import org.camunda.bpm.webapp.impl.security.auth.Authentications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ua.com.integrity.model.User;
import ua.com.integrity.service.orgstructure.OrgStructure;
import ua.com.integrity.service.orgstructure.OrgStructureHelper;

@RestController
@RequestMapping("api/ou")
public class OrgUnitRestController {

    @Autowired
    private OrgStructure orgStructure;

    @Autowired
    private OrgStructureHelper orgStructureHelper;

    @RequestMapping(value = "/user", method = RequestMethod.GET, produces = "application/json")
    public Set<User> getUsers() {
        return orgStructure.getUsers();
    }

    //why it used Set, because unit manager can have deputy
    @RequestMapping(value = "/myUnitManager", method = RequestMethod.GET, produces = "application/json")
    public User getMyUnitManager(HttpSession session) {
        return orgStructureHelper.getUnitManager(getCurrentUserId(session));
    }

    @RequestMapping(value = "/myDepManager", method = RequestMethod.GET, produces = "application/json")
    public User getMyDepManager(HttpSession session) {
        return orgStructureHelper.getDepartmentManager(getCurrentUserId(session));
    }

    @RequestMapping(value = "/{userId}/unitManager", method = RequestMethod.GET, produces = "application/json")
    public User getUnitManager(@PathVariable String userId) {
        return orgStructureHelper.getUnitManager(userId);
    }

    @RequestMapping(value = "/{userId}/depManager", method = RequestMethod.GET, produces = "application/json")
    public User getDepManager(@PathVariable String userId) {
        return orgStructureHelper.getDepartmentManager(userId);
    }

    @RequestMapping(value = "/support", method = RequestMethod.GET, produces = "application/json")
    public Set<User> getSupportEmployees() {
        return orgStructureHelper.getSupportEmployees();
    }

    @RequestMapping(value = "/accountant", method = RequestMethod.GET, produces = "application/json")
    public Set<User> getAccountants() {
        return orgStructureHelper.getAccountants();
    }

    @RequestMapping(value = "/sysadmin", method = RequestMethod.GET, produces = "application/json")
    public Set<User> getSysAdmins() {
        return orgStructureHelper.getSysAdmins();
    }

    @RequestMapping(value = "/om", method = RequestMethod.GET, produces = "application/json")
    public Set<User> getOfficeManagers() {
        return orgStructureHelper.getOfficeManagers();
    }

    @RequestMapping(value = "/generalManager", method = RequestMethod.GET, produces = "application/json")
    public Set<User> getGeneralManager() {
        return orgStructureHelper.getGeneralManager();
    }

    @RequestMapping(value = "/hr", method = RequestMethod.GET, produces = "application/json")
    public Set<User> getHR() {
        return orgStructureHelper.getHR();
    }

    @RequestMapping(value = "/pm", method = RequestMethod.GET, produces = "application/json")
    public Set<User> getPMs() {
        return orgStructureHelper.getPMs();
    }

    @RequestMapping(value = "/{userId}/colleagues", method = RequestMethod.GET, produces = "application/json")
    public Set<User> getColleagues(@PathVariable String userId) {
        return orgStructureHelper.getColleagues(userId);
    }

    private String getCurrentUserId(HttpSession session) {
        Authentications authentications = (Authentications) session.getAttribute("authenticatedUser");
        return authentications.getAuthentications().get(0).getIdentityId();
    }

}
