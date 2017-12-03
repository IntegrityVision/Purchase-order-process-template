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

package ua.com.integrity.process.common;

import java.util.List;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Permissions;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.db.entitymanager.DbEntityManager;
import org.camunda.bpm.engine.impl.persistence.entity.AuthorizationEntity;
import org.camunda.bpm.extension.reactor.CamundaReactor;
import org.camunda.bpm.extension.reactor.bus.CamundaSelector;

@CamundaSelector(type = "userTask", event = TaskListener.EVENTNAME_CREATE)
public class CreateTaskListener implements TaskListener {

    public CreateTaskListener(ProcessEngine processEngine) {
        CamundaReactor.eventBus(processEngine).register(this);
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        String processReaders = (String) delegateTask.getVariable("processReaders");
        if ((processReaders != null) && (!processReaders.isEmpty())) {
            for (String processReader : processReaders.split(",")) {
                setReader(processReader, delegateTask);
            }
        }
    }

    private void setReader(String user, DelegateTask delegateTask) {
        if (!isUserAuthorized(user, delegateTask)) {
            DbEntityManager dbEntityManager = Context.getCommandContext().getSession(DbEntityManager.class);
            AuthorizationEntity newAuthorization = createReadAuthorization(user, delegateTask.getId());
            if (!isAuthExitInDbEntityCache(newAuthorization, dbEntityManager)) {
                dbEntityManager.insert(newAuthorization);
            }
        }
    }

    private boolean isUserAuthorized(String user, DelegateTask delegateTask) {
        return ProcessEngines.getDefaultProcessEngine().getAuthorizationService().createAuthorizationQuery()
                .userIdIn(user).resourceId(delegateTask.getId()).count() > 0;
    }

    private AuthorizationEntity createReadAuthorization(String user, String taskId) {
        AuthorizationEntity authorization = new AuthorizationEntity(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId(user);
        authorization.setResource(Resources.TASK);
        authorization.setResourceId(taskId);
        authorization.addPermission(Permissions.READ);
        return authorization;
    }

    private boolean isAuthExitInDbEntityCache(AuthorizationEntity authorization, DbEntityManager dbEntityManager) {
        List<AuthorizationEntity> authorizationEntities = dbEntityManager.getDbEntityCache().getEntitiesByType(AuthorizationEntity.class);
        for (AuthorizationEntity authorizationEntity : authorizationEntities) {
            if (isAuthorizationPKEqual(authorizationEntity, authorization)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAuthorizationPKEqual(AuthorizationEntity authorization, AuthorizationEntity secondAuthorization) {
        return authorization.getAuthorizationType() == secondAuthorization.getAuthorizationType()
                && (authorization.getResourceType() == secondAuthorization.getResourceType())
                && (authorization.getUserId() != null) && (secondAuthorization.getUserId() != null)
                && (authorization.getUserId().equals(secondAuthorization.getUserId()))
                && (authorization.getResourceId() != null) && (secondAuthorization.getResourceId() != null)
                && (authorization.getResourceId().equals(secondAuthorization.getResourceId()));
    }

}
