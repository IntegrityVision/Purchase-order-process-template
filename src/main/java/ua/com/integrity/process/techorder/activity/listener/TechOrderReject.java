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

package ua.com.integrity.process.techorder.activity.listener;

import javax.annotation.Resource;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.com.integrity.config.HistoryMessages;
import ua.com.integrity.model.techorder.TechOrder;
import ua.com.integrity.process.common.ProcessHelper;
import ua.com.integrity.service.orgstructure.OrgStructure;

@Component
public class TechOrderReject implements TaskListener {

    @Resource
    private HistoryMessages historyMessages;

    @Autowired
    private OrgStructure orgStructure;

    @Override
    public void notify(DelegateTask delegateTask) {
        TechOrder techOrder = (TechOrder) delegateTask.getVariable("techorder");
        String approve = (String) delegateTask.getVariable("approve");
        String commentMsg;
        if (approve.equals("no")) {
            commentMsg = String.format(historyMessages.getToStatusDeclined(), techOrder.getDeclineReason());
            this.orgStructure.getUsers().stream()
                    .filter(user -> delegateTask.getAssignee().equals(user.getEmail())).findFirst()
                    .ifPresent(techOrder::setDeclineUser);
        } else {
            commentMsg = historyMessages.getToStatusApproved();
        }
        ProcessHelper.createComment(delegateTask, commentMsg);
    }

}
