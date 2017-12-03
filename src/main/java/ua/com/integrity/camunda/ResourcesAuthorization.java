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

package ua.com.integrity.camunda;

import static ua.com.integrity.camunda.GroupName.ALL_USERS;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.UUID;

import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.FilterService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Permission;
import org.camunda.bpm.engine.authorization.Permissions;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.filter.Filter;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.impl.persistence.entity.FilterEntity;
import org.camunda.bpm.engine.rest.spi.ProcessEngineProvider;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.com.integrity.model.User;
import ua.com.integrity.service.orgstructure.OrgStructure;

@Component
public class ResourcesAuthorization {

    @Autowired
    private OrgStructure orgStructure;

    public static final String ENGINE_NAME = "default";

    public void initDb() {
        ProcessEngine processEngine = lookupProcessEngine(ENGINE_NAME);
        if (processEngine != null) {
            IdentityService identityService = processEngine.getIdentityService();
            AuthorizationService authorizationService = processEngine.getAuthorizationService();
            // the group must be created at the first start of the application
            if (identityService.createGroupQuery().groupId(ALL_USERS).list().size() == 0) {

                Group group = identityService.newGroup(ALL_USERS);
                group.setName(ALL_USERS);
                identityService.saveGroup(group);

                // set Auth for new group
                addAuthorizationGroup(authorizationService, Resources.APPLICATION, "tasklist", ALL_USERS, new Permission[] {Permissions.ACCESS});
                addAuthorizationGroup(authorizationService, Resources.USER, Authorization.ANY, ALL_USERS, new Permission[] {Permissions.READ});
                addAuthorizationGroup(authorizationService, Resources.PROCESS_DEFINITION, "TechOrder", ALL_USERS, new Permission[] {Permissions.READ,
                        Permissions.CREATE_INSTANCE, Permissions.READ_HISTORY});
                addAuthorizationGroup(authorizationService, Resources.PROCESS_INSTANCE, Authorization.ANY, ALL_USERS, new Permission[] {Permissions.CREATE});
                createDefaultFilter(processEngine);
            }

            // create users
            Set<User> users = orgStructure.getUsers();
            Map<String, String> userPass = orgStructure.getUserPass(users);
            users.stream().filter(user -> !userExist(user.getEmail(), identityService)).forEach(user -> createUser(user, userPass, processEngine));

        } else {
            throw new RuntimeException("Unable to init db");
        }
    }

    public static ProcessEngine lookupProcessEngine(String engineName) {
        ServiceLoader<ProcessEngineProvider> serviceLoader = ServiceLoader.load(ProcessEngineProvider.class);
        Iterator<ProcessEngineProvider> iterator = serviceLoader.iterator();
        if (iterator.hasNext()) {
            ProcessEngineProvider provider = iterator.next();
            return provider.getProcessEngine(engineName);
        }
        return null;
    }

    private boolean userExist(String userId, IdentityService identityService) {
        return identityService.createUserQuery().userId(userId).singleResult() != null;
    }

    private void createDefaultFilter(ProcessEngine processEngine) {
        TaskService taskService = processEngine.getTaskService();
        FilterService filterService = processEngine.getFilterService();

        TaskQuery myTasksQuery = taskService.createTaskQuery().taskAssigneeExpression("${currentUser()}").active();
        FilterEntity myTasksFilter = (FilterEntity) filterService.newTaskFilter("Мої завдання");
        myTasksFilter.setQuery(myTasksQuery);
        myTasksFilter.setPropertiesInternal("{\"refresh\":true}");
        filterService.saveFilter(myTasksFilter);

        TaskQuery myQueueQuery = taskService.createTaskQuery().taskCandidateUserExpression("${currentUser()}").active();
        FilterEntity myQueueFilter = (FilterEntity) filterService.newTaskFilter("В черзі");
        myQueueFilter.setQuery(myQueueQuery);
        myQueueFilter.setPropertiesInternal("{\"refresh\":true}");
        filterService.saveFilter(myQueueFilter);

        addAuthorizationGroup(processEngine.getAuthorizationService(), Resources.FILTER, myTasksFilter.getId(), ALL_USERS, new Permission[] {Permissions.READ});
        addAuthorizationGroup(processEngine.getAuthorizationService(), Resources.FILTER, myQueueFilter.getId(), ALL_USERS, new Permission[] {Permissions.READ});
    }

    private void createViewFilter(ProcessEngine processEngine, String userId) {
        TaskService taskService = processEngine.getTaskService();
        FilterService filterService = processEngine.getFilterService();
        TaskQuery myReadTaskQuery = taskService.createTaskQuery().processVariableValueLike("processReaders", "%" + userId + "%").active();
        FilterEntity myReadTaskFilter = (FilterEntity) filterService.newTaskFilter("На перегляд");
        myReadTaskFilter.setQuery(myReadTaskQuery);
        myReadTaskFilter.setOwner(userId);
        myReadTaskFilter.setPropertiesInternal("{\"refresh\":true}");
        filterService.saveFilter(myReadTaskFilter);
    }

    private void deleteViewFilter(String userId, ProcessEngine processEngine) {
        FilterService filterService = processEngine.getFilterService();
        Filter filter = filterService.createFilterQuery().filterName("На перегляд").filterOwner(userId).singleResult();
        filterService.deleteFilter(filter.getId());
    }

    private void addAuthorizationGroup(AuthorizationService authorizationService,
                                       Resource resource, String resourceId,
                                       String groupId, Permission[] permissions) {
        Authorization newAuthorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        newAuthorization.setGroupId(groupId);
        newAuthorization.setResource(resource);
        newAuthorization.setResourceId(resourceId);
        for (Permission permission : permissions) {
            newAuthorization.addPermission(permission);
        }
        authorizationService.saveAuthorization(newAuthorization);
    }

    public void addAuthorizationUser(AuthorizationService authorizationService,
                                     Resource resource, String resourceId,
                                     String userId, Permission[] permissions) {
        Authorization newAuthorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        newAuthorization.setUserId(userId);
        newAuthorization.setResource(resource);
        newAuthorization.setResourceId(resourceId);
        Arrays.stream(permissions).forEach(newAuthorization::addPermission);
        for (Permission permission : permissions) {
            newAuthorization.addPermission(permission);
        }
        authorizationService.saveAuthorization(newAuthorization);
    }

    private void createUser(User user, Map<String, String> userPass, ProcessEngine processEngine) {
        IdentityService identityService = processEngine.getIdentityService();
        if (!userExist(user.getEmail(), identityService)) {

            String[] fio = user.getName().split(" ");

            org.camunda.bpm.engine.identity.User newUser = identityService.newUser(user.getEmail());

            newUser.setEmail(user.getEmail());
            newUser.setFirstName(fio[1]);
            newUser.setLastName(fio[0]);
            String password = userPass.get(user.getEmail());
            newUser.setPassword(isNotEmpty(password) ? password : UUID.randomUUID().toString());
            identityService.saveUser(newUser);

            identityService.createMembership(user.getEmail(), ALL_USERS);
            createViewFilter(processEngine, user.getEmail());
        }
    }

    private boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    private void deleteUser(User user, ProcessEngine processEngine) {
        deleteViewFilter(user.getEmail(), processEngine);
        IdentityService identityService = processEngine.getIdentityService();
        List<Group> userGroups = identityService.createGroupQuery().groupMember(user.getEmail()).list();
        userGroups.forEach(group -> identityService.deleteMembership(user.getEmail(), group.getId()));
        identityService.deleteUser(user.getEmail());
    }

    public void updateUsers() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        Set<User> currentUsers = this.orgStructure.getUsers();
        Set<User> firedUsers = this.orgStructure.getFiredUsers(currentUsers);
        Set<User> newUsers = this.orgStructure.getNewUsers(currentUsers);
        firedUsers.forEach(user -> deleteUser(user, processEngine));
        Map<String, String> userPass = this.orgStructure.getUserPass(newUsers);
        newUsers.forEach(user -> createUser(user, userPass, processEngine));
        this.orgStructure.update();
    }

}
